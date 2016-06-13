package com.leyikao.onlinelearn.serviceapp.qa.mapper;

import java.util.List;
import java.util.Map;

public interface IQAUserMapper {
	public List<String> qureyCourseList();

	public List<Map<String, Object>> problemList(Map<String, Object> map);

	public List<Map<String, Object>> qureyCoutInfo(String problemId);

	public int saveProblem(Map<String, String> map);

	public int saveReply(Map<String, String> map);

	public List<Map<String, Object>> queryReplyList(Map<String, Object> map);

	public List<Map<String, Object>> questionCloseList(String replyid);

	public List<Map<String, String>> questionCloseList(Map<String, String> map);

	public int saveSeekhelp(Map<String, String> map);

	public int saveAccept(Map<String, String> map);

	public int saveThanks(Map<String, String> map);

	public List<Map<String, Object>> problemListCount(Map<String, String> map);

	public List<Map<String, Object>> queryReplyListCount(String problemId);

	public List<Map<String, Object>> myProblemList(Map<String, Object> map);

	public List<Map<String, Object>> myProblemListCount(Map<String, String> map);

	public List<Map<String, Object>> myReplyList(Map<String, Object> map);

	public List<Map<String, Object>> myReplyListCount(Map<String, String> map);

	public List<Map<String, Object>> seekHelpCount(Map<String, String> map);




}
