package com.leyikao.onlinelearn.serviceapp.dao.mybatis;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.leyikao.onlinelearn.serviceapp.td.dao.mybatis.TerminalUserDao;
import com.leyikao.onlinelearn.serviceapp.util.MessageSourceHelper;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class) 
/*@ContextConfiguration("file:src/main/resources/spring-bean.xml") */
/*@ContextConfiguration("file:WEB-INF/classes/spring-bean.xml")*/
@ContextConfiguration(locations={"classpath:*/WEB-INF/classes/spring-bean.xml"})
@Transactional
public class UserDaoTest {
	private final Logger logger = LoggerFactory.getLogger(UserDaoTest.class);
	
	private String userId;
	private String mobilePhone = "18612344321";
	
	@Resource
	TerminalUserDao userDao;
	
	@Autowired
	private MessageSourceHelper messageSourceHelper;
	
	@Before
	public void before(){
		int effectRows = userDao.registerMobileClient(mobilePhone, "123qwe", "wind");
		assertTrue(effectRows == 1);
		
		List<Map<String, String>> userInfo = userDao.queryUser(mobilePhone);
		assertTrue(userInfo.size() == 1);
		
		userId = userInfo.get(0).get("userId");
		assertTrue(!StringUtils.isEmpty(userId));
	}
	
	@After
	public void after(){
		int effectRows = userDao.deleteMobilePhoneUser(mobilePhone);
		assertTrue(effectRows == 1);
	}
	
	@Test
	public void saveExamType(){
		String nationalExam = messageSourceHelper.getMessage("tu.exam.type.national.exam");
		int effectRows = userDao.saveExamType(userId, nationalExam);
		assertTrue(effectRows == 1);
		
		List<Map<String, String>> userInfo = userDao.queryUser(mobilePhone);
		assertTrue(userInfo.size() == 1);
		
		String currentExamType = userInfo.get(0).get("currentExamType");
		logger.info("examTcurrentExamTypeype: " + currentExamType);
		assertTrue(nationalExam.equals(currentExamType));
	}
}
