<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/cctaglib" prefix="cc"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String imgBasePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>用户收货地址列表</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

	${flash_message}
});
</script>
</head>
<body>
	<div class="path">
		用户管理 &raquo;用户收货地址列表
		<span><cc:message key="admin.page.total" args="${page.total}"/></span>
	</div>
	<form id="listForm" action="list.do" method="get">
		<div class="bar">
			<!-- 
			<a href="add.do" class="iconButton">
				<span class="addIcon">&nbsp;</span><cc:message key="admin.common.add" />
			</a>
			-->
			<div class="buttonWrap">
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span><cc:message key="admin.common.delete" />
				</a>
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span><cc:message key="admin.common.refresh" />
				</a>
				<div class="menuWrap">
					<a href="javascript:;" id="pageSizeSelect" class="button">
						<cc:message key="admin.page.pageSize" /><span class="arrow">&nbsp;</span>
					</a>
					<div class="popupMenu">
						<ul id="pageSizeOption">
							<li>
								<a href="javascript:;" <c:if test="${page.pageSize == 10}">class="current"</c:if> val="10" >10</a>
							</li>
							<li>
								<a href="javascript:;" <c:if test="${page.pageSize == 20}">class="current"</c:if> val="20">20</a>
							</li>
							<li>
								<a href="javascript:;" <c:if test="${page.pageSize == 50}"> class="current"</c:if>val="50">50</a>
							</li>
							<li>
								<a href="javascript:;"  <c:if test="${page.pageSize == 100}">class="current"</c:if>val="100">100</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="menuWrap">
				<div class="search">
					<span id="searchPropertySelect" class="arrow">&nbsp;</span>
					<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				<div class="popupMenu">
					<ul id="searchPropertyOption">
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'USERNAME'}"> class="current"</c:if> val="USERNAME">用户名</a>
						</li>
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'TELEPHONE'}">class="current"</c:if> val="TELEPHONE">手机号</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="USERNAME">用户名</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="TELEPHONE">用户手机号</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="ADDRESS">地址</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="IS_DEFAULT">是否是默认地址</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="ZIP_CODE">邮编</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="TAG">标签</a>
				</th>
				<th>
					<span><cc:message key="admin.common.handle" /></span>
				</th>
			</tr>
			<c:forEach items="${page.content}" var="row" varStatus="status">
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${row.ID}" />
					</td>
					<td>
						${row.USERNAME}
					</td>
					<td>
						${row.TELEPHONE}
					</td>
					<td>
						<span title="${row.ADDRESS}">${row.ADDRESS}</span>
					</td>
					<td>
						<c:choose>
							<c:when test="${row.IS_DEFAULT ==0}">
								否
							</c:when>
							<c:when test="${row.IS_DEFAULT ==1}">
								是
							</c:when>
							<c:otherwise>
								--
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						${row.ZIP_CODE}
					</td>
					<td>
						${row.TAG}
					</td>
					<td>
					</td>
				</tr>
			</c:forEach>
		</table>
		<%@ include file="/include/pagination.jsp" %> 
	</form>
</body>
</html>