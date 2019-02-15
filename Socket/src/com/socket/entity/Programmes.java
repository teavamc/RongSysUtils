package com.socket.entity;

import java.util.List;

/**
 * 节目播出单管理
 * @author HTT
 */
public class Programmes {
	private String sfid;//播出单编号
	private String scategory;//播出单类别
	private String createtime;//建立时间
	private String broaddate;//播出日期
	private String userid;//操作用户编号
	private String remark;//备注
	
	private List<Program_List> prolist;//节目列表
	
	public String getSfid() {
		return sfid;
	}
	public void setSfid(String sfid) {
		this.sfid = sfid;
	}
	public String getScategory() {
		return scategory;
	}
	public void setScategory(String scategory) {
		this.scategory = scategory;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<Program_List> getProlist() {
		return prolist;
	}
	public void setProlist(List<Program_List> prolist) {
		this.prolist = prolist;
	}
	public String getBroaddate() {
		return broaddate;
	}
	public void setBroaddate(String broaddate) {
		this.broaddate = broaddate;
	}
	
	
}
