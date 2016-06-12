package com.leyikao.onlinelearn.serviceapp.td.dao.mybatis;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.leyikao.onlinelearn.serviceapp.td.dao.IQuestionDao;
import com.leyikao.onlinelearn.serviceapp.td.mapper.IQuestionMapper;

public class QuestionDao implements IQuestionDao {

	@Resource
	private IQuestionMapper questionMapper;

	@Override
	public Map<String, Object> statInfo(List<String> testPaperIds) {
		List<Map<String, Object>> kpGroupList = questionMapper.statInfo(testPaperIds);
		
		Map<String, Object> kpGroupMap = new ConcurrentHashMap<>();
		kpGroupList.forEach(kpGroup -> {
			kpGroupMap.put(kpGroup.get("knowledgePointId").toString(), kpGroup.get("kpCount"));
		});

		return kpGroupMap;
	}

	@Override
	public Map<String, Map<String, String>> idList(List<String> knowledgePointIdList) {
		List<Map<String, String>> dataQuestionList = questionMapper.idList(knowledgePointIdList);
		Map<String, Map<String, String>> dataQuestionMap = new ConcurrentHashMap<>();
		dataQuestionList.forEach(item -> dataQuestionMap.put(item.get("questionId"), item));
		return dataQuestionMap;
	}

	@Override
	public List<Map<String, String>> questionIdsByTestPaper(String testPaperId) {
		return questionMapper.questionIdsByTestPaper(testPaperId);
	}

	@Override
	public Map<String, Map<String, Object>> questionsInfo(
			Collection<String> questionIdList) {
		Map<String, Map<String, Object>> infoMap = new ConcurrentHashMap<>();
		
		List<Map<String, Object>> infos =  questionMapper.questionsInfo(questionIdList);
		infos.forEach(info -> {
			infoMap.put(info.get("questionId").toString(), info);
		});
		
		return infoMap;
	}

	@Override
	public List<Map<String, String>> questionOptions(List<String> questionIdList) {
		return questionMapper.questionOptions(questionIdList);
	}

	@Override
	public List<Map<String, String>> correctAnswer(List<String> questionIdList) {
		return questionMapper.correctAnswer(questionIdList);
	}

	@Override
	public List<Map<String, Object>> questionAnswer(Collection<String> questionIdList) {
		return questionMapper.questionAnswer(questionIdList);
	}

	@Override
	public Map<String, Map<String, Object>> yearOfQuestion(Collection<String> questionIds) {
		Map<String, Map<String, Object>> map = new HashMap<>();
		List<Map<String, Object>> list = questionMapper.yearOfQuestion(questionIds);
		list.forEach(item -> map.put(item.get("questionId").toString(), item));
		return map;
	}

}
