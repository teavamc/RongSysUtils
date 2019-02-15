package com.sms.entity;

/**
 * 短信接收
 *
 */
public class Receive{
	private String rmid;//接收短信id
	private String rmobile;//电话号码
	private String rcontent;//短信内容
	private String recivedtime;//短信发送时间
	private Boolean isread;//是否发送
	private String remark;//备注

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRmid() {
		return rmid;
	}
	public void setRmid(String rmid) {
		this.rmid = rmid;
	}
	public String getRmobile() {
		return rmobile;
	}
	public void setRmobile(String rmobile) {
		this.rmobile = rmobile;
	}
	public String getRcontent() {
		return rcontent;
	}
	public void setRcontent(String rcontent) {
		this.rcontent = rcontent;
	}
	public String getRecivedtime() {
		return recivedtime;
	}
	public void setRecivedtime(String recivedtime) {
		this.recivedtime = recivedtime;
	}
	public Boolean getIsread() {
		return isread;
	}
	public void setIsread(Boolean isread) {
		this.isread = isread;
	}

}
