package com.hslg.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.HtmlUtils;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.hslg.service.CommonwealLoveRecordService;
import com.hslg.service.CommonwealLoveService;
import com.hslg.service.UserTokenService;

@Controller("mobileConvenientLoveController")
@RequestMapping("/api/convenient/love/profile")
public class ConvenientLoveController extends BaseController {
	
	private static Logger logger = Logger.getLogger(ConvenientLoveController.class); 
	
	@Resource(name = "hslgCommonwealLoveService")
	private CommonwealLoveService commonwealLoveService;
	
	@Resource(name = "hslgCommonwealLoveRecordService")
	private CommonwealLoveRecordService commonwealLoveRecordService;
	
	@Resource(name = "hslgUserTokenService")
	private UserTokenService userTokenService;
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryAllAd(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("爱心捐赠列表");
		String user_id =ivo.getString("user_id","");
		DataSet ds =commonwealLoveService.list();
		Row row =null;
		String total_num ="0";
		String total_money ="0";
		String user_buy_money ="0";
		String user_donate_money ="0";
		if(ds != null)
		{
			row =(Row)ds.get(0);
			total_num =row.getString("num","0");
			total_money =row.getString("money","0");
		}
		if(StringUtils.isEmptyOrNull(user_id))
		{
			double duser_money =commonwealLoveRecordService.getUserDonateMoney(user_id);
			user_donate_money =String.valueOf(duser_money);
		}
		OVO ovo =new OVO(0,"","");
		ovo.set("total_money", total_money);
		ovo.set("total_num", total_num);
		ovo.set("user_buy_money", user_buy_money);
		ovo.set("user_donate_money", user_donate_money);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	@RequestMapping(value ="/notify")
	public @ResponseBody
	String notify(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String order_no =ivo.getString("order_no","");
		String user_id =ivo.getString("user_id","");
		Row tokenRow =userTokenService.find(user_id);
		if(tokenRow == null)
		{
			ovo =new OVO(-20012,"用户未登录","用户未登录");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row orderRow =commonwealLoveRecordService.findByOrderNo(order_no);
		if(orderRow == null)
		{
			ovo =new OVO(-20012,"订单号不存在","订单号不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		orderRow =MapUtils.convertMaptoRowWithoutNullField(orderRow);
		orderRow.put("pay_state", "1");
		commonwealLoveRecordService.update(orderRow);
		OVO ovo =new OVO(0,"通知服务器成功","通知服务器成功");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 查询广告详情
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/find")
	public @ResponseBody
	String findById(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("广告详情");
		String id=ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-10011,"id不能为空","id不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row =commonwealLoveService.find(id);
		if(row == null)
		{
			OVO ovo =new OVO(-10011,"对应的数据不存在","对应的数据不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String content =row.getString("content","");
		Setting setting =SettingUtils.get();
		String siteUrl =setting.getSiteUrl();
		String domain =siteUrl;
		int iPos =siteUrl.lastIndexOf("/");
		if(iPos != -1)
		{
			domain =siteUrl.substring(0,iPos);
		}
		//替换图片标签的url为http全路径
		content =HtmlUtils.fillImgSrcWithDomain(domain, content);
		// 转义字符串中的换行，不然在转成json对象时会报错
		content =StringUtils.string2Json(content);
		OVO ovo =new OVO(0,"","");
		ovo.set("content", content);
		ovo.set("id", id);
		//浏览次数+1
		Row adRow =new Row();
		adRow.put("id", id);
		String view_num =row.getString("view_num","");
		if(StringUtils.isEmptyOrNull(view_num))
		{
			view_num ="0";
		}
		int num =Integer.parseInt(view_num);
		num ++;
		adRow.put("view_num", num);
		commonwealLoveService.update(adRow);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
