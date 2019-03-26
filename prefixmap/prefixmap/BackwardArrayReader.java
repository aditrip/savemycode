package com.sleepycat.util.prefixmap;

public class BackwardArrayReader extends ByteArrayReader {
    private final byte[] bytes;
    private int pos;

    public BackwardArrayReader(byte[] bytes) {
      this.bytes = bytes;
    }

    @Override
    public byte readByte() {
      return bytes[pos--];
    }

    @Override
    public void readBytes(byte[] b, int offset, int len) {
      for(int i=0;i<len;i++) {
        b[offset+i] = bytes[pos--];
      }
    }


    @Override
    public long getPosition() {
      return pos;
    }

    @Override
    public void setPosition(long pos) {
      this.pos = (int) pos;
    }

    @Override
    public boolean backward() {
      return true;
    }

}
