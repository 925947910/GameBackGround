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
import com.yxb.cms.architect.constant.BussinessCode;
import com.yxb.cms.architect.constant.Constants;
import com.yxb.cms.architect.constant.RedisKeys;
import com.yxb.cms.architect.utils.BussinessMsgUtil;
import com.yxb.cms.architect.utils.HttpClientUtil;
import com.yxb.cms.dao.BillsMapper;
import com.yxb.cms.dao.FreezeMapper;
import com.yxb.cms.dao.GameRecMapper;
import com.yxb.cms.dao.GameUserMapper;
import com.yxb.cms.dao.OrderMapper;
import com.yxb.cms.dao.RbBallMapper;
import com.yxb.cms.domain.bo.BussinessMsg;
import com.yxb.cms.domain.vo.User;
import com.yxb.cms.domain.vo.benzBmwInfo;
import com.yxb.cms.domain.vo.billsInfo;
import com.yxb.cms.domain.vo.freezeInfo;
import com.yxb.cms.domain.vo.gameRec;
import com.yxb.cms.domain.vo.gameUser;
import com.yxb.cms.domain.vo.orderInfo;
import com.yxb.cms.domain.vo.rbBallInfo;
import com.yxb.cms.handler.RedisClient;

import org.apache.http.HttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yxb.cms.architect.constant.Constants;
import java.util.*;

import javax.servlet.http.HttpServletRequest;


/**
 * 用户信息服务类
 * @author yangxiaobing
 * @date 2017/7/14
 */

@Service
public class GameActiveService {

    private Logger log = LogManager.getLogger(GameActiveService.class);
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private RbBallMapper RbBallMapper;
  

    public String benzBmwInfo(benzBmwInfo benzBmwInfo){
    	if(benzBmwInfo.getSearchTerm()!=null){
    		redisClient.hset(Constants.REDIS_DB3, "BenzBmw", "result",benzBmwInfo.getSearchTerm());
    	}
    	
    	Map<String, String> info=redisClient.hgetAll(Constants.REDIS_DB3, "BenzBmw");
    	List<benzBmwInfo> benzBmwInfoList=new ArrayList<benzBmwInfo>();
    	benzBmwInfo newBenzBmwInfo= new benzBmwInfo();
    	newBenzBmwInfo.setIssue(Long.parseLong(info.get("issue")));
    	newBenzBmwInfo.setResult(info.get("result"));
    	benzBmwInfoList.add(newBenzBmwInfo);
    	
       Map<String, Object> map = new HashMap<String, Object>();
       map.put("code",0);
       map.put("msg","");
       map.put("data", benzBmwInfoList);
       return Json.toJson(map);
   }
    public String selectRbBallResultPageList(rbBallInfo rbBallInfo){
    	
    	 Calendar calendar = Calendar.getInstance();
         calendar.set(Calendar.HOUR_OF_DAY, 0);
         calendar.set(Calendar.MINUTE, 0);
         calendar.set(Calendar.SECOND, 0);
         
         long zeroSec=calendar.getTimeInMillis()/1000;
         rbBallInfo.setBegin(zeroSec-1);
         rbBallInfo.setEnd(zeroSec+1+24*3600);
        List<rbBallInfo> rbBallInfoList = RbBallMapper.selectRbBallListByPage(rbBallInfo);
        Long count = RbBallMapper.selectCountRbBall(rbBallInfo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data", rbBallInfoList);
        return Json.toJson(map);
    }
    @Transactional
    @SystemServiceLog(description="addRbBallService")
    public BussinessMsg addRbBall(rbBallInfo rbBallInfo) throws Exception{
    	 Calendar calendar = Calendar.getInstance();
         long day = calendar.get(Calendar.DATE);
         long month = calendar.get(Calendar.MONTH)+1;
         long year = calendar.get(Calendar.YEAR);
         
         calendar.set(Calendar.HOUR_OF_DAY, 0);
         calendar.set(Calendar.MINUTE, 0);
         calendar.set(Calendar.SECOND, 0);
         
         long time = System.currentTimeMillis()/1000;
        long issue= year*10000000+month*100000+day*1000+rbBallInfo.getIssue();
        rbBallInfo.setIssue(issue);
        rbBallInfo.setLotteryPool(0);
        rbBallInfo.setLotteryPrice(0);
        rbBallInfo.setTime(time);
        try {
        	RbBallMapper.addRbBall(rbBallInfo);
        } catch (Exception e) {
            log.error("",e);
            throw e;
        }
        return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
    }
   
}
