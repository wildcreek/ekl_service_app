package com.leyikao.onlinelearn.serviceapp.td.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IQuestionMapper {
	public List<Map<String, Object>> statInfo(List<String> testPaperIds);
	public List<Map<String, String>> idList(List<String> knowledgePointIds);
	public List<Map<String, String>> questionIdsByTestPaper(String testPaperId);
	public List<Map<String, Object>> questionsInfo(Collection<String> questionIdList);
	public List<Map<String, String>> questionOptions(List<String> questionIdList);
	public List<Map<String, String>> correctAnswer(List<String> questionIds);
	public List<Map<String, Object>> questionAnswer(Collection<String> questionIds);
	public List<Map<String, Object>> yearOfQuestion(Collection<String> questionIds);
}
