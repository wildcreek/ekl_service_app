package com.leyikao.onlinelearn.serviceapp.td.dao;


public interface IFeedbackDao {
	public int add(String userId, String score, String feedbackContent);
}
