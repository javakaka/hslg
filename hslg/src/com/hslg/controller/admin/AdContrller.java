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
import com.hslg.service.AdService;

@Controller("hslgPlatformAdController")
@RequestMapping("/hslgpage/platform/ad/profile")
public class AdContrller  extends BaseController{

	@Resource(name = "hslgAdService")
	private AdService adService;
	
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		Page page = adService.queryPage(pageable);
		model.addAttribute("page", page);
		adService.getRow().clear();
		return "/hslgpage/platform/ad/profile/list";
	}
	
	/**
	 * 检查广告名称是否已存在
	 */
	@RequestMapping(value = "/check_name", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkAdName(String NAME) {
		if (StringUtils.isEmptyOrNull(NAME)) {
			return false;
		}
		if(adService.isAdNameExisted(NAME))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 检查广告名称是否已存在
	 */
	@RequestMapping(value = "/check_extra_name", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkAdExtraName(String ID,String NAME) {
		if (StringUtils.isEmptyOrNull(ID)) {
			return false;
		}
		if (StringUtils.isEmptyOrNull(NAME)) {
			return false;
		}
		if(adService.isAdNameExisted(ID,NAME))
		{
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/hslgpage/platform/ad/profile/add";
	}	

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		row.put("view_num", "0");
		String start_time =row.getString("start_time","");
		String end_time =row.getString("end_time","");
		if(start_time.length()<=10)
		{
			start_time +=" 00:00:00";
		}
		if(end_time.length()<=10)
		{
			end_time +=" 00:00:00";
		}
		row.put("start_time", start_time);
		row.put("end_time", end_time);
		adService.insert(row);
		addFlashMessage(redirectAttributes, Message.success("添加成功"));
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) {
		Assert.notNull(id);
		model.addAttribute("row", adService.findById(id));
		return "/hslgpage/platform/ad/profile/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row adRow=MapUtils.convertMaptoRowWithoutNullField(map);
//		String content =adRow.getString("content","");
//		content =content.replaceAll("\"", "&quot;");
//		adRow.put("content", content);
		String start_time =adRow.getString("start_time","");
		String end_time =adRow.getString("end_time","");
		if(start_time.length()<=10)
		{
			start_time +=" 00:00:00";
		}
		if(end_time.length()<=10)
		{
			end_time +=" 00:00:00";
		}
		adRow.put("start_time", start_time);
		adRow.put("end_time", end_time);
		adService.update(adRow);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		adService.delete(ids);
		return SUCCESS_MESSAGE;
	}
}
