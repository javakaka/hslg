package com.hslg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.plugin.pay.Unifiedorder;
import com.ezcloud.framework.plugin.pay.WeixinPreOrderResult;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.FieldUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.util.WeixinUtil;
import com.ezcloud.framework.util.XmlUtil;
import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;
import com.ezcloud.utility.StringUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 */
@Component("hslgWeiXinAppPayService")
public class WeiXinAppPayService extends Service {

	private static Logger logger = Logger.getLogger(WeiXinAppPayService.class);
	
	//商家key API 密钥
	private static final String SHOP_KEY ="haoshanlegouhaoshanlegou2015tong";
	@Resource(name = "hslgUserService")
	private UserService userService;
	
	@Resource(name = "hslgUserTokenService")
	private UserTokenService userTokenService;
	
	@Resource(name = "hslgCommonwealLoveRecordService")
	private CommonwealLoveRecordService orderService;
	
	public WeiXinAppPayService() 
	{
		
	}
	
/**
 * 字段名		变量名			必填	类型		示例值					描述
公众账号ID		appid			是	String(32)	wx8888888888888888			微信分配的公众账号ID
商户号		mch_id			是	String(32)	1900000109				微信支付分配的商户号
设备号		device_info		否	String(32)	013467007045764				终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
随机字符串		nonce_str		是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
签名			sign			是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
商品描述		body			是	String(32)	Ipad mini  16G  白色		商品或支付单简要描述
商品详情		detail			否	String(8192)	Ipad mini  16G  白色		商品名称明细列表
附加数据		attach			否	String(127)	说明	附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
商户订单号		out_trade_no	是	String(32)	1217752501201407033233368018	商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
货币类型		fee_type		否	String(16)	CNY	符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
总金额		total_fee		是	Int	888	订单总金额，只能为整数，详见支付金额
终端IP		spbill_create_ip是	String(16)	8.8.8.8	APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
交易起始时间	time_start		否	String(14)	20091225091010	订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
交易结束时间	time_expire		否	String(14)	20091227091010	订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则
注意：最短失效时间间隔必须大于5分钟
商品标记		goods_tag		否	String(32)	WXG		商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
通知地址		notify_url		是	String(256)	http://www.baidu.com/	接收微信支付异步通知回调地址
交易类型		trade_type		是	String(16)	JSAPI	取值如下：JSAPI，NATIVE，APP，WAP,详细说明见参数规定
商品ID		product_id		否	String(32)	12235413214070356458058	trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
用户标识		openid			否	String(128)	oUpF8uMuAJO_M2pxb1Q9zNjWeS6o	trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
 * 
 * 共10个必填字段
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * @param ivo
 * @return
 * @throws Exception 
 */
	public OVO validate(IVO ivo) throws Exception
	{
		OVO ovo =new OVO();
		String user_id =ivo.getString("user_id");
		String app_ip =ivo.getString("app_ip");
		String device =ivo.getString("device");
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
		String money =ivo.getString("money","0");
		if(StringUtils.isEmptyOrNull(money))
		{
			ovo =new OVO(-20011,"捐赠金额不能为空","捐赠金额不能为空");
			return ovo;
		}
		double dmoney =Double.parseDouble(money);
		if(dmoney == 0)
		{
			ovo =new OVO(-20011,"捐赠金额不能为0","捐赠金额不能为0");
			return ovo;
		}
		String order_no="";
		String cur_time =DateUtil.getCurrentDateTime().replace(" ", "").replace(":", "").replace("-", "");
		Random random =new Random(100);
		int rand_int =random.nextInt(1000);
		//倒数第三第四为定义：01 爱心捐赠02善小档案捐赠03公益项目捐赠
		order_no =cur_time+"01"+String.valueOf(rand_int);
		Row orderRow =new Row();
		orderRow.put("user_id", user_id);
		orderRow.put("love_id", "1");
		orderRow.put("order_no", order_no);
		orderRow.put("donate_date", DateUtil.getCurrentDate());
		orderRow.put("money", money);
		orderRow.put("pay_state", "0");
		orderRow.put("pay_type", "3");
		orderService.insert(orderRow);
		String order_id =orderRow.getString("id");
		Unifiedorder unifiiedorder =new Unifiedorder();
		unifiiedorder.setAppid("wxd0283113ebc9baac");
		unifiiedorder.setMch_id("1287207001");
		String nonce_str =StringUtil.getRandKeys(28).toUpperCase();
		unifiiedorder.setNonce_str(nonce_str);
		String sign ="";
//		String body ="订单:"+order_no+"支付备注";
		String body ="好善乐购爱心捐赠订单:"+order_no;
		String out_trade_no =order_no;
		unifiiedorder.setOut_trade_no(out_trade_no);
		unifiiedorder.setBody(body);
		String fee_type ="CNY";
		unifiiedorder.setFee_type(fee_type);
		String total_fee ="0";
		dmoney =dmoney*100;//(分)
		total_fee =StringUtils.subStrBeforeDotNotIncludeDot(String.valueOf(dmoney));
		unifiiedorder.setTotal_fee(total_fee);
		String spbill_create_ip=app_ip;
		unifiiedorder.setSpbill_create_ip(spbill_create_ip);
		Setting setting =SettingUtils.get();
		String site_url =setting.getSiteUrl();
		String notify_url =site_url+"/api/pay/weixin/app/notify.do";
		notify_url +="?order_no="+AesUtil.encode(order_no);
		unifiiedorder.setNotify_url(notify_url);
		String trade_type ="APP";
		unifiiedorder.setTrade_type(trade_type);
		Row row =FieldUtil.getObjectNotEmptyFieldsUrlParamsStr(unifiiedorder, SHOP_KEY);
		String xml =row.getString("xml","");
		logger.info("发送给统一下单接口的xml数据＝＝＝＝＝＝>>"+xml);
		String resp =WeixinUtil.createPreOrder(xml);
		logger.info("统一下单接口返回的xml数据＝＝＝＝＝＝>>"+resp);
		ovo =parsePreOrderResult(resp,device);
		if(ovo.iCode<0)
		{
			return ovo;
		}
		//预付单处理成功 设置订单的状态0待支付1已支付未到账2支付完成
		Row updateRow =new Row();
		updateRow.put("id", order_id);
		updateRow.put("pay_state", "1");
		orderService.update(updateRow);
		logger.info("APP调起微信支付的数据＝＝＝＝＝＝>>"+ovo.oForm);
		return ovo;
	}
	
	public OVO parsePreOrderResult(String xml,String device) throws JException
	{
		OVO ovo =null;
		WeixinPreOrderResult result =null;
		HashMap map=XmlUtil.xmlToMap(xml);
		Row resultRow =MapUtils.convertMaptoRowWithoutNullField(map);
		String return_code =resultRow.getString("return_code","");
		String return_msg =resultRow.getString("return_msg","");
		String appid =resultRow.getString("appid","");
		String mch_id =resultRow.getString("mch_id","");
		String nonce_str =resultRow.getString("nonce_str","");
		String sign =resultRow.getString("sign","");
		String result_code =resultRow.getString("result_code","");
		String err_code =resultRow.getString("err_code","");
		String err_code_desc =resultRow.getString("err_code_desc","");
		String prepay_id =resultRow.getString("prepay_id","");
		String trade_type =resultRow.getString("trade_type","");
		//调用预付单成功
		if(!StringUtils.isEmptyOrNull(return_code) && !StringUtils.isEmptyOrNull(result_code)
		&& return_code.equalsIgnoreCase("SUCCESS") && result_code.equalsIgnoreCase("SUCCESS"))
		{
			ovo =new OVO(0,"预付单处理成功","预付单处理成功");
			ovo.set("result", "success");
			ArrayList list = new ArrayList();
			list.add("appid");
			list.add("partnerid");
			list.add("prepayid");
			list.add("noncestr");
			list.add("timestamp");
			list.add("package");
			Row dataRow =new Row();
			dataRow.put("appid", appid);
			dataRow.put("partnerid", mch_id);
			dataRow.put("prepayid", prepay_id);
			dataRow.put("noncestr", nonce_str);
			long time =System.currentTimeMillis()/1000;
			dataRow.put("timestamp", time);
			if(device.equals("1"))
			{
				dataRow.put("package", "prepay_id="+prepay_id);
			}
			else if(device.equals("2"))
			{
				dataRow.put("package", "Sign=WXPay");
			}
//			dataRow.put("package", "Sign=WXPay");
			String new_sign =FieldUtil.getWeixinRequestSign(list, dataRow, SHOP_KEY);
			ovo.set("appId", appid);
			ovo.set("partnerId", mch_id);
			ovo.set("prepayId", prepay_id);
			ovo.set("nonceStr", nonce_str);
			ovo.set("timeStamp", time);
			if(device.equals("1"))
			{
				ovo.set("package", "prepay_id="+prepay_id);
			}
			else if(device.equals("2"))
			{
				ovo.set("package", "Sign=WXPay");
			}
//			ovo.set("package", "Sign=WXPay");
			ovo.set("sign", new_sign);
		}
		else
		{
			ovo =new OVO(-10030,"预付单处理失败","预付单处理失败");
			ovo.set("result", "fail");
			ovo.set("err_code", err_code);
			ovo.set("err_code_desc", err_code_desc);
		}
		return ovo;
	}
}