<%--
  modified by Hokis
  2017-5-18 20:07:59
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${tablePage ne null}"> 
	<!-- 表格底部页码 -->
	<div class="box">
		<div class="col-sm-12">
			<div class="pull-left">
				<div class="" id="">
					&nbsp;&nbsp;共 ${tablePage.totalRow }
					条记录/共
					${tablePage.pageCount }
					页
				</div>
			</div>
			<div class="text-center">
				<div class="" id="">
					<ul class="pagination">
						<c:if test="${tablePage.currentPage ne 1 }">
							<li class=""><a
								href="?page=1${tablePage.otherParam }">首页</a></li>
							<c:if test="${tablePage.pageCount ge 3 }">
								<li class=""><a
									href="?page=${tablePage.currentPage-1 }${tablePage.otherParam }"><span
										aria-hidden="true">&laquo;</span></a></li>
							</c:if>
							<c:if test="${tablePage.pageCount le (3-1) }">
								<li class="disabled"><a href="#"><span
										aria-hidden="true">&laquo;</span></a></li>
							</c:if>
						</c:if>
						<c:if test="${tablePage.currentPage eq 1 }">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#"><span aria-hidden="true">&laquo;</span></a></li>
						</c:if>
						
						<c:forEach begin="${tablePage.pageRange[0] }" end="${tablePage.pageRange[1] }" step="1" varStatus="numIndex" >
							<c:if test="${tablePage.currentPage eq numIndex.index }">
								<li class="active"><a
									href="?page=${numIndex.index }${tablePage.otherParam }">${numIndex.index }</a></li>
							</c:if>
							<c:if test="${tablePage.currentPage ne numIndex.index }">
								<li><a
									href="?page=${numIndex.index }${tablePage.otherParam }">${numIndex.index }</a></li>
							</c:if>
						</c:forEach>
						
						<c:if test="${tablePage.currentPage ne tablePage.pageCount }">
						
							<c:if test="${tablePage.pageCount ge 3 }">
								<li><a
									href="?page=${tablePage.currentPage + 1 }${tablePage.otherParam }"><span
										aria-hidden="true">&raquo;</span></a></li>
							</c:if>
							<c:if test="${tablePage.pageCount le (3-1) }">
								<li class="disabled"><a href="#"><span
										aria-hidden="true">&raquo;</span></a></li>
							</c:if>
							<li><a
								href="?page=${tablePage.pageCount }${tablePage.otherParam }">末页</a></li>
						</c:if>
						<c:if test="${tablePage.currentPage eq tablePage.pageCount }">
							<li class="disabled"><a href="#"><span aria-hidden="true">&raquo;</span></a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</c:if>
					</ul>
				</div>
			</div>
			<div class="clearfix"></div>
		</div>
	</div>
	<!--end 表格底部信息 -->
</c:if>
