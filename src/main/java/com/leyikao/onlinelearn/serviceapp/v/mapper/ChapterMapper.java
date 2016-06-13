package com.leyikao.onlinelearn.serviceapp.v.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.leyikao.onlinelearn.serviceapp.v.pojo.Chapter;

public interface ChapterMapper {
	
	public List<Chapter> getChaptersByCourseId(@Param("courseId") String courseId);

	public List<Chapter> getChapterByParentId(@Param("chapterId") Integer chapterId, @Param("userId") String userId);

}
