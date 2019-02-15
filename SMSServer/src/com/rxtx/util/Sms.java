package com.rxtx.util;

import java.util.ArrayList;
import java.util.List;

/***
 * 短信猫操作类  测试用
 * 包括短信猫的打开、关闭、读取等操作
 * @author hzy
 *
 */
public class Sms{

	private CommonSms commonsms;
	private static char symbol1 = 13;
	private static String strReturn = "", atCommand = "";

	public boolean SendSms(Port myport,int op) {
		if(!myport.isIsused())
		{		
		System.out.println("COM通讯端口未正常打开!");		
		return false;
		}
		setMessageMode(myport,op);
		// 空格
		char symbol2 = 34;
		// ctrl~z 发送指令
		char symbol3 = 26;
		try {
			if(op==1){
				atCommand = "AT+CSMP=17,169,0,08" + String.valueOf(symbol1);
				strReturn = myport.sendAT(atCommand);
				System.out.println(strReturn);
				if (strReturn.indexOf("OK", 0) != -1) {
					atCommand = "AT+CMGS=" + commonsms.getRecver()
							+ String.valueOf(symbol1);
					strReturn = myport.sendAT(atCommand);
					atCommand = StringUtil.encodeHex(commonsms.getSmstext().trim())
							+ String.valueOf(symbol3) + String.valueOf(symbol1);
					strReturn = myport.sendAT(atCommand);
					if (strReturn.indexOf("OK") != -1
							&& strReturn.indexOf("+CMGS") != -1) {
						System.out.println("短信发送成功...");
						return true;
					}
				}   
			}else if(op==0){
				String[] txt = SendUtil.PDUEncoder(commonsms.getRecver(), commonsms.getSmstext().trim()).split(",");
				atCommand = "AT+CMGS=" + txt[0] + String.valueOf(symbol1);
				strReturn = myport.sendAT(atCommand);
				strReturn = myport.sendAT(txt[1]+ String.valueOf(symbol3) + String.valueOf(symbol1));
				if (strReturn.indexOf("OK") != -1
						&& strReturn.indexOf("+CMGS") != -1) {
					System.out.println("短信发送成功...");
					return true;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();			
			System.out.println("短信发送失败...");		
			return false;
		}	
		System.out.println("短信发送失败...");	
		return false;
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
				System.out.println("*************消息模式 设置成功************");
				return true;
			}
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}	
	
	/**
	* 读取所有短信 
	* @return CommonSms集合
	*/
	public List<CommonSms> RecvSmsList(Port myport) {
		if(!myport.isIsused())
		{
			System.out.println("System Message:  COM通讯端口未正常打开!");		
			return null;
		}
		List<CommonSms> listMes = new ArrayList<CommonSms>();
		try {
			atCommand = "AT+CMGL=\"REC UNREAD\"";
//			atCommand = "AT+CMGL=\"ALL\"";
//			AT+CMGL="REC UNREAD"代表显示未读短信清单
//			           AT+CMGL= "REC READ"代表显示已读短信清单
//			           AT+CMGL= "STO SENT"代表显示已发送的存储短信清单
//			           AT+CMGL= "STO UNSENT"代表显示未发送的存储短信清单
//			           AT+CMGL= "ALL"代表显示所有短信清单
			strReturn = myport.sendAT(atCommand);
			if(strReturn.contains("ERROR"))
			System.out.println("System Message:  读取短信出错!");
			listMes = StringUtil.analyseArraySMS(strReturn);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listMes;
	}

	
	/**
	 * 删除短信
	 * @param index 短信存储的位置
	 * @return boolean
	 */

	public boolean DeleteSMS(int index,Port myport) {
		if(!myport.isIsused()){
			System.out.println("System Message:  COM通讯端口未正常打开!");
			return false;
		}
		try {
			atCommand = "AT+CMGD=" + index;
			strReturn = myport.sendAT(atCommand);
			if (strReturn.indexOf("OK") != -1) {
				System.out.println("System Message:  成功删除存储位置为" + index
						+ "的短信......");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 删除短信猫中所有短信
	 * @return boolean
	 */
	public boolean DeleteAllSMS(Port myport)
	{
		List list=RecvSmsList(myport);
		boolean ret=true;
		if(list!=null&&!list.equals("")&&list.size()>0)
		{		
		for(int i=0;i<list.size();i++)
		{
			CommonSms tempcomsms=(CommonSms)list.get(i);
			if(!DeleteSMS(tempcomsms.getId(),myport))
			{
				ret=false;
			}
		}
		}
		return ret;
	}
	public CommonSms getCommonsms() {
		return commonsms;
	}

	public void setCommonsms(CommonSms commonsms) {
		this.commonsms = commonsms;
	}
	/**
	 * 号码，内容，发送短信息
	 * @param phone
	 * @param countstring
	 * @throws Exception
	 */
	public static void sendmsn(String phone,String countstring){
		 Sms s = new Sms();
		  // 发送测试		
		  CommonSms cs=new CommonSms();
		  cs.setRecver(phone);
		  cs.setSmstext(countstring);
		  s.setCommonsms(cs);
		  Port myort=new Port("COM4");
		  System.out.println(myort.isIsused()+"     "+myort.getCOMname());
		 s.SendSms(myort,0);	
		 s.setMessageMode(myort,1);
		  List<CommonSms> recvlist = s.RecvSmsList(myort);
		  if(recvlist!=null){
			  for(CommonSms sms:recvlist){
				  System.out.println("发送人："+sms.getSender()+"  时间："+sms.getDate()+"  状态："+sms.getState());
				  System.out.println("内容："+sms.getSmstext());
			  }
		  }
		  myort.close();
	}
	
	public static void main(String[] args) throws Exception {
		sendmsn("18569051230","urgent:mp3,1700399.mp3,600");
	}	
}