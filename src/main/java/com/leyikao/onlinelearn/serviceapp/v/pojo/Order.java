package com.leyikao.onlinelearn.serviceapp.v.pojo;

import java.util.Date;
/**
 * @author jack.lee
 * 订单 pojo
 */
public class Order {
	private Integer orderId;	//订单id
	private String orderCode;	//订单code
	private Integer userId; 	//用户id
	private Integer courseId;	//课程id
	private Date orderTime;		//d下单时间
	private char state;			//订单状态（0-未付款，1-付款成功，2-付款失败，3-取消订单）
	
	
	public Order() {

	}
	public Order(Integer orderId, String orderCode, Integer userId, Integer courseId, Date orderTime, char state) {

		this.orderId = orderId;
		this.orderCode = orderCode;
		this.userId = userId;
		this.courseId = courseId;
		this.orderTime = orderTime;
		this.state = state;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public char getState() {
		return state;
	}
	public void setState(char state) {
		this.state = state;
	}
	
	
}
