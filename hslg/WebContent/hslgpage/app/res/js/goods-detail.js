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
		addGoodsToShopCar(USER_ID,GOODS_ID);
	});
});

function collectGoods(user_id,goods_id)
{

}

function addGoodsToShopCar(user_id,goods_id)
{

}