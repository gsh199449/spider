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
people
<c:forEach items="${relatedPeople}" var="bucket">
    ${bucket.key} -> ${bucket.docCount} <br>
</c:forEach>
relatedLocation
<c:forEach items="${relatedLocation}" var="bucket">
    ${bucket.key} -> ${bucket.docCount}<br>
</c:forEach>
relatedInstitution
<c:forEach items="${relatedInstitution}" var="bucket">
    ${bucket.key} -> ${bucket.docCount}<br>
</c:forEach>
relatedKeywords
<c:forEach items="${relatedKeywords}" var="bucket">
    ${bucket.key} -> ${bucket.docCount}<br>
</c:forEach>

</body>
</html>
