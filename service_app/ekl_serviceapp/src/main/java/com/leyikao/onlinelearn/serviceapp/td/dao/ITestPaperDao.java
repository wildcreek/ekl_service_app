package com.leyikao.onlinelearn.serviceapp.td.dao;

import java.util.List;
import java.util.Map;

public interface ITestPaperDao {
	public List<String> queryTestPaperIds(String courseName, String examType);
	public List<Map<String, String>> queryTestPaperInfo(String examType);
}
