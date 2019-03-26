package com.sleepycat.util.prefixmap;

import java.util.List;

public class ForwardMultiArrayReader extends ByteArrayReader {
    
    private final List<byte[]> byteChunks;
    private int relPos;          // position in the current array which will be read next.
    private final int chunkSize;
    private final int exponentBits;
    private byte[] current;
    private int chunkIdx;     // current chunkIdx
    
    public ForwardMultiArrayReader(List<byte[]> byteChunks, int powerBits) {
        this.byteChunks = byteChunks;
        this.exponentBits = powerBits;
        this.chunkSize = 1 << exponentBits;
        this.current = byteChunks.size() == 0 ? null :
                                  byteChunks.get(chunkIdx);
      }

    @Override
    public byte readByte() {
      if (relPos == chunkSize) {
        current = byteChunks.get(++chunkIdx);
        relPos = 0;
      }
      return current[relPos++];
    }
    
    
    @Override
    public void readBytes(byte[] b, int offset, int len) {
      while(len > 0) {
        int remainingChunk = chunkSize - relPos;
        if (len <= remainingChunk) {
          System.arraycopy(current, relPos, b, offset, len);
          relPos += len;
          break;
        } else {
          if (remainingChunk > 0) {
            System.arraycopy(current, relPos, b, offset, remainingChunk);
            offset += remainingChunk;
            len -= remainingChunk;
          }
          current = byteChunks.get(++chunkIdx);
          relPos = 0;
        }
      }
    }
    
    /**
     * get the absolute position in the list of arrays.
     */
    @Override
    public long getPosition() {
      return ((long) chunkIdx)*chunkSize + relPos;
    }

    @Override
    public void setPosition(long absPos) {
      chunkIdx = (int) (absPos >> exponentBits);
      current = byteChunks.get(chunkIdx);
      relPos = (int) (absPos & (chunkSize -1));
      assert getPosition() == absPos;
    }

    @Override
    public boolean backward() {
      return false;
    }
    
}
