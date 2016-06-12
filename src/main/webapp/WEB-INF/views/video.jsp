<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">

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
<script type="text/javascript"
	src="<%=basePath%>resource/js/jquery.min.js"></script>
</head>
<body>
	<div class="main-container">
		<div class="content-contianer">
			<div class="middle-layer">
				<!--
        <div class="toapp-banner ">
          <a id="js-toapp" data-search="sectionId=8913" href="javascript:;">
            <img src="<%=basePath%>resource/img/banner.jpg?1" width="100%"></a>
          <span id="js-toapp-close">×</span>
        </div>
        -->
				<div class="headbar">
					<h1 class="headbar-title header-learn" style="color:#302722">
						${course.courseName} <span>${course.num}人在学习</span>
					</h1>
					<a href="<%=basePath%>learn/course?courseId=${course.courseId}&userId=${userId}" id="go-all-course" class="go-all-course">
						<i></i>
					</a> 
					<c:if test="${course.admire==1}">
						<a id="dianzan" class="btn-add-collection  js-btn-collection btn-remove-collection" data-id="458"
							href="javascript:dianzan()"> <i></i>
						</a>
					</c:if>
					<c:if test="${course.admire==0}">
						<a id="dianzan" class="btn-add-collection  js-btn-collection" data-id="458"
							href="javascript:dianzan()"> <i></i>
						</a>
					</c:if>
					<c:if test="${course.admire==null}">
						<a id="dianzan" class="btn-add-collection  js-btn-collection" data-id="458"
							href="javascript:dianzan()"> <i></i>
						</a>
					</c:if>
				</div>

				<div class="content-inner">
					<div class="video-wrap clearfix">
						<div id="video" class="jwplayer aspectMode playlist-none jw-user-inactive" tabindex="0" style="width: 100%; background-color: rgb(0, 0, 0); display: inline-block; opacity: 1;">
							<div style="background: rgb(51, 51, 51) none repeat scroll 0% 0%; color: rgb(255, 255, 255); position: absolute; text-align: center; font: 14px/2em Microsoft YaHei, SimSun, Arial; width: 80px; height: 30px; left: 47%; top: 75%; z-index: 999; visibility: hidden;">播放</div>
							<span id="video_view" class="jwmain">
								<video 
								preload="none" 
								controls="" 
								x-webkit-airplay="allow" 
								poster="${imgUrl}" 
								width="600" height="490" 
								src="${mp4Url}">
								</video>
							</span>
						</div>
					</div>
					<div class="course-container">
						<ul class="course-chapter">
							<c:forEach items="${cclist}" var="cc">
								<li>
									<h2 class="chapter-title">
										<i></i> ${cc.chapter.chapterName} <span></span>
									</h2>
									<ul class="course-sections">
										<c:forEach items="${cc.list}" var="cp">
											<li><i class="section-icon section-icon-video"></i> <a
												data-id="8909"
												href="<%=basePath%>video/play?chapterId=${cp.chapterId}&courseId=${course.courseId}&userId=${userId}">${cp.chapterName}</a>
													<c:if test="${cp.schedule == 1}">
													<i class="section-state-icon section-state-icon-learnt" val="${cp.schedule}"></i>
													</c:if>
													<c:if test="${cp.schedule == 0}" >
													<i class="section-state-icon section-state-icon-learning" val="${cp.schedule}"></i>
													</c:if>
													<c:if test="${cp.schedule == null}">
													<i class="section-state-icon" val="${cp.schedule}"></i>
													</c:if>												

											</li>
										</c:forEach>
									</ul>
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
	<script>
		var time = window.setInterval(function(){ 
			if($("video").length > 0){
				$("video").attr("width","100%").removeAttr('height').on('canplay',function(){
					
					saveSchme(0);
				}).on('ended',function(){
					
					saveSchme(1);
				});
				window.clearInterval(time); 
			}
		}, 300); 
	
		function saveSchme(flag){
			$.ajax({
				type : "POST",
				dataType : "text",
				url : '<%=basePath%>video/saveSchem',
				data : {
					"courseId":"${course.courseId}",
					"chapterId":"${chapterId}",
					"videoId": "${video.videoId}",
					"flag":flag,
					"userId": "${userId}"
				},
				success : function(data) {
					
				}
			});
		}
		
		function dianzan(){
			$.ajax({
				type:"post",
				dataType:"json",
				url:"<%=basePath%>learn/dianzan",
				data:{courseId:'${course.courseId}',userId:"${userId}"},
				success:function(e){
					if(e.success=="1"){
						$("#dianzan").attr("class","btn-add-collection  js-btn-collection btn-remove-collection");
					}else if(e.success=="0"){
						$("#dianzan").attr("class","btn-add-collection  js-btn-collection");
					}
				}
			});

		}

	</script>
	<div style="display: none"></div>
</body>
</html>