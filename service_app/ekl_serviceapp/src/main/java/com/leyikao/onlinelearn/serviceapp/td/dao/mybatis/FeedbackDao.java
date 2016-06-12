package com.leyikao.onlinelearn.serviceapp.td.dao.mybatis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.leyikao.onlinelearn.serviceapp.td.dao.IFeedbackDao;
import com.leyikao.onlinelearn.serviceapp.td.mapper.IFeedbackMapper;

public class FeedbackDao implements IFeedbackDao {

	@Resource
	private IFeedbackMapper feedbackMapper;
	
	@Override
	public int add(String userId, String score, String feedbackContent) {
		Map<String, String> map = new ConcurrentHashMap<>();
		map.put("userId", userId);
		map.put("score", score);
		map.put("feedbackContent", feedbackContent);
		return feedbackMapper.add(map);
	}

}