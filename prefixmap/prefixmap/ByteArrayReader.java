package com.sleepycat.util.prefixmap;

public abstract class ByteArrayReader extends PrefixMapReader {
    
    /** Get current absolute read position. */
    public abstract long getPosition();

    /** Set current absolute read position. */
    public abstract void setPosition(long pos);

    /** Returns true if this reader reads byte array in reverse */
    public abstract boolean backward();

}
