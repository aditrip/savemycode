package com.test.pkg1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class PMemConcRW {

	public static void main(String[] args) throws Exception {

		int fileSize = 100;
		/*
		 * getconf PAGE_SIZE = 4096 = 2 pow 12. 4K buffer sizes will make too
		 * many - bad idea. 8MB Buffers
		 */
		int nBuffers = 10;

		if (args.length > 0 && (args[0] != null && !args[0].isEmpty()))
			fileSize = Integer.parseInt(args[0]);

		boolean nvm = false;

		if (args.length > 1 && (args[1] != null && !args[1].isEmpty()))
			nvm = Boolean.parseBoolean(args[1]);
		File f = null;
		if (args.length > 2 && (args[2] != null && !args[2].isEmpty()))
			nBuffers = Integer.parseInt(args[2]);
		
		System.out.println("args:" + fileSize +"  " + nvm + "  " + nBuffers);

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

		System.out.println("file size in bytes:" + fileSize);

		Thread writeThread1 = new Thread(new CWriteTask(nBuffers, bufferSize, fc, false));
		Thread writeThread2 = new Thread(new CWriteTask(nBuffers, bufferSize, fc, false));
		long startTime = System.nanoTime();
		writeThread1.start();
		writeThread2.start();
		Thread readThread1 = new Thread(new CReadTask(nBuffers, bufferSize, fc, false));
		Thread readThread2 = new Thread(new CReadTask(nBuffers, bufferSize, fc, false));
		readThread1.start();
		readThread2.start();
		writeThread1.join();
		writeThread2.join();
		readThread1.join();
		readThread2.join();
		long endTime = System.nanoTime();
		
		System.out.println("TOTAL TIME FOR WRITE FOLLOWED BY READ:" + (endTime - startTime)/1000/1000 + "milli seconds");

		raf.close();
		fc.close();
		

		if (f.exists()) {
			f.delete();
		}

	}

	static void unmap(FileChannel fc, MappedByteBuffer bb) throws Exception {
		Class<?> fcClass = fc.getClass();
		java.lang.reflect.Method unmapMethod = fcClass.getDeclaredMethod("unmap",
				new Class[] { java.nio.MappedByteBuffer.class });
		unmapMethod.setAccessible(true);
		unmapMethod.invoke(null, new Object[] { bb });
	}

}

class CWriteTask implements Runnable {

	private int nBuffers;
	private int bufferSize;
	private FileChannel fc;
	private boolean unmap = false;

	public CWriteTask(int nBuffers, int bufferSize, FileChannel fc, boolean unmap) {
		this.nBuffers = nBuffers;
		this.bufferSize = bufferSize;
		this.fc = fc;
		this.unmap = unmap;
	}

	@Override
	public void run() {
		long startTime = 0L;
		long mappingEndTime = 0L;
		long mappingPutEndTime = 0L;
		int offset = 0;

		MappedByteBuffer[] buffers = new MappedByteBuffer[nBuffers];
		if (nBuffers > 1) {
			byte[] srcData = new byte[bufferSize];
			for (int i = 0; i < srcData.length; i++) {
				srcData[i] = (byte) 70;
			}

			for (int i = 0; i < nBuffers; i++) {
				startTime = System.nanoTime();
				MappedByteBuffer memBuffer = null;
				try {
					memBuffer = fc.map(FileChannel.MapMode.READ_WRITE, offset, bufferSize);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
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
			MappedByteBuffer memBuffer = null;
			try {
				memBuffer = fc.map(FileChannel.MapMode.READ_WRITE, offset, bufferSize);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			mappingEndTime = System.nanoTime() - startTime;
			memBuffer.put(srcData);
			mappingPutEndTime = System.nanoTime() - mappingEndTime - startTime;
			buffers[0] = memBuffer;
		}

		/*
		 * If file data is not synced to storage then normal file is faster than
		 * nvdimm file
		 */
		try {
			startTime = System.nanoTime();
			fc.force(false);
		} catch (IOException e) { e.printStackTrace();}
		finally {
			
		}
		
		long fdataSyncEndTime = System.nanoTime() - startTime;
		long endUnMapTime = 0L;
		if (unmap) {
			startTime = System.nanoTime();
			for (int i = 0; i < nBuffers; i++)
				try {
					PMemWriteAndRead.unmap(fc, buffers[i]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			endUnMapTime = System.nanoTime() - startTime;
		}
		
		System.out.println("\n  ---- WRITE TIMES ---------");
		System.out.println("WRITE  :Mapping Time:" + (mappingEndTime) / 1000 + " micro seconds");
		System.out.println("WRITE  :BufferPut Time:" + (mappingPutEndTime) / 1000 / 1000 + " milli seconds");
		System.out.println("WRITE  :fdatasync Time:" + (fdataSyncEndTime) / 1000 / 1000 + " milli seconds");

		System.out.println("UnMap Time:" + (endUnMapTime) / 1000 + " micro seconds");

		System.out.println(
				"WRITE   :Total Time:" + (mappingEndTime + mappingPutEndTime + fdataSyncEndTime + endUnMapTime) / 1000 / 1000
						+ " milli seconds");

	}

}

class CReadTask implements Runnable {

	private int nBuffers;
	private int bufferSize;
	private FileChannel fc;
	private boolean unmap = false;

	public CReadTask(int nBuffers, int bufferSize, FileChannel fc, boolean unmap) {
		this.nBuffers = nBuffers;
		this.bufferSize = bufferSize;
		this.fc = fc;
		this.unmap = unmap;
	}

	@Override
	public void run() {
		long startTime = 0L;
		long mappingEndTime = 0L;
		long mappingGETEndTime = 0L;
		int offset = 0;

		MappedByteBuffer[] buffers = new MappedByteBuffer[nBuffers];

		byte[] destData = new byte[bufferSize];

		for (int i = 0; i < nBuffers; i++) {
			startTime = System.nanoTime();
			try {
				buffers[i] = fc.map(FileChannel.MapMode.READ_ONLY, offset, bufferSize);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			mappingEndTime += (System.nanoTime() - startTime);
			
			startTime = System.nanoTime();
			buffers[i].get(destData);
			if (destData[90] != (byte) 70) {
				System.out.println("MappedBuffer NOT READ" + buffers[i]);
				i--;
				continue;
				
			}
			mappingGETEndTime += (System.nanoTime() - startTime);
			offset += bufferSize;

		}

		/*
		 * If file data is not synced to storage then normal file is faster than
		 * nvdimm file
		 */
		try {
			startTime = System.nanoTime();
			//fc.force(false);
		} //catch (IOException e) {e.printStackTrace();}
		finally {
			
		}
		long fdataSyncEndTime = System.nanoTime() - startTime;
		long endUnMapTime = 0L;
		if (unmap) {
			startTime = System.nanoTime();
			for (int i = 0; i < nBuffers; i++)
				try {
					PMemWriteAndRead.unmap(fc, buffers[i]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			endUnMapTime = System.nanoTime() - startTime;
		}
		System.out.println("\n\n  ---- READ TIMES ---------");
		System.out.println("READ   :Mapping Time:" + (mappingEndTime) / 1000 + " micro seconds");
		System.out.println("READ   :BufferGET Time:" + (mappingGETEndTime) / 1000  + " micro seconds");
		System.out.println("READ   :fdatasync Time:" + (fdataSyncEndTime) / 1000 + " micro seconds");

		System.out.println("READ MODE: UnMap Time:" + (endUnMapTime) / 1000  + " micro seconds");

		System.out.println(
				"READE Total Time:" + (mappingEndTime + mappingGETEndTime + fdataSyncEndTime + endUnMapTime) / 1000 / 1000
						+ " milli seconds");

	}
}
