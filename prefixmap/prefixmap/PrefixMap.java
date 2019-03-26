package com.sleepycat.util.prefixmap;

import java.io.IOException;
import java.util.List;

import com.sleepycat.util.prefixmap.Node.Edge;

/**
 * PrefixMap is based on a Finite State Machine and a Prefix Trie. It follows
 * the FSM so that DFA minimization can be done to make the structure minimal.
 * 
 * A state is represented by a Node. Transition is represented by an edge.
 * 
 * @author adi
 *
 * @param <T>
 */
public class PrefixMap<T> {

    /*
     * byteStore is used to store the byte arrays of the PrefixMap. It is used
     * by the PrexiMapBuilder when nodes are evicted to the PrefixMap. Note
     * that PrefixMap is a read only data structure. Hence this structure
     * should not be used by clients until fully built. This is done by
     * PrefixMapBuilder.finish method.
     * 
     * It is also used when the byte array of the prefix map is more than 1
     * GB..
     * 
     */
    final ByteArrayStore byteStore;
    /*
     * When PrefixMap is read from the persistent form, it is stored in a byte
     * array if it fits in 1G.
     */
    final byte[] byteArray;

    private long rootNode = -1L;

    /* FLAGS FOR Byte Array Encoding of the PrefixMap */
    public static final byte BIT_HAS_OUTPUT = (byte) 1 << 0;

    /*
     * This edge points to a node whose address does not start from the end of
     * this current node.
     */
    public static final byte BIT_HAS_NEXT_ADDRESS = (byte) 1 << 1;

    /*
     * Last edge of the node. All the edges of this node has been searched if
     * the last edge is reached.
     */
    public static final byte BIT_IS_LAST = (byte) 1 << 2;

    /*
     * Current implementation has no input suffix folding. Hence all input
     * lengths are of same size. However, these bits are used by the
     * implementation, so that it is flexible to move to other use cases.
     */

    public static final byte BIT_IS_FINAL = (byte) 1 << 3;

    /*
     * Since the prefix map is a state machine which allows edges from the
     * final state as well. We need to know if the edge points to node which is
     * the leaf node in the prefix tree. A leaf node has no outgoing edges from
     * it.
     */
    public static final byte BIT_LEAF_NODE = (byte) 1 << 4;
    /*
     * Required for ByteSequenceOutput or CharSequenceOutput. Currently not
     * used. TODO: May be there is better way to determine a string output
     * without knowing it's length in advance.
     */
    public static final byte BIT_HAS_OUTPUT_LENGTH = (byte) 1 << 6;

    public static final int MAX_POWER_BITS = 30;

    public final InputType inputType;
    public final OutputPrefixOperations<T> outputs;

    long lastAddedNode = -1L;

    /* Constructor used by PrefixMapBuilder */
    public PrefixMap(InputType inputType,
            OutputPrefixOperations<T> outputs,
            int byteStorePowerBits) {
        this.inputType = inputType;
        this.outputs = outputs;
        byteStore = new ByteArrayStore(byteStorePowerBits);
        /* First byte is 0 - indicates termination for the reader */
        byteStore.writeByte((byte) 0);
        byteArray = null;
    }

    /*
     * A Node is evicted when it is no more required by the PrefixMapBuilder.
     * Node eviction is equivalent to serializing the node in the byte array. A
     * node is evicted in a way that it can be read in reverse by the reader.
     * 
     * Format of writing. write En,En-1,En-2,...E1 En = nth Edge, where n is
     * the number of edges in the node.
     * 
     * Format of writing an Edge. flag byte input if one byte, as it is. else,
     * in reverse var int. output as reverse var long. nextNode address as var
     * long. if nextNode != nextNode in byte array.
     * 
     * TODO: Note that, currently DFA minimization using equivalent states is
     * not done. In order to do that, before adding the Node to prefixMap, it
     * is checked in a Node's HashMap<Node,offsetAddress> if the node already
     * exists, then corresponding offsetAddress is returned and lastAddedNode
     * is not updated A Node's Hash is calculated using all it's edges. For
     * now, this HashMap is removed as it will use lot of memory and may not
     * result in much minimization. It will be useful when prefixMaps's merge
     * with others to form a bigger PrefixMap.
     * 
     * TODO: Current implementation does not take care of output lengths. I do
     * not foresee any use case for a PrefixMap with <String,String> input
     * output pair for now. If output is numeric then the encoding takes care
     * of the output length.
     *
     */
    public long addNode(Node<T> node) throws IOException {
        final long startOffset = byteStore.getPosition();
        long thisNodeOffsetAddress = -1L;
        List<Edge<T>> edges = node.getEdges();
        int numEdges = edges.size();
        //System.out.println("Ading node to prefix map. startOffset="+startOffset+" numEdges:"+numEdges);
        for (int edgeIdx = (numEdges - 1); edgeIdx >= 0; edgeIdx--) {
            Edge<T> edge = edges.get(edgeIdx);
            int flags = 0;
            if (edgeIdx == (numEdges - 1)) {
                flags += BIT_IS_LAST;
            }
            if (edge.isFinal) {
                flags += BIT_IS_FINAL;
            }
            if (edge.nextNode != lastAddedNode) {
                flags += BIT_HAS_NEXT_ADDRESS;
            }

            if (!(edge.nextNode > 0)) {
                flags += BIT_LEAF_NODE;
            }

            if (edge.output != null && edge.output != outputs
                                                             .getNullOutput()) {
                flags += BIT_HAS_OUTPUT;
            }

            /* Write in revese. nextNodeAdress,output,input,flag */
            StringBuilder sb = new StringBuilder();

            if (flag(flags, BIT_HAS_NEXT_ADDRESS)) {
                if (edge.nextNode > 0) {
                    sb.append("  pos:"+byteStore.getPosition() +" nextNode:" + edge.nextNode);
                    byteStore.writeVarLongReverse(edge.nextNode);
                }
            }

            if (flag(flags, BIT_HAS_OUTPUT)) {
                sb.append("  pos:"+byteStore.getPosition() +" output:" + edge.output);
                outputs.writeReverse(edge.output, byteStore);
            }

            if (inputType == InputType.BYTE) {
                sb.append("  pos:"+byteStore.getPosition() +" input:" + edge.input);
                byteStore.writeByte((byte) edge.input);
            }

            sb.append("  pos:"+byteStore.getPosition() +" flags:" + flags);
            byteStore.writeByte((byte) flags);

            thisNodeOffsetAddress = byteStore.getPosition() - 1;
            sb.append("thisNodeOffsetAddress:"+thisNodeOffsetAddress);
            if (thisNodeOffsetAddress > startOffset) {
                lastAddedNode = thisNodeOffsetAddress;
            }
            //System.out.println("WRITING EDGE:" + sb.toString());

        }

        return thisNodeOffsetAddress;

    }

    public static boolean flag(int flags, byte bit) {
        return ((flags & bit) != 0);
    }

    /* Constructor used to deserialize prefixMap from the persistence layer */

    public PrefixMap(PrefixMapReader reader,
            OutputPrefixOperations<T> outputs,
            int maxPowerBits) throws IOException {

        this.outputs = outputs;

        if (maxPowerBits < 1 || maxPowerBits > 30) {
            throw new IllegalArgumentException("maxPowerBits: only 1-30" +
                    " allowed. Got: " + maxPowerBits);
        }

        final byte inType = reader.readByte();
        switch (inType) {
            case 0:
                inputType = InputType.BYTE;
                break;
            case 1:
                inputType = InputType.SHORT;
                break;
            case 2:
                inputType = InputType.INT;
                break;
            default:
                throw new IllegalStateException("Invalid Input Type " +
                        inType);
        }

        rootNode = reader.readVarLong();
        long bytesSize = reader.readVarLong();
        if (bytesSize > 1 << maxPowerBits) {
            // FST is big: we need multiple pages
            byteStore = new ByteArrayStore(reader, bytesSize,
                                           1 << maxPowerBits);
            byteArray = null;
        } else {
            // FST fits into a single block: use ByteArrayBytesStoreReader for
            // less overhead
            byteStore = null;
            byteArray = new byte[(int) bytesSize];
            reader.readBytes(byteArray, 0, byteArray.length);
        }

    }

    void finish(long newRootNode) {
        
        assert newRootNode <= byteStore.getPosition();
        if (rootNode != -1) {
            throw new IllegalStateException("already finished");
        }
        if (newRootNode == -1) {
            newRootNode = 0;
        }
        rootNode = newRootNode;
        System.out.println("PrefixMap finish, last evicted nodeaddress is the newRootNode:" +
                newRootNode);
        byteStore.finish();
    }

    /*
     * Make sure that PrefixMapWriter is wrapped over the RandomAccessFile at
     * write offset to start writing into. This method does not set any offset.
     */
    public void writeToFile(PrefixMapWriter writer) throws IOException {
        if (rootNode == -1) {
            throw new IllegalStateException("PrefixMap's finish method is not called.");
        }

        final byte inpType;
        if (inputType == InputType.BYTE) {
            inpType = 0;
        } else if (inputType == InputType.SHORT) {
            inpType = 1;
        } else {
            inpType = 2;
        }
        writer.writeByte(inpType);
        writer.writeVarLong(rootNode);
        if (byteStore != null) {
            long bytesSize = byteStore.getPosition();
            writer.writeVarLong(bytesSize);
            byteStore.writeTo(writer);
        } else {
            assert byteArray != null;
            writer.writeVarLong(byteArray.length);
            writer.writeBytes(byteArray, 0, byteArray.length);
        }
    }

    /** Read Methods */
    public ByteArrayReader getReader() {
        if (byteStore == null) {
            return new BackwardArrayReader(byteArray);
        }
        return byteStore.getBackwardReader();

    }

    public PrefixMap<T> readFromFile(
                                     PrefixMapReader reader,
                                     OutputPrefixOperations<T> outputs)
        throws IOException {
        return new PrefixMap<T>(reader, outputs, MAX_POWER_BITS);
    }

    public InputType getInputType() {
        return inputType;
    }

    public T get(long input) throws IOException {
        if (rootNode == -1L) {
            throw new IllegalStateException("PrefixMap not yet finished");
        }
        ByteArrayStore inputBytes = 
                new ByteArrayStore(3); // 2 pow 3 = 8. size of long.
        inputBytes.writeLong(input);
        T output = search(inputBytes.getByteArray(), rootNode);
        if(outputs.getNoOutput() == output) {
            return null;
        } 
        return output;
        
    }
    /*
     * search for the given input.
     * If input exists, corresponding output is returned.
     * Else NO_OUTPUT is returned. 
     * 
     */
    public T search(byte[] input, long nodeOffset) throws IOException {

        ByteArrayReader reader = getReader();
        T output = outputs.getNullOutput();
        /* TODO: Only doing for InputType.BYTE for now. */
        int inputByte = 256;

        /*
         * Read nodes. Number of nodes read = input.length .
         * Break when input not found.
         */
        reader.setPosition(nodeOffset);
        for (int inputIdx = 0; inputIdx < input.length; inputIdx++) {
            /*
             * read all edges of the node until input matches or last edge is
             * reached.
             */
            while (true) {
                byte flags = reader.readByte();
                if (inputType == InputType.BYTE) {
                    inputByte = reader.readByte();
                }
                T edgeOutput = outputs.getNullOutput();
                if (flag(flags, BIT_HAS_OUTPUT)) {
                    edgeOutput = outputs.read(reader);
                }
                if (flag(flags, BIT_HAS_NEXT_ADDRESS)) {
                    nodeOffset = reader.readVarLong();
                }
                if (inputByte == input[inputIdx]) {
                    /* edge found. Add output and Move to next node. */
                    /* Add relevant output according to FINAL and NOT LEAF */
                    if (flag(flags, BIT_IS_FINAL) && !flag(flags,
                                                           BIT_LEAF_NODE) &&
                            inputIdx == (input.length - 1)) {

                        /*
                         * TODO: Not possile now. But in future, add
                         * finalOutput of the edge in this block. And return
                         * the output.
                         */

                    } else {
                        output = outputs.add(output, edgeOutput);
                    }

                    /* Move to the next node or return */
                    if (flag(flags, BIT_LEAF_NODE)) {
                        /* Nothing more to search for */
                        if (inputIdx == (input.length - 1)) {
                            return output;
                        }
                        return outputs.getNoOutput();

                    }

                    if (flag(flags, BIT_HAS_NEXT_ADDRESS)) {
                        reader.setPosition(nodeOffset);
                        break;
                    }
                    /*
                     * The reader should already by on the next node. now. Move
                     * to the next node.
                     */
                    break;

                }
                /* Check other edge now, if this was not the last edge.*/
                if (flag(flags, BIT_IS_LAST)) {
                    /*
                     * This is the last edge of the node. 
                     * And input byte was not matched to any edge in the node.
                     */
                    return outputs.getNoOutput();

                }

                /* keep moving. Reader is already on the start of next edge. */

            }
        }
        
        return outputs.getNoOutput();

    }
    
    public long size() {
        long byteArraySize;
        int overheads;
        //Overheads include 8 byte rootNodeAddress.
        overheads = 8;
        
        if(byteArray == null) {
            byteArraySize = byteStore.getPosition();
        } else {
            byteArraySize = byteArray.length;
        }
        return byteArraySize + overheads;
    }

}
