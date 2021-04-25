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



import com.yxb.cms.controller.BasicController;
import com.yxb.cms.domain.bo.BussinessMsg;
import com.yxb.cms.domain.vo.User;
import com.yxb.cms.domain.vo.billsInfo;
import com.yxb.cms.domain.vo.freezeInfo;
import com.yxb.cms.domain.vo.gameRec;
import com.yxb.cms.domain.vo.orderInfo;
import com.yxb.cms.service.game.GameCapitalService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
@RequestMapping("gameCapital")
public class GameCapitalController extends BasicController {


	@Autowired
	private GameCapitalService GameCapitalService;

	
	
		@RequestMapping("/gameRec_list.do")
		public String toGameRecListPage() {
			return "game/gameRec_list";
		}
		@RequestMapping("/ajax_gameRec_list.do")
		@ResponseBody
		public String ajaxGameRecList(gameRec gameRec){
			try {
				if (!StringUtils.isBlank(gameRec.getBeginStr())) {
					DateFormat format=	new SimpleDateFormat("yyyy-MM-dd");
					Date date=format.parse(gameRec.getBeginStr());
					gameRec.setBegin(date.getTime()/1000);
				}
				if (!StringUtils.isBlank(gameRec.getEndStr())) {
					DateFormat format=	new SimpleDateFormat("yyyy-MM-dd");
					Date date=format.parse(gameRec.getEndStr());
					gameRec.setEnd(date.getTime()/1000);
				}
			} catch (Exception e) {
				log.error("",e);
				// TODO: handle exception
			}
			String str=GameCapitalService.selectGameRecResultPageList(gameRec);
			return str;
		}
	/**
	 *跳转到用户列表页面
	 * @return
	 */
	@RequestMapping("/bills_list.do")
	public String toBillsListPage() {
		return "game/bills_list";
	}
	/**
	 * 加载用户列表List
	 * @param user
	 * @return
	 */
	@RequestMapping("/ajax_bills_list.do")
	@ResponseBody
	public String ajaxBillsList(billsInfo billsInfo){
		
	 User 	user=this.getCurrentUser();
	if(!user.getUserLoginName().equals("admin")&&!user.getUserLoginName().equals("mng001")){
		billsInfo.setSearchTerm1("agentIdTerm");
		billsInfo.setSearchContent1(user.getUserId()+"");
	}
		
		try {
			if (!StringUtils.isBlank(billsInfo.getBeginStr())) {
				DateFormat format=	new SimpleDateFormat("yyyy-MM-dd");
				Date date=format.parse(billsInfo.getBeginStr());
				billsInfo.setBegin(date.getTime()/1000);
			}
			if (!StringUtils.isBlank(billsInfo.getEndStr())) {
				DateFormat format=	new SimpleDateFormat("yyyy-MM-dd");
				Date date=format.parse(billsInfo.getEndStr());
				billsInfo.setEnd(date.getTime()/1000);
			}
		} catch (Exception e) {
			log.error("",e);
			// TODO: handle exception
		}
		String str=GameCapitalService.selectBillsResultPageList(billsInfo);
		return str;
	}
	/**
	 *跳转到用户列表页面
	 * @return
	 */
	@RequestMapping("/freeze_list.do")
	public String toFreezeListPage() {
		return "game/freeze_list";
	}
	/**
	 * 加载用户列表List
	 * @param user
	 * @return
	 */
	@RequestMapping("/ajax_freeze_list.do")
	@ResponseBody
	public String ajaxFreezeList(freezeInfo freezeInfo){
		String str=GameCapitalService.selectFreezeResultPageList(freezeInfo);
		return str;
		
	}

	/**
	 *跳转到用户列表页面
	 * @return
	 */
	@RequestMapping("/order_list.do")
	public String toOrderListPage() {
		return "game/order_list";
	}
	/**
	 * 加载用户列表List
	 * @param user
	 * @return
	 */
	@RequestMapping("/ajax_order_list.do")
	@ResponseBody
	public String ajaxOrderList(orderInfo orderInfo,HttpServletRequest request){
		 User 	user=this.getCurrentUser();
		if(!user.getUserLoginName().equals("admin")&&!user.getUserLoginName().equals("mng001")){
			orderInfo.setSearchTerm("agentIdTerm");
			orderInfo.setSearchContent(user.getUserId()+"");
		}
		try {
			if (!StringUtils.isBlank(orderInfo.getBeginStr())) {
				DateFormat format=	new SimpleDateFormat("yyyy-MM-dd");
				Date date=format.parse(orderInfo.getBeginStr());
				orderInfo.setBegin(date.getTime()/1000);
			}
			if (!StringUtils.isBlank(orderInfo.getEndStr())) {
				DateFormat format=	new SimpleDateFormat("yyyy-MM-dd");
				Date date=format.parse(orderInfo.getEndStr());
				orderInfo.setEnd(date.getTime()/1000);
			}
		} catch (Exception e) {
			log.error("",e);
			// TODO: handle exception
		}
		
		
		String str=GameCapitalService.selectOrderResultPageList(orderInfo, request);
		return str;
		
	}
	@RequestMapping("/ajax_order_review.do")
	@ResponseBody
	public BussinessMsg orderReview(int orderId,int succ) throws Exception {
			return GameCapitalService.orderReview(orderId,succ);
		
		
	}


}
