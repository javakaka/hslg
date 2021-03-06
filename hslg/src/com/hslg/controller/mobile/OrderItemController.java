package com.hslg.controller.mobile;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;
import com.hslg.service.OrderItemService;
import com.hslg.service.OrderService;
import com.hslg.service.ShopCouponService;
import com.hslg.service.UserService;
/**
 * 订单
 * @author TongJianbo
 *
 */
@Controller("mobileOrderItemController")
@RequestMapping("/api/order/item")
public class OrderItemController extends BaseController {
	
	private static Logger logger = Logger.getLogger(OrderItemController.class); 
	
	@Resource(name = "cxhlUserService")
	private UserService userService;
	
	@Resource(name = "cxhlOrderService")
	private OrderService orderService;
	
	@Resource(name = "cxhlOrderItemService")
	private OrderItemService orderItemService;
	
	@Resource(name = "cxhlShopCouponService")
	private ShopCouponService shopCouponService;
	
	/**
	 * 根据订单编号查询订单项列表
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryPage(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("根据订单编号查询订单项列表");
		String order_id =ivo.getString("id","");
		if(StringUtils.isEmptyOrNull(order_id))
		{
			ovo =new OVO(-10021,"订单ID不能为空","订单ID不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		DataSet ds =orderItemService.findOrderItems(order_id);
		ovo =new OVO(0,"","");
		ovo.set("order_items", ds);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 创建订单
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/add")
	public @ResponseBody
	String add(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("创建订单");
		String user_id =ivo.getString("user_id","");
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row userRow =userService.find(user_id);
		if(userRow == null)
		{
			ovo =new OVO(-1,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String money =ivo.getString("money","");
		if(StringUtils.isEmptyOrNull(money))
		{
			ovo =new OVO(-1,"订单总价格不能为空","订单总价格不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//订单项目列表
		DataSet item_list =(DataSet)ivo.get("items");
		
		
		String error_msg =validateOrderItems(item_list);
		if( !StringUtils.isEmptyOrNull(error_msg))
		{
			ovo =new OVO(-1,error_msg,error_msg);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String validate_money =String.valueOf(calculateOrderMoney(item_list));
		validate_money =NumberUtils.getTwoDecimal(validate_money);
		money =NumberUtils.getTwoDecimal(money);
		if( !money.equals(validate_money))
		{
			ovo =new OVO(-1,"订单价格计算错误，不能创建订单","订单价格计算错误，不能创建订单");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//创建订单
		Row orderRow =new Row();
		orderRow.put("user_id", user_id);
		orderRow.put("money", money);
		orderRow.put("state", "1");//1待支付2已支付
		String cur_time =DateUtil.getCurrentDateTime();
		String order_no =cur_time.replace("-", "").replace(" ", "").replace(":", "");
		int order_num =orderService.getOrderNumByCreateTime(cur_time);
		if(order_num ==0)
		{
			order_num =10001;
		}
		else
		{
			order_num ++;
		}
		order_no =order_no+order_num;
		orderRow.put("order_no", order_no);
		orderService.insert(orderRow);
		String order_id =orderRow.getString("id","");
		//保存订单项到数据库
		for(int i=0;i<item_list.size(); i++)
		{
			Row item =(Row)item_list.get(i);
			item.put("order_id", order_id);
			orderItemService.insert(item);
		}
		ovo =new OVO(0,"下单成功","下单成功");
		ovo.set("order_no", order_no);
		ovo.set("id", order_id);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/detail")
	public @ResponseBody
	String find(HttpServletRequest request) throws Exception
	{
		//
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/delete")
	public @ResponseBody
	String delete(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"收藏编号不能为空","收藏编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
//		shopService.delete(id);
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

	public String validateOrderItems(DataSet ds)
	{
		String error_msg ="";
		if(ds == null || ds.size() ==0)
		{
			error_msg ="订单项为空，不能创建订单";
			return error_msg;
		}
		for(int i=0;i<ds.size(); i++)
		{
			Row item =(Row)ds.get(i);
			String coupon_id =item.getString("counpon_id","");
			String price =item.getString("price","");
			String num =item.getString("num","");
			boolean bool =shopCouponService.isExisted(coupon_id);
			if(! bool)
			{
				error_msg ="优惠券的编号不存在，请提交正确的订单数据";
				break;
			}
			if(StringUtils.isEmptyOrNull(price))
			{
				error_msg ="优惠券的价格不能为空，请提交正确的订单数据";
				break;
			}
			if(! NumberUtils.isNumber(price))
			{
				error_msg ="优惠券的价格应该是数字，请提交正确的订单数据";
				break;
			}
			if(StringUtils.isEmptyOrNull(num))
			{
				error_msg ="优惠券的数量不能为空，请提交正确的订单数据";
				break;
			}
			if(! NumberUtils.isNumber(num))
			{
				error_msg ="优惠券的数量应该是数字，请提交正确的订单数据";
				break;
			}
		}
		return error_msg;
	}
	
	/**
	 * 计算订单价格
	 * @param ds
	 * @return
	 */
	public double calculateOrderMoney(DataSet ds)
	{
		double sum =0;
		for(int i=0;i<ds.size(); i++)
		{
			Row item =(Row)ds.get(i);
			String price =item.getString("price","");
			String num =item.getString("num","");
			BigDecimal b_price =new BigDecimal(price);
			BigDecimal b_num =new BigDecimal(num);
			sum =b_price.multiply(b_num).doubleValue();
		}
		return sum;
	}
	
	
}
