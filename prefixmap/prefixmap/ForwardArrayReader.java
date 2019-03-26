package com.sleepycat.util.prefixmap;

public class ForwardArrayReader extends ByteArrayReader{
    private final byte[] bytes;
    private int pos;

    public ForwardArrayReader(byte[] bytes) {
      this.bytes = bytes;
    }

    @Override
    public byte readByte() {
      return bytes[pos++];
    }

    @Override
    public void readBytes(byte[] b, int offset, int len) {
      System.arraycopy(bytes, pos, b, offset, len);
      pos += len;
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
      return false;
    }

}
