<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>全部课程</title>
<meta name="description"
	content="免费学习平台，创新的网络一站式学习、实践体验；服务及时贴心，内容专业、有趣易学。" />
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1">
<meta http-equiv="cleartype" content="on">
<meta name="msapplication-TileImage"
	content="<%=basePath%>resource/img/touch/apple-touch-icon-144x144-precomposed.png">
<meta name="msapplication-TileColor" content="#222222">
<link rel="stylesheet" href="<%=basePath%>resource/css/normalize.css">
<link rel="stylesheet" href="<%=basePath%>resource/css/main.css">
<script type="text/javascript">var base = "<%=basePath%>";</script>
<style>

</style>
</head>

<body>
	<!--
  <ul class="nav-wrap">
    <li class="nav-item-list curr">
      <a href="<%=basePath%>resource/list">
        <span></span>
        课程
      </a>
    </li>
    <li class="nav-item-course">
      <a href="<%=basePath%>resource/course">
        <span></span>
        我的
      </a>
    </li>
    <li class="nav-item-app">
      <a href="<%=basePath%>resource/app">
        <span></span>
        下载APP
      </a>
    </li>
  </ul>
  -->
	<div class="main-container">
		<div class="content-contianer">
			<div class="middle-layer">
				<!--
        <div class="toapp-banner ">
          <a id="js-toapp" data-search="list" href="javascript:;">
            <img src="<%=basePath%>resource/img/banner.jpg?1" width="100%"></a>
          <span id="js-toapp-close">×</span>
        </div>
        -->
				<div class="headbar">
					<h1 class="headbar-title js-course-sort" style="color:#302722">
						<em></em> 全部课程
					</h1>
					<a href="sdh://ykl.fanhui.com" id="go-all-course" class="go-all-course">
						<i></i>
					</a> 
					
					<ul id="course-filter-wrap" class="course-filter-wrap js-sidebar-lang">
						<li style="background-image: url('<%=basePath%>resource/img/all.png')">
							<a class="curr" data-id="" data-title="全部课程" href="javascript:void(0)">全部课程(${sum})</a>
						</li>
						<c:forEach items="${typeList}" var="type">
						<li style="background-image: url('${type.ptoUrl}')">
							<a class="" data-id="${type.typeCode}" data-title="${type.typeName}"
							href="javascript:void(0)">${type.typeName}(${type.courseNum})</a>
						</li>
						</c:forEach>
					</ul>

				</div>
				<div class="content-inner">
						<div class="course-list js-course-list">
							<ul id="p-1">
								<c:forEach items="${courses}" var="course" >
								<li class="course-list-item clearfix" onclick="javascript:forword(${course.courseId})">
									<div class="item-avator">
										<a href="<%=basePath%>learn/course?courseId=${course.courseId}&userId=${userId}"> 
										<img style="width: 100%;height:100%" src="<%=basePath%>${course.photoUrl}" width="160" alt="">
										</a>
									</div>
									<div class="item-content">
										<h3 class="item-header">
											<a href="<%=basePath%>learn/course?courseId=${course.courseId}&userId=${userId}" style="color:#3a312c">${course.courseName}</a>
										</h3>
										<div class="item-footer clearfix">
											<span class="item-footer-num"><i></i>${course.num}人</span> <span
												class="item-footer-splitor">|</span> <span
												class="item-footer-dur"><i></i>${course.timelong}分钟</span>
										</div>
									</div>
								</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				
			</div>
		</div>
	</div>

	<!-- Add your site or application content here -->
	<script src="<%=basePath%>resource/js/lib/zepto.min.js"></script>
	<script src="<%=basePath%>resource/js/common/common.js?t=150716"></script>
	<div style="display: none">
		<!-- 代码统计区 -->
	</div>
	<script src="<%=basePath%>resource/js/common/string.js?t=1.0"></script>
	<script src="<%=basePath%>resource/js/list.js?t=1.89"></script>
	<script> 
		function forword(e){
			window.location.href="<%=basePath%>learn/course?courseId="+e+"&userId=${userId}";
			
		}
	</script>
</body>
</html>