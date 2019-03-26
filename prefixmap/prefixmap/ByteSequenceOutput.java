package com.sleepycat.util.prefixmap;

import java.io.IOException;

public class ByteSequenceOutput<T> extends OutputPrefixOperations<T> {

    @Override
    public T commonPrefix(T e1, T e2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T substract(T e1, T e2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T add(T prefix, T e2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T getNullOutput() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T getNoOutput() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void write(T output, PrefixMapWriter writer) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeReverse(T output, PrefixMapWriter writer)
        throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public T read(PrefixMapReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}
