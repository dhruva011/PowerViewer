package org.power;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Manager {
	private static FileManager fileManager;
	private Thread leftThread;
	private Thread rightThread;
	private static long BUFFER_SIZE;
	public static File file;
	public Manager(File file) {
		super();
		Manager.file = file;
		fileManager = new FileManager(file);
	}
	
	public void init() throws IOException, InterruptedException {
		SlidingWindow window = SlidingWindow.getObject();
		//Create the three Buffers 
		ByteBuffer buffer = ByteBuffer.allocate((int)BUFFER_SIZE);
		BufferWrapper current = new BufferWrapper(buffer,"");
		current.setStartByte(0);
		current.fillCompleteBuffer(fileManager, BUFFER_SIZE);
		current.shrinkTolastNewLine(current.getBuffer());
		current.setEndByte(current.getBuffer().limit());
		current.printBuffer();
		//System.out.println(current);
		

		buffer = ByteBuffer.allocate((int)BUFFER_SIZE);
		BufferWrapper right = new BufferWrapper(buffer,"");
		right.setStartByte(current.getEndByte()+1);
		right.fillCompleteBuffer(fileManager, BUFFER_SIZE);
		right.shrinkTolastNewLine(right.getBuffer());
		right.setEndByte(right.getStartByte()+right.getBuffer().limit());
		//right.printBuffer();
		//System.out.println(right);
		
		buffer = ByteBuffer.allocate((int)BUFFER_SIZE);
		BufferWrapper rightMost = new BufferWrapper(buffer,"");
		rightMost.setStartByte(right.getEndByte()+1);
		rightMost.fillCompleteBuffer(fileManager, BUFFER_SIZE);
		rightMost.shrinkTolastNewLine(rightMost.getBuffer());
		rightMost.setEndByte(rightMost.getStartByte()+rightMost.getBuffer().limit());
		//left.printBuffer();
		//System.out.println(left);
		
		
		
		window.getRightQ().putFirst(right);
		window.getRightQ().putLast(rightMost);
		window.getLeftQ().put(current);
	//	System.out.println(window);
	//	System.out.println(window.getRightQ());
	//	System.out.println(window.getLeftQ());
		Worker leftObject = new Worker();
		leftThread = new Thread(leftObject);
		leftThread.setName("UP");
		
		Worker rightObject = new Worker();
		rightThread = new Thread(rightObject);
		rightThread.setName("DOWN");
		
		leftThread.start();
		rightThread.start();
		//System.out.println(window);
	}
	
	public void reset() throws Exception {
		fileManager = new FileManager(file);
		
		SlidingWindow window = SlidingWindow.getObject();
		//Create the three Buffers 
		BufferWrapper current = window.getLeftQ().getFirst();
		current.setStartByte(0);
		current.fillCompleteBuffer(fileManager, BUFFER_SIZE);
		current.shrinkTolastNewLine(current.getBuffer());
		current.setEndByte(current.getBuffer().limit());
		current.printBuffer();
		//System.out.println(current);
		

		BufferWrapper right = window.getRightQ().getFirst();
		right.setStartByte(current.getEndByte()+1);
		right.fillCompleteBuffer(fileManager, BUFFER_SIZE);
		right.shrinkTolastNewLine(right.getBuffer());
		right.setEndByte(right.getStartByte()+right.getBuffer().limit());
		//right.printBuffer();
		//System.out.println(right);
		
		BufferWrapper rightMost = window.getRightQ().getLast();
		rightMost.setStartByte(right.getEndByte()+1);
		rightMost.fillCompleteBuffer(fileManager, BUFFER_SIZE);
		rightMost.shrinkTolastNewLine(rightMost.getBuffer());
		rightMost.setEndByte(rightMost.getStartByte()+rightMost.getBuffer().limit());
		//left.printBuffer();
		//System.out.println(left);
	}
	
	public void scrollUp() throws InterruptedException, IOException {
		//System.out.println("Scrollling Up-------------------------------------------------------------------------");
		//Algorithm
		//check the leftQ if the start byte is zero then the projected page is first page no need to scroll up
		synchronized(Lock.getLockUP()) {
			
		SlidingWindow window = SlidingWindow.getObject();
		
		BufferWrapper currentScreen = window.getLeftQ().peekFirst();
		if(currentScreen.getStartByte()==0) {
			//System.out.println("Beginging of Page.");
			//System.out.println(window);
			return ;
		}
		
		//Otherwise move the 1st element from leftQ to rightQ and use last Buffer of RightQ as Temporary
		currentScreen = window.getLeftQ().takeFirst();
		
		window.setTemporary(window.getRightQ().takeLast());
		window.getRightQ().putFirst(currentScreen);
		
		//Now Signal the thread to fill the left queue
		
		Lock.getLockUP().notify();
		
		Lock.getLockUP().wait();
		currentScreen = window.getLeftQ().peekFirst();
		currentScreen.printBuffer();
		
		//Thread.sleep(1000);
		//System.out.println(window);
		}
	}
	
	public void scrollDown() throws InterruptedException, IOException {
		//Scrolling Down
		//System.out.println("Scrolling Down");
		SlidingWindow window = SlidingWindow.getObject();
		
		if(window.getRightQ().isEmpty()) {
			System.out.println(window);
			System.out.println("End of the file.");
			return ;
		}
		
		synchronized(Lock.getLockDOWN()) {
		//Take the element from left q and move it to end of rightQueue
		//take the first element from right Q then process it and then move it to left q 
			BufferWrapper currentScreen = window.getRightQ().takeFirst();
			window.setTemporary(window.getLeftQ().takeFirst());
			currentScreen.printBuffer();
			window.getLeftQ().putFirst(currentScreen);
		
			Lock.getLockDOWN().notify();
			Lock.getLockDOWN().wait();
			//System.out.println("After Scrolling Down Window = "+window);
			//Thread.sleep(1000);
			//System.out.println(window);
		}
	}

	public static long getBUFFER_SIZE() {
		return BUFFER_SIZE;
	}

	public static void setBUFFER_SIZE(long bUFFER_SIZE) {
		BUFFER_SIZE = bUFFER_SIZE;
	}

	public static File getFile() {
		return file;
	}

	public static void setFile(File file) {
		Manager.file = file;
	}

	public static FileManager getFileManager() {
		return fileManager;
	}

	public static void setFileManager(FileManager fileManager) {
		Manager.fileManager = fileManager;
	}
	
	public void shutdownThread() {
		
	}
	
}
