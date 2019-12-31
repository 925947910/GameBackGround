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
						<form class="layui-form" id="billsSearchForm">
							<div class="layui-input-inline">
								<label for="begin">开始时间:</label><input name="beginStr" id="beginStr"type="date" /> 
							</div>
							<div class="layui-input-inline">
								<label for="end">结束时间:</label><input name="endStr"id="endStr" type="date" />
							</div>
							<div class="layui-input-inline" style="width: 110px;">
								<select name="searchTerm">
									<option value="uidTerm">玩家ID</option>
								</select>
							</div>
							<div class="layui-input-inline" style="width: 145px;">
								<input type="text" name="searchContent" value=""
									placeholder="请输入关键字" class="layui-input search_input">
							</div>
							<a class="layui-btn mineralBillsSearchList_btn" lay-submit lay-filter="mineralBillsSearchFilter"><i class="layui-icon larry-icon larry-chaxun7"></i>查询</a>
						</form>
					</div>
					

				</blockquote>
				<div class="larry-separate"></div>
				<!-- 用户列表 -->
				<div class="layui-tab-item layui-show " style="padding: 10px 15px;">
					<table id="mineralBillsTableList" lay-filter="mineralBillsTableId"></table>
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
            elem: '#mineralBillsTableList',
            url: '${ctx}/gameMineral/ajax_mineralBills_list.do',
            id:'mineralBillsTableId',
            method: 'post',
            height:'full-140',
            skin:'row',
            even:'true',
            size: 'sm',
            cols: [[

                {type:"checkbox"},
                {field:'id', title: 'id',align:'center' },
                {field:'uid', title: '玩家id',align:'center'},
                {field:'nick', title: '昵称',align:'center'},
                {field:'mineral', title: '矿石剩余',align:'center',templet: '#mineralTpl'},
                {field:'cost', title: '矿石变化',align:'center',templet: '#costTpl'},
                {field:'freeze', title: '冻结数量',align:'center',templet: '#freezeTpl'},
                {field:'type', title: '类型',align:'center',templet: '#typeTpl'},
                {field:'tagId', title: '来源目标ID',align:'center'},
                {field:'reason', title: '描述',align:'center'},
                {field:'time', title: '时间',align:'center',templet:"<div>{{layui.util.toDateString(d.time*1000)}}</div>"}
            ]],
            page: true,
            done: function (res, curr, count) {
                common.resizeGrid();
                layer.close(loading);

            }
        });

        /**查询*/
        $(".mineralBillsSearchList_btn").click(function(){
            var loading = layer.load(0,{ shade: [0.3,'#000']});
            //监听提交
            form.on('submit(mineralBillsSearchFilter)', function (data) {
                table.reload('mineralBillsTableId',{
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

<script type="text/html" id="freezeTpl">
   {{d.freeze/100}}
</script>
<script type="text/html" id="mineralTpl">
   {{d.mineral/100}}
</script>
<script type="text/html" id="costTpl">
   {{d.cost/100}}
</script>

	<script type="text/html" id="typeTpl">

    {{# if(d.type == 8){ }}
                                   购买商品奖励            
    {{# } else if(d.type == 9){ }}
                                   推荐购买返点
    {{# } else if(d.type == 10){ }}
                                    游戏挖矿
    {{# } else if(d.type == 13){ }}
          gm后台添加                                       
    {{# } else { }}
    {{d.type}}
    {{# }  }}
</script>



</body>
</html>