package com.leyikao.onlinelearn.serviceapp.v.pojo;
/**
 * @author jack.lee
 * 章节pojo
 */
public class Chapter {
	
	private Integer chapterId;	 // 章节id	
	private Integer courseId;	 //	课程名称	
	private Integer chapterNum;  //	章节号 排序
	private String chapterName; //	章节名称
	private Integer parentChapterId;  //父章节 
	
	private Integer schedule ;   //章节阅读状态   0 - 未读， 1 已读 ， 2 已完成

	public Integer getChapterId() {
		return chapterId;
	}
	public void setChapterId(Integer chapterId) {
		this.chapterId = chapterId;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public Integer getChapterNum() {
		return chapterNum;
	}
	public void setChapterNum(Integer chapterNum) {
		this.chapterNum = chapterNum;
	}
	public String getChapterName() {
		return chapterName;
	}
	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}
	public Integer getParentChapterId() {
		return parentChapterId;
	}
	public void setParentChapterId(Integer parentChapterId) {
		this.parentChapterId = parentChapterId;
	}
	public Integer getSchedule() {
		return schedule;
	}
	public void setSchedule(Integer schedule) {
		this.schedule = schedule;
	}


}
