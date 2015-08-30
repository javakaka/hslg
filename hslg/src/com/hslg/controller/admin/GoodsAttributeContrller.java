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
import com.ezcloud.framework.util.ResponseVO;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.Row;
import com.hslg.service.GoodsAttributeService;
import com.hslg.service.GoodsService;

@Controller("hslgPlatformGoodsAttributeController")
@RequestMapping("/hslgpage/platform/goods/attribute")
public class GoodsAttributeContrller  extends BaseController{

	@Resource(name="hslgGoodsService")
	  private GoodsService goodsService;
	  
	  @Resource(name="hslgGoodsAttributeService")
	  private GoodsAttributeService goodsAttributeService;
	  
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		goodsService.getRow().put("pageable", pageable);
		Page page = goodsService.queryPage();
		model.addAttribute("page", page);
		goodsService.getRow().clear();
		return "/paimaipage/platform/auction/goods/attribute/list";
	}
	
	@RequestMapping(value = "/add")
	public String add(String id,ModelMap model) {
		return "/paimaipage/platform/auction/goods/attribute/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		ResponseVO ovo =new ResponseVO();
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		String goods_id =row.getString("goods_id","");
		int rowNum =0;
		if(! StringUtils.isEmptyOrNull(goods_id))
		{
			Row attrRow =goodsAttributeService.findByGoodsId(goods_id);
			if(attrRow == null)
			{
				rowNum =goodsAttributeService.insert(row);
			}
			else
			{
				rowNum =goodsAttributeService.update(row);
			}
		}
		else
		{
			ovo =new ResponseVO(-1, "商品编号为空", "商品编号为空");
			return ovo;
		}
		if(rowNum >0)
		{
			ovo =new ResponseVO(1, "success", "success");
			ovo.put("id", row.getString("id"));
		}
		else
		{
			ovo =new ResponseVO(-1, "fail", "fail");
		}
		return ovo;
	}

	@RequestMapping(value = "/edit")
	public String edit(String goods_id, ModelMap model) throws Exception {
		Row row =new Row();
		if(! StringUtils.isEmptyOrNull(goods_id))
		{
			row =goodsAttributeService.findByGoodsId(goods_id);
		}
		model.addAttribute("row", row);
		model.addAttribute("goods_id", goods_id);
		return "/hslgpage/platform/goods/attribute/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		goodsAttributeService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(String[] ids) {
		goodsAttributeService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
}