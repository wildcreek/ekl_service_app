package com.leyikao.onlinelearn.serviceapp.td.mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface IPracticeHistoryMapper {
	public int add(Map<String, String> parameterMap);
	public List<String> getUUID();
}
