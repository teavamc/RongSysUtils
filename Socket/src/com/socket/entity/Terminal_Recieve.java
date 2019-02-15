package com.socket.entity;

/**
 * 终端访问记录
 * @author HTT
 *
 */
public class Terminal_Recieve {
	
	private String ReceiveID;//编号
	private String TerminalID;//终端IMEI号
	private String ProgrammeID;//节目播出单编号
	private String CreatedTime;//访问时间
	private String Remark;//备注
	public String getReceiveID() {
		return ReceiveID;
	}
	public void setReceiveID(String receiveID) {
		ReceiveID = receiveID;
	}
	public String getTerminalID() {
		return TerminalID;
	}
	public void setTerminalID(String terminalID) {
		TerminalID = terminalID;
	}
	public String getProgrammeID() {
		return ProgrammeID;
	}
	public void setProgrammeID(String programmeID) {
		ProgrammeID = programmeID;
	}
	public String getCreatedTime() {
		return CreatedTime;
	}
	public void setCreatedTime(String createdTime) {
		CreatedTime = createdTime;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	@Override
	public String toString() {
		return "Terminal_Recieve [ReceiveID=" + ReceiveID + ", TerminalID="
				+ TerminalID + ", ProgrammeID=" + ProgrammeID
				+ ", CreatedTime=" + CreatedTime + ", Remark=" + Remark + "]";
	}
	
	
}
