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
import com.yxb.cms.architect.utils.CommonHelper;
import com.yxb.cms.controller.BasicController;
import com.yxb.cms.domain.bo.BussinessMsg;
import com.yxb.cms.domain.bo.ExcelExport;
import com.yxb.cms.domain.vo.User;
import com.yxb.cms.domain.vo.benzBmwInfo;
import com.yxb.cms.domain.vo.rbBallInfo;
import com.yxb.cms.service.game.GameActiveService;




import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;




@Controller
@RequestMapping("gameActive")
public class GameActiveController extends BasicController {


    @Autowired
    private GameActiveService GameActiveService;

    /**
     *跳转到用户列表页面
     * @return
     */
    @RequestMapping("/rbBall_thr_list.do")
    public String rbBallList3(Model model) {
    	model.addAttribute("period", 3);
        return "game/rbBall_list";
    }
    @RequestMapping("/rbBall_five_list.do")
    public String rbBallList5(Model model) {
    	model.addAttribute("period", 5);
        return "game/rbBall_list";
    }
    @RequestMapping("/rbBall_ten_list.do")
    public String rbBallList10(Model model) {
    	model.addAttribute("period", 10);
        return "game/rbBall_list";
    }
    @RequestMapping("/ajax_rbBall_list.do")
	@ResponseBody
	public String ajaxRbBallList(rbBallInfo rbBallInfo,int period){
    	rbBallInfo.setPeriod(period);
    	String str=GameActiveService.selectRbBallResultPageList(rbBallInfo);
		return str;
		
	}
    @RequestMapping("/rbBall_add.do")
	public String toRbBallAdd(Model model,int period){
    	model.addAttribute("period", period);
    	return "game/rbBall_edit";
		
	}
    @RequestMapping("/ajax_add_rbBall.do")
   	@ResponseBody
   	public BussinessMsg addRbBall(rbBallInfo rbBallInfo){
 
   	  try {
          return GameActiveService.addRbBall(rbBallInfo);
      } catch (Exception e) {
          log.error("保存错误",e);
          return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_ERROR);
      }
     
   	}
    

    
    @RequestMapping("/ajax_rbBall_excel_add.do")
   	@ResponseBody
   	public BussinessMsg addRbBallExcel(HttpServletRequest request,int period){
 
   	  try {
          return GameActiveService.addRbBallExcel(request,period);
      } catch (Exception e) {
          log.error("保存错误",e);
          return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_ERROR);
      }
     
   	}
    /**
     * 导出用户列表信息
     * @param user 用户实体
     * @return
     */
    @RequestMapping("/excel_rbBall_export.do")
    public ModelAndView excelRbBallExport(rbBallInfo rbBallInfo){
        ExcelExport excelExport = GameActiveService.excelRbBallInfoList(rbBallInfo);
        return CommonHelper.getExcelModelAndView(excelExport);
    }
    
    @RequestMapping("/benzBmw_list.do")
    public String benzBmwInfo() {
    	return "game/benzBmw_info";
    }
    @RequestMapping("/edit_benzBmw.do")
    public String to_benzBmw_edit(Model model,int uid,int per,int pool ,int perWin) {
    	model.addAttribute("uid", uid);
    	model.addAttribute("per", per);
    	model.addAttribute("pool", pool);
    	model.addAttribute("perWin", perWin);
    	return "game/benzBmw_edit";
    }
    @RequestMapping("/ajax_edit_benzBmw.do")
   	@ResponseBody
   	public BussinessMsg edit_benzBmw(benzBmwInfo benzBmwInfo){
 
   	  try {
          return GameActiveService.edit_benzBmw(benzBmwInfo);
      } catch (Exception e) {
          log.error("保存错误",e);
          return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_ERROR);
      }
     
   	}
    
    
    
    @RequestMapping("/ajax_benzBmw_list.do")
   	@ResponseBody
   	public String ajaxBenzBmwInfo(benzBmwInfo benzBmwInfo){
   		
       	String str=GameActiveService.benzBmwInfo(benzBmwInfo);
   		return str;
   		
   	}
    
}