package com.leyikao.onlinelearn.serviceapp.v.pojo;

import java.util.Date;
/**
 * @author jack.lee
 * 课程 pojo
 */
public class Course {
	
	private Integer courseId;		//	课程id	
	private String courseName;	    //  课程名称	
	private String photoUrl;  		//	图片url
	private Float  price;           //	价格
	private Integer timelong;       //	时长
	private String introduction; 	//课程介绍
	private char needpay ;		 	//是否收费课程
	
	private Integer admire;         //点亮 点赞图标 
	private Integer num;            // 学习人数 
	
	public Course() {
		
	}
	public Course(Integer courseId, String courseName, String photoUrl, Float price, Integer timelong, String introduction, char needpay) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.photoUrl = photoUrl;
		this.price = price;
		this.timelong = timelong;
		this.introduction = introduction;
		this.needpay = needpay;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Integer getTimelong() {
		return timelong;
	}
	public void setTimelong(Integer timelong) {
		this.timelong = timelong;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public char getNeedpay() {
		return needpay;
	}
	public void setNeedpay(char needpay) {
		this.needpay = needpay;
	}
	public Integer getAdmire() {
		return admire;
	}
	public void setAdmire(Integer admire) {
		this.admire = admire;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
	
}
