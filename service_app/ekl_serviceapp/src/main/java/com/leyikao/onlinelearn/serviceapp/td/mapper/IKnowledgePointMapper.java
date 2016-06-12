package com.leyikao.onlinelearn.serviceapp.td.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface IKnowledgePointMapper {
	public List<Map<String, String>> queryAllInfo();
}
