package com.sms.mapper;

import java.util.List;

import com.sms.entity.Receive;
import com.sms.entity.Send;

public interface MessageMapper {

	/**
	 * 查询未发送短信
	 * @return
	 * @throws Exception
	 */
	public List<Send> getUnSendMessage() throws Exception;
	/**
	 * 添加接收短信
	 * @param r
	 * @throws Exception
	 */
	public void addReceiveMessage(Receive r) throws Exception;
	/**
	 * 设置短信发送
	 * @param smid
	 * @throws Exception
	 */
	public void setMessageSend(Send s) throws Exception;
}
