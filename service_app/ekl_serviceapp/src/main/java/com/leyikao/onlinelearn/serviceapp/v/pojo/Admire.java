package com.leyikao.onlinelearn.serviceapp.v.pojo;

import java.sql.Timestamp;
import java.util.Date;
/**
 * @author jack.lee
 * 点赞 pojo
 */
public class Admire {
	
	private Integer id ;		//	id
	private Integer courseId ;	//	课程id	
	private String userId ;	//	评论人id	
	private Integer zang ;			//	0-踩 ， 1-顶 	
	private Timestamp time ;			//	点赞时间	
	
	public Admire() {

	}
	public Admire(Integer id, Integer courseId, String userId, Integer zang, Timestamp time) {

		this.id = id;
		this.courseId = courseId;
		this.userId = userId;
		this.zang = zang;
		this.time = time;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getZang() {
		return zang;
	}
	public void setZang(Integer zang) {
		this.zang = zang;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}

}
