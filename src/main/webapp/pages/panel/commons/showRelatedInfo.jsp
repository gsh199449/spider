<%--
  Created by IntelliJ IDEA.
  User: gaoshen
  Date: 2016/12/27
  Time: 10:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>相关资讯</title>
    <%@include file="/pages/commons/header.jsp" %>
</head>
<body>
<%@include file="/pages/commons/head.jsp" %>
<div class="container">
    <div class="row">
        <h1 class="display-1">
            ${title}
        </h1>
    </div>
    <div class="row">
        <div class="col-sm-6">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>人物名称</th>
                    <th>提及次数</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${relatedPeople}" var="bucket">
                    <tr>
                        <td>${bucket.key} </td>
                        <td> ${bucket.docCount}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="col-sm-6">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>地点名称</th>
                    <th>提及次数</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach items="${relatedLocation}" var="bucket">
                    <tr>
                        <td>${bucket.key} </td>
                        <td> ${bucket.docCount}</td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-6">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>机构名称</th>
                    <th>提及次数</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach items="${relatedInstitution}" var="bucket">
                    <tr>
                        <td>${bucket.key} </td>
                        <td> ${bucket.docCount}</td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </div>
        <div class="col-sm-6">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>关键词</th>
                    <th>提及次数</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach items="${relatedKeywords}" var="bucket">
                    <tr>
                        <td>${bucket.key} </td>
                        <td> ${bucket.docCount}</td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </div>
    </div>
</div>
<%@include file="../../commons/minScript.jsp" %>
</body>
</html>
