package com.leyikao.onlinelearn.serviceapp.td.mapper;

import java.util.List;
import java.util.Map;

public interface ICollectionMapper {
	public List<Map<String, Object>> statInfo(String userId);
	
	public int collectionQuestion(Map<String, String> map);
	
	public List<Map<String, String>> idList(Map<String, Object> map);
}
