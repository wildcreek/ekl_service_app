package com.leyikao.onlinelearn.serviceapp.td.business;

import java.text.DecimalFormat;
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
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.leyikao.onlinelearn.serviceapp.td.dao.IPracticeHistoryDao;
import com.leyikao.onlinelearn.serviceapp.td.dao.mybatis.AnswerResultDao;
import com.leyikao.onlinelearn.serviceapp.td.pojo.AnswerResultInfo;
import com.leyikao.onlinelearn.serviceapp.td.pojo.KnowledgePointNode;
import com.leyikao.onlinelearn.serviceapp.td.pojo.KnowledgePointTree;
import com.leyikao.onlinelearn.serviceapp.util.Utils.AnswerResult;

public class AnswerResultBusiness {

	@Autowired
	private AnswerResultDao answerResultDao;
	
	@Autowired
	private IPracticeHistoryDao practiceHistoryDao;
	
	@Autowired
	private QuestionBusiness questionBusiness;
	
	@Autowired
	private KnowledgePointBusiness kpBusiness;
	
	public Map<String, Map<String, Object>> statInfoByKnowledgePoint(String userId, Collection<String> knowledgePointIds){
		
		List<Map<String, Object>> answerInfoList =  answerResultDao.answerInfoByKnowledgePoint(userId, knowledgePointIds);
		
		// 按题目分组(因为每道题可以作答多次)
		Map<String, List<AnswerResultInfo>> questionGroup = new ConcurrentHashMap<>(); 
		answerInfoList.forEach(entry -> {
			String knowledgePointId = entry.get("knowledgePointId").toString();
			String questionId = entry.get("questionId").toString();
			int answerStatus = Integer.parseInt(entry.get("answerStatus").toString());
			boolean isCorrectAnswer = (answerStatus == 1 ? true : false);
			String lastModifyTime = entry.get("lastModifyTime").toString();
			
			List<AnswerResultInfo> infoList = questionGroup.get(questionId);
			if (infoList == null){
				infoList = new ArrayList<>();
				questionGroup.put(questionId, infoList);
			}
			
			infoList.add(new AnswerResultInfo(userId, knowledgePointId, questionId, isCorrectAnswer, lastModifyTime));
		});
		
		// 获取题目分组中最近的错误答题，且按照知识点分组
		Map<String, Map<String, Object>> statInfoMap = new ConcurrentHashMap<>();
		questionGroup.entrySet().forEach(entry -> {
			Collections.sort(entry.getValue());
			AnswerResultInfo info = entry.getValue().get(entry.getValue().size() - 1);
			
			String knowledgePointId = info.getKnowledgePointId();
			Map<String, Object> answerInfo = statInfoMap.get(knowledgePointId);
			if (answerInfo == null){
				answerInfo = new ConcurrentHashMap<>();
				statInfoMap.put(knowledgePointId, answerInfo);
			}
			
			if (info.getIsCorrectAnswer()){
				Object rightCount = answerInfo.get("rightNumber");
				rightCount = (rightCount == null ? 1 : (Integer.parseInt(rightCount.toString()) + 1));
				answerInfo.put("rightNumber", rightCount);
			} else {
				Object errorCount = answerInfo.get("errorNumber");
				errorCount = (errorCount == null ? 1 : (Integer.parseInt(errorCount.toString()) + 1));
				answerInfo.put("errorNumber", errorCount);
			}
			
			Object answerCount = answerInfo.get("answerNumber");
			answerCount = (answerCount == null ? 1 : (Integer.parseInt(answerCount.toString()) + 1));
			answerInfo.put("answerNumber", answerCount);
		});
				
		return statInfoMap;
	}
	
	public Map<String, Map<String, String>> questionAnswerStat(Collection<String> questionIdList){
		
		List<Map<String, Object>> statInfoList =  answerResultDao.questionAnswerStat(questionIdList);
		
		Map<String, Map<String, String>> statInfoMap = new ConcurrentHashMap<>();
		statInfoList.forEach(statInfo -> {
			String id = statInfo.get("questionId").toString();
			Map<String, String> answerInfo = statInfoMap.get(id);
			if (answerInfo == null){
				answerInfo = new ConcurrentHashMap<>();
				statInfoMap.put(id, answerInfo);
			}
			
			int answerStatus = Integer.parseInt(statInfo.get("answerStatus").toString());
			boolean isCorrectAnswer = (answerStatus == 1 ? true : false);
			String answerNumber = statInfo.get("answerNumber").toString();
			if (isCorrectAnswer){
				answerInfo.put("rightNumber", answerNumber);
			} else {
				answerInfo.put("errorNumber", answerNumber);
			}
			
			String temp = answerInfo.get("answerNumber");
			if (StringUtils.isEmpty(temp)){
				answerInfo.put("answerNumber", answerNumber);
			} else {
				temp = (Integer.parseInt(temp) + Integer.parseInt(answerNumber)) + "";
				answerInfo.put("answerNumber", temp);
			}
			
		});
		
		return statInfoMap;
	}
	
	/**
	 * 该试卷多次练习的正确和错误情况统计
	 * @param testPaperId
	 * @return
	 */
	public double avgCorrectRate(String testPaperId){
		final Map<String, Map<String, Integer>> statInfoMap = new ConcurrentHashMap<>();
		
		List<Map<String, Object>> statInfoList =  answerResultDao.testDatabaseAnswerStat(testPaperId);
		statInfoList.forEach(statInfo -> {
			String practiceHistoryId = statInfo.get("practiceHistoryId").toString();
			Map<String, Integer> perTimePracticeStat = statInfoMap.get(practiceHistoryId);
			if (perTimePracticeStat == null){
				perTimePracticeStat = new ConcurrentHashMap<>();
				statInfoMap.put(practiceHistoryId, perTimePracticeStat);
			}
			
			int answerStatus = Integer.parseInt(statInfo.get("answerStatus").toString());
			boolean isCorrectAnswer = (answerStatus == 1 ? true : false);
			Integer answerCount = Integer.parseInt(statInfo.get("answerCount").toString());
			if (isCorrectAnswer){
				perTimePracticeStat.put("rightNumber", answerCount);
			} 
			
			Integer temp = perTimePracticeStat.get("answerNumber");
			if (temp == null){
				perTimePracticeStat.put("answerNumber", answerCount);
			} else {
				temp = (temp + answerCount);
				perTimePracticeStat.put("answerNumber", temp);
			}
		});
		
		final AtomicReference<Double> correctRateOfSum = new AtomicReference<>(0d);
		statInfoMap.entrySet().forEach(entry -> {
			Map<String, Integer> tdAnswerStat = entry.getValue();
			Integer rightNumber = tdAnswerStat.get("rightNumber");
			if (rightNumber != null){
				Double sumRate = correctRateOfSum.get();
				synchronized (sumRate) {
					sumRate += tdAnswerStat.get("rightNumber") / (tdAnswerStat.get("answerNumber") + 0.0);
					correctRateOfSum.set(sumRate);
				}
			}
		});
		
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		double avgCorrectRate = 0d;
		if (correctRateOfSum.get() > 0){
			avgCorrectRate = Double.parseDouble(decimalFormat.format(correctRateOfSum.get() / statInfoMap.size()));
		}
		
		return avgCorrectRate;
	}
	
	public Map<String, Map<String, String>> errorDataQuestion(String userId, Collection<String> knowledgePointIdList){
		List<Map<String, Object>> errorQuestions =  answerResultDao.errorDataQuestions(userId, knowledgePointIdList);
		
		// 按题目分组(因为每道题可以作答多次)
		Map<String, List<AnswerResultInfo>> questionGroup = new ConcurrentHashMap<>(); 
		errorQuestions.forEach(entry -> {
			String knowledgePointId = entry.get("knowledgePointId").toString();
			String questionId = entry.get("questionId").toString();
			int answerStatus = Integer.parseInt(entry.get("answerStatus").toString());
			boolean isCorrectAnswer = (answerStatus == 1 ? true : false);
			String lastModifyTime = entry.get("lastModifyTime").toString();
			
			List<AnswerResultInfo> infoList = questionGroup.get(questionId);
			if (infoList == null){
				infoList = new ArrayList<>();
				questionGroup.put(questionId, infoList);
			}
			
			infoList.add(new AnswerResultInfo(userId, knowledgePointId, questionId, isCorrectAnswer, lastModifyTime));
		});
		
		// 获取题目分组中最近的错误答题
		List<String> questionIdList = new ArrayList<>();
		questionGroup.entrySet().forEach(entry -> {
			Collections.sort(entry.getValue());
			AnswerResultInfo info = entry.getValue().get(entry.getValue().size() - 1);
			if (!info.getIsCorrectAnswer()){
				questionIdList.add(info.getQuestionId());
			}
		});
		
		Map<String, Map<String, String>> dataQuestionMap = new ConcurrentHashMap<>();
		errorQuestions.forEach(item -> {
			Map<String, String> map = new HashMap<>();
			String questionId = item.get("questionId").toString();
			String qDescription = item.get("qDescription").toString();
			if (questionIdList.contains(questionId)){
				map.put("questionId",questionId);
				map.put("qDescription", qDescription);
				if (!StringUtils.isEmpty(item.get("dataQuestionId"))){
					map.put("dataQuestionId", item.get("dataQuestionId").toString());
					map.put("name", item.get("dataQuestionName").toString());
					map.put("description", item.get("dataDescription").toString());
					
				}
				dataQuestionMap.put(questionId, map);
			}
		});
		
		return dataQuestionMap;
	}
	
	/**
	 * 统计用户练习记录中每个知识点的错题数量
	 * @param userId
	 * @return
	 */
	public Map<String, Integer> errorQuestionStat(String userId){
		Map<String, Integer> statMap = new HashMap<>();
		
		List<Map<String, Object>> errorQuestions = answerResultDao.errorQuestions(userId);
		
		// 按题目分组(因为每道题可以作答多次)
		Map<String, List<AnswerResultInfo>> questionGroup = new ConcurrentHashMap<>(); 
		errorQuestions.forEach(entry -> {
			String knowledgePointId = entry.get("knowledgePointId").toString();
			String questionId = entry.get("questionId").toString();
			int answerStatus = Integer.parseInt(entry.get("answerStatus").toString());
			boolean isCorrectAnswer = (answerStatus == 1 ? true : false);
			String lastModifyTime = entry.get("lastModifyTime").toString();
			
			List<AnswerResultInfo> infoList = questionGroup.get(questionId);
			if (infoList == null){
				infoList = new ArrayList<>();
				questionGroup.put(questionId, infoList);
			}
			
			infoList.add(new AnswerResultInfo(userId, knowledgePointId, questionId, isCorrectAnswer, lastModifyTime));
		});
		
		// 获取题目分组中最近的错误答题，且按照知识点分组
		questionGroup.entrySet().forEach(entry -> {
			Collections.sort(entry.getValue());
			AnswerResultInfo info = entry.getValue().get(entry.getValue().size() - 1);
			if (!info.getIsCorrectAnswer()){
				Integer count = statMap.get(info.getKnowledgePointId());
				if (count == null){
					count = 1;
					statMap.put(info.getKnowledgePointId(), count);
				} else {
					statMap.put(info.getKnowledgePointId(), ++count);
				}
			}
		});
		
		return statMap;
	}
	
	public List<String> idList(String userId, Collection<String> knowledgePointList){
		return answerResultDao.idList(userId, knowledgePointList);
	}
	
	public Map<String, String> add(Map<String, Map<String, String>> answerResultMap, String practiceHistoryId){
		Map<String, String> questionToAnswerResult = new ConcurrentHashMap<String, String>();
		answerResultMap.entrySet().forEach(entry -> {
			String answerResultId = practiceHistoryDao.getUUID();
			entry.getValue().put("practiceHistoryId", practiceHistoryId);
			entry.getValue().put("answerResultId", answerResultId);
			questionToAnswerResult.put(entry.getKey(), answerResultId);
		});
		
		// 判断答题是否正确，且把判断结果放入回答结果
		Map<String, String> correctAnswerMap = questionBusiness.correctAnswer(new ArrayList<>(answerResultMap.keySet()));
		wrongJudgment(correctAnswerMap, answerResultMap);
				
		answerResultDao.add(answerResultMap);
		
		return questionToAnswerResult;
	}
	
	public List<Map<String, Object>> answerResultGroupByKnowledgePoint(List<Map<String, String>> answerResults){
		
		List<Map<String, Object>> kpNodeList = new ArrayList<>();
		
		List<String> answerIdList = new ArrayList<>();
		answerResults.forEach(item -> answerIdList.add(item.get("questionId")));
		
		List<Map<String, Object>> questionList = questionBusiness.questionsInfo(answerIdList);
		
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
			Map<String, Object> kpInfo = new HashMap<String, Object>();
			kpNodeList.add(kpInfo);
			kpInfo.put("knowledgePointId", rootNode.getId());
			kpInfo.put("knowledgePointName", rootNode.getName());
			kpInfo.put("knowledgePointDescr", rootNode.getDescr());
			
			List<Map<String, String>> questionOrderList = new ArrayList<>();
			kpInfo.put("answerResultList", questionOrderList);
			
			List<String> questionIdGroup = new ArrayList<>();
			Set<String> subIds = rootNode.getSubIdList();
			subIds.forEach( kpId -> {
				if ( kpToQuestion.get(kpId) != null){
					questionIdGroup.addAll(kpToQuestion.get(kpId));
				}
			});
			
			for (Map<String, String> answerInfo : answerResults){
				String questionId = answerInfo.get("questionId").toString();
				if (questionIdGroup.contains(questionId)){
					questionOrderList.add(answerInfo);
				}
			}
		});
		
		Collections.sort(kpNodeList, new Comparator<Object>(){
			@SuppressWarnings("unchecked")
			@Override
			public int compare(Object o1, Object o2) {
				final AtomicInteger maxOrderId1 = new AtomicInteger(0);
				{
					Map<String, Object> map = (Map<String,Object>)o1;
					List<Map<String, String>> answerResultList = (List<Map<String, String>>)map.get("answerResultList");
					answerResultList.forEach(item -> {
						int orderId = answerIdList.indexOf(item.get("questionId").toString());
						int max = maxOrderId1.get();
						if (orderId > max){
							maxOrderId1.compareAndSet(max, orderId);
						}
					});
				}
				final AtomicInteger maxOrderId2 = new AtomicInteger(0);
				{
					Map<String, Object> map = (Map<String,Object>)o2;
					List<Map<String, String>> answerResultList = (List<Map<String, String>>)map.get("answerResultList");
					answerResultList.forEach(item -> {
						int orderId = answerIdList.indexOf(item.get("questionId").toString());
						int max = maxOrderId2.get();
						if (orderId > max){
							maxOrderId2.compareAndSet(max, orderId);
						}
					});
				}
				
				return maxOrderId1.get() > maxOrderId2.get() ? 1 : -1;
			}
			
		});
		
		return kpNodeList;
	}
	
	public Map<String, Map<String, Object>> practiceAnswerResult(String practiceHistoryId, boolean isOnlyErrorAnswer){
		return answerResultDao.practiceAnswerResult(practiceHistoryId, isOnlyErrorAnswer);
	}
	
	public Map<String, Map<String, Object>> answerInfoByQuestion(Collection<String> questionIdList){
		List<Map<String, Object>> answerList = answerResultDao.answerInfoByQuestion(questionIdList);
		
		// 统计每题的回答次数
		Map<String, Integer> timeStatMap = new ConcurrentHashMap<>();
		answerList.forEach(item -> {
			String questionId = item.get("questionId").toString();
			Integer times = timeStatMap.get(questionId);
			if (times == null){
				times = 1;
				timeStatMap.put(questionId, times);
			} else {
				timeStatMap.put(questionId, ++times);
			}
		});
		
		// 统计每题的正确率
		Map<String, Integer> correctAnswerStat = new ConcurrentHashMap<>();
		answerList.forEach(item -> {
			String questionId = item.get("questionId").toString();
			int answerStatus = Integer.parseInt(item.get("answerStatus").toString());
			boolean isCorrectAnswer = answerStatus == 1 ? true : false;
			if (isCorrectAnswer){
				Integer times = correctAnswerStat.get(questionId);
				if (times == null){
					times = 1;
					correctAnswerStat.put(questionId, times);
				} else {
					correctAnswerStat.put(questionId, ++times);
				}
			}
		});
		Map<String, String> correctRateMap = new ConcurrentHashMap<>();
		timeStatMap.entrySet().forEach(entry -> {
			Integer answerTimes = entry.getValue();
			Integer correctTimes = correctAnswerStat.get(entry.getKey());
			if (correctTimes == null){ correctTimes = 0; }
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			String correctRate = decimalFormat.format(correctTimes / (answerTimes + 0.0));
			correctRateMap.put(entry.getKey(), correctRate);
		});
		
		// 统计每道题的错得最多的选项
		Map<String, Map<String, Integer>> errorOptionMap = new ConcurrentHashMap<>();
		answerList.forEach(item -> {
			String questionId = item.get("questionId").toString();
			int answerStatus = Integer.parseInt(item.get("answerStatus").toString());
			boolean isCorrectAnswer = answerStatus == 1 ? true : false;
			if (!isCorrectAnswer){
				Map<String, Integer> optionStat = errorOptionMap.get(questionId);
				if (optionStat == null){
					optionStat = new ConcurrentHashMap<>();
					errorOptionMap.put(questionId, optionStat);
				}
				
				String submitOptions = item.get("submitOptions").toString();
				Integer times = optionStat.get(submitOptions);
				if (times == null){
					times = 1;
					optionStat.put(submitOptions, times);
				} else {
					optionStat.put(submitOptions, ++times);
				}
			}
		});
		Map<String, String> easyErrorOptionMap = new ConcurrentHashMap<>();
		errorOptionMap.entrySet().forEach( entry -> {
			Map<String, Integer> optionStat = entry.getValue();
			String easyErrorOption = "";
			int max = 0;
			for (Map.Entry<String, Integer> option : optionStat.entrySet()) {
				int times = option.getValue();
				if (times > max){
					max = times;
					easyErrorOption = option.getKey();
				}
			};
			if (!StringUtils.isEmpty(easyErrorOption)){
				easyErrorOptionMap.put(entry.getKey(), easyErrorOption);
			}
			
		});
		
		
		Map<String, Map<String, Object>> statMap = new ConcurrentHashMap<>();
		questionIdList.forEach( questionId -> {
			Map<String, Object> statInfo = new ConcurrentHashMap<>();
			statMap.put(questionId, statInfo);
			
			Integer times = timeStatMap.get(questionId);
			times = times == null ? 0 : times;
			statInfo.put("times", times + "");
			
			String correctRate = correctRateMap.get(questionId);
			correctRate = StringUtils.isEmpty(correctRate) ? "" : correctRate;
			statInfo.put("correctRate", correctRate);
			
			String errorProneOptions = easyErrorOptionMap.get(questionId);
			errorProneOptions = StringUtils.isEmpty(errorProneOptions) ? "" : errorProneOptions;
			statInfo.put("errorProneOptions", errorProneOptions);
		});
		
		return statMap;
		
	}

	private void wrongJudgment(Map<String, String> correctAnswerMap, Map<String, Map<String, String>> answerResultMap){
		answerResultMap.entrySet().forEach(entry -> {
			String correctAnswer = correctAnswerMap.get(entry.getKey());
			String submitOptions = entry.getValue().get("submitOptions");
			if (!StringUtils.isEmpty(submitOptions)){
				if (correctAnswer.equalsIgnoreCase(submitOptions)){
					entry.getValue().put("answerStatus", AnswerResult.RIGHT);
				} else {
					entry.getValue().put("answerStatus", AnswerResult.ERROR);
				}
			} else {
				entry.getValue().put("answerStatus", AnswerResult.NOT_ANSWERED);
			}
		});
	}
	
	public Integer answerQuestionNumber(){
		return answerResultDao.answerQuestionNumber();
	}
}
