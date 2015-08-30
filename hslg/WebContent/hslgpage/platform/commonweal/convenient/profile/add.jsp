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
<title>添加便民信息</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/admin/editor/kindeditor.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	//[@flash_message /]
	
	// 表单验证
	$inputForm.validate({
		rules: {
			NAME: {
				required: true
			},
			TYPE_ID: {
				required: true
			},
			LINK_NAME: "required",
			LINK_TEL: "required"
		}
	});
	
	KindEditor.ready(function(K) {
		$("input[id^='uploadButton_']").each(function(i,v){
			var obj = this;
			var index=i;
			var uploadbutton = K.uploadbutton({
				button : obj,
				fieldName : 'file',
				url : framework.base + "/upload/file/upload.do",
				afterUpload : function(data) {
					var m_type =data.message.type;
					var m_content =data.message.content;
					if (m_type =="success") {
						var url = K.formatUrl(data.url, 'absolute');
						K('#ICON_URL').val(url);
						$("#sumbImg").attr("src",url);
					} else {
						$.message(m_type,m_content);
					}
				},
				afterError : function(str) {
					$.message("error","上传图片出错:"+ str);
				}
			});
			uploadbutton.fileBox.change(function(e) {
				uploadbutton.submit();
			});
		});
	});

});
</script>
</head>
<body>
	<div class="path">
		公益管理 &raquo; 添加便民信息
	</div>
	<form id="inputForm" action="save.do" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>标题:
				</th>
				<td>
					<input type="text" id="NAME" name="NAME" class="text" maxlength="200"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>类型:
				</th>
				<td>
					<select id="TYPE_ID" name="TYPE_ID" style="width:190px;" maxlength="200" >
						<option value="" selected>请选择...</option>
						<option value="1" >开锁服务</option>
						<option value="2" >快递</option>
						<option value="3" >家电维修</option>
						<option value="4" >家政保洁</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>Logo 图片:
				</th>
				<td>
					<input type="text" id="ICON_URL" name="ICON_URL" class="text" maxlength="200" readonly />
					<input type="button" id="uploadButton_0" value="上传" />
					<img src="" style="width:200px;height:200px;" id="sumbImg"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>联系人姓名:
				</th>
				<td>
					<input type="text" id="LINK_NAME" name="LINK_NAME" class="text" maxlength="200"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>联系人电话:
				</th>
				<td>
					<input type="text" id="LINK_TEL" name="LINK_TEL" class="text" maxlength="200"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>地址:
				</th>
				<td>
					<input type="text" id="ADDRESS" name="ADDRESS" class="text" maxlength="200"/>
				</td>
			</tr>
			<!--
			<tr>
				<th>
					广告详情:
				</th>
				<td>
					<textarea id="editor" name="CONTENT" class="editor"></textarea>
				</td>
			</tr>
			-->
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="<cc:message key="admin.common.submit" />" />
					<input type="button" id="backButton" class="button" value="<cc:message key="admin.common.back" />" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>