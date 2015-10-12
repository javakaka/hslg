<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/cctaglib" prefix="cc"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
 <head>
  <meta charset="utf-8" />
  <title>${row.TITLE}</title>
  <meta name="description" content="${row.TITLE}" />
  <meta name="keywords" content="${row.TITLE}" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
  <meta content="telephone=no" name="format-detection">
  <meta name="apple-mobile-web-app-capable" content="${row.TITLE}">
  <meta name="apple-mobile-web-app-title" content="${row.TITLE}" />
  <meta name="apple-mobile-web-app-capable" content="yes" />
  <meta content="telephone=no" name="format-detection" />
  <meta name="full-screen" content="yes" />
  <meta name="x5-fullscreen" content="true" /> 
  <link rel="Stylesheet" href="<%=basePath%>hslgpage/app/res/css/main.css" />
 </head>
 <body >
  <div id="main" class="wrapper white " style="text-indent: 20px;">
	${row.CONTENT}
  </div>
  <script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
  <script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
 </body>
 <script>
 	$(function(){
 		
 	});
 </script>
</html>