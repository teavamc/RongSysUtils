package com.socket.entity;
/**
 * 终端授权电话表
 * @author HTT
 *
 */
public class TerminalTels {

	private String telid;//编号
	private String tid;//终端IMEI编号
	private String tel;//电话号码
	private String isuse;//是否使用
	public String getTelid() {
		return telid;
	}
	public void setTelid(String telid) {
		this.telid = telid;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getIsuse() {
		return isuse;
	}
	public void setIsuse(String isuse) {
		this.isuse = isuse;
	}
	
}
