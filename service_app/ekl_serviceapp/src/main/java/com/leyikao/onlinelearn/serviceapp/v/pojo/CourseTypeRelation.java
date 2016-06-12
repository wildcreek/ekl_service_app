package com.leyikao.onlinelearn.serviceapp.v.pojo;

public class CourseTypeRelation {
	
	private Integer id;			// id	
	private Integer courseId;	//	课程id
	private String typeCode;	//	类型编码

	public CourseTypeRelation() {

	}
	public CourseTypeRelation(Integer id, Integer courseId, String typeCode) {
		this.id = id;
		this.courseId = courseId;
		this.typeCode = typeCode;
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
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

}
