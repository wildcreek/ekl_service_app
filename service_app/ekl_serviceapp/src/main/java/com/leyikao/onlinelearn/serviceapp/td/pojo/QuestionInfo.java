package com.leyikao.onlinelearn.serviceapp.td.pojo;

import java.io.Serializable;

public class QuestionInfo implements Comparable<QuestionInfo>, Serializable {
	private static final long serialVersionUID = 7353576093056639872L;
	private String questionId;
	private String questionType;
	private String description;
	private String picUrl;
	private String orderId;
	private String knowledgePointId;
	private String knowledgePointName;

	public QuestionInfo() {
		super();
	}

	public QuestionInfo(String questionId, String questionType,
			String description, String picUrl, String orderId,
			String knowledgePointId, String knowledgePointName) {
		super();
		this.questionId = questionId;
		this.questionType = questionType;
		this.description = description;
		this.picUrl = picUrl;
		this.orderId = orderId;
		this.knowledgePointId = knowledgePointId;
		this.knowledgePointName = knowledgePointName;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getKnowledgePointId() {
		return knowledgePointId;
	}

	public void setKnowledgePointId(String knowledgePointId) {
		this.knowledgePointId = knowledgePointId;
	}

	public String getKnowledgePointName() {
		return knowledgePointName;
	}

	public void setKnowledgePointName(String knowledgePointName) {
		this.knowledgePointName = knowledgePointName;
	}

	@Override
	public int compareTo(QuestionInfo o) {
		if (o == null) {
			return 0;
		}
		return this.orderId.compareTo(o.orderId) > 0 ? 1 : 0;
	}
}
