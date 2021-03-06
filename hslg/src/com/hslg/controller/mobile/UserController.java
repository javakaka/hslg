package com.hslg.controller.mobile;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.service.system.SystemMailService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.Md5Util;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.Base64Util;
import com.ezcloud.utility.DateUtil;
import com.ezcloud.utility.FileUtil;
import com.ezcloud.utility.StringUtil;
import com.hslg.service.SMSService;
import com.hslg.service.UserService;
import com.hslg.service.UserTokenService;

@Controller("mobileUserController")
@RequestMapping("/api/user")
public class UserController extends BaseController {
	
	private static Logger logger = Logger.getLogger(UserController.class); 
	
	@Resource(name = "hslgUserService")
	private UserService userService;
	
	@Resource(name = "frameworkSystemMailService")
	private SystemMailService mailService;
	
	@Resource(name = "hslgSMSService")
	private SMSService smsService;
	
	@Resource(name = "hslgUserTokenService")
	private UserTokenService userTokenService;
	
	/**
	 * 登录
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/login")
	public @ResponseBody
	String  login(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String username =ivo.getString("username",null);
		if(username == null || username.replace(" ","").length() ==0)
		{
			ovo =new OVO(-1001, "请求参数错误", "用户名不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String password =ivo.getString("password",null);
		if(password == null || password.replace(" ","").length() ==0)
		{
			ovo =new OVO(-1001, "请求参数错误", "密码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String version =ivo.getString("version",null);
		if(version == null || version.replace(" ","").length() ==0)
		{
			ovo =new OVO(-1001, "请求参数错误", "版本号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		logger.info("当前登录用户的客户端版本是:"+version);
		Row userRow =userService.login(username);
		if(userRow == null )
		{
			ovo =new OVO(-1001, "登录失败", "用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String dbPassword =userRow.getString("password","");
		if( dbPassword.length()>0 && dbPassword.equals(password) )
		{
			String id=userRow.getString("id");
			String name=userRow.getString("name","");
			String user_name=userRow.getString("username","");
			String telephone=userRow.getString("telephone","");
			String register_time=userRow.getString("register_time","");
			String avatar =userRow.getString("avatar","");
			String pay_password=userRow.getString("pay_password","");
			String nickname=userRow.getString("nickname","");
			ovo =new OVO(0, "登录成功", "");
			ovo.set("id", id);
			ovo.set("username", user_name);
			ovo.set("name", name);
			ovo.set("telephone", telephone);
			ovo.set("register_time", register_time);
			ovo.set("pay_password", pay_password);
			ovo.set("nickname", nickname);
			if(! StringUtils.isEmptyOrNull(avatar))
			{
				Setting setting =SettingUtils.get();
				String site_url =setting.getSiteUrl();
				avatar =site_url+"/"+avatar;
			}
			ovo.set("avatar", avatar);
			//写日志
			//如果版本号有变更，则更新登录版本号
			String userVersion =userRow.getString("current_version","");
			if(!version.equals(userVersion))
			{
				Row updateRow =new Row();
				updateRow.put("id", id);
				updateRow.put("current_version", version);
				userService.update(updateRow);
			}
			//token session
			String token ="";
			String cur_time =DateUtil.getCurrentDateTime();
			token =Md5Util.Md5(id+cur_time);
			Row tokenRow =new Row();
			tokenRow.put("user_id", id);
			tokenRow.put("token", token);
			ovo.set("token", token);
			ovo.set("collection_num", "0");
			ovo.set("score", "0");
			userTokenService.update(tokenRow);
		}
		else
		{
			ovo =new OVO(-1002, "登录失败,密码错误", "登录失败,密码错误");
		}
		logger.info("登录成功");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/changePassword")
	public @ResponseBody
	String changePassword(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String user_id =ivo.getString("user_id",null);
		String oldPwd =ivo.getString("oldPwd",null);
		String newPwd =ivo.getString("newPwd",null);
		String sms_code =ivo.getString("sms_code",null);
		String telephone =ivo.getString("telephone",null);
		
		if(user_id == null || user_id.replace(" ", "").length() ==0){
			ovo =new OVO(-10005,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(telephone == null || telephone.replace(" ", "").length() ==0){
			ovo =new OVO(-10005,"手机号码不能为空","手机号码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(oldPwd == null || oldPwd.replace(" ", "").length() ==0){
			ovo =new OVO(-10006,"原密码不能为空","原密码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(newPwd == null || newPwd.replace(" ", "").length() ==0){
			ovo =new OVO(-10007,"新密码不能为空","新密码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(oldPwd.equals(newPwd))
		{
			ovo =new OVO(-10008,"新密码不能和旧密码相同","新密码不能和旧密码相同");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(sms_code == null || sms_code.replace(" ", "").length() ==0){
			ovo =new OVO(-10006,"短信验证码不能为空","短信验证码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//短信验证码是否存在
		boolean codeExsited =smsService.findByCodeAndTelphone(sms_code, telephone,2);
		if(! codeExsited)
		{
			ovo =new OVO(-10010,"短信验证码错误或已过期","短信验证码错误或已过期");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		int status =userService.changePassword(user_id, oldPwd, newPwd);
		if(status == 1)
		{
			ovo =new OVO(-10002,"用户不存在","用户不存在");
		}
		else if(status == 2)
		{
			ovo =new OVO(-10002,"原密码错误","原密码错误");
		}
		else if(status == 3)
		{
			ovo =new OVO(-10002,"修改密码失败","修改密码失败");
		}
		else
		{
			ovo =new OVO(0,"操作成功","");
		}
		String json =AesUtil.encode(VOConvert.ovoToJson(ovo));
		return json;
	}
	
	/**
	 * 房东租客用户注册
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/register")
	public @ResponseBody
	String register(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		Row insertRow =new Row();
		/**手机号码 必填 **/
		String telephone =ivo.getString("telephone",null);
		if(StringUtils.isEmptyOrNull(telephone))
		{
			ovo =new OVO(-10010,"手机号码不能为空","手机号码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String username =ivo.getString("username",null);
		if(StringUtils.isEmptyOrNull(username))
		{
			ovo =new OVO(-10010,"用户名不能为空","用户名不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//检查用户名是否已经存在
		Row usernameRow =userService.findByUserName(username);
		if(usernameRow != null)
		{
			ovo =new OVO(-10010,"用户名已经存在","用户名已经存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		else
		{
			insertRow.put("username", username);
		}
		if(! StringUtils.isTelphone(telephone))
		{
			ovo =new OVO(-10010,"手机号码不正确","手机号码不正确");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row staff =userService.findByTelephone(telephone);
		if(staff != null)
		{
			ovo =new OVO(-10010,"手机号码已被注册","手机号码已被注册");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		insertRow.put("telephone", telephone);
		/**密码 必填 **/
		String password =ivo.getString("password",null);
		if(StringUtils.isEmptyOrNull(password))
		{
			ovo =new OVO(-10010,"密码不能为空","密码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		insertRow.put("password", password);
		
		/**短信验证码 必填**/
		String sms_code =ivo.getString("sms_code",null);
		if(StringUtils.isEmptyOrNull(sms_code))
		{
			ovo =new OVO(-10010,"短信验证码不能为空","短信验证码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		boolean codeExsited =smsService.findByCodeAndTelphone(sms_code, telephone,1);
		if(! codeExsited)
		{
			ovo =new OVO(-10010,"短信验证码错误或已过期","短信验证码错误或已过期");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		/**注册时间 取服务器时间 **/
		String register_time =DateUtil.getCurrentDateTime();
		insertRow.put("register_time", register_time);
		/**终端类型 必填 1 android 2 ios **/
		String device =ivo.getString("device",null);
		if(StringUtils.isEmptyOrNull(device))
		{
			ovo =new OVO(-10010,"设备类型不能为空","设备类型不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		insertRow.put("device", device);
		/**机器码 必填 **/
		String device_code =ivo.getString("device_code",null);
		if(StringUtils.isEmptyOrNull(device_code))
		{
			ovo =new OVO(-10010,"机器码不能为空","机器码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		insertRow.put("device_code", device_code);
		/**客户端版本 必填 **/
		String version =ivo.getString("version",null);
		if(StringUtils.isEmptyOrNull(version))
		{
			ovo =new OVO(-10010,"客户端版本号不能为空","客户端版本号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
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
		insertRow.put("user_code", user_code);
		insertRow.put("register_version", version);
		insertRow.put("create_time", register_time);
		int num =userService.insert(insertRow);
		
		if(num > 0)
		{
			ovo =new OVO(0,"注册成功","");
			Row userRow =userService.findByTelephone(telephone);
			if(userRow == null )
			{
				ovo =new OVO(-1001, "登录失败", "用户不存在");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
			String id=userRow.getString("id");
			telephone=userRow.getString("telephone","");
			username=userRow.getString("username","");
			register_time=userRow.getString("register_time","");
			ovo.set("id", id);
			ovo.set("telephone", telephone);
			ovo.set("username", username);
			ovo.set("register_time", register_time);
			//写日志
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		else
		{
			ovo =new OVO(-10010,"注册失败","注册失败");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
	}
	
	/**
	 * 根据用户id查询用户信息
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/profile")
	public @ResponseBody
	String queryProfile(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		Row userRow =null;
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-10010,"用户编号不能为空","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		userRow =userService.find(id);
		if(userRow == null)
		{
			ovo =new OVO(-10010,"用户不存在","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		
		String name=userRow.getString("name","");
		String username=userRow.getString("username","");
		String telephone=userRow.getString("telephone","");
		String register_time=userRow.getString("register_time","");
		String sex=userRow.getString("sex","");
		String pay_password=userRow.getString("pay_password","");
		String nickname=userRow.getString("nickname","");
		String avatar =userRow.getString("avatar","");
		if(! StringUtils.isEmptyOrNull(avatar))
		{
			Setting setting =SettingUtils.get();
			String site_url =setting.getSiteUrl();
			avatar =site_url+"/"+avatar;
		}
		ovo =new OVO(0, "查询成功", "");
		ovo.set("id", id);
		ovo.set("username", username);
		ovo.set("name", name);
		ovo.set("telephone", telephone);
		ovo.set("register_time", register_time);
		ovo.set("pay_password", pay_password);
		ovo.set("avatar", avatar);
		ovo.set("nickname", nickname);
		ovo.set("collection_num", "0");
		ovo.set("score", "0");
		Row tokenRow =userTokenService.find(id);
		String token ="";
		if(tokenRow != null)
		{
			token =tokenRow.getString("token","");
		}
		ovo.set("token", token);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 修改用户信息
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/updateProfile")
	public @ResponseBody
	String updateProfile(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		Row userRow =null;
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-10010,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		userRow =userService.find(id);
		if(userRow == null)
		{
			ovo =new OVO(-10010,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		userRow =new Row();
		userRow.put("id", id);
		String name =ivo.getString("name",null);
		if(!StringUtils.isEmptyOrNull(name))
		{
			userRow.put("name", name);
		}
		//验证唯一性
		String nickname =ivo.getString("nickname",null);
		boolean nickname_existed=false;
		if(!StringUtils.isEmptyOrNull(nickname))
		{
			nickname_existed =userService.isNicknameExisted(id,nickname);
			if(nickname_existed)
			{
				ovo =new OVO(-10010,"此昵称已存在，请使用其他昵称","");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
			userRow.put("nickname", nickname);
		}
		int rowNum =userService.update(userRow);
		if(rowNum ==0)
		{
			ovo =new OVO(-10010, "操作失败", "操作失败");
		}
		else
		{
			ovo =new OVO(0, "操作成功", "操作成功");
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 绑定银行卡
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/bind_card")
	public @ResponseBody
	String bindBankCard(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		Row userRow =null;
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-10010,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		userRow =userService.find(id);
		if(userRow == null)
		{
			ovo =new OVO(-10010,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String name =ivo.getString("user_name",null);
		if(StringUtils.isEmptyOrNull(name))
		{
			ovo =new OVO(-10010,"用户姓名不能为空","用户姓名不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String card_no =ivo.getString("card_no",null);
		String old_bank_card_no =userRow.getString("bank_card_no","");
		if(StringUtils.isEmptyOrNull(card_no))
		{
			ovo =new OVO(-10010,"银行卡号不能为空","银行卡号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		name =AesUtil.encode(name);
		card_no =AesUtil.encode(card_no);
		if(!StringUtils.isEmptyOrNull(old_bank_card_no))
		{
			if(card_no.equals(old_bank_card_no))
			{
				ovo =new OVO(-10010,"此银行卡已绑定","此银行卡已绑定");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
		}
		userRow =new Row();
		userRow.put("id", id);
		userRow.put("bank_card_no", card_no);
		String bank_card_type ="";
		userRow.put("bank_card_type", bank_card_type);
		userRow.put("name", name);
		int rowNum =userService.update(userRow);
		if(rowNum ==0)
		{
			ovo =new OVO(-10010, "操作失败", "操作失败");
		}
		else
		{
			ovo =new OVO(10010, "操作成功", "");
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 绑定支付银行卡
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/bind_credit_card")
	public @ResponseBody
	String bindCreditCard(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		Row userRow =null;
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-10010,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		userRow =userService.find(id);
		if(userRow == null)
		{
			ovo =new OVO(-10010,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String name =ivo.getString("user_name",null);
		if(StringUtils.isEmptyOrNull(name))
		{
			ovo =new OVO(-10010,"用户姓名不能为空","用户姓名不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String password =ivo.getString("password",null);
		if(StringUtils.isEmptyOrNull(password))
		{
			ovo =new OVO(-10010,"密码不能为空","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String oldPwd =userRow.getString("password",null);
		if(! password.equals(oldPwd))
		{
			ovo =new OVO(-10010,"密码错误","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));			
		}
		String card_no =ivo.getString("card_no",null);
		String old_credit_card_no =userRow.getString("credit_card_no","");
		if(StringUtils.isEmptyOrNull(card_no))
		{
			ovo =new OVO(-10010,"银行卡号不能为空","银行卡号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		name =AesUtil.encode(name);
		card_no =AesUtil.encode(card_no);
		if(!StringUtils.isEmptyOrNull(old_credit_card_no))
		{
			if(card_no.equals(old_credit_card_no))
			{
				ovo =new OVO(-10010,"此银行卡已绑定","此银行卡已绑定");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
		}
		userRow =new Row();
		userRow.put("id", id);
		userRow.put("credit_card_no", card_no);
		String credit_card_type ="";
		userRow.put("credit_card_type", credit_card_type);
		userRow.put("name", name);
		int rowNum =userService.update(userRow);
		if(rowNum ==0)
		{
			ovo =new OVO(-10010, "操作失败", "操作失败");
		}
		else
		{
			ovo =new OVO(10010, "操作成功", "操作成功");
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 找回密码
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/resetPassword")
	public @ResponseBody
	String resetPasswordBySms(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String telephone =ivo.getString("telephone",null);
		if(StringUtils.isEmptyOrNull(telephone))
		{
			ovo =new OVO(-10010,"手机号码不能为空","手机号码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String password =ivo.getString("password",null);
		if(StringUtils.isEmptyOrNull(telephone))
		{
			ovo =new OVO(-10010,"密码不能为空","密码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//验证手机号码是否正确
		Row userRow =userService.findByTelephone(telephone);
		if(userRow == null)
		{
			ovo =new OVO(-10010,"用户不存在","非法用户");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String sms_code =ivo.getString("sms_code",null);
		if(StringUtils.isEmptyOrNull(sms_code))
		{
			ovo =new OVO(-10010,"短信验证码不能为空","短信验证码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//短信验证码是否存在
		boolean codeExsited =smsService.findByCodeAndTelphone(sms_code, telephone,2);
		if(! codeExsited)
		{
			ovo =new OVO(-10010,"短信验证码错误或已过期","短信验证码错误或已过期");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String id =userRow.getString("id","");
		//更新密码
		Row pwdRow =new Row();
		pwdRow.put("password", password);
		pwdRow.put("id", id);
		int rowNum =userService.update(pwdRow);
		if(rowNum >0)
		{
			ovo =new OVO(0,"操作成功","");
		}
		else
		{
			ovo =new OVO(-10010,"操作失败","操作失败");
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 更改手机号码
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/changeTelephone")
	public @ResponseBody
	String changeTelephone(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		//用户ID
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String telephone =ivo.getString("telephone",null);
		if(StringUtils.isEmptyOrNull(telephone))
		{
			ovo =new OVO(-1,"手机号码不能为空","手机号码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String sms_code =ivo.getString("sms_code","");
		if(StringUtils.isEmptyOrNull(sms_code))
		{
			ovo =new OVO(-1,"短信验证码不能为空","短信验证码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		boolean codeExsited =smsService.findByCodeAndTelphone(sms_code, telephone,3);
		if(! codeExsited)
		{
			ovo =new OVO(-10010,"短信验证码错误或已过期","短信验证码错误或已过期");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row userRow =new Row();
		userRow.put("id", id);
		userRow.put("telephone", telephone);
		int num =userService.update(userRow);
		if(num <=0)
		{
			ovo =new OVO(-1,"操作失败,请检查相关参数是否正确","操作失败,请检查相关参数是否正确");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		ovo =new OVO(0,"操作成功","操作成功");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 设置支付密码
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/setPayPassword")
	public @ResponseBody
	String setPayPassword(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		//用户ID
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String pay_password =ivo.getString("pay_password",null);
		if(StringUtils.isEmptyOrNull(pay_password))
		{
			ovo =new OVO(-1,"支付密码不能为空","支付密码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row userRow =new Row();
		userRow.put("id", id);
		userRow.put("pay_password", pay_password);
		int num =userService.update(userRow);
		if(num <=0)
		{
			ovo =new OVO(-1,"操作失败,请检查相关参数是否正确","操作失败,请检查相关参数是否正确");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		ovo =new OVO(0,"操作成功","操作成功");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * logout
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/logout")
	public @ResponseBody
	String logout(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		//用户ID
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		userTokenService.delete(id);
		ovo =new OVO(0,"操作成功","操作成功");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 上传头像，统一用base64上传，图片格式为png，且上传之前要压缩图片，使图片小于100K
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/upload_avatar")
	public @ResponseBody
	String upload_avatar(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		//用户ID
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//base64 编码图片
		Setting setting =SettingUtils.get();
		String file_path =setting.getPhysicalPath();
		String avatar_uri ="";
		String userPic2 =ivo.getString("picture_base64_str",null);
		String userPic = new String(Base64Util.decode(userPic2));
		String imgPath ="";
		String imgName =id;
		if(userPic != null && !userPic.trim().equals(""))
		{
			String basePath =file_path+"/resources"+"/cxhl_user_icon/";
			File file =new File(basePath);
			if(! file.isDirectory())
			{
				FileUtil.mkdir(basePath);
			}
			imgPath=basePath+"/"+imgName+".png";
			avatar_uri ="/resources"+"/cxhl_user_icon"+"/"+imgName+".png";
			imgPath =imgPath.replace("\\\\","\\");
			imgPath =imgPath.replace("\\","/");
			Base64Util.GenerateImage(userPic, imgPath);
		}
		Row row =new Row();
		row.put("id", id);
		row.put("avatar", avatar_uri);
		userService.update(row);
		ovo =new OVO(0,"上传头像成功","上传头像成功");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

	
	
}
