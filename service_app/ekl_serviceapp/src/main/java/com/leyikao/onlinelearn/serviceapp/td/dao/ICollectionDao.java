package com.leyikao.onlinelearn.serviceapp.td.dao;

import java.util.List;
import java.util.Map;

public interface ICollectionDao {
	public Map<String, Map<String, Object>> statInfo(String userId);
	
	public int collectionQuestion(String userId, String questionId);
	
	public Map<String, Map<String, String>> idList(String userId, List<String> knowledgePointIdList);
}
