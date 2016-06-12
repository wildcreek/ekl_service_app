package com.leyikao.onlinelearn.serviceapp.td.dao.mybatis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.leyikao.onlinelearn.serviceapp.td.dao.IKnowledgePointDao;
import com.leyikao.onlinelearn.serviceapp.td.mapper.IKnowledgePointMapper;

public class KnowledgePointDao implements IKnowledgePointDao {

	@Resource
	private IKnowledgePointMapper knowledgePointMapper;
	
	@Override
	public Map<String, Map<String, String>> queryAll() {
		Map<String, Map<String, String>> idToKnowledgePoint = new ConcurrentHashMap<>();
		
		List<Map<String, String>>  knowledgePointTree = knowledgePointMapper.queryAllInfo();
		knowledgePointTree.forEach(kpInfo -> idToKnowledgePoint.put(kpInfo.get("knowledgePointId"), kpInfo));
		
		return idToKnowledgePoint;
	}
}
