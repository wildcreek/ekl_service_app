package com.leyikao.onlinelearn.serviceapp.td.dao;

import java.util.List;
import java.util.Map;

public interface ICourseDao {
	
	/**
	 * 获取该考试类型的上级类型的所有课程信息
	 * @param examType
	 * @return
	 */
	public List<Map<String, String>> queryCourseInfo(String examType);
	
	public List<String> detailExamList(String parentExamType);
	
	public String examTime(String examType);
}
