package com.test.pkg1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class PMemCommitSim {

	public static int[] chunkSizes = { 640, 670, 700, 710, 770, 830, 890, 990, 1040 };
	public static byte[][] dataPool = new byte[chunkSizes.length][];
	static {
		int i = 0;
		for (int chunkSize : chunkSizes) {
			byte[] b = new byte[chunkSize];
			for (int j = 0; j < chunkSize; j++) {
				b[j] = (byte) (chunkSize/10);
			}
			dataPool[i++] = b;
		}
	}

	/* args */
	private static int fileSize;
	private static int nBuffers;
	private static boolean nvm;
	private static boolean fsync;
	private static boolean fdatasync;
	private static int commitByteInterval;

	public static void main(String[] args) throws Exception {

		fileSize = 100;
		commitByteInterval = 5 * (1<<20);
		/*
		 * getconf PAGE_SIZE = 4096 = 2 pow 12. 4K buffer sizes will make too
		 * many - bad idea. 8MB Buffers
		 */
		nBuffers = 10;
		int offset = 0;

		if (args.length > 0 && (args[0] != null && !args[0].isEmpty()))
			fileSize = Integer.parseInt(args[0]);

		nvm = false;

		if (args.length > 1 && (args[1] != null && !args[1].isEmpty()))
			nvm = Boolean.parseBoolean(args[1]);
		File f = null;
		if (args.length > 2 && (args[2] != null && !args[2].isEmpty()))
			nBuffers = Integer.parseInt(args[2]);

		fsync = false;
		fdatasync = true;

		if (args.length > 3 && (args[3] != null && !args[3].isEmpty()))
			fsync = Boolean.parseBoolean(args[3]);

		if (args.length > 4 && (args[4] != null && !args[4].isEmpty()))
			fdatasync = Boolean.parseBoolean(args[4]);

		if (args.length > 5 && (args[5] != null && !args[5].isEmpty()))
			commitByteInterval = (Integer.parseInt(args[5])) * (1<<20);

		System.out.println("args: filesize:" + fileSize + "  nvm:" + nvm + "  nBuffers" + nBuffers + " fsync:" + fsync
				+ " fdatasync:" + fdatasync + " commitByteInterval:" + commitByteInterval);

		if (nvm)
			f = new File("/home/adi/pmem/nvmfile1");
		else
			f = new File("/home/adi/mmap_exps/file1");

		if (f.exists()) {
			f.delete();
		}
		f.createNewFile();
		RandomAccessFile raf = new RandomAccessFile(f, "rw");

		FileChannel fc = raf.getChannel();
		fileSize *= (1 << 20);

		int bufferSize = fileSize / nBuffers;

		System.out.println("written file size in bytes:" + fileSize);
		System.out.println("buffer size in bytes:" + bufferSize);

		long startTime = 0L;
		long mappingEndTime = 0L;

		MappedByteBuffer[] buffers = new MappedByteBuffer[nBuffers];

		/* MemMap the file */
		for (int i = 0; i < nBuffers; i++) {
			startTime = System.nanoTime();
			MappedByteBuffer memBuffer = fc.map(FileChannel.MapMode.READ_WRITE, offset, bufferSize);
			mappingEndTime += (System.nanoTime() - startTime);
			buffers[i] = memBuffer;
			offset += bufferSize;
		}

		/*
		 * Put randomly sizes data in buffer and commit
		 */

		long[] times = randomDataCommits(fc, raf, buffers, bufferSize);

		raf.close();
		fc.close();
		startTime = System.nanoTime();
		for (int i = 0; i < nBuffers; i++)
			unmap(fc, buffers[i]);
		long endUnMapTime = System.nanoTime() - startTime;

		/*
		 * if (f.exists()) { f.delete(); }
		 */

		System.out.println("Mapping Time:" + (mappingEndTime) / 1000 + " micro seconds");

		System.out.println("UnMap Time:" + (endUnMapTime) / 1000 + " micro seconds");

		System.out.println(
				"Total Time:" + (mappingEndTime + times[0] + times[1] + endUnMapTime) / 1000 / 1000 + " milli seconds");

	}

	private static void unmap(FileChannel fc, MappedByteBuffer bb) throws Exception {
		Class<?> fcClass = fc.getClass();
		java.lang.reflect.Method unmapMethod = fcClass.getDeclaredMethod("unmap",
				new Class[] { java.nio.MappedByteBuffer.class });
		unmapMethod.setAccessible(true);
		unmapMethod.invoke(null, new Object[] { bb });
	}

	private static long[] randomDataCommits(FileChannel fc, RandomAccessFile raf, MappedByteBuffer[] buffers,
			int bufferSize) throws IOException {
		/*
		 * Write byte, 3byte, 4bytes, 8 bytes, 10 bytes chunk randomly
		 * 
		 */
		int nextLSN = 0;
		commitByteInterval /= nBuffers;

		int lastCommitLSN = 0;
		long startTime = 0;
		long fdataSyncEndTime = 0;
		long bufferPutTime = 0;

		long seed = System.nanoTime();
		Random chunkPicker = new Random(seed);
		while (nextLSN < bufferSize) {
			int dataChunkNo = chunkPicker.nextInt(chunkSizes.length);

			int length = chunkSizes[dataChunkNo];
			if ((nextLSN + length) > bufferSize) {
				nextLSN = bufferSize + 1;
				continue;
			}

			for (MappedByteBuffer buffer : buffers) {
				startTime = System.nanoTime();
				buffer.put(dataPool[dataChunkNo], 0, length);
				bufferPutTime += (System.nanoTime() - startTime);
			}

			nextLSN += length;
			if ((nextLSN - lastCommitLSN) >= commitByteInterval) {
				//System.out.println("Going to commit at nextLSN:" + nextLSN);
				startTime = System.nanoTime();
				if (fdatasync)
					fc.force(false);
				if (fsync)
					raf.getFD().sync();
				fdataSyncEndTime += (System.nanoTime() - startTime);
				lastCommitLSN = nextLSN;
			}

		}

		/* Finsihed writing the complete file. fsync. */
		startTime = System.nanoTime();
		if (fdatasync)
			fc.force(false);
		if (fsync)
			raf.getFD().sync();
		fdataSyncEndTime += (System.nanoTime() - startTime);

		System.out.println("BufferPut Time:" + (bufferPutTime) / 1000 / 1000 + " milli seconds");
		if (fdatasync)
			System.out.println("fdatasync Time:" + (fdataSyncEndTime) / 1000 / 1000 + " milli seconds");
		if (fsync)
			System.out.println("fsync Time:" + (fdataSyncEndTime) / 1000 / 1000 + " milli seconds");

		long[] times = new long[2];
		times[0] = bufferPutTime;
		times[1] = fdataSyncEndTime;
		return times;

	}

}
