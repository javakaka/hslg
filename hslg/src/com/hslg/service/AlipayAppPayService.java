package com.hslg.service;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 */
@Component("hslgAlipayAppPayService")
public class AlipayAppPayService extends Service {

	private static Logger logger = Logger.getLogger(AlipayAppPayService.class);
	
	@Resource(name = "hslgUserService")
	private UserService userService;
	
	@Resource(name = "hslgOrderService")
	private OrderService orderService;
	
	@Resource(name = "hslgUserTokenService")
	private UserTokenService userTokenService;
	
	public AlipayAppPayService() 
	{
		
	}
	
	/**
	 * 
	 * @param ivo
	 * @return
	 * @throws Exception
	 */
	public OVO validate(IVO ivo) throws Exception
	{
		OVO ovo =new OVO();
		String user_id =ivo.getString("user_id");
		String order_id =ivo.getString("order_id");
		Row userRow =userService.find(user_id);
		if(userRow == null)
		{
			ovo =new OVO(-20011,"用户不存在","用户不存在");
			return ovo;
		}
		Row tokenRow =userTokenService.find(user_id);
		if(tokenRow == null)
		{
			ovo =new OVO(-20012,"用户未登录","用户未登录");
			return ovo;
		}
		Row orderRow =orderService.find(order_id);
		if(orderRow == null)
		{
			ovo =new OVO(-20013,"订单不存在","订单不存在");
			return ovo;
		}
		String order_no=orderRow.getString("order_no");
		String order_state =orderRow.getString("state","");
		if(!order_state.equals("0"))
		{
			ovo =new OVO(-20014,"订单正在处理中，不能重复支付","订单正在处理中，不能重复支付");
			return ovo;
		}
		//校验订单是否属于当前用户
		String order_user_id =orderRow.getString("user_id","");
		if(! user_id.equals(order_user_id))
		{
			ovo =new OVO(-20016,"非法操作","非法操作");
			return ovo;
		}
		String money =orderRow.getString("money","0");
		Setting setting =SettingUtils.get();
		String site_url =setting.getSiteUrl();
		String notify_url =site_url+"/api/pay/alipay/app/notify.do";
		//设置订单的状态［state］为1，处理中(0待付款，1已付款未到账，2已到账待收货，3已收货4申请退款5退款未到账，6已退款)
		Row updateRow =new Row();
		updateRow.put("id", order_id);
		updateRow.put("state", "1");
		orderService.update(updateRow);
		ovo =new OVO(0,"","");
		ovo.set("order_id", order_id);
		ovo.set("order_no", order_no);
		ovo.set("money", money);
		ovo.set("notify_url", notify_url);
		logger.info("支付宝支付订单校验通过");
		return ovo;
	}
}