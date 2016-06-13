package com.leyikao.onlinelearn.serviceapp.v.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.leyikao.onlinelearn.serviceapp.v.pojo.Admire;

public interface AdmireMapper {

	void addAdmire(@Param("userId") String userId, @Param("courseId") String courseId, @Param("flag") int flag);

	List<Admire> checkAdmire(@Param("userId") String userId, @Param("courseId") String courseId);

	void updateAdmire(@Param("userId") String userId, @Param("courseId") String courseId, @Param("flag") int flag);
	
	

}
