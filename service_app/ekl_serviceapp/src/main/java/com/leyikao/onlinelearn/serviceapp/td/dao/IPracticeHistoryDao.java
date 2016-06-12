package com.leyikao.onlinelearn.serviceapp.td.dao;

public interface IPracticeHistoryDao {
	public int add(String practiceHistoryId, String userId, String testPaperId,
				   String answerType, String beginTime, String endTime,
				   String isFinishAnswer);

	public String getUUID();
}
