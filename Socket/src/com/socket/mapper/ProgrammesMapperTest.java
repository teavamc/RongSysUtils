package com.socket.mapper;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.socket.entity.Programmes;


public class ProgrammesMapperTest {
//	private ApplicationContext applicationContext;

	//在setUp这个方法得到spring容器
	@Before
	public void setUp() throws Exception {
//		applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
	}

	@Test
	public void testGetProgrammesByIMEI() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("imei", "123");
		map.put("date", "2017-01-08");
//		ProgrammesMapper userMapper = (ProgrammesMapper) applicationContext.getBean("programmesMapper");
		Programmes user = null;
		try {
//			user = userMapper.getProgrammesByIMEI(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(user);
	}

}
