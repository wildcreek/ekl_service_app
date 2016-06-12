package com.leyikao.onlinelearn.serviceapp.v.business;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leyikao.onlinelearn.serviceapp.util.ToolUtil;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Course;
import com.leyikao.onlinelearn.serviceapp.v.pojo.CourseType;
import com.leyikao.onlinelearn.serviceapp.v.service.IndexService;

@Controller
@RequestMapping("/index")
public class IndexController {
	
	public static int PAGE_SIZE = 10;
	
	@Resource
	private IndexService indexService; 
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model){
		boolean results = false;
		String transactionid = request.getParameter("transactionid");
		String auth = request.getParameter("auth");
		if (ToolUtil.checkNumber(transactionid) && ToolUtil.strNotNull(auth)) {
			String md5 = ToolUtil.MD5(ToolUtil.MD5(transactionid) + "ZCB");
			if (md5.equals(auth)) {
				Long s = (System.currentTimeMillis() - Long.valueOf(transactionid)) / 1000;
				if (s <= 45 && s >= 0) {
					results = true;
				}
			}
		}
		
		//String userId= request.getSession().getAttribute("userId")==null ?null :request.getSession().getAttribute("userId").toString();
		String typeCode= request.getParameter("typeCode");
		String userId = request.getParameter("userId");
		List<Course> list = indexService.getCourseList(PAGE_SIZE,typeCode);
		List<CourseType> typeList= indexService.getCourseTypeList();
		int sum = 0;
		for (CourseType ct : typeList) {
			sum += ct.getCourseNum();
		}
		model.addAttribute("courses", list);
		model.addAttribute("typeList", typeList);
		model.addAttribute("typeCode", typeCode);
		model.addAttribute("sum", sum);
		model.addAttribute("userId", userId);
		return "index";
	}
	@ResponseBody
	@RequestMapping("/pageList")
	public Map<String, Object> pageList(HttpServletRequest request){
		String pageNum = request.getParameter("pageNum");
		String typeCoder = request.getParameter("typeCode");
		String typeCode= "".equals(typeCoder)?null :typeCoder;
		List<Course> list = indexService.getCoursePageList(PAGE_SIZE,typeCode,pageNum);
		Map<String ,Object> map= new HashMap<String ,Object>();
		map.put("success", "success");
		map.put("pageList", list);
		return map;
	}

}
