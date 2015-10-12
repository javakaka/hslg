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
import com.ezcloud.framework.util.Md5Util;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.ResponseVO;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;
import com.ezcloud.utility.StringUtil;
import com.hslg.service.UserCollectionService;
import com.hslg.service.UserService;

@Controller("cxhlPlatformUserCollectionController")
@RequestMapping("/hslgpage/platform/member/collection")
public class UserCollectionContrller  extends BaseController{

	@Resource(name = "hslgUserService")
	private UserService userService;
	
	@Resource(name = "hslgUserCollectionService")
	private UserCollectionService userCollectionService;
	
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		userCollectionService.getRow().put("pageable", pageable);
		Page page = userCollectionService.queryPage();
		model.addAttribute("page", page);
		userService.getRow().clear();
		return "/hslgpage/platform/member/collection/list";
	}
	
	/**
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/SelectUser")
	public String selectUserList(String id,Pageable pageable, ModelMap model) {
		userService.getRow().put("pageable", pageable);
		Page page = userService.queryPage();
		model.addAttribute("page", page);
		if(StringUtils.isEmptyOrNull(id))
		{
			id ="";
		}
		model.addAttribute("id", id);
		userService.getRow().clear();
		return "/hslgpage/platform/member/collection/SelectUser";
	}

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/hslgpage/platform/member/collection/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row userRow =MapUtils.convertMaptoRowWithoutNullField(map);
		String telephone =userRow.getString("telephone","");
		if(StringUtils.isEmptyOrNull(telephone))
		{
			addFlashMessage(redirectAttributes, Message.error("手机号码不能为空"));
			return "redirect:add.do";
		}
		String password =userRow.getString("password","");
		if(StringUtils.isEmptyOrNull(password))
		{
			addFlashMessage(redirectAttributes, Message.error("密码不能为空"));
			return "redirect:add.do";
		}
		userRow.put("password", Md5Util.Md5(password));
		userRow.put("register_time", DateUtil.getCurrentDateTime());
		int rand_len =6;
		int user_total =userService.getUserTotalNum();
		int result =user_total/100000 ;
		if(result <10)
		{
			rand_len =6;
		}
		else if(result>10 && result <100)
		{
			rand_len =7;
		}
		else if( result>100 && result <1000)
		{
			rand_len =8;
		}
		String user_code="";
		boolean bool =false;
		do
		{
			user_code= StringUtil.getRandKeys(rand_len).toUpperCase();
			bool =userService.isInviteUserCodeExsited(user_code);
		}
		while(bool);
		userRow.put("user_code", user_code);
		userService.insert(userRow);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) throws Exception {
		Assert.notNull(id);
		Row userRow =userService.find(String.valueOf(id));
		model.addAttribute("row", userRow);
		return "/hslgpage/platform/member/collection/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row userRow=MapUtils.convertMaptoRowWithoutNullField(map);
		userService.update(userRow);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		userService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	
	
	
	/**
	 * 检查身份证号码是否已存在
	 */
	@RequestMapping(value = "/collectGooods")
	public @ResponseBody
	ResponseVO collectGooods(String user_id, String goods_id) 
	{
		ResponseVO ovo =null;
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new ResponseVO(-1, "用户未登录", "用户未登录");
			return ovo;
		}
		if(StringUtils.isEmptyOrNull(goods_id))
		{
			ovo =new ResponseVO(-1, "用户未选中商品", "用户未选中商品");
			return ovo;
		}
		Row row =userCollectionService.find(user_id, goods_id);
		if(row == null )
		{
			Row insertRow =new Row();
			insertRow.put("user_id", user_id);
			insertRow.put("goods_id", goods_id);
			userCollectionService.insert(insertRow);
		}
		ovo =new ResponseVO(0, "success", "success");
		return ovo;
	}
	
}
