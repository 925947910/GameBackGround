package com.yxb.cms.domain.vo;

import java.io.Serializable;

import com.yxb.cms.domain.dto.PageDto;


public class benzBmwInfo extends PageDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long issue;
	private String result;
	public Long getIssue() {
		return issue;
	}
	public void setIssue(Long issue) {
		this.issue = issue;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
	
	
	

}
