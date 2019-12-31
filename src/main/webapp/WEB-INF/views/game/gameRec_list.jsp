<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/comm/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>后台管理系统</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="keywords" content="后台管理系统">
<meta name="description" content="致力于提供通用版本后台管理解决方案">

<link rel="shortcut icon" href="${ctx}/static/img/favicon.ico">
<link rel="stylesheet" href="${ctx}/static/layui_v2/css/layui.css">
<link rel="stylesheet" href="${ctx}/static/css/global.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/common.css" media="all">
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/personal.css" media="all">
<link rel="stylesheet" type="text/css"
	href="http://at.alicdn.com/t/font_9h680jcse4620529.css">
<script src="${ctx}/static/layui_v2/layui.js"></script>
<body>
	<div class="larry-grid layui-anim layui-anim-upbit larryTheme-A ">
		<div class="larry-personal">
			<div class="layui-tab">
				<blockquote class="layui-elem-quote mylog-info-tit">
					<div class="layui-inline">
						<form class="layui-form" id="gameRecSearchForm">
							<div class="layui-input-inline">
								<label for="begin">开始时间:</label><input name="beginStr" id="beginStr"type="date" /> 
							</div>
							<div class="layui-input-inline">
								<label for="end">结束时间:</label><input name="endStr"id="endStr" type="date" />
							</div>
							<div class="layui-input-inline" style="width: 110px;">
								<select name="searchTerm">
									<option value="codeTerm">对局流水号</option>
								</select>
							</div>
							<div class="layui-input-inline" style="width: 145px;">
								<input type="text" name="searchContent" value=""
									placeholder="请输入关键字" class="layui-input search_input">
							</div>
							<a class="layui-btn gameRecSearchList_btn" lay-submit lay-filter="gameRecSearchFilter"><i class="layui-icon larry-icon larry-chaxun7"></i>查询</a>
						</form>
					</div>
					
				</blockquote>
				<div class="larry-separate"></div>
				<!-- 用户列表 -->
				<div class="layui-tab-item layui-show " style="padding: 10px 15px;">
					<table id="gameRecTableList" lay-filter="gameRecTableId"></table>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
    var $;
    layui.config({
        base : "${ctx}/static/js/"
    }).use(['form', 'table', 'layer','common','util'], function () {
         $ =  layui.$;
                var form = layui.form,
                table = layui.table,
                layer = layui.layer,
                common = layui.common;
                util=layui.util;

        var loading = layer.load(0,{ shade: [0.3,'#000']});
        /**用户表格加载*/
        table.render({
            elem: '#gameRecTableList',
            url: '${ctx}/gameCapital/ajax_gameRec_list.do',
            id:'gameRecTableId',
            method: 'post',
            height:'full-140',
            skin:'row',
            even:'true',
            size: 'sm',
            cols: [[
                {type:"checkbox"},
                {field:'id', title: 'id',align:'center' },
                {field:'recordCode', title: '游戏对局流水号',align:'center'},
                {field:'gameId', title: '游戏',align:'center' ,templet: '#gameTpl'},
                {field:'gameResult', title: '对局详情',align:'center'},
                {field:'beginTime', title: '开始时间',align:'center',templet:"<div>{{layui.util.toDateString(d.beginTime*1000)}}</div>"},
                {field:'endTime', title: '结束时间',align:'center',templet:"<div>{{layui.util.toDateString(d.endTime*1000)}}</div>"}
            ]],
            page: true,
            done: function (res, curr, count) {
                common.resizeGrid();
                layer.close(loading);

            }
        });

        /**查询*/
        $(".gameRecSearchList_btn").click(function(){
            var loading = layer.load(0,{ shade: [0.3,'#000']});
            //监听提交
            form.on('submit(gameRecSearchFilter)', function (data) {
                table.reload('gameRecTableId',{
                    where: {
                    	    beginStr:data.field.beginStr,
                    	    endStr:data.field.endStr,
                            searchTerm:data.field.searchTerm,
                            searchContent:data.field.searchContent
                    },
                    height: 'full-140',
                    page: true,
                    done: function (res, curr, count) {
                        common.resizeGrid();
                        layer.close(loading);

                    }
                });

            });

        });



    });
</script>


<script type="text/html" id="gameTpl">
    {{# if(d.gameId == 1){ }}
                                      贪吃蛇                             
    {{# } else if(d.gameId == 2){ }}
                                   坦克大战
    {{# } else if(d.gameId == 3){ }}
                                    斗币大咖跑       
    {{# } else { }}
    {{d.gameId}}
    {{# }  }}
</script>





</body>
</html>