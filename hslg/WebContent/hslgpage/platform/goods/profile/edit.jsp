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
<title>编辑商品</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-form-to-json.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<link href="<%=basePath%>/res/css/diymen/tipswindown.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/css/diymen/tipswindown.js?version=1.4"></script>
<script type="text/javascript">
$().ready(function() {
	var $inputForm = $("#inputForm");
	var $submitBtn = $("#submitBtn");
	$submitBtn.click(function(){
		$("#inputForm").submit();
	});
	
	${flash_message}
	$("#inputForm").validate({ 
		  onsubmit:true,// 是否在提交是验证 
		  onfocusout:false,// 是否在获取焦点时验证 
		  onkeyup :false,// 是否在敲击键盘时验证
		  rules: 
		  { 
			  TYPE_ID:
			   {
		          required:true,
			    },
			    NAME:
			   {
		          required:true,
			    },
			    SUMMARY:
			   {
		          required:true,
		          rangelength:[0,32]
			    },
			    WEIGHT:
			   {
		          required:true,
		          number: true,
		          min: 0
			    },
			   UNIT:
			   {
		          required:true,
			    },
			   RAW_PRICE:
			   {
		          required:true,
		          number: true,
		          min: 0
			    },
			    IS_COUPON:
			   {
		          required:true,
			    },
			    COUPON_PRICE:
			   {
		          required:true,
		          number: true,
		          min: 0
			    },
			    TOTAL_NUM:
			   {
		          required:true,
		          number: true,
		          min: 0
			    },
			    SCORE:
			   {
		          required:true,
		          number: true,
		          min: 0
			    },
			    HOT:
			   {
		          required:true,
			    },
			    TEAM_BUY:
			   {
		          required:true,
			    },
			    STATE:
			   {
		          required:true,
			    },
			  }, 
			  messages:
			  { 
				  TYPE_ID:
				  {
			        required:'请选择商品分类'
			      },
			      NAME:
				  {
			        required:'请输入商品名称'
			      },
			      SUMMARY:
				  {
			        required:'请输入商品简介'
			      },
			      WEIGHT:
				  {
			        required:'请输入商品重量',
			      },
				  UNIT:
				  {
			        required:'请输入商品重量单位'
			      },
			      RAW_PRICE:
				  {
			        required:'请输入商品价格'
			      },
				  IS_COUPON:
				  {
			        required:'请选择商品是否打折'
			      },
				  COUPON_PRICE:
				  {
			        required:'请输入商品打折价格'
			      },
			      TOTAL_NUM:
				  {
			        required:'请输入商品数量'
			      },
			      SCORE:
				  {
			        required:'请输入商品慈善积分'
			      },
			      HOT:
				  {
			        required:'请选择商品是否热销'
			      },
				  TEAM_BUY:
				  {
			        required:'请选择商品是否团购'
			      },
				  STATE:
				  {
			        required:'请选择商品状态'
			      }
			  },
			  submitHandler: function(form) {  //通过之后回调 
				  var params=$(form).serialize();
				  params=$(form).serializeArray();
				  params=$(form).serializeJson();
				  delete params["TYPE_NAME"];
				  /* params=JSON.stringify(params); */
			  	  /* alert(params); */
					$.ajax({
							url: "<%=basePath%>hslgpage/platform/goods/profile/save.do",
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

function selectSessionIcon(){
	var id=$("#AUCTION_ID").val();
	if(typeof id == "undefined" || id == "" )
	{
		id ="";
	}
	title ="选择拍卖专场";
	url="iframe:<%=basePath%>paimaipage/platform/auction/subject/select.do?id="+id;
	width=620;
	iframeName="selectSessionIframeId";
	popWindow(title,url,width,height,drag,time,showBg,cssName,iframeName);
}

function setSelectedSession(id,name)
{
	$("#AUCTION_ID").val(id);
	$("#SESSION_NAME").val(name);
}

function sessionIframeSelectTarget()
{
 	window.frames["selectSessionIframeId"].selectTarget();
}

function selectTypeIcon(){
	var id=$("#TYPE_ID").val();
	if(typeof id == "undefined" || id == "" )
	{
		id ="";
	}
	title ="选择商品类型";
	url="iframe:<%=basePath%>hslgpage/platform/goods/type/select.do?id="+id;
	width=620;
	iframeName="selectTypeIframeId";
	popWindow(title,url,width,height,drag,time,showBg,cssName,iframeName);
}

function setSelectedType(id,name)
{
	$("#TYPE_ID").val(id);
	$("#TYPE_NAME").val(name);
}

function typeIframeSelectTarget()
{
 	window.frames["selectTypeIframeId"].selectTarget();
}

</script>
</head>
<body>
	<form id="inputForm" action="save.do" method="post">
	<input type="text" id="ID" name="ID" class="hidden" maxlength="200" value="${row.ID}" />
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>请选择商品分类:
				</th>
				<td>
					<input type="hidden" id="TYPE_ID"name="TYPE_ID" class="text" maxlength="200" value="${row.TYPE_ID}" />
					<input type="text" id="TYPE_NAME" name="TYPE_NAME" class="text" maxlength="200" value="${row.TYPE_NAME}" readonly />
					<img id="select_type_ico_id" name="select_type_ico_id" src="<%=basePath%>res/images/select_window.gif" onclick="selectTypeIcon();" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>商品名称:
				</th>
				<td>
					<input type="text" id="NAME" name="NAME" class="text" maxlength="200" value="${row.NAME}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>商品简介:
				</th>
				<td>
					<input type="text" id="SUMMARY" name="SUMMARY" class="text" maxlength="200" value="${row.SUMMARY}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>重量:
				</th>
				<td>
					<input type="text" id="WEIGHT" name="WEIGHT" class="text" maxlength="200" value="${row.WEIGHT}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>单位:
				</th>
				<td>
					<input type="text" id="UNIT" name="UNIT" class="text" maxlength="200" value="${row.UNIT}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>原价:
				</th>
				<td>
					<input type="text" id="RAW_PRICE" name="RAW_PRICE" class="text" maxlength="200" value="${row.RAW_PRICE}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>是否优惠:
				</th>
				<td>
					<select id="IS_COUPON" name="IS_COUPON" style="width:190px;">
						<c:choose>
							<c:when test="${row.IS_COUPON == 1}">
								<option value="" >请选择...</option>
								<option value="1" selected>打折</option>
								<option value="0" >不打折</option>
							</c:when>
							<c:when test="${row.IS_COUPON == 0}">
								<option value="" >请选择...</option>
								<option value="1" >打折</option>
								<option value="0" selected>不打折</option>
							</c:when>
							<c:otherwise>
								<option value="" selected>请选择...</option>
								<option value="1" >打折</option>
								<option value="0" >不打折</option>
							</c:otherwise>
						</c:choose>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>优惠价:
				</th>
				<td>
					<input type="text" id="COUPON_PRICE" name="COUPON_PRICE" class="text" maxlength="200" value="${row.COUPON_PRICE}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>数量:
				</th>
				<td>
					<input type="text" id="TOTAL_NUM" name="TOTAL_NUM" class="text" maxlength="200" value="${row.TOTAL_NUM}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>慈善积分:
				</th>
				<td>
					<input type="text" id="SCORE" name="SCORE" class="text" maxlength="200" value="${row.SCORE}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>是否热卖:
				</th>
				<td>
					<select id="HOT" name="HOT" style="width:190px;">
						<c:choose>
							<c:when test="${row.HOT == 1}">
								<option value="1" selected>热卖</option>
								<option value="0" >非热卖</option>
							</c:when>
							<c:when test="${row.HOT == 0}">
								<option value="1" >热卖</option>
								<option value="0" selected>非热卖</option>
							</c:when>
							<c:otherwise>
								<option value="1" >热卖</option>
								<option value="0" selected>非热卖</option>
							</c:otherwise>
						</c:choose>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>是否可团购:
				</th>
				<td>
					<select id="TEAM_BUY" name="TEAM_BUY" style="width:190px;">
						<c:choose>
							<c:when test="${row.TEAM_BUY == 1}">
								<option value="1" selected>可团购</option>
								<option value="0" >非团购</option>
							</c:when>
							<c:when test="${row.TEAM_BUY == 0}">
								<option value="1" >可团购</option>
								<option value="0" selected>非团购</option>
							</c:when>
							<c:otherwise>
								<option value="1" >可团购</option>
								<option value="0" selected>非团购</option>
							</c:otherwise>
						</c:choose>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>状态:
				</th>
				<td>
					<select id="STATE" name="STATE" style="width:190px;">
						<c:choose>
							<c:when test="${row.STATE == 1}">
								<option value="" >请选择...</option>
								<option value="1" selected>上架</option>
								<option value="0" >下架</option>
							</c:when>
							<c:when test="${row.STATE == 0}">
								<option value="" >请选择...</option>
								<option value="1" >上架</option>
								<option value="0" selected>下架</option>
							</c:when>
							<c:otherwise>
								<option value="" selected>请选择...</option>
								<option value="1" >上架</option>
								<option value="0" >下架</option>
							</c:otherwise>
						</c:choose>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="button" id="submitBtn"  class="button" value="确定"/>
					<input type="button" id="backBtn" class="button" value="返回" onclick="javascript:window.parent.location.href='list.do'"/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>