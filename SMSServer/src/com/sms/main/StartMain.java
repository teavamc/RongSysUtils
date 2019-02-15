package com.sms.main;

import com.rxtx.util.Port;
import com.sms.util.Const;
import com.sms.util.Logger;
import com.sms.util.Tools;


public class StartMain {
	private SMSThread sendThread;
	private Port myport;
	public static int threadcCyc = 3000;//线程轮询时间间隔
	public static SenderData mydata ;
	public static StartMain main;
	protected  Logger logger = Logger.getLogger(this.getClass());
	
	public static void main(String[] args) {
		mydata = new SenderData();
		main = new StartMain();
		main.start();
	}
	public StartMain(){
		myport=new Port(Tools.GetValueByKey(Const.CONFIG, "comPort"));
	}
	public void start(){
		if (sendThread == null) {
			sendThread=new SMSThread(mydata,myport);
			sendThread.start();
			logger.info("正常日志   信息:启动短信服务" );
		}
	}
	public void stop(){
		if(myport!=null){
			myport.close();
		}
		if(null!=sendThread && !sendThread.isInterrupted()) {
			sendThread.interrupt();
		}
		logger.info("正常日志   信息:关闭短信服务" );
	}

}
