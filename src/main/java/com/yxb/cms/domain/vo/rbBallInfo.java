package com.yxb.cms.domain.vo;

import java.io.Serializable;

import com.yxb.cms.domain.dto.PageDto;


public class rbBallInfo extends PageDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Long issue;
	private String lotteryResult;
	private Integer lotteryPool;
	private Integer lotteryPrice;
	private Integer totalWin;
	private Integer isDraw;
	private Long time;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getIssue() {
		return issue;
	}
	public void setIssue(Long issue) {
		this.issue = issue;
	}
	public String getLotteryResult() {
		return lotteryResult;
	}
	public void setLotteryResult(String lotteryResult) {
		this.lotteryResult = lotteryResult;
	}
	public Integer getLotteryPool() {
		return lotteryPool;
	}
	public void setLotteryPool(Integer lotteryPool) {
		this.lotteryPool = lotteryPool;
	}
	public Integer getLotteryPrice() {
		return lotteryPrice;
	}
	public void setLotteryPrice(Integer lotteryPrice) {
		this.lotteryPrice = lotteryPrice;
	}
	public Integer getTotalWin() {
		return totalWin;
	}
	public void setTotalWin(Integer totalWin) {
		this.totalWin = totalWin;
	}
	public Integer getIsDraw() {
		return isDraw;
	}
	public void setIsDraw(Integer isDraw) {
		this.isDraw = isDraw;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	
	
	

}
