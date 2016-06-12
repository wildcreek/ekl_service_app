package com.leyikao.onlinelearn.serviceapp.v.pojo;

import java.util.Date;

public class User {
	
	private Integer id; 
	private String userId; 
	private String thirdPartyLoginId; 	// 第三方登录唯一标识
	private Integer thirdPartyLoginType;//第三方登录类型：1- QQ 2- 微博 3- 微信
	private String mobilePhone; 			//手机号
	private String password; 			// 密码
	private Integer userType;			// 用户类型：1-老师  2-学生
	private String  userName ; 			//姓名
	private Integer age; 				//年龄
	private String nickname; 			//呢称
	private Integer sex;  				//性别：1-男  2-女
	private String schoolName;          //学校名称
	private String major ;              //专业
	private String picUrl;              //用户自定义头像url
	private Integer totalPoint ;        //总积分
	private String currentExamType;     //当前关注的考试类型
	private String email;               //邮箱
	private Date createTime;            //该用户的创建时间

	public User() {

	}
	public User(Integer id, String userId, String thirdPartyLoginId, Integer thirdPartyLoginType, String mobilePhone, String password, Integer userType, String userName, Integer age, String nickname, Integer sex, String schoolName, String major, String picUrl, Integer totalPoint, String currentExamType, String email, Date createTime) {

		this.id = id;
		this.userId = userId;
		this.thirdPartyLoginId = thirdPartyLoginId;
		this.thirdPartyLoginType = thirdPartyLoginType;
		this.mobilePhone = mobilePhone;
		this.password = password;
		this.userType = userType;
		this.userName = userName;
		this.age = age;
		this.nickname = nickname;
		this.sex = sex;
		this.schoolName = schoolName;
		this.major = major;
		this.picUrl = picUrl;
		this.totalPoint = totalPoint;
		this.currentExamType = currentExamType;
		this.email = email;
		this.createTime = createTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getThirdPartyLoginId() {
		return thirdPartyLoginId;
	}
	public void setThirdPartyLoginId(String thirdPartyLoginId) {
		this.thirdPartyLoginId = thirdPartyLoginId;
	}
	public Integer getThirdPartyLoginType() {
		return thirdPartyLoginType;
	}
	public void setThirdPartyLoginType(Integer thirdPartyLoginType) {
		this.thirdPartyLoginType = thirdPartyLoginType;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public Integer getTotalPoint() {
		return totalPoint;
	}
	public void setTotalPoint(Integer totalPoint) {
		this.totalPoint = totalPoint;
	}
	public String getCurrentExamType() {
		return currentExamType;
	}
	public void setCurrentExamType(String currentExamType) {
		this.currentExamType = currentExamType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	  
	

}
