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
import com.hslg.service.CommonwealArchiveService;

@Controller("cxhlPlatformConvenientTypeController")
@RequestMapping("/hslgpage/platform/commonweal/archives/profile")
public class CommonwealArchiveContrller  extends BaseController{

	@Resource(name = "hslgCommonwealArchiveService")
	private CommonwealArchiveService commonwealArchiveService;
	

	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		commonwealArchiveService.getRow().put("pageable", pageable);
		Page page = commonwealArchiveService.queryPage();
		model.addAttribute("page", page);
		commonwealArchiveService.getRow().clear();
		return "/hslgpage/platform/commonweal/archives/profile/list";
	}
	
	@RequestMapping(value = "/select")
	public String selectUserList(String id,Pageable pageable, ModelMap model) {
		commonwealArchiveService.getRow().put("pageable", pageable);
		Page page = commonwealArchiveService.queryPage();
		model.addAttribute("page", page);
		if(StringUtils.isEmptyOrNull(id))
		{
			id ="";
		}
		model.addAttribute("id", id);
		commonwealArchiveService.getRow().clear();
		return "/hslgpage/platform/commonweal/archives/profile/select";
	}
	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/hslgpage/platform/commonweal/archives/profile/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		commonwealArchiveService.insert(row);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) throws Exception {
		Assert.notNull(id);
		Row row =commonwealArchiveService.find(id);
		model.addAttribute("row",row);
		return "/hslgpage/platform/commonweal/archives/profile/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		commonwealArchiveService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(String[] ids) {
		commonwealArchiveService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
}
