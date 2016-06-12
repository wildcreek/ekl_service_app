package com.leyikao.onlinelearn.serviceapp.td.pojo;

import java.io.Serializable;

/**
 * 答题信息
 */
public class AnswerResultInfo implements Comparable<AnswerResultInfo>, Serializable {
	private static final long serialVersionUID = 3865847163228793041L;
	private String userId;
	private String knowledgePointId;
	private String questionId;
	private boolean isCorrectAnswer;
	private String lastModifyTime;

	public AnswerResultInfo() {
		super();
	}

	public AnswerResultInfo(String userId, String knowledgePointId,
			String questionId, boolean isCorrectAnswer, String lastModifyTime) {
		super();
		this.userId = userId;
		this.knowledgePointId = knowledgePointId;
		this.questionId = questionId;
		this.isCorrectAnswer = isCorrectAnswer;
		this.lastModifyTime = lastModifyTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getKnowledgePointId() {
		return knowledgePointId;
	}

	public void setKnowledgePointId(String knowledgePointId) {
		this.knowledgePointId = knowledgePointId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public boolean getIsCorrectAnswer() {
		return isCorrectAnswer;
	}

	public void setIsCorrectAnswer(boolean isCorrectAnswer) {
		this.isCorrectAnswer = isCorrectAnswer;
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	@Override
	public int compareTo(AnswerResultInfo o) {
		return this.lastModifyTime.compareTo(o.lastModifyTime);
	}
}
