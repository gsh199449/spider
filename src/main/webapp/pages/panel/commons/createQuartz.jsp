<%--
  Created by IntelliJ IDEA.
  User: gaoshen
  Date: 2017/1/18
  Time: 20:14
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
<head>
    <title>定时网页抓取任务创建</title>
    <%@include file="../../commons/header.jsp" %>
</head>
<%@include file="../../commons/head.jsp" %>
<body>
<div class="container">
    <div class="row">
        <form action="${pageContext.request.contextPath}/panel/commons/createQuartz" method="post"
              id="quartzForm">
            <div class="form-group">
                <label for="spiderInfoId">爬虫模板ID</label>
                <input type="text" class="" id="spiderInfoId" name="spiderInfoId" placeholder="输入已注册的爬虫模板ID"
                       value="${spiderInfoId}">
            </div>
            <div class="form-group">
                <label for="hourInterval">循环间隔小时数</label>
                <input type="number" class="" id="hourInterval" name="hourInterval" placeholder="输入循环间隔小时数">
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-secondary">提交</button>
            </div>
        </form>
    </div>
</div>
<%@include file="../../commons/allScript.jsp" %>
<script>
    $().ready(function () {
        $("#quartzForm").validate({
            rules: {
                spiderInfoId: {
                    required: true,
                    remote: "${pageContext.request.contextPath}/commons/spider/checkQuartzJob"
                },
                hourInterval: {
                    required: true
                }
            },
            highlight: function (element) {
                $(element).closest('.form-group').addClass('has-error');
            },
            success: function (label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },
            errorPlacement: function (error, element) {
                element.parent('div').append(error);
            }
        });
    });
</script>
</body>

</html>