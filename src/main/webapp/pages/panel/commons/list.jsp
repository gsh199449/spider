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
    <title>资讯列表</title>
    <%@include file="../../commons/header.jsp" %>
    <script type="text/javascript">
        $(function () {
            var validate = $("#webpageForm").validate({
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
        function showDetail(id) {
            rpc('${pageContext.request.contextPath}/commons/webpage/getWebpageById', {id: id}, function (data) {
                $("#modalTitle").text(data.result.title);
                var modalBody = $("#modalBody");
                modalBody.html('');
                modalBody.append("<h4>正文</h4>");
                modalBody.append('<p>' + data.result.content + '</p>');
                modalBody.append("<h4>关键词</h4>");
                if (data.result.keywords != undefined) {
                    $.each(data.result.keywords, function (i, word) {
                        modalBody.append(word + ' ,');
                    });
                }
                modalBody.append("<h4>摘要</h4>");
                if (data.result.summary != undefined) {
                    modalBody.append('<p>' + data.result.summary.join(' ,') + '</p>');
                }
                modalBody.append("<h4>发布时间</h4>");
                modalBody.append('<span>' + data.result.publishTime + '</span>');
                modalBody.append("<h4>动态字段</h4>");
                if (data.result.dynamicFields != undefined) {
                    $.each(data.result.dynamicFields, function (k, v) {
                        modalBody.append("<p>" + k + " : " + v + "</p>");
                    });
                }
                $('#myModal').modal('show');
            });
        }
    </script>
</head>
<body>
<%@include file="../../commons/head.jsp" %>
<div class="container">
    <form class="form-inline" id="webpageForm" action="${pageContext.request.contextPath}/panel/commons/list">
        <div class="form-group">
            <label for="query">关键词:</label>
            <input class="form-control" id="query" name="query" value="${query}">
        </div>
        <div class="form-group">
            <label for="page">页码:</label>
            <input class="form-control" type="number" id="page" name="page" value="${page}">
        </div>
        <div class="form-group">
            <label for="domain">域名:</label>
            <input class="form-control" id="domain" name="domain" value="${domain}">
        </div>
        <button type="submit" class="btn btn-primary" id="priceSubmit">搜索</button>
    </form>
</div>
<div class="container">
    <table class="table table-hover">
        <thead class="thead-inverse">
        <tr>
            <th>#</th>
            <th>标题</th>
            <th>网站</th>
            <th>时间</th>
            <th>查看</th>
            <th>转到</th>
            <th>删除</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${resultBundle}" var="webpage" varStatus="index">
            <tr>
                <th scope="row">${index.count}</th>
                <td>${webpage.title}</td>
                <td>${webpage.domain}</td>
                <td><fmt:formatDate value="${webpage.gathertime}" pattern="yyyy/MM/dd HH:mm:ss"/></td>
                <td>
                    <button onclick="showDetail('${webpage.id}')" class="btn btn-info">Show</button>
                </td>
                <td>
                    <a href="${webpage.url}" class="btn btn-primary" target="_blank">Go</a>
                </td>
                <td>
                    <button onclick="rpcAndShowData('${pageContext.request.contextPath}/commons/webpage/deleteById',{id:'${webpage.id}'})"
                            class="btn btn-danger">
                        删除
                    </button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
