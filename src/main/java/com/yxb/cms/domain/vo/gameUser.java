package com.yxb.cms.domain.vo;

import java.io.Serializable;

import com.yxb.cms.domain.dto.PageDto;

public class gameUser extends PageDto implements Serializable {
    private static final long serialVersionUID = 1L;
	private Integer id;
	private String acc;
	private String pwd;
	private String extractPwd;
	private String nick;
	private String phone;
	private String email;
	private Integer coin;
	private Integer mineral;
	private Integer  version;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAcc() {
		return acc;
	}
	public void setAcc(String acc) {
		this.acc = acc;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getExtractPwd() {
		return extractPwd;
	}
	public void setExtractPwd(String extractPwd) {
		this.extractPwd = extractPwd;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getCoin() {
		return coin;
	}
	public void setCoin(Integer coin) {
		this.coin = coin;
	}
	public Integer getMineral() {
		return mineral;
	}
	public void setMineral(Integer mineral) {
		this.mineral = mineral;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}

}
