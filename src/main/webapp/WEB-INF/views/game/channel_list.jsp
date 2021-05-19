<%@ page isELIgnored="false" language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/comm/mytags.jsp" %>
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

				</blockquote>
				<div class="larry-separate"></div>
				<div class="layui-input-inline">
								<h2 class="t-center">支付通道:</h2><input id="channelIn"  type="text" readonly="readonly" />
								</br>
								<h2 class="t-center">代付通道:</h2><input id="channelOut"  type="text" readonly="readonly" />
							</div>
							
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
            url: '${ctx}/gameCapital/ajax_channel_list.do',
         
            id:'orderTableId',
            method: 'post',
            height:'full-140',
            skin:'row',
            even:'true',
            size: 'sm',
            cols: [[
                {type:"checkbox"},
                {field:'id', title: '通道ID',align:'center' },
                {field:'name', title: '通道名字',align:'center'},
                {field:'extractPer', title: '支付手续费',align:'center'},
                {title: '操作', align:'center', width: '17%',toolbar: '#orderBar'}
                ]],
            page: true,
            done: function (res, curr, count) {
            	 $("#channelIn").val(res.channelIn);
            	 $("#channelOut").val(res.channelOut);
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
            	var channelName = data.name;
                var url = "${ctx}/gameCapital/ajax_channel_choose.do";
                var param = {channelName:channelName,act:'channelIn'};
                common.ajaxCmsConfirm('系统提示', '你确定设为支付通道?',url,param);
            }else if(layEvent === 'orderOff'){
            	var channelName = data.name;
                var url = "${ctx}/gameCapital/ajax_channel_choose.do";
                var param = {channelName:channelName,act:'channelOut'};
                common.ajaxCmsConfirm('系统提示', '你确定设为代付通道?',url,param);
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
<script type="text/html" id="platTpl">
    {{# if(d.plat == 1){ }}
                    OtPay
    {{# } else if(d.plat == 2){ }}
                    TikPay
    {{# } else if(d.plat == 3){ }}
                    AmPay
    {{# } else { }}
    {{d.plat}}
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
        <shiro:hasPermission name="c3BRMr4h">
            <a class="layui-btn layui-btn-xs orderOn" lay-event="orderOn"><i class="layui-icon larry-icon larry-bianji2"></i>设为支付</a>
        </shiro:hasPermission>
    </div>
    <div class="layui-btn-group">
        <shiro:hasPermission name="c3BRMr4h">
            <a class="layui-btn layui-btn-xs orderOff" lay-event="orderOff"><i class="layui-icon larry-icon larry-bianji2"></i>设为代付</a>
        </shiro:hasPermission>
    </div>
</script>
	


</body>
</html>