<%--
  Created by IntelliJ IDEA.
  User: gaoshen
  Date: 16/5/4
  Time: 下午3:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${appName }</title>
    <%@include file="../../commons/header.jsp" %>
</head>
<body>
	<%@include file="../../commons/head.jsp" %>
	<div class="container">
	    <div class="jumbotron">
	    	<div class="text-center">
		        <h2 class="display-2 ">欢迎来到${appName }<span class="text-danger" style="font-size: 20;"> &nbsp;&nbsp;Version:${appVersion }</span></h2>
		        <p class="lead">请选择上方导航栏中的功能</p>
	    	</div>
	    	
	        <!-- about more -->
	        <div class="col-md-12">
		        <a href="${onlineDocumentation }" class="btn btn-info pull-right" target="_blank">在线帮助</a>
	        </div>
	        <!-- end about more -->
	    </div>
	    
	    <%-- <!-- version info -->
	    <div class="col-md-12">
	    	<!-- current version -->
	    	<div class="row">
	    		<div class="alert alert-info">
	    			<h4>当前版本:<small>${appVersion }</small></h4>
	    			<ul>
	    				<li>优化..</li>
	    				<li>修复..</li>
	    			</ul>
	    		</div>
	    	</div>
	    	<!-- end current version -->
	    	
	    	<!-- history version -->
	    	<div class="row">
	    		<div class="alert alert-danger alert-dismissible">
		    		<div class="text-center"><h3>历史版本&nbsp;&nbsp;<a href="javascript:void(0);" class="btn btn-danger btn-sm" onclick="setVisible(this);">显示</a></h3></div>
		    		<div id="history-content" style="display: none;">
			    		<hr/>
		    			<div class="box">
			    			<h5>版本:<small>4.0</small></h5>
			    			<ul>
			    				<li>优化..</li>
			    				<li>修复..</li>
			    			</ul>
		    			</div>
		    			<hr/>
		    			<div class="box">
			    			<h5>版本:<small>3.0</small></h5>
			    			<ul>
			    				<li>优化..</li>
			    				<li>修复..</li>
			    			</ul>
		    			</div>
		    		</div>
	    		</div>
	    	</div>
	    	<!-- end history version -->
	    </div>
	    <!-- end version info --> --%>
	</div>
	
	<%@include file="../../commons/minScript.jsp" %>
	<%-- <script type="text/javascript">
		function setVisible(btn){
			if($("#history-content").is(":hidden")){
				$("#history-content").fadeIn(500);
				$(btn).html("隐藏");
			}else{
				$("#history-content").fadeOut(500);
				$(btn).html("显示");
			}
		}
	</script> --%>
</body>
</html>
