package com.socket.mapper;

import java.util.List;

import com.socket.entity.TerminalTels;

public interface TerminalTelsMapper {

	/**
	 * 获取终端的授权电话
	 * @param imei
	 * @return
	 * @throws Exception
	 */
	public List<TerminalTels> getTerminalTelsByIMEI(String imei) throws Exception;
}
