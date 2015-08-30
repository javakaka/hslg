package com.hslg.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.VOConvert;
import com.hslg.service.GoodsService;
import com.hslg.service.GoodsTypeService;
/**
 * 订单
 * @author TongJianbo
 */
@Controller("mobileGoodsTypeController")
@RequestMapping("/api/goodstype")
public class GoodsTypeController extends BaseController {
	
	private static Logger logger = Logger.getLogger(GoodsTypeController.class); 
	
	@Resource(name = "hslgGoodsService")
	private GoodsService goodsService;
	
	@Resource(name = "hslgGoodsTypeService")
	private GoodsTypeService goodsTypeService;
	
	/**
	 * 分页查询商品分类
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryPage(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("分页查询商品分类");
		DataSet list =goodsTypeService.list();
		ovo =new OVO(0,"操作成功","操作成功");
		ovo.set("list", list);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
}
