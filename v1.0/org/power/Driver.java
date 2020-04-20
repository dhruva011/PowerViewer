package org.power;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Driver {
	static final int BUFFER_SIZE = 1000;
	public static void main(String[] args) throws IOException, InterruptedException {
		//File file = new File("C:\\Users\\Punit\\Downloads\\prediction-of-asteroid-diameter\\Asteroid.csv");
		//"C:\\Users\\Punit\\Anaconda3\\Lib\\site-packages\\statsmodels\\tsa\\tests\\results";
		File file = new File("C:\\Users\\Punit\\Desktop\\Paper\\file.csv");
		//File file = new File("C:\\Users\\Punit\\Anaconda3\\Lib\\site-packages\\sklearn\\datasets\\data\\wine_data.csv");
		Manager m = new Manager(file);
		Manager.setBUFFER_SIZE(BUFFER_SIZE);
		m.init();
		System.out.println(SharedData.getData());
		Scanner in = new Scanner(System.in);
		int x = 0;
		while(x==0) {
			if(in.nextLine().trim().equals("")) {
				m.scrollDown();
				System.out.println(SharedData.getData());
			}
			else {
				m.scrollUp();
				System.out.println(SharedData.getData());
			}
		}
		in.close();
		
	}
}
