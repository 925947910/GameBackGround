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
                                <option value="accTerm">玩家账号</option>
                                <option value="nickTerm">玩家昵称</option>
                                <option value="agentIdTerm">代理Id</option>
                               
                            </select>
                        </div>
                        <div class="layui-input-inline" style="width:145px;">
                            <input type="text" name="searchContent" value="" placeholder="请输入关键字" class="layui-input search_input">
                        </div>
                        <a class="layui-btn userSearchList_btn" lay-submit lay-filter="userSearchFilter"><i class="layui-icon larry-icon larry-chaxun7"></i>查询</a>
                    </form>
                </div>
                 <shiro:hasPermission name="CWiObpIP">
                    <div class="layui-inline">
                        <a class="layui-btn layui-btn-normal gameUserAdd_btn"> <i class="layui-icon larry-icon larry-xinzeng1"></i>新增玩家</a>
                    </div>
                </shiro:hasPermission>
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
            url: '${ctx}/gameUser/ajax_user_list.do',
            id:'userTableId',
            method: 'post',
            height:'full-140',
            skin:'row',
            even:'true',
            size: 'sm',

            cols: [[
                {type:"checkbox"},
                {field:'id', title: '玩家ID',align:'center' },
                {field:'acc', title: '玩家账号',align:'center'},
                {field:'pwd', title: '玩家密码',align:'center'},
                {field:'nick', title: '玩家昵称',align:'center'},
                {field:'phone', title: '电话号码',align:'center'},
                {field:'sex', title: '性别',align:'center'},
                {field:'coin', title: '金币',align:'center' },
                {field:'freezed', title: '是否冻结',align:'center'},
                {field:'isTourist', title: '是否游客',align:'center'},
                {field:'isLeader', title: '是否团长',align:'center'},
                {field:'agentId', title: '代理Id',align:'center'},
                {field:'presenterId', title: '推荐人Id',align:'center'},
                {field:'regTime', title: '注册时间',align:'center',templet:"<div>{{layui.util.toDateString(d.regTime*1000)}}</div>"},
                
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

        /**新增*/
        $(".gameUserAdd_btn").click(function(){
            var url = "${ctx}/gameUser/gameUser_add.do";
            common.cmsLayOpen('新增玩家',url,'550px','265px');
        });
        /**监听工具条*/
        table.on('tool(userTableId)', function(obj){
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值

            //添加金币
            if(layEvent === 'coin_add') {
                var userId = data.id;
                var act = "coin";
                var url =  "${ctx}/gameUser/user_update.do?userId="+userId+"&act="+act;
                common.cmsLayOpen('编辑金币',url,'550px','265px');

            //添加矿石
            }else if(layEvent === 'mineral_add') {
                    var userId = data.id;
                    var act = "mineral";
                    var url =  "${ctx}/gameUser/user_update.do?userId="+userId+"&act="+act;
                    common.cmsLayOpen('编辑矿石',url,'550px','265px');

               
                }else if(layEvent === 'update_pwd'){
                	 var userId = data.id;
                     var act = "pwd";
                     var url =  "${ctx}/gameUser/user_update.do?userId="+userId+"&act="+act;
                     common.cmsLayOpen('修改密码',url,'550px','265px');
                	
                }else if(layEvent === 'user_freeze'){
               	 var userId = data.id;
                 var act = "freeze";
                 var url =  "${ctx}/gameUser/user_update.do?userId="+userId+"&act="+act;
                 common.cmsLayOpen('解冻冻结',url,'550px','265px');
            	
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
            <a class="layui-btn layui-btn-xs coin_add" lay-event="coin_add"><i class="layui-icon larry-icon larry-bianji2"></i>添加金币</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="42wUOYef">
            <a class="layui-btn layui-btn-xs update_user" lay-event="update_pwd"><i class="layui-icon larry-icon larry-bianji2"></i>修改密码</a>
        </shiro:hasPermission>
         <shiro:hasPermission name="uG9vazWK">
            <a class="layui-btn layui-btn-xs mineral_add" lay-event="mineral_add"><i class="layui-icon larry-icon larry-bianji2"></i>添加矿石</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="QsTIzP4o">
            <a class="layui-btn layui-btn-xs user_freeze" lay-event="user_freeze"><i class="layui-icon larry-icon larry-bianji2"></i>解冻冻结</a>
        </shiro:hasPermission>
    </div>
</script>

</body>
</html>