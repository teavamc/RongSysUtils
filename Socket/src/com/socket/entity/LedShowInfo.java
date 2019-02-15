package com.socket.entity;

/**
 * Led显示信息
 *
 */
public class LedShowInfo {
	private String lsid;//ID
	private String content;//信息内容
	private String showtime;//显示时间
	private String createtime;//创建时间
	private String tid;
	public String getLsid() {
		return lsid;
	}
	public void setLsid(String lsid) {
		this.lsid = lsid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getShowtime() {
		return showtime;
	}
	public void setShowtime(String showtime) {
		this.showtime = showtime;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}

}
