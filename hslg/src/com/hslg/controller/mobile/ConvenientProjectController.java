package com.hslg.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.hslg.service.CommonwealProjectRecordService;
import com.hslg.service.CommonwealProjectService;
import com.hslg.service.UserTokenService;

@Controller("mobileConvenientProjectController")
@RequestMapping("/api/convenient/project/profile")
public class ConvenientProjectController extends BaseController {
	
	private static Logger logger = Logger.getLogger(ConvenientProjectController.class); 
	
	@Resource(name = "hslgCommonwealProjectService")
	private CommonwealProjectService commonwealProjectService;
	
	@Resource(name = "hslgCommonwealProjectRecordService")
	private CommonwealProjectRecordService commonwealProjectRecordService;
	
	@Resource(name = "hslgUserTokenService")
	private UserTokenService userTokenService;
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryAllAd(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet ds =commonwealProjectService.list(page, page_size);
		OVO ovo =new OVO(0,"","");
		ovo.set("list", ds);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	@RequestMapping(value ="/notify")
	public @ResponseBody
	String notify(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String order_no =ivo.getString("order_no","");
		String user_id =ivo.getString("user_id","");
		Row tokenRow =userTokenService.find(user_id);
		if(tokenRow == null)
		{
			ovo =new OVO(-20012,"用户未登录","用户未登录");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row orderRow =commonwealProjectRecordService.findByOrderNo(order_no);
		if(orderRow == null)
		{
			ovo =new OVO(-20012,"订单号不存在","订单号不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		orderRow =MapUtils.convertMaptoRowWithoutNullField(orderRow);
		String pay_state =orderRow.getString("pay_state","-1");
		if(pay_state.equals("2"))
		{
			OVO ovo =new OVO(0,"通知服务器成功","通知服务器成功");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		orderRow.put("pay_state", "1");
		commonwealProjectRecordService.update(orderRow);
		OVO ovo =new OVO(0,"通知服务器成功","通知服务器成功");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 查询广告详情
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/find")
	public @ResponseBody
	String findById(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("广告详情");
		String id=ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-10011,"id不能为空","id不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row =commonwealProjectService.find(id);
		ovo =new OVO(0,"","");
		ovo.set("project", row);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
