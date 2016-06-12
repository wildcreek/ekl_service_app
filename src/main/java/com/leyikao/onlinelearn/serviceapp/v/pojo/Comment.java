package com.leyikao.onlinelearn.serviceapp.v.pojo;

import java.sql.Timestamp;
import java.util.Date;
/**
 * @author jack.lee
 *	评论 pojo
 */
public class Comment {
	
	private Integer id;			//  id
	private Integer courseId;	//  程id	
	private User user;	//	评论人id	
	private String content;		//	评论内容	
	private Integer commentNum;//	评论顺序
	private Timestamp commentTime;	//	评论时间
	private char hidden;		//	是否匿名 （0-否，1-是）
	private String stCommentTime; //评论时间  
	private String userPhoto;   //评论人图片地址 
	
	public Comment() {

	}
	public Comment(Integer id, Integer courseId, User user, String content, Integer commentNum, Timestamp commentTime, char hidden) {

		this.id = id;
		this.courseId = courseId;
		this.user = user;
		this.content = content;
		this.commentNum = commentNum;
		this.commentTime = commentTime;
		this.hidden = hidden;
	}
	
	
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}
	public Date getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Timestamp commentTime) {
		this.commentTime = commentTime;
	}
	public char getHidden() {
		return hidden;
	}
	public void setHidden(char hidden) {
		this.hidden = hidden;
	}
	public String getStCommentTime() {
		return stCommentTime;
	}
	public void setStCommentTime(String stCommentTime) {
		this.stCommentTime = stCommentTime;
	}
	

}
