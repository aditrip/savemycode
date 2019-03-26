package com.sleepycat.util.prefixmap;

import java.util.ArrayList;
import java.util.List;

/*
 * Node represents a State of the PrefixMap. It is used by the
 * PrefixMapBuilder. PrefixMap contains only the state transitions represented
 * by edges. Nodes or states are only the addresses in the byte array. They do
 * not consume any space, except while building the prefix map.
 * 
 * A final state in a FSM is marked in the edges pointing to this node.
 * 
 */
public class Node<T> {

    private T lastEdgeOutput;
    // TODO: Convert this into array of edges.
    private List<Edge<T>> edges = new ArrayList<Edge<T>>();

    private boolean isFinal;

    final private PrefixMapBuilder<T> builder;

    public Node(PrefixMapBuilder<T> builder) {
        this.builder = builder;
        lastEdgeOutput = builder.outputOps.getNullOutput();
    }

    /*
     * An edge represents a state transition. Given an input, the state is
     * transitioned from 'this' node to 'nextNode'. Note that an edge should
     * have nextNode. Although in PrefixMapBuilder, the edge points to a
     * builder node. What we need is the address of the next node in the
     * PrefixMap byte[]. However, that is not available at the time of adding
     * the edge. It is set when the nodes are evicted to the PrefixMap byte
     * array. Also, the boolean isFinal on the edge is set at the time of
     * eviction.
     * 
     * @see setLastEdgeNextNode
     * 
     * @see markEdgesFinal
     */
    public void addEdge(byte input) {
        Edge<T> edge = new Edge<T>();
        edge.input = input;
        edges.add(edge);
    }

    public T getLastEdgeOutput() {
        return lastEdgeOutput;
    }

    public void setLastEdgeOutput(T lastEdgeOutput) {
        edges.get(edges.size() - 1).output = lastEdgeOutput;
        this.lastEdgeOutput = lastEdgeOutput;
    }

    public void setLastEdgeFinal() {
        edges.get(edges.size() - 1).isFinal = true;

    }

    public void markEdgesFinal() {
        for (Edge<T> e : edges) {
            e.isFinal = true;
        }
    }

    /*
     * Note that the way nodes are evicted, setting nextNode on last edge would
     * end up setting nextNode on all edges.
     * 
     * Also while serializing these edges to the byte array, if the byte array
     * writer's position is at the same address as nextNode, there is no need
     * of writing the nextNode in the byte array.
     */
    public void setLastEdgeNextNode(long nextNode) {
        edges.get(edges.size() - 1).nextNode = nextNode;
    }

    public List<Edge<T>> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge<T>> edges) {
        this.edges = edges;
    }

    public void clear() {
        edges = new ArrayList<Edge<T>>();
        setFinal(false);
    }

    /*
     * An edge represents a state transition. Given an input, the state is
     * transitioned from 'this' node to 'nextNode'.
     */

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    /* This is used by the builder to prepend the suffix
    from last output. */
    public void prependOutput(T outputPrefix) {

        for (int edgeIdx = 0; edgeIdx < edges.size(); edgeIdx++) {
            T edgeOutput = edges.get(edgeIdx).output;
            edges.get(edgeIdx).output = builder.outputOps.add(outputPrefix,
                                                              edgeOutput);

        }

    }

    public static class Edge<T> {
        public int input;
        public T output;
        /*
         * PrefixMap is a byte[]. This boolean will be serialized into a flag
         * variable. And will be used to determine end of search for an input.
         */
        public boolean isFinal;
        /*
         * This is the offset in the PrefixMap byte array of the target node.
         * This is used to traverse the input to get the output. This may be
         * called as the nextNodeAddress.
         */
        public long nextNode;

    }
}
