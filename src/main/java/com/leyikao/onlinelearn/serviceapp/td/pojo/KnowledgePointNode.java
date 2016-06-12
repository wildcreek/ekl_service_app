package com.leyikao.onlinelearn.serviceapp.td.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;

/**
 * 在集合中按照order升序存放
 *
 */
public class KnowledgePointNode implements Comparable<KnowledgePointNode>,
		Serializable {
	private static final long serialVersionUID = -9016651076084422551L;
	
	@JsonBackReference
	private KnowledgePointNode parentNode;
	
	@JsonManagedReference
	private List<KnowledgePointNode> subNodeList = new ArrayList<>();

	@JsonIgnore
	private Integer orderId;

	private String name;
	private String id;
	private String descr;
	private int questionNumber;
	private int answerNumber;
	private int rightNumber;
	private double correctRateOfLastTime;

	public KnowledgePointNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(KnowledgePointNode parentNode) {
		this.parentNode = parentNode;
	}

	public List<KnowledgePointNode> getSubNodeList() {
		return subNodeList;
	}

	public void setSubNodeList(List<KnowledgePointNode> subNodeList) {
		this.subNodeList = subNodeList;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	public int getAnswerNumber() {
		return answerNumber;
	}

	public void setAnswerNumber(int answerNumber) {
		this.answerNumber = answerNumber;
	}

	public double getCorrectRateOfLastTime() {
		return correctRateOfLastTime;
	}

	public void setCorrectRateOfLastTime(double correctRateOfLastTime) {
		this.correctRateOfLastTime = correctRateOfLastTime;
	}

	public int getRightNumber() {
		return rightNumber;
	}

	public void setRightNumber(int rightNumber) {
		this.rightNumber = rightNumber;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@JsonIgnore
	public Set<String> getSubIdList() {
		Set<String> subIds = new HashSet<>();
		subKpIds(this, subIds);
		return subIds;
	}

	private void subKpIds(KnowledgePointNode node, Set<String> subIds) {
		subIds.add(node.getId());
		if (node.getSubNodeList().size() > 0) {
			node.getSubNodeList().forEach(item -> {
				subKpIds(item, subIds);
			});
		}
	}

	@Override
	public int compareTo(KnowledgePointNode o) {
		return this.getOrderId().compareTo(o.getOrderId());
	}

}
