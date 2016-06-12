package com.leyikao.onlinelearn.serviceapp.td.mapper;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
@Repository
public interface IAnswerResultMapper {
	public List<Map<String, Object>> answerInfoByKnowledgePoint(Map<String, Object> map);
	public List<Map<String, Object>> errorQuestions(String userId);
	public List<Map<String, Object>> errorDataQuestions(Map<String, Object> map);
	public List<String> idList(Map<String, Object> map);
	public int add(List<Map<String, String>> list);
	public List<Map<String, Object>> testDatabaseAnswerStat(String testPaperId);
	public List<Map<String, Object>> questionAnswerStat(Collection<String> questionIds);
	public List<Map<String, Object>> practiceAnswerResult(String practiceHistoryId);
	public List<Map<String, Object>> answerInfoByQuestion(Collection<String> questionIds);
	public List<String> answerQuestionNumber();
	
}
