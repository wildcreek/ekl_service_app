package com.leyikao.onlinelearn.serviceapp.v.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.leyikao.onlinelearn.serviceapp.v.mapper.CourseMapper;
import com.leyikao.onlinelearn.serviceapp.v.mapper.CourseTypeMapper;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Course;
import com.leyikao.onlinelearn.serviceapp.v.pojo.CourseType;

@Component
public class IndexService {
	
	@Resource
	private CourseMapper courseMapper;
	
	@Resource
	private CourseTypeMapper courseTypeMapper;
	
	public List<Course> getCourseList(int ps, String courseType){
		
		return courseMapper.getCourseList(ps ,courseType);
	}

	public List<CourseType> getCourseTypeList() {
		
		return courseTypeMapper.getCourseTypeList();
	}

	public List<Course> getCoursePageList(int ps, String courseType, String pageNum) {
		int pn = Integer.parseInt(pageNum);
		return courseMapper.getCoursePageList(ps,courseType,(pn-1)*ps);
	}
}
