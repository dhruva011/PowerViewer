package org.power;

public class SharedData {
	private static String data="";
	private static String startByte = "";
	private static String endByte = "";
	
	public static String getData() {
		return data;
	}

	public static void setData(String data) {
		SharedData.data = data;
	}

	public static String getStartByte() {
		return startByte;
	}

	public static void setStartByte(String startByte) {
		SharedData.startByte = startByte;
	}

	public static String getEndByte() {
		return endByte;
	}

	public static void setEndByte(String endByte) {
		SharedData.endByte = endByte;
	}
	
	
	
}
