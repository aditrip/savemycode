package com.sleepycat.util.prefixmap;

import java.io.IOException;

public abstract class PrefixMapWriter {
    /**
     * Writes a single byte.
     */
    public abstract void writeByte(byte b) throws IOException;

    /**
     * Writes an array of bytes.
     * 
     * @param b the bytes to write
     * @param length the number of bytes to write
     */
    public void writeBytes(byte[] b, int length) throws IOException {
        writeBytes(b, 0, length);
    }

    /**
     * Writes an array of bytes.
     * 
     * @param b the bytes to write
     * @param offset the offset in the byte array
     * @param length the number of bytes to write
     */
    public abstract void writeBytes(byte[] b, int offset, int length)
        throws IOException;

    /**
     * Writes an int as four bytes. 32-bit unsigned integer written as four
     * bytes, high-order bytes first.
     * 
     * @see PrefixMapReader#readInt()
     */
    public void writeInt(int i) throws IOException {
        writeByte((byte) (i >> 24));
        writeByte((byte) (i >> 16));
        writeByte((byte) (i >> 8));
        writeByte((byte) i);
    }

    /**
     * Writes a long as eight bytes.
     * 
     * 64-bit unsigned integer written as eight bytes, high-order bytes first.
     * 
     * @see PrefixMapReader#readLong()
     */
    public void writeLong(long i) throws IOException {
        writeInt((int) (i >> 32));
        writeInt((int) i);
    }

    /**
     * Writes a long in a variable-length format. Writes between one and nine
     * bytes. Smaller values take fewer bytes. Negative numbers are not
     * allowed.
     * 
     * @see PrefixMapWriter#writeVarInt(int)}.
     * @see PrefixMapReader#readVarLong(long)
     */

    public void writeVarLong(long l) throws IOException {
        if (l < 0) {
            throw new IllegalArgumentException("Negative" +
                    " varLong not allowed. Argument: " + l );
        }
        while ((l & ~0x7FL) != 0L) {
            writeByte((byte) ((l & 0x7FL) | 0x80L));
            l >>>= 7;
        }
        writeByte((byte) l);
    }
    
    /*
     * TODO: Remove this.
     * This is dirty. Not every writer will need to implement reverse writes.
     * To achive this, need to implement a better byte reverse method
     * in ByteArrayStore than the current one.
     */
    public abstract void writeVarLongReverse(long l) throws IOException;
    public abstract void writeVarIntReverse(int i) throws IOException;

    /**
     * Writes an int in a variable-length format. Writes between one and five
     * bytes. Smaller values take fewer bytes. Negative numbers are not
     * allowed.
     * <p>
     * The most significant bit of each byte indicates whether more bytes
     * remain to be read. 
     * Values from 0 to 127 may be stored in a single byte,
     * values from 128 to 16,383 are store in two bytes, and so on.
     * </p>
     * 
     * @param i
     * @throws IOException
     * @see {@link PrefixMapReader#readVarInt()}
     */
    public final void writeVarInt(int i) throws IOException {
        if (i < 0) {
            throw new IllegalArgumentException("Negative" +
                    " varInt not allowed. Argument: " + i );
        }
        while ((i & ~0x7F) != 0) {
            writeByte((byte) ((i & 0x7F) | 0x80));
            i >>>= 7;
        }
        writeByte((byte) i);
    }

}
