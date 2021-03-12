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
							<div class="layui-input-inline" style="width: 110px;">
								<select name="searchTerm">
								    <option value='null'>默认</option>
									<option value="Ferrari">法拉利</option>
									<option value="Lambo">兰博</option>
									<option value="BMW">宝马</option>
									<option value="Benz">奔驰</option>
									<option value="Audi">奥迪</option>
									<option value="Honda">本田</option>
									<option value="Toyota">丰田</option>
									<option value="Volkswagen">大众</option>
								</select>
							</div>
							<a class="layui-btn billsSearchList_btn" lay-submit lay-filter="billsSearchFilter"><i class="layui-icon larry-icon larry-chaxun7"></i>设置开奖</a>
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
            url: '${ctx}/gameActive/ajax_benzBmw_info.do',
            id:'billsTableId',
            method: 'post',
            height:'full-140',
            skin:'row',
            even:'true',
            size: 'sm',
            cols: [[
                {type:"checkbox"},
                {field:'issue', title: '当前对局id',align:'center' },
                {field:'result', title: '开奖结果',align:'center'}
               
            ]],
            page: true,
            done: function (res, curr, count) {
                common.resizeGrid();
                layer.close(loading);

            }
        });

        /**查询*/
        $(".billsSearchList_btn").click(function(){
            var loading = layer.load(0,{ shade: [0.3,'#000']});
            //监听提交
            form.on('submit(billsSearchFilter)', function (data) {
                table.reload('billsTableId',{
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