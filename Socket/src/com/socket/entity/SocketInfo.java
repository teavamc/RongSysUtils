package com.socket.entity;

import java.util.Date;

public class SocketInfo {

	private String channelAddress;
	private String imei;
	private Integer byteCount=0;//标示当前文件字节发送的长度到哪个位置
	private byte[] byteFile;
	private Date lastTime;
	private Integer filelenth=0;//设置当前发送文件的总字数
	public String getChannelAddress() {
		return channelAddress;
	}
	public void setChannelAddress(String channelAddress) {
		this.channelAddress = channelAddress;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public Integer getByteCount() {
		return byteCount;
	}
	public void setByteCount(Integer byteCount) {
		this.byteCount = byteCount;
	}
	public byte[] getByteFile() {
		return byteFile;
	}
	public void setByteFile(byte[] byteFile) {
		this.byteFile = byteFile;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public Integer getFilelenth() {
		return filelenth;
	}
	public void setFilelenth(Integer filelenth) {
		this.filelenth = filelenth;
	}
	
	
}
