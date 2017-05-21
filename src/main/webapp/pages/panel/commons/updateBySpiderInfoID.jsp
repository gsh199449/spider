<%--
  Created by IntelliJ IDEA.
  User: gsh199449
  Date: 2016/10/11
  Time: 下午6:49
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>数据更新</title>
    <%@include file="../../commons/header.jsp" %>
    <%@include file="../../commons/allScript.jsp" %>
    <script type="text/javascript">
        $(function () {
            var validate = $("#updateForm").validate({
                submitHandler: function (form) {
                    $.post("${pageContext.request.contextPath}/commons/webpage/updateBySpiderinfoID", $("#updateForm").serialize(), function (json) {
                        if (json.success) {
                            showModal("任务提交成功", json.result + ",追踪编码:" + json.traceId);
                        } else {
                            showModal("任务提交失败", json.errorMsg);
                        }
                    });
                },
                rules: {
                    spiderInfoIdUpdateBy: {
                        required: true
                    },
                    spiderInfoJson: {
                        required: true
                    },
                    callbackUrl: {
                        required: false
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
</head>
<body>
<%@include file="../../commons/head.jsp" %>
<div class="container">
    <form id="updateForm">
        <div class="form-group">
            <label for="spiderInfoIdUpdateBy">spiderInfoIdUpdateBy</label>
            <input type="text" class="form-control" id="spiderInfoIdUpdateBy" name="spiderInfoIdUpdateBy"
                   placeholder="待更新的爬虫模板ID"
                   value="${spiderInfoIdUpdateBy}">
        </div>
        <fieldset class="form-group">
            <label for="spiderInfoJson">Json爬虫模板</label>
            <textarea class="form-control" id="spiderInfoJson" rows="10"
                      name="spiderInfoJson">${spiderInfoJson}</textarea>
        </fieldset>
        <div class="form-group">
            <label for="callbackUrl">回调地址</label>
            <input type="text" class="form-control" id="callbackUrl" name="callbackUrl" placeholder="回调地址">
        </div>
        <button type="submit" class="btn btn-danger">提交</button>
    </form>
</div>
</body>
</html>
