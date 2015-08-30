package com.hslg.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.hslg.service.GoodsAttributeService;
import com.hslg.service.GoodsService;
import com.hslg.service.UserCollectionService;
import com.hslg.service.UserService;
/**
 * 订单
 * @author TongJianbo
 *
 */
@Controller("mobileGoodsController")
@RequestMapping("/api/goods/profile")
public class GoodsController extends BaseController {
	
	private static Logger logger = Logger.getLogger(GoodsController.class); 
	
	@Resource(name = "hslgUserService")
	private UserService userService;
	
	@Resource(name = "hslgGoodsService")
	private GoodsService goodsService;
	
	@Resource(name = "hslgUserCollectionService")
	private UserCollectionService userCollectionService;
	
	/**
	 * 分页查询商品
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryPage(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("分页查询商品");
		String type_id =ivo.getString("type_id","");
		String is_hot =ivo.getString("is_hot","");
		String team_buy =ivo.getString("team_buy","");
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet list =goodsService.list(type_id,is_hot,team_buy,page,page_size);
		ovo =new OVO(0,"","");
		ovo.set("list", list);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/*
	 * 商品详情页面，查询是否已收藏、推荐商品数据
	 */
	@RequestMapping(value ="/detail")
	public @ResponseBody
	String queryDetail(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String id =ivo.getString("id","");
		String user_id =ivo.getString("user_id","");
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"商品编号不能为空","商品编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		
		Row row =goodsService.findDetail(id);
		if(row ==null)
		{
			ovo =new OVO(-1,"商品不存在","商品不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String name =row.getString("name","");
		String type_id =row.getString("type_id","");
		String left_num =row.getString("left_num","");
		String sale_num =row.getString("sale_num","");
		String score =row.getString("score","");
		String weight =row.getString("weight","");
		String unit =row.getString("unit","");
		String raw_price =row.getString("raw_price","");
		String coupon_price =row.getString("coupon_price","");
		String is_coupon =row.getString("is_coupon","");
		String summary =row.getString("summary","");
		String detail =row.getString("detail","");
		String file_path =row.getString("file_path","");
		//是否已收藏
		String is_collected ="0";
		if(!StringUtils.isEmptyOrNull(user_id))
		{
			Row collectionRow =userCollectionService.find(user_id, id);
			if(collectionRow != null)
			{
				is_collected ="1";
			}
		}
		//推荐商品，当前商品所属类型的同类型商品，当前商品id往后最靠近的两个商品
		DataSet similarityDs =goodsService.findSimilarityGoods(id, type_id);
		ovo =new OVO(0,"","");
		ovo.set("id",id );
		ovo.set("name", name);
		ovo.set("type_id", type_id);
		ovo.set("left_num", left_num);
		ovo.set("sale_num", sale_num);
		ovo.set("score", score);
		ovo.set("weight", weight);
		ovo.set("unit", unit);
		ovo.set("raw_price", raw_price);
		ovo.set("coupon_price", coupon_price);
		ovo.set("is_coupon", is_coupon);
		ovo.set("summary", summary);
		ovo.set("detail", detail);
		ovo.set("file_path", file_path);
		ovo.set("is_collected", is_collected);
		ovo.set("similarity_list", similarityDs);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
}
