package com.leyikao.onlinelearn.serviceapp.td.webservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;

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

import com.leyikao.onlinelearn.serviceapp.td.business.TerminalUserBusiness;
import com.leyikao.onlinelearn.serviceapp.util.MessageSourceHelper;
import com.leyikao.onlinelearn.serviceapp.util.ResourceHelper;
import com.leyikao.onlinelearn.serviceapp.util.WebServiceTemplate;

@Path("/tuService")
public class TerminalUserWebService {
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private TerminalUserBusiness userBusiness;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private MessageSourceHelper messageSourceHelper;
	
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * 手机用户注册
	 * 处理逻辑 1，验证码有效期检查；2，重复注册检查
	 * 
	 * @param jsonInfo：{"mobilePhone":"13512312311","verificationCode":"121325","password":"999999"}
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/registerMobileClient")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> registerMobileClient(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {

		String templateJson = "{\"mobilePhone\":\"13512312311\",\"verificationCode\":\"121325\",\"password\":\"999999\",\"nickname\":\"风一样的\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String mobilePhone = requestJsonMap.get("mobilePhone").toString();
				String verificationCode = requestJsonMap.get("verificationCode").toString();
				String password = requestJsonMap.get("password").toString();
				String nickname = requestJsonMap.get("nickname").toString();
				notice = userBusiness.registerMobileClient(mobilePhone, verificationCode, password, nickname);
				if (StringUtils.isEmpty(notice)){
					notice = messageSourceHelper.getMessage("tu.register.success");
					isSuccess = true;
				}
			}
		}.execute();
	}
	
	/**
	 * app 用户登录
	 * @param jsonInfo
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/loginMobileClient")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> loginMobileClient(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {

		String templateJson = "{\"username\":\"13512312300\",\"password\":\"999999\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String userName = requestJsonMap.get("username").toString();
				String password = requestJsonMap.get("password").toString();
				Map<String, String> userInfo = userBusiness.loginMobileClient(userName,	password);
				if(userInfo.containsKey("notice")){
					notice = userInfo.get("notice");
					userInfo.remove("notice");
				}
				if (userInfo.size() > 0 && !StringUtils.isEmpty(userInfo.get("userId"))){
					data.put("userInfo", userInfo);
				}
			}
		}.execute();
	}
	
	/**
	 * 第三方登录
	 * @param jsonInfo
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/thirdPartyLogin")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> thirdPartyLogin(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {

		String templateJson = "{\"thirdPartyLoginId\":\"abc13512x312311\",\"thirdPartyLoginType\":\"1\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				Map<String, String> userInfo = userBusiness.thirdPartyLogin(requestJsonMap.get("thirdPartyLoginId").toString(),
						requestJsonMap.get("thirdPartyLoginType").toString());
				
				boolean isSuccess = "1".equals(userInfo.get("isSuccess"));
				userInfo.remove("isSuccess");
				if (isSuccess) {
					data.put("userInfo", userInfo);
				} else {
					notice = messageSourceHelper.getMessage("tu.login.failure");
				}
			}
		}.execute();
	}
	  
	/**
	 * 密码修改
	 * @param jsonInfo
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/revisePassword")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> revisePassword(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {
		String templateJson = "{\"mobilePhone\":\"13512312311\",\"verificationCode\":\"122529\",\"password\":\"999999\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String mobilePhone = requestJsonMap.get("mobilePhone").toString();
				String verificationCode = requestJsonMap.get("verificationCode").toString();
				String password = requestJsonMap.get("password").toString();
				notice = userBusiness.revisePassword(mobilePhone, verificationCode, password);
				if (StringUtils.isEmpty(notice)){
					notice = messageSourceHelper.getMessage("tu.modify.password.success");
					isSuccess = true;
				}
			}
		}.execute();
	}
	
	
	/**
	 * 获取验证码
	 * @param jsonInfo
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/getVerificationCode")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> getVerificationCode(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {
		
		String templateJson = "{\"mobilePhone\":\"13512312311\",\"isRegistered\":\"1\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String mobilePhone = requestJsonMap.get("mobilePhone").toString();
				String temp = requestJsonMap.get("isRegistered").toString();
				boolean isRegistered = "1".equals(temp) ? true : false;
				/*Map<String, String> resultMap = userBusiness.generatorVerificationCode(mobilePhone, isRegistered);*/
				Map<String, String> resultMap = userBusiness.sendSMSVerificationCode(mobilePhone, isRegistered);
				String verificationCode = resultMap.get("verificationCode");
				notice = resultMap.get("notice");
				if (StringUtils.isEmpty(notice)){
					data.put("verifiCode", verificationCode);
				}
			}
		}.execute();
	}
	
	
	/**
	 * 参与试卷人数
	 * @param jsonInfo
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/examNumber")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> examNumber(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				Integer userCount = userBusiness.userCount();
				data.put("examNumber", userCount);
			}
		}.execute();
	}
	
	@POST
	@Path("/saveExamType")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> saveExamType(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {
		String templateJson = "{\"userId\":\"1304\",\"examType\":\"考研\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String userId = requestJsonMap.get("userId").toString();
				String examType = requestJsonMap.get("examType").toString();
				int effectRows = userBusiness.saveExamType(userId, examType);
				if (effectRows > 0){
					isSuccess = true;
				} else {
					notice = messageSourceHelper.getMessage("tu.exam.type.save.failure");
				}
			}
		}.execute();
	}
	
	@POST
	@Path("/uploadUserPicture")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> uploadUserPicture(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {
		String templateJson = "{\"userId\":\"00334\",\"picture\":\"234134\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String userId = requestJsonMap.get("userId").toString();
				
				Map<String, String> userInfo = userBusiness.queryUserById(userId);
				String createTime = userInfo.get("createTime");
				if (!StringUtils.isEmpty(createTime)){
					createTime = createTime.substring(0, 10);
				}
				
				File webappsPath = new File(ResourceHelper.getProperty("sc.image.service.file.path"));
				if (!webappsPath.canWrite()){
					logger.error(webappsPath + "不能写");
				}
				
				String userImageFolder = ResourceHelper.getProperty("sc.user.image.folder");
				String userImageDir = userImageFolder + "/" + createTime;
				File userImagePath = new File(webappsPath.getPath() + "/" + userImageDir);
				if (!userImagePath.exists()){
					userImagePath.mkdirs();
				}
				
				String picture = requestJsonMap.get("picture").toString();
				boolean isValidString = Base64.isBase64(picture);
				if (isValidString){
					byte[] pictureBytes = Base64.decodeBase64(picture);
					try {
						Instant start = Instant.now();
						FileOutputStream output = new FileOutputStream(userImagePath + File.separator + userId + ".jpg");
						output.write(pictureBytes);
						output.flush();
						output.close();
						Instant end = Instant.now();
						logger.info("图片保存成功：" + (userImagePath + File.separator + userId + ".jpg"));
						logger.info("保存图片用时：" + (end.getNano() - start.getNano()) + "(纳秒)");
						
						// 保存图片的项目路径名，即不包括服务的ip和端口
						String userImagePathName = userImageDir + File.separator + userId + ".jpg";
						int effectRows = userBusiness.saveUserImage(userId, userImagePathName);
						if (effectRows > 0){
							String webPath = ResourceHelper.getProperty("sc.image.service.web.path");
							data.put("picUrl", webPath + "/lykimg/" + userImagePathName);
							notice = messageSourceHelper.getMessage("tu.upload.user.picture.success");
							isSuccess = true;
						} else {
							notice = messageSourceHelper.getMessage("tu.upload.user.picture.failure");
						}
						
					} catch (IOException e) {
						logger.error(e.getMessage(), e.getCause());
						notice = messageSourceHelper.getMessage("tu.upload.user.picture.failure");
					}
				} else {
					logger.error("图片的base64字符串格式不正确");
					notice = messageSourceHelper.getMessage("common.network.anomaly");
				}
			}
		}.execute();
	}
	
	@POST
	@Path("/updateNickname")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> updateNickname(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {
	
		String templateJson = "{\"userId\":\"15bf62f4-165a-11e5-9ab5-525400c8f09f\",\"nickname\":\"你是谁\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String userId = requestJsonMap.get("userId").toString();
				String nickname = requestJsonMap.get("nickname").toString();
		
				Map<String, String> resultMap = userBusiness.updateNickname(userId,nickname);
				int effectRows = Integer.parseInt(resultMap.get("effectRows"));
				
				if(effectRows > 0){
					notice = messageSourceHelper.getMessage("tu.nickname.modify.success");
					isSuccess = true;
				} else {
					notice = resultMap.get("notice");
					if (StringUtils.isEmpty(notice)){
						notice = messageSourceHelper.getMessage("tu.nickname.modify.failure");
					}
				}
			}
		}.execute();
	
	}
	
	@POST
	@Path("/teacherInfoList")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> teacherInfoList(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {
	
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				data.put("teacherInfoList", userBusiness.teacherInfoList());
			}
		}.execute();
	
	}
	
	@POST
	@Path("/teacherImageList")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> teacherImageList(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {
	
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				data.put("teacherImageList", userBusiness.teacherImageList());
			}
		}.execute();
	}
	
	@POST
	@Path("/teacherDetail")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> teacherDetail(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {
	
		String templateJson = "{\"userId\":\"034556\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String userId = requestJsonMap.get("userId").toString();
				data.put("teacherDetail", userBusiness.teacherDetail(userId));
			}
		}.execute();
	}
	
	@POST
	@Path("/famousSayings")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> famousSayings(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {
	
		String templateJson = "{}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				data.put("contentList", userBusiness.famousSayings());
			}
		}.execute();
	}
	
	@POST
	@Path("/sendFeedback")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> sendFeedback(
			@FormParam("jsonInfo") String jsonInfo) throws Exception {
	
		String templateJson = "{\"userId\":\"0023\",\"score\":\"1\",\"feedbackContent\":\"响应速度太慢\"}";
		return new WebServiceTemplate(jsonInfo, templateJson, request.getRequestURI()){
			@Override
			protected void business() {
				String userId = requestJsonMap.get("userId").toString();
				String score = requestJsonMap.get("score").toString();
				String feedbackContent = requestJsonMap.get("feedbackContent").toString().trim();
				int effectRows = userBusiness.addFeedback(userId, score, feedbackContent);
				if (effectRows == 0) {
					notice = messageSourceHelper.getMessage("tu.send.feedback.save.failure");
				} else {
					notice = messageSourceHelper.getMessage("tu.send.feedback.save.success");
					isSuccess = true;
				}
			}
		}.execute();
	}
}
