package com.test.pkg1;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class PMemTest {

	public static void main(String[] args) throws Exception {

		int fileSize = 100;
		/*
		 * getconf PAGE_SIZE = 4096 = 2 pow 12. 4K buffer sizes will make too
		 * many - bad idea. 8MB Buffers
		 */
		int nBuffers = 10;
		int offset = 0;

		if (args.length > 0 && (args[0] != null && !args[0].isEmpty()))
			fileSize = Integer.parseInt(args[0]);

		boolean nvm = true;

		if (args.length > 1 && (args[1] != null && !args[1].isEmpty()))
			nvm = Boolean.parseBoolean(args[1]);
		File f = null;
		if (args.length > 2 && (args[2] != null && !args[2].isEmpty()))
			nBuffers = Integer.parseInt(args[2]);

		boolean fsync = false;
		boolean fdatasync = true;

		if (args.length > 3 && (args[3] != null && !args[3].isEmpty()))
			fsync = Boolean.parseBoolean(args[3]);

		if (args.length > 4 && (args[4] != null && !args[4].isEmpty()))
			fdatasync = Boolean.parseBoolean(args[4]);

		System.out.println("args: filesize:" + fileSize + "  nvm:" + nvm + "  nBuffers" + nBuffers + " fsync:" + fsync
				+ " fdatasync:" + fdatasync);

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
		long mappingPutEndTime = 0L;

		MappedByteBuffer[] buffers = new MappedByteBuffer[nBuffers];
		if (nBuffers > 1) {
			byte[] srcData = new byte[bufferSize];
			for (int i = 0; i < srcData.length; i++) {
				srcData[i] = (byte) 70;
			}

			for (int i = 0; i < nBuffers; i++) {
				startTime = System.nanoTime();
				MappedByteBuffer memBuffer = fc.map(FileChannel.MapMode.READ_WRITE, offset, bufferSize);
				mappingEndTime += (System.nanoTime() - startTime);
				startTime = System.nanoTime();
				memBuffer.put(srcData);
				mappingPutEndTime += (System.nanoTime() - startTime);
				buffers[i] = memBuffer;
				offset += bufferSize;

			}

		} else {
			byte[] srcData = new byte[bufferSize];
			for (int i = 0; i < srcData.length; i++) {
				srcData[i] = (byte) 69;
			}
			startTime = System.nanoTime();
			MappedByteBuffer memBuffer = fc.map(FileChannel.MapMode.READ_WRITE, offset, bufferSize);
			mappingEndTime = System.nanoTime() - startTime;
			memBuffer.put(srcData);
			mappingPutEndTime = System.nanoTime() - mappingEndTime - startTime;
			buffers[0] = memBuffer;
			offset += bufferSize;
		}

		/*
		 * If file data is not synced to storage then normal file is faster than
		 * nvdimm file
		 */
		startTime = System.nanoTime();
		if (fdatasync)
			fc.force(false);
		if (fsync)
			raf.getFD().sync();
		long fdataSyncEndTime = System.nanoTime() - startTime;
		raf.close();
		fc.close();
		startTime = System.nanoTime();
		for (int i = 0; i < nBuffers; i++)
			unmap(fc, buffers[i]);
		long endUnMapTime = System.nanoTime() - startTime;

		
		 if (f.exists()) { f.delete(); }
		 

		System.out.println("Mapping Time:" + (mappingEndTime) / 1000 + " micro seconds");
		System.out.println("BufferPut Time:" + (mappingPutEndTime) / 1000 / 1000 + " milli seconds");
		if (fdatasync)
			System.out.println("fdatasync Time:" + (fdataSyncEndTime) / 1000 / 1000 + " milli seconds");
		if (fsync)
			System.out.println("fsync Time:" + (fdataSyncEndTime) / 1000 / 1000 + " milli seconds");

		System.out.println("UnMap Time:" + (endUnMapTime) / 1000 + " micro seconds");

		System.out.println(
				"Total Time:" + (mappingEndTime + mappingPutEndTime + fdataSyncEndTime + endUnMapTime) / 1000 / 1000
						+ " milli seconds");

	}

	private static void unmap(FileChannel fc, MappedByteBuffer bb) throws Exception {
		Class<?> fcClass = fc.getClass();
		java.lang.reflect.Method unmapMethod = fcClass.getDeclaredMethod("unmap",
				new Class[] { java.nio.MappedByteBuffer.class });
		unmapMethod.setAccessible(true);
		unmapMethod.invoke(null, new Object[] { bb });
	}

}
