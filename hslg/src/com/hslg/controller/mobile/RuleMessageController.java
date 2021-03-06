package com.hslg.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.hslg.service.RuleMessageService;

@Controller("mobileRuleMessageController")
@RequestMapping("/api/promsg")
public class RuleMessageController extends BaseController {
	
	private static Logger logger = Logger.getLogger(RuleMessageController.class); 
	
	@Resource(name = "hslgRuleMessageService")
	private RuleMessageService ruleMessageService;

	/**
	 * 查询关于我们
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/about_us")
	public @ResponseBody
	String queryAboutUs(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("关于我们");
		String id="3";
		Row row =ruleMessageService.findById(id);
		String content =row.getString("content","");
		// 转义字符串中的换行，不然在转成json对象时会报错
		content =StringUtils.string2Json(content);
		OVO ovo =new OVO(0,"","");
		ovo.set("content", content);
		//浏览次数+1
		Row tempRow =new Row();
		tempRow.put("id", id);
		String view_num =tempRow.getString("view_num","");
		if(StringUtils.isEmptyOrNull(view_num))
		{
			view_num ="0";
		}
		int num =Integer.parseInt(view_num);
		num ++;
		tempRow.put("view_num", num);
		ruleMessageService.update(tempRow);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**
	 * 查询免责申明
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/statement")
	public @ResponseBody
	String queryStatement(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("查询免责申明");
		String id="4";
		Row row =ruleMessageService.findById(id);
		String content =row.getString("content","");
		// 转义字符串中的换行，不然在转成json对象时会报错
		content =StringUtils.string2Json(content);
		OVO ovo =new OVO(0,"","");
		ovo.set("content", content);
		//浏览次数+1
		Row tempRow =new Row();
		tempRow.put("id", id);
		String view_num =tempRow.getString("view_num","");
		if(StringUtils.isEmptyOrNull(view_num))
		{
			view_num ="0";
		}
		int num =Integer.parseInt(view_num);
		num ++;
		tempRow.put("view_num", num);
		ruleMessageService.update(tempRow);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 查询房东租客的二维码分享信息
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/core_qrcode")
	public @ResponseBody
	String queryCoreQrCode(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("查询房东租客的二维码分享信息");
		String id="1";
		Row row =ruleMessageService.findById(id);
		String content =row.getString("content","");
		// 转义字符串中的换行，不然在转成json对象时会报错
		content =StringUtils.string2Json(content);
		OVO ovo =new OVO(0,"","");
		ovo.set("content", content);
		//浏览次数+1
		Row tempRow =new Row();
		tempRow.put("id", id);
		String view_num =tempRow.getString("view_num","");
		if(StringUtils.isEmptyOrNull(view_num))
		{
			view_num ="0";
		}
		int num =Integer.parseInt(view_num);
		num ++;
		tempRow.put("view_num", num);
		ruleMessageService.update(tempRow);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 查询中介版二维码分享信息
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/agent_qrcode")
	public @ResponseBody
	String queryAgentQrCode(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("查询中介版二维码分享信息");
		String id="2";
		Row row =ruleMessageService.findById(id);
		String content =row.getString("content","");
		// 转义字符串中的换行，不然在转成json对象时会报错
		content =StringUtils.string2Json(content);
		OVO ovo =new OVO(0,"","");
		ovo.set("content", content);
		//浏览次数+1
		Row tempRow =new Row();
		tempRow.put("id", id);
		String view_num =tempRow.getString("view_num","");
		if(StringUtils.isEmptyOrNull(view_num))
		{
			view_num ="0";
		}
		int num =Integer.parseInt(view_num);
		num ++;
		tempRow.put("view_num", num);
		ruleMessageService.update(tempRow);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

	/**
	 * 查询分享的图片与标题
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/share")
	public @ResponseBody
	String queryShareMsg(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("查询分享的图片与标题");
		String id="5";
		Row row =ruleMessageService.findById(id);
		String title =row.getString("title","");
		String icon_url =row.getString("icon_url","");
		Setting setting =SettingUtils.get();
		String siteUrl =setting.getSiteUrl();
		String serverUrl =setting.getSiteUrl()+"/app/front/msg.do?id=5";
		if(!StringUtils.isEmptyOrNull(siteUrl))
		{
			siteUrl =siteUrl.replace("hslg", "");
		}
		icon_url =siteUrl+icon_url;
		OVO ovo =new OVO(0,"","");
		ovo.set("title", title);
		ovo.set("icon_url", icon_url);
		ovo.set("server_url", serverUrl);
		//浏览次数+1
		Row tempRow =new Row();
		tempRow.put("id", id);
		String view_num =tempRow.getString("view_num","");
		if(StringUtils.isEmptyOrNull(view_num))
		{
			view_num ="0";
		}
		int num =Integer.parseInt(view_num);
		num ++;
		tempRow.put("view_num", num);
		ruleMessageService.update(tempRow);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
}
