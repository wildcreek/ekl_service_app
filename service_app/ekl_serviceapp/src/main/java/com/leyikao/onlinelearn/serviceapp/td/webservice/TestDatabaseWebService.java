package com.leyikao.onlinelearn.serviceapp.td.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.leyikao.onlinelearn.serviceapp.td.business.CourseBusiness;
import com.leyikao.onlinelearn.serviceapp.td.business.TestDatabaseBusiness;
import com.leyikao.onlinelearn.serviceapp.td.pojo.KnowledgePointNode;
import com.leyikao.onlinelearn.serviceapp.util.MessageSourceHelper;
import com.leyikao.onlinelearn.serviceapp.util.ResourceHelper;
import com.leyikao.onlinelearn.serviceapp.util.Utils;
import com.leyikao.onlinelearn.serviceapp.util.Utils.QuestionOperateType;
import com.leyikao.onlinelearn.serviceapp.util.WebServiceTemplate;


@Path("/tdService")
public class TestDatabaseWebService {
	
	@Autowired
	private TestDatabaseBusiness tdBusiness;
	
	@Autowired
	private CourseBusiness courseBusiness;
	
	@Autowired
	private MessageSourceHelper messageSourceHelper;
	
	@Autowired
	private HttpServletRequest request;
	
	@POST
    @Path("/queryTestdatabase")  
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> queryTestdatabase(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson = "{\"userId\":\"001234\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String userId = requestJsonMap.get("userId").toString();
				if (StringUtils.isEmpty(userId)){
					notice = messageSourceHelper.getMessage("td.query.test.database.userid.cannt.null");
				}
				Map<String, Object> resultMap = tdBusiness.queryTestdatabase(userId);
				@SuppressWarnings("unchecked")
				List<KnowledgePointNode> nodeList = (List<KnowledgePointNode>)resultMap.get("nodeList");
				if (nodeList.size() > 0){
					data.put("nodeList", nodeList);
				} else {
					notice = (String)resultMap.get("notice");
					if (StringUtils.isEmpty(notice)){
						notice = messageSourceHelper.getMessage("td.query.test.database.load.failure");
					}
				}
			}
		}.execute();
	}
	
	@POST
    @Path("/queryCollections")  
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> queryCollections(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson = "{\"userId\":\"15bf62f4-165a-11e5-9ab5-525400c8f09f\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String userId = requestJsonMap.get("userId").toString();
				List<KnowledgePointNode> nodeList = tdBusiness.queryCollections(userId);
				data.put("nodeList", nodeList);
			}
		}.execute();
	}
	
	@POST
    @Path("/errorQuestions")  
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> errorQuestions(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson = "{\"userId\":\"001234\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String userId = requestJsonMap.get("userId").toString();
				List<KnowledgePointNode> nodeList = tdBusiness.errorQuestions(userId);
				data.put("nodeList", nodeList);
			}
		}.execute();
	}
	
	
	
	@POST
    @Path("/queryTestPaper")  
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> queryTestPaper(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson = "{\"examType\":\"国考\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String examType = requestJsonMap.get("examType").toString();
				List<Map<String, String>> testPaperList = tdBusiness.queryTestPaper(examType);
				data.put("testPaperList", testPaperList);
			}
		}.execute();
	}
	
	@POST
    @Path("/collectionQuestion")  
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> collectionQuestion(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson = "{\"userId\":\"00023434\",\"questionId\":\"00023\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String userId = requestJsonMap.get("userId").toString();
				String questionId = requestJsonMap.get("questionId").toString();
				int effectRows = tdBusiness.collectionQuestion(userId, questionId);
				if (effectRows == 0){
					notice = messageSourceHelper.getMessage("td.collection.question.failure");
				} 
			}
		}.execute();
	}
	
	@POST
    @Path("/queryQuestions")  
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> queryQuestions(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson = "{\"queryType\":\"1\",\"userId\":\"001\",\"knowledgePointId\":\"0005\",\"testPaperId\":\"0005\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String queryType = requestJsonMap.get("queryType").toString();
				String userId = requestJsonMap.get("userId").toString();
				String knowledgePointId = requestJsonMap.get("knowledgePointId").toString();
				String testPaperId = requestJsonMap.get("testPaperId").toString();
				
				if (QuestionOperateType.REAL.equals(queryType)) {
				} else if (QuestionOperateType.ERROR.equals(queryType)
						|| QuestionOperateType.COLLECTION.equals(queryType)
						|| QuestionOperateType.TEST_DATABASE.equals(queryType)) {
				}
				
				Map<String, Object> questionInfo = tdBusiness.queryQuestions(queryType, userId, knowledgePointId, testPaperId);
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> questionList = (List<Map<String, Object>>)questionInfo.get("knowledgePointList");
				if (questionList == null || questionList.size() == 0){
					notice = messageSourceHelper.getMessage("td.collection.question.failure");
				} else {
					data.putAll(questionInfo);
				}
			}
		}.execute();
	}
	
	@POST
	@Path("/submitPaper")  
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> submitPaper(@FormParam("jsonInfo") String jsonInfo) throws Exception{
/*		String templateJson = "{\"answerType\":\"1\"," + 
							  "\"userId\":\"15812345678\"," + 
							  "\"testPaperId\":\"000102\"," + 
							  "\"useTime\":\"3600\"," + 
							  "\"answerResults\":[{\"questionId\":\"00003\",\"submitOptions\":\"A\",\"useTime\":\"30\"}," + 
							  					 "{\"questionId\":\"00004\",\"submitOptions\":\"D\",\"useTime\":\"3\"}," + 
							  					 "{\"questionId\":\"00005\",\"submitOptions\":\"B\",\"useTime\":\"30\"}]}";*/
		
		String templateJson = "{\"userId\":\"1\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				/*String answerType = requestJsonMap.get("answerType").toString();*/
				String userId = requestJsonMap.get("userId").toString();
				/*String testPaperId = requestJsonMap.get("testPaperId").toString();
				String userTime = requestJsonMap.get("useTime").toString();
				
				@SuppressWarnings("unchecked")
				List<Map<String, String>> answerResults = (List<Map<String, String>>)requestJsonMap.get("answerResults");
				
				Map<String, Object> resonseAnswers = tdBusiness.submitPaper(answerType, userId, testPaperId, userTime, answerResults);
				data.putAll(resonseAnswers);
				if (resonseAnswers.size() == 0){
					notice = messageSourceHelper.getMessage("td.collection.question.failure");
				}*/
				
				// 把练习情况保存在session
			}
		}.execute();
	}
	
	@POST
    @Path("/answerAnalytical")  
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> answerAnalytical(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson = "{\"analyticalType\":\"1\",\"practiceHistoryId\":\"000102\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String analyticalType = requestJsonMap.get("analyticalType").toString();
				String practiceHistoryId = requestJsonMap.get("practiceHistoryId").toString();
				
				Map<String, Map<String, Object>> analyticalMap = tdBusiness.answerAnalytical(analyticalType, practiceHistoryId);
				data.put("analyticalList", analyticalMap.values());
				if (analyticalMap.size() == 0){
					notice = messageSourceHelper.getMessage("td.answer.analytical.no.date");
				}
			}
		}.execute();
	}
	
	@POST
    @Path("/pictureCarouselUrls")  
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> pictureCarouselUrls(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				List<String> urls = new ArrayList<>();
				String basePath = ResourceHelper.getProperty("sc.image.service.web.address");
				urls.add(basePath + ResourceHelper.getProperty("sc.carouselfigure.path.one"));
				urls.add(basePath + ResourceHelper.getProperty("sc.carouselfigure.path.two"));
				urls.add(basePath + ResourceHelper.getProperty("sc.carouselfigure.path.three"));
				data.put("urls", urls);
			}
		}.execute();
	}
	
	@POST
    @Path("/answerQuestionNumber")  
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> answerQuestionNumber(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				Integer number = tdBusiness.answerQuestionNumber();
				data.put("answerQuestionNumber", number);
			}
		}.execute();
	}
	
	@POST
    @Path("/detailExamList")  
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> detailExamList(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson = "{\"examType\":\"公考\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String basePath = ResourceHelper.getProperty("sc.image.service.web.path") + "/lykimg/";
				String parentExamType = requestJsonMap.get("examType").toString();
				List<String> detailExamList = tdBusiness.detailExamList(parentExamType);
				if (detailExamList.size() > 0) {
					Map<String, String> cityNameImages = Utils.cityNameImages();
					List<Map<String, String>> examTypeInfoList = new ArrayList<>();
					for (String item : detailExamList){
						for (Map.Entry<String, String> entry : cityNameImages.entrySet()){
							if (item.contains(entry.getKey())){
								Map<String, String> examTypeInfo = new HashMap<String, String>();
								examTypeInfo.put("name", item);
								examTypeInfo.put("image", basePath + entry.getValue());
								examTypeInfoList.add(examTypeInfo);
								break;
							}
						};
					};
					
					data.put("examTypeList", examTypeInfoList);
				} else {
					notice = messageSourceHelper.getMessage("td.detail.examtype.load.failure");
				}
				
			}
		}.execute();
	}
	
	@POST
    @Path("/weekCalendar")  
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> weekCalendar(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				Map<String, Object> weekCalendarInfo = tdBusiness.weekCalendarInfo();
				data.putAll(weekCalendarInfo);
			}
		}.execute();
	}
	
	
	@POST
    @Path("/examDeadlineDays")  
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> examDeadlineDays(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson = "{\"examType\":\"北京市考\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String examType = requestJsonMap.get("examType").toString();
				long examDeadlineDays = courseBusiness.examDeadlineDays(examType);
				data.put("examDeadlineDays", examDeadlineDays + "");
			}
		}.execute();
	}
	
	
}
