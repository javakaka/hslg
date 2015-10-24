var USER_ID =$("#user_id").val()
var GOODS_ID =$("#goods_id").val()
var PageUrl =baseUrl+"/hslgpage/app/front/goods/judge-page.do"
var OFFSET = 5;
var page = 1;
var page_size = 10;
var myScroll,
	pullDownEl, pullDownOffset,
	pullUpEl, pullUpOffset,
	generatedCount = 0;
var maxScrollY = 0;
var hasMoreData = false;

document.addEventListener('touchmove', function(e) {
	e.preventDefault();
}, false);

document.addEventListener('DOMContentLoaded', function() {
	$(document).ready(function() {
		initPage();
		autoAdapterImg("goodsDetailContent");
		loadData();
	});
}, false);

//加载列表
function loadData(){
	pullDownEl = document.getElementById('pullDown');
	pullDownOffset = pullDownEl.offsetHeight;
	pullUpEl = document.getElementById('pullUp');
	pullUpOffset = pullUpEl.offsetHeight;
	hasMoreData = false;
	$("#pullUp").hide();
	pullDownEl.className = 'loading';
	pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中...';

	page = 1;
	var params ={page:page,page_size:page_size,goods_id:GOODS_ID};
	$.ajax({
		type:"post",
		url:PageUrl,
		data:params,
		beforeSend: function (XMLHttpRequest){
		},
		success: function (data, textStatus){
			var code =data.code;
			appendData(data.oForm.LIST);
			var data_len = data.oForm.LIST.length;
			/**/
			if (data_len < page_size)
			{
				hasMoreData = false;
				$("#pullUp").hide();
			}
			else
			{
				hasMoreData = true;
				$("#pullUp").show();
			}
			
			myScroll = new iScroll('wrapper', {
					useTransition: true,
					topOffset: pullDownOffset,
					onRefresh: function() {
						if (pullDownEl.className.match('loading')) {
							pullDownEl.className = 'idle';
							pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉更新...';
							this.minScrollY = -pullDownOffset;
						}
						if (pullUpEl.className.match('loading')) {
							pullUpEl.className = 'idle';
							pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉更新...';
						}
					},
					onScrollMove: function() {
						if (this.y > OFFSET && !pullDownEl.className.match('flip')) {
							pullDownEl.className = 'flip';
							pullDownEl.querySelector('.pullDownLabel').innerHTML = '释放更新...';
							this.minScrollY = 0;
						} else if (this.y < OFFSET && pullDownEl.className.match('flip')) {
							pullDownEl.className = 'idle';
							pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉更新...';
							this.minScrollY = -pullDownOffset;
						} 
						if (this.y < (maxScrollY - pullUpOffset - OFFSET) && !pullUpEl.className.match('flip')) {
							if (hasMoreData) {
								this.maxScrollY = this.maxScrollY - pullUpOffset;
								pullUpEl.className = 'flip';
								pullUpEl.querySelector('.pullUpLabel').innerHTML = '释放更新...';
							}
						} else if (this.y > (maxScrollY - pullUpOffset - OFFSET) && pullUpEl.className.match('flip')) {
							if (hasMoreData) {
								this.maxScrollY = maxScrollY;
								pullUpEl.className = 'idle';
								pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉更新...';
							}
						}
					},
					onScrollEnd: function() {
						if (pullDownEl.className.match('flip')) {
							pullDownEl.className = 'loading';
							pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中...';
							refresh();
						}
						if (hasMoreData && pullUpEl.className.match('flip')) {
							pullUpEl.className = 'loading';
							pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';
							nextPage();
						}
					}
				});
				myScroll.refresh();
				if (hasMoreData) {
					myScroll.maxScrollY = myScroll.maxScrollY + pullUpOffset;
				} else {
					myScroll.maxScrollY = myScroll.maxScrollY;
				}
				maxScrollY = myScroll.maxScrollY;
		},
		complete: function (XMLHttpRequest, textStatus){
		
		},
		error: function (){
		}
	});
}

function initPage(){

	/*** tab change**/
	$("[name='tab_button']").click(function(obj){
		$("[name='tab_button']").removeClass("selected");
		$obj =$(this);
		$obj.addClass("selected");
		var data_num =$obj.attr("data-num")
		
		$("[name='tabContentDiv']").removeClass("selected");
		if(data_num ==  1)
		{
			$("#goodsDetailContent").addClass("selected");
		}
		else if(data_num ==  2)
		{
			$("#goodsAttributeContent").addClass("selected");
		}
		else if(data_num ==  3)
		{
			$("#goodsJudgementContent").addClass("selected");
		}
	});
	
	/** 收藏 **/
	$("#collectionBtn").click(function(){
		collectGoods(USER_ID,GOODS_ID);
	});
	/** 加入购物车 **/
	$("#addToCarBtn").click(function(){
		var json_str ="{\"id\":\""+GOODS_ID+"\",\"left_num\":\""+left_num+"\",\"raw_price\":\""+raw_price+"\",\"is_coupon\":\""+is_coupon+"\",\"coupon_price\":\""+coupon_price+"\",}";
		goodDetail.intoShoppingChar(json_str);
	});
}

function collectGoods(user_id,goods_id)
{
		var params={goods_id: GOODS_ID,user_id: USER_ID}
		$.ajax({
				url: baseUrl+"hslgpage/platform/member/collection/collectGooods.do",
				type: "POST",
				data: params,
				dataType: "json",
				cache: false,
				beforeSend: function (XMLHttpRequest){
				},
				success: function(ovo, textStatus) {
					var code =ovo.code;
					if(code >=0)
					{
						alert("收藏成功");	
					}
					else
					{
						alert("收藏出错");
					}
				},
				complete: function (XMLHttpRequest, textStatus){
					//alert("complete...");
				},
				error: function (){
					alert('error');
				}
			});
}


//追加数据
function appendData(list){
	var total =list.length;
	var html ="";
	if(typeof total !="undefined" && total !="")
	{
		$.each(list, function (i,item){
				html +="<div class=\"judge-item\">";
				html +="<div class=\"user-profile\">";
				if(typeof item.AVATAR == "undefined" || item.AVATAR == "")
				{
					html +="<div class=\"avatar\"><img src=\""+baseUrl+"/resources/cxhl_user_icon/default_user_avatar.png\" class=\"icon\"/></div>";
				}
				else
				{
					html +="<div class=\"avatar\"><img src=\""+baseUrl+item.AVATAR+"\" class=\"icon\"/></div>";
				}
				html +="<div class=\"username\">"+item.NAME+"</div>";
				html +="</div>";
				html +="<div class=\"content\">"+item.CONTENT+"</div>";
				html +="<div class=\"date\">"+item.CREATE_TIME+"</div>";
				html +="</div>";
		});
		$("#judge_list").append(html);
	}
	else
	{
		if(page == 1)
		{
			html ="<div style='margin:0 auto;width:60%;text-align: center;'>暂无数据</div>";
			$("#judge_list").html(html); 
		}
	}
}

/**
*下一页
**/
function nextPage() {
	page++;
	var params ={page:page,page_size:page_size,goods_id:GOODS_ID};
	$.ajax({
		type:"post",
		url:PageUrl,
		data:params,
		beforeSend: function (XMLHttpRequest){
		},
		success: function (data, textStatus){
			appendData(data.oForm.LIST);
			var data_len = data.oForm.LIST.length;
			if (data_len < page_size) 
			{
				hasMoreData = false;
				$("#pullUp").hide();
			} 
			else
			{
				hasMoreData = true;
				$("#pullUp").show();
			}
			myScroll.refresh();
			if (hasMoreData) {
				myScroll.maxScrollY = myScroll.maxScrollY + pullUpOffset;
			} else {
				myScroll.maxScrollY = myScroll.maxScrollY;
			}
			maxScrollY = myScroll.maxScrollY;
		},
		complete: function (XMLHttpRequest, textStatus){
		
		},
		error: function (){
			alert('error');
		}
	});
}

/**
*刷新
**/
function refresh() {
	page=1;
	var params ={page:page,page_size:page_size,goods_id:GOODS_ID};
	$.ajax({
		type:"post",
		url:PageUrl,
		data:params,
		beforeSend: function (XMLHttpRequest){
		},
		success: function (data, textStatus){
			$("#judge_list").html("");
			appendData(data.oForm.LIST);
			var data_len = data.oForm.LIST.length;
			if (data_len < page_size) 
			{
				hasMoreData = false;
				$("#pullUp").hide();
			} 
			else
			{
				hasMoreData = true;
				$("#pullUp").show();
			}
			myScroll.refresh();
			if (hasMoreData) {
				myScroll.maxScrollY = myScroll.maxScrollY + pullUpOffset;
			} else {
				myScroll.maxScrollY = myScroll.maxScrollY;
			}
			maxScrollY = myScroll.maxScrollY;
		},
		complete: function (XMLHttpRequest, textStatus){
		
		},
		error: function (){
			alert('error');
		}
	});
}

function autoAdapterImg(div_id)
{
	var divId ="#"+div_id;
	var img_list =$(divId).find("img");
	var div_width =$(divId).width();
	$.each(img_list,function(i,item){
		var img_w =item.width;
		var img_h =item.height;
		var ratio =div_width / img_w;
		var img_adapter_h =img_h *ratio;
		item.width =div_width;
		item.height =img_adapter_h;
	});
}