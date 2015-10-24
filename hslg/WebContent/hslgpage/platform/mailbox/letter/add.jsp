<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/cctaglib" prefix="cc"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String contextBasePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>添加用户消息</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/admin/editor/kindeditor.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<link href="<%=basePath%>/res/css/diymen/tipswindown.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/css/diymen/tipswindown.js?version=1.4"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	//[@flash_message /]
	
	// 表单验证
	$inputForm.validate({
		rules: {
			TITLE: {
				required: true,
			},
		TITLE: "required",
		AUDIT_STATUS: "required",
		SUMMARY: "required",
		CONTENT: "required"
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
						$('#iconVeiw').attr("src",url);
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
<script type="text/javascript">
var title;
var url;
var width=600;
var height=350;
var drag="true";
var time="";
var showBg="true";
var cssName="leotheme";
var iframeName="selectIframeId";

function popWindow(title,url,width,height,drag,time,showBg,cssName,iframeName)
{
	tipsWindown(title,url,width,height,drag,time,showBg,cssName,iframeName);
}

function closeTipWindow()
{
	tipsWindown.close();
}

function selectUserIcon(){
	var id=$("#TO_ID").val();
	if(typeof id == "undefined" || id == "" )
	{
		id ="";
	}
	title ="选择收件人";
	url="iframe:<%=basePath%>hslgpage/platform/member/profile/SelectUser.do?id="+id;
	width=620;
	iframeName="selectUserIframeId";
	popWindow(title,url,width,height,drag,time,showBg,cssName,iframeName);
}

function setSelectedUser(id,name)
{
	$("#TO_ID").val(id);
	$("#TO_NAME").html(name);
}

function userIframeSelectTarget()
{
 	window.frames["selectUserIframeId"].selectTarget();
}

</script>
</head>
<body>
	<div class="path">
		系统管理管理 &raquo; 添加用户消息
	</div>
	<form id="inputForm" action="save.do" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>请选择收件人:
				</th>
				<td>
					<input type="hidden" id="TO_ID"name="TO_ID" class="text" maxlength="200" value="" />
					<span id="TO_NAME" name="TO_NAME" class="text" maxlength="200"></span>
					<img id="select_type_ico_id" name="select_type_ico_id" src="<%=basePath%>res/images/select_window.gif" onclick="selectUserIcon();" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>标题:
				</th>
				<td>
					<input type="text" id="TITLE" name="TITLE" class="text" maxlength="200" value=""/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>简介:
				</th>
				<td>
					<textarea type="text" id="SUMMARY" name="SUMMARY"  rows="2" cols="50" ></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>审核状态:
				</th>
				<td>
					<select id="AUDIT_STATUS" name="AUDIT_STATUS" style="width:190px;">
						<option value="" selected>请选择...</option>
						<option value="1" >待审核</option>
						<option value="2">审核通过</option>
						<option value="0">审核不通过</option>
						</select>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>阅读状态:
				</th>
				<td>
				<select id="READ_STATUS" name="READ_STATUS" style="width:190px;">
						<option value="" selected>请选择...</option>
						<option value="1" >已读</option>
						<option value="2" >未读</option>
				</select>
				</td>
			</tr>
			<tr>
				<th>
					消息详情:
				</th>
				<td>
					<textarea id="editor" name="CONTENT" class="editor"></textarea>
				</td>
			</tr>
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