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
                                <option value="uidTerm">使用者Id</option>
                                <option value="statusTerm">使用状态</option>
                            </select>
                        </div>
                        <div class="layui-input-inline" style="width:145px;">
                            <input type="text" name="searchContent" value="" placeholder="请输入关键字" class="layui-input search_input">
                        </div>
                        <a class="layui-btn mineralCodeSearchList_btn" lay-submit lay-filter="mineralCodeSearchFilter"><i class="layui-icon larry-icon larry-chaxun7"></i>查询</a>
                    </form>
                </div>
                <shiro:hasPermission name="B69B6W6k">
                    <div class="layui-inline">
                        <a class="layui-btn layui-btn-normal mineralCodeAdd_btn"> <i class="layui-icon larry-icon larry-xinzeng1"></i>新增矿卡</a>
                    </div>
                </shiro:hasPermission>
              
            </blockquote>
            <div class="larry-separate"></div>
            <!-- 用户列表 -->
            <div class="layui-tab-item layui-show " style="padding: 10px 15px;">
                <table id="mineralCodeTableList"  lay-filter="mineralCodeTableId" ></table>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var $;
    layui.config({
        base : "${ctx}/static/js/"
    }).use(['form', 'table', 'layer','common'], function () {
         $ =  layui.$;
                var form = layui.form,
                table = layui.table,
                layer = layui.layer,
                common = layui.common;

        var loading = layer.load(0,{ shade: [0.3,'#000']});
        /**用户表格加载*/
        table.render({
            elem: '#mineralCodeTableList',
            url: '${ctx}/gameMineral/ajax_mineralCode_list.do',
            id:'mineralCodeTableId',
            method: 'post',
            height:'full-140',
            skin:'row',
            even:'true',
            size: 'sm',
            cols: [[
                {type:"checkbox"},
                {field:'id', title: 'ID',align:'center' },
                {field:'codeNo', title: '矿卡号码',align:'center'},
                {field:'mineral', title: '潜力值',align:'center',templet: '#mineralTpl'},
                {field:'status', title: '矿卡状态',align:'center',width: '8%',templet: '#mineralCodeStatusTpl'},
                {field:'codeDesc', title: '描述',align:'center'},
                {field:'type', title: '矿卡类型',align:'center',width: '12%'},
                {field:'uid', title: '领取者',align:'center'},
                {title: '操作', align:'center', width: '17%',toolbar: '#mineralCodeBar'}
            ]],
            page: true,
            done: function (res, curr, count) {
                common.resizeGrid();
                layer.close(loading);

            }
        });

        /**查询*/
        $(".mineralCodeSearchList_btn").click(function(){
            var loading = layer.load(0,{ shade: [0.3,'#000']});
            //监听提交
            form.on('submit(mineralCodeSearchFilter)', function (data) {
                table.reload('mineralCodeTableId',{
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

        /**新增*/
        $(".mineralCodeAdd_btn").click(function(){
            var url = "${ctx}/gameMineral/mineralCode_add.do";
            common.cmsLayOpen('新增矿卡',url,'550px','265px');
        });




        /**监听工具条*/
        table.on('tool(mineralCodeTableId)', function(obj){
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值

             if(layEvent === 'mineralCode_fail') {
                var id = data.id;
                
                var url = "${ctx}/gameMineral/ajax_mineralCode_fail.do";
                var param = {mineralCodeId:id};
                common.ajaxCmsConfirm('系统提示', '确定失效矿卡吗?',url,param);

            }
        });


    });
</script>
<!-- 用户状态tpl-->
<script type="text/html" id="mineralCodeStatusTpl">

    {{# if(d.status == 0){ }}
    <span style="color:#5FB878;font-weight:bold"> 0-有效</span>
    {{# } else if(d.status == 1){ }}
    <span style="color:#FF5722;font-weight:bold"> 1-已使用</span>
     {{# } else if(d.status == 2){ }}
    <span style="color:#FF5722;font-weight:bold"> 2-失效</span>
    {{# } else { }}
    {{d.status}}
    {{# }  }}
</script>

<script type="text/html" id="mineralTpl">
   {{d.mineral/100}}
</script>
<!--工具条 -->
<script type="text/html" id="mineralCodeBar">
    <div class="layui-btn-group">
        <shiro:hasPermission name="AELCG3Rd">
            <a class="layui-btn layui-btn-xs layui-btn-danger mineralCode_fail" lay-event="mineralCode_fail"><i class="layui-icon larry-icon larry-ttpodicon"></i>失效</a>
        </shiro:hasPermission>
    </div>
</script>



</body>
</html>