package com.sms.main;

import com.sms.util.Logger;


public class StopMain {
	protected  Logger logger = Logger.getLogger(this.getClass());
	
	public static void main(String[] args) {
		StartMain.main.stop();
	}

}
