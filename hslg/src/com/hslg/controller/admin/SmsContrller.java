package com.hslg.controller.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.service.system.SystemConfigService;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Controller("hslgPlatformSmsController")
@RequestMapping("/hslgpage/platform/setting/sms")
public class SmsContrller  extends BaseController{

	@Resource(name = "frameworkSystemConfigService")
	private SystemConfigService systemConfigService;
	
	/**
	 * 查询短信接口配置信息
	 * @return
	 */
	@RequestMapping(value = "/SmsSetting")
	public String list(ModelMap model) {
		DataSet ds =systemConfigService.getConfigData("APP_SMS_INTERFACE");
		String url ="";
		String username ="";
		String password ="";
		String sms_switch ="";
		String cgid ="";
		String csid ="";
		String busi_code ="";
		if( ds != null && ds.size()>0 )
		{
			for(int i=0; i< ds.size(); i++)
			{
				Row row =(Row)ds.get(i);
				busi_code =row.getString("busi_code","");
				if(busi_code.equals("URL"))
				{
					url =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("USERNAME"))
				{
					username =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("PASSWORD"))
				{
					password =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("SWITCH"))
				{
					sms_switch =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("CGID"))
				{
					cgid =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("CSID"))
				{
					csid =row.getString("busi_code_set","");
				}
			}
		}
		model.addAttribute("url", url);
		model.addAttribute("username", username);
		model.addAttribute("password", password);
		model.addAttribute("sms_switch", sms_switch);
		model.addAttribute("cgid", cgid);
		model.addAttribute("csid", csid);
		model.addAttribute("busi_type", "APP_SMS_INTERFACE");
		return "/hslgpage/platform/setting/sms/SmsSetting";
	}
	
	
	@RequestMapping(value = "/SaveSmsSetting")
	public String save(String url, String username,String password,String sms_switch,
			String cgid,String csid,ModelMap model,RedirectAttributes redirectAttributes) {
		Assert.notNull(url, "url 不能为空");
		Assert.notNull(username, "username 不能为空");
		Assert.notNull(password, "password 不能为空");
		Assert.notNull(sms_switch, "是否开放短信注册不能为空");
		Assert.notNull(cgid, "通道组编号不能为空");
		Assert.notNull(csid, "默认使用的签名编号(未指定签名编号时传此值到服务器)");
		String busi_type="APP_SMS_INTERFACE";
		systemConfigService.setConfigData(busi_type,"URL",url,"发送短信的url");
		systemConfigService.setConfigData(busi_type,"USERNAME",username,"短信接口用户名");
		systemConfigService.setConfigData(busi_type,"PASSWORD",password,"短信接口用户密码");
		systemConfigService.setConfigData(busi_type,"SWITCH",sms_switch,"是否开放短信注册1开放0关闭");
		systemConfigService.setConfigData(busi_type,"CGID",cgid,"通道组编号");
		systemConfigService.setConfigData(busi_type,"CSID",csid,"默认使用的签名编号(未指定签名编号时传此值到服务器)");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:SmsSetting.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		return SUCCESS_MESSAGE;
	}
}
