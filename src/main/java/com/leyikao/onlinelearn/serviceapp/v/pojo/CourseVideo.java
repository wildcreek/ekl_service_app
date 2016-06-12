package com.leyikao.onlinelearn.serviceapp.v.pojo;

/**
 * @author jack.lee
 * 课程  视频关联 pojo
 */
public class CourseVideo {
	
	private Integer id;			//  id
	private Integer courseId;	//  课程id
	private Integer chapterId;	//	图片url
	private Integer videoId;	//	价格	
	private Integer videoNum;	//	视频在每章的排序号（1， 2 ，3）
	private String introduction;//  课程介绍

	public CourseVideo() {

	}
	public CourseVideo(Integer id, Integer courseId, Integer chapterId, Integer videoId, Integer videoNum, String introduction) {
		this.id = id;
		this.courseId = courseId;
		this.chapterId = chapterId;
		this.videoId = videoId;
		this.videoNum = videoNum;
		this.introduction = introduction;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public Integer getChapterId() {
		return chapterId;
	}
	public void setChapterId(Integer chapterId) {
		this.chapterId = chapterId;
	}
	public Integer getVideoId() {
		return videoId;
	}
	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}
	public Integer getVideoNum() {
		return videoNum;
	}
	public void setVideoNum(Integer videoNum) {
		this.videoNum = videoNum;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

}
