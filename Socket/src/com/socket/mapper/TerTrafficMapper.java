package com.socket.mapper;

import com.socket.entity.TerTraffic;

public interface TerTrafficMapper {

	/**
	 * 查询终端流量信息
	 * @return
	 * @throws Exception
	 */
	public TerTraffic getTerTrafficByIMEI(String imei) throws Exception;
	/**
	 * 更新终端流量数据
	 * @param tt
	 * @throws Exception
	 */
	public void updateTerTrafficByIMEI(TerTraffic tt) throws Exception;
	/**
	 * 插入终端流量数据
	 * @param tt
	 * @throws Exception
	 */
	public void insertTerTraffic(TerTraffic tt) throws Exception;
}
