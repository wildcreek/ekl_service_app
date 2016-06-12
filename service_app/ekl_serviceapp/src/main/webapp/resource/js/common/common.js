/**
 * wap模块通用工具类
 * @author zhangshaoliu
 * @date 2015-07-16
 */
var commonUtil = (function(win, $) {
	var doc = win.document;

	/**
	 * 若用户已安装app，则使用schema URL拉起APP，否则，跳转到下载页面，引导用户安装
	 * @param  {String} schemaStr 自定义协议字符串
	 */
	function launchAPP(schemaStr) {
		if(!schemaStr){
			return;
		}

		var timer;
		var url = 'mukewang://?' + schemaStr;
        var schemaShell = document.createElement('iframe');
        schemaShell.style.display = 'none';

        var clearSchemaShell = function() {
             if (!schemaShell) {
                 return;
             }
            clearTimeout(timer);
            ifr.onload = null;
			schemaShell.parentNode.removeChild(schemaShell);
            schemaShell = null;
         };

        schemaShell.onload = clearSchemaShell;
        schemaShell.src = url;
        document.body.appendChild(schemaShell);

		var t = Date.now();
		timer = setTimeout(function() {
			if (Date.now() - t > 500) {
				return;
			}
			location.href = '/wap/app';
		}, 480);
	}


	// 隐藏地址栏
	function hideUrlBarOnLoad() {
		// 如果有hash，就不做处理
		if(location.hash){
			return;
		}
		$(win).on('load', function() {
			setTimeout(function() {
				win.scrollTo(0, 1);
			}, 0);
		});
	};

	// 是否是微信平台
	function isWeChat() {
		var ua = navigator.userAgent.toLowerCase();
		if (ua.match(/MicroMessenger/i) == "micromessenger") {
			return true;
		}
		return false;
	}

	// Enable CSS active pseudo styles in Mobile browser
	function enableActive() {
		$(doc).on('touchstart', function() {});
	};

	// 阻止页面的默认滚动行为
	function preventPageScrolling() {

		// target hasScroll?
		function __hasScroll(target) {
			target.scrollTo(0, 1);
			return target.scrollTop ? true : false;
		}

		$(doc).on('touchmove', function(evt) {
			// if evte.target is Input Range or evt.target has scroll, stop here
			if (evt.target.type === 'range' || __hasScroll(evt.target)) {
				return;
			}
			evt.preventDefault();
		});
	};

	// 返回通用工具接口
	return {
		launchAPP: launchAPP,
		hideUrlBarOnLoad: hideUrlBarOnLoad,
		isWeChat: isWeChat,
		enableActive: enableActive,
		preventPageScrolling: preventPageScrolling
	}

})(window, window.Zepto);


/**
 * 通用组件逻辑交互
 * @author zhangshaoliu
 * @date 2015-07-16
 */
(function(win, $) {
	var doc = win.document;

	// 通过判断手势方向（上或下），对页面底部菜单栏做显隐切换
	function switchMenuBar(){
		var startX,
			starY,
			endX,
			endY;

		$(doc)
			.on('touchstart', function(evt) {
				var touch = evt.touches[0];
				startX = touch.pageX;
				starY = touch.pageY;
			})
			.on('touchmove', function(evt){
				var touch = evt.touches[0];
				endX = (touch.pageX - startX);
				endY = (touch.pageY - starY);

				if (Math.abs(endX) < Math.abs(endY) && Math.abs(endY) > 10) {
					if (endY > 0) {
						$(".nav-wrap").removeClass('nav-hide');
					} else {
						$(".nav-wrap").addClass('nav-hide');
					}
				}
          	});

	}

	// 打开慕课网应用banner条
	if($('.toapp-banner').length){
		// 如果不在微信平台
		if (!commonUtil.isWeChat()) {
			$('#js-toapp').click(function(e) {
				var schemaStr = $(this).data('search');
				schemaStr && commonUtil.launchAPP(schemaStr);
			});
		}

		// 关闭按钮
		$('#js-toapp-close').click(function() {
			$(this).parent().remove();
		});
	}

	$(function() {
		// 隐藏地址栏
		commonUtil.hideUrlBarOnLoad();

		// 激活按钮hover效果
		commonUtil.enableActive();

		// 触摸屏幕底部菜单栏显隐切换
		switchMenuBar();
	});

})(window, window.Zepto);