package com.leyikao.onlinelearn.serviceapp.v.pojo;

import java.util.Date;
/**
 * @author jack.lee
 * 视频
 */
public class Video {
	
	private Integer videoId;		//视频id	
	private String videoName;		//视频名称	
	private Integer videoSizeKb;	//视频大小（单位：kb）	
	private Integer videoSizeMb;	//视频大小（单位：mb）	
	private char videoType;			//视频类型（flv，其他）	
	private char uploadState;		//上传状态（0-上传失败，1-上传成功，2-上传中）
	private Date uploadTime;		//上传时间	
	private String videoheadUrl;	//视频头地址	
	private String videoUrl;		//完整视频地址	
	private Integer  timelong;		//视频时长	
	private String  ccVideoId;  	// cc视频id  
	private String imgUrl;    		//视频图片地址  
	private String playJsCode;  	//视频 js 播放代码 

	public Video() {

	}
	

	public Video(Integer videoId, String videoName, Integer videoSizeKb, Integer videoSizeMb, char videoType, char uploadState, Date uploadTime, String videoheadUrl, String videoUrl, Integer timelong, String ccVideoId, String imgUrl, String playJsCode) {

		this.videoId = videoId;
		this.videoName = videoName;
		this.videoSizeKb = videoSizeKb;
		this.videoSizeMb = videoSizeMb;
		this.videoType = videoType;
		this.uploadState = uploadState;
		this.uploadTime = uploadTime;
		this.videoheadUrl = videoheadUrl;
		this.videoUrl = videoUrl;
		this.timelong = timelong;
		this.ccVideoId = ccVideoId;
		this.imgUrl = imgUrl;
		this.playJsCode = playJsCode;
	}


	public String getCcVideoId() {
		return ccVideoId;
	}
	public void setCcVideoId(String ccVideoId) {
		this.ccVideoId = ccVideoId;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getPlayJsCode() {
		return playJsCode;
	}
	public void setPlayJsCode(String playJsCode) {
		this.playJsCode = playJsCode;
	}
	public Integer getVideoId() {
		return videoId;
	}
	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	public Integer getVideoSizeKb() {
		return videoSizeKb;
	}
	public void setVideoSizeKb(Integer videoSizeKb) {
		this.videoSizeKb = videoSizeKb;
	}
	public Integer getVideoSizeMb() {
		return videoSizeMb;
	}
	public void setVideoSizeMb(Integer videoSizeMb) {
		this.videoSizeMb = videoSizeMb;
	}
	public char getVideoType() {
		return videoType;
	}
	public void setVideoType(char videoType) {
		this.videoType = videoType;
	}
	public char getUploadState() {
		return uploadState;
	}
	public void setUploadState(char uploadState) {
		this.uploadState = uploadState;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getVideoheadUrl() {
		return videoheadUrl;
	}
	public void setVideoheadUrl(String videoheadUrl) {
		this.videoheadUrl = videoheadUrl;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public Integer getTimelong() {
		return timelong;
	}
	public void setTimelong(Integer timelong) {
		this.timelong = timelong;
	}

	

}
