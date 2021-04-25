<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/comm/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>后台管理系统</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="后台管理系统">
    <meta name="description" content="致力于提供通用版本后台管理解决方案">

    <link rel="shortcut icon" href="${ctx}/static/img/favicon.ico">
    <link rel="stylesheet" href="${ctx}/static/layui_v2/css/layui.css">
    <link rel="stylesheet" href="${ctx}/static/css/global.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/common.css" media="all">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/personal.css" media="all">
    <link rel="stylesheet" type="text/css" href="http://at.alicdn.com/t/font_9h680jcse4620529.css">
    <script src="${ctx}/static/layui_v2/layui.js"></script>


<body>
<div class="larry-grid layui-anim layui-anim-upbit larryTheme-A ">
    <div class="larry-personal">
        <div class="layui-tab">
            <blockquote class="layui-elem-quote mylog-info-tit">
                <div class="layui-inline">
                    <form class="layui-form" id="userSearchForm">
                        <div class="layui-input-inline" style="width:110px;">
                            <select name="searchTerm" >
                                <option value="idTerm">玩家ID</option>
                            </select>
                        </div>
                        <div class="layui-input-inline" style="width:145px;">
                            <input type="text" name="searchContent" value="" placeholder="请输入关键字" class="layui-input search_input">
                        </div>
                        <a class="layui-btn userSearchList_btn" lay-submit lay-filter="userSearchFilter"><i class="layui-icon larry-icon larry-chaxun7"></i>查询</a>
                    </form>
            </blockquote>
             <!--
        
            <!-- 用户列表 -->
            <div class="layui-tab-item layui-show " style="padding: 10px 15px;">
                <table id="userTableList"  lay-filter="userTableId" ></table>
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

        var loading = layer.load(0,{ shade: [0.3,'#000']});
        /**用户表格加载*/
        table.render({
            elem: '#userTableList',
            url: '${ctx}/gameActive/ajax_benzBmw_list.do',
            id:'userTableId',
            method: 'post',
            height:'full-140',
            skin:'row',
            even:'true',
            size: 'sm',

            cols: [[
                {type:"checkbox"},
                {field:'uid', title: '玩家ID',align:'center' },
                {field:'pool', title: '金币池',align:'center'},
                {field:'per', title: '最大回本比例',align:'center'},
                {field:'perWin', title: '胜率',align:'center'},
                {title: '操作', align:'center', width: '17%',toolbar: '#userBar'}
            ]],
            page: true,
            done: function (res, curr, count) {
            	$("#currOnlines").val(res.currOnlines);
                common.resizeGrid();
                layer.close(loading);

            }
        });

        /**查询*/
        $(".userSearchList_btn").click(function(){
            var loading = layer.load(0,{ shade: [0.3,'#000']});
            //监听提交
            form.on('submit(userSearchFilter)', function (data) {
                table.reload('userTableId',{
                    where: {
                            searchTerm:data.field.searchTerm,
                            searchContent:data.field.searchContent
                    },
                    height: 'full-140',
                    page: true,
                    done: function (res, curr, count) {
                    	$("#currOnlines").val(res.currOnlines);
                        common.resizeGrid();
                        layer.close(loading);

                    }
                });

            });

        });


        /**监听工具条*/
        table.on('tool(userTableId)', function(obj){
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值

            //添加金币
            if(layEvent === 'edit_benzBmw') {
                var uid = data.uid;
                var pool = data.pool;
                var per = data.per;
                var perWin = data.perWin;
                var url =  "${ctx}/gameActive/edit_benzBmw.do?uid="+uid+"&pool="+pool+"&per="+per+"&perWin="+perWin;
                common.cmsLayOpen('编辑金币',url,'550px','265px');

            //添加矿石
            }
            
            
        });


    });
</script>
<script type="text/html" id="mineralTpl">
   {{d.mineral/100}}
</script>
<script type="text/html" id="coinTpl">
   {{d.coin}}
</script>
<!--工具条 -->
<script type="text/html" id="userBar">
    <div class="layui-btn-group">
        <shiro:hasPermission name="fZKo6sJb">
            <a class="layui-btn layui-btn-xs edit_benzBmw" lay-event="edit_benzBmw"><i class="layui-icon larry-icon larry-bianji2"></i>编辑</a>
        </shiro:hasPermission>       
    </div>
</script>

</body>
</html>