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
<link rel="stylesheet" href="<%=basePath%>resource/css/normalize.css" />
<link rel="stylesheet" href="<%=basePath%>resource/css/main.css" />
<link rel="stylesheet" href="<%=basePath%>resource/js/esimakin-twbs-pagination-fbdd6fc/pagination.css" />
</head>

<body>
	<div class="main-container">
		<div class="content-contianer">
			<div class="middle-layer">
				<!--
                <div class="toapp-banner ">
                <a id="js-toapp" data-search="courseId=458" href="javascript:;">
                    <img src="<%=basePath%>resource/img/banner.jpg?1" width="100%"></a>
                <span id="js-toapp-close">×</span>
            </div>
            -->
				<div class="headbar">
					<h1 class="headbar-title header-learn" style="color:#302722">
						${course.courseName} <span>${course.num}人在学习</span>
					</h1>
					<a href="<%=basePath%>index/list?userId=${userId}" id="go-all-course" class="go-all-course">
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
						<div id="video" style="position: relative;">
							<img src="<%=basePath%>resource/img/img-holder.png" width="100%"
								alt=""> <img src="<%=basePath%>${course.photoUrl}" 
								width="100%" style="position: absolute; top: 0; left: 0;" />
						</div>
					</div>
					<ul class="course-tab">
						<li class="curr">章节</li>
						<li>课程详情</li>
					</ul>
					<div class="course-container">
						<div class="js-tab">
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
						<div class="js-tab" style="display: none">
							<ul class="course-intro">
								<li>
									<h2 class="chapter-title">课程名称</h2>
									<p class="course-intro-title">${course.courseName}</p>
								</li>
								<li>
									<h2 class="chapter-title">课程介绍</h2>
									<p>${course.introduction}</p>
								</li>
								<li>
									<h2 class="chapter-title">评论</h2>
									<p>
									<ul id="commentlist">
										<c:if test="${!empty cmlist}" >
											<c:forEach items="${cmlist}" var="cm">
											<li>
												<p class="bor">
													<span class="chapter-intro-user"> <img
														src='${cm.userPhoto}'
														width='50' height='50' /> ${cm.user.userName} <span
														title="创建时间" class="l timeago">${cm.stCommentTime}</span>
													</span> ${cm.content}
												</p>
											</li>
											</c:forEach>
										</c:if>
										

									</ul>
								</li>
							</ul>
							<br />
							<!-- 分页开始   -->
							<div style="text-align: center;">
							 <ul id="Pagination" class="pagination-sm"></ul>
							 </div>
							<!-- 分页结束 -->
							<div class="sub_text">
								<input id="commentext" type="text" name="commentext">
								<button id="combut" type="button">发表评论</button>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Add your site or application content here -->

	<script src="<%=basePath%>resource/js/lib/zepto.min.js"></script>
	<script src="<%=basePath%>resource/js/common/common.js?t=150716"></script>
	<script src="<%=basePath%>resource/js/learn.js?t=12"></script>



<script type="text/javascript" src="<%=basePath%>resource/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resource/js/esimakin-twbs-pagination-fbdd6fc/jquery.twbsPagination.min.js"></script>
<script>
		var num_entries = ${comCount};
		if(num_entries==0){
			num_entries=1;
		}
		function myPage(total){
			$('#Pagination').twbsPagination({
			      totalPages: total%10!=0?total/10+1:total/10,
			      visiblePages: 3,
			      prev:'上一页',
			      next:'下一页',
			      first:false,
			      last:false,
			      onPageClick: function (event, page) {
			    	  initPages(page-1);
			      }
			  });
		}
		myPage(num_entries);
		var pageInfo = {};
		pageInfo.islogin = 1;
		var course = course || {};
		course.videoUrl = "";
		course.imageUrl = "http://img.mukewang.com/55b0547f000129f906000338-280-160.jpg"
		course.allowId = ""

		var pageSize = 10;

		$("#combut").click(function() {
			var text = $("#commentext").val();
			$.ajax({
				type : "POST",
				dataType : "json",
				url : 'addComment',
				data : {
					"text" : text,
					"courseId" : "${course.courseId}",
					"userId" : "${userId}"
				},
				success : function(data) {
					if (data.success) {

						initPages(0);
						$("#commentext").val("");
						$('.page').unbind('page');
						myPage(data.comCount);
						/* $("#Pagination").pagination(data.comCount, {
							num_edge_entries : 1, // 结尾显示1条
							num_display_entries : 5, // 超过5条 隐藏
							callback : pageselectCallback,
							items_per_page : pageSize
						//pageSize 每页几条
						}); */
					} else {
						alert("评论发表失败！");
					}

				}
			});
		});
		//分页 ---------------------

		// Create pagination element
		/* $("#Pagination").pagination(num_entries, {
			num_edge_entries : 1, // 结尾显示1条
			num_display_entries : 5, // 超过5条 隐藏
			callback : pageselectCallback,
			items_per_page : pageSize
		//pageSize 每页几条
		}); */
		
		function pageselectCallback(page_index, jq) {
			initPages(page_index);
			return false;
		}

		function initPages(pageIndex) {
			$.ajax({
						type : "POST",
						dataType : "json",
						url : 'commentPage',
						data : {
							"pageIndex" : pageIndex + 1,
							"pageSize" : pageSize,
							"courseId" : "${course.courseId}"
						},
						success : function(data) {
							//alert(data.cmlist[1].content);
							var comhtml = "";
							for (var i = 0; i < data.cmlist.length; i++) {
								comhtml = comhtml
										+ "<li>"
										+ "<p class=\"bor\">"
										+ "<span class=\"chapter-intro-user\">"
										+
										//"<img src=\""+data.cmlist[i].user.picUrl+"\" width='50' height='50' />"+  
										"<img src='"+data.cmlist[i].userPhoto+"' width='50' height='50' />"
										+ data.cmlist[i].user.userName
										+ "<span title=\"创建时间\" class=\"l timeago\">时间："
										+ data.cmlist[i].stCommentTime
										+ "</span>" + "</span>"
										+ data.cmlist[i].content + "</p>"
										+ "</li>"

							}

							$("#commentlist").html(comhtml);

						}
					});

		}
		function dianzan(){

			$.ajax({
				type:"post",
				dataType:"json",
				url:"dianzan",
				data:{courseId:'${course.courseId}',
					  userId : "${userId}"},
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
</body>
</html>