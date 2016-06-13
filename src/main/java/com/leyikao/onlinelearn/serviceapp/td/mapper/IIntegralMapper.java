package com.leyikao.onlinelearn.serviceapp.td.mapper;

import java.util.List;
import java.util.Map;

public interface IIntegralMapper {
	public List<Map<String, Object>> findIntegralRule(Map<String, String> map);

	public int updateUserCore(Map<String, Object> map);

	public int insertIntegralPool(Map<String, Object> map);

	public  List<Map<String, Object>> findIntegralPool(Map<String, String> map);

	public int updateIntegralPool(Map<String, Object> map);

	public Map<String, String> findReply(String replyid);

	public Map<String, Object> countPractice(String userId);

	public int updateUserCoreBythirdPartyLoginId(Map<String, Object> map);

}
