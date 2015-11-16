package com.ezcloud.framework.controller.pay;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.controller.ApiBaseController;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.SpringUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.util.WeixinUtil;
import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.hslg.service.CommonwealLoveRecordService;
import com.hslg.service.UserService;

/**
 * 微信APP支付 
 * @author Thinkive.TongJianbo
 */
@Controller("frameworkWeiXinPayController")
@RequestMapping("/api/pay/weixin/app")
public class WeiXinAPPPayController extends ApiBaseController{
	
	private static Logger logger = Logger.getLogger(WeiXinAPPPayController.class);
	
	@Resource(name = "hslgUserService")
	private UserService userService;
	
	@Resource(name = "hslgCommonwealLoveRecordService")
	private CommonwealLoveRecordService orderService;
	
	/**
	 * 客户端发起支付请求，到服务器端认证
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/validate")
	public @ResponseBody
	String validate(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("客户端发起微信支付请求，服务器端开始验证请求是否合法...");
		String user_id =ivo.getString("user_id","");
		String  service_name=ivo.getString("service_name","");
		String  device=ivo.getString("device","1");//1 android 2 ios
		if(StringUtils.isEmptyOrNull(service_name))
		{
			ovo =new OVO(-20010,"参数错误，请指定支付服务名称[service_name]","参数错误，请指定支付服务名称[service_name]");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(device))
		{
			device ="1";
		}
		ivo.set("device", device);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-20010,"参数错误，用户编号[user_id]不能为空","参数错误，用户编号[user_id]不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String app_remote_ip =request.getRemoteAddr();
		String app_remote_host =request.getRemoteHost();
		logger.info("app_remote_ip========>>"+app_remote_ip);
		logger.info("app_remote_host========>>"+app_remote_host);
		ivo.set("app_ip", app_remote_ip);
		Object serviceObj =null;
		try
		{
			serviceObj =SpringUtils.getBean(service_name);
		}catch(NoSuchBeanDefinitionException exp)
		{
			ovo =new OVO(-20010,"系统不存在[service_name]所对应的服务，请和管理员联系","系统不存在[service_name]所对应的服务，请和管理员联系");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(serviceObj == null)
		{
			ovo =new OVO(-20010,"系统不存在[service_name]所对应的服务，请和管理员联系","系统不存在[service_name]所对应的服务，请和管理员联系");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Method method =serviceObj.getClass().getDeclaredMethod("validate",IVO.class);
		ovo =(OVO)method.invoke(serviceObj,ivo);
		System.out.println("ovo----------"+ovo.oForm);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 客户端发起支付请求，到服务器端认证
	 * @param request
	 * @return
	 * <xml>
		  <return_code><![CDATA[SUCCESS]]></return_code>
		  <return_msg><![CDATA[OK]]></return_msg>
		</xml>
	 * @throws IOException 
	 * @throws Exception
	 */
	@RequestMapping(value ="/notify")
	public void notify(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String returen_msg_template ="<xml><return_code><![CDATA[{RETURN_CODE}]]></return_code><return_msg><![CDATA[{RETURN_MSG}]]></return_msg></xml>";
		String return_code ="SUCCESS";
		String return_msg ="";
		logger.info("微信支付回调处理过程...");
		HashMap<String, String> map =null;
		Row row =new Row();
		try {
			map = WeixinUtil.parseXml(request);
			row =MapUtils.convertMaptoRowWithoutNullField(map);
			logger.info("解析微信服务器发来的数据===>>"+row);
		} catch (Exception e) {
			logger.info("解析http post 数据转换成 map对象出错:"+e.getMessage());
			return_code ="FAIL";
			return_msg ="FAIL";
		}
		if(return_code.equals("FAIL"))
		{
			returen_msg_template =returen_msg_template.replace("{RETURN_CODE}", return_code);
			returen_msg_template =returen_msg_template.replace("{RETURN_MSG}", return_msg);
			response.getWriter().print(returen_msg_template);
			return;
		}
		//验证微信服务器处理支付是否成功
		String wechat_return_code =row.getString("return_code","FAIL");
		String wechat_return_msg =row.getString("return_msg","");
		String result_code =row.getString("result_code","FAIL");
		//处理失败
		if( ! (wechat_return_code.equals("SUCCESS") && result_code.equals("SUCCESS")))
		{
			String err_code =row.getString("err_code","");
			String err_code_des =row.getString("err_code_des","");
			logger.info("微信支付返回交易出错原因[return_msg]:"+wechat_return_msg+",[err_code]:"+err_code+",[err_code_des]:"+err_code_des);
			return_code ="FAIL";
			return_msg ="FAIL";
			returen_msg_template =returen_msg_template.replace("{RETURN_CODE}", return_code);
			returen_msg_template =returen_msg_template.replace("{RETURN_MSG}", return_msg);
			response.getWriter().print(returen_msg_template);
			return;
		}
		//验证订单号是否相等
		String xml_order_no =row.getString("out_trade_no","");
		String total_fee =row.getString("total_fee","");
		String transaction_id =row.getString("transaction_id","");
		String appid =row.getString("appid","");
		String mch_id =row.getString("mch_id","");
		String nonce_str =row.getString("nonce_str","");
		String sign =row.getString("sign","");
		String time_end =row.getString("time_end","");
		logger.info("url订单号[xml_order_no]:"+xml_order_no);
		//判断订单是否存在
		Row orderRow =orderService.findByOrderNo(xml_order_no);
		if(orderRow == null)
		{
			logger.info("订单号不存在:,[order_no]:"+xml_order_no);
			return_code ="FAIL";
			return_msg ="FAIL";
			returen_msg_template =returen_msg_template.replace("{RETURN_CODE}", return_code);
			returen_msg_template =returen_msg_template.replace("{RETURN_MSG}", return_msg);
			response.getWriter().print(returen_msg_template);
			return;
		}
		//判断订单是否已正确处理订单状态，如果已处理，则直接返回处理正确响应结果给微信，否则，更改订单状态
		String state =orderRow.getString("state","");
		if(state.equals("2"))
		{
			return_code ="SUCCESS";
			return_msg ="SUCCESS";
			returen_msg_template =returen_msg_template.replace("{RETURN_CODE}", return_code);
			returen_msg_template =returen_msg_template.replace("{RETURN_MSG}", return_msg);
			response.getWriter().print(returen_msg_template);
			return;
		}
		Row updateRow =new Row();
		updateRow.put("id", orderRow.getString("id"));
		updateRow.put("state", "2");//0待支付1已支付未到账2支付完成
		updateRow.put("pay_type", "3");//1支付宝2银联3微信
		updateRow.put("pay_order_no", transaction_id);
		updateRow.put("pay_finish_time", time_end);
		double dtotal_fee =Double.parseDouble(total_fee);
		double dnew_paid_fee =dtotal_fee;
		String new_paid_fee =String.valueOf(dnew_paid_fee);
		new_paid_fee =NumberUtils.getTwoDecimal(new_paid_fee);
		updateRow.put("money", new_paid_fee);
		orderService.update(updateRow);
		return_code ="SUCCESS";
		return_msg ="SUCCESS";
		returen_msg_template =returen_msg_template.replace("{RETURN_CODE}", return_code);
		returen_msg_template =returen_msg_template.replace("{RETURN_MSG}", return_msg);
		response.getWriter().print(returen_msg_template);
		return;
	}
}
