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
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.hslg.service.CommonServiceService;
import com.hslg.service.CommonServiceTypeService;

@Controller("hslgPlatformCommonServiceController")
@RequestMapping("/hslgpage/platform/commonweal/commonservice/profile")
public class CommonServiceContrller  extends BaseController{

	@Resource(name = "hslgCommonServiceService")
	private CommonServiceService commonServiceService;

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
		commonServiceService.getRow().put("pageable", pageable);
		Page page = commonServiceService.queryPage();
		model.addAttribute("page", page);
		commonServiceService.getRow().clear();
		return "/hslgpage/platform/commonweal/commonservice/profile/list";
	}
	
	@RequestMapping(value = "/select")
	public String selectUserList(String id,Pageable pageable, ModelMap model) {
		commonServiceService.getRow().put("pageable", pageable);
		Page page = commonServiceService.queryPage();
		model.addAttribute("page", page);
		if(StringUtils.isEmptyOrNull(id))
		{
			id ="";
		}
		model.addAttribute("id", id);
		commonServiceService.getRow().clear();
		return "/hslgpage/platform/commonweal/commonservice/profile/select";
	}
	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		//分类列表
		DataSet type_list =commonServiceTypeService.list();
		model.addAttribute("type_list", type_list);
		return "/hslgpage/platform/commonweal/commonservice/profile/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public  String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		row.put("view_num", "0");
		commonServiceService.insert(row);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) throws Exception {
		Assert.notNull(id, "id null");
		Row row =commonServiceService.find(id);
		model.addAttribute("row", row);
		//分类列表
		DataSet type_list =commonServiceTypeService.list();
		model.addAttribute("type_list", type_list);
		return "/hslgpage/platform/commonweal/commonservice/profile/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		commonServiceService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(String[] ids) {
		commonServiceService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
}
