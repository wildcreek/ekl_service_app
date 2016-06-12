package com.leyikao.onlinelearn.serviceapp.v.mapper;

import com.leyikao.onlinelearn.serviceapp.v.pojo.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
	
	public List<Comment> getComments(@Param("courseId") String courseId, @Param("pageSize") int pageSize);

	public void addComment(@Param("userId") String userId, @Param("courseId") String courseId, @Param("text") String text);

	public int getCommentCount(@Param("courseId") String courseId);

	public List<Comment> getCommentPages(@Param("courseId") String courseId, @Param("st") int st, @Param("ps") int ps);

}
