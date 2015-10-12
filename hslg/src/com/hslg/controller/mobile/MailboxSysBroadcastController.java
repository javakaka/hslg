package com.hslg.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.hslg.service.MailboxSysBroadcastService;

/**
 * 广播
 * @author Administrator
 *
 */
@Controller("mobileMailboxSysBroadcastController")
@RequestMapping("/api/mailbox/sysbroadcast")
public class MailboxSysBroadcastController extends BaseController {
	

	@Resource(name = "hslgMailboxSysBroadcastService")
	private MailboxSysBroadcastService mailboxSysBroadcastService;
	
	/**
	 * 根据用户编号分页查询反馈记录
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value ="/list")
	public @ResponseBody
	String page(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		OVO ovo =null;
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet ds =mailboxSysBroadcastService.list(page,page_size);
		if(ds != null && ds.size() > 0 )
		{
			Setting setting =SettingUtils.get();
			String site_url =setting.getSiteUrl();
			for(int i=0;i<ds.size(); i++ )
			{
				Row temp =(Row)ds.get(i);
				String icon_url =temp.getString("icon_url","");
				if(! StringUtils.isEmptyOrNull(icon_url))
				{
					icon_url =icon_url.replace("/hslg", "");
					icon_url =site_url+icon_url;
					temp.put("icon_url", icon_url);
					ds.set(i, temp);
				}
			}
		}
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
		Row row =mailboxSysBroadcastService.findById(id);
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
		ovo =new OVO(0,"操作成功","操作成功");
		ovo.set("content", content);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
