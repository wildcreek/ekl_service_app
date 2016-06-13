package com.leyikao.onlinelearn.serviceapp.v.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.leyikao.onlinelearn.serviceapp.v.pojo.Schedule;

public interface ScheduleMapper {
	void saveSchem(@Param("userId") String userId, @Param("chapterId") String chapterId,
				   @Param("videoId") String videoId, @Param("flag") String flag, @Param("courseId") String courseId);

	List<Schedule> userHasSchme(@Param("userId") String userId, @Param("chapterId") String chapterId,
								@Param("videoId") String videoId, @Param("courseId") String courseId);

	void updateSchem(@Param("scheduleId") Integer scheduleId, @Param("flag") String flag);

}
