package com.leyikao.onlinelearn.serviceapp.td.mapper;

import java.util.List;
import java.util.Map;

public interface ITestPaperMapper {
	public List<String> queryTestPaperIds(Map<String, String> map);
	
	public List<Map<String, String>> queryTestPaperInfo(String examType);
}
