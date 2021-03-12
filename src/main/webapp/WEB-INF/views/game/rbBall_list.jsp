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
						
							
						</form>
					</div>
					<shiro:hasPermission name="63Ry9Rel">
						<div class="layui-inline">
							<a class="layui-btn layui-btn-normal rbBallAdd_btn"> <i
								class="layui-icon larry-icon larry-xinzeng1"></i>新增一期
							</a>
						</div>
					</shiro:hasPermission>

				</blockquote>
				<div class="larry-separate"></div>
				<!-- 用户列表 -->
				<div class="layui-tab-item layui-show " style="padding: 10px 15px;">
					<table id="billsTableList" lay-filter="billsTableId"></table>
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
            elem: '#billsTableList',
            url: '${ctx}/gameActive/ajax_rbBall_list.do',
            id:'billsTableId',
            method: 'post',
            height:'full-140',
            skin:'row',
            even:'true',
            size: 'sm',
            cols: [[
                {type:"checkbox"},
                {field:'id', title: 'id',align:'center' },
                {field:'issue', title: '期No.',align:'center'},
                {field:'lotteryPool', title: '累计投注',align:'center'},
                {field:'lotteryResult', title: '开奖结果',align:'center'},
                {field:'lotteryPrice', title: '累计中奖',align:'center'}
            ]],
            page: true,
            done: function (res, curr, count) {
                common.resizeGrid();
                layer.close(loading);

            }
        });
        
        $(".rbBallAdd_btn").click(function(){
            var url = "${ctx}/gameActive/rbBall_add.do";
            common.cmsLayOpen('新增一期',url,'550px','265px');
        });


    });
</script>

<script type="text/html" id="remainTpl">
   {{d.remain}}
</script>

<script type="text/html" id="costTpl">
   {{d.cost}}
</script>

<script type="text/html" id="typeTpl">
    {{# if(d.type == 1){ }}
                                    游戏扣费                             
    {{# } else if(d.type == 2){ }}
                                   游戏结算奖励
    {{# } else if(d.type == 3){ }}
                                    充值                           
    {{# } else if(d.type == 4){ }}
                                  冻结
    {{# } else if(d.type == 5){ }}
                                   冻结返还
    {{# } else if(d.type == 6){ }}
                                    提现
    {{# } else if(d.type == 7){ }}
                                    游戏交易
    {{# } else if(d.type == 10){ }}
                                    游戏挖矿
    {{# } else if(d.type == 12){ }}
                                   后台修改
    {{# } else if(d.type == 20){ }}
                                   红绿球下注
    {{# } else if(d.type == 22){ }}
                                   一元购下注
    {{# } else { }}
    {{d.type}}
    {{# }  }}
</script>




</body>
</html>