package com.leyikao.onlinelearn.serviceapp.td.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.leyikao.onlinelearn.serviceapp.td.dao.IQuestionDao;
import com.leyikao.onlinelearn.serviceapp.td.pojo.KnowledgePointNode;
import com.leyikao.onlinelearn.serviceapp.td.pojo.KnowledgePointTree;
import com.leyikao.onlinelearn.serviceapp.td.pojo.QuestionOption;
import com.leyikao.onlinelearn.serviceapp.util.ResourceHelper;


public class QuestionBusiness {
	
	@Autowired
	private IQuestionDao questionDao;
	
	@Autowired
	private KnowledgePointBusiness kpBusiness;
	
	public List<Map<String, Object>> questionAndOptionsInfo(List<String> questionIdList){
		Map<String, Map<String, Object>> questionsInfo = questionDao.questionsInfo(questionIdList);
		Map<String, List<QuestionOption>> optionMap = questionOptions(questionIdList);
		
		// 按照传入的题目顺序返回
		List<Map<String, Object>> questionInfoList = new ArrayList<>();
		questionIdList.forEach(questionId -> {
			Map<String, Object> questionInfo = questionsInfo.get(questionId);
			List<QuestionOption> options = optionMap.get(questionId);
			questionInfo.put("questionOptions", options);
			questionInfoList.add(questionInfo);
		});
		return questionInfoList;
	}
	
	public List<Map<String, Object>> questionsInfo(List<String> questionIdList){
		Map<String, Map<String, Object>> questionsInfo = questionDao.questionsInfo(questionIdList);
		
		// 按照传入的题目顺序返回
		List<Map<String, Object>> questionInfoList = new ArrayList<>();
		questionIdList.forEach(questionId -> {
			Map<String, Object> questionInfo = questionsInfo.get(questionId);
			questionInfoList.add(questionInfo);
		});
		
		return questionInfoList;
	}
	
	public Map<String, List<QuestionOption>> questionOptions(List<String> questionIdList){
		Map<String, List<QuestionOption>> optionMap = new ConcurrentHashMap<>();
		
		List<Map<String, String>> options = questionDao.questionOptions(questionIdList);
		
		// 对选项按题目Id分组
		options.forEach(option -> {
			String questionId = option.get("questionId");
			String name = option.get("questionOptionName");
			String description = option.get("description");
			
			List<QuestionOption> optionList = optionMap.get(questionId);
			if (optionList == null){
				optionList = new ArrayList<>();
				optionMap.put(questionId, optionList);
			}
			optionList.add(new QuestionOption(name, description));
		});
		
		optionMap.entrySet().forEach( entry -> {
			Collections.sort(entry.getValue());
		});
		
		return optionMap;
	}
	
	public Map<String, String> correctAnswer(List<String> questionIdList){
		Map<String, String> correctAnswerMap = new ConcurrentHashMap<>();
		
		List<Map<String, String>> correctAnswerList = questionDao.correctAnswer(questionIdList);
		
		Map<String, List<QuestionOption>> sortQuestionOptionMap = new HashMap<>();
		correctAnswerList.forEach(item -> {
			String id = item.get("questionId");
			String optionName = item.get("questionOptionName");
			List<QuestionOption> optionList = sortQuestionOptionMap.get(id);
			if (optionList == null){
				optionList = new ArrayList<>();
				sortQuestionOptionMap.put(id, optionList);
			} 
			optionList.add(new QuestionOption(optionName, ""));
		});
		
		sortQuestionOptionMap.entrySet().forEach(entry -> {
			List<QuestionOption> optionList = entry.getValue();
			Collections.sort(optionList);
			String options = "";
			for (QuestionOption qo : optionList){
				options += (qo.getName() + ",");
			}
			options = options.substring(0, options.length() - 1);
			correctAnswerMap.put(entry.getKey(), options);
		});
		
		return correctAnswerMap;
	}
	
	public List<Map<String, Object>> questionGroupByKnowledgePoint(List<String> questionOrderId){
		
		List<Map<String, Object>> kpNodeList = new ArrayList<>();
		
		List<Map<String, Object>> questionList = questionAndOptionsInfo(questionOrderId);
		
		// 按知识点分组
		Map<String, List<String>> kpToQuestion = new ConcurrentHashMap<>();
		questionList.forEach(item -> {
			String kpId = item.get("knowledgePointId").toString();
			List<String> questionIdList = kpToQuestion.get(kpId);
			if (questionIdList == null){
				questionIdList = new ArrayList<>();
				kpToQuestion.put(kpId, questionIdList);
			}
			String questionId = item.get("questionId").toString();
			questionIdList.add(questionId);
		});
		
		// 按一级知识点分组题目
		KnowledgePointTree kpTree =  kpBusiness.buildNodeList(kpToQuestion.keySet());
		List<KnowledgePointNode> rootNodeList = kpTree.getNodeList();
		rootNodeList.forEach( rootNode -> {
			Map<String, Object> kpInfo = new ConcurrentHashMap<String, Object>();
			kpNodeList.add(kpInfo);
			kpInfo.put("knowledgePointId", rootNode.getId());
			kpInfo.put("knowledgePointName", rootNode.getName());
			kpInfo.put("knowledgePointDescr", rootNode.getDescr());
			
			List<Map<String, Object>> questionOrderList = new ArrayList<>();
			kpInfo.put("questionList", questionOrderList);
			
			List<String> questionIdGroup = new ArrayList<>();
			Set<String> subIds = rootNode.getSubIdList();
			subIds.forEach( kpId -> {
				if ( kpToQuestion.get(kpId) != null){
					questionIdGroup.addAll(kpToQuestion.get(kpId));
				}
			});
			
			for (Map<String, Object> questionInfo : questionList){
				String questionId = questionInfo.get("questionId").toString();
				if (questionIdGroup.contains(questionId)){
					questionOrderList.add(questionInfo);
				}
			}
		});
		
		Collections.sort(kpNodeList, new Comparator<Object>(){
			@SuppressWarnings("unchecked")
			@Override
			public int compare(Object o1, Object o2) {
				final AtomicInteger minOrderId1 = new AtomicInteger(Integer.MAX_VALUE);
				{
					Map<String, Object> map = (Map<String,Object>)o1;
					List<Map<String, Object>> questionOrderList = (List<Map<String, Object>>)map.get("questionList");
					questionOrderList.forEach(item -> {
						int orderId = Integer.parseInt(item.get("orderId").toString());
						int min = minOrderId1.get();
						if (orderId < min){
							minOrderId1.compareAndSet(min, orderId);
						}
					});
				}
				final AtomicInteger minOrderId2 = new AtomicInteger(Integer.MAX_VALUE);
				{
					Map<String, Object> map = (Map<String,Object>)o2;
					List<Map<String, Object>> questionOrderList = (List<Map<String, Object>>)map.get("questionList");
					questionOrderList.forEach(item -> {
						int orderId = Integer.parseInt(item.get("orderId").toString());
						int min = minOrderId2.get();
						if (orderId < min){
							minOrderId2.compareAndSet(min, orderId);
						}
					});
				}
				
				System.out.println("mino1: " + minOrderId1.get() + ", mino2: " + minOrderId2.get());
				return minOrderId1.get() > minOrderId2.get() ? 1 : -1;
			}
			
		});
		
		return kpNodeList;
	}
	
	public Map<String, Map<String, Object>> questionAnswer(Collection<String> questionIdList){
		Map<String, Map<String, Object>> questionAnswerMap = new ConcurrentHashMap<>();
		
		Map<String, List<QuestionOption>> correctOptionMap = new ConcurrentHashMap<>();
		
		final String imageServicePath = ResourceHelper.getProperty("sc.image.service.web.path");
		
		List<Map<String, Object>> questionAnswerList = questionDao.questionAnswer(questionIdList);
		questionAnswerList.forEach(item -> {
			String questionId = item.get("questionId").toString();
			String testPaperName = item.get("testPaperName").toString();
			
			String answerAnalysis = item.get("answerAnalysis").toString();
			if ( !StringUtils.isEmpty(answerAnalysis) ){
				answerAnalysis = answerAnalysis.replaceAll("/lykimg/paper/", imageServicePath +"/lykimg/paper/");
			}
			
			Map<String, Object> answerInfo = questionAnswerMap.get(questionId);
			if (answerInfo == null){
				answerInfo = new HashMap<>();
				questionAnswerMap.put(questionId, answerInfo);
				answerInfo.put("questionId", questionId);
				answerInfo.put("answerAnalysis", answerAnalysis);
				answerInfo.put("source", testPaperName);
			}
			
			// 获取正确选择，且按选项名称的字典顺序升序排（多项正确答题时）
			String optionName = item.get("questionOptionName").toString();
			List<QuestionOption> optionList = correctOptionMap.get(questionId);
			if (optionList == null){
				optionList = new ArrayList<>();
				correctOptionMap.put(questionId, optionList);
			}
			optionList.add(new QuestionOption(optionName, ""));
		
		});
		
		questionAnswerMap.entrySet().forEach( entry -> {
			List<QuestionOption> optionList = correctOptionMap.get(entry.getKey());
			Collections.sort(optionList);
			String correctOption = "";
			if (optionList != null){
				for (QuestionOption qo : optionList){
					correctOption += (qo.getName() + ",");
				}
				correctOption = correctOption.substring(0, correctOption.length() - 1);
			}
			entry.getValue().put("correctOptions", correctOption);
		});
		
		return questionAnswerMap;
	}
	
	public Map<String, Map<String, Object>> yearOfQuestion(Collection<String> questionIdList){
		return questionDao.yearOfQuestion(questionIdList);
	}
}