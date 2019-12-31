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
import com.yxb.cms.architect.constant.RedisKeys;
import com.yxb.cms.architect.utils.HttpClientUtil;
import com.yxb.cms.dao.BillsMapper;
import com.yxb.cms.dao.FreezeMapper;
import com.yxb.cms.dao.GameRecMapper;
import com.yxb.cms.dao.OrderMapper;
import com.yxb.cms.domain.vo.billsInfo;
import com.yxb.cms.domain.vo.freezeInfo;
import com.yxb.cms.domain.vo.gameRec;
import com.yxb.cms.domain.vo.orderInfo;
import com.yxb.cms.handler.RedisClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 用户信息服务类
 * @author yangxiaobing
 * @date 2017/7/14
 */

@Service
public class GameCapitalService {

    private Logger log = LogManager.getLogger(GameCapitalService.class);
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private BillsMapper BillsMapper;
    @Autowired
    private FreezeMapper FreezeMapper;
    @Autowired
    private OrderMapper  OrderMapper;
    @Autowired
    private GameRecMapper GameRecMapper;

    /**
     * 用户信息分页显示
     * @param user 用户实体
     * @return
     */
    public String selectGameRecResultPageList(gameRec gameRec){
        List<gameRec> gameRecList = GameRecMapper.selectGameRecListByPage(gameRec);
        Long count = GameRecMapper.selectCountGameRec(gameRec);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data", gameRecList);
        return Json.toJson(map);
    }
    public String selectBillsResultPageList(billsInfo billsInfo){
        List<billsInfo> billsInfoList = BillsMapper.selectBillsInfoListByPage(billsInfo);
        Long count = BillsMapper.selectCountBills(billsInfo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data", billsInfoList);
        return Json.toJson(map);
    }

    public String selectFreezeResultPageList(freezeInfo freezeInfo){
        List<freezeInfo> freezeInfoList = FreezeMapper.selectFreezeInfoListByPage(freezeInfo);
        Long count = FreezeMapper.selectCountFreeze(freezeInfo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data", freezeInfoList);
        return Json.toJson(map);
    }
    public String selectOrderResultPageList(orderInfo orderInfo){
        List<orderInfo> orderInfoList = OrderMapper.selectOrderInfoListByPage(orderInfo);
        Long count = OrderMapper.selectCountOrder(orderInfo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data", orderInfoList);
        return Json.toJson(map);
    }

	public boolean unFreeze(int id) {
		try {
			HttpClientUtil client=HttpClientUtil.getInstance();
			Map<String,Object> dataMap=new  HashMap<String, Object>(); 
			Map<String,String>  param=new  HashMap<String, String>(); 
			dataMap.put("freezeId", id+"");
			String paramStr=JSON.toJSONString(dataMap);
			param=new  HashMap<String, String>();  
			param.put("param",paramStr);
			String uri=redisClient.hget(4,RedisKeys._GAME_INTERFACE_URI,RedisKeys._GAME_UNFREEZE);
			String JsonAuth=client.doPostWithJsonResult(uri, param);
			JSONObject AuthData=JSON.parseObject(JsonAuth);
			int resultCode=AuthData.getIntValue("status");
			return true;
		} catch (Exception e) {
			log.error("",e);
			return false;
		}
		
	}
}
