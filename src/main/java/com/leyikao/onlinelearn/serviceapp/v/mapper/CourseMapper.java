package com.leyikao.onlinelearn.serviceapp.v.mapper;

import com.leyikao.onlinelearn.serviceapp.v.pojo.Course;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseMapper {
	
	public List<Course> getCourseList(@Param("ps") int ps, @Param("courseType") String courseType);

	public Course getCourseById(@Param("courseId") String courseId, @Param("userId") String userId);

	public List<Course> getCoursePageList(@Param("ps") int ps, @Param("courseType") String courseType, @Param("pn") int pn);

}
