package com.leyikao.onlinelearn.serviceapp.v.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.leyikao.onlinelearn.serviceapp.v.mapper.AdmireMapper;
import com.leyikao.onlinelearn.serviceapp.v.mapper.ChapterMapper;
import com.leyikao.onlinelearn.serviceapp.v.mapper.CommentMapper;
import com.leyikao.onlinelearn.serviceapp.v.mapper.CourseMapper;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Admire;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Chapter;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Comment;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Course;

@Component
public class LearnService {
	
	@Resource
    private CourseMapper courseMapper; 
	@Resource 
	private ChapterMapper chapterMapper;
	@Resource
	private CommentMapper commentMapper;
	@Resource
	private AdmireMapper admireMapper;
	

	public Course getCourseById(String courseId,String userId) {
		return courseMapper.getCourseById(courseId,userId);
	}
	
	public List<Chapter> getChaptersByCourseId(String courseId){
		
		return chapterMapper.getChaptersByCourseId(courseId);
	}

	public List<Chapter> getChapterByParentId(Integer chapterId, String userId) {
		
		return chapterMapper.getChapterByParentId(chapterId,userId);
	}

	public List<Comment> getCommentsByCourseId(String courseId, int pageSize) {
		
		return commentMapper.getComments(courseId,pageSize);
	}

	public String addComment(String userId, String courseId, String text) {
		commentMapper.addComment(userId,courseId,text);
		return "";
	}

	public int getCommentCount(String courseId) {
		
		return commentMapper.getCommentCount(courseId);
	}

	public List<Comment> getCommentsPages(String courseId, String pageNum, String pageSize) {
		int pn = Integer.parseInt(pageNum);
		int ps = Integer.parseInt(pageSize);
		
		return commentMapper.getCommentPages(courseId,(pn-1)*ps,ps);
	}

	public String dianzan(String userId, String courseId) {
		List<Admire> list = admireMapper.checkAdmire(userId,courseId);
		if(list.size()==0){
			admireMapper.addAdmire(userId,courseId,1);
			return "1";
		}else{
			if(list.get(0).getZang()==1){
				admireMapper.updateAdmire(userId,courseId,0);
				return "0";
			}else {
				admireMapper.updateAdmire(userId,courseId,1);
				return "1";
			}
		}
	}


}
