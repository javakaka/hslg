package com.hslg.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.hslg.service.GoodsJudgementService;
import com.hslg.service.GoodsService;
/**
 * 订单
 * @author TongJianbo
 */
@Controller("mobileGoodsJudgementController")
@RequestMapping("/api/goods/judgement")
public class GoodsJudgementController extends BaseController {
	
	private static Logger logger = Logger.getLogger(GoodsJudgementController.class); 
	
	@Resource(name = "hslgGoodsService")
	private GoodsService goodsService;
	
	@Resource(name = "hslgGoodsJudgementService")
	private GoodsJudgementService goodsJudgementService;
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryPage(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("分页查询");
		String goods_id =ivo.getString("goods_id",null);
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet list =goodsJudgementService.list(goods_id, page, page_size);
		ovo =new OVO(0,"操作成功","操作成功");
		ovo.set("list", list);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 添加商品评论
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/add")
	public @ResponseBody
	String add(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("分页查询");
		String goods_id =ivo.getString("goods_id",null);
		String user_id =ivo.getString("user_id",null);
		String content =ivo.getString("content",null);
		if(StringUtils.isEmptyOrNull(goods_id))
		{
			ovo =new OVO(-1,"商品编号不能为空","商品编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(content))
		{
			ovo =new OVO(-1,"评价内容不能为空","评价内容不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row judgeRow =new Row();
		judgeRow.put("goods_id", goods_id);
		judgeRow.put("user_id", user_id);
		judgeRow.put("content", content);
		judgeRow.put("state", "1");
		goodsJudgementService.insert(judgeRow);
		ovo =new OVO(0,"操作成功","操作成功");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
}
