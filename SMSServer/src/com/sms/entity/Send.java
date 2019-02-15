package com.sms.entity;

/**
 * 短信发送
 *
 */
public class Send{
	private String smid;//接收短信id
	private String smobile;//电话号码
	private String scontent;//短信内容
	private String sendtime;//短信发送时间
	private Boolean issend;//是否发送
	private int sendtimes;//发送次数
	private String tid;//终端id	
	private String remark;//备注

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSmid() {
		return smid;
	}
	public void setSmid(String smid) {
		this.smid = smid;
	}
	public String getSmobile() {
		return smobile;
	}
	public void setSmobile(String smobile) {
		this.smobile = smobile;
	}
	public String getScontent() {
		return scontent;
	}
	public void setScontent(String scontent) {
		this.scontent = scontent;
	}
	public Boolean getIssend() {
		return issend;
	}
	public void setIssend(Boolean issend) {
		this.issend = issend;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public int getSendtimes() {
		return sendtimes;
	}
	public void setSendtimes(int sendtimes) {
		this.sendtimes = sendtimes;
	}
	

}
