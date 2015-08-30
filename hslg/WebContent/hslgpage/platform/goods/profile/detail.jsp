<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/cctaglib" prefix="cc"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>商品图文信息</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/admin/editor/kindeditor.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script src="<%=basePath%>/res/js/web.js"></script>
<script src="<%=basePath%>/res/js/Http.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $submitBtn = $("#submitBtn");
	$submitBtn.click( function() {
		var isValid =checkForm();
		if(isValid)
		{
			submit();
		}
		return false;
	});
	//[@flash_message /]
	var ID ="";
	var REMARK ="";
	function checkForm()
	{
		ID =$("#ID").val();
		if(typeof ID == "undefined" || ID == "")
		{
			$.message("error","ID 不能为空");
			return false;
		}
		return true;
	}

	function submit()
	{
		//alert('submit');
		/**/
		var ID =$("#ID").val();
		REMARK =$("#editor").val();
		if(typeof REMARK == "undefined" || REMARK == "")
		{
			REMARK ="";
		}
		//alert(REMARK);
		var params={ID:ID,DETAIL: REMARK}
		$.ajax({
				url: "<%=basePath%>hslgpage/platform/goods/profile/saveDetail.do",
				type: "POST",
				data: params,
				dataType: "json",
				cache: false,
				beforeSend: function (XMLHttpRequest){
					//alert('.....');
				},
				success: function(ovo, textStatus) {
					var code =ovo.code;
					if(code >=0)
					{
						$.message("success","保存成功");
					}
					else
					{
						$.message("error",ovo.msg);
					}
				},
				complete: function (XMLHttpRequest, textStatus){
					//alert("complete...");
				},
				error: function (){
					alert('error...');
				}
			});
			
		//window.parent.enableTab("detail","detail.jsp?id=1");
	}
	
});
</script>
</head>
<body >
	<form id="inputForm" action="" method="post">
	<input type="hidden" id="ID" name="ID" class="text" maxlength="200" value="${id}"/>
		<table class="input">
			<tr>
				<th>
					商品缩略图:
				</th>
				<td>
					<input type="button" name="uploadBtn" id="uploadBtn" class="button" value="点击上传" onclick="upload()"/>
				</td>
			</tr>
			<c:forEach items="${dataset}" var="row" varStatus="status">
				<tr>
				<th>
				</th>
				<td>
					<img src="${row.PATH}" style="width:150px;height:150px;" />
				</td>
				</tr>
			</c:forEach>
			<tr>
				<th>
					商品图文详情:
				</th>
				<td>
					<textarea id="editor"  name="DETAIL" class="editor">${detail }</textarea>
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="button" id="submitBtn"  class="button" value="保存"/>
					<input type="button" id="backBtn" class="button" value="返回" onclick="javascript:window.parent.location.href='list.do'"/>
				</td>
			</tr>
		</table>
	</form>
</body>
<script type="text/javascript">
var id =request("id");
var deal_type ="goods_icon";
var type ="goods_icon";
var sub_type ="goods_icon";
var cover ="1";
var file_type ="*.jpg;*.png;*.gif";
var callback ="confirm";
function upload()
{
	if(typeof id =="undefined" || id== "")
	{
		alert("未选择商品");
		return false;
	}
	var url ="<%=basePath%>sysupload/Upload.jsp";
	url+="?deal_type="+deal_type;
	url+="&deal_code="+id;
	url+="&type="+type;
	url+="&sub_type="+sub_type;
	url+="&cover="+cover;
	url+="&file_type="+file_type;
	url+="&callback="+callback;
	window.open (url,'uploadwindow','height=400,width=680,top=200,left=200 ,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
}

//复制版本文件到下载目录
function confirm()
{
	window.location.reload();
}
</script>
</html>