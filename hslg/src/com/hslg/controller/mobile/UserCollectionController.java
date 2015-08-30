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
import com.hslg.service.UserCollectionService;
import com.hslg.service.UserService;
/**
 * 用户收货地址
 * @author TongJianbo
 *
 */
@Controller("mobileUserCollectionController")
@RequestMapping("/api/user/collection")
public class UserCollectionController extends BaseController {
	
	private static Logger logger = Logger.getLogger(UserCollectionController.class); 
	
	@Resource(name = "hslgUserService")
	private UserService userService;
	
	@Resource(name = "hslgUserCollectionService")
	private UserCollectionService userCollectionService;
	
	
	/**
	 * 用户分页查询
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryPage(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet list =userCollectionService.list(user_id, page, page_size);
		ovo =new OVO(0,"","");
		ovo.set("list", list);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	@RequestMapping(value ="/add")
	public @ResponseBody
	String add(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String user_id =ivo.getString("user_id","");
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String goods_id =ivo.getString("goods_id","");
		if(StringUtils.isEmptyOrNull(goods_id))
		{
			ovo =new OVO(-1,"商品编号不能为空","商品编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row userRow =userService.find(user_id);
		if(userRow == null)
		{
			ovo =new OVO(-1,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		
		Row collection_row =userCollectionService.find(user_id,goods_id);
		if(collection_row != null)
		{
			ovo =new OVO(-10021,"用户已收藏此商品，不能重复收藏","用户已收藏此商品，不能重复收藏");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row =new Row();
		row.put("user_id", user_id);
		row.put("goods_id", goods_id);
		userCollectionService.insert(row);
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**
	 * 查询收货地址详情
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/detail")
	public @ResponseBody
	String find(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("查询收货地址详情");
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"编号不能为空","编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row = userCollectionService.find(id);
		String province_id =row.getString("province_id","");
		String province_name =row.getString("province_name","");
		String city_id =row.getString("city_id","");
		String city_name =row.getString("city_name","");
		String region_id =row.getString("region_id","");
		String region_name =row.getString("region_name","");
		String address =row.getString("address","");
		String receive_name =row.getString("receive_name","");
		String receive_tel =row.getString("receive_tel","");
		String zip_code =row.getString("zip_code","");
		String is_default =row.getString("is_default","");
		ovo =new OVO(0,"操作成功","");
		ovo.set("id", id);
		ovo.set("province_id", province_id);
		ovo.set("province_name", province_name);
		ovo.set("city_id", city_id);
		ovo.set("city_name", city_name);
		ovo.set("region_id", region_id);
		ovo.set("region_name", region_name);
		ovo.set("address", address);
		ovo.set("receive_name", receive_name);
		ovo.set("receive_tel", receive_tel);
		ovo.set("zip_code", zip_code);
		ovo.set("is_default", is_default);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/delete")
	public @ResponseBody
	String delete(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"收藏编号不能为空","收藏编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		userCollectionService.delete(id);
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

	
}
