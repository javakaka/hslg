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
  <title>商品详情</title>
  <meta name="description" content="商品详情" />
  <meta name="keywords" content="商品详情" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
  <meta content="telephone=no" name="format-detection">
  <meta name="apple-mobile-web-app-capable" content="商品详情">
  <meta name="apple-mobile-web-app-title" content="商品详情" />
  <meta name="apple-mobile-web-app-capable" content="yes" />
  <meta content="telephone=no" name="format-detection" />
  <meta name="full-screen" content="yes" />
  <meta name="x5-fullscreen" content="true" /> 
  <link rel="Stylesheet" href="<%=basePath%>hslgpage/app/res/css/main.css" />
 </head>
 <body >
 <input type="hidden" name="user_id" id="user_id" value="${user_id}"/>
  <input type="hidden" name="goods_id" id="goods_id" value="${goods_id}"/>
  <div id="main" class="wrapper white " style="text-indent: 20px;">
  <!-- icon -->
  <div class="goods-base">
	  <div class="goods-icon">
		<img src="${icon_row.PATH}" class="" />
	  </div>
	  <div class="goods-summary">${goods_row.SUMMARY} </div>
	  <div class="goods-action">
			<div class="block-left">
				<div class="up"><span class="coupon-price">${goods_row.COUPON_PRICE}元</span><span></span><span class="raw-price">${goods_row.RAW_PRICE}元</span><span></span></div>
				<div class="down">慈善积分:<span>${goods_row.SCORE}</span></div>
			</div>
			<div class="block-center"  id="collectionBtn">收藏</div>
			<div class="block-right">
				<div class="up">现货</div>
				<div class="down" id="addToCarBtn">加入购物车</div>
			</div>
	  </div>
  </div>
  <!-- like -->
  <div class="goods-push">
  	<div class="title">也许您还喜欢</div>
  	<div class="goods">
		<div class="icon">
			<img src="" class=""/>
		</div>
		<div class="summary">${goods_row.COUPON_PRICE}</div>
		<div class="price">600元</div>
  	</div>
  	<div class="goods">
		<div class="icon">
			<img src="" class=""/>
		</div>
		<div class="summary">龙杨茶叶</div>
		<div class="price">600元</div>
  	</div>
  </div>
  <!-- tab -->
  <div class="goods-tab">
	  <div class="tool-bar">
		  <div class="left selected" id="tabLeftBtn" name="tab_button" data-num="1">图文详情</div>
		  <div class="center" id="tabCenterBtn" name="tab_button" data-num="2">产品参数</div>
		  <div class="right" id="tabRightBtn" name="tab_button" data-num="3">评价</div>
	  </div>
	  <div class="tab-content selected" id="goodsDetailContent" name="tabContentDiv">${goods_row.DETAIL}</div>
	  <div class="tab-content" id="goodsAttributeContent" name="tabContentDiv">
	  	<div class="attribute-line">
			<div class="left">商品名称:</div>
			<div class="right">${goods_row.NAME}</div>
	  	</div>
	  	<div class="attribute-line">
			<div class="left">品牌:</div>
			<div class="right">${attr_row.BRAND}</div>
	  	</div>
	  	<div class="attribute-line">
			<div class="left">规格:</div>
			<div class="right">${attr_row.SPECIFICATION}</div>
	  	</div>
	  	<div class="attribute-line">
			<div class="left">包装:</div>
			<div class="right">${attr_row.PACKAGING}</div>
	  	</div>
	  	<div class="attribute-line">
			<div class="left">原产地:</div>
			<div class="right">${attr_row.ORIGIN}</div>
	  	</div>
	  	<div class="attribute-line">
			<div class="left">保质期:</div>
			<div class="right">${attr_row.EXPIRY_DATE}</div>
	  	</div>
	  	<div class="attribute-line">
			<div class="left">储藏方法:</div>
			<div class="right">${attr_row.STORE}</div>
	  	</div>
	  </div>
	  <div class="tab-content" id="goodsJudgementContent" name="tabContentDiv">
	  <c:forEach items="${judge_list}" var="item" varStatus="status">
	   <div class="judge-item">
			  <div class="user-profile">
				  <div class="avatar"><img src="<%=basePath%>${item.avatar}" class=""/></div>
				  <div class="username">${item.name}</div>
			  </div>
			  <div class="content">${item.CONTENT}</div>
			  <div class="date">${item.CREATE_TIME}</div>
		  </div>
	  </c:forEach>
		  <!--
		  <div class="judge-item">
			  <div class="user-profile">
				  <div class="avatar"><img src="" class=""/></div>
				  <div class="username">123456</div>
			  </div>
			  <div class="content">非常好</div>
			  <div class="date">2015-08-16</div>
		  </div>
		  <div class="judge-item">
			  <div class="user-profile">
				  <div class="avatar"><img src="" class=""/></div>
				  <div class="username">123456</div>
			  </div>
			  <div class="content">非常好</div>
			  <div class="date">2015-08-16</div>
		  </div>
		  -->
	  </div>
  </div>
  </div>
  <script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
  <script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
    <script type="text/javascript" src="<%=basePath%>/res/js/web.js"></script>
  <script type="text/javascript" src="<%=basePath%>hslgpage/app/res/js/goods-detail.js"></script>
 </body>
 <script>
 var baseUrl ="<%=basePath%>";
 var raw_price ='${goods_row.RAW_PRICE}';
 var coupon_price ='${goods_row.COUPON_PRICE}';
 var is_coupon ='${goods_row.IS_COUPON}';
 var left_num ='${goods_row.LEFT_NUM}';
 	$(function(){
 		
 	});
 </script>
</html>