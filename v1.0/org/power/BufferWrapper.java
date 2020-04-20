package org.power;

import java.io.IOException;
import java.nio.ByteBuffer;

public class BufferWrapper {
	private ByteBuffer buffer;
	private long startByte;
	private long endByte;
	private String name;
	public BufferWrapper(ByteBuffer buffer, String name) {
		super();
		this.buffer = buffer;
		this.name = name;
	}
	
	public ByteBuffer fillCompleteBuffer(FileManager fileManager,long BUFFER_SIZE) throws IOException {
		//System.out.println("Buffer size = "+BUFFER_SIZE);
		ByteBuffer buffer = fileManager.readFromFile(this.startByte, this.startByte+BUFFER_SIZE);
		this.buffer = buffer;
		return buffer;
	}
	
	public ByteBuffer shrinkTolastNewLine(ByteBuffer buffer) {
		int index = -1;
		for(int i = buffer.limit()-1;i>=0;i--) {
			buffer.position(i);
			if((char)buffer.get(i)=='\n') {
				index = i;
				break;
			}
		}
		if(index>-1)
		buffer.limit(index);
		buffer.rewind();
		
		this.buffer = buffer;
		return buffer;
	}
	
	public ByteBuffer shrinkToFirstNewLine(ByteBuffer buffer) {
		int index = -1;
		int limit = buffer.limit();
		for(int i = 0;i<limit;i++) {
			buffer.position(i);
			if((char)buffer.get(i)=='\n') {
				index = i;
				break;
			}
		}
		if(index>-1 && index < limit)
		buffer.position(index);
		//buffer.rewind();
		buffer.limit(limit);
		
		this.buffer = buffer;
		return buffer;
	}
	
	public boolean isEmpty() {
		if(this.startByte<0 || this.endByte<0)
			return true;
		return false;
	}
	
	public String printBuffer() throws IOException {
		if(isEmpty()) {
			System.out.println("Buffer Empty.....");
			return "";
		}
		int position = this.buffer.position();
		int limit = this.buffer.limit();
		//System.out.println(buffer);
		byte[] rawBytes = new byte[buffer.remaining()];
		buffer.get(rawBytes);
		//Get the byte from buffer
		String s = new String(rawBytes);
		SharedData.setData(s.trim());
		//System.out.println(s);
		
		SharedData.setStartByte(this.getStartByte()+"");
		SharedData.setEndByte(this.getEndByte()+"");
		
		buffer.position(position);
		buffer.limit(limit);
		return s;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}
	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	public long getStartByte() {
		return startByte;
	}
	public void setStartByte(long startByte) {
		this.startByte = startByte;
	}
	public long getEndByte() {
		return endByte;
	}
	public void setEndByte(long endByte) {
		this.endByte = endByte;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		return "Start Byte = "+this.startByte+" End Byte = "+this.endByte+" "+this.buffer;
	}
}
