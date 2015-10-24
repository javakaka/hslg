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
import com.hslg.service.CommonServiceTypeService;

@Controller("hslgPlatformCommonServiceTypeController")
@RequestMapping("/hslgpage/platform/commonweal/commonservice/type")
public class CommonServiceTypeContrller  extends BaseController{

	@Resource(name = "hslgCommonServiceTypeService")
	private CommonServiceTypeService commonServiceTypeService;
	
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		commonServiceTypeService.getRow().put("pageable", pageable);
		Page page = commonServiceTypeService.queryPage();
		model.addAttribute("page", page);
		commonServiceTypeService.getRow().clear();
		return "/hslgpage/platform/commonweal/commonservice/type/list";
	}
	
	@RequestMapping(value = "/select")
	public String selectUserList(String id,Pageable pageable, ModelMap model) {
		commonServiceTypeService.getRow().put("pageable", pageable);
		Page page = commonServiceTypeService.queryPage();
		model.addAttribute("page", page);
		if(StringUtils.isEmptyOrNull(id))
		{
			id ="";
		}
		model.addAttribute("id", id);
		commonServiceTypeService.getRow().clear();
		return "/hslgpage/platform/commonweal/commonservice/type/select";
	}
	@RequestMapping(value = "/add")
	public String add( ModelMap model) {
		return "/hslgpage/platform/commonweal/commonservice/type/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map, ModelMap model,RedirectAttributes redirectAttributes) {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		commonServiceTypeService.insert(row);
		addFlashMessage(redirectAttributes, Message.success("操作成功"));
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) throws Exception {
		Assert.notNull(id);
		Row row =commonServiceTypeService.find(id);
		model.addAttribute("row", row);
		return "/hslgpage/platform/commonweal/commonservice/type/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		commonServiceTypeService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(String[] ids) {
		commonServiceTypeService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
}
