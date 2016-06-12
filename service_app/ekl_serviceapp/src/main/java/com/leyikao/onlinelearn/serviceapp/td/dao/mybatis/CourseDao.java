package com.leyikao.onlinelearn.serviceapp.td.dao.mybatis;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.leyikao.onlinelearn.serviceapp.td.dao.ICourseDao;
import com.leyikao.onlinelearn.serviceapp.td.mapper.ICourseMapper;

public class CourseDao implements ICourseDao{
	
	@Resource
	private ICourseMapper iCourseMapper;

	/**
	 * 根据考试类型获取它对应的上级考试类型的所有课程信息
	 */
	@Override
	public List<Map<String, String>> queryCourseInfo(String examType) {
		return iCourseMapper.queryCourseInfo(examType);
	}

	@Override
	public List<String> detailExamList(String parentExamType) {
		return iCourseMapper.detailExamList(parentExamType);
	}

	@Override
	public String examTime(String examType) {
		String examTime = "";
		List<String> examList = iCourseMapper.examTime(examType);
		if (examList != null && examList.size() > 0){
			examTime = examList.get(0);
		}
		return examTime;
	}
	
}
