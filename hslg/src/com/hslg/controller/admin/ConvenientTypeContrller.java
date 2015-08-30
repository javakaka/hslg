package com.hslg.controller.admin;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import com.hslg.service.ConvenientTypeService;

@Controller("hslgPlatformConvenientTypeController")
@RequestMapping("/hslgpage/platform/commonweal/convenient/type")
public class ConvenientTypeContrller  extends BaseController{

	@Resource(name = "hslgConvenientTypeService")
	private ConvenientTypeService convenientTypeService;
	

	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		convenientTypeService.getRow().put("pageable", pageable);
		Page page = convenientTypeService.queryPage();
		model.addAttribute("page", page);
		convenientTypeService.getRow().clear();
		return "/hslgpage/platform/commonweal/convenient/type/list";
	}
	
	@RequestMapping(value = "/select")
	public String selectUserList(String id,Pageable pageable, ModelMap model) {
		convenientTypeService.getRow().put("pageable", pageable);
		Page page = convenientTypeService.queryPage();
		model.addAttribute("page", page);
		if(StringUtils.isEmptyOrNull(id))
		{
			id ="";
		}
		model.addAttribute("id", id);
		convenientTypeService.getRow().clear();
		return "/hslgpage/platform/commonweal/convenient/type/select";
	}
	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/hslgpage/platform/commonweal/convenient/type/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
//		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) throws Exception {
		return "/hslgpage/platform/commonweal/convenient/type/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		convenientTypeService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(String[] ids) {
		convenientTypeService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
}
