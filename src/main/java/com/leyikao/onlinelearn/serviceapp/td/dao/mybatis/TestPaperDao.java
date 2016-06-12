package com.leyikao.onlinelearn.serviceapp.td.dao.mybatis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.leyikao.onlinelearn.serviceapp.td.dao.ITestPaperDao;
import com.leyikao.onlinelearn.serviceapp.td.mapper.ITestPaperMapper;

public class TestPaperDao implements ITestPaperDao {

	@Resource
	private ITestPaperMapper testPaperMapper;
	
	@Override
	public List<String> queryTestPaperIds(String courseName, String examType) {
		Map<String, String> map = new ConcurrentHashMap<>();
		map.put("courseName", courseName);
		map.put("examType", "%" + examType + "%");
		return testPaperMapper.queryTestPaperIds(map);
	}
	
	@Override
	public List<Map<String, String>> queryTestPaperInfo(String examType) {
		return testPaperMapper.queryTestPaperInfo("%" + examType + "%");
	}

}
