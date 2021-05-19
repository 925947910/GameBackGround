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



import com.alibaba.druid.util.StringUtils;
import com.yxb.cms.architect.annotation.SystemServiceLog;
import com.yxb.cms.architect.constant.BussinessCode;
import com.yxb.cms.architect.constant.Constants;
import com.yxb.cms.architect.utils.BussinessMsgUtil;
import com.yxb.cms.architect.utils.CommonHelper;
import com.yxb.cms.dao.RbBallMapper;
import com.yxb.cms.domain.bo.BussinessMsg;
import com.yxb.cms.domain.bo.ExcelExport;
import com.yxb.cms.domain.vo.User;
import com.yxb.cms.domain.vo.benzBmwInfo;
import com.yxb.cms.domain.vo.rbBallInfo;
import com.yxb.cms.handler.RedisClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.InputStream;
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
    	List<benzBmwInfo> benzBmwInfoList=new ArrayList<benzBmwInfo>();
    	long count=1;
    	String id=benzBmwInfo.getSearchContent();
    	if(!StringUtils.isEmpty(id)){
    		Map<String,String> data=redisClient.hgetAll(Constants.REDIS_DB3, "BenzBmw:"+id);
    		if(!data.isEmpty()){
				benzBmwInfo newBenzBmwInfo= new benzBmwInfo();
				newBenzBmwInfo.setUid(Integer.parseInt(id));
				newBenzBmwInfo.setPool(Integer.parseInt(data.get("pool")));
				newBenzBmwInfo.setPer(Integer.parseInt(data.get("per")));
				newBenzBmwInfo.setPerWin(Integer.parseInt(data.get("perWin")));
				benzBmwInfoList.add(newBenzBmwInfo);
			}
    	}else{
    		
    		  count=redisClient.zcount(Constants.REDIS_DB3, "BenzBmwSort", "0","10000000000");
    		    int start=(benzBmwInfo.getPage()-1)*benzBmwInfo.getLimit();
    		    int end=benzBmwInfo.getPage()*benzBmwInfo.getLimit()-1;
    	    	Set<String> keys=redisClient.zrevrange(Constants.REDIS_DB3, "BenzBmwSort",start,end);
    	    	
    	    	for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
    				String key = iterator.next();
    				String[] k=key.split(":");
    				try {
    					Map<String,String> data=redisClient.hgetAll(Constants.REDIS_DB3, key);
    					if(data.isEmpty()){
    						redisClient.zrem(Constants.REDIS_DB3,"BenzBmwSort", key);
    						continue;
    					}
    					benzBmwInfo newBenzBmwInfo= new benzBmwInfo();
    					newBenzBmwInfo.setUid(Integer.parseInt(k[1]));
    					newBenzBmwInfo.setPool(Integer.parseInt(data.get("pool")));
    					newBenzBmwInfo.setPer(Integer.parseInt(data.get("per")));
    					newBenzBmwInfo.setPerWin(Integer.parseInt(data.get("perWin")));
    					benzBmwInfoList.add(newBenzBmwInfo);
    				} catch (Exception e) {
    				}	
    			}
    		
    		
    		
    	}
    
      
       Map<String, Object> map = new HashMap<String, Object>();
       map.put("code",0);
       map.put("msg","");
       map.put("count",count);
       map.put("data", benzBmwInfoList);
       return Json.toJson(map);
   }
    

    public BussinessMsg edit_benzBmw(benzBmwInfo benzBmwInfo){
    	Map<String,String> hash=new HashMap<String, String>();
//    	hash.put("pool", benzBmwInfo.getPool()+"");
    	hash.put("per", benzBmwInfo.getPer()+"");
    	hash.put("perWin", benzBmwInfo.getPerWin()+"");
        redisClient.hmset(Constants.REDIS_DB3, "BenzBmw:"+benzBmwInfo.getUid(), hash);
    	
        return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
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
        map.put("period", rbBallInfo.getPeriod());
        return Json.toJson(map);
    }
    
    /**
     * 用户列表EXCEL导出
     * @param user 用户实体
     * @return
     */
    public ExcelExport excelRbBallInfoList(rbBallInfo rbBallInfo){
    	rbBallInfo.setPage(1);
    	rbBallInfo.setLimit(1000);
        ExcelExport excelExport = new ExcelExport();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        
        long zeroSec=calendar.getTimeInMillis()/1000;
        rbBallInfo.setBegin(zeroSec-1);
        rbBallInfo.setEnd(zeroSec+1+24*3600);
        List<rbBallInfo> rbBallInfoList = RbBallMapper.selectRbBallListByPage(rbBallInfo);
        
        excelExport.addColumnInfo("id","id");
        excelExport.addColumnInfo("期No.","issue");
        excelExport.addColumnInfo("累计投注","lotteryPool");
        excelExport.addColumnInfo("开奖结果","lotteryResult");
        excelExport.addColumnInfo("是否开奖","isDraw");
        excelExport.addColumnInfo("累计中奖","totalWin");
        excelExport.setDataList(rbBallInfoList);
        return excelExport;
    }
 

    @SystemServiceLog(description="addRbBallService")
    public BussinessMsg    addRbBallExcel(HttpServletRequest request,int period) throws Exception{

    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

           MultipartFile file = multipartRequest.getFile("file");
           if (file.isEmpty()) {
               return BussinessMsgUtil.returnCodeMessage(BussinessCode.ADD_RBBALL_EXCEL_FAILED);
           }
           InputStream inputStream = file.getInputStream();
           List<List<Object>> list = CommonHelper.getBankListByExcel(inputStream, file.getOriginalFilename());
           inputStream.close();

           for (int i = 0; i < list.size(); i++) {
               List<Object> lo = list.get(i);
              try {
            	 String[] p1 =lo.get(0).toString().split("\\.");
            	 String[] p2 =lo.get(1).toString().split("\\.");
            	  long issueNum= new Long(p1[0]);
            	  String result= p2[0];
            	  addRbBall(issueNum,period, result);
			} catch (Exception e) {
				// TODO: handle exception
			}

           }
     
        return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
    }
    @Transactional
    public void addRbBall(long issueNum,int period,String result) throws Exception{
    		Calendar calendar = Calendar.getInstance();
    		long day = calendar.get(Calendar.DATE);
    		long month = calendar.get(Calendar.MONTH)+1;
    		long year = calendar.get(Calendar.YEAR);

    		calendar.set(Calendar.HOUR_OF_DAY, 0);
    		calendar.set(Calendar.MINUTE, 0);
    		calendar.set(Calendar.SECOND, 0);

    		long time = System.currentTimeMillis()/1000;
    		long issue= year*10000000+month*100000+day*1000+issueNum;
    		rbBallInfo rbBallInfo=new rbBallInfo();
    		rbBallInfo.setIssue(issue);
    		rbBallInfo.setPeriod(period);
    		rbBallInfo.setLotteryPool(0);
    		rbBallInfo.setLotteryPrice(0);
    		rbBallInfo.setLotteryResult(result);
    		rbBallInfo.setTotalWin(0);
    		rbBallInfo.setIsDraw(0);
    		rbBallInfo.setTime(time);
    		List<rbBallInfo> l=RbBallMapper.getRbBallByIssue(issue,period);
    		if(l!=null&&l.size()==1){
    			RbBallMapper.setRbBallResult(rbBallInfo);
    		}else{
    			RbBallMapper.addRbBall(rbBallInfo);
    		}
    }
    
    @SystemServiceLog(description="addRbBallService")
    public BussinessMsg addRbBall(rbBallInfo rbBallInfo) throws Exception{
    	addRbBall(rbBallInfo.getIssue(),rbBallInfo.getPeriod(), rbBallInfo.getLotteryResult());
        return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
    }
    

    
    
   
}
