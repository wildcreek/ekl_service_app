package com.leyikao.onlinelearn.serviceapp.v.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leyikao.onlinelearn.serviceapp.util.ResourceHelper;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Chapter;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Comment;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Course;
import com.leyikao.onlinelearn.serviceapp.v.pojo.CourseChapterList;
import com.leyikao.onlinelearn.serviceapp.v.service.LearnService;

@Controller
@RequestMapping("/learn")
public class LearnController {
	
	private static int PAGE_SIZE = 10;
	@Resource 
	private LearnService learnService;
	
	@RequestMapping("/course")
	public String learnCourse(HttpServletRequest request, Model model){
		//String userId= request.getSession().getAttribute("userId").toString();
		String userId= request.getParameter("userId");
		String courseId = request.getParameter("courseId");
		Course course = learnService.getCourseById(courseId,userId);
		List<Chapter> list= learnService.getChaptersByCourseId(courseId);
		List<CourseChapterList> cclist= new ArrayList<CourseChapterList>();//章节列表

		for(int i=0;i<list.size();i++){
			CourseChapterList courseChapterList = new CourseChapterList();
			courseChapterList.setChapter(list.get(i));
			List<Chapter> sonlist= learnService.getChapterByParentId(list.get(i).getChapterId(),userId);
			courseChapterList.setList(sonlist);
			cclist.add(courseChapterList);
		}
		
		List<Comment> cmlist= learnService.getCommentsByCourseId(courseId,PAGE_SIZE);//评论列表 
		
		String stCommentTime="";
		Date now = new Date();
		for(int i=0, n=cmlist.size();i<n;i++){
			
			long times = now.getTime() - cmlist.get(i).getCommentTime().getTime();
			if(times<1000*60){
				stCommentTime= "刚刚";
			}else if(1000*60<=times&&times<1000*60*60){
				stCommentTime= times/(1000*60) +"分钟前";
			}else if(1000*60*60<=times&&times<1000*60*60*24){
				stCommentTime = times/(1000*60*60) +"小时前";
			}else if(1000*60*60*24<=times){
				stCommentTime = times/(1000*60*60*24)+"天前";
			}
			cmlist.get(i).setStCommentTime(stCommentTime);
			String basePath = ResourceHelper.getProperty("sc.image.service.web.path") + "/lykimg/";
			String picUrl =  (basePath + cmlist.get(i).getUser().getPicUrl());
			if(cmlist.get(i).getUser().getPicUrl()==null || "".equals(cmlist.get(i).getUser().getPicUrl())){
				cmlist.get(i).setUserPhoto("http://img.mukewang.com/user/55dc4b730001067001800180-80-80.jpg");
			}else{
				cmlist.get(i).setUserPhoto(picUrl);
			}
			
		}
		int comCount = learnService.getCommentCount(courseId); //评论总条数  
		model.addAttribute("course", course).
			  addAttribute("cclist", cclist).
			  addAttribute("cmlist", cmlist).
			  addAttribute("comCount", comCount).
			  addAttribute("userId",userId);

		return "learn";
	}
	
	@ResponseBody
	@RequestMapping("/addComment")
	public Map<String, Object> addComment(HttpServletRequest request){
		String text= request.getParameter("text")==null ? "":request.getParameter("text").toString();
		String courseId= request.getParameter("courseId") == null ? "" : request.getParameter("courseId").toString();
		//String userId= request.getSession().getAttribute("userId").toString();
		String userId = request.getParameter("userId");
		String flag= learnService.addComment(userId,courseId,text);
		int comCount = learnService.getCommentCount(courseId); //评论总条数  

		Map<String , Object> map= new HashMap<String ,Object>();
		map.put("success", "success");
		map.put("comCount", comCount);
		return map;
	}
	/**
	 * ajax 分页 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/commentPage")
	public Map<String ,Object> commentPage(HttpServletRequest request){
		String pageNum= request.getParameter("pageIndex")== null ?"" : request.getParameter("pageIndex");
		String pageSize = request.getParameter("pageSize")==null ? "" : request.getParameter("pageSize");
		String courseId = request.getParameter("courseId");
		List<Comment> cmlist= learnService.getCommentsPages(courseId,pageNum,pageSize);//评论列表 
		String stCommentTime="";
		Date now = new Date();
		for(int i=0, n=cmlist.size();i<n;i++){
			
			long times = now.getTime() - cmlist.get(i).getCommentTime().getTime();
			if(times<1000*60){
				stCommentTime= "刚刚发布";
			}else if(1000*60<=times&&times<1000*60*60){
				stCommentTime= times/(1000*60) +"分钟前";
			}else if(1000*60*60<=times&&times<1000*60*60*24){
				stCommentTime = times/(1000*60*60) +"小时前";
			}else if(1000*60*60*24<=times){
				stCommentTime = times/(1000*60*60*24)+"天前";
			}
			cmlist.get(i).setStCommentTime(stCommentTime);
			String basePath = ResourceHelper.getProperty("sc.image.service.web.path") + "/lykimg/";
			String picUrl =  (basePath + cmlist.get(i).getUser().getPicUrl());
			if(cmlist.get(i).getUser().getPicUrl()==null || "".equals(cmlist.get(i).getUser().getPicUrl())){
				cmlist.get(i).setUserPhoto("http://img.mukewang.com/user/55dc4b730001067001800180-80-80.jpg");
			}else{
				cmlist.get(i).setUserPhoto(picUrl);
			}

		}
		Map<String , Object> map= new HashMap<String ,Object>();
		map.put("success", "success");
		map.put("cmlist", cmlist);
		return map;
	}
	@ResponseBody
	@RequestMapping("dianzan")
	public Map<String ,Object> dianzan(HttpServletRequest request){
		//String userId= request.getSession().getAttribute("userId").toString();
		String userId = request.getParameter("userId");
		String courseId= request.getParameter("courseId");
		String flag = learnService.dianzan(userId,courseId);
		Map<String ,Object> map =new HashMap<String ,Object>();
		map.put("success", flag);
		return map ;
	}
}
