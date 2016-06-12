package com.leyikao.onlinelearn.serviceapp.v.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leyikao.onlinelearn.serviceapp.util.VideoUtils;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Chapter;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Course;
import com.leyikao.onlinelearn.serviceapp.v.pojo.CourseChapterList;
import com.leyikao.onlinelearn.serviceapp.v.pojo.Video;
import com.leyikao.onlinelearn.serviceapp.v.service.LearnService;
import com.leyikao.onlinelearn.serviceapp.v.service.VideoService;

@Controller
@RequestMapping("/video")
public class VideoController {
	private static String CC_USERID="AE0E3FDF9C394E37";
	private static String CC_KEY="7tGjqeIESHwoKSIP71Q0rumo65wpzE4N";
	@Resource
	private VideoService videoService;
	@Resource
	private LearnService learnService;
	
	@RequestMapping("play")
	public String videoPlay(HttpServletRequest request, Model model){
		//String userId= request.getSession().getAttribute("userId").toString();
		String userId= request.getParameter("userId");
		String chapterId=request.getParameter("chapterId");
		String courseId = request.getParameter("courseId");
		List<Chapter> list= learnService.getChaptersByCourseId(courseId);
		List<CourseChapterList> cclist= new ArrayList<CourseChapterList>();//章节列表
		Course course = learnService.getCourseById(courseId,userId);

		for(int i=0;i<list.size();i++){
			CourseChapterList courseChapterList = new CourseChapterList();
			courseChapterList.setChapter(list.get(i));
			List<Chapter> sonlist= learnService.getChapterByParentId(list.get(i).getChapterId(),userId);
			courseChapterList.setList(sonlist);
			cclist.add(courseChapterList);
		}

		Video video = videoService.getVideoBychapterId(chapterId);
		String ccVideoId=video.getCcVideoId();
		//mp4视频，图片播放地址 
		String mp4Url = VideoUtils.getMp4XmlUrl(CC_USERID, video.getCcVideoId(), CC_KEY);
		String imgUrl = VideoUtils.getImgXmlUrl(CC_USERID, video.getCcVideoId(), CC_KEY); 
		model.addAttribute("chapterId", chapterId);
		model.addAttribute("video", video);
		model.addAttribute("cclist", cclist);
		model.addAttribute("course", course);
		model.addAttribute("ccVideoId", ccVideoId);
		model.addAttribute("imgUrl", imgUrl);
		model.addAttribute("mp4Url", mp4Url);
		model.addAttribute("userId", userId);
		return "video";
	}
	
	@ResponseBody
	@RequestMapping("saveSchem")
	public Map<String,Object> saveSchem(HttpServletRequest request){
		//String userId= request.getSession().getAttribute("userId").toString();
		String userId = request.getParameter("userId");
		String flag=request.getParameter("flag").toString();
		String courseId =request.getParameter("courseId").toString();
		String chapterId = request.getParameter("chapterId").toString();
		String videoId =request.getParameter("videoId").toString();
		videoService.saveSchem(userId,chapterId,videoId,flag,courseId);
		return null;
	}
	
}
