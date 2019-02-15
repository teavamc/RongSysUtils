package com.socket.entity;
/**
 * 终端状态表
 * @author HTT
 *
 */
public class Terminal_Conditions {

	/**
	 * 终端IMEI号
	 */
	 private String terminalID;
	 /**
	  * 硬件版本
	  */
	 private String hardwareVersion ;
	 /**
	  * 软件版本
	  */
     private String softwareVersion ;
     /**
      * 内核温度
      */
     private String temperature ;
	 /**
	  * 网络类型
	  */
     private String networkTypes ;
	 /**
	  * 信号质量
	  */
     private String signalQuality ;
     /**
      * SD卡容量
      */
     private String sdCapacity ;
     /**
      * MP3音量
      */
     private Integer mp3Volume;
     /**
      * FM音量
      */
     private Integer fmVolume;
     /**
      * GSM音量
      */
     private Integer gsmVolume;
     /**
      * NULL音量
      */
     private Integer nullVolume;
     /**
      * 发射功率
      */
     private String transmitpower;
     /**
      * 反射功率
      */
     private String reflectedpower;
     /**
      * 工作电压
      */
     private String workvoltage;
	/**
      * 更新日期
      */
     private String createdTime;
     /**
      * FM调频发射RDS码
      */
     private String rds;
     /**
      * FM调频发射频率
      */
     private String fmfrequency;
     /**
      * 基站信息
      */
     private String basestation;
     /**
      * 备注
      */
     private String remark ;
	public String getTerminalID() {
		return terminalID;
	}
	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}
	public String getHardwareVersion() {
		return hardwareVersion;
	}
	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getNetworkTypes() {
		return networkTypes;
	}
	public void setNetworkTypes(String networkTypes) {
		this.networkTypes = networkTypes;
	}
	public String getSignalQuality() {
		return signalQuality;
	}
	public void setSignalQuality(String signalQuality) {
		this.signalQuality = signalQuality;
	}
	public String getSdCapacity() {
		return sdCapacity;
	}
	public void setSdCapacity(String sdCapacity) {
		this.sdCapacity = sdCapacity;
	}
	public Integer getMp3Volume() {
		return mp3Volume;
	}
	public void setMp3Volume(Integer mp3Volume) {
		this.mp3Volume = mp3Volume;
	}
	public Integer getFmVolume() {
		return fmVolume;
	}
	public void setFmVolume(Integer fmVolume) {
		this.fmVolume = fmVolume;
	}
	public Integer getGsmVolume() {
		return gsmVolume;
	}
	public void setGsmVolume(Integer gsmVolume) {
		this.gsmVolume = gsmVolume;
	}
	public Integer getNullVolume() {
		return nullVolume;
	}
	public void setNullVolume(Integer nullVolume) {
		this.nullVolume = nullVolume;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTransmitpower() {
		return transmitpower;
	}
	public void setTransmitpower(String transmitpower) {
		this.transmitpower = transmitpower;
	}
	public String getReflectedpower() {
		return reflectedpower;
	}
	public void setReflectedpower(String reflectedpower) {
		this.reflectedpower = reflectedpower;
	}
	public String getWorkvoltage() {
		return workvoltage;
	}
	public void setWorkvoltage(String workvoltage) {
		this.workvoltage = workvoltage;
	}
	public String getRds() {
		return rds;
	}
	public void setRds(String rds) {
		this.rds = rds;
	}
	public String getFmfrequency() {
		return fmfrequency;
	}
	public void setFmfrequency(String fmfrequency) {
		this.fmfrequency = fmfrequency;
	}
	public String getBasestation() {
		return basestation;
	}
	public void setBasestation(String basestation) {
		this.basestation = basestation;
	}
     
}
