package com.leyikao.onlinelearn.serviceapp.v.pojo;
/**
 * @author jack.lee
 *	观看进度 
 */
public class Schedule {
	
	private Integer scheduleId;	//id
	private String userId;		//用户 id
	private Integer courseId;	//课程 id
	private Integer chapterId;      //章节id 
	private Integer videoId;	//视频 id
	private Integer seen;		//已观看百分比(60,75,100)   %
	private Integer timelong;	//已观看时长	
	private Integer complete;	    //是否已经看完（0-否，1-是）
	
	
	public Schedule() {

	}

	public Schedule(Integer scheduleId, String userId, Integer courseId, Integer chapterId, Integer videoId, Integer seen, Integer timelong, Integer complete) {
		super();
		this.scheduleId = scheduleId;
		this.userId = userId;
		this.courseId = courseId;
		this.chapterId = chapterId;
		this.videoId = videoId;
		this.seen = seen;
		this.timelong = timelong;
		this.complete = complete;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	
	public Integer getChapterId() {
		return chapterId;
	}

	public void setChapterId(Integer chapterId) {
		this.chapterId = chapterId;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public Integer getVideoId() {
		return videoId;
	}
	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}
	public Integer getSeen() {
		return seen;
	}
	public void setSeen(Integer seen) {
		this.seen = seen;
	}
	public Integer getTimelong() {
		return timelong;
	}
	public void setTimelong(Integer timelong) {
		this.timelong = timelong;
	}
	public Integer getComplete() {
		return complete;
	}
	public void setComplete(Integer complete) {
		this.complete = complete;
	}
	
	

}
