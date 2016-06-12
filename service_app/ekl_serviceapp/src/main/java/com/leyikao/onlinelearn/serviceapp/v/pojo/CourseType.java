package com.leyikao.onlinelearn.serviceapp.v.pojo;
/**
 * @author jack.lee
 * 课程类型pojo
 */
public class CourseType {
	
	private Integer id;	  		//类型id
	private String typeCode;	//类型编码（关联课程，用此字段）
	private String typeName;	//类型名
	private Integer typeNum;	//类型排序字段
	private String ptoUrl;      //类型图片呢URL
	
	private Integer courseNum;  //包含课程数
	
	public CourseType() {
	}

	public CourseType(Integer id, String typeCode, String typeName, Integer typeNum) {

		this.id = id;
		this.typeCode = typeCode;
		this.typeName = typeName;
		this.typeNum = typeNum;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getTypeNum() {
		return typeNum;
	}

	public void setTypeNum(Integer typeNum) {
		this.typeNum = typeNum;
	}

	public Integer getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(Integer courseNum) {
		this.courseNum = courseNum;
	}

	public String getPtoUrl() {
		return ptoUrl;
	}

	public void setPtoUrl(String ptoUrl) {
		this.ptoUrl = ptoUrl;
	}

	
	
}
