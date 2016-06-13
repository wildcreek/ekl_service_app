package com.leyikao.onlinelearn.serviceapp.v.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.leyikao.onlinelearn.serviceapp.v.pojo.Comment;

public interface CommentMapper {
	
	public List<Comment> getComments(@Param("courseId") String courseId, @Param("pageSize") int pageSize);

	public void addComment(@Param("userId") String userId, @Param("courseId") String courseId, @Param("text") String text);

	public int getCommentCount(@Param("courseId") String courseId);

	public List<Comment> getCommentPages(@Param("courseId") String courseId, @Param("st") int st, @Param("ps") int ps);

}
