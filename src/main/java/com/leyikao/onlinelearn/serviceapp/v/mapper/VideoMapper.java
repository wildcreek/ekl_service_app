package com.leyikao.onlinelearn.serviceapp.v.mapper;

import org.apache.ibatis.annotations.Param;

import com.leyikao.onlinelearn.serviceapp.v.pojo.Video;

public interface VideoMapper {

	Video getVideoBychapterId(@Param("chapterId") String chapterId);

}
