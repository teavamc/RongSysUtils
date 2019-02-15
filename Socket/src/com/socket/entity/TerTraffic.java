package com.socket.entity;

public class TerTraffic {
	private int ttid; //终端流量信息编号
	private String tid;//终端编号
	private Double usetraffic=0.0;//使用流量
	private String lasttime;//最后统计时间
	private String remark;//备注
	
	private Double trafficlimit=0.0;//流量限制
	public int getTtid() {
		return ttid;
	}
	public void setTtid(int ttid) {
		this.ttid = ttid;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public Double getUsetraffic() {
		return usetraffic;
	}
	public void setUsetraffic(Double usetraffic) {
		this.usetraffic = usetraffic;
	}
	public String getLasttime() {
		return lasttime;
	}
	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getTrafficlimit() {
		return trafficlimit;
	}
	public void setTrafficlimit(Double trafficlimit) {
		this.trafficlimit = trafficlimit;
	}
	
	
}
