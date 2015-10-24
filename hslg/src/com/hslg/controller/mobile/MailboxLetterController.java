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
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.hslg.service.UserLetterService;

/**
 * 用户消息
 * @author Administrator
 *
 */
@Controller("mobileMailboxLetterController")
@RequestMapping("/api/mailbox/userletter")
public class MailboxLetterController extends BaseController {
	
	private static Logger logger = Logger.getLogger(MailboxLetterController.class); 
	

	@Resource(name = "hslgUserLetterService")
	private UserLetterService userLetterService;
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String page(HttpServletRequest request) throws Exception
	{
		logger.info("get sysinfo page");
		parseRequest(request);
		OVO ovo =null;
		String user_id =ivo.getString("user_id","");
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-10001,"用户ID不能为空","用户ID不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet ds =userLetterService.list(user_id,page,page_size);
		ovo =new OVO(0,"","");
		ovo.set("list", ds);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 详情
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/detail")
	public @ResponseBody
	String add(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String id=ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-11000,"id不能为空","id不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row =userLetterService.findById(id);
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
		Row updateRow =new Row();
		updateRow.put("id", id);
		updateRow.put("read_status", "1");
		userLetterService.update(updateRow);
		ovo =new OVO(0,"操作成功","操作成功");
		ovo.set("content", content);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
