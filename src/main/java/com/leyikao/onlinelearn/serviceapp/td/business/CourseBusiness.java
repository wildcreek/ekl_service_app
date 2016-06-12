package com.leyikao.onlinelearn.serviceapp.td.business;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.leyikao.onlinelearn.serviceapp.td.dao.ICourseDao;
import com.leyikao.onlinelearn.serviceapp.util.Utils;


public class CourseBusiness {
	
	@Autowired
	private ICourseDao courseDao;
	
	public long examDeadlineDays(String examType){
		long examDeadlineDays = -1; 
		String examTime = courseDao.examTime(examType);
		if (!StringUtils.isEmpty(examTime)){
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utils.DateParttern.DATE_TIME);
			LocalDate deadlineTime = LocalDate.parse(examTime, formatter);
			long days =  deadlineTime.toEpochDay() - LocalDate.now().toEpochDay();
			if (days >= 0){
				examDeadlineDays = days;
			}
		}
		
		return examDeadlineDays;
	}
}