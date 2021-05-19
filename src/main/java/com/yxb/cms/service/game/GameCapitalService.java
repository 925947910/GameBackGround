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
import com.alibaba.fastjson.parser.Feature;
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
import com.yxb.cms.domain.bo.BussinessMsg;
import com.yxb.cms.domain.vo.billsInfo;
import com.yxb.cms.domain.vo.freezeInfo;
import com.yxb.cms.domain.vo.gameChannel;
import com.yxb.cms.domain.vo.gameRec;
import com.yxb.cms.domain.vo.gameUser;
import com.yxb.cms.domain.vo.orderInfo;
import com.yxb.cms.handler.RedisClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.StringUtil;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import javax.servlet.http.HttpServletRequest;


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
    @Autowired
    private GameUserMapper GameUserMapper;
    @Autowired 
    private  GameUserService GameUserService;
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
    public String selectOrderResultPageList(orderInfo orderInfo,HttpServletRequest request){
    	Float sum=OrderMapper.orderSumOfCost(orderInfo);
    	//request.setAttribute("sum",sum);
        List<orderInfo> orderInfoList = OrderMapper.selectOrderInfoListByPage(orderInfo);
        Long count = OrderMapper.selectCountOrder(orderInfo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("sum", sum);
        map.put("data", orderInfoList);
        return Json.toJson(map);
    }
    public String  selectChannelPageList(gameChannel channel,HttpServletRequest request){
    	List<gameChannel> gameChannels=new ArrayList<gameChannel>();
    	        List<String> idList=new ArrayList<String>();
    	        Set<String> channels=redisClient.keys(Constants.REDIS_DB1, "interfaceUri:*");
    	        channels.remove("interfaceUri:0");
    	        long   count=channels.size();
    	        int beginIndex=channel.getPage()*channel.getLimit()-channel.getLimit();
    	        int endIndex=channel.getPage()*channel.getLimit();  
    	      int index=1;  
    		for (Iterator iterator = channels.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				index+=1;
				if(index>=beginIndex&&index<=endIndex){
					idList.add(string);
				}
				if(index>endIndex){
					break;
				}
			 }	
       for (int i = 0; i < idList.size(); i++) {
    	   Map<String,String> channelMap=redisClient.hgetAll(Constants.REDIS_DB1,idList.get(i));
    	   if(channelMap!=null&&!channelMap.isEmpty()){
    		   String[] k=idList.get(i).split(":");
    		   gameChannel gameChannel= new gameChannel();
    		   gameChannel.setId(Integer.parseInt(k[1]));
    		   gameChannel.setName(channelMap.get("name"));
    		   gameChannel.setExtractPer(Integer.parseInt(channelMap.get("extractPer")));
    		   gameChannels.add(gameChannel);
    	   }else{
    		   count-=1; 
    	   } 
    	}
       
       Map<String,String> channelMap=redisClient.hgetAll(Constants.REDIS_DB1,"interfaceUri:0");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data", gameChannels);
        map.put("channelIn",channelMap.get("channelIn"));
        map.put("channelOut",channelMap.get("channelOut"));
        return Json.toJson(map);
    }
    public BussinessMsg   channelChoose(String channelName,String act)throws Exception{
    	redisClient.hset(Constants.REDIS_DB1,"interfaceUri:0", act, channelName);
        return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
        
    	
    }
    
    @SystemServiceLog(description="审核提现Service")
    public BussinessMsg   orderReview(int orderId,int succ)throws Exception{
    	List<orderInfo> orderInfos=OrderMapper.OrderInfoById(orderId);
    	if(orderInfos.size()!=1) {
    		throw new RuntimeException("订单不存在");
    	}
    	if(orderInfos.get(0).getStatus()!=2) {
    		throw new RuntimeException("订单已审核");
    	}
    	if(orderInfos.get(0).getOrderType()!=2) {
    		throw new RuntimeException("充值订单不予审核");
    	}
    	String	JsonAuth;
    	
    	   String url=redisClient.hget(Constants.REDIS_DB1,Constants.INTERFACE_URI+0, "verifyOrderUrl");
    	   Map<String,String> ReqParam=  new HashMap<String, String>();
    	   JSONObject param= new JSONObject();
    	   param.put("orderId", orderId);
    	   param.put("succ", succ);
    	   ReqParam.put("param", param.toString());
		try {
			HttpClientUtil  client=HttpClientUtil.getInstance();
			
			JsonAuth=client.doPostWithJsonResult(url, ReqParam);
		} catch (Exception e) {
			throw new RuntimeException("请求超时");
		}
		if(JsonAuth==null) {
			throw new RuntimeException("请求数据为空");
		}
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!JsonAuth:"+JsonAuth);
        return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
        
    	
    }
   
}
