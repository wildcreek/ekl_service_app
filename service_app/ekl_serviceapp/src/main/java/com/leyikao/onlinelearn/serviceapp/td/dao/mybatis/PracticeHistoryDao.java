package com.leyikao.onlinelearn.serviceapp.td.dao.mybatis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.leyikao.onlinelearn.serviceapp.td.dao.IPracticeHistoryDao;
import com.leyikao.onlinelearn.serviceapp.td.mapper.IPracticeHistoryMapper;

public class PracticeHistoryDao implements IPracticeHistoryDao{

	@Resource
	private IPracticeHistoryMapper practiceHistoryMapper;
	
	@Override
	public int add(String practiceHistoryId, String userId, String testPaperId, String answerType,
			String beginTime, String endTime, String isFinishAnswer) {
		
		Map<String, String> parameterMap = new ConcurrentHashMap<>();
		parameterMap.put("practiceHistoryId", practiceHistoryId);
		parameterMap.put("userId", userId);
		parameterMap.put("testPaperId", testPaperId);
		parameterMap.put("answerType", answerType);
		parameterMap.put("beginTime", beginTime);
		parameterMap.put("endTime", endTime);
		parameterMap.put("isFinishAnswer", isFinishAnswer);
		return practiceHistoryMapper.add(parameterMap);
	}

	@Override
	public String getUUID() {
		List<String> uuids = practiceHistoryMapper.getUUID();
		String uuid = (uuids.size() > 0 ? uuids.get(0) : "");
		return uuid;
	}
	
}
