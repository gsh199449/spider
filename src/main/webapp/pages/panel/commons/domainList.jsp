<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: gaoshen
  Date: 16/5/5
  Time: 下午6:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>网站列表</title>
    <%@include file="../../commons/header.jsp" %>
</head>
<body>
<%@include file="../../commons/head.jsp" %>
<div class="container">
    <table class="table table-hover">
        <thead>
        <tr>
            <th>#</th>
            <th>网站域名</th>
            <th>资讯数</th>
            <th>查看列表</th>
            <th>删除</th>
            <th>导出数据</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${domainList}" var="domain" varStatus="index">
            <tr>
                <th scope="row">${index.count}</th>
                <td>${domain.key}</td>
                <td>${domain.value}</td>
                <td><a class="btn btn-info"
                       href="${pageContext.request.contextPath}/panel/commons/list?domain=${domain.key}">查看资讯列表</a>
                </td>
                <td><a class="btn btn-danger"
                       onclick="rpcAndShowData('${pageContext.request.contextPath}/commons/webpage/deleteByDomain', {domain: '${domain.key}'});">删除网站数据</a>
                </td>
                <td><a class="btn btn-info"
                       href="${pageContext.request.contextPath}/commons/webpage/exportWebpageJSONByDomain?domain=${domain.key}">导出该网站数据JSON</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<div class="container" id="wordCloudBody" style="height:400px"></div>
<%@include file="../../commons/allScript.jsp" %>
</body>
</html>
