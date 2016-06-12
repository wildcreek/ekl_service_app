package com.leyikao.onlinelearn.serviceapp.td.business;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;



import javax.annotation.Resource;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;



import com.leyikao.onlinelearn.serviceapp.td.dao.ICollectionDao;
import com.leyikao.onlinelearn.serviceapp.td.dao.ICourseDao;
import com.leyikao.onlinelearn.serviceapp.td.dao.IPracticeHistoryDao;
import com.leyikao.onlinelearn.serviceapp.td.dao.IQuestionDao;
import com.leyikao.onlinelearn.serviceapp.td.dao.ITestPaperDao;
import com.leyikao.onlinelearn.serviceapp.td.pojo.KnowledgePointNode;
import com.leyikao.onlinelearn.serviceapp.td.pojo.KnowledgePointTree;
import com.leyikao.onlinelearn.serviceapp.util.MessageSourceHelper;
import com.leyikao.onlinelearn.serviceapp.util.ResourceHelper;
import com.leyikao.onlinelearn.serviceapp.util.SimilarityDegree;
import com.leyikao.onlinelearn.serviceapp.util.Utils;
import com.leyikao.onlinelearn.serviceapp.util.Utils.AnalyticalType;
import com.leyikao.onlinelearn.serviceapp.util.Utils.AnswerResult;
import com.leyikao.onlinelearn.serviceapp.util.Utils.QuestionOperateType;

/**
 * 题库：试卷、题目、选项、练习历史、题目答题情况、课程、知识点、收藏
 *
 */
public class TestDatabaseBusiness {
	@Autowired
	private ITestPaperDao testPaperDao;
	
	@Autowired
	private IQuestionDao questionDao;
	
	@Resource
	private ICourseDao courseDao;
	
	@Autowired
	private ICollectionDao collectionDao;
	
	@Autowired
	private IPracticeHistoryDao practiceHistoryDao;
	
	@Autowired
	private AnswerResultBusiness answerResultBusiness;
	
	@Autowired
	private TerminalUserBusiness tuBusiness;
	
	@Autowired
	private KnowledgePointBusiness kpBusiness;
	
	@Autowired
	private QuestionBusiness questionBusiness;
	
	@Autowired
	private MessageSourceHelper messageSourceHelper;
	
	protected final Logger logger = LoggerFactory.getLogger(TestDatabaseBusiness.class);
	
	public Map<String, Object> queryTestdatabase(String userId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<KnowledgePointNode> nodeList = new ArrayList<>();
		resultMap.put("nodeList", nodeList);
		
		Map<String, String> userInfo = tuBusiness.queryUserById(userId);
		String currentExamType = userInfo.get("currentExamType");
		if (StringUtils.isEmpty(currentExamType)){
			logger.error("用户：" + userId + "，没有设置考试类型");
			resultMap.put("notice", "请在“我”中选择备考类型");
			return resultMap;
		}
		
		List<Map<String, String>> courseInfoList = courseDao.queryCourseInfo(currentExamType);
		if (courseInfoList.size() == 0){
			logger.error("用户：" + userId + "，" + currentExamType + "没有设置课程，请检查td_course表");
			return resultMap;
		}

		Set<String> testPaperIds = new HashSet<>();
		courseInfoList.forEach(courseInfo -> {
			String courseName = courseInfo.get("courseName");
			String examType = courseInfo.get("examType");
			testPaperIds.addAll(testPaperDao.queryTestPaperIds(courseName, examType));
		});
		
		if (testPaperIds.size() == 0){
			logger.error("用户：" + userId + "，" + currentExamType + "没有试卷，请检查td_testPaper表");
			return resultMap;
		}
		
		Map<String, Object> questionStatMap = questionDao.statInfo(new ArrayList<>(testPaperIds));
		Map<String, Map<String, Object>> answerStatMap = answerResultBusiness.statInfoByKnowledgePoint(userId, questionStatMap.keySet());
		
		// 构建题目所属的知识点的节点树
		KnowledgePointTree kpTree = kpBusiness.buildNodeList(questionStatMap.keySet());
		
		// 题目直接所属知识点的统计信息
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		questionStatMap.entrySet().forEach(kpGroup -> {
			String id = kpGroup.getKey();
			KnowledgePointNode node = kpTree.getNodeMap().get(id);
			if (node != null){
				int questionNumber = Integer.parseInt(kpGroup.getValue().toString());
				node.setQuestionNumber(questionNumber);
				
				Map<String, Object> answerInfo = answerStatMap.get(id);
				if (answerInfo != null){
					int answerNumber = 0;
					if (answerInfo.get("answerNumber") != null){
						answerNumber = Integer.parseInt(answerInfo.get("answerNumber").toString());
					}
					node.setAnswerNumber(answerNumber);
					
					int rightNumber = 0;
					if (answerInfo.get("rightNumber") != null){
						rightNumber = Integer.parseInt(answerStatMap.get(id).get("rightNumber").toString());
					}
					node.setRightNumber(rightNumber);
					
					String correctRateOfLastTime = "0";
					if (answerNumber > 0){
						correctRateOfLastTime = decimalFormat.format(rightNumber / (answerNumber + 0.0));
					}
					node.setCorrectRateOfLastTime(Double.parseDouble(correctRateOfLastTime));
				}
			} else {
				logger.error("缺少知识点：" + id + " (在td_knowledgePoint中增加该知点 或 td_question中修改为存在的所属知识点 或 删除该题目)");
			}
		});
		
		kpTree.summaryUpperLayerNode();
		
		nodeList.addAll(kpTree.getNodeList());
		if (nodeList.size() == 0){
			logger.error("用户：" + userId + "，请检查试卷" + Arrays.toString(testPaperIds.toArray()) + 
				"下的题目的所属知识点是否正确，还请检查td_knowledgePoint表配置是否正确");
		}
		
		return resultMap;
	}
	
	public List<KnowledgePointNode> queryCollections(String userId){
		Map<String, Map<String, Object>> statMap = collectionDao.statInfo(userId);
		
		// 构建题目所属的知识点的节点树
		KnowledgePointTree kpTree = kpBusiness.buildNodeList(statMap.keySet());
		
		statMap.entrySet().forEach(entry -> {
			int collectionCount = Integer.parseInt(entry.getValue().get("collectionCount").toString());
			KnowledgePointNode node = kpTree.getNodeMap().get(entry.getKey());
			node.setQuestionNumber(collectionCount);
		});
		
		kpTree.summaryUpperLayerNode();
		
		return kpTree.getNodeList();
	}
	
	public List<KnowledgePointNode> errorQuestions(String userId){
		Map<String, Integer> statMap = answerResultBusiness.errorQuestionStat(userId);
		
		// 构建题目所属的知识点的节点树
		KnowledgePointTree kpTree = kpBusiness.buildNodeList(statMap.keySet());
		
		statMap.entrySet().forEach(entry -> {
			KnowledgePointNode node = kpTree.getNodeMap().get(entry.getKey());
			node.setQuestionNumber(entry.getValue());
		});
		
		kpTree.summaryUpperLayerNode();
		
		return kpTree.getNodeList();
	}
	
	public List<Map<String, String>> queryTestPaper(String examType){
		List<Map<String, String>> testPaperList = testPaperDao.queryTestPaperInfo("%" + examType + "%");
		testPaperList.forEach(item -> {
			if (StringUtils.isEmpty(item.get("isAnswer"))){
				item.put("isAnswer", "0");
			} else {
				item.put("isAnswer", "1");
			}
		});
		return testPaperList;
	}
	
	public int collectionQuestion(String userId, String questionId){
		return collectionDao.collectionQuestion(userId, questionId);
	}
	
	public Map<String, Object> queryQuestions(String queryType,
			String userId, String knowledgePointId, String testPaperId) {
		
		Map<String, Object> questionMap = new ConcurrentHashMap<>();
		
		// 题库一次查看的题目数量
		int questionSize = Integer.parseInt(ResourceHelper.getProperty("sc.question.count.per.time.practice"));
		
		final List<String> queryQuestionIds = new ArrayList<>();

		// 题目资料
		final Map<String, Map<String, String>> dataQuestionMap = new ConcurrentHashMap<>();;
		if (QuestionOperateType.REAL.equals(queryType)){
			List<Map<String, String>> dataQuestionList = questionDao.questionIdsByTestPaper(testPaperId);
			dataQuestionList.forEach(item -> {
				String questionId = item.get("questionId");
				dataQuestionMap.put(questionId, item);
				queryQuestionIds.add(questionId);
			});
		} else if (QuestionOperateType.TEST_DATABASE.equals(queryType)) {
			Set<String> subKpIds = kpBusiness.subKnowledgePointIds(knowledgePointId);
			
			// 已经回答的题目
			List<String> answerQuestionIds = answerResultBusiness.idList(userId, subKpIds);
			
			// 知识点所属的所有题目
			dataQuestionMap.putAll(questionDao.idList( new ArrayList<>(subKpIds)));
			
			// 题库中未答的题目
			final Map<String, Map<String, String>> filterMap = new ConcurrentHashMap<>(dataQuestionMap);
			answerQuestionIds.forEach(p -> {
				if (filterMap.keySet().contains(p)){
					filterMap.remove(p);
				}
			});
			
			if (filterMap.size() > 0){
				dataQuestionMap.clear();
				dataQuestionMap.putAll(filterMap);
			}
			
		} else if (QuestionOperateType.ERROR.equals(queryType) || QuestionOperateType.COLLECTION.equals(queryType)) {
			Set<String> subKpIds = kpBusiness.subKnowledgePointIds(knowledgePointId);
			if (QuestionOperateType.ERROR.equals(queryType)){
				dataQuestionMap.putAll(answerResultBusiness.errorDataQuestion(userId, subKpIds));
			} else {
				dataQuestionMap.putAll(collectionDao.idList(userId, new ArrayList<>(subKpIds)));
			}
		}
		
		// 出题(包含资料分析题)
		if (QuestionOperateType.TEST_DATABASE.equals(queryType) || QuestionOperateType.ERROR.equals(queryType) || QuestionOperateType.COLLECTION.equals(queryType)){
			queryQuestionIds.addAll(generatorRandomQuestion(dataQuestionMap, questionSize));
		}
		
		String imageServicePath = ResourceHelper.getProperty("sc.image.service.web.path");
		
		// 题目资料
		final Map<String, Map<String, String>> finalDataQuestionMap = new ConcurrentHashMap<>(dataQuestionMap);
		final List<Map<String, String>> dataQuestionList = new ArrayList<>();
		Set<String> dataQuestionIdSet = new HashSet<String>();
		questionMap.put("dataQuestionList", dataQuestionList);
		queryQuestionIds.forEach(item -> {
			Map<String, String> dataQuestion = finalDataQuestionMap.get(item);
			Object dataQuestionId = dataQuestion.get("dataQuestionId");
			if (dataQuestion != null && !StringUtils.isEmpty(dataQuestionId) && !dataQuestionIdSet.contains(dataQuestionId)){
				dataQuestion.remove("questionId");
				String descr = dataQuestion.get("description");
				if (!StringUtils.isEmpty(descr)){
					dataQuestion.put("description", descr.replaceAll("/lykimg/paper/datapic/", imageServicePath +"/lykimg/paper/datapic/"));
				}
				
				dataQuestionList.add(dataQuestion);
				dataQuestionIdSet.add(dataQuestionId.toString());
			}
		});
		 
		// 知识点、题目及选项信息
		List<Map<String, Object>> kpNodeList = new ArrayList<>();
		questionMap.put("knowledgePointList", kpNodeList);
		if (QuestionOperateType.REAL.equals(queryType) && queryQuestionIds != null && queryQuestionIds.size() > 0){
			kpNodeList.addAll(questionBusiness.questionGroupByKnowledgePoint(queryQuestionIds));
		} else if (QuestionOperateType.TEST_DATABASE.equals(queryType) 
			|| QuestionOperateType.ERROR.equals(queryType)
			|| QuestionOperateType.COLLECTION.equals(queryType)) {
			
			Map<String, Object> kpNode = new HashMap<>();
			kpNodeList.add(kpNode);
			
			Map<String, String> kpInfo = kpBusiness.getInfo(knowledgePointId);
			kpNode.put("knowledgePointId", kpInfo.get("knowledgePointId"));
			kpNode.put("knowledgePointName", kpInfo.get("knowledgePointName"));
			kpNode.put("knowledgePointDescr", kpInfo.get("knowledgePointDescr"));
			
			if (queryQuestionIds != null && queryQuestionIds.size() > 0){
				List<Map<String, Object>> questionList = questionBusiness.questionAndOptionsInfo(queryQuestionIds);
				kpNode.put("questionList", questionList);
			}
		}
		
		// 处理题目中的图片路径
		kpNodeList.forEach(item -> {
			Object questionObjectList = item.get("questionList");
			if (questionObjectList != null){
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> questionList = (List<Map<String, Object>>)questionObjectList;
				questionList.forEach(q -> {
					String descr = q.get("description").toString();
					q.put("description", descr.replaceAll("/lykimg/paper/", imageServicePath +"/lykimg/paper/"));
				});
			}
		});
		
		return questionMap;
	}
	
	/**
	 * 保存答题结果(需要有事务处理)
	 * @param answerType
	 * @param userId
	 * @param testPaperId
	 * @param userTime
	 * @param answerResults
	 * @return
	 */
	public Map<String, Object> submitPaper(String answerType, String userId,
			String testPaperId, String userTime,
			List<Map<String, String>> answerResults) {
		
		// 入库
		String practiceHistoryId = practiceHistoryDao.getUUID();
		String isFinishAnswer = "1"; // 暂时不检查是否答完
		Instant currentInstant = Instant.now();
		String submitTime = Utils.instantToString(currentInstant, Utils.DateParttern.DATE_TIME);
		userTime = (StringUtils.isEmpty(userTime) ? "0" : userTime);
		Instant beginInstant = currentInstant.minusSeconds(Integer.parseInt(userTime));
		String beginTime = Utils.instantToString(beginInstant, Utils.DateParttern.DATE_TIME);
		practiceHistoryDao.add(practiceHistoryId, userId, testPaperId, answerType, beginTime, submitTime, isFinishAnswer);
		
		Map<String, Map<String, String>> answerResultMap = new ConcurrentHashMap<>();
		answerResults.forEach( item -> {
			answerResultMap.put(item.get("questionId"), item);
		});
		
		Map<String, String> questionToAnswerResult = answerResultBusiness.add(answerResultMap, practiceHistoryId);
		answerResultMap.entrySet().forEach(entry -> {
			entry.getValue().put("answerResultId", questionToAnswerResult.get(entry.getKey()));
		});
		
		// 处理响应
		Map<String, Object> responseResult = new ConcurrentHashMap<>();
		responseResult.put("practiceHistoryId", practiceHistoryId);
		if (QuestionOperateType.REAL.equals(answerType)) {
			
			responseResult.put("submitTime", submitTime);
			
			// 正确回答的题数
			long answerCorrectNumber = answerResultMap.entrySet().stream().filter(
					p -> AnswerResult.RIGHT.equals(p.getValue().get("answerStatus"))).count();
			responseResult.put("answerCorrectNumber", answerCorrectNumber);
			
			
			long answerNumber = answerResultMap.entrySet().stream().filter(
					p -> !AnswerResult.NOT_ANSWERED.equals(p.getValue().get("answerStatus"))).count();
			responseResult.put("answerNumber", answerNumber);
			
			if (answerNumber > 0){
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				double correctRate = Double.parseDouble(decimalFormat.format(answerCorrectNumber / (answerNumber + 0.0)));
				responseResult.put("correctRate", correctRate);
			} else {
				responseResult.put("correctRate", 0);
			}
			
			// 平均正确率
			double avgCorrectRate = answerResultBusiness.avgCorrectRate(testPaperId);
			responseResult.put("avgCorrectRate", avgCorrectRate);
			
			
			// 每道题的回答情况
			List<Map<String, Object>> knowledgePointList = answerResultBusiness.answerResultGroupByKnowledgePoint(answerResults);
			responseResult.put("knowledgePointList", knowledgePointList);
		} else if (QuestionOperateType.TEST_DATABASE.equals(answerType) 
			|| QuestionOperateType.ERROR.equals(answerType)
			|| QuestionOperateType.COLLECTION.equals(answerType)) {
			
			List<String> questionIdList = new ArrayList<>();
			answerResults.forEach(item -> questionIdList.add(item.get("questionId")));
			
			Map<String, Map<String, String>> statMap = answerResultBusiness.questionAnswerStat(questionIdList);
			answerResults.forEach(item -> {
				String questionId = item.get("questionId");
				if (statMap.get(questionId) != null){
					String pepleNumber = statMap.get(questionId).get("answerNumber");
					item.put("pepleNumber", pepleNumber);
					
					String rightNumber = statMap.get(questionId).get("rightNumber");
					if (StringUtils.isEmpty(rightNumber)) {
						rightNumber = "0";
					}
					DecimalFormat decimalFormat = new DecimalFormat("0.00");
					String correctRate = decimalFormat.format(Integer.parseInt(rightNumber) / (Integer.parseInt(pepleNumber) + 0.0));
					item.put("correctRate", correctRate);
				} else {
					item.put("pepleNumber", "0");
					item.put("correctRate", "0");
				}
			
			});
			responseResult.put("answerResultList", answerResults);
		}
		
		return responseResult;
	}
	
	/**
	 * 
	 * @param analyticalType
	 * @param practiceHistoryId
	 * @param questionId
	 * @param answerResultId
	 * @return 每个题目的解析信息
	 */
	public Map<String, Map<String, Object>> answerAnalytical(String analyticalType, String practiceHistoryId){
		Map<String, Map<String, Object>> analyticalMap = new ConcurrentHashMap<>();
		List<String> questionIdList = new ArrayList<String>();
		
		Map<String, Map<String, Object>> questionAnswerMap = null;
		if (AnalyticalType.ALL_ERROR.equals(analyticalType) || AnalyticalType.ALL_QUESTION.equals(analyticalType)){
			boolean isOnlyErrorAnswer = (AnalyticalType.ALL_ERROR.equals(analyticalType) ? true : false);
			questionAnswerMap = answerResultBusiness.practiceAnswerResult(practiceHistoryId, isOnlyErrorAnswer);
			questionIdList.addAll(questionAnswerMap.keySet());
		}
		
		
		if (questionAnswerMap != null && questionIdList.size() > 0){
			Map<String, Map<String, Object>> answerInfoMap = questionBusiness.questionAnswer(questionIdList);
			Map<String, Map<String, Object>> answerStatMap = answerResultBusiness.answerInfoByQuestion(questionIdList);
			questionAnswerMap.entrySet().forEach( entry -> {
				/**
				 * 题目回答情况
				 * 题目ID：questionId
				 * 回答状态：answerStatus（2- 未回答，1- 是回答正确 ，0- 是答案错误）
				 * 作答的选项：submitOptions（多个选项用逗号分开）
				 * 作答用时：useTime（秒数）
				 */
				analyticalMap.put(entry.getKey(), entry.getValue());
				
				/**
				 * 题目答案和解析
				 * 正确的选项：correctOptions（多个选项用逗号分开）
				 * 解析结果：answerAnalysis
				 * 来源：source
				 */
				Map<String, Object> answerInfo = answerInfoMap.get(entry.getKey());
				if (answerInfo == null){
					answerInfo = new ConcurrentHashMap<>();
					answerInfo.put("answerAnalysis", "");
					answerInfo.put("source", "");
					answerInfo.put("correctOptions", "");
				}
				analyticalMap.get(entry.getKey()).putAll(answerInfo);
				
				
				/**
				 * 答题统计
				 * 本题作答的次数：times
			     * 正确率：correctRate
				 * 易错选项的名称：errorProneOptions（多个选项用逗号分开）
				 */
				Map<String, Object> statInfo = answerStatMap.get(entry.getKey());
				if (statInfo == null){
					statInfo = new ConcurrentHashMap<>();
					statInfo.put("times", "");
					statInfo.put("correctRate", "");
					statInfo.put("errorProneOptions", "");
				}
				analyticalMap.get(entry.getKey()).putAll(statInfo);
			});
		}
		
		return analyticalMap;
	}
	
	public Integer answerQuestionNumber(){
		return answerResultBusiness.answerQuestionNumber();
	}
	
	public List<String> detailExamList(String parentExamType){
		List<String> detailExamList = courseDao.detailExamList(parentExamType);
		
		String nationalExam = messageSourceHelper.getMessage("tu.exam.type.national.exam");
		if (nationalExam.equals(parentExamType)){
			detailExamList.remove(nationalExam);
		}
		
		if (detailExamList.size() == 0){
			logger.error(parentExamType + "在td_course中没有配置详细考试类型");
		}
		
		return detailExamList;
	}
	
	public Map<String, Object> weekCalendarInfo(){
		Map<String, Object> weekCalendar = new HashMap<>();
		LocalDate localDate = LocalDate.now();
		
		weekCalendar.put("nowMonthDay", (localDate.getMonthValue() + "-" + localDate.getDayOfMonth()));
		
		List<Map<String, Object>> daysOfWeekInfo = new ArrayList<>();
		weekCalendar.put("daysOfWeek", daysOfWeekInfo);
		
		Map<Integer, String> names = Utils.weekDisplayNames(messageSourceHelper);
		List<Integer> days = Utils.daysOfWeek();
		
		int indexOfNowDay = days.indexOf(localDate.getDayOfMonth());
		weekCalendar.put("nowName", names.get(indexOfNowDay));
		
		for (int index = 0; index < days.size(); index++){
			Integer day = days.get(index);
			Map<String, Object> info = new HashMap<>();
			info.put("name", names.get(index + 1));
			info.put("day", day);
			daysOfWeekInfo.add(info);
		}
		return weekCalendar;
	}
	
	private List<String> generatorRandomQuestion(final Map<String, Map<String, String>> dataQuestionMap, final int SIZE){
		final List<String> resultIds = new ArrayList<>();
		if (dataQuestionMap.size() > SIZE){
			// 资料分组
			final Map<String, List<String>> dataQuestionGroup = new ConcurrentHashMap<>();
			dataQuestionMap.entrySet().forEach(item -> {
				String dataQuestionId = item.getValue().get("dataQuestionId");
				if (!StringUtils.isEmpty(dataQuestionId)){
					List<String> questionIdList = dataQuestionGroup.get(dataQuestionId);
					if (questionIdList == null){
						questionIdList = new ArrayList<>();
						dataQuestionGroup.put(dataQuestionId, questionIdList);
					}
					questionIdList.add(item.getKey());
				}
			});
			
			final double someDegreeSentinel = 0.5;
			final Random random = new Random();
			
			List<String> dateQuestionDescr = new ArrayList<>();
			final List<String> randomRegionIdList = new ArrayList<>(dataQuestionMap.keySet());
			do {
				int questionTotalCount = randomRegionIdList.size();
				int index = random.nextInt(questionTotalCount);
				String id = randomRegionIdList.get(index);
				Map<String, String> dataQuestionInfo = dataQuestionMap.get(id);
				String dataQuestionId = dataQuestionInfo.get("dataQuestionId");
				if (StringUtils.isEmpty(dataQuestionId)){
					String description = dataQuestionInfo.get("qDescription");
					long someCount = resultIds.stream().filter(questionId -> {
						String compareDescr = dataQuestionMap.get(questionId).get("qDescription");
						Instant start = Instant.now();
						boolean isSome = SimilarityDegree.isSome(description, compareDescr, someDegreeSentinel);
						if (isSome){
							randomRegionIdList.remove(id);
							Instant end = Instant.now();
							System.out.println("description: " + description);
							System.out.println("compareDescr: " + compareDescr);
							long responseTime = end.toEpochMilli() - start.toEpochMilli();
							System.out.println("calculate similarity degree: " + responseTime);
						}
						return isSome;
					}).limit(1).count();
					if (someCount == 0){
						resultIds.add(id);
						randomRegionIdList.remove(id);
					}
				
				} else {
					String dataDescription = dataQuestionInfo.get("description");
					long someCount = dateQuestionDescr.stream().filter(compareItem -> {
						Instant start = Instant.now();
						boolean isSome = SimilarityDegree.isSome(dataDescription, compareItem, someDegreeSentinel);
						if (isSome){
							dataQuestionGroup.get(dataQuestionId).forEach(randomRegionIdList::remove);
							Instant end = Instant.now();
							System.out.println("description: " + dataDescription);
							System.out.println("compareDescr: " + compareItem);
							long responseTime = end.toEpochMilli() - start.toEpochMilli();
							System.out.println("calculate similarity degree: " + responseTime);
						}
						return isSome;
					}).limit(1).count();
					if (someCount == 0){
						final List<String> dataQuestionIdList = dataQuestionGroup.get(dataQuestionId);
						if ( (dataQuestionIdList.size() + resultIds.size()) > SIZE){
							dataQuestionIdList.forEach(randomRegionIdList::remove);
							continue;
						}
						resultIds.addAll(dataQuestionIdList);
						dataQuestionIdList.forEach(randomRegionIdList::remove);
						dateQuestionDescr.add("dataDescription");
					} 
				}
			
			} while (resultIds.size() < SIZE && randomRegionIdList.size() > 0);
		} else {
			resultIds.addAll(dataQuestionMap.keySet());
		}
		
		return resultIds;
	}
	
}
