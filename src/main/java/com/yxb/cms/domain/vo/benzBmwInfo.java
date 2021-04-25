package com.yxb.cms.domain.vo;

import java.io.Serializable;

import com.yxb.cms.domain.dto.PageDto;


public class benzBmwInfo extends PageDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int uid;
	private int pool;
	private int per;
	private int perWin;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getPool() {
		return pool;
	}
	public void setPool(int pool) {
		this.pool = pool;
	}
	public int getPer() {
		return per;
	}
	public void setPer(int per) {
		this.per = per;
	}
	public int getPerWin() {
		return perWin;
	}
	public void setPerWin(int perWin) {
		this.perWin = perWin;
	}
	
	
	
	

}
