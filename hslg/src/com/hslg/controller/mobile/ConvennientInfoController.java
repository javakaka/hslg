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
import com.hslg.service.ConvenientInfoService;

@Controller("mobileConvennientInfoController")
@RequestMapping("/api/convennient/info/profile")
public class ConvennientInfoController extends BaseController {
	
	private static Logger logger = Logger.getLogger(ConvennientInfoController.class); 
	@Resource(name = "hslgConvenientInfoService")
	private ConvenientInfoService convenientInfoService;
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryAll(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("ConvennientInfo list ");
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		String type_id =ivo.getString("type_id","10");
		DataSet ds =convenientInfoService.list(page,page_size,type_id);
		OVO ovo =new OVO(0,"","");
		ovo.set("list", ds);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
