package com.sleepycat.util.prefixmap;

import java.util.List;

/**
 * Note that for the Backward reader, the caller should set the absolute
 * position before the reader can be used.
 * 
 * Without setting position it will cause ArrayIndexOutOfBoundsException.
 * 
 * By default the position is set as the size of byte[] in the last chunk. This
 * may or may not work depending on how writes are done.
 * 
 *
 */

public class BackwardMultiArrayReader extends ByteArrayReader {

    private final List<byte[]> byteChunks;
    private int relPos; // position in the current array which will be read
                        // next.
    private final int chunkSize;
    private final int exponentBits;
    private byte[] current;
    private int chunkIdx = -1; // current chunkIdx. Need to be set by
                               // setPosition call.

    public BackwardMultiArrayReader(List<byte[]> byteChunks, int powerBits) {
        this.byteChunks = byteChunks;
        this.exponentBits = powerBits;
        this.chunkSize = 1 << exponentBits;
    }

    @Override
    public long getPosition() {
        return ((long) chunkIdx) * chunkSize + relPos;
    }

    @Override
    public void setPosition(long absPos) {
        chunkIdx = (int) (absPos >> exponentBits);
        current = byteChunks.get(chunkIdx);
        relPos = (int) (absPos & (chunkSize - 1));
        assert getPosition() == absPos : "absPos=" + absPos + " getPos()=" +
                getPosition();
    }

    @Override
    public boolean backward() {
        return true;
    }

    @Override
    public byte readByte() {
        if (relPos == -1) {
            current = byteChunks.get(--chunkIdx);
            relPos = chunkSize - 1;
        }
        return current[relPos--];
    }


    @Override
    public void readBytes(byte[] b, int offset, int len) {
        for (int i = 0; i < len; i++) {
            b[offset + i] = readByte();
        }
    }

}
