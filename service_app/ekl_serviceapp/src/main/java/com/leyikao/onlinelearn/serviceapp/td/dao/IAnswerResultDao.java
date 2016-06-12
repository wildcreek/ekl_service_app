package com.leyikao.onlinelearn.serviceapp.td.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IAnswerResultDao {
	public List<Map<String, Object>> answerInfoByKnowledgePoint(String userId, Collection<String> knowledgePointIdList);
	public List<Map<String, Object>> errorQuestions(String userId);
	public List<Map<String, Object>> errorDataQuestions(String userId, Collection<String> knowledgePointIdList);
	public List<String> idList(String userId, Collection<String> knowledgePointIdList);
	public int add(Map<String, Map<String, String>> answerResultMap);
	public List<Map<String, Object>> testDatabaseAnswerStat(String testPaperId);
	public List<Map<String, Object>> questionAnswerStat(Collection<String> questionIdList);
	public Map<String, Map<String, Object>> practiceAnswerResult(String practiceHistoryId, boolean isOnlyErrorAnswer);
	public List<Map<String, Object>> answerInfoByQuestion(Collection<String> questionIdList);
	public Integer answerQuestionNumber();
}
