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



import com.yxb.cms.architect.annotation.SystemServiceLog;
import com.yxb.cms.architect.constant.BusinessConstants;
import com.yxb.cms.architect.constant.BussinessCode;
import com.yxb.cms.architect.utils.BussinessMsgUtil;
import com.yxb.cms.architect.utils.QRCodeUtil;
import com.yxb.cms.architect.utils.TokenMaker;
import com.yxb.cms.dao.MineralBillsMapper;
import com.yxb.cms.dao.MineralCodeMapper;
import com.yxb.cms.domain.bo.BussinessMsg;
import com.yxb.cms.domain.po.bills;
import com.yxb.cms.domain.po.mineralBills;
import com.yxb.cms.domain.po.userMineral;
import com.yxb.cms.domain.vo.UserRole;
import com.yxb.cms.domain.vo.billsInfo;
import com.yxb.cms.domain.vo.gameUser;
import com.yxb.cms.domain.vo.mineralBillsInfo;
import com.yxb.cms.domain.vo.mineralCode;
import com.yxb.cms.handler.RedisClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 用户信息服务类
 * @author yangxiaobing
 * @date 2017/7/14
 */

@Service
public class GameMineralService {
	public static final int			CODE_WANLIBALL       = 0;
	public static final int			STATUS_UNUSED       = 0;
	public static final int			STATUS_USED       = 1;
	public static final int			STATUS_INVALID       = 2;
	public static final int   EVENT_MINERAL_GM_CHARGE=13; // 后台充值矿石返点
	private Logger log = LogManager.getLogger(GameMineralService.class);
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private MineralCodeMapper MineralCodeMapper;
	@Autowired
	private MineralBillsMapper MineralBillsMapper;
	@Value("${mineral.code.IconPath}")
	private String IconPath;
	@Value("${mineral.code.MineralCodePath}")
	private String MineralCodePath;
	/**
	 * 用户信息分页显示
	 * @param user 用户实体
	 * @return
	 */
	public String selectMineralCodeResultPageList(mineralCode mineralCode){
		List<mineralCode> mineralCodeList = MineralCodeMapper.selectMineralCodeListByPage(mineralCode);
		Long count = MineralCodeMapper.selectCountMineralCode(mineralCode);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code",0);
		map.put("msg","");
		map.put("count",count);
		map.put("data", mineralCodeList);
		return Json.toJson(map);
	}


	@Transactional
	public BussinessMsg genMineralCodes(String path,int mineral,int num,String mineralCodeDesc) throws Exception{
		log.info("生成矿卡开始");
		long start = System.currentTimeMillis();
		Integer count=0;
		try {
			for (int i = 0; i < num; i++) {
				String CodeNo=TokenMaker.getInstance().makeToken();
				mineralCode code= new mineralCode();
				code.setMineral(mineral*100);
				code.setType(CODE_WANLIBALL);
				code.setCodeDesc(mineralCodeDesc);
				code.setCodeNo(CodeNo);
				code.setStatus(STATUS_UNUSED);
				if(MineralCodeMapper.insertMineralCode(code)==1) {

					Date date = new Date();  // 获取当前系统时间
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");  // 设置日期格式
					String picture = simpleDateFormat.format(date)+"_"+i;
					// 存放在二维码中的内容
					String text = CodeNo;
					// 嵌入二维码的图片路径
					String imgPath = IconPath;
					// 生成的二维码的路径及名称
					String destPath = MineralCodePath+picture+".jpg";
					//生成二维码
					QRCodeUtil.encode(text, imgPath, destPath, true);
					// 解析二维码
					//					String str = QRCodeUtil.decode(destPath);
					// 打印出解析出的内容
					//					System.out.println(str);

				}else {
					count+=1;
				}	
			}
		} catch (Exception e) {
			log.error("保存用户信息方法内部错误",e);
			throw e;
		}finally {
			log.info("保存用户信息结束,用时" + (System.currentTimeMillis() - start) + "毫秒");
		}
		return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS,count);
	}


	public String selectMineralBillsResultPageList(mineralBillsInfo mineralBillsInfo) {
		List<mineralBillsInfo> mineralBillsInfoList = MineralBillsMapper.selectMineralBillsInfoListByPage(mineralBillsInfo);
		Long count = MineralBillsMapper.selectCountMineralBills(mineralBillsInfo);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code",0);
		map.put("msg","");
		map.put("count",count);
		map.put("data", mineralBillsInfoList);
		return Json.toJson(map);
	}
	public BussinessMsg failedMineralCode(Integer mineralCodeId) throws Exception{
		try {
			if(MineralCodeMapper.failedMineralCode(mineralCodeId)!=1) {
				return BussinessMsgUtil.returnCodeMessage(BussinessCode.FAILED_MINERALCODE_FAILED);
			};
		} catch (Exception e) {
			log.error("失效用户方法内部错误",e);
			throw e;
		}
		return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
	}



	@Transactional
	@SystemServiceLog(description="添加玩矿石Service")
	public BussinessMsg   addMineral(Integer userId,Integer mineral,Integer tagId,String desc)throws Exception{
		List<userMineral> luserMineral=MineralCodeMapper.getUserMineral(userId);
		int newMineral= mineral;
		if(luserMineral.size()==0) {
			//新插入矿
			if(mineral<0) {
				throw new RuntimeException("矿石初始不能为负");	
			}
			if(MineralCodeMapper.insertMineral(userId,newMineral)!=1) {
				throw new RuntimeException("新增矿石失败");
			}
		}else {
			//更新玩家矿
			newMineral=mineral+luserMineral.get(0).getMineral();
			if(newMineral<0) {
				throw new RuntimeException("矿石不足");	
			}
			if(MineralCodeMapper.updateMineralNum(userId, newMineral, luserMineral.get(0).getVersion())!=1) {
				throw new RuntimeException("修改矿石失败");
			}
		}
		writeMineralBill(userId, mineral, EVENT_MINERAL_GM_CHARGE, tagId, desc, newMineral, 0);
		return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);

	}

	public  void writeMineralBill(int Uid,int Cost , int Type, int tagId,String Reason,int mineral,int freeze) throws Exception {	
		String nick=redisClient.hget(0, "user:"+Uid,"nick");
		mineralBills  mineralBills= new  mineralBills();
		mineralBills.setUid(Uid);
		mineralBills.setNick(nick);
		mineralBills.setMineral(mineral);
		mineralBills.setCost(Cost);
		mineralBills.setType(Type);
		mineralBills.setTagId(tagId);
		mineralBills.setFreeze(freeze);
		mineralBills.setReason(Reason);
		mineralBills.setTime(new Date().getTime()/1000);
		MineralCodeMapper.writeMineralBills(mineralBills);
	}

}
