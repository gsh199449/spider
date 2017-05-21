<%--
  Created by IntelliJ IDEA.
  User: gaoshen
  Date: 16/5/2
  Time: 下午3:46
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>抓取任务列表</title>
    <%@include file="/pages/commons/header.jsp" %>
    <%@include file="../../commons/allScript.jsp" %>
    <script>
        function showTable(taskId) {
            rpc('${pageContext.request.contextPath}/commons/spider/getTaskById', {
                taskId: taskId,
                containsExtraInfo: false
            }, function (data) {
                $("#taskListTableBody").html("");
                $.each(data.result.descriptions, function (k, v) {
                    $('<tr style="word-break:break-all; word-wrap:break-word;"><th scope="row">' + k + '</th><td>' + v + '</td></tr>')
                            .appendTo("#taskListTableBody");
                });
                $('#taskListModal').modal('show');
            });
        }
    </script>
</head>
<body>
<%@include file="/pages/commons/head.jsp" %>
<div id="taskListModal" class="modal fade" tabindex="-1" role="dialog" style="overflow:scroll">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title">任务状态列表</h4>
            </div>
            <div class="modal-body">
                <table class="table">
                    <thead class="thead-inverse">
                    <tr>
                        <th>时间</th>
                        <th>状态</th>
                    </tr>
                    </thead>
                    <tbody id="taskListTableBody">
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<div class="container">
    <button onclick="rpcAndShowData('${pageContext.request.contextPath}/commons/spider/deleteAll')"
            class="btn btn-danger">删除全部已停止爬虫
    </button>
    <div class="alert alert-success" role="alert">
        <strong>抓取任务数:</strong>${resultBundle.count}
        <strong>运行任务数:</strong>${runningTaskCount}
    </div>
    <table class="table table-hover">
        <thead class="thead-inverse">
        <tr>
            <%--<th>任务ID</th>--%>
            <th>#</th>
            <th>任务名称</th>
            <th>已抓取数量</th>
            <th>抓取状态</th>
            <th>抓取状态列表</th>
            <th>查看详情</th>
            <th>编辑模板</th>
            <th>查看数据</th>
            <th>停止</th>
            <th>删除</th>
            <th>导出数据</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${resultBundle.resultList}" var="task" varStatus="index">
            <tr>
                    <%--<th scope="row">${task.taskId}</th>--%>
                <th scope="row">${index.count}</th>
                <td>${task.name}</td>
                <td>${task.count}</td>
                <td>${task.state}</td>
                <td>
                    <button onclick="showTable('${task.taskId}')" class="btn btn-info">查看状态</button>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/commons/spider/getTaskById?taskId=${task.taskId}"
                       class="btn btn-warning" target="_blank">查看详情</a>
                </td>
                <td>
                    <c:if test="${spiderInfoList.get(index.index) != \"null\"}">
                        <form action="${pageContext.request.contextPath}/panel/commons/editSpiderInfo" method="post"
                              target="_blank">
                            <input hidden name="jsonSpiderInfo" value="${spiderInfoList.get(index.index)}">
                            <button class="btn btn-primary" type="submit">编辑模板</button>
                        </form>
                    </c:if>
                    <c:if test="${spiderInfoList.get(index.index) == \"null\"}">
                        <button class="btn btn-primary disabled" type="submit">编辑模板</button>
                    </c:if>
                </td>
                <td>
                    <c:if test="${spiderInfoList.get(index.index) == \"null\"}">
                        <a class="btn btn-secondary disabled" target="_blank">查看数据</a>
                    </c:if>
                    <c:if test="${spiderInfoList.get(index.index) != \"null\"}">
                        <a href="${pageContext.request.contextPath}/panel/commons/list?domain=${task.name}"
                           class="btn btn-secondary" target="_blank">查看数据</a>
                    </c:if>

                </td>
                <td>
                    <c:if test="${task.state == 'RUNNING'}">
                        <button onclick="rpcAndShowData('${pageContext.request.contextPath}/commons/spider/stop',{uuid:'${task.taskId}'})"
                                class="btn btn-danger">停止
                        </button>
                    </c:if>
                    <c:if test="${task.state == 'STOP'}">
                        <button onclick="rpcAndShowData('${pageContext.request.contextPath}/commons/spider/stop',{uuid:'${task.taskId}'})"
                                class="btn btn-danger" disabled>停止
                        </button>
                    </c:if>
                </td>
                <td>
                    <c:if test="${task.state == 'RUNNING'}"><a class="btn btn-danger disabled">正在抓取</a></c:if>
                    <c:if test="${task.state == 'STOP'}">
                        <button onclick="rpcAndShowData('${pageContext.request.contextPath}/commons/spider/delete',{uuid:'${task.taskId}'})"
                                class="btn btn-danger" >删除
                        </button>
                    </c:if>
                </td>
                <td>
                    <c:if test="${spiderInfoList.get(index.index) == \"null\"}">
                        <div class="btn-group">
                            <button type="button" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                导出数据数据
                            </button>
                            <div class="dropdown-menu">
                                <a class="dropdown-item">下载标题正文对</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item">下载JSON</a>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${spiderInfoList.get(index.index) != \"null\"}">
                        <div class="btn-group">
                            <button type="button" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                导出数据数据
                            </button>
                            <div class="dropdown-menu">
                                <a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/commons/webpage/exportTitleContentPairBySpiderUUID?uuid=${task.taskId}">下载标题正文对</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/commons/webpage/exportWebpageJSONBySpiderUUID?uuid=${task.taskId}">下载JSON</a>
                            </div>
                        </div>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>

