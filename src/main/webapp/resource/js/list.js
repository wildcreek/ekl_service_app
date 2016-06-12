/**
 * 课程列表页面业务逻辑
 * @author zhangshaoliu
 * @update 2015-07-14
 */
(function(global, $) {
	/**
	 * 数据请求状态
	 * YES 请求中
	 * NO  已完成
	 */
	var $win = $(global),
		state = {
			YES: 1,
			NO: 0
		},
		isRequest = state.NO, // 是否在请求数据，初始值为还未开始请求
		pageIndex = 0, // 当前页码
		isLastPage = false; // 是否是最后一页

	// loading icon切换
	var loadingIcon = {
		add: function() {
			$(".course-list").append('<span class="btn-loading"></span>');
		},
		remove: function() {
			$(".btn-loading").remove();
		}
	};

	/**
	 * 加载课程列表
	 * @param  {Nunber} page 页码
	 */
	function loadCourseList(page) {
		if (isRequest) {
			return;
		}

		page = page || 1;
		isRequest = state.YES;

		var _data = {
			typeCode: getCurrCourseTypeId(),
			pageNum: page,
		}

		$.ajax({
			url: base + "index/pageList",
			data: _data,
			method: "post",
			dataType: "json",
			beforeSend: function() {
				loadingIcon.add();
			},
			success: function(res) {
				var str = '',
					dataList = res.pageList;

				isLastPage = res.pageList.length < 10 ? true : false;

				if (checkData(dataList)) {
					str = data2Str(dataList, page);
					pageIndex = page * 1 + 1;
				} else {
					str = '<div class="nodata">没有相关课程</div>';
				}

				setTimeout(function() {
					loadingIcon.remove();
					$(".js-course-list").append(str);
					isRequest = state.NO;
				}, 500);
			},
			error: function() {
				loadingIcon.remove();
			}

		});
	}

	// 获取当前选中的课程类型的id
	function getCurrCourseTypeId() {
		return $(".js-sidebar-lang .curr").data("id");
	}

	//检查数据是否正确
	function checkData(data) {
		return data.length > 0;
	}

	// 将数据转为html串
	function data2Str(dataList, page) {
		var location = (window.location+'').split('/');
		var basePath = location[0]+'//'+location[2]+'/'+location[3];
		
		
		var str = '<ul id="p-' + page + '">';

		for (var k in dataList) {
			var dur = dataList[k].courseId,
				temp = Math.ceil(dur / 60) + "小时",
				coursename = dataList[k].courseName;

			if (dataList[k].courseName.strLen() > 36) {
				coursename = dataList[k].courseName.subCHStr(0, 34) + '..';
			}

			str += '<li class="course-list-item clearfix">';
			str += ' <div class="item-avator">';
			str += '    <a href="'+basePath+'/learn/course?courseId=' + dataList[k].courseId + '">';
			str += '      <img style="width: 100%;height:100%" src="' +basePath+"/"+ dataList[k].photoUrl + '" width="160" alt="">';
			str += '    </a>';
			str += '</div>';
			str += '<div class="item-content">';
			str += '    <h3 class="item-header">';
			str += '      <a href="'+basePath+'/learn/course?courseId=' + dataList[k].courseId + '">' + coursename + '</a>';
			str += '    </h3>';

			str += '    <div class="item-footer clearfix">';
			str += '      <span class="item-footer-num"><i></i>' + dataList[k].num + '人</span>';
			str += '      <span class="item-footer-splitor">|</span>';
			str += '      <span class="item-footer-dur"><i></i>' +  dataList[k].timelong + '分钟</span>';
			str += '    </div>';
			str += '</div>';
			str += '</li> ';
		}

		str += "</ul>";
		return str;
	}

	// 添加事件监听
	var addEventMonitor = function() {
		var $allCourseEl = $(".js-course-sort"),
			$courseFilterEl = $(".js-sidebar-lang"),
			$courseListEl = $(".js-course-list");

		// 点击全部课程的交互
		$allCourseEl.on("click", function() {
			if ($(this).hasClass("headbar-title-on")) {
				$(this).removeClass("headbar-title-on");
				$courseFilterEl.hide();
				$courseListEl.show();

			} else {
				var height = $(window).height() - 114;
				if ($courseFilterEl.height() < height) {
					$courseFilterEl.css("height", height);
				}

				$(this).addClass("headbar-title-on");
				$courseFilterEl.show();
				$courseListEl.hide();
			}
		});

		// 切换课程类型
		$(".js-sidebar-lang a").on("click", function() {
			$(this).addClass('curr').parent().siblings().find("a").removeClass('curr');
			$allCourseEl.html($(this).attr("data-title") + "<em></em>").removeClass('headbar-title-on');

			$courseListEl.html("").show();
			$courseFilterEl.hide();
			loadCourseList();
		});

		// 滚动加载更多课程
		$win.on("scroll", function(event) {
			var scrollDis = $win.scrollTop(),
				pageHeight = $(document).height(),
				viewHeight = $win.height();

			if (scrollDis >= pageHeight - viewHeight - 100) {
				if (!isLastPage) {
					loadCourseList(pageIndex);
				}
			}
		});
	};

	$(function() {

		addEventMonitor();
		loadCourseList();
	});
})(window, window.Zepto);