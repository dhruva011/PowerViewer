package org.power;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileManager {
	private RandomAccessFile raf;
	private FileChannel channel;
	
	//Read the data from file
	ByteBuffer readFromFile(long start,long end) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate((int)(end-start+1));
		raf.seek(start);
		channel.read(buffer);
		((Buffer)buffer).flip();
		return buffer;
	}
	
	FileManager(File file){
		try {
			this.raf = new RandomAccessFile(file,"r");
			channel = raf.getChannel();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	void destroy() throws Exception{
		channel.close();
		raf.close();
	}
	
	public void finalize() throws Exception{
		destroy();
	}
}
