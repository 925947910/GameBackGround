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

    <script src="${ctx}/static/layui_v2/layui.js"></script>

</head>
<body class="childrenBody" style="font-size: 12px;margin: 10px 10px 0;">
<form class="layui-form layui-form-pane">

    <div class="layui-form-item">
        <label class="layui-form-label">玩家账号</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" name="acc"  maxlength="20" value="" placeholder="请输入玩家账号">
        </div>
    </div>
        <div class="layui-form-item">
        <label class="layui-form-label">玩家昵称</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" name="nick"  maxlength="20" value="" placeholder="请输入玩家昵称">
        </div>
    </div>
    </div>
        <div class="layui-form-item">
        <label class="layui-form-label">电话号码</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" name="phone"  maxlength="20" value="" placeholder="请输入电话号码">
        </div>
    </div>
   
    <div class="layui-form-item" style="text-align: center;">
            <button class="layui-btn" lay-submit="" lay-filter="saveGameUser">保存</button>
            <button type="layui-btn" id="cancle" class="layui-btn layui-btn-primary">取消</button>

    </div>
</form>
<script type="text/javascript">
    layui.config({
        base : "${ctx}/static/js/"
    }).use(['form','layer','jquery','common'],function(){
        var $ = layui.$,
                form = layui.form,
                common = layui.common,
                layer = parent.layer === undefined ? layui.layer : parent.layer;


        /**保存*/
        form.on("submit(saveGameUser)",function(data){
            var gameUserSaveLoading = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
            //登陆验证
            $.ajax({
                url : '${ctx}/gameUser/ajax_save_gameUser.do',
                type : 'post',
                async: false,
                data : data.field,
                success : function(data) {
                    if(data.returnCode == 0000){
                        top.layer.close(gameUserSaveLoading);
                        common.cmsLaySucMsg("添加成功！还需添加:"+data.returnData+"个")
                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        parent.layer.close(index); //再执行关闭                        //刷新父页面
                        parent.location.reload();
                    }else{
                        top.layer.close(gameUserSaveLoading);
                        common.cmsLayErrorMsg(data.returnMessage);
                    }
                },error:function(data){
                    top.layer.close(index);

                }
            });
            return false;
        });
        //取消
        $("#cancle").click(function(){
            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
            parent.layer.close(index); //再执行关闭
        });

    });

</script>
</body>
</html>