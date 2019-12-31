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


import com.yxb.cms.architect.constant.BussinessCode;
import com.yxb.cms.architect.utils.BussinessMsgUtil;
import com.yxb.cms.controller.BasicController;
import com.yxb.cms.domain.bo.BussinessMsg;
import com.yxb.cms.domain.vo.User;
import com.yxb.cms.domain.vo.gameUser;
import com.yxb.cms.service.game.GameUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
@RequestMapping("gameUser")
public class GameUserController extends BasicController {


    @Autowired
    private GameUserService GameUserService;

    /**
     *跳转到用户列表页面
     * @return
     */
    @RequestMapping("/user_list.do")
    public String toUserListPage() {
        return "game/user_list";
    }
    /**
     * 加载用户列表List
     * @param user
     * @return
     */
    @RequestMapping("/ajax_user_list.do")
    @ResponseBody
    public String ajaxUserList(gameUser user){
        return GameUserService.selectUserResultPageList(user);
    }

    @RequestMapping("/user_update.do")
    public String userUpdatePage(Model model,Integer userId,String act){
        model.addAttribute("userId", userId);
        switch (act) {
		case "coin":
			 return "game/coin_edit";

		default:
			 return "game/mineral_edit";
		}
       
    }
    @RequestMapping("/ajax_add_coin.do")
    @ResponseBody
    public BussinessMsg ajaxAddCoin(Integer userId,Integer coin,Integer tagId,String desc){
    	   try {
    		   return GameUserService.addCoin(userId,coin*100,tagId, desc);
           } catch (Exception e) {
               log.error("添加玩家金币内部错误",e);
               return BussinessMsgUtil.returnCodeMessage(BussinessCode.ADD_COIN_FAILED);
           }
    	
    	
       
    }
   



}
