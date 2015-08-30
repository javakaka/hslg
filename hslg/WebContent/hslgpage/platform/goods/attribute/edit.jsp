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
<title>编辑商品属性</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-form-to-json.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $submitBtn = $("#submitBtn");
	$submitBtn.click(function(){
		$("#inputForm").submit();
	});
	
	${flash_message};
	
	$("#inputForm").validate({ 
		  onsubmit:true,// 是否在提交是验证 
		  onfocusout:false,// 是否在获取焦点时验证 
		  onkeyup :false,// 是否在敲击键盘时验证
		  rules: 
		  { 
			    GOODS_ID:
			   {
		          required:true
			    },
			    BRAND:
			   {
		          required:true,
		          rangelength:[0,32]
			    },
			    SPECIFICATION:
			   {
		          required:true
			    },
			   PACKAGING:
			   {
		          required:true
			    },
			   ORIGIN:
			   {
		          required:true
			    },
			    EXPIRY_DATE:
			   {
		          required:true,
		          number: true,
		          min: 0
			    },
			    STORE:
			   {
		          required:true
			    }
			  }, 
			  messages:
			  { 
			      GOODS_ID:
				  {
			        required:'商品编号不能为空'
			      },
			      BRAND:
				  {
			        required:'请输入商品品牌'
			      },
			      SPECIFICATION:
				  {
			        required:'请输入商品规格',
			      },
				  PACKAGING:
				  {
			        required:'请输入商品包装'
			      },
			      ORIGIN:
				  {
			        required:'请输入商品原产地'
			      },
				  EXPIRY_DATE:
				  {
			        required:'请选择商品保质期'
			      },
				  STORE:
				  {
			        required:'请输入商品储存方法'
			      }
			  },
			  submitHandler: function(form) {  //通过之后回调 
				  var params=$(form).serialize();
				  params=$(form).serializeArray();
				  params=$(form).serializeJson();
				  delete params["TYPE_NAME"];
					$.ajax({
							url: "<%=basePath%>hslgpage/platform/goods/attribute/save.do",
							type: "post",
							data: params,
							dataType: "json",
							cache: false,
							beforeSend: function (XMLHttpRequest){
							},
							success: function(ovo, textStatus) {
								var code =ovo.code;
								if(code >=0)
								{
									var v_id =ovo.oForm.ID;
									$("#ID").val(v_id);
									window.parent.changeTabUrl("base","edit.do?id="+v_id);
									window.parent.enableTab("detail","detail.do?id="+v_id);
									window.parent.enableTab("attribute","<%=basePath%>hslgpage/platform/goods/attribute/edit.do?goods_id="+v_id);
									$.message("success","保存成功");
								}
								else
								{
									$.message("error",ovo.msg);
								}
							},
							complete: function (XMLHttpRequest, textStatus){
							},
							error: function (){
								$.message("error","处理出错");
							}
						});
			  },
			  invalidHandler: function(form, validator) 
			  {  
				//不通过回调
			    return false;
			  }
	});
});
</script>
</head>
<body>
	<form id="inputForm" action="save.do" method="post">
	<input type="hidden" id="ID" name="ID" class="text" maxlength="200" value="${row.ID}"/>
	<input type="hidden" id="GOODS_ID" name="GOODS_ID" class="text" maxlength="200" value="${goods_id}"/>
		<table class="input">
			<tr>
				<th>
					品牌:
				</th>
				<td>
					<input type="text" id="BRAND" name="BRAND" class="text" maxlength="200" value="${row.BRAND}" />
				</td>
			</tr>
			<tr>
				<th>
					规格:
				</th>
				<td>
					<input type="text" id="SPECIFICATION" name="SPECIFICATION" class="text" maxlength="200" value="${row.SPECIFICATION}"/>
				</td>
			</tr>
			
			<tr>
				<th>
					包装:
				</th>
				<td>
					<input type="text" id="PACKAGING" name="PACKAGING" class="text" maxlength="200" value="${row.PACKAGING}"/>
				</td>
			</tr>
			<tr>
				<th>
					原产地:
				</th>
				<td>
					<input type="text" id="ORIGIN" name="ORIGIN" class="text" maxlength="200" value="${row.ORIGIN}" />
				</td>
			</tr>
			<tr>
				<th>
					保质期:
				</th>
				<td>
					<input type="text" id="EXPIRY_DATE" name="EXPIRY_DATE" class="text" maxlength="200" value="${row.EXPIRY_DATE}" />天
				</td>
			</tr>
			<tr>
				<th>
					储藏方法:
				</th>
				<td>
					<input type="text" id="STORE" name="STORE" class="text" maxlength="200" value="${row.STORE}" />
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="button" id="submitBtn" class="button" value="确定"/>
					<input type="button" id="backBtn" class="button" value="返回" onclick="javascript:window.parent.location.href='../profile/list.do'"/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>