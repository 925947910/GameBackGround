package com.yxb.cms.domain.vo;

import java.io.Serializable;

import com.yxb.cms.domain.dto.PageDto;

public class orderInfo  extends PageDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String accountOut;
	private String accountIn;
	private String orderLocal;
	private String orderRemote;
	private Integer plat;
	private Integer uid;
	private Integer agentId;
	private float cost;
	private String  currency;
	private Integer coin;
	private Integer  orderType;
	private Long  time;
	private Integer  status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccountOut() {
		return accountOut;
	}
	public void setAccountOut(String accountOut) {
		this.accountOut = accountOut;
	}
	public String getAccountIn() {
		return accountIn;
	}
	public void setAccountIn(String accountIn) {
		this.accountIn = accountIn;
	}
	public String getOrderLocal() {
		return orderLocal;
	}
	public void setOrderLocal(String orderLocal) {
		this.orderLocal = orderLocal;
	}
	public String getOrderRemote() {
		return orderRemote;
	}
	public void setOrderRemote(String orderRemote) {
		this.orderRemote = orderRemote;
	}
	public Integer getPlat() {
		return plat;
	}
	public void setPlat(Integer plat) {
		this.plat = plat;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getAgentId() {
		return agentId;
	}
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Integer getCoin() {
		return coin;
	}
	public void setCoin(Integer coin) {
		this.coin = coin;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
