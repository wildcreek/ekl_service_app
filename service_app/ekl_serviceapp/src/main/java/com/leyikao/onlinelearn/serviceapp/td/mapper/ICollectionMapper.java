package com.leyikao.onlinelearn.serviceapp.td.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface ICollectionMapper {
	public List<Map<String, Object>> statInfo(String userId);
	
	public int collectionQuestion(Map<String, String> map);
	
	public List<Map<String, String>> idList(Map<String, Object> map);
}
