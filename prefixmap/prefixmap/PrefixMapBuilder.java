package com.sleepycat.util.prefixmap;

import java.io.IOException;
import java.util.Arrays;

public class PrefixMapBuilder<T> {

    int MAX_INPUT_LENGTH = 8;
    int DEFAULT_PAGE_SIZE_POWER_BITS = 12; /* 4KB Chunks in ByteStore */

    @SuppressWarnings({ "cast", "unchecked" })
    Node<T>[] builderNodes = (Node<T>[]) new Node[MAX_INPUT_LENGTH + 1];

    Node<T> START_NODE = null;

    byte[] currInput = null;
    byte[] prevInput = null;
    T currOutput;
    T prevOutput;

    T NULL_OUTPUT;

    InputType inputType;
    OutputPrefixOperations<T> outputOps;

    PrefixMap<T> prefixMap;

    public PrefixMapBuilder(InputType inputType,
            OutputPrefixOperations<T> outputOps) {
        this.inputType = inputType;
        this.outputOps = outputOps;
        NULL_OUTPUT = outputOps.getNullOutput();
        prefixMap = new PrefixMap<T>(inputType, outputOps,
                                     DEFAULT_PAGE_SIZE_POWER_BITS);
        builderNodes[0] = new Node<T>(this);

    }

    /*
     * The algorithm to build PrefixMap. a) Find the common prefix between
     * currInput and prevInput. b) Evict all nodes from prevInput.length downTo
     * prefixLength.(inclusive) c) Eviction should clear the node. d) update
     * nextNode in the prevNode of the edge. e) Add nodes and edges
     * corresponding to the current suffix. f) Mark the last node added as
     * final. g) Adjust outputs with current output.
     * 
     * It is important to call builder.finish after all input,output pairs are
     * added.
     */
    public void add(byte[] input, T output) throws IOException {
        if (inputType != InputType.BYTE) {
            throw new UnsupportedOperationException("Current version only supports byte inputs");
        }
        currInput = input;
        if (START_NODE == null) {
            builderNodes[0] = new Node<T>(this);
            START_NODE = builderNodes[0];
        }
        int prefixLength = getPrefixLength(prevInput, currInput);
        //System.out.println("prevInput:" + prevInput + "  currInput:" + currInput + "  prefixLength:" + prefixLength);

        /*
         * Note that the below is not possible with current implementation for
         * LSNs only. However, trying to make this more generic and considering
         * other types of inputs like strings.
         */
        if (builderNodes.length < input.length) {
            final Node<T>[] copyOfBuilderNodes = Arrays.copyOf(builderNodes,
                                                               input.length);
            for (int idx =
                builderNodes.length; idx < copyOfBuilderNodes.length; idx++) {
                copyOfBuilderNodes[idx] = new Node<T>(this);
            }
            builderNodes = copyOfBuilderNodes;
        }

        // #2 evict the suffix of prev input with respect to current prefix.
        evictLastSuffix(prefixLength);
        /*
         * Add edges for the current suffix. Mark the node corresponding to
         * currInput.length as Final. Note that it is possible that there are
         * outgoing edges from a Final node. Possible for string inputs. This
         * condition can be detected while adding edges for the current input.
         * Edges are added from prefixLength indexed node. Check if that node
         * was marked final during the last input. If yes, this will be a TODO
         * - the edge leading to the final node can not be final. But it will
         * have a finalOutput - indicating that an input ends here with the
         * finalOutput. For now, a final node can not have any outgoing edges.
         */

        for (int idx = prefixLength + 1; idx <= currInput.length; idx++) {

            if (builderNodes[idx - 1].isFinal()) {
                // TODO
            }

            if (builderNodes[idx] == null) {
                builderNodes[idx] = new Node<T>(this);
            }
            //System.out.println("On Nodeidx:"+(idx-1) +" add edge input:"+currInput[idx-1]);
            builderNodes[idx - 1].addEdge(currInput[idx - 1]);
            if (idx == currInput.length) {
                builderNodes[idx - 1].setLastEdgeFinal();
                builderNodes[idx].setFinal(true);
            }
        }

        /*
         * #3 Adjust outputs. The logic for outputs is as below. Let's say the
         * input is i1,i2,i3,...in and curr output is currOp. And existing
         * outputs on corresponding edges are: o1,o2,o3,...on. Let's call them
         * previous outputs.
         * 
         * Now previous outputs on the edges e1 to prefixLength need to be
         * adjusted.
         * 
         * on first edge, common output prefix of o1 and currOp will remain.
         * 
         * Say, common output prefix on e1 is currOpPrefix1.
         * 
         * And suffix from last output is called prevOpSuffix1.
         * 
         * And suffix from curreOutput is called currOpSuffix1.
         * 
         * So on edge1,
         * 
         * currOpPrefix1 = prefix(currOp, o1)
         * 
         * prevOpSuffix1 = o1 - currOpPrefix1
         * 
         * o1 = currOpPrefix1 // o1 readjusted.
         * 
         * currOpSuffix1 = currOp - prefix1.
         * 
         * (CurrOpSuffix1=Remaining output which needs adjustment).
         * 
         * Prepend prevOpSuffix1 on all the edges of next node.
         * prependSuffix(prevOpSuffix1, nextNode)
         * 
         * currOp = currOpSuffix1;
         * 
         * Do the same on e2.
         * 
         * 
         */
        T currOp = output;
        T currOpPrefix = NULL_OUTPUT;
        T currOpSuffix;
        if (prefixLength > 0) {
            for (int nodeIdx = 0; nodeIdx < prefixLength; nodeIdx++) {
                T Oi = builderNodes[nodeIdx].getLastEdgeOutput();
                /* TODO : For string output, edges with null output will
                 * be involved too.
                 */
                if (Oi != NULL_OUTPUT) {
                    currOpPrefix = outputOps.commonPrefix(currOp, Oi);
                    /* Oi = currOpPrefix. Oi readjusted. */
                    //System.out.println("Oi readjusted. nodeIdx:" + nodeIdx+ " setLastEdgeOutput:" + currOpPrefix);
                    builderNodes[nodeIdx].setLastEdgeOutput(currOpPrefix);

                    T prevOpSuffix = outputOps.substract(Oi, currOpPrefix);

                    /* Prepend prev output suffix on next node */
                    //System.out.println("prepending on nodeIdx:" +(nodeIdx+1)+" prevOpSuffix:" + prevOpSuffix);
                    builderNodes[nodeIdx + 1].prependOutput(prevOpSuffix);
                } else {
                    currOpPrefix = outputOps.getNullOutput();
                }
                /* Remaining output which will be pushed to next edges. */
                currOp = outputOps.substract(currOp, currOpPrefix);

            }
        }

        /*
         * Now set the remaining output on the first input edge after the
         * prefix.
         */
        //System.out.println("Remaining output on nodeIdx:" + prefixLength+ " setLastEdgeOutput:" + currOp);
        builderNodes[prefixLength].setLastEdgeOutput(currOp);
        
        prevInput = currInput;

    }

    /*
     * Convenience method TODO: Can't afford to create a new object for every
     * add. Create a bigger ByteStore and keep adding to it
     */
    public void add(long input, T output) throws IOException {
        ByteArrayStore bytes = new ByteArrayStore(3);
        bytes.writeLong(input);
        add(bytes.getByteArray(), output);

    }

    public PrefixMap<T> finish() throws IOException {

        evictLastSuffix(0);
        long rootAddress = evictNode(builderNodes[0]);
        prefixMap.finish(rootAddress);
        return prefixMap;

    }

    private void evictLastSuffix(int prefixLength) throws IOException {
        if (prevInput == null) {
            return;
        }
        //System.out.println("Evict last suffix from:"+prevInput.length+" to:"+(prefixLength+1));
        for (int idx = prevInput.length; idx > prefixLength; idx--) {
            Node<T> node = builderNodes[idx];
            //System.out.println("****** Evicting nodeIdx:"+idx +" *******");
            builderNodes[idx - 1].setLastEdgeNextNode(evictNode(node));
        }

    }

    /*
     * Serialize this node to the PrefixMap byte[]
     */
    private long evictNode(Node<T> node) throws IOException {
        long nodeOffset = -1;
        if (node.getEdges().size() > 0) {
            nodeOffset = prefixMap.addNode(node);
        }
        node.clear();
        //System.out.println("Evicted node address:" + nodeOffset);
        return nodeOffset;

    }

    public int getPrefixLength(byte[] prevInput, byte[] currInput) {
        if (prevInput == null || prevInput.length == 0 || currInput == null ||
                currInput.length == 0) {
            return 0;
        }

        int idx = 0;
        while (prevInput[idx] == currInput[idx] && idx < prevInput.length &&
                idx < currInput.length) {
            idx++;
        }

        return idx;

    }

}
