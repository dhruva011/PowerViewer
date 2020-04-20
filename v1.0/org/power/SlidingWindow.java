package org.power;

import java.util.concurrent.LinkedBlockingDeque;

//SingleTon  
public class SlidingWindow {
	private static SlidingWindow sld;
	private LinkedBlockingDeque<BufferWrapper> rightQ;
	private LinkedBlockingDeque<BufferWrapper> leftQ;
	private static int count = 0;
	private BufferWrapper temporary;
	private SlidingWindow() {
		rightQ = new LinkedBlockingDeque<BufferWrapper>(2);
		leftQ = new LinkedBlockingDeque<BufferWrapper>(1);
	}
	
	public static SlidingWindow getObject() {
		if(count==0) {
			count++;
			sld = new SlidingWindow();
		}
		return sld;
	}
	public LinkedBlockingDeque<BufferWrapper> getRightQ() {
		return rightQ;
	}

	public void setRightQ(LinkedBlockingDeque<BufferWrapper> rightQ) {
		this.rightQ = rightQ;
	}

	public LinkedBlockingDeque<BufferWrapper> getLeftQ() {
		return leftQ;
	}

	public void setLeftQ(LinkedBlockingDeque<BufferWrapper> leftQ) {
		this.leftQ = leftQ;
	}
	
	
	public BufferWrapper getTemporary() {
		return temporary;
	}

	public void setTemporary(BufferWrapper temporary) {
		this.temporary = temporary;
	}

	public String toString() {
		return "Left Queue = "+this.leftQ+"\nRight Queue = "+this.rightQ;
	}
}
