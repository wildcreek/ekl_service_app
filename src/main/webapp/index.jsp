<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String userId= request.getParameter("userId");
	//request.getSession().setAttribute("userId", userId);
	response.sendRedirect(basePath+"index/list?userId="+userId);
%>

