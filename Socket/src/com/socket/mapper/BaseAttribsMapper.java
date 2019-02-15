package com.socket.mapper;

import java.util.List;
import com.socket.entity.BaseAttribs;

public interface BaseAttribsMapper {

	/**
	 * 查询服务器属性
	 * @return
	 * @throws Exception
	 */
	public List<BaseAttribs> getBaseAttribs() throws Exception;
}
