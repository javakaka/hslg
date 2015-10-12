package com.hslg.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;
import com.hslg.service.UserTokenService;

@Controller("mobileConvenientFinanceController")
@RequestMapping("/api/convenient/finance")
public class ConvenientFinanceController extends BaseController {
	
	private static Logger logger = Logger.getLogger(ConvenientFinanceController.class); 
	
	@Resource(name = "hslgUserTokenService")
	private UserTokenService userTokenService;
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/summary")
	public @ResponseBody
	String queryAllAd(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("善小财务汇总");
		String user_id =ivo.getString("user_id","");
		String current_date =DateUtil.getCurrentDate();
//		机构募集善款
		String org_total_money ="0";
//		商城累计公益款
		String shop_total_money ="0";
//		上月机构对公账户余额
		String last_month_left_money ="0";
//		上月进出金额
		String last_month_total_money ="0";
//		用户购物公益善款
		String user_shop_money ="0";
//		用户捐赠的金额
		String user_donate_money ="0";
		OVO ovo =new OVO(0,"","");
		ovo.set("current_date", current_date);
		ovo.set("org_total_money", org_total_money);
		ovo.set("shop_total_money", shop_total_money);
		ovo.set("last_month_left_money", last_month_left_money);
		ovo.set("last_month_total_money", last_month_total_money);
		ovo.set("user_shop_money", user_shop_money);
		ovo.set("user_donate_money", user_donate_money);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
}
