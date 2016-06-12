package com.leyikao.onlinelearn.serviceapp.td.business;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.leyikao.onlinelearn.serviceapp.td.dao.IKnowledgePointDao;
import com.leyikao.onlinelearn.serviceapp.td.pojo.KnowledgePointNode;
import com.leyikao.onlinelearn.serviceapp.td.pojo.KnowledgePointTree;

public class KnowledgePointBusiness {
	
	@Autowired
	private IKnowledgePointDao kpDao;
	
	/**
	 * 获取指定知识点的所有知识点 且 包括它本身
	 * @param kpId
	 * @return
	 */
	public Set<String> subKnowledgePointIds(String kpId){
		Set<String> subIds = new HashSet<String>();
		subKpIds(kpDao.queryAll(), subIds, kpId);
		return subIds;
	}
	
	/**
	 * 根据指定的叶子知识点id构建节点列表(即部分的知识点树)
	 * @param kpIdList
	 * @return
	 */
	public KnowledgePointTree buildNodeList(Collection<String> kpIdList){
		
		KnowledgePointTree kpTree = new KnowledgePointTree();
		
		Map<String, Map<String, String>> kpMap = chooseKpIds(kpDao.queryAll(), kpIdList);
		
		kpMap.entrySet().forEach(kpEntry -> {
			// 从没有上级Id的知识点开始遍历，即先根遍历
			Map<String, String> kpInfo = kpEntry.getValue();
			String parentId = kpInfo.get("parentKnowledgePointId");
			if (StringUtils.isEmpty(parentId)){
				KnowledgePointNode node = new KnowledgePointNode();
				
				String name = kpInfo.get("knowledgePointName");
				node.setName(name);
				
				String kpId = kpInfo.get("knowledgePointId");
				node.setId(kpId);
				
				String descr = kpInfo.get("knowledgePointDescr");
				node.setDescr(descr);
				
				int orderId = Integer.parseInt(kpInfo.get("orderId"));
				node.setOrderId(orderId);
				
				kpTree.getNodeList().add(node);
				kpTree.getNodeMap().put(kpId, node);
				
				buildNextLevelNodes(kpMap, kpTree.getNodeMap(), kpId, node);
			}
		});
		
		return kpTree;
	}
	
	public Map<String, String> getInfo(String knowledgePointId){
		return kpDao.queryAll().get(knowledgePointId);
	}
	
	/**
	 * 构建下级知识点信息
	 * @param kpMap
	 * @param kpId
	 * @param node
	 */
	private void buildNextLevelNodes(Map<String, Map<String, String>> kpMap, Map<String, KnowledgePointNode> nodeMap, 
		String kpId, KnowledgePointNode node){
		
		kpMap.entrySet().forEach(kpEntry -> {
			Map<String, String> kpInfo = kpEntry.getValue();
			if (kpId.equals(kpInfo.get("parentKnowledgePointId"))){
				KnowledgePointNode subNode = new KnowledgePointNode();
				
				String name = kpInfo.get("knowledgePointName");
				subNode.setName(name);
				
				String id = kpInfo.get("knowledgePointId");
				subNode.setId(id);
				
				String descr = kpInfo.get("knowledgePointDescr");
				subNode.setDescr(descr);
				
				int orderId = Integer.parseInt(kpInfo.get("orderId"));
				subNode.setOrderId(orderId);
				
				nodeMap.put(id, subNode);
				subNode.setParentNode(node);
				node.getSubNodeList().add(subNode);
				
				buildNextLevelNodes(kpMap, nodeMap, id, subNode);
			}
		});
	}
	
	/**
	 * 选择指定知识点及它的上级知识点
	 * @param allKnowledgePoint
	 * @param kpIds
	 * @return
	 */
	private Map<String, Map<String, String>> chooseKpIds(Map<String, Map<String, String>> allKnowledgePoint, 
		Collection<String> kpIds){
		Map<String, Map<String, String>> resultMap = new ConcurrentHashMap<>();
		kpIds.forEach(kpId -> {
			chooseKpIds(resultMap, allKnowledgePoint, kpId);
		});
		return resultMap;
	}
	
	private void chooseKpIds(Map<String, Map<String, String>> resultMap, Map<String, Map<String, String>> allKnowledgePoint, String kpId){
		
		Map<String, String> kpInfo = allKnowledgePoint.get(kpId);
		resultMap.put(kpId, kpInfo);
		
		String parentIds = kpInfo.get("parentKnowledgePointId");
		if (!StringUtils.isEmpty(parentIds)){
			chooseKpIds(resultMap, allKnowledgePoint, parentIds);
		}
	}
	
	/**
	 * 获取指定知识点的下级所有知识点 且 包括它本身
	 * @param kpMap
	 * @param subIds
	 * @param kpId
	 */
	private void subKpIds(Map<String, Map<String, String>> kpMap, Set<String> subIds, String kpId){
		subIds.add(kpId);
		kpMap.entrySet().forEach(entry -> {
			String parentId = entry.getValue().get("parentKnowledgePointId");
			if (kpId.equals(parentId)){
				String id = entry.getValue().get("knowledgePointId");
				subKpIds(kpMap, subIds, id);
			}
		});
	}
}
