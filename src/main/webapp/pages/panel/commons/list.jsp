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
    <%@include file="../../commons/allScript.jsp" %>
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
        
        function clearForm(){
			$("#query").val(null);
			$("#domain").val(null);
		}
    </script>
    <style type="text/css">
        .divide {
            height: 5px;
            width: 100%;
        }
    </style>

</head>
<body>
<%@include file="../../commons/head.jsp" %>

<div class="col-md-10 col-md-offset-1">
    <div class="divide"></div>
    <div class="container">
	    <form class="form-inline" id="webpageForm" action="${pageContext.request.contextPath}/panel/commons/list">
	    	<div class="col-md-5">
		        <div class="form-group">
		            <label for="query">关键词:</label>
		            <input class="form-control" id="query" name="query" value="${param.query}">
		        </div>
	    	</div>
	    	<div class="col-md-5">
		        <div class="form-group">
		            <label for="domain">域名:</label><span style="color: red;">(*支持模糊)</span>
		            <input class="form-control" id="domain" name="domain" value="${param.domain}">
		        </div>
	    	</div>
	    	<div class="col-md-2">
		        <button type="submit" class="btn btn-primary" id="priceSubmit">搜索</button>
		        &nbsp;
		        <a href="javascript:void(0);" class="btn btn-danger" onclick="clearForm();">重置</a>
	    	</div>
	    </form>
	</div>
    <div class="divide"></div>
	<div class="container">
	    <div class="row">
	        <table class="table table-border table-hover">
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
	            	<c:if test="${resultBundle ne null}">
                        <c:forEach items="${resultBundle}" var="webpage" varStatus="wpIndex">
	            			<tr>
			                    <th scope="row">${wpIndex.count}</th>
			                    <td>${webpage.title}</td>
			                    <td>${webpage.domain}</td>
			                    <td><fmt:formatDate value="${webpage.gathertime}" pattern="yyyy/MM/dd HH:mm:ss"/></td>
			                    <td>
			                        <button onclick="showDetail('${webpage.id}')" class="btn btn-info">Show</button>
			                    </td>
			                    <td>
			                        <a href="${pageContext.request.contextPath}/panel/commons/showWebpageById?id=${webpage.id}"
			                           class="btn btn-primary" target="_blank">Go</a>
			                    </td>
			                    <td>
			                        <button onclick="rpcAndShowData('${pageContext.request.contextPath}/commons/webpage/deleteById',{id:'${webpage.id}'})"
			                                class="btn btn-danger"> 删除</button>
			                    </td>
			                </tr>
	            		</c:forEach> 
	            	</c:if>
	            	<c:if test="${resultBundle eq null}">
	            		<tr>
	                		<th colspan="7"><div class="text-center"><h3>无数据！</h3></div></th>
	                	</tr>
	            	</c:if>
	            </tbody>
	        </table>
	    </div>
	    
	    <!-- 底部页码 -->
	    <%@include file="../../commons/tablePage.jsp" %>
	</div>

</div>
</body>
</html>
