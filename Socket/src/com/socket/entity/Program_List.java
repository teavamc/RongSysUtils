package com.socket.entity;

import java.util.List;

/**
 * 节目播出单的节目明细管理
 * @ClassName ProList
 */
public class Program_List {

	private String proid;//播出单编号
	private String protype;//播出节目类别
	private String proname;//节目名称
	private String programid;//节目编号
	private String begintime;//开始时间
	private String broadtime;//播放时间
	private String remark;//备注
	
	private String fname;//文件名字
	private String filename;//文件
	private String address;//文件地址
	private String urls;//下载地址	
	public String getProid() {
		return proid;
	}
	public void setProid(String proid) {
		this.proid = proid;
	}
	public String getProtype() {
		return protype;
	}
	public void setProtype(String protype) {
		this.protype = protype;
	}
	public String getProname() {
		return proname;
	}
	public void setProname(String proname) {
		this.proname = proname;
	}
	public String getProgramid() {
		return programid;
	}
	public void setProgramid(String programid) {
		this.programid = programid;
	}
	public String getBegintime() {
		return begintime;
	}
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUrls() {
		return urls;
	}
	public void setUrls(String urls) {
		this.urls = urls;
	}
	@Override
	public String toString() {
		return "Program_List [proid=" + proid + ", protype=" + protype
				+ ", proname=" + proname + ", programid=" + programid
				+ ", begintime=" + begintime + ", broadtime=" + broadtime
				+ ", remark=" + remark + ", fname=" + fname + ", filename="
				+ filename + ", address=" + address + ", urls=" + urls + "]";
	}
	public String getBroadtime() {
		return broadtime;
	}
	public void setBroadtime(String broadtime) {
		this.broadtime = broadtime;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
}
