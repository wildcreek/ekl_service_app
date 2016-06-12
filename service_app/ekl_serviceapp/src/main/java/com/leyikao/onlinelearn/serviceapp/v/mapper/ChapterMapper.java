package com.leyikao.onlinelearn.serviceapp.v.mapper;

import com.leyikao.onlinelearn.serviceapp.v.pojo.Chapter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterMapper {
	
	public List<Chapter> getChaptersByCourseId(@Param("courseId") String courseId);

	public List<Chapter> getChapterByParentId(@Param("chapterId") Integer chapterId, @Param("userId") String userId);

}
