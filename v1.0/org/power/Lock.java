package org.power;

public class Lock {
	private static Lock UP;
	private static Lock DOWN;
	private static int countLockUP = 0;
	private static int countLockDOWN = 0;
	private Lock() {
		super();
	}
	public static Lock getLockUP() {
		if(countLockUP==0) {
			UP = new Lock(); 
			countLockUP++;
		}
		return UP;
	}
	public static Lock getLockDOWN() {
		if(countLockDOWN==0) {
			DOWN = new Lock(); 
			countLockDOWN++;
		}
		return DOWN;
	}
}
