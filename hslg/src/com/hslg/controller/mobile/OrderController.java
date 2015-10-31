package com.hslg.controller.mobile;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;
import com.hslg.service.GoodsService;
import com.hslg.service.OrderItemService;
import com.hslg.service.OrderService;
import com.hslg.service.UserAddressService;
import com.hslg.service.UserService;
/**
 * 订单
 * @author TongJianbo
 *
 */
@Controller("mobileOrderController")
@RequestMapping("/api/order/profile")
public class OrderController extends BaseController {
	
	private static Logger logger = Logger.getLogger(OrderController.class); 
	
	@Resource(name = "hslgUserService")
	private UserService userService;
	
	@Resource(name = "hslgOrderService")
	private OrderService orderService;
	
	@Resource(name = "hslgOrderItemService")
	private OrderItemService orderItemService;
	
	@Resource(name = "hslgGoodsService")
	private GoodsService goodsService;
	
	@Resource(name = "hslgUserAddressService")
	private UserAddressService userAddressService;
	
	/**
	 * 用户分页查询自己的订单，按创建时间倒序排列
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryPage(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("用户分页查询自己的订单，按创建时间倒序排列");
		String user_id =ivo.getString("user_id","");
		String state =ivo.getString("state","");
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet list =orderService.listWithOneGoods(user_id,state,page,page_size);
		ovo =new OVO(0,"","");
		ovo.set("list", list);
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
		String address_id =ivo.getString("address_id","");
		if(StringUtils.isEmptyOrNull(address_id))
		{
			ovo =new OVO(-1,"订单收货地址不能为空","订单收货地址不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String transfer_fee =ivo.getString("transfer_fee","");
		if(StringUtils.isEmptyOrNull(transfer_fee))
		{
			ovo =new OVO(-1,"订单运费不能为空，如果没有运费，则此字段的值为0","订单运费不能为空，如果没有运费，则此字段的值为0");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String order_message =ivo.getString("order_message","");
		if(StringUtils.isEmptyOrNull(order_message))
		{
			order_message ="";
		}
		String order_type =ivo.getString("order_type","");
		if(StringUtils.isEmptyOrNull(order_type))
		{
			ovo =new OVO(-1,"订单类型不能为空，1在线支付2货到付款3水票支付","订单类型不能为空，1在线支付2货到付款3水票支付");
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
		double order_money =Double.parseDouble(validate_money)+Double.parseDouble(transfer_fee);
		validate_money =String.valueOf(order_money);
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
		orderRow.put("state", "0");//0待付款，1已付款未到账，2已到账待收货，3已收货4申请退款5退款未到账，6已退款
		orderRow.put("address_id", address_id);
		orderRow.put("transfer_fee", transfer_fee);
		orderRow.put("order_message", order_message);
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
		orderRow.put("order_type", order_type);
		orderService.insert(orderRow);
		String order_id =orderRow.getString("id","");
		//保存订单项到数据库、减去库存
		for(int i=0;i<item_list.size(); i++)
		{
			Row item =(Row)item_list.get(i);
//			减去库存、增加已售数量
			String goods_id =item.getString("goods_id");
			String goods_num =item.getString("goods_num");
			String updateSql ="update hslg_goods set left_num=left_num-"+goods_num+" , "
					+ " total_num=total_num-"+goods_num+" , sale_num=sale_num+"+goods_num+" where id='"+goods_id+"'";
			goodsService.update(updateSql);
//			保存订单项到数据库
			item.put("order_id", order_id);
			orderItemService.insert(item);
		}
		ovo =new OVO(0,"下单成功","下单成功");
		ovo.set("order_no", order_no);
		ovo.set("id", order_id);
		ovo.set("money", money);
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
		parseRequest(request);
		logger.info("创建订单");
		String id =ivo.getString("id","");
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"订单编号不能为空","订单编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row orderRow =orderService.find(id);
		if(orderRow == null)
		{
			ovo =new OVO(-1,"订单不存在","订单不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String address_id =orderRow.getString("address_id","");
		DataSet goods_list =orderItemService.findOrderItems(id);
		Row addrRow =userAddressService.find(address_id);
		String receive_name ="";
		String receive_tel ="";
		String receive_address ="";
		if(addrRow != null )
		{
			receive_name =addrRow.getString("receive_name", "");
			receive_tel =addrRow.getString("receive_tel", "");
			receive_address =addrRow.getString("address", "");
		}
		String transfer_fee =orderRow.getString("transfer_fee","");
		String money =orderRow.getString("money","");
		String transfer_state =orderRow.getString("transfer_state","0");//0未配送/已下单1已配送/配送中2已完成
		String order_no =orderRow.getString("money","");
		String create_time =orderRow.getString("create_time","");
		ovo =new OVO(0,"","");
		ovo.set("transfer_fee", transfer_fee);
		ovo.set("money", money);
		ovo.set("transfer_state", transfer_state);
		ovo.set("order_no", order_no);
		ovo.set("create_time", create_time);
		ovo.set("receive_name", receive_name);
		ovo.set("receive_tel", receive_tel);
		ovo.set("receive_address", receive_address);
		ovo.set("goods_list", goods_list);
		
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
			String price =item.getString("goods_price","");
			String num =item.getString("goods_num","");
			String goods_id =item.getString("goods_id","");
			if(StringUtils.isEmptyOrNull(goods_id))
			{
				error_msg ="商品的编号不能为空，请提交正确的订单数据";
				break;
			}
			if(StringUtils.isEmptyOrNull(price))
			{
				error_msg ="商品的价格不能为空，请提交正确的订单数据";
				break;
			}
			if(! NumberUtils.isNumber(price))
			{
				error_msg ="商品的价格应该是数字，请提交正确的订单数据";
				break;
			}
			if(StringUtils.isEmptyOrNull(num))
			{
				error_msg ="商品的数量不能为空，请提交正确的订单数据";
				break;
			}
			if(! NumberUtils.isNumber(num))
			{
				error_msg ="商品的数量应该是数字，请提交正确的订单数据";
				break;
			}
			//检查商品库存是否足够
			boolean bool =goodsService.isGoodsNumEnough(goods_id, num);
			if(! bool)
			{
				error_msg ="商品库存数量不足，请提交正确的订单数据";
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
	@SuppressWarnings("unchecked")
	public double calculateOrderMoney(DataSet ds)
	{
		double sum =0;
		for(int i=0;i<ds.size(); i++)
		{
			double temp =0;
			Row item =(Row)ds.get(i);
			String price =item.getString("goods_price","");
			String num =item.getString("goods_num","");
			BigDecimal b_price =new BigDecimal(price);
			BigDecimal b_num =new BigDecimal(num);
			temp =b_price.multiply(b_num).doubleValue();
			item.put("goods_money",temp);
			ds.set(i, item);
			sum +=temp;
		}
		return sum;
	}
}
