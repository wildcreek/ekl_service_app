$(function(){
/*if(course.videoUrl&&course.videoUrl!="false"){
	thePlayer=jwplayer('video').setup({
		file: course.videoUrl.replace(/\.flv\s*$/,".mp4"),
		autostart:"false",
		width:"100%",
		startparam:"starttime",
		aspectratio:"16:9",
		image:course.imageUrl,
		primary:"html5",
		autochange:true
	});
}*/
$(".chapter-title").on("click",function(){

	var obj=$(this).find("span")
	if(obj.hasClass('open')){
		obj.removeClass('open')
		$(this).parent("li").find('ul').show();
	}else{
		obj.addClass('open')
		$(this).parent("li").find('ul').hide();
		return
	}
})

	$(".course-tab li").on("click",function(){
		var obj=$(this)
		var index=obj.index()
		obj.addClass('curr').siblings().removeClass('curr')
		$(".js-tab").hide().eq(index).show()

	})
});
$('.course-sections').on('click', 'a', function(e){
	if (!pageInfo.islogin && course.allowId && $(this).attr('data-id') != course.allowId) {
		e.preventDefault();
		if (!$('.pop-prevent').length) {
			$('body').append('<div class="pop-prevent" ><div class="t"><div class="tb"><div class="tw"><p>请下载APP或登录观看完整课程</p><a href="/wap/login">登录/注册</a><a href="http://www.imooc.com/mobile/appdown">下载APP</a></div></div></div></div>')
		}
	}
})

//关注课程
$(function(){

	var isAjax=0;
	$('.js-btn-collection').on('click',function(){

		if(isAjax) return;
		isAjax=1;
		var obj=$(this);
		var id=obj.attr("data-id")
		var url="/ajaxfollow.json";
		if(obj.hasClass('btn-remove-collection')){
			url="/ajaxfollow.json";//取消关注
		}
		$.ajax({
			type: "GET",//java编码阶段请把此请求改为post
			url: url,
			dataType:"json",
			data: {
				course_id:id
			},
			success: function(res){
				isAjax=0;
				if(res.result==0){
					if(obj.hasClass('btn-remove-collection')){
						obj.removeClass('btn-remove-collection')
					}else{
						obj.addClass('btn-remove-collection')
					}
				}
			},
			error:function(){
            	isAjax=0
        	}
		});
	});
})
