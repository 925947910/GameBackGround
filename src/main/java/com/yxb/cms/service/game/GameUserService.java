/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2017 © yangxiaobing, 873559947@qq.com
 *
 * This file is part of contentManagerSystem.
 * contentManagerSystem is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * contentManagerSystem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with contentManagerSystem.  If not, see <http://www.gnu.org/licenses/>.
 *
 * 这个文件是contentManagerSystem的一部分。
 * 您可以单独使用或分发这个文件，但请不要移除这个头部声明信息.
 * contentManagerSystem是一个自由软件，您可以自由分发、修改其中的源代码或者重新发布它，
 * 新的任何修改后的重新发布版必须同样在遵守GPL3或更后续的版本协议下发布.
 * 关于GPL协议的细则请参考COPYING文件，
 * 您可以在contentManagerSystem的相关目录中获得GPL协议的副本，
 * 如果没有找到，请连接到 http://www.gnu.org/licenses/ 查看。
 *
 * - Author: yangxiaobing
 * - Contact: 873559947@qq.com
 * - License: GNU Lesser General Public License (GPL)
 * - source code availability: http://git.oschina.net/yangxiaobing_175/contentManagerSystem
 */
package com.yxb.cms.service.game;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yxb.cms.architect.annotation.SystemServiceLog;
import com.yxb.cms.architect.constant.BusinessConstants;
import com.yxb.cms.architect.constant.BussinessCode;
import com.yxb.cms.architect.constant.Constants;
import com.yxb.cms.architect.constant.RedisKeys;
import com.yxb.cms.architect.utils.BussinessMsgUtil;
import com.yxb.cms.architect.utils.HttpClientUtil;
import com.yxb.cms.dao.BillsMapper;
import com.yxb.cms.dao.GameUserMapper;
import com.yxb.cms.domain.bo.BussinessMsg;
import com.yxb.cms.domain.bo.ExcelExport;
import com.yxb.cms.domain.po.bills;
import com.yxb.cms.domain.vo.Role;
import com.yxb.cms.domain.vo.User;
import com.yxb.cms.domain.vo.UserRole;
import com.yxb.cms.domain.vo.gameUser;
import com.yxb.cms.handler.RedisClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 用户信息服务类
 * @author yangxiaobing
 * @date 2017/7/14
 */

@Service
public class GameUserService {
	public static final int   EVENT_COIN_GM_CHARGE=12; // 后台金币修改	
    private Logger log = LogManager.getLogger(GameUserService.class);
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private GameUserMapper GameUserMapper;
    @Autowired
    private BillsMapper BillsMapper;


    /**
     * 用户信息分页显示
     * @param user 用户实体
     * @return
     */
    public String selectUserResultPageList(gameUser user){
        List<gameUser> userList = GameUserMapper.selectUserListByPage(user);
       
        Long count = GameUserMapper.selectCountUser(user);
        long now=System.currentTimeMillis()/1000;
        Set<String> uids=redisClient.zrangeByScore(Constants.REDIS_DB5, "Onlines", now-60, now);
        Map <Integer,Long> maps=new HashMap<Integer, Long>();
     for (Iterator iterator = uids.iterator(); iterator.hasNext();) {
		String uidStr = (String) iterator.next();
		String timeStr= (String) iterator.next();
		maps.put(Integer.parseInt(uidStr),Long.parseLong(timeStr));
	   }
     for (int i = 0; i < userList.size(); i++) {
    	 gameUser  currUser = userList.get(i);	
    	Long t= maps.get(currUser.getId());
    	 if(t==null){
    		 t=1614528000L;
    	 }
    	 currUser.setOnline(t);
		}  
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data", userList);
        map.put("currOnlines", uids.size()/2);
        return Json.toJson(map);
    }

    

    @Transactional
    @SystemServiceLog(description="修改玩家密码Service")
    public BussinessMsg   updatePwd(Integer userId,String pwd)throws Exception{
    	if(GameUserMapper.updatePwd(userId, pwd)!=1) {
			throw new RuntimeException("修改玩家密码失败");
		}
        return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
    }
    @Transactional
    @SystemServiceLog(description="修改玩家密码Service")
    public BussinessMsg   userFreeze(Integer userId,int freeze)throws Exception{
    	if(GameUserMapper.userFreeze(userId, freeze)!=1) {
			throw new RuntimeException("解冻冻结 ");
		}
        return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
    }
    @Transactional
    @SystemServiceLog(description="添加玩家金币Service")
    public BussinessMsg   addCoin(Integer userId,Integer coin,Integer tagId,String desc)throws Exception{
    	List<gameUser> DBUsers=GameUserMapper.checkCoin(userId);
		gameUser DBUser=DBUsers.get(0);
		int version = DBUser.getVersion();
		int oldCoin = DBUser.getCoin();
		int newCoin = oldCoin+coin;
		if(DBUser.getIsTourist()!=1) {
			throw new RuntimeException("非内部玩家不可添加");
		}
		if(newCoin<0) {
			throw new RuntimeException("金币不能为负");
		}
		if(GameUserMapper.coinChange(userId, newCoin, version)!=1) {
			throw new RuntimeException("修改金币失败");
		}
		writeBill(userId,DBUser.getAgentId(),DBUser.getNick(),coin, newCoin, EVENT_COIN_GM_CHARGE, tagId,desc,"","");
        return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
    }
	public  void writeBill(int Uid,int agentId,String nick,int Cost ,int Remain, int Type, int TagId,String Reason,String accountOut,String accountIn)  throws Exception{	
		redisClient.hset(Constants.REDIS_DB0, "user:"+Uid,"coin",Remain+"");
		bills  bills= new  bills();
		bills.setUid(Uid);
		bills.setAgentId(agentId);
		bills.setNick(nick);
		bills.setRemain(Remain);
		bills.setCost(Cost);
		bills.setType(Type);
		bills.setTagId(TagId);
		bills.setAccountOut(accountOut);
		bills.setAccountIn(accountIn);
		bills.setReason(Reason);
		bills.setTime(new Date().getTime()/1000);
	    BillsMapper.writeBills(bills);
	}

	public boolean saveGameUser(String acc,String pwd, String nick, String phone) {
		try {
			HttpClientUtil client=HttpClientUtil.getInstance();
			Map<String,Object> dataMap=new  HashMap<String, Object>(); 
			Map<String,String>  param=new  HashMap<String, String>(); 
			
			dataMap.put("token", "");
			dataMap.put("acc", acc);
			dataMap.put("pwd", pwd);
			dataMap.put("nick", nick);
			dataMap.put("phone", phone);
			String paramStr=JSON.toJSONString(dataMap);
			param=new  HashMap<String, String>();  
			param.put("param",paramStr);
			String uri=redisClient.hget(Constants.REDIS_DB4,RedisKeys._GAME_INTERFACE_URI,RedisKeys._GAME_REGIST);
			String JsonAuth=client.doPostWithJsonResult(uri, param);
			JSONObject AuthData=JSON.parseObject(JsonAuth);
			System.out.println(JsonAuth);
			int resultCode=AuthData.getIntValue("status");
			return true;
		} catch (Exception e) {
			log.error("",e);
			return false;
		}	}


}
