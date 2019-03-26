package com.sleepycat.util.prefixmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Manages a list of byte arrays for PrefixMap.
 * 
 * PrefixMap data structure is encoded in a byte array.
 * 
 * When PrefixMap is getting built, we allocate new byte arrays in a particular
 * chunk size, say equal to one block/page.
 * 
 * ByteStore allocates a new byte array of a given chunkSize, each time one is
 * required. It is possible that chunkSize equal to the page size will give
 * better performance.
 * 
 * A new byte array is allocated each time a page full bytes are written.
 * 
 * 
 * This class is used to read and write bytes to the byte array.
 * Encodes/Decodes Long and Int into a packed format.
 * 
 * This byte[] stores information which is read in reverse by the reader.
 * 
 * Note that Java's ByteBuffer and byte[] can not handle more than 4GB.
 * 
 */
public class ByteArrayStore extends PrefixMapWriter {

    /*
     * Currently this store does not support parallel writes. When it does,
     * this variable needs to be an AtomicLong.
     * 
     */
    public static long CURRENT_RAM_BYTES_USED = 0L;
    
    /*
     * This gives the total bytes used by all the PrefixMap's
     * used in the environment.
     */
    public static long TOTAL_BYTE_ARRAYS_SIZE = 0L;

    private final List<byte[]> byteChunks = new ArrayList<>();

    private byte[] currentBytes;
    private long absPos;
    /* Position in the currentByteArray */
    private int pos;
    private long limit;

    private final int chunkSize;
    /*
     * chunk size will be specified by (2 exp b). Apart from being power of 2,
     * it allows the "modulo chunkSize" operation as a faster bit operation.
     */

    private final int exponentBits;

    public ByteArrayStore(int powerBits) {
        this.exponentBits = powerBits;
        this.chunkSize = 1 << exponentBits;
        pos = chunkSize;
    }

    /**
     * Initialize the ByteArrayStore.
     *
     * @param reader - An input stream on the stored prefixMap.
     * @param nBytes - length of the prefixMap's byte array.
     * @param maxChunkSize - byte array's max size. If the prefixMaps's byte
     *            array is longer than maxChunkSize then more than one
     *            byteArray will be used. For 64 bit JVM, use 30 as
     *            maxChunkSize. And for 32 bit JVM use 28 as maxChunkSize.
     * @throws IOException
     */
    public ByteArrayStore(PrefixMapReader reader,
            long nBytes,
            int maxChunkSize) throws IOException {
        int chunkSz = 2;
        int chunkBits = 1;

        /* Calculate the required chunkSize for the given prefixMap. */
        while (chunkSz < nBytes && chunkSz < maxChunkSize) {
            chunkBits++;
            chunkSz *= 2;
        }

        this.exponentBits = chunkBits;
        this.chunkSize = chunkSz;
        long remainingBytes = nBytes;
        while (remainingBytes > 0) {
            final int chunkLength = (int) Math.min(chunkSz, remainingBytes);
            byte[] chunk = new byte[chunkLength];
            reader.readBytes(chunk, 0, chunk.length);
            byteChunks.add(chunk);
            remainingBytes -= chunkLength;
        }

        pos = byteChunks.get(byteChunks.size() - 1).length;
    }

    public void writeByte(long posAbs, byte b) {
        int chunkIdx = (int) (posAbs >> exponentBits);
        byte[] chunk = byteChunks.get(chunkIdx);
        chunk[(int) (posAbs & (chunkSize - 1))] = b;
    }

    /*
     * Write is always done in a forward manner.
     */
    @Override
    public void writeByte(byte b) {
        assert absPos < limit;
        if (pos == chunkSize) {
            currentBytes = new byte[chunkSize];
            byteChunks.add(currentBytes);
            pos = 0;
        }
        currentBytes[pos++] = b;
        absPos++;
        TOTAL_BYTE_ARRAYS_SIZE++;
    }

    /*
     * A package-private read method which can be used for asserting
     * invariants.
     */
    byte readByte(long posAbs) {
        assert posAbs < this.absPos;
        int chunkIndex = (int) (posAbs >> exponentBits);
        int idx = (int) (posAbs & (chunkSize - 1));
        byte[] chunk = byteChunks.get(chunkIndex);
        return chunk[idx];
    }

    /**
     * Write the given byte array b to currentBytes
     */
    @Override
    public void writeBytes(byte[] b, int offset, int len) {
        if (b == null || currentBytes == null) {
            return;
        }
        while (len > 0) {
            int rem = chunkSize - pos;
            if (len <= rem) {
                System.arraycopy(b, offset, currentBytes, pos, len);
                pos += len;
                break;
            } else {
                if (rem > 0) {
                    System.arraycopy(b, offset, currentBytes, pos, rem);
                    offset += rem;
                    len -= rem;
                }
                currentBytes = new byte[chunkSize];
                byteChunks.add(currentBytes);
                pos = 0;
            }
        }
    }

    int getExponentBits() {
        return exponentBits;
    }

    /*
     * Base 128 Varints encoding. As described in:
     * https://developers.google.com/protocol-buffers/docs/encoding
     * 
     */
    @Override
    public final void writeVarIntReverse(int i) {
        long startAbsPos = absPos;
        while ((i & ~0x7F) != 0) {
            writeByte((byte) ((i & 0x7F) | 0x80));
            i >>>= 7;
        }
        writeByte((byte) i);

        reverseBytes(startAbsPos);
    }

    @Override
    public final void writeVarLongReverse(long l) {
        if (l < 0) {
            throw new IllegalArgumentException("Negative packedLong. given long: " +
                    l + ")");
        }
        //System.out.println("Writing long:"+l+" in reverse at:"+ absPos);
        long startAbsPos = absPos;
        while ((l & ~0x7FL) != 0L) {
            writeByte((byte) ((l & 0x7FL) | 0x80L));
            l >>>= 7;
        }
        writeByte((byte) l);

        reverseBytes(startAbsPos);
    }

    /*
     * This method is only meant for reversing few bytes. Few means less than
     * chunkSize; startAbsPos is inclusive.
     */
    private void reverseBytes(long startAbsPos) {
        assert absPos > startAbsPos;
        long endAbsPos = absPos - 1;

        /*
         * If reversing happens in current byte array.
         */
        byte temp;
        int startRelPos = (int) (startAbsPos & (chunkSize - 1));
        int endRelPos = (int) (absPos & (chunkSize - 1));
        if ((endAbsPos >> exponentBits) == (startAbsPos >> exponentBits)) {
            for (int idx = (endRelPos - 1); idx >= startRelPos; idx--) {
                temp = currentBytes[idx];
                currentBytes[idx] = currentBytes[startRelPos];
                currentBytes[startRelPos++] = temp;
            }
        } // else the range of bytes to be reversed involves two byte arrays.
        else {
            byte[] startChunk = byteChunks.get(byteChunks.size() - 2);
            byte[] endChunk = currentBytes;
            for (int idx = (endRelPos - 1); idx >= startRelPos; idx--) {
                if (idx == -1) {
                    idx = chunkSize - 1;
                    endChunk = startChunk;
                }
                temp = endChunk[idx];
                endChunk[idx] = startChunk[startRelPos];
                startChunk[startRelPos++] = temp;
            }

        }

    }

    public long getPosition() {
        return ((long) byteChunks.size() - 1) * chunkSize + pos;
    }

    /**
     * newLength should be less than current absolute position. This method can
     * not grow the ByteStore
     */
    public void truncate(long newLength) {
        if (newLength > getPosition() || newLength <= 0) {
            throw new IllegalArgumentException(" Truncating ByteStore with" +
                    "invalid arguments: newLength=" + newLength +
                    " getPosition:" + getPosition());
        }
        int chunkIdx = (int) (newLength >> exponentBits);
        pos = (int) (newLength & (chunkSize - 1));
        if (pos == 0) {
            chunkIdx--;
            pos = chunkSize;
        }
        byteChunks.subList(chunkIdx + 1, byteChunks.size()).clear();
        if (newLength == 0) {
            currentBytes = null;
        } else {
            currentBytes = byteChunks.get(chunkIdx);
        }
        assert newLength == getPosition();
    }

    public void finish() {
        if (currentBytes != null) {
            byte[] lastChunk = new byte[pos];
            System.arraycopy(currentBytes, 0, lastChunk, 0, pos);
            byteChunks.set(byteChunks.size() - 1, lastChunk);
            currentBytes = null;
        }
    }

    /** Writes all the bytes to the target {@link PrefixMapWriter}. */
    public void writeTo(PrefixMapWriter out) throws IOException {
        for (byte[] chunk : byteChunks) {
            out.writeBytes(chunk, 0, chunk.length);
        }
    }

    public ByteArrayReader getForwardReader() {
        if (byteChunks.size() <= 1) {
            return new ForwardArrayReader(byteChunks.get(0));
        }
        return new ForwardMultiArrayReader(byteChunks, exponentBits);
    }

    public ByteArrayReader getBackwardReader() {
        if (byteChunks.size() <= 1) {
            return new BackwardArrayReader(byteChunks.get(0));
        }
        return new BackwardMultiArrayReader(byteChunks, exponentBits);
    }

    /*
     * If this byte store fits in 1G, return as single byte[]. Else return
     * null.
     */
    public byte[] getByteArray() {
        if (byteChunks.size() <= 1) {
            return currentBytes;
        }
        long prefixMapSize = byteChunks.size() * chunkSize;
        if (prefixMapSize <= (1 << 30)) {
            byte[] copyBytes = new byte[byteChunks.size() * chunkSize];
            int offset = 0;
            for (int chunkNo = 0; chunkNo < byteChunks.size(); chunkNo++) {
                System.arraycopy(byteChunks.get(chunkNo), 0, copyBytes, offset,
                                 chunkSize);
                offset += chunkSize;
            }
            return copyBytes;
        }
        return null;

    }


}
