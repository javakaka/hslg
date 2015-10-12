package com.ezcloud.framework.controller.pay;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.util.AlipayNotify;
import com.ezcloud.framework.controller.ApiBaseController;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.SpringUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.hslg.service.CommonwealArchiveRecordService;
import com.hslg.service.CommonwealLoveRecordService;
import com.hslg.service.CommonwealProjectRecordService;
import com.hslg.service.OrderService;

/**
 * 支付宝APP支付 
 * @author Thinkive.TongJianbo
 */
@Controller("frameworkAlipayPayController")
@RequestMapping("/api/pay/alipay/app")
public class AlipayAPPPayController extends ApiBaseController{
	
	private static Logger logger = Logger.getLogger(AlipayAPPPayController.class);
	
	@Resource(name = "hslgOrderService")
	private OrderService orderService;
	
	@Resource(name = "hslgCommonwealLoveRecordService")
	private CommonwealLoveRecordService commonwealLoveRecordService;
	
	@Resource(name = "hslgCommonwealArchiveRecordService")
	private CommonwealArchiveRecordService commonwealArchiveRecordService;
	
	@Resource(name = "hslgCommonwealProjectRecordService")
	private CommonwealProjectRecordService commonwealProjectRecordService;
	
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
		logger.info("客户端发起支付宝支付请求，服务器端开始验证请求是否合法...");
		String user_id =ivo.getString("user_id","");
		String order_id =ivo.getString("order_id","");
		String  service_name=ivo.getString("service_name","");
		if(StringUtils.isEmptyOrNull(service_name))
		{
			ovo =new OVO(-20010,"参数错误，请指定支付服务名称[service_name]","参数错误，请指定支付服务名称[service_name]");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-20010,"参数错误，用户编号[user_id]不能为空","参数错误，用户编号[user_id]不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(order_id))
		{
			ovo =new OVO(-20010,"参数错误，订单编号[order_id]不能为空","参数错误，订单编号[order_id]不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Object serviceObj =null;
		try
		{
			serviceObj =SpringUtils.getBean(service_name);//hslgAlipayAppPayService
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
		logger.info("支付宝支付订单校验结果,返回给客户端的数据："+ovo.oForm);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 客户端发起支付请求，到服务器端认证
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value ="/notify")
	public void notify(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		String out_trade_no = request.getParameter("out_trade_no");
		//支付宝交易号
		String trade_no = request.getParameter("trade_no");
		//交易状态
		String trade_status = request.getParameter("trade_status");
		//订单总金额
		String total_fee = request.getParameter("total_fee");
		//交易付款时间
		String gmt_payment =request.getParameter("gmt_payment");
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		if(AlipayNotify.verify(params)){//验证成功
			logger.info("支付宝的通知返回参数验证成功");
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码
			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS") ){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
				//判断订单是否存在
				Row orderRow =orderService.findByOrderNo(out_trade_no);
				if(orderRow == null)
				{
					logger.info("订单号不存在:,[order_no]:"+out_trade_no);
					response.getWriter().print("fail");
					return;
				}
				//判断订单是否已正确处理订单状态，如果已处理，则直接返回处理正确响应结果给微信，否则，更改订单状态
				String state =orderRow.getString("state","");
				if(state.equals("2"))
				{
					logger.info("订单号已通过支付宝支付成功:,[order_no]:"+out_trade_no);
					response.getWriter().print("success");
					return;
				}
				Row updateRow =new Row();
				updateRow.put("id", orderRow.getString("id"));
				updateRow.put("state", "2");//0待付款，1已付款未到账，2已到账待收货，3已收货4申请退款5退款未到账，6已退款
				updateRow.put("pay_type", "2");//1微信支付2支付宝支付
				updateRow.put("api_order_no", trade_no);
				updateRow.put("pay_finish_time", gmt_payment);
				String already_paid_money =orderRow.getString("paid_money","0");
				if(StringUtils.isEmptyOrNull(already_paid_money))
				{
					already_paid_money ="0";
				}
				double dalready_paid_money =Double.parseDouble(already_paid_money);
				double dtotal_fee =Double.parseDouble(total_fee);
				double dnew_paid_fee =dalready_paid_money+dtotal_fee;
				String new_paid_fee =String.valueOf(dnew_paid_fee);
				new_paid_fee =NumberUtils.getTwoDecimal(new_paid_fee);
				updateRow.put("paid_money", new_paid_fee);
				int rowNum =orderService.update(updateRow);
				if(rowNum >0)
				{
					response.getWriter().print("success");
					return;
				}
				else
				{
					response.getWriter().print("fail");
					return;
				}
				//注意：
				//该种交易状态只在两种情况下出现
				//1、开通了普通即时到账，买家付款成功后。
				//2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
			} 
//			else if (trade_status.equals("TRADE_SUCCESS")){
//				//判断该笔订单是否在商户网站中已经做过处理
//					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
//					//如果有做过处理，不执行商户的业务程序
//				//注意：
//				//该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
//			}
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
//			out.println("success");	//请不要修改或删除
			//////////////////////////////////////////////////////////////////////////////////////////
		}else{//验证失败
			response.getWriter().print("fail");
			return;
		}
	}
	/**
	 * 爱心捐赠通知
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value ="/notifyLoveDonate")
	public void notifyLoveDonate(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		String out_trade_no = request.getParameter("out_trade_no");
		//支付宝交易号
		String trade_no = request.getParameter("trade_no");
		//交易状态
		String trade_status = request.getParameter("trade_status");
		//订单总金额
		String total_fee = request.getParameter("total_fee");
		//交易付款时间
		String gmt_payment =request.getParameter("gmt_payment");
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		if(AlipayNotify.verify(params)){//验证成功
			logger.info("支付宝的通知返回参数验证成功");
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码
			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS") ){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				//判断订单是否存在
				Row orderRow =commonwealLoveRecordService.findByOrderNo(out_trade_no);
				if(orderRow == null)
				{
					logger.info("订单号不存在:,[order_no]:"+out_trade_no);
					response.getWriter().print("fail");
					return;
				}
				//判断订单是否已正确处理订单状态，如果已处理，则直接返回处理正确响应结果给微信，否则，更改订单状态
				String state =orderRow.getString("pay_state","");
				if(state.equals("2"))
				{
					logger.info("订单号已通过支付宝支付成功:,[order_no]:"+out_trade_no);
					response.getWriter().print("success");
					return;
				}
				Row updateRow =new Row();
				updateRow.put("id", orderRow.getString("id"));
				updateRow.put("pay_state", "2");//0待支付1已支付未到账2支付完成
				updateRow.put("pay_type", "1");//1支付宝2银联
				updateRow.put("pay_order_no", trade_no);
				updateRow.put("pay_finish_time", gmt_payment);
				updateRow.put("money", total_fee);
				int rowNum =commonwealLoveRecordService.update(updateRow);
				if(rowNum >0)
				{
					response.getWriter().print("success");
					return;
				}
				else
				{
					response.getWriter().print("fail");
					return;
				}
				//注意：
				//该种交易状态只在两种情况下出现
				//1、开通了普通即时到账，买家付款成功后。
				//2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
			} 
//			else if (trade_status.equals("TRADE_SUCCESS")){
//				//判断该笔订单是否在商户网站中已经做过处理
//					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
//					//如果有做过处理，不执行商户的业务程序
//				//注意：
//				//该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
//			}
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
//			out.println("success");	//请不要修改或删除
			//////////////////////////////////////////////////////////////////////////////////////////
		}else{//验证失败
			response.getWriter().print("fail");
			return;
		}
	}
	/**
	 * 档案捐赠通知
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value ="/notifyProjectDonate")
	public void notifyProjectDonate(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		String out_trade_no = request.getParameter("out_trade_no");
		//支付宝交易号
		String trade_no = request.getParameter("trade_no");
		//交易状态
		String trade_status = request.getParameter("trade_status");
		//订单总金额
		String total_fee = request.getParameter("total_fee");
		//交易付款时间
		String gmt_payment =request.getParameter("gmt_payment");
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		if(AlipayNotify.verify(params)){//验证成功
			logger.info("支付宝的通知返回参数验证成功");
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码
			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS") ){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				//判断订单是否存在
				Row orderRow =commonwealProjectRecordService.findByOrderNo(out_trade_no);
				if(orderRow == null)
				{
					logger.info("订单号不存在:,[order_no]:"+out_trade_no);
					response.getWriter().print("fail");
					return;
				}
				//判断订单是否已正确处理订单状态，如果已处理，则直接返回处理正确响应结果给微信，否则，更改订单状态
				String state =orderRow.getString("pay_state","");
				if(state.equals("2"))
				{
					logger.info("订单号已通过支付宝支付成功:,[order_no]:"+out_trade_no);
					response.getWriter().print("success");
					return;
				}
				Row updateRow =new Row();
				updateRow.put("id", orderRow.getString("id"));
				updateRow.put("pay_state", "2");//0待支付1已支付未到账2支付完成
				updateRow.put("pay_type", "1");//1支付宝2银联
				updateRow.put("pay_order_no", trade_no);
				updateRow.put("pay_finish_time", gmt_payment);
				updateRow.put("money", total_fee);
				int rowNum =commonwealProjectRecordService.update(updateRow);
				if(rowNum >0)
				{
					response.getWriter().print("success");
					return;
				}
				else
				{
					response.getWriter().print("fail");
					return;
				}
				//注意：
				//该种交易状态只在两种情况下出现
				//1、开通了普通即时到账，买家付款成功后。
				//2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
			} 
//			else if (trade_status.equals("TRADE_SUCCESS")){
//				//判断该笔订单是否在商户网站中已经做过处理
//					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
//					//如果有做过处理，不执行商户的业务程序
//				//注意：
//				//该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
//			}
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
//			out.println("success");	//请不要修改或删除
			//////////////////////////////////////////////////////////////////////////////////////////
		}else{//验证失败
			response.getWriter().print("fail");
			return;
		}
	}
	/**
	 * 项目捐赠通知
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value ="/notifyArchiveDonate")
	public void notifyArchiveDonate(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		String out_trade_no = request.getParameter("out_trade_no");
		//支付宝交易号
		String trade_no = request.getParameter("trade_no");
		//交易状态
		String trade_status = request.getParameter("trade_status");
		//订单总金额
		String total_fee = request.getParameter("total_fee");
		//交易付款时间
		String gmt_payment =request.getParameter("gmt_payment");
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		if(AlipayNotify.verify(params)){//验证成功
			logger.info("支付宝的通知返回参数验证成功");
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码
			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS") ){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				//判断订单是否存在
				Row orderRow =commonwealArchiveRecordService.findByOrderNo(out_trade_no);
				if(orderRow == null)
				{
					logger.info("订单号不存在:,[order_no]:"+out_trade_no);
					response.getWriter().print("fail");
					return;
				}
				//判断订单是否已正确处理订单状态，如果已处理，则直接返回处理正确响应结果给微信，否则，更改订单状态
				String state =orderRow.getString("pay_state","");
				if(state.equals("2"))
				{
					logger.info("订单号已通过支付宝支付成功:,[order_no]:"+out_trade_no);
					response.getWriter().print("success");
					return;
				}
				Row updateRow =new Row();
				updateRow.put("id", orderRow.getString("id"));
				updateRow.put("pay_state", "2");//0待支付1已支付未到账2支付完成
				updateRow.put("pay_type", "1");//1支付宝2银联
				updateRow.put("pay_order_no", trade_no);
				updateRow.put("pay_finish_time", gmt_payment);
				updateRow.put("money", total_fee);
				int rowNum =commonwealLoveRecordService.update(updateRow);
				if(rowNum >0)
				{
					response.getWriter().print("success");
					return;
				}
				else
				{
					response.getWriter().print("fail");
					return;
				}
				//注意：
				//该种交易状态只在两种情况下出现
				//1、开通了普通即时到账，买家付款成功后。
				//2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
			} 
//			else if (trade_status.equals("TRADE_SUCCESS")){
//				//判断该笔订单是否在商户网站中已经做过处理
//					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
//					//如果有做过处理，不执行商户的业务程序
//				//注意：
//				//该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
//			}
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
//			out.println("success");	//请不要修改或删除
			//////////////////////////////////////////////////////////////////////////////////////////
		}else{//验证失败
			response.getWriter().print("fail");
			return;
		}
	}
}
