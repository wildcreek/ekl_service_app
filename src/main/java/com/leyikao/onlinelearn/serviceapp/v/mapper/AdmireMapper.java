package com.leyikao.onlinelearn.serviceapp.v.mapper;

import com.leyikao.onlinelearn.serviceapp.v.pojo.Admire;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmireMapper {

	void addAdmire(@Param("userId") String userId, @Param("courseId") String courseId, @Param("flag") int flag);

	List<Admire> checkAdmire(@Param("userId") String userId, @Param("courseId") String courseId);

	void updateAdmire(@Param("userId") String userId, @Param("courseId") String courseId, @Param("flag") int flag);
	
	

}
