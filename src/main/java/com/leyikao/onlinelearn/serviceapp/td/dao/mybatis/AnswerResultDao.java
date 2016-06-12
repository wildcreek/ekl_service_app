package com.leyikao.onlinelearn.serviceapp.td.dao.mybatis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.leyikao.onlinelearn.serviceapp.td.dao.IAnswerResultDao;
import com.leyikao.onlinelearn.serviceapp.td.mapper.IAnswerResultMapper;

public class AnswerResultDao implements IAnswerResultDao {

	@Resource
	private IAnswerResultMapper answerResultMapper;
	
	@Override
	public List<Map<String, Object>> answerInfoByKnowledgePoint(String userId, Collection<String> knowledgePointIdList) {
		Map<String, Object> parameterMap = new ConcurrentHashMap<String, Object>();
		parameterMap.put("userId", userId);
		parameterMap.put("knowledgePointIds", knowledgePointIdList);
		return answerResultMapper.answerInfoByKnowledgePoint(parameterMap);
		
	}

	@Override
	public List<Map<String, Object>> errorQuestions(String userId) {
		return answerResultMapper.errorQuestions(userId);
	}

	@Override
	public List<String> idList(String userId, Collection<String> knowledgePointIdList) {
		Map<String, Object> parameterMap = new ConcurrentHashMap<>();
		parameterMap.put("userId", userId);
		parameterMap.put("knowledgePointIds", knowledgePointIdList);
		return answerResultMapper.idList(parameterMap);
	}

	@Override
	public int add(Map<String, Map<String, String>> answerResultMap) {
		int effectRows = 0;
		if (answerResultMap.size() > 0){
			effectRows = answerResultMapper.add(new ArrayList<>(answerResultMap.values()));
		}
		
		return effectRows;
	}

	@Override
	public List<Map<String, Object>> testDatabaseAnswerStat(String testPaperId) {
		return answerResultMapper.testDatabaseAnswerStat(testPaperId);
	}

	@Override
	public List<Map<String, Object>> questionAnswerStat(
			Collection<String> questionIdList) {
		return answerResultMapper.questionAnswerStat(questionIdList);
	}

	@Override
	public Map<String, Map<String, Object>> practiceAnswerResult(
			String practiceHistoryId, boolean isOnlyErrorAnswer) {
		Map<String, Map<String, Object>> answerResultMap = new ConcurrentHashMap<>();
		
		List<Map<String, Object>> rows = answerResultMapper.practiceAnswerResult(practiceHistoryId);
		rows.forEach(item -> {
			int answerStatus = Integer.parseInt(item.get("answerStatus").toString());
			boolean isErrorAnswer = (answerStatus == 0 ? true : false);
			String questionId = item.get("questionId").toString();
			if (!isOnlyErrorAnswer){
				answerResultMap.put(questionId, item);
			} else if (isOnlyErrorAnswer && isErrorAnswer){
				answerResultMap.put(questionId, item);
			}
		});
		
		return answerResultMap;
	}

	@Override
	public List<Map<String, Object>> answerInfoByQuestion(Collection<String> questionIdList) {
		return answerResultMapper.answerInfoByQuestion(questionIdList);
	}

	@Override
	public Integer answerQuestionNumber() {
		List<String> questionStat = answerResultMapper.answerQuestionNumber();
		Integer questionNumber = 0;
		if (questionStat.size() > 0){
			questionNumber = Integer.parseInt(questionStat.get(0));
		}
		return questionNumber;
	}

	@Override
	public List<Map<String, Object>> errorDataQuestions(String userId,
			Collection<String> knowledgePointIdList) {
		Map<String, Object> parameterMap = new ConcurrentHashMap<>();
		parameterMap.put("userId", userId);
		parameterMap.put("knowledgePointIds", knowledgePointIdList);
		return answerResultMapper.errorDataQuestions(parameterMap);
	}

}
