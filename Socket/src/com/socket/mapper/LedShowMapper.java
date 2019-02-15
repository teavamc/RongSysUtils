package com.socket.mapper;

import com.socket.entity.LedShowInfo;

public interface LedShowMapper {

	/**
	 * 查询终端最新LED显示
	 * @return
	 * @throws Exception
	 */
	public LedShowInfo getTerNewLedShow() throws Exception;
}
