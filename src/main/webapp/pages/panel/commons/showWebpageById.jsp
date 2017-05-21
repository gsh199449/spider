<%--
  Created by IntelliJ IDEA.
  User: gsh199449
  Date: 2016/12/26
  Time: 下午8:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>${webpage.title}</title>
    <%@include file="/pages/commons/header.jsp" %>
</head>
<body>
<%@include file="/pages/commons/head.jsp" %>
<div class="container">
    <div class="row">
        <div class="col-sm-9">
            <h2 class="display-1">${webpage.title}</h2>
            <p>
                ${webpage.content}
            </p>
            <h3>网页元信息</h3>
            <table class="table table-hover">
                <thead>
                <tr>
                    <th style="width: 20%">属性名称</th>
                    <th style="width: 80%">属性值</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>关键词</td>
                    <td>
                        <c:forEach items="${webpage.keywords}" var="word">
                            <span class="label label-primary">${word}</span>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td>类别</td>
                    <td>${webpage.category}</td>
                </tr>
                <tr>
                    <td>摘要</td>
                    <td style="word-break:break-all; word-wrap:break-word;">
                        <c:forEach items="${webpage.summary}" var="sentence">
                            ${sentence},
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td>人名</td>
                    <td style="word-break:break-all; word-wrap:break-word;">
                        <c:forEach items="${webpage.namedEntity.get('nr')}" var="word">
                            <a class="btn btn-info btn-sm" style="margin: 1px"
                               href="${pageContext.request.contextPath}/panel/commons/showRelatedInfo?query=namedEntity.nr:${word}">${word}</a>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td>地名</td>
                    <td style="word-break:break-all; word-wrap:break-word;">
                        <c:forEach items="${webpage.namedEntity.get('ns')}" var="word">
                            <a class="btn btn-info btn-sm" style="margin: 1px"
                               href="${pageContext.request.contextPath}/panel/commons/showRelatedInfo?query=namedEntity.ns:${word}">${word}</a>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td>机构名</td>
                    <td style="word-break:break-all; word-wrap:break-word;">
                        <c:forEach items="${webpage.namedEntity.get('nt')}" var="word">
                            <a class="btn btn-info btn-sm" style="margin: 1px"
                               href="${pageContext.request.contextPath}/panel/commons/showRelatedInfo?query=namedEntity.nt:${word}">${word}</a>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td>发布时间</td>
                    <td>
                        <fmt:formatDate value="${webpage.publishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                </tr>
                <tr>
                    <td>采集时间</td>
                    <td>
                        <fmt:formatDate value="${webpage.gathertime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                </tr>
                <tr>
                    <td>网页处理耗时</td>
                    <td>
                        ${webpage.processTime/(1000.0)}秒
                    </td>
                </tr>
                <tr>
                    <td>采集模板</td>
                    <td>
                        <a class="btn btn-info" target="_blank"
                           href="${pageContext.request.contextPath}/panel/commons/editSpiderInfoById?spiderInfoId=${webpage.spiderInfoId}">查看爬虫模板</a>
                    </td>
                </tr>
                <tr>
                    <td>原网站</td>
                    <td>
                        <a class="btn btn-info" target="_blank"
                           href="${pageContext.request.contextPath}/panel/commons/list?domain=${webpage.domain}">查看${webpage.domain}的新闻</a>
                    </td>
                </tr>
                </tbody>
            </table>
            <h3>动态字段</h3>
            <table class="table table-reflow">
                <thead>
                <tr>
                    <c:forEach items="${webpage.dynamicFields}" var="entry">
                        <th>${entry.key}</th>
                    </c:forEach>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <c:forEach items="${webpage.dynamicFields}" var="entry">
                        <td>${entry.value}</td>
                    </c:forEach>
                </tr>
                </tbody>
            </table>
            <p>
                <a href="${webpage.url}" target="_blank">阅读原文</a>
            </p>
        </div>
        <div class="col-sm-3">
            <h3>相关资讯</h3>
            <ul class="list-group">
                <c:forEach items="${relatedWebpageList}" var="relatedWebpage">
                    <li class="list-group-item">
                        <a href="${pageContext.request.contextPath}/panel/commons/showWebpageById?id=${relatedWebpage.id}">${relatedWebpage.title}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
<%@include file="../../commons/minScript.jsp" %>
</body>
</html>
