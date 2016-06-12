package com.leyikao.onlinelearn.serviceapp.qa.webservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import com.leyikao.onlinelearn.serviceapp.qa.business.QAUserBusiness;
import com.leyikao.onlinelearn.serviceapp.util.MessageSourceHelper;
import com.leyikao.onlinelearn.serviceapp.util.ResourceHelper;
import com.leyikao.onlinelearn.serviceapp.util.WebServiceTemplate;
/*
 * 问答控制类
 */
@Path("/qaService")
public class QAUserWebService {
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private QAUserBusiness qaUserBusiness;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private MessageSourceHelper messageSourceHelper;
	
	@Autowired
	private HttpServletRequest request;
	/*
	 * 加载考试类型
	 */
	@POST
	@Path("/qureyCourseList")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> qureyCourseList(@FormParam("jsonInfo") String jsonInfo) throws Exception {
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				List courselist = qaUserBusiness.qureyCourseList();
				data.put("courseNameList", courselist);
				if(courselist==null||courselist.size()<=0){
					notice=messageSourceHelper.getMessage("td.course.load.failure");
				}
			}
		}.execute();

	}
	/*
	 * 待解决问题列表
	 */
	@POST
	@Path("/problemList")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object>  problemList(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson ="{\"userId\":\"00023434\",\"pageNumber\":\"1\",\"courseName\":\"行测\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				List<Map<String, Object>> problemList = qaUserBusiness.problemList(requestJsonMap.get("userId").toString(),requestJsonMap.get("pageNumber").toString(),requestJsonMap.get("courseName").toString());
				String totalPage=qaUserBusiness.problemListCount(requestJsonMap.get("userId").toString(),requestJsonMap.get("pageNumber").toString(),requestJsonMap.get("courseName").toString());
				data.put("problemList", problemList);
				data.put("totalPage", totalPage);
		
			}
		}.execute();

		
	}
	/*
	 *提出问题
	 */
	@POST
	@Path("/uploadProblem")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object>  uploadProblem(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson ="{\"userId\":\"00023434\",\"courseName\":\"行测\",\"picture\":\"\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				 Format format = new SimpleDateFormat("yyyy-MM-dd");
			    String createTime=  format.format(new Date()).toString();
				
				if (!StringUtils.isEmpty(createTime)){
					createTime = createTime.substring(0, 10);
				}
				
			
				
				String imageFolder = ResourceHelper.getProperty("sc.image.service.file.path");
				String userImageFolder = ResourceHelper.getProperty("sc.problem.image.folder");
				String userImageDir = imageFolder + "/" + userImageFolder + "/" + createTime;
				File userImagePath = new File( userImageDir);
				if (!userImagePath.exists()){
					userImagePath.mkdirs();
				}
				
				String picture = requestJsonMap.get("picture").toString();
				boolean isValidString = Base64.isBase64(picture);
				if (isValidString){
					byte[] pictureBytes = Base64.decodeBase64(picture);
					try {
						Instant start = Instant.now();
						String uuidpic=UUID.randomUUID().toString();
						FileOutputStream output = new FileOutputStream(userImagePath + File.separator + uuidpic + ".jpg");
						output.write(pictureBytes);
						output.flush();
						output.close();
						Instant end = Instant.now();
						logger.info("图片保存成功：" + (userImagePath + File.separator + uuidpic + ".jpg"));
						logger.info("保存图片用时：" + (end.getNano() - start.getNano()) + "(纳秒)");
						
						// 保存图片的项目路径名，即不包括服务的ip和端口
						String likimg = ResourceHelper.getProperty("sc.lykimg.image.folder");

						String userImagePathName = likimg + "/" +userImageFolder+"/"+createTime+"/"+ uuidpic + ".jpg";
				
						String questionDescr = requestJsonMap.get("questionDescr").toString();
						Map<String,String> map= qaUserBusiness.uploadProblem(requestJsonMap.get("userId").toString(),requestJsonMap.get("courseName").toString(),userImagePathName,questionDescr);
						data.putAll(map);
					} catch (IOException e) {
						logger.info(e.getMessage(), e.getCause());
						notice = messageSourceHelper.getMessage("tu.upload.user.picture.failure");
					}
				} else {
					logger.error("图片的base64字符串格式不正确");
					notice = messageSourceHelper.getMessage("common.network.anomaly");
				}
				
				

			}
		}.execute();

		
	}
	
	/*
	 * 得到回复列表
	 */
	@POST
	@Path("/replyList")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> replyList(@FormParam("jsonInfo") String jsonInfo) throws Exception {
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				List<Map<String,Object>> replyList = qaUserBusiness.replyList(requestJsonMap.get("pageNumber").toString(),requestJsonMap.get("problemId").toString());
				String totalPage=qaUserBusiness.replyListCount(requestJsonMap.get("problemId").toString());
				data.put("replyList", replyList);
				data.put("totalPage",totalPage);
			}
		}.execute();

	}
	/*
	 * 回答问题
	 */
	@POST
	@Path("/uploadReply")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object>  uploadReply(@FormParam("jsonInfo") String jsonInfo) throws Exception{
		String templateJson = "{\"userId\":\"1e1174a8-2ebc-11e5-9d7f-525400c8f09f\",\"problemId\":\"088cfe69-0837-4485-88b5-289b85f3f9e2\",\"quoteReplyId\":\"\",\"replyDescr\":\"这是什么球1\",\"picture\":\"\"}";

		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
					Format format = new SimpleDateFormat("yyyy-MM-dd");
				    String createTime=  format.format(new Date()).toString();
					
					if (!StringUtils.isEmpty(createTime)){
						createTime = createTime.substring(0, 10);
					}
					
					File webappsPath = new File(request.getSession().getServletContext().getRealPath("/")).getParentFile();
					if (!webappsPath.canWrite()){
						logger.error(webappsPath + "不能写");
					}
					
					String imageFolder = ResourceHelper.getProperty("sc.image.service.file.path");
					String userImageFolder = ResourceHelper.getProperty("sc.problem.image.folder");
					String userImageDir = imageFolder + "/" + userImageFolder + "/" + createTime;
					File userImagePath = new File(userImageDir);
					if (!userImagePath.exists()){
						userImagePath.mkdirs();
					}
					String userImagePathName ="";
					if(requestJsonMap.get("picture")!=null&&!"".equals(requestJsonMap.get("picture").toString())){
			
						String picture = requestJsonMap.get("picture").toString();
						
						boolean isValidString = Base64.isBase64(picture);
						
						if (isValidString){
							byte[] pictureBytes = Base64.decodeBase64(picture);
							try {
								Instant start = Instant.now();
								String uuidpic=UUID.randomUUID().toString();
								FileOutputStream output = new FileOutputStream(userImagePath + File.separator + uuidpic + ".jpg");
								output.write(pictureBytes);
								output.flush();
								output.close();
								Instant end = Instant.now();
								logger.info("图片保存成功：" + (userImagePath + File.separator + uuidpic + ".jpg"));
								logger.info("保存图片用时：" + (end.getNano() - start.getNano()) + "(纳秒)");
								String likimg = ResourceHelper.getProperty("sc.lykimg.image.folder");
	
								// 保存图片的项目路径名，即不包括服务的ip和端口
								 userImagePathName = likimg + "/" +userImageFolder+"/"+createTime+"/"+ uuidpic + ".jpg";
	
								
							
								
							} catch (IOException e) {
								logger.info(e.getMessage(), e.getCause());
								notice = messageSourceHelper.getMessage("tu.upload.user.picture.failure");
							}
						} else {
							logger.error("图片的base64字符串格式不正确");
							notice = messageSourceHelper.getMessage("common.network.anomaly");
						}
					}
					
					String userId=requestJsonMap.get("userId").toString();
					String problemId=requestJsonMap.get("problemId").toString();
					String quoteReplyId=requestJsonMap.get("quoteReplyId").toString();
					String replyDescr = requestJsonMap.get("replyDescr").toString();
					Map<String,String> map=qaUserBusiness.uploadReply(userId,problemId,quoteReplyId,replyDescr,userImagePathName);
					data.putAll(map);
			}
		}.execute();

		
	}
	/*
	 * 得到追问信息
	 */
	@POST
	@Path("/questionCloselyList")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> questionCloselyList(@FormParam("jsonInfo") String jsonInfo) throws Exception {
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				List<Map<String,Object>> questionCloseList = qaUserBusiness.questionCloselyList(requestJsonMap.get("replyId").toString());
				data.put("questionCloseList", questionCloseList);
			
			}
		}.execute();

	}
	/*
	 *我的提问列表
	 */
	@POST
	@Path("/myProblemList")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> myProblemList(@FormParam("jsonInfo") String jsonInfo) throws Exception {
		String templateJson ="{\"userId\":\"00023434\",\"pageNumber\":\"1\",\"courseName\":\"行测\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				List<Map<String,Object>> problemList = qaUserBusiness.myProblemList(requestJsonMap.get("userId").toString(),requestJsonMap.get("pageNumber").toString(),requestJsonMap.get("courseName").toString());
				data.put("problemList", problemList);
				String totalPage=qaUserBusiness.myProblemListCount(requestJsonMap.get("userId").toString(),requestJsonMap.get("courseName").toString());
				data.put("totalPage", totalPage);
		
			}
		}.execute();

	}
	/*
	 *我回复过的问题列表
	 */
	@POST
	@Path("/myReplyList")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> myReplyList(@FormParam("jsonInfo") String jsonInfo) throws Exception {
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				List<Map<String,Object>> problemList = qaUserBusiness.myReplyList(requestJsonMap.get("userId").toString(),requestJsonMap.get("pageNumber").toString(),requestJsonMap.get("courseName").toString());
				data.put("problemList", problemList);
				String totalPage=qaUserBusiness.myReplyListCount(requestJsonMap.get("userId").toString(),requestJsonMap.get("courseName").toString());
				data.put("totalPage", totalPage);
		
			}
		}.execute();

	}
	/*
	 *求助
	 */
	@POST
	@Path("/seekhelpAnswer")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> seekhelpAnswer(	@FormParam("jsonInfo") String jsonInfo) throws Exception {
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String seekhelpTimes = qaUserBusiness.seekhelpAnswer(requestJsonMap.get("userId").toString(),requestJsonMap.get("problemId").toString());
				data.put("seekhelpTimes", seekhelpTimes);
			
			}
		}.execute();

	}
	
	@POST
	@Path("/acceptReply")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> acceptReply(	@FormParam("jsonInfo") String jsonInfo) throws Exception {
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				qaUserBusiness.acceptReply(requestJsonMap.get("userId").toString(),requestJsonMap.get("replyId").toString());
		
			
			}
		}.execute();

	}
	
	@POST
	@Path("/thanksReply")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> thanksReply(	@FormParam("jsonInfo") String jsonInfo) throws Exception {
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				qaUserBusiness.thanksReply(requestJsonMap.get("userId").toString(),requestJsonMap.get("replyId").toString());
		
			
			}
		}.execute();

	}
}
 