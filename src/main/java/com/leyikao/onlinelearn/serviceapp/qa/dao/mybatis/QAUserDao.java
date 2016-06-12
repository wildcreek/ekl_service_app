package com.leyikao.onlinelearn.serviceapp.qa.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.leyikao.onlinelearn.serviceapp.qa.dao.IQAUserDao;
import com.leyikao.onlinelearn.serviceapp.qa.mapper.IQAUserMapper;

public class QAUserDao implements IQAUserDao{
	@Resource
	private IQAUserMapper qAUserMapper;
	@Override
	public List<String> qureyCourseList() {
		// TODO Auto-generated method stub
		return qAUserMapper.qureyCourseList();

	}
	@Override
	public List<Map<String, Object>> problemList(String userId,Integer pageNumber,
			String courseName,Integer pagesize) {
		// TODO Auto-generated method stub
		Map<String, Object> map =new HashMap();
		map.put("pageNumber", pageNumber);
		map.put("courseName", courseName);
		map.put("userId", userId);
		map.put("pagesize", pagesize);
		return qAUserMapper.problemList(map);
	}
	@Override
	public List<Map<String, Object>> qureyCoutInfo(String problemId) {
		// TODO Auto-generated method stub
		return qAUserMapper.qureyCoutInfo(problemId);
	}
	@Override
	public int saveProblem(String userId, String courseName, String picUrl,
			String problemId, String questionDescr) {
		Map<String,String> map=new HashMap();
		map.put("userId", userId);
		map.put("courseName", courseName);
		map.put("picUrl", picUrl);
		map.put("problemId", problemId);
		map.put("questionDescr", questionDescr);
		return qAUserMapper.saveProblem(map);
		
	}
	@Override
	public int saveReply(String userId, String replyid, String problemId,
			String quoteReplyId, String replyDescr, String userImagePathName) {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap();
		map.put("userId", userId);
		map.put("replyId", replyid);
		map.put("problemId", problemId);
		map.put("quoteReplyId", quoteReplyId);
		map.put("replyDescr", replyDescr);
		map.put("userImagePathName", userImagePathName);
		return qAUserMapper.saveReply(map);
	

	}
	@Override
	public  List<Map<String, Object>> queryReplyList(Integer pageNumber, String problemId,Integer pagesize) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap();
		map.put("pageNumber", pageNumber);
		map.put("pagesize", pagesize);
		map.put("problemId", problemId);
		return qAUserMapper.queryReplyList(map);
		
	}
	@Override
	public List<Map<String, Object>> questionCloseList(String replyid) {
		// TODO Auto-generated method stub
		 return qAUserMapper.questionCloseList(replyid);
	}
	@Override
	public List<Map<String, String>> queryProblemList(String userId,
			String pageNumber, String courseName) {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap();
		map.put("userId", userId);
		map.put("pageNumber", pageNumber);
		map.put("courseName", courseName);
	
		 return qAUserMapper.questionCloseList(map);
	}
	@Override
	public int saveSeekhelp(String userId, String problemId,String seekhelpId) {
		Map<String,String> map=new HashMap();
		map.put("userId", userId);
		map.put("problemId", problemId);
		map.put("seekhelpId", seekhelpId);
		return qAUserMapper.saveSeekhelp(map);		
	}
	@Override
	public int saveAccept(String userId, String replyId, String acceptId) {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap();
		map.put("userId", userId);
		map.put("replyId", replyId);
		map.put("acceptId", acceptId);
		return qAUserMapper.saveAccept(map);	
		
	}
	@Override
	public int saveThanks(String userId, String replyId, String thanksId) {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap();
		map.put("userId", userId);
		map.put("replyId", replyId);
		map.put("thanksId", thanksId);
		return qAUserMapper.saveThanks(map);	
	}
	@Override
	public List<Map<String, Object>> problemListCount(String userId,
			String courseName) {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap();
		map.put("courseName", courseName);
		map.put("userId", userId);
		return qAUserMapper.problemListCount(map);	
	}
	@Override
	public List<Map<String, Object>> queryReplyListCount(String problemId) {
		// TODO Auto-generated method stub
	
		 return qAUserMapper.queryReplyListCount(problemId);
	}
	@Override
	public List<Map<String, Object>> myProblemList(Integer pageNumber,
			String courseName, String userId, Integer pagesize) {
		Map<String, Object> map =new HashMap();
		map.put("pageNumber", pageNumber);
		map.put("courseName", courseName);
		map.put("userId", userId);
		map.put("pagesize", pagesize);
		return qAUserMapper.myProblemList(map);
	}
	@Override
	public List<Map<String, Object>> myProblemListCount(String userId,
			String courseName) {
		Map<String,String> map=new HashMap();
		map.put("courseName", courseName);
		map.put("userId", userId);
		return qAUserMapper.myProblemListCount(map);	
	}
	@Override
	public List<Map<String, Object>> myReplyList(Integer pageNumber,
			String courseName, String userId, Integer pagesize) {
		Map<String, Object> map =new HashMap();
		map.put("pageNumber", pageNumber);
		map.put("courseName", courseName);
		map.put("userId", userId);
		map.put("pagesize", pagesize);
		return qAUserMapper.myReplyList(map);
	}
	@Override
	public List<Map<String, Object>> myReplyListCount(String userId,
			String courseName) {
		Map<String,String> map=new HashMap();
		map.put("courseName", courseName);
		map.put("userId", userId);
		return qAUserMapper.myReplyListCount(map);	
	}
	@Override
	public List<Map<String, Object>> seekHelpCount(String problemId,
			String userId) {
		Map<String,String> map=new HashMap();
		map.put("problemId", problemId);
		map.put("userId", userId);
		return qAUserMapper.seekHelpCount(map);	
	}



	
}
