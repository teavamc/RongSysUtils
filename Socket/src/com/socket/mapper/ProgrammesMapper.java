package com.socket.mapper;

import java.util.Map;

import com.socket.entity.Programmes;

public interface ProgrammesMapper {
	/**
	 * 根据设备IMEI号和播出日期获取节目单
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Programmes getProgrammesByIMEI(Map<String, String> map) throws Exception;
	/**
	 * 根据files获取文件节目信息
	 * @param fileName
	 * @return
	 */
	public String getProAddressByFile(String fileName);
}
