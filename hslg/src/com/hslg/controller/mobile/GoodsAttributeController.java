package com.hslg.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.hslg.service.GoodsAttributeService;
import com.hslg.service.GoodsService;
/**
 * 订单
 * @author TongJianbo
 */
@Controller("mobileGoodsAttributeController")
@RequestMapping("/api/goods/attribute")
public class GoodsAttributeController extends BaseController {
	
	private static Logger logger = Logger.getLogger(GoodsAttributeController.class); 
	
	@Resource(name = "hslgGoodsService")
	private GoodsService goodsService;
	
	@Resource(name = "hslgGoodsAttributeService")
	private GoodsAttributeService goodsAttributeService;
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryAttribute(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("查询商品参数");
		String goods_id =ivo.getString("goods_id","");
		if(StringUtils.isEmptyOrNull(goods_id))
		{
			ovo =new OVO(-1,"商品编号不能为空","商品编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		
		Row row =goodsService.findDetail(goods_id);
		if(row ==null)
		{
			ovo =new OVO(-1,"商品不存在","商品不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String id ="";
		String brand ="";
		String specification ="";
		String packaging ="";
		String origin ="";
		String expiry_date ="";
		String store ="";
		String goods_name =row.getString("name","");
		Row attributeRow =goodsAttributeService.list(goods_id);
		if(attributeRow != null)
		{
			id =attributeRow.getString("id","");
			brand =attributeRow.getString("brand","");
			specification =attributeRow.getString("specification","");
			packaging =attributeRow.getString("packaging","");
			origin =attributeRow.getString("origin","");
			expiry_date =attributeRow.getString("expiry_date","");
			store =attributeRow.getString("store","");
		}
		ovo =new OVO(0,"","");
		ovo.set("id",id );
		ovo.set("goods_id", goods_id);
		ovo.set("brand", brand);
		ovo.set("specification", specification);
		ovo.set("packaging", packaging);
		ovo.set("origin", origin);
		ovo.set("expiry_date", expiry_date);
		ovo.set("store", store);
		ovo.set("goods_name", goods_name);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
}
