package com.leyikao.onlinelearn.serviceapp.td.mapper;

import java.util.List;
import java.util.Map;


public interface IPracticeHistoryMapper {
	public int add(Map<String, String> parameterMap);
	public List<String> getUUID();
}
