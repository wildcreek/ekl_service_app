package com.leyikao.onlinelearn.serviceapp.v.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.leyikao.onlinelearn.serviceapp.v.mapper.ScheduleMapper;
import com.leyikao.onlinelearn.serviceapp.v.mapper.VideoMapper;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Schedule;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Video;

@Component
public class VideoService {
	
	@Resource
	private VideoMapper videoMapper;
	@Resource
	private ScheduleMapper scheduleMapper;
	
	public String playVideo(){
		
		return null;
	}

	public Video getVideoBychapterId(String chapterId) {
		
		return videoMapper.getVideoBychapterId(chapterId);
	}

	public void saveSchem(String userId, String chapterId,String videoId, String flag, String courseId) {
		List<Schedule> list = scheduleMapper.userHasSchme(userId,chapterId,videoId,courseId); 
		if(list.size()==0){
			if(flag.equals("0")){
				scheduleMapper.saveSchem(userId,chapterId,videoId,flag,courseId);
			}
		}else if(list.size()==1){
			if(flag.equals("1")){
				scheduleMapper.updateSchem(list.get(0).getScheduleId(),flag);
			}
		}
		
	}

}
