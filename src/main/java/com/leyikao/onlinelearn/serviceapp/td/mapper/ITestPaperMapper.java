package com.leyikao.onlinelearn.serviceapp.td.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface ITestPaperMapper {
	public List<String> queryTestPaperIds(Map<String, String> map);
	
	public List<Map<String, String>> queryTestPaperInfo(String examType);
}
