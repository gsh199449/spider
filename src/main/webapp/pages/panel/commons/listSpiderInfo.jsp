<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: gaoshen
  Date: 16/4/27
  Time: 下午5:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>爬虫模板列表</title>
    <%@include file="../../commons/header.jsp" %>
    <%@include file="../../commons/allScript.jsp" %>
    <script type="text/javascript">
        $(function () {
            var validate = $("#spiderInfoForm").validate({
                rules: {
                    page: {
                        required: true,
                        number: true
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
        function checkAll() {
            $('input:checkbox').each(function () {
                $(this).attr('checked', true);
            });
        }
        function startAll() {
            var idList = [];
            $("input:checkbox:checked").each(function () {
                idList.push($(this).attr('data-infoid'));
            });
            rpcAndShowData('${pageContext.request.contextPath}/commons/spider/startAll', {spiderInfoIdList: idList.join(',')});
        }
    </script>
</head>
<body>
<%@include file="../../commons/head.jsp" %>
<div class="container">
    <form class="form-inline" id="spiderInfoForm"
          action="${pageContext.request.contextPath}/panel/commons/listSpiderInfo">
        <div class="form-group">
            <label for="page">页码:</label>
            <input class="form-control" type="number" id="page" name="page" value="${page}">
        </div>
        <div class="form-group">
            <label for="domain">域名:</label>
            <input class="form-control" id="domain" name="domain" value="${domain}">
        </div>
        <button type="submit" class="btn btn-primary">搜索</button>
    </form>
</div>
<div class="container">
    <div class="row">
        <button type="button" onclick="startAll()">启动选中</button>
        <button type="button" onclick="checkAll()">全选</button>
    </div>
    <table class="table table-hover">
        <thead class="thead-inverse">
        <tr>
            <th>#</th>
            <th>网站域名</th>
            <th>网站名称</th>
            <%--<th>查看数据</th>--%>
            <th>编辑</th>
            <th>删除</th>
            <th>定时任务</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${spiderInfoList}" var="info" varStatus="index">
            <tr>
                <th><label>
                    <input type="checkbox" data-infoid="${info.id}">${index.count}
                </label></th>
                <td>${info.domain}</td>
                <td>${info.siteName}</td>
                    <%--<td></td>--%>
                <td>
                    <a class="btn btn-info"
                       href="${pageContext.request.contextPath}/panel/commons/editSpiderInfoById?spiderInfoId=${info.id}">编辑</a>
                </td>
                <td>
                    <button onclick="rpcAndShowData('${pageContext.request.contextPath}/commons/spiderinfo/deleteById',{id:'${info.id}'})"
                            class="btn btn-danger">
                        删除
                    </button>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/panel/commons/createQuartz?spiderInfoId=${info.id}"
                       class="btn btn-secondary">创建定时任务</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
