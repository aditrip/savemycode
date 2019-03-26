package com.adi.mmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class MmapDirectory {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		final Object rwSync = new Object();
		
		final File f = new File("/home/adi/mmap_dir/0.jdb");
		final byte[] b = {(byte)'A', (byte)'D', (byte)'I', (byte)'T', (byte)'Y', (byte)'A', (byte)' ', (byte)'T', (byte)'R', (byte)'I'};
		FileChannel fc = FileChannel.open(f.toPath(), StandardOpenOption.READ);
		MappedByteBuffer buffer;
		
		new Thread(new Runnable() {
			@Override 
			public void run() {
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(f);
				    for(int i=0; i<20; i++) {
				    fos.write(b, 0, 10);
				    synchronized(rwSync) {
				    	rwSync.wait(20);
				    }
				    }
				    } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException ioe) {
						// TODO Auto-generated catch block
						ioe.printStackTrace();
					} finally {
						try {
							if(fos != null)
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		).start();
		buffer = fc.map(MapMode.READ_ONLY, 0 , fc.size());
		
		System.out.println("INITIAL BUFFER STATE:" + buffer);
		buffer.load();
		synchronized(rwSync) {
		rwSync.notifyAll();
		}
		while(buffer.position() < buffer.limit()) {
			//System.out.println("BUFFER STATE:" + buffer);
			System.out.print((char) buffer.get());
			Thread.sleep(5);
		}
		System.out.println("After notify BUFFER STATE:" + buffer);
		buffer.load();
		
		while(buffer.position() < buffer.limit()) {
			//System.out.println("BUFFER STATE:" + buffer);
			System.out.println((char) buffer.get());
			Thread.sleep(5);
		}
		
		System.out.println(buffer);
		
	}

}
