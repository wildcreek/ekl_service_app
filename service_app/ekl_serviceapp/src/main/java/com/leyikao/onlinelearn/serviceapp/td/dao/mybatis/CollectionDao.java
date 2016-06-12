package com.leyikao.onlinelearn.serviceapp.td.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.leyikao.onlinelearn.serviceapp.td.dao.ICollectionDao;
import com.leyikao.onlinelearn.serviceapp.td.mapper.ICollectionMapper;

public class CollectionDao implements ICollectionDao {

	@Resource
	private ICollectionMapper collectionMapper;
	
	@Override
	public Map<String, Map<String, Object>> statInfo(String userId) {
		List<Map<String, Object>> rowData = collectionMapper.statInfo(userId);
		Map<String, Map<String, Object>> statMap = new ConcurrentHashMap<>();
		rowData.forEach(entry -> {
			statMap.put(entry.get("knowledgePointId").toString(), entry);
		});
		return statMap;
	}

	@Override
	public int collectionQuestion(String userId, String questionId) {
		Map<String, String> map = new ConcurrentHashMap<>();
		map.put("userId", userId);
		map.put("questionId", questionId);
		return collectionMapper.collectionQuestion(map);
	}

	@Override
	public Map<String, Map<String, String>> idList(String userId, List<String> knowledgePointIdList) {
		Map<String, Object> map = new ConcurrentHashMap<>();
		map.put("userId", userId);
		map.put("knowledgePointIds", knowledgePointIdList);
		List<Map<String, String>> dataQuestionList = collectionMapper.idList(map);
		Map<String, Map<String, String>> dataQuestionMap = new HashMap<>();
		dataQuestionList.forEach(item -> dataQuestionMap.put(item.get("questionId"), item));
		return dataQuestionMap;
	}

}
