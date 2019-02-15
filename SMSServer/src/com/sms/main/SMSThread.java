package com.sms.main;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.rxtx.util.CommonSms;
import com.rxtx.util.Port;
import com.rxtx.util.SendUtil;
import com.rxtx.util.StringUtil;
import com.sms.entity.Receive;
import com.sms.entity.Send;
import com.sms.util.Logger;
import com.sms.util.Tools;

public class SMSThread extends Thread{
	@Resource(name="messageService")
	protected  Logger logger = Logger.getLogger(this.getClass());
	protected SenderData mydata ;
	private Port myport;
	private static char symbol1 = 13;
	private static String strReturn = "", atCommand = "";
	public SMSThread(SenderData mydata,Port myport){
		this.mydata = mydata;
		this.myport = myport;
	}
	public void run()
	{
		while(!this.isInterrupted())
		{
			//读取短信
			getSmsList();
			//发送短信
			getAndSendMessage(1);//1:应急广播系统,2:信息发布系统,3:山洪预警系统
			getAndSendMessage(2);
			getAndSendMessage(3);
			try {  
				Thread.sleep(3000); //每隔3s执行一次  
			} catch (InterruptedException e) {  
				logger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:短信线程错误" );
			} 
		}
	}
	/**
	 * 发送短信
	 * @param type 1://应急广播系统,2://信息发布系统,3://山洪预警系统
	 */
	private void getAndSendMessage(int type){
		List<Send> meslist = null;
		String sysText = "";
		switch(type){
		case 1://应急广播系统
			meslist = mydata.getBroadUnSendMessage();
			sysText="应急广播系统";
			break;
		case 2://信息发布系统
			meslist = mydata.getInfoUnSendMessage();
			sysText="信息发布系统";
			break;
		case 3://山洪预警系统
			meslist = mydata.getIOTUnSendMessage();
			sysText="山洪预警系统";
			break;
		}
		try {
			if(meslist!=null&&meslist.size()>0){
				setMessageMode(myport,0);
				for(Send pd :meslist){//发送短信
					String phone = pd.getSmobile();
					String content = pd.getScontent();
					if(!phone.equals("")&&!content.equals("")){
						logger.info("正常日志  信息: "+phone+" 短信发送 ");
						Boolean flag = sendMessage(phone, content);
						pd.setIssend(flag);   //设置短信发送状态
						pd.setSendtimes(pd.getSendtimes()+1); //设置短信发送次数
						switch(type){
						case 1://应急广播系统
							mydata.setBroadMessageSend(pd);
							break;
						case 2://信息发布系统
							mydata.setInfoMessageSend(pd);
							break;
						case 3://山洪预警系统
							mydata.setIOTMessageSend(pd);
							break;
						}
						Thread.sleep(3000); //每隔2s执行一次  
					}
				}
			}
		} catch (Exception e1) {
			logger.error("出错日志  记录:" + e1.getMessage().toString() + "  信息:读取"+sysText+"数据库待发送短信进行发送出错" );
		}
	}
	private boolean sendMessage(String mobile, String content) {
		if(!myport.isIsused()) {	
			logger.error("出错日志    信息:COM通讯端口未正常打开!" );
			return false;
		}
		char symbol2 = 34;// 空格
		char symbol3 = 26;// ctrl~z 发送指令
		try {
			String[] txt = SendUtil.PDUEncoder(mobile, content.trim()).split(",");
			atCommand = "AT+CMGS=" + txt[0] + String.valueOf(symbol1);
//			atCommand = "AT+CMGS=" + txt[0] + "\r" + txt[1] + String.valueOf(26);
			strReturn = myport.sendAT(atCommand);
			strReturn = myport.sendAT(txt[1]+ String.valueOf(symbol3) + String.valueOf(symbol1));
			if (strReturn.indexOf("OK") != -1
					&& strReturn.indexOf("+CMGS") != -1) {
				System.out.println("短信发送成功...");
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();			
			logger.error("出错日志  记录:" + ex.getMessage().toString() + "  信息:发送短信到"+mobile+"出错" );
			return false;
		}	
		logger.error("出错日志  信息:发送短信到"+mobile+"失败" );
		return false;

	}
	/**
	 * 读取所有短信
	 */
	private void getSmsList(){
		setMessageMode(myport,1);
		List<CommonSms> recvlist = RecvSmsList(myport);
 		  if(recvlist!=null){
 			  for(CommonSms sms:recvlist){
 				  System.out.println("发送人："+sms.getSender()+"  时间："+sms.getDate()+"  状态："+sms.getState());
 				  System.out.println("内容："+sms.getSmstext());
 				logger.info("正常日志  信息:  接收  "+sms.getSender()+ " 的短信");
 				Receive receive = new Receive();
 				receive.setRmobile(sms.getSender());
 				receive.setRcontent(sms.getSmstext());
 				receive.setRecivedtime(Tools.date2Str(sms.getDate()));
 				receive.setIsread(false);
 				mydata.addReceiveMessage(receive);
 			  }
 		  }
	}
	/**
	* 读取所有短信 
	* @return CommonSms集合
	*/
	private List<CommonSms> RecvSmsList(Port myport) {
		if(!myport.isIsused()) {
			logger.error("出错日志    信息:COM通讯端口未正常打开!" );	
			return null;
		}
		List<CommonSms> listMes = new ArrayList<CommonSms>();
		try {
//			atCommand = "AT+CMGL=0";
			atCommand = "AT+CMGL=\"REC UNREAD\"";
//			atCommand = "AT+CMGL=\"ALL\"";
//			AT+CMGL="REC UNREAD"代表显示未读短信清单
//			           AT+CMGL= "REC READ"代表显示已读短信清单
//			           AT+CMGL= "STO SENT"代表显示已发送的存储短信清单
//			           AT+CMGL= "STO UNSENT"代表显示未发送的存储短信清单
//			           AT+CMGL= "ALL"代表显示所有短信清单
			strReturn = myport.sendAT(atCommand);
			if(strReturn.contains("ERROR")) logger.error("出错日志    信息:读取短信出错!" );
			listMes = StringUtil.analyseArraySMS(strReturn);
		} catch (Exception ex) {
			logger.error("出错日志  记录:" + ex.getMessage().toString() + "  信息:接收短信错误" );
		}
		return listMes;
	}
	/**
	 * 设置消息模式 
	 * @param op
	 * 0-pdu 1-text(默认1 文本方式 )
	 * @return
	 */
	public boolean setMessageMode(Port myport,int op) {
		try {
			String atCommand = "AT+CMGF=" + String.valueOf(op)
					+ String.valueOf(symbol1);
			String 	strReturn = myport.sendAT(atCommand);
			if (strReturn.indexOf("OK", 0) != -1) {
//				System.out.println("*************"+(op==1?"文本":"pdu")+"模式 设置成功************");
				return true;
			}
			return false;
		} catch (Exception ex) {
			logger.error("出错日志  信息:发送短信文本方式设置出错" );
			return false;
		}
	}	
}

