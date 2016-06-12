package com.leyikao.onlinelearn.serviceapp.v.mapper;

import com.leyikao.onlinelearn.serviceapp.v.pojo.CourseType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseTypeMapper {

	public List<CourseType> getCourseTypeList();
	
}
