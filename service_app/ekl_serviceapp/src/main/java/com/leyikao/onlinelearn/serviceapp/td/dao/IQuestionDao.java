package com.leyikao.onlinelearn.serviceapp.td.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IQuestionDao {
	public Map<String, Object> statInfo(List<String> testPaperIdList);
	public Map<String, Map<String, String>> idList(List<String> knowledgePointIdList);
	public List<Map<String, String>> questionIdsByTestPaper(String testPaperId);
	public Map<String, Map<String, Object>> questionsInfo(Collection<String> questionIdList);
	public List<Map<String, String>> questionOptions(List<String> questionIdList);
	public List<Map<String, String>> correctAnswer(List<String> questionIdList);
	public List<Map<String, Object>> questionAnswer(Collection<String> questionIdList);
	public Map<String, Map<String, Object>> yearOfQuestion(Collection<String> questionIds);
}
