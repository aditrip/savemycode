package com.sleepycat.util.prefixmap;

import java.io.IOException;


/**
 * Provides the basic operation for PrefixMap inputs and outputs.
 * 
 * Note that, some of these operations may assume that since inputs and outputs
 * are in sorted order and so the operands may obey an order.
 * 
 * Also, currently these operations are meant for outputs as inputs are
 * byte sequences and only prefix operation is required on an input.
 *
 * @param <T> - Type of Output on which PrefixMap operations will be done.
 */
public abstract class OutputPrefixOperations<T> {
    
    /** commonPrefix("cab", "cabby") will be "cab" 
     *  commonPrefix(70,75) is 70 */
    public abstract T commonPrefix(T e1, T e2);
    

    /** This is suffix operation.
     *  subtract("cabby", "cab") is "by" which is the suffix
     *  substract(75,70) is 5 which is the suffix of 75.
     */
    public abstract T substract(T e1, T e2);

    /** 
     * This is a prepend prefix operation.
     * add("foo", "bar") is "foobar" */
    public abstract T add(T prefix, T e2);
    
    /**
     * Defines null output.
     * Note that this needs to be singleton since operator "==" and "!="
     *  is used on them.
     */
    
    public abstract T getNullOutput();
    public abstract T getNoOutput();
    
    public abstract void write(T output, PrefixMapWriter writer) throws IOException;
    
    public abstract void writeReverse(T output, PrefixMapWriter writer) throws IOException;

    /** Decode an output value previously written with {@link
     *  #write(Object, PrefixMapWriter)}. */
    public abstract T read(PrefixMapReader in) throws IOException;

}
