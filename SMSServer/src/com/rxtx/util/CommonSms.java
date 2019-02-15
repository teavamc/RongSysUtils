package com.rxtx.util;

import java.util.Date;

/***
 * CommonSms 短信用于全局变量 
 * @author cms
 *@version 1.9, 09/10/20
 * 
 */
public class CommonSms{

	/** id */
	private int id;
	/**type区分维修人员还是营业厅发送短信*/
	private int type;//区分给维修人员还是给营业厅发送短信
	/**短信内容*/
	private String smstext;
	/**短信发送方*/
	private String sender;//短信发送方
	/**短信接收发*/
	private String recver;//短信接收发
	/**时间*/
	private Date date;
	/**消息状态*/
	private String state;//消息状态
	/**对应的营业厅名称*/
	private String bhname;//对应的营业厅名称

	
	public String getBhname() {
		return bhname;
	}
	public void setBhname(String bhname) {
		this.bhname = bhname;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSmstext() {
		return smstext;
	}
	public void setSmstext(String smstext) {
		this.smstext = smstext;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
		
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getRecver() {
		return recver;
	}
	public void setRecver(String recver) {
		this.recver = recver;
	}
	
}

