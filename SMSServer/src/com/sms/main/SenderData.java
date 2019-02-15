package com.sms.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.sms.entity.Receive;
import com.sms.entity.Send;
import com.sms.mapper.IOTSmsMapper;
import com.sms.mapper.InfoSmsMapper;
import com.sms.mapper.MessageMapper;
import com.sms.util.Logger;

public class SenderData {
	MessageMapper messageMapper;
	InfoSmsMapper infosmsMapper;
	IOTSmsMapper iotsmsMapper;
	private SqlSessionFactory sqlSessionFactory;
	private SqlSessionFactory infosqlSessionFactory;
	private SqlSessionFactory iotsqlSessionFactory;
	protected Logger logger = Logger.getLogger(this.getClass());
	public SenderData(){
		//mybatis配置文件
		String resource = "mybatis/SqlMapConfig.xml";
		String inforesource = "mybatis/InfoSqlMapConfig.xml";
		String iotresource = "mybatis/IOTSqlMapConfig.xml";
		//得到配置文件流
		InputStream inputStream = null;
		InputStream infoinputStream = null;
		InputStream iotinputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			infoinputStream = Resources.getResourceAsStream(inforesource);
			iotinputStream = Resources.getResourceAsStream(iotresource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//创建会话工厂,传入mybatis的配置信息
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		infosqlSessionFactory = new SqlSessionFactoryBuilder().build(infoinputStream);
		iotsqlSessionFactory = new SqlSessionFactoryBuilder().build(iotinputStream);
	}
	/**
	 * 获取应急广播系统未发送短信
	 * @return
	 */
	public List<Send> getBroadUnSendMessage(){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		messageMapper = sqlSession.getMapper(MessageMapper.class);
		List<Send> sends = null;
		try {
			sends = messageMapper.getUnSendMessage();
		} catch (Exception e) {
			logger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:获取应急广播系统未发送短信,getBroadUnSendMessage" );
		}
		sqlSession.close();
		return sends;
	}
	/**
	 * 获取信息发布系统未发送短信
	 * @return
	 */
	public List<Send> getInfoUnSendMessage(){
		SqlSession sqlSession = infosqlSessionFactory.openSession();
		infosmsMapper = sqlSession.getMapper(InfoSmsMapper.class);
		List<Send> sends = null;
		try {
			sends = infosmsMapper.getUnSendMessage();
		} catch (Exception e) {
			logger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:获取信息发布系统未发送短信,getInfoUnSendMessage" );
		}
		sqlSession.close();
		return sends;
	}
	/**
	 * 获取山洪预警系统未发送短信
	 * @return
	 */
	public List<Send> getIOTUnSendMessage(){
		SqlSession sqlSession = iotsqlSessionFactory.openSession();
		iotsmsMapper = sqlSession.getMapper(IOTSmsMapper.class);
		List<Send> sends = null;
		try {
			sends = iotsmsMapper.getUnSendMessage();
		} catch (Exception e) {
			logger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:获取山洪预警系统未发送短信,getIOTUnSendMessage" );
		}
		sqlSession.close();
		return sends;
	}

	/**
	 * 设置应急广播系统短信已发送
	 * @param smdi
	 */
	public void setBroadMessageSend(Send send)  {
		try {
			SqlSession sqlSession = sqlSessionFactory.openSession();
			messageMapper = sqlSession.getMapper(MessageMapper.class);
			messageMapper.setMessageSend(send);
			sqlSession.commit();
			sqlSession.close();
		}  catch (Exception err) {
			logger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:设置应急广播系统短信已发送,setBroadMessageSend" );
		}
	}
	/**
	 * 设置信息发布系统短信已发送
	 * @param smdi
	 */
	public void setInfoMessageSend(Send send)  {
		try {
			SqlSession sqlSession = infosqlSessionFactory.openSession();
			infosmsMapper = sqlSession.getMapper(InfoSmsMapper.class);
			infosmsMapper.setMessageSend(send);
			sqlSession.commit();
			sqlSession.close();
		}  catch (Exception err) {
			logger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:设置信息发布系统短信已发送,setInfoMessageSend" );
		}
	}
	/**
	 * 设置山洪预警系统短信已发送
	 * @param smdi
	 */
	public void setIOTMessageSend(Send send)  {
		try {
			SqlSession sqlSession = iotsqlSessionFactory.openSession();
			iotsmsMapper = sqlSession.getMapper(IOTSmsMapper.class);
			iotsmsMapper.setMessageSend(send);
			sqlSession.commit();
			sqlSession.close();
		}  catch (Exception err) {
			logger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:设置山洪预警系统短信已发送,setInfoMessageSend" );
		}
	}
	/**
	 * 添加接收短信
	 * @param smdi
	 */
	public void addReceiveMessage(Receive receive)
	{
		try
		{
			SqlSession sqlSession = sqlSessionFactory.openSession();
			messageMapper = sqlSession.getMapper(MessageMapper.class);
			messageMapper.addReceiveMessage(receive);
			sqlSession.commit();
			sqlSession.close();
		}
		catch (Exception err)
		{
			logger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:添加接收短信,addReceiveMessage" );
		}
	}
}
