package com.leyikao.onlinelearn.serviceapp.td.business;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.leyikao.onlinelearn.serviceapp.td.dao.ICoreDao;
import com.leyikao.onlinelearn.serviceapp.td.dao.ICourseDao;
import com.leyikao.onlinelearn.serviceapp.td.dao.IUserDao;

public class CoreBusiness {
	private String submitPaper = "submitPaper";// 交卷
	private String loginMobileClient = "loginMobileClient";// 登陆
	private String thirdPartyRegister = "thirdPartyRegister";// 第三方登陆
	private String acceptReply = "acceptReply";// 采纳actionkey值
	private String unacceptReply = "unacceptReply";// 被采纳
	private String uploadUserPicture = "uploadUserPicture";// 上传用户头像
	@Autowired
	private ICoreDao coreDao;
	@Autowired
	private IUserDao userDao;
	Integer integralNumber = 0;// 改变的积分
	String uId = "";
	boolean tag=false;
	public void doBefore(JoinPoint jp) {//改方法计算本次操作增减积分数
		try {

			String menthname = jp.getSignature().getName();
			ObjectMapper objectMapper = new ObjectMapper();
			Map<?, ?> map = objectMapper.readValue(jp.getArgs()[0].toString(),	Map.class);
			List<Map<String, Object>> integralRule = coreDao.findIntegralRule(menthname);

				if (integralRule != null && integralRule.size() > 0) {
					Object userId = map.get("userId");
					if(userId!=null){
					uId = userId.toString();
					}
					integralNumber = Integer.parseInt(integralRule.get(0).get("integralNumber").toString());
					String actionId = integralRule.get(0).get("actionId").toString();
					
					if (uploadUserPicture.equals(menthname)) {// 上传头像积分
						integralNumber = uploadUserPictureCoin(uId,
								integralNumber, actionId);
					}
					
					if (loginMobileClient.equals(menthname)) {// 登陆
						String mobilePhone=map.get("username").toString();
						List<Map<String, String>> userInfoList = userDao.queryUser(mobilePhone);
						uId=userInfoList.get(0).get("userId");
						integralNumber = loginClientCoin(uId, integralNumber,
								actionId);
					}

					if (acceptReply.equals(menthname)) {// 被采纳人加分
						integralRule = coreDao.findIntegralRule(unacceptReply);
						Object replyId = map.get("replyId");
						Map<String,String> repmap=coreDao.findReply(replyId.toString());
						uId=repmap.get("userId");
						integralNumber = Integer.parseInt(integralRule.get(0).get("integralNumber").toString());
					}
					
					if (submitPaper.equals(menthname)) {// 交卷
						integralNumber = submitPaperCoin(uId, integralNumber,
								menthname);
					}
					if (thirdPartyRegister.equals(menthname)) {// 第三方登陆
						uId=map.get("thirdPartyLoginId").toString();
						integralNumber = loginClientCoin(uId, integralNumber,
								actionId);
					}
					tag=true;

				}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {//将改变的积分数返回给客户端

		Object retVal = pjp.proceed();
		String menthname = pjp.getSignature().getName();
		if (tag) {//更新总积分返回改变积分数
			Map<String, Object> resultmap = (Map<String, Object>) retVal;
			Map<String, Object> datamap = (Map<String, Object>) resultmap.get("data");
			datamap.put("changecoin", integralNumber);
		
			if (thirdPartyRegister.equals(menthname)) {// 第三方登陆
				coreDao.updateUserCoreBythirdPartyLoginId(uId,integralNumber);
			}else{
				coreDao.updateUserCore(uId, integralNumber);
			}
			
		}
		return retVal;
	}

	public void doThrowing(JoinPoint jp, Throwable ex) {

	}
	public void doAfter(JoinPoint jp) {//操作执行完后更新数据库

	}
	
	//交卷计算总积分
	private int submitPaperCoin(String uId, int integralNumber,
			String actionId) {
		Map<String,Object> map=coreDao.countPractice(uId);
		long countcore=(Long)map.get("countcore");
		int core=(int)countcore;
		return core*integralNumber;
	}
	//每天登陆计算积分
	private int loginClientCoin(String uId, int integralNumber, String actionId) {
		// TODO Auto-generated method stub
		 List<Map<String, Object>> map = coreDao.findIntegralPool(uId, actionId);
		if (map == null || map.size() == 0) {
			coreDao.insertIntegralPool(uId, actionId, integralNumber);
			return integralNumber;
		}
		Timestamp lasttime = (Timestamp) map.get(0).get("lastModifyTime");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String nowstring = df.format(date.getTime()) + " 00:00:00";

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		String afterstring = df.format(date.getTime()) + " 00:00:00";

		if (lasttime.after(Timestamp.valueOf(nowstring))
				&& lasttime.before(Timestamp.valueOf(afterstring))) {// 表示今天登陆过
			return 0;
		}

		coreDao.updateIntegralPool(uId, actionId,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		return integralNumber;
	}

	private int uploadUserPictureCoin(String uId, int integralNumber,
			String actionId) {
		// TODO Auto-generated method stub
		 List<Map<String, Object>>  map = coreDao.findIntegralPool(uId, actionId);
		if (map == null || map.size() == 0) {
			coreDao.insertIntegralPool(uId, actionId, integralNumber);
			return integralNumber;
		}
		return 0;
	}

}
