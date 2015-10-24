var USER_ID =$("#user_id").val()
var GOODS_ID =$("#goods_id").val()
$(function(){

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
});

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