package com.hslg.controller.app;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ezcloud.framework.controller.BaseController;
import com.hslg.service.RuleMessageService;

@Controller("hslgSiteProMsgController")
public class RuleMessagePageContrller  extends BaseController{
	
	@Resource(name = "hslgRuleMessageService")
	private RuleMessageService ruleMessageService;


	@RequestMapping(value = "/app/front/msg")
	public String edit(String id, ModelMap model) {
		Assert.notNull(id);
		model.addAttribute("row", ruleMessageService.findById(id));
		return "/hslgpage/app/front/share/share";
	}
}