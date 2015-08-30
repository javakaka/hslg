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
import com.hslg.service.CommonwealProjectService;

@Controller("hslgPlatformCommonwealProjectController")
@RequestMapping("/hslgpage/platform/commonweal/project/profile")
public class CommonwealProjectContrller  extends BaseController{

	@Resource(name = "hslgCommonwealProjectService")
	private CommonwealProjectService commonwealProjectService;

	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		commonwealProjectService.getRow().put("pageable", pageable);
		Page page = commonwealProjectService.queryPage();
		model.addAttribute("page", page);
		commonwealProjectService.getRow().clear();
		return "/hslgpage/platform/commonweal/project/profile/list";
	}
	
	@RequestMapping(value = "/select")
	public String selectUserList(String id,Pageable pageable, ModelMap model) {
		commonwealProjectService.getRow().put("pageable", pageable);
		Page page = commonwealProjectService.queryPage();
		model.addAttribute("page", page);
		if(StringUtils.isEmptyOrNull(id))
		{
			id ="";
		}
		model.addAttribute("id", id);
		commonwealProjectService.getRow().clear();
		return "/hslgpage/platform/commonweal/project/profile/select";
	}
	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/hslgpage/platform/commonweal/project/profile/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		commonwealProjectService.insert(row);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) throws Exception {
		Row row =commonwealProjectService.find(id);
		model.addAttribute("row",row);
		return "/hslgpage/platform/commonweal/project/profile/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		commonwealProjectService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(String[] ids) {
		commonwealProjectService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
}
