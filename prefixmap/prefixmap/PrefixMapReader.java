package com.sleepycat.util.prefixmap;

import java.io.IOException;

public abstract class PrefixMapReader {

    /**
     * Reads and returns a single byte.
     * 
     */
    public abstract byte readByte() throws IOException;

    /**
     * Reads a specified number of bytes into an array at the specified offset.
     * 
     * @param b the array to read bytes into
     * @param offset the offset in the array to start storing bytes
     * @param len the number of bytes to read
     * 
     */
    public abstract void readBytes(byte[] b, int offset, int len)
        throws IOException;

    /**
     * Reads an 8 byte long value
     * @return long value
     * @throws IOException
     * 
     * @see PrefixMapWriter#writeLong()
     * 
     */
    public long readLong() throws IOException {
        return (((long) readInt()) << 32) | (readInt() & 0xFFFFFFFFL);
    }

    /**
     * Reads four bytes and returns an int.
     * @see PrefixMapWriter#writeInt()
     */
    public int readInt() throws IOException {
        return ((readByte() & 0xFF) << 24) | ((readByte() & 0xFF) << 16) |
                ((readByte() & 0xFF) << 8) | (readByte() & 0xFF);
    }

    public int readVarInt() throws IOException {
        byte b = readByte();
        int i = b & 0x7F;
        for (int shift = 7; (b & 0x80) != 0; shift += 7) {
            b = readByte();
            i |= (b & 0x7F) << shift;
        }
        return i;
    }
    
    /**
     * Does not support negative values.
     * @return long value read
     * @throws IOException
     * @see {@link PrefixMapWriter#writeVarLong(long)}
     */
    public long readVarLong() throws IOException {

        byte b = readByte();
        long l = b & 0x7F;
        for (int shift = 7; (b & 0x80) != 0; shift += 7) {
          b = readByte();
          l |= (b & 0x7FL) << shift;
        }
        return l;
    }

}
