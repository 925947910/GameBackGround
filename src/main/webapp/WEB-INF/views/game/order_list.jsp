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
						<form class="layui-form" id="orderSearchForm">
							<div class="layui-input-inline" style="width: 110px;">
								<select name="searchTerm">
									<option value="uidTerm">玩家ID</option>
									<option value="idTerm">订单ID</option>
									<option  value="orderNoTerm">订单号</option>
									<option  value="orderTypeTerm">订单类型</option>
								</select>
							</div>
							<div class="layui-input-inline" style="width: 145px;">
								<input type="text" name="searchContent" value=""
									placeholder="请输入关键字" class="layui-input search_input">
							</div>
							<a class="layui-btn orderSearchList_btn" lay-submit lay-filter="orderSearchFilter"><i class="layui-icon larry-icon larry-chaxun7"></i>查询</a>
						</form>
					</div>
					<!--      <shiro:hasPermission name="0rbT8t2P">
                    <div class="layui-inline">
                        <a class="layui-btn layui-btn-normal userAdd_btn"> <i class="layui-icon larry-icon larry-xinzeng1"></i>新增用户</a>
                    </div>
                </shiro:hasPermission>
                <shiro:hasPermission name="0jOfTHGx">
                    <div class="layui-inline">
                        <a class="layui-btn layui-btn-normal excelUserExport_btn"  style="background-color:#5FB878"> <i class="layui-icon larry-icon larry-danye"></i>导出</a>
                    </div>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="lBE3hz5c">
                    <div class="layui-inline">
                        <a class="layui-btn layui-btn-danger userBatchFail_btn"><i class="layui-icon larry-icon larry-shanchu"></i>批量失效</a>
                    </div>
                </shiro:hasPermission>
 -->
				</blockquote>
				<div class="larry-separate"></div>
				<!-- 用户列表 -->
				<div class="layui-tab-item layui-show " style="padding: 10px 15px;">
					<table id="orderTableList" lay-filter="orderTableId"></table>
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
            elem: '#orderTableList',
            url: '${ctx}/gameCapital/ajax_order_list.do',
            id:'orderTableId',
            method: 'post',
            height:'full-140',
            skin:'row',
            even:'true',
            size: 'sm',
            cols: [[
                {type:"checkbox"},
                {field:'id', title: '订单ID',align:'center' },
                {field:'uid', title: '玩家ID',align:'center'},
                {field:'accountOut', title: '账号',align:'center'},
                {field:'accountIn', title: '昵称',align:'center'},
                {field:'orderRemote', title: '订单号',align:'center'},
                {field:'currency', title: '钱包地址',align:'center'},
                {field:'coin', title: '金币',align:'center',templet: '#coinTpl'},
                {field:'orderType', title: '订单类型',align:'center',templet: '#orderTypeTpl'},
                {field:'status', title: '订单状态',align:'center',templet: '#statusTpl'},
                {field:'time', title: '时间',align:'center',templet:"<div>{{layui.util.toDateString(d.time*1000)}}</div>"},
                {title: '操作', align:'center', width: '17%',toolbar: '#orderBar'}
                ]],
            page: true,
            done: function (res, curr, count) {
                common.resizeGrid();
                layer.close(loading);

            }
        });

        /**查询*/
        $(".orderSearchList_btn").click(function(){
            var loading = layer.load(0,{ shade: [0.3,'#000']});
            //监听提交
            form.on('submit(orderSearchFilter)', function (data) {
                table.reload('orderTableId',{
                    where: {
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

        /**监听工具条*/
        table.on('tool(orderTableId)', function(obj){
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值
            if(layEvent === 'orderOn') {
            	var orderId = data.id;
                var url = "${ctx}/gameCapital/ajax_order_review.do";
                var param = {orderId:orderId,succ:1};
                common.ajaxCmsConfirm('系统提示', '你确定审核冻结资金?',url,param);
            }else if(layEvent === 'orderOff'){
            	var orderId = data.id;
                var url = "${ctx}/gameCapital/ajax_order_review.do";
                var param = {orderId:orderId,succ:0};
                common.ajaxCmsConfirm('系统提示', '你确定审核冻结资金?',url,param);
            }
            
        });


    });
</script>
<script type="text/html" id="coinTpl">
   {{d.coin/100}}
</script>>
<script type="text/html" id="orderTypeTpl">
    {{# if(d.orderType == 1){ }}
                                          充值订单
    {{# } else if(d.orderType == 2){ }}
                                         提现订单
    {{# } else { }}
    {{d.orderType}}
    {{# }  }}
</script>
<script type="text/html" id="statusTpl">
    {{# if(d.status == 0){ }}
                                    初始订单                               
    {{# } else if(d.status == 1){ }}
                                    订单校验成功
    {{# } else if(d.status == 2){ }}
                                    订单处理中                           
    {{# } else if(d.status == 3){ }}
                                    成功订单
    {{# } else if(d.status == 4){ }}
                                    失败订单
    {{# } else { }}
    {{d.status}}
    {{# }  }}
</script>
</script>
	<!--工具条 -->
	<script type="text/html" id="orderBar">
    <div class="layui-btn-group">
        <shiro:hasPermission name="gA3ITNsk">
            <a class="layui-btn layui-btn-xs orderOn" lay-event="orderOn"><i class="layui-icon larry-icon larry-bianji2"></i>审核通过</a>
        </shiro:hasPermission>
    </div>
    <div class="layui-btn-group">
        <shiro:hasPermission name="gA3ITNsk">
            <a class="layui-btn layui-btn-xs orderOff" lay-event="orderOff"><i class="layui-icon larry-icon larry-bianji2"></i>审核拒绝</a>
        </shiro:hasPermission>
    </div>
</script>
	


</body>
</html>