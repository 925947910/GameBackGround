package com.yxb.cms.domain.vo;

import java.io.Serializable;

import com.yxb.cms.domain.dto.PageDto;


public class billsInfo extends PageDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer uid;
	private Integer agentId;
	private String nick;
	private Integer remain;
	private Integer cost;
	private Integer type;
	private Integer tagId;
	private String reason;
	private Long time;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}

	public Integer getRemain() {
		return remain;
	}
	public void setRemain(Integer remain) {
		this.remain = remain;
	}
	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getTagId() {
		return tagId;
	}
	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	
	
	

}
