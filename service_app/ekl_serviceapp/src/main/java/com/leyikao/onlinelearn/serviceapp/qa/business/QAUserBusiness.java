package com.leyikao.onlinelearn.serviceapp.qa.business;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.leyikao.onlinelearn.serviceapp.qa.dao.IQAUserDao;
import com.leyikao.onlinelearn.serviceapp.util.MessageSourceHelper;
import com.leyikao.onlinelearn.serviceapp.util.ResourceHelper;
import com.leyikao.onlinelearn.serviceapp.util.Utils;

/*
 * 问答业务处理类
 */
public class QAUserBusiness {
	@Autowired
	private IQAUserDao qaUserDao;
	@Autowired
	private MessageSourceHelper messageSourceHelper;

	public List<String> qureyCourseList() {
		// TODO Auto-generated method stub
		List<String> courseList = qaUserDao.qureyCourseList();
		return courseList;
	}

	public List<Map<String, Object>> problemList(String userId,String pageNumber,
			String courseName) {
		String pagesize = ResourceHelper.getProperty("sc.page.size");

		List<Map<String, Object>> problemList = qaUserDao.problemList(userId,
				(Integer.parseInt(pageNumber)-1)*Integer.parseInt(pagesize), courseName,Integer.parseInt(pagesize));

		for (Map<String, Object> map : problemList) {
			String problemId = map.get("problemId").toString();
			List<Map<String, Object>> mapcountlist = qaUserDao
					.qureyCoutInfo(problemId);
			Map<String, Object> mapcount=mapcountlist.get(0);
			
			List<Map<String, Object>> maplist=qaUserDao.seekHelpCount(problemId,userId);
			map.put("isseek", Integer.parseInt(maplist.get(0).get("seekhelpcount").toString())>0?"1":"0")	;	
			map.put("picUrl", Utils.getUserWebImgUrl(map.get("picUrl").toString()));
			map.put("problemPicUrl", Utils.getWebImgUrl(map.get("problemPicUrl").toString()));
			map.put("createTime", Utils.getSubDateString((Timestamp)map.get("createTime")));
			for(Map.Entry<String, Object> entry:mapcount.entrySet()){
				map.put(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
		return problemList;
	}

	public  Map<String, String> uploadProblem(String userId, String courseName, String picUrl,String questionDescr) {
		String problemId = UUID.randomUUID().toString();
	/*	String questionDescr = messageSourceHelper
				.getMessage("td_problemdescr_" + (new Random().nextInt(4) + 1));*/

		int tag = qaUserDao.saveProblem(userId, courseName, picUrl, problemId,
				questionDescr);
		 Map<String, String> map=new HashMap();
		if(tag>0){
			map.put("problemId", problemId);
			map.put("publishTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString());
		}
		// TODO Auto-generated method stub
		return map;
	}

	public Map<String, String> uploadReply(String userId, String problemId,
			String quoteReplyId, String replyDescr, String userImagePathName) {
		String replyid = UUID.randomUUID().toString();
		int tag = qaUserDao.saveReply(userId, replyid, problemId, quoteReplyId,
				replyDescr, userImagePathName);
		Map<String, String> replyrep = new HashMap();
		if (tag > 0) {

			Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createTime = format.format(new Date()).toString();
			replyrep.put("replyId", replyid);
			replyrep.put("publishTime", createTime);
		}
		// TODO Auto-generated method stub
		return replyrep;
	}

	public List<Map<String, Object>> replyList(String pageNumber,
			String problemId) {
		// TODO Auto-generated method stub
		String pagesize = ResourceHelper.getProperty("sc.page.size");
		List<Map<String, Object>> maplist=qaUserDao.queryReplyList((Integer.parseInt(pageNumber)-1)*Integer.parseInt(pagesize), problemId,Integer.parseInt(pagesize));
		for(Map<String, Object> map:maplist){
			map.put("replyPicUrl", Utils.getWebImgUrl(map.get("replyPicUrl")));
			map.put("userPicUrl", Utils.getUserWebImgUrl(map.get("userPicUrl")));			
			map.put("isAccept", map.get("acceptId")==null?"0":"1");
			map.put("isShare", map.get("thanksId")==null?"0":"1");
			map.put("publishTime", Utils.getSubDateString((Timestamp)map.get("publishTime")));
		}
		Collections.sort(maplist,new Comparator<Map<String,Object>>(){//排序
            public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {
//            第一次比较专业
               return arg1.get("isAccept").toString().compareTo(arg0.get("isAccept").toString());

            }
        });
		return  maplist;
	}

	public List<Map<String, Object>> questionCloselyList(String replyid) {
		// TODO Auto-generated method stub
		 List<Map<String, Object>> listmap=qaUserDao.questionCloseList(replyid);
		 for(Map<String, Object> map:listmap){
				map.put("userPicUrl", Utils.getUserWebImgUrl(map.get("userPicUrl")));
				map.put("replyPicUrl", Utils.getWebImgUrl(map.get("replyPicUrl")));
				map.put("createTime", Utils.getSubDateString((Timestamp)map.get("createTime")));

		 }
		return listmap;
	}

	public List<Map<String, Object>> myProblemList(String userId,
			String pageNumber, String courseName) {
		// TODO Auto-generated method stub
		String pagesize = ResourceHelper.getProperty("sc.page.size");
		List<Map<String, Object>> problemList = qaUserDao.myProblemList(
				(Integer.parseInt(pageNumber)-1)*Integer.parseInt(pagesize), courseName,userId,Integer.parseInt(pagesize));

		for (Map<String, Object> map : problemList) {
			String problemId = map.get("problemId").toString();
			List<Map<String, Object>> mapcountlist = qaUserDao
					.qureyCoutInfo(problemId);
			Map<String, Object> mapcount=mapcountlist.get(0);
		
			map.put("picUrl", Utils.getUserWebImgUrl(map.get("picUrl").toString()));
			map.put("problemPicUrl", Utils.getWebImgUrl(map.get("problemPicUrl").toString()));
			map.put("createTime", Utils.getSubDateString((Timestamp)map.get("createTime")));

			List<Map<String, Object>> maplist=qaUserDao.seekHelpCount(problemId,userId);
			map.put("isseek", Integer.parseInt(maplist.get(0).get("seekhelpcount").toString())>0?"1":"0")	;
			for(Map.Entry<String, Object> entry:mapcount.entrySet()){
				map.put(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
		return problemList;

	}

	public String seekhelpAnswer(String userId, String problemId) {
		// TODO Auto-generated method stub
		String seekhelpId = UUID.randomUUID().toString();

		qaUserDao.saveSeekhelp(	userId, problemId,seekhelpId);
		List<Map<String, Object>> mapcount = qaUserDao
				.qureyCoutInfo(problemId);
		
		return String.valueOf(mapcount.get(0).get("seekhelpTimes"));
	}

	public void acceptReply(String userId, String replyId) {
		// TODO Auto-generated method stub
		String acceptId = UUID.randomUUID().toString();

		qaUserDao.saveAccept(userId,replyId,acceptId);
		
	}

	public void thanksReply(String userId, String replyId) {
		// TODO Auto-generated method stub
		String thanksId = UUID.randomUUID().toString();
		qaUserDao.saveThanks(userId,replyId,thanksId);
		
	}
	/*
	 * 得到未解决列表总页数
	 */
	public String problemListCount(String userId,String pageNumber,
			String courseName) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> problemListcount=qaUserDao.problemListCount(userId, courseName);
		return String.valueOf(problemListcount.get(0).get("problemcount"));
	}

	public String replyListCount(String problemId) {
		// TODO Auto-generated method stub
		List maplist=qaUserDao.queryReplyListCount( problemId);

		return maplist.get(0).toString();
	}

	public String myProblemListCount(String userId, String courseName) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> problemListcount=qaUserDao.myProblemListCount(userId, courseName);
		return String.valueOf(problemListcount.get(0).get("problemcount"));
	}

	public List<Map<String, Object>> myReplyList(String userId, String pageNumber,
			String courseName) {
		// TODO Auto-generated method stub
		String pagesize = ResourceHelper.getProperty("sc.page.size");
		List<Map<String, Object>> problemList = qaUserDao.myReplyList(
				(Integer.parseInt(pageNumber)-1)*Integer.parseInt(pagesize), courseName,userId,Integer.parseInt(pagesize));

		for (Map<String, Object> map : problemList) {
			String problemId = map.get("problemId").toString();
			List<Map<String, Object>> mapcountlist = qaUserDao
					.qureyCoutInfo(problemId);
			Map<String, Object> mapcount=mapcountlist.get(0);
			List<Map<String, Object>> maplist=qaUserDao.seekHelpCount(problemId,userId);
			map.put("isseek", Integer.parseInt(maplist.get(0).get("seekhelpcount").toString())>0?"1":"0")	;
			map.put("picUrl", Utils.getUserWebImgUrl(map.get("picUrl")));
			map.put("problemPicUrl", Utils.getWebImgUrl(map.get("problemPicUrl")));
			map.put("createTime", Utils.getSubDateString((Timestamp)map.get("createTime")));

			for(Map.Entry<String, Object> entry:mapcount.entrySet()){
				map.put(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
		return problemList;
	}

	public String myReplyListCount(String userId, String courseName) {
		List<Map<String, Object>> problemListcount=qaUserDao.myReplyListCount(userId, courseName);
		return String.valueOf(problemListcount.get(0).get("problemcount"));
	}

}
