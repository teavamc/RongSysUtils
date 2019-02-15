package com.socket.mapper;

import java.util.Map;

import com.socket.entity.Terminal_Conditions;
import com.socket.entity.Terminal_Recieve;
/**
 * 终端相关
 * @author HTT
 *
 */
public interface TerminalMapper {

	/**
	 * 插入终端状态属性
	 * @param tc
	 * @throws Exception
	 */
	public void insertConditionsOfIMEI( Terminal_Conditions tc) throws Exception;
	/**
	 * 获取设备的终端交互时间
	 * @param imei
	 * @return
	 * @throws Exception
	 */
	public String getIntertime( String imei) throws Exception;
	/**
	 * 查询终端应急rds码和调频频率
	 * @param imei
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getRdsAndfreq( String imei) throws Exception;
	/**
	 * 获取设备是否启用状态
	 * @param imei
	 * @return true 启用，false 停用
	 * @throws Exception
	 */
	public Boolean getTerIsuse( String imei) throws Exception;
	/**
	 * 根据IMEI号获取终端状态
	 * @param imei
	 * @return
	 * @throws Exception
	 */
	public Terminal_Conditions getTerminalCondition(String imei) ;
	/**
	 * 添加终端访问记录
	 * @param tr
	 * @throws Exception
	 */
	public void insertTerminalRecieve( Terminal_Recieve tr) throws Exception;
	/**
	 * 是否存在该终端状态
	 * @param imei
	 * @return
	 * @throws Exception
	 */
	public int existCondition( String imei) throws Exception;
	/**
	 * 更新终端状态参数信息
	 * @param tc
	 * @throws Exception
	 */
	public void updateConditions1(Terminal_Conditions tc) throws Exception;
	/**
	 * 更新终端音量状态信息
	 * @param tc
	 * @throws Exception
	 */
	public void updateConditions2(Terminal_Conditions tc) throws Exception;
	/**
	 * 更新终端fm调频发射机参数
	 * @param tc
	 * @throws Exception
	 */
	public void updateConditions3(Terminal_Conditions tc) throws Exception;
	/**
	 * 更新终端基站信息
	 * @param tc
	 * @throws Exception
	 */
	public void updateBasestation(Terminal_Conditions tc) throws Exception;
	/**
	 * 设置设备不可用
	 * @param imei
	 * @throws Exception
	 */
	public void updateTerNouse( String imei) throws Exception;
}
