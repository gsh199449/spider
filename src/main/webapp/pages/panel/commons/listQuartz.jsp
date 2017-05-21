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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
<head>
    <title>定时网页抓取任务列表</title>
    <%@include file="../../commons/header.jsp" %>
</head>

<body>
<%@include file="../../commons/head.jsp" %>
<div class="container">
    <div class="row">
        <table>
            <thead>
            <tr>
                <th>网站名称</th>
                <th>上次执行时间</th>
                <th>下次执行时间</th>
                <th>创建时间</th>
                <th>删除任务</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${list}" var="entry">
                <tr>
                    <td>${entry.value.left.siteName}</td>
                    <td><fmt:formatDate value="${entry.value.right.previousFireTime}"
                                        pattern="yyyy/MM/dd HH:mm:ss"/></td>
                    <td><fmt:formatDate value="${entry.value.right.nextFireTime}"
                                        pattern="yyyy/MM/dd HH:mm:ss"/></td>
                    <td><fmt:formatDate value="${entry.value.right.startTime}"
                                        pattern="yyyy/MM/dd HH:mm:ss"/></td>
                    <td>
                        <button onclick="deleteQuartzJob('${entry.key}')" class="am-btn am-btn-default">删除定时任务
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@include file="../../commons/minScript.jsp" %>
<script>
    function deleteQuartzJob(spiderInfoId) {
        var cc = confirm("是否要删除定时任务");
        if (cc) {
            $.get('${pageContext.request.contextPath}/commons/spider/removeQuartzJob', {spiderInfoId: spiderInfoId}, function () {
                alert("删除定时任务成功");
            });
        }
    }
</script>
</body>

</html>