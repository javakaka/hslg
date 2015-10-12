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
import com.hslg.service.MailboxSysBroadcastService;

@Controller("hslgPlatformMailboxSysBroadcastController")
@RequestMapping("/hslgpage/platform/mailbox/sysbroadcast")
public class MailboxSysBroadcastContrller  extends BaseController{
	
	@Resource(name = "hslgMailboxSysBroadcastService")
	private MailboxSysBroadcastService mailboxSysBroadcastService;
	
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		Page page = mailboxSysBroadcastService.queryPage(pageable);
		model.addAttribute("page", page);
		mailboxSysBroadcastService.getRow().clear();
		return "/hslgpage/platform/mailbox/sysbroadcast/list";
	}
	
	
	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/hslgpage/platform/mailbox/sysbroadcast/add";
	}	

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		mailboxSysBroadcastService.insert(row);
		addFlashMessage(redirectAttributes, Message.success("添加成功"));
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) {
		Assert.notNull(id);
		model.addAttribute("row", mailboxSysBroadcastService.findById(id));
		return "/hslgpage/platform/mailbox/sysbroadcast/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row adRow=MapUtils.convertMaptoRowWithoutNullField(map);
		mailboxSysBroadcastService.update(adRow);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		mailboxSysBroadcastService.delete(ids);
		return SUCCESS_MESSAGE;
	}
}
