package com.leyikao.onlinelearn.serviceapp.td.pojo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *	知识点的节点树 
 *
 */
public class KnowledgePointTree {
	private List<KnowledgePointNode> nodeList = new ArrayList<>();
	private Map<String, KnowledgePointNode> nodeMap = new HashMap<>();

	public List<KnowledgePointNode> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<KnowledgePointNode> nodeList) {
		this.nodeList = nodeList;
	}

	public Map<String, KnowledgePointNode> getNodeMap() {
		return nodeMap;
	}

	public void setNodeMap(Map<String, KnowledgePointNode> nodeMap) {
		this.nodeMap = nodeMap;
	}
	
	/**
	 * 排序 及 汇总上级节点的统计信息
	 */
	public void summaryUpperLayerNode(){
		Collections.sort(nodeList);
		nodeList.forEach(node -> summaryUpperLayerNode(node));
	}
	
	private void summaryUpperLayerNode(KnowledgePointNode node){
		if (node.getSubNodeList().size() != 0){
			List<KnowledgePointNode> subNodeList = node.getSubNodeList();
			Collections.sort(subNodeList);
			
			int questionNumber = node.getQuestionNumber();
			int answerNumber = node.getAnswerNumber();
			int rightNumber = node.getRightNumber();
			
			for (KnowledgePointNode subNode : subNodeList) {
				summaryUpperLayerNode(subNode);
				
				questionNumber += subNode.getQuestionNumber();
				answerNumber += subNode.getAnswerNumber(); 
				rightNumber += subNode.getRightNumber();
			};
			
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			double correctRateOfLastTime = 0;
			if (answerNumber > 0){
				correctRateOfLastTime = Double.parseDouble(decimalFormat.format(rightNumber / (answerNumber + 0.0)));
			}
			
			node.setQuestionNumber(questionNumber);
			node.setAnswerNumber(answerNumber);
			node.setRightNumber(rightNumber);
			node.setCorrectRateOfLastTime(correctRateOfLastTime);
		}
	}

}
