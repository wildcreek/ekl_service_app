package com.leyikao.onlinelearn.serviceapp.td.mapper;

import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository
public interface IFeedbackMapper {
	public int add(Map<String, String> map);
}
