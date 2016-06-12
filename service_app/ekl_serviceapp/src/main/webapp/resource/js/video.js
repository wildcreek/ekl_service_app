if(course.videoUrl&&course.videoUrl!="false"){
	thePlayer=jwplayer('video').setup({	
		file: course.videoUrl.replace(/\.flv\s*$/,".mp4"),
		autostart:"true",
		width:"100%",
		startparam:"starttime",
		aspectratio:"16:9",
		image:course.imageUrl,
		primary:"html5",
		autochange:true,
        events: {
            onComplete: function(){
                sentLearnTime();
            }
        }
	});	
}

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
	var sentLearnTime=(function(){
		if(!pageInfo.mid) return
	 	var _params={},
	 		lastTime=0,
	 		startTime=new Date().getTime();
		var fn;
	    _params.mid=pageInfo.mid;

	    window.setInterval(fn=function(){
			var overTime,
				stayTime;
			if(typeof(thePlayer)!='object') return //no video no time;
			overTime=new Date().getTime();
			stayTime=parseInt(overTime-startTime)/1000;

			_params.time=stayTime-lastTime;
			_params.learn_time =thePlayer.getPosition();
	
			$.ajax({
				url:'/course/ajaxmediauser/',
				data:_params,
				type:"POST",
				dataType:'json',
				success:function(data){
					if(data.result== '0'){
          				if(data.data.media){
          					$(".course-section-current .section-state-icon").removeClass('section-state-icon-learning').addClass('section-state-icon-learnt')
          				}
					} 
				}
			});
		},60000);

		window.onbeforeunload=function(){
			var overTime,
				stayTime;
			if(typeof(thePlayer)!='object') return //no video no time;
			overTime=new Date().getTime();
			stayTime=parseInt(overTime-startTime)/1000;

			_params.time=stayTime-lastTime;
			_params.learn_time =thePlayer.getPosition();
	
			$.ajax({
				url:'/course/ajaxmediauser/',
				data:_params,
				type:"POST",
				async:false,
				dataType:'json',
				success:function(data){
					if(data.result=='0'){
						lastTime=stayTime;
					} 
				}
			});
		}

		return fn;
	})();

 $('.course-sections').on('click', 'a', function(e){
      if (!pageInfo.islogin && course.allowId && $(this).attr('data-id') != course.allowId) {
          e.preventDefault();
          if (!$('.pop-prevent').length) {
              $('body').append('<div class="pop-prevent" ><div class="t"><div class="tb"><div class="tw"><p>请下载APP或登录观看完整课程</p><a href="/wap/login">登录/注册</a><a href="http://www.imooc.com/mobile/appdown">下载APP</a></div></div></div></div>')
         }
     }
});

