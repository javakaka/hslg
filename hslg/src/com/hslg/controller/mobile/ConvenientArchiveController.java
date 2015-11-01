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
import com.hslg.service.CommonwealArchiveRecordService;
import com.hslg.service.CommonwealArchiveService;
import com.hslg.service.UserTokenService;

@Controller("mobileConvenientArchiveController")
@RequestMapping("/api/convenient/archive/profile")
public class ConvenientArchiveController extends BaseController {
	
	private static Logger logger = Logger.getLogger(ConvenientArchiveController.class); 
	
	@Resource(name = "hslgCommonwealArchiveService")
	private CommonwealArchiveService commonwealArchiveService;
	
	@Resource(name = "hslgCommonwealArchiveRecordService")
	private CommonwealArchiveRecordService commonwealArchiveRecordService;
	
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
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet ds =commonwealArchiveService.list(page, page_size);
		OVO ovo =new OVO(0,"","");
		ovo.set("list", ds);
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
		Row orderRow =commonwealArchiveRecordService.findByOrderNo(order_no);
		if(orderRow == null)
		{
			ovo =new OVO(-20012,"订单号不存在","订单号不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		orderRow =MapUtils.convertMaptoRowWithoutNullField(orderRow);
		String pay_state =orderRow.getString("pay_state","-1");
		if(pay_state.equals("2"))
		{
			OVO ovo =new OVO(0,"通知服务器成功","通知服务器成功");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		orderRow.put("pay_state", "1");
		commonwealArchiveRecordService.update(orderRow);
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
		Row row =commonwealArchiveService.find(id);
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
		commonwealArchiveService.update(adRow);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
