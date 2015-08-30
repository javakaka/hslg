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
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.Row;
import com.hslg.service.ConvenientInfoService;

@Controller("hslgPlatformConvenientInfoController")
@RequestMapping("/hslgpage/platform/commonweal/convenient/profile")
public class ConvenientInfoContrller  extends BaseController{

	@Resource(name = "hslgConvenientInfoService")
	private ConvenientInfoService convenientInfoService;
	

	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		convenientInfoService.getRow().put("pageable", pageable);
		Page page = convenientInfoService.queryPage();
		model.addAttribute("page", page);
		convenientInfoService.getRow().clear();
		return "/hslgpage/platform/commonweal/convenient/profile/list";
	}
	
	@RequestMapping(value = "/select")
	public String selectUserList(String id,Pageable pageable, ModelMap model) {
		convenientInfoService.getRow().put("pageable", pageable);
		Page page = convenientInfoService.queryPage();
		model.addAttribute("page", page);
		if(StringUtils.isEmptyOrNull(id))
		{
			id ="";
		}
		model.addAttribute("id", id);
		convenientInfoService.getRow().clear();
		return "/hslgpage/platform/commonweal/convenient/profile/select";
	}
	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/hslgpage/platform/commonweal/convenient/profile/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public  String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		row.put("view_num", "0");
		convenientInfoService.insert(row);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) throws Exception {
		Assert.notNull(id, "id null");
		Row row =convenientInfoService.find(id);
		model.addAttribute("row", row);
		return "/hslgpage/platform/commonweal/convenient/profile/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		convenientInfoService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(String[] ids) {
		convenientInfoService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
}
