package com.leyikao.onlinelearn.serviceapp.qa.dao;

import java.util.List;
import java.util.Map;


public interface IQAUserDao {
	public List<String> qureyCourseList();

	public List<Map<String, Object>> problemList(String pageNumber,
												 Integer courseName, String userId, Integer pagesize);

	public List<Map<String, Object>> qureyCoutInfo(String problemId);

	public int saveProblem(String userId, String courseName, String picUrl,
						   String problemId, String questionDescr);

	public int saveReply(String userId, String replyid, String problemId,
						 String quoteReplyId, String replyDescr, String userImagePathName);

	public List<Map<String, Object>> queryReplyList(Integer pageNumber, String problemId, Integer pagesize);

	public List<Map<String, Object>> questionCloseList(String replyid);

	public List<Map<String, String>> queryProblemList(String userId,
													  String pageNumber, String courseName);

	public int saveSeekhelp(String userId, String problemId, String seekhelpId);

	public int saveAccept(String userId, String replyId, String acceptId);

	public int saveThanks(String userId, String replyId, String thanksId);

	public List<Map<String, Object>> problemListCount(String userId,
													  String courseName);

	public List<Map<String, Object>> queryReplyListCount(String problemId);

	public List<Map<String, Object>> myProblemList(Integer pageNumber,
												   String courseName, String userId, Integer pagesize);

	public List<Map<String, Object>> myProblemListCount(String userId,
														String courseName);

	public List<Map<String, Object>> myReplyList(Integer pageNumber, String courseName,
												 String userId, Integer pagesize);

	public List<Map<String, Object>> myReplyListCount(String userId,
													  String courseName);

	public List<Map<String, Object>> seekHelpCount(String problemId,
												   String userById);


}
