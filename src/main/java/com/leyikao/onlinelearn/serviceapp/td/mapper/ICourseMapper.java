package com.leyikao.onlinelearn.serviceapp.td.mapper;

import java.util.List;
import java.util.Map;

public interface ICourseMapper {
	public List<Map<String, String>> queryCourseInfo(String examType);
	public List<String> detailExamList(String parentExamType);
	public List<String> examTime(String examType);
}
