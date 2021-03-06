package com.hslg.controller.admin;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.vo.Row;
import com.hslg.service.MailboxSysInfoService;

@Controller("hslgPlatformMailboxSysInfoController")
@RequestMapping("/hslgpage/platform/mailbox/sysinfo")
public class MailboxSysInfoContrller  extends BaseController{
	
	@Resource(name = "hslgMailboxSysInfoService")
	private MailboxSysInfoService mailboxSysInfoService;
	
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		Page page = mailboxSysInfoService.queryPage(pageable);
		model.addAttribute("page", page);
		mailboxSysInfoService.getRow().clear();
		return "/hslgpage/platform/mailbox/sysinfo/list";
	}
	
	
	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/hslgpage/platform/mailbox/sysinfo/add";
	}	

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		mailboxSysInfoService.insert(row);
		addFlashMessage(redirectAttributes, Message.success("添加成功"));
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) {
		Assert.notNull(id);
		model.addAttribute("row", mailboxSysInfoService.findById(id));
		return "/hslgpage/platform/mailbox/sysinfo/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row adRow=MapUtils.convertMaptoRowWithoutNullField(map);
		mailboxSysInfoService.update(adRow);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		mailboxSysInfoService.delete(ids);
		return SUCCESS_MESSAGE;
	}
}
