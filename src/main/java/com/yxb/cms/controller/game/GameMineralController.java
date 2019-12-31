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
package com.yxb.cms.controller.game;


import com.yxb.cms.architect.annotation.SystemControllerLog;
import com.yxb.cms.architect.constant.BussinessCode;
import com.yxb.cms.architect.utils.BussinessMsgUtil;
import com.yxb.cms.controller.BasicController;
import com.yxb.cms.domain.bo.BussinessMsg;
import com.yxb.cms.domain.vo.User;
import com.yxb.cms.domain.vo.billsInfo;
import com.yxb.cms.domain.vo.gameUser;
import com.yxb.cms.domain.vo.mineralBillsInfo;
import com.yxb.cms.domain.vo.mineralCode;
import com.yxb.cms.service.game.GameMineralService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
@RequestMapping("gameMineral")
public class GameMineralController extends BasicController {


    @Autowired
    private GameMineralService GameMineralService;

    /**
     *跳转到用户列表页面
     * @return
     */
    @RequestMapping("/mineralCode_list.do")
    public String toMineralCodeListPage() {
        return "game/mineralCode_list";
    }
    /**
     * 加载用户列表List
     * @param user
     * @return
     */
    @RequestMapping("/ajax_mineralCode_list.do")
    @ResponseBody
    public String ajaxMineralCodeList(mineralCode mineralCode){
        return GameMineralService.selectMineralCodeResultPageList(mineralCode);
    }

    @RequestMapping("/mineralCode_add.do")
    public String toMineralCodeAddPage(Model model) {
        //新增页面标识
        model.addAttribute("pageFlag", "addPage");
        return "game/mineralCode_edit";
    }
  
    @RequestMapping("/ajax_save_mineralCode.do")
    @ResponseBody
    @SystemControllerLog(description="保存矿卡")
    public BussinessMsg ajaxSaveMineralCode(HttpServletRequest request,int mineral ,int num ,String mineralCodeDesc ){
    	String path=request.getServletContext().getRealPath("/");
        try {
            return GameMineralService.genMineralCodes(path,mineral,num,mineralCodeDesc);
        } catch (Exception e) {
            log.error("保存矿卡方法内部错误",e);
            return BussinessMsgUtil.returnCodeMessage(BussinessCode.GEN_MINERALCODE_FAILED);
        }
    }
    @RequestMapping("/mineralBills_list.do")
	public String toMineralBillsListPage() {
		return "game/mineralBills_list";
	}
	/**
	 * 加载用户列表List
	 * @param user
	 * @return
	 */
	@RequestMapping("/ajax_mineralBills_list.do")
	@ResponseBody
	public String ajaxMineralBillsList(mineralBillsInfo mineralBillsInfo){
		try {
			if (!StringUtils.isBlank(mineralBillsInfo.getBeginStr())) {
				DateFormat format=	new SimpleDateFormat("yyyy-MM-dd");
				Date date=format.parse(mineralBillsInfo.getBeginStr());
				mineralBillsInfo.setBegin(date.getTime()/1000);
			}
			if (!StringUtils.isBlank(mineralBillsInfo.getEndStr())) {
				DateFormat format=	new SimpleDateFormat("yyyy-MM-dd");
				Date date=format.parse(mineralBillsInfo.getEndStr());
				mineralBillsInfo.setEnd(date.getTime()/1000);
			}
		} catch (Exception e) {
			log.error("",e);
			// TODO: handle exception
		}
		String str=GameMineralService.selectMineralBillsResultPageList(mineralBillsInfo);
		return str;
	}

	 @RequestMapping("/ajax_mineralCode_fail.do")
	    @ResponseBody
	    @SystemControllerLog(description="失效矿卡")
	    public BussinessMsg ajaxMineralCodeFail(Integer mineralCodeId){
	        try {
	            return GameMineralService.failedMineralCode(mineralCodeId);
	        } catch (Exception e) {
	            log.error("删除矿卡方法内部错误",e);
	            return BussinessMsgUtil.returnCodeMessage(BussinessCode.USER_FAIL_ERROR);
	        }
	    }
	    @RequestMapping("/ajax_add_mineral.do")
	    @ResponseBody
	    public BussinessMsg ajaxAddMineral(Integer userId,Integer mineral,Integer tagId,String desc){
	    	   try {
	    		   return GameMineralService.addMineral(userId,mineral*100,tagId, desc);
	           } catch (Exception e) {
	               log.error("添加玩家矿石内部错误",e);
	               return BussinessMsgUtil.returnCodeMessage(BussinessCode.ADD_MINERAL_FAILED);
	           }
	    	
	    	
	       
	    }
}
