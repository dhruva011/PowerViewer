package org.power;

import java.nio.Buffer;

public class Worker implements Runnable {
	
	public void run() {
		SlidingWindow window = SlidingWindow.getObject();
		try {
			while(true) {
				if(Thread.currentThread().getName().equalsIgnoreCase("UP")) {
					//scrolling up
					synchronized(Lock.getLockUP()) {
						Lock.getLockUP().wait();
						//Use the window temporary buffer to fill the data 
						//Initialize the temp buffer with proper start and end 
						//then add the temporary buffer to left Q
						BufferWrapper first = window.getRightQ().takeFirst();
						long end = first.getStartByte()-1;
						window.getRightQ().putFirst(first);
						
						long start = end-Manager.getBUFFER_SIZE();
						//System.out.println("Start = "+start+" End = "+end);
						if(start<0)
							start = 0;
						
						BufferWrapper tempBuffer = window.getTemporary();
						tempBuffer.setEndByte(end);
						tempBuffer.setStartByte(start);
						
						tempBuffer.fillCompleteBuffer(Manager.getFileManager(), Manager.getBUFFER_SIZE());
						
						if(start>0)
							tempBuffer.shrinkToFirstNewLine(tempBuffer.getBuffer());
						
						if(start==0) {
							tempBuffer.shrinkTolastNewLine(tempBuffer.getBuffer());
							((Buffer)tempBuffer.getBuffer()).limit((int)tempBuffer.getEndByte());
						}
						
						tempBuffer.setStartByte(tempBuffer.getStartByte()+((Buffer)tempBuffer.getBuffer()).position());
						
						window.getLeftQ().putFirst(tempBuffer);
						Lock.getLockUP().notify();
						//System.out.println("Scrolling Up");
						//System.out.println("From Thread - \n" +window);
					}
				}
				else if(Thread.currentThread().getName().equalsIgnoreCase("DOWN")) {
					//scrolling down add new chunk to rightQueue
					synchronized(Lock.getLockDOWN()) {
						Lock.getLockDOWN().wait();
						//Use the window temp  buffer to fill the data
						//Init the temp buffer and add it to end of right Queue
						BufferWrapper tempBuffer = window.getTemporary();
						BufferWrapper first = window.getRightQ().takeFirst();
						tempBuffer.setStartByte(first.getEndByte()+1);
						window.getRightQ().putFirst(first);
						
						tempBuffer.setEndByte(tempBuffer.getStartByte() + Manager.getBUFFER_SIZE());
						
						tempBuffer.fillCompleteBuffer(Manager.getFileManager(), Manager.getBUFFER_SIZE());
						tempBuffer.shrinkTolastNewLine(tempBuffer.getBuffer());
						tempBuffer.setEndByte(tempBuffer.getStartByte() + ((Buffer)tempBuffer.getBuffer()).limit());
						
						window.getRightQ().putLast(tempBuffer);
						//System.out.println("Scrolling Down");
						//System.out.println("From Thread - \n" +window);
						Lock.getLockDOWN().notify();
						
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
