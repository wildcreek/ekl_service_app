package com.leyikao.onlinelearn.serviceapp.v.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.leyikao.onlinelearn.serviceapp.v.pojo.Course;

public interface CourseMapper {
	
	public List<Course> getCourseList(@Param("ps") int ps, @Param("courseType") String courseType);

	public Course getCourseById(@Param("courseId") String courseId, @Param("userId") String userId);

	public List<Course> getCoursePageList(@Param("ps") int ps, @Param("courseType") String courseType, @Param("pn") int pn);

}
