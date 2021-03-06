package com.hslg.test;

import java.io.UnsupportedEncodingException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.Base64Util;
import com.ezcloud.utility.NetUtil;

/**
 * @author Administrator
 *
 */
public class ApiTest {

	//查询版本
	public static void getVersion()
	{
//		String url ="http://localhost:8080/hslg/api/version/lastest.do";
		String url ="http://120.25.253.240:8080/hslg/api/version/lastest.do";
		IVO ivo =new IVO();
		   try {
			   //1房租宝房东租客版2中介版
//				ivo.set("app", "1");
				//1 ios 2 android 3 wp
				ivo.set("device", "2");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n 加密前 ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	//发送短信验证码
	public static void sendSms()
	{
		String url ="http://localhost:8080/hslg/api/sms/send.do";
//		String url ="http://120.25.253.240:8080/hslg/api/sms/send.do";
		IVO ivo =new IVO();
		   try {
			   //手机号
				ivo.set("telephone", "13826531136");
//				ivo.set("telephone", "13590856852");
				//1 发送短信 0不发送短信
//				ivo.set("type", "0");
				ivo.set("type", "1");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n 加密前 ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	//注册
	public static void register()
	{
		String url ="http://localhost:8080/hslg/api/user/register.do";
//			String url ="http://120.25.253.240:8080/hslg/api/user/register.do";
		IVO ivo =new IVO();
	   try {
			ivo.set("telephone", "13826531136");
			ivo.set("username", "kakaka");
			ivo.set("password", "E10ADC3949BA59ABBE56E057F20F883E");
			ivo.set("sms_code", "452381");
			ivo.set("device", "1");
			ivo.set("device_code", "1212312735713575663");
			ivo.set("address", "1212312735713575663");
			//客户端版本
			ivo.set("version", "1.0");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	//登陆
	public static void login()
	{
//		String url ="http://localhost:8080/hslg/api/user/login.do";
		String url ="http://120.25.253.240:8080/hslg/api/user/login.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("username", "13826531136");
				ivo.set("username", "kakaka");
				ivo.set("username", "13826531135");
			   ivo.set("password", "670B14728AD9902AECBA32E22FA4F6BD");
				//客户端版本
				ivo.set("version", "1.1");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n 加密前 ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	//logout
	public static void logout()
	{
		String url ="http://localhost:8080/hslg/api/user/logout.do";
//		String url ="http://120.25.253.240:8080/hslg/api/user/logout.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "1");
//			ivo.set("username", "13826531136");
//				ivo.set("username", "13826531137");
//			ivo.set("password", "E10ADC3949BA59ABBE56E057F20F883E");
			//客户端版本
//			ivo.set("version", "1.1");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//登陆
	public static void login_with_incrypt_str()
	{
//		String url ="http://localhost:8080/hslg/api/action/login.do";
		String url ="http://120.25.253.240:8080/hslg/api/action/login.do";
		IVO ivo =new IVO();
		   try {
				String json ="Ngn1aWQ+u1siNR+rQkMMkJbWYIGCZ1TsoMykxSMNwq5o1DFiS1vO3s59YYanc+CASepqMi7TfTfpN2MsyvLyPPk9VokIKztnrCqk6Z57gy9HqH6DxmEDF+xD2kUbry/iVIUs9IdX73hXxPavHAZ7Z72ChNndvIRlHwtCwy2JvlXIHv3ahbFTxYfsvMZorzBnb3c20u1fTaJz6poV7RcIQMmvgz2tlj23kSoEXwvuYdy6n9d+Wup022OiuLFXQRpz8LieXjsO0rcWho5+P/A1N30S3zSlSTAwSG1sho6S69CgKnsFUtscIME2wgmdMnMnTuTw8cd0MkRiuA5y8mmWH39sx3qbPuESrcj2ouXqP2eqjzzs9GfNTUy3sOnkcnjyvukUxrymScxO3FjHCUjxSjmosN7PbvWAEShAo9Rh2f62q489U1bldH7qnSdAwKUQzDRXgBbiuDABybn0kgosAFTM2MNWo/KYISBKgch7NaeB3Rwoj0cfMcCR26V+dGZ1AFruTcziqSfqy1E/MyINtw5NBTTp8oto1xlAV0xNQ4Upcp6YapkopDk2NVZhCc7DAhaBMkQZRFRUXV6WDWED7vdI37pt0EiOA/nJJTKN6hN5/33VXcF7OaInwGlOVAaR";
				System.out.println("\n  json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	//查询用户基本信息
	public static void queryProfile()
	{
		String url ="http://localhost:8080/hslg/api/user/profile.do";
//		String url ="http://120.25.253.240:8080/hslg/api/user/profile.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("id", "1");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n 加密前 ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	//修改密码
	public static void changePassword()
	{
//		String url ="http://localhost:8080/hslg/api/user/changePassword.do";
		String url ="http://120.25.253.240:8080/hslg/api/user/changePassword.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("user_id", "2");
				ivo.set("oldPwd", "E10ADC3949BA59ABBE56E057F20F883E");//123456
				ivo.set("newPwd", "670B14728AD9902AECBA32E22FA4F6BD");//000000
				ivo.set("sms_code", "109389");
				ivo.set("telephone", "13826531136");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n 加密前 ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	
	//找回密码时发送短信验证码
	public static void sendSmsResetPwd()
	{
//		String url ="http://localhost:8080/hslg/api/sms/send_reset_pwd.do";
		String url ="http://120.25.253.240:8080/hslg/api/sms/send_reset_pwd.do";
		IVO ivo =new IVO();
		   try {
			   //手机号
				ivo.set("telephone", "13826531136");
				//1 发送短信 0不发送短信
//				ivo.set("type", "0");
				ivo.set("type", "1");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n 加密前 ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	//通过短信重置密码
	public static void resetPassword()
	{
		String url ="http://localhost:8080/hslg/api/user/resetPassword.do";
//		String url ="http://120.25.253.240:8080/hslg/api/user/resetPassword.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("telephone", "13826531136");
				ivo.set("password", "E10ADC3949BA59ABBE56E057F20F883E");
				ivo.set("sms_code", "140402");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	//修改个人信息
	public static void updateProfile()
	{
//		String url ="http://localhost:8080/hslg/api/user/updateProfile.do";
		String url ="http://120.25.253.240:8080/hslg/api/user/updateProfile.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("id", "1");
//				ivo.set("name", "小童");
//				ivo.set("email", "510836102@qq.com");
//				ivo.set("username", "tong.kaka");
				ivo.set("nickname", "黑河");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	public static void mailtest()
	{
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();  
		mailSender.setHost("smtp.qq.com");  
		mailSender.setUsername("1914662148@qq.com");  
		mailSender.setPassword("1989125tjb"); 
		SimpleMailMessage smm = new SimpleMailMessage();  
		// 设定邮件参数  
		smm.setFrom(mailSender.getUsername());  
		smm.setTo("dabao1989125@163.com");
		smm.setSubject("Hello world");
		smm.setText("Hello world via spring mail sender");  
		// 发送邮件  
		System.out.println("start send .......");
		mailSender.send(smm);
		System.out.println("end send .......");
	}
	
	/**
	 * 查询所有的城市和城市所辖区域
	 */
	public static void queryCityAndZone()
	{
	String url ="http://localhost:8080/hslg/api/zone/CityAndZone.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 查询广告列表
	 */
	public static void queryAllAds()
	{
	String url ="http://120.25.253.240:8080/hslg/api/ad/list.do";
//	String url ="http://localhost:8080/hslg/api/ad/list.do";
	IVO ivo =new IVO();
	try {
			ivo.set("page", "1");
			ivo.set("page", "5");
//			ivo.set("page", "2");
//			ivo.set("page", "3");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } 
		catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询广告详情
	 */
	public static void queryAdDetail()
	{
	String url ="http://localhost:8080/hslg/api/ad/find.do";
//	String url ="http://120.25.253.240:8080/hslg/api/ad/find.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
			OVO ovo =VOConvert.jsonToOvo(res);
			System.out.println("ovo.oForm=====>>"+ovo.oForm);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 查询关于我们
	 */
	public static void queryAboutUs()
	{
//	String url ="http://localhost:8080/hslg/api/about_us/find.do";
	String url ="http://120.25.253.240:8080/hslg/api/about_us/find.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试事物
	 */
	public static void testTx()
	{
	String url ="http://localhost:8080/hslg/api/test/testTx.do";
//	String url ="http://120.25.253.240:8080/hslg/api/about_us/find.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 检查地理位置文件版本
	 */
	public static void checkGeographyFileVersion()
	{
	String url ="http://localhost:8080/hslg/api/zone/checkVersion.do";
//	String url ="http://120.25.253.240:8080/hslg/api/zone/checkVersion.do";
	IVO ivo =new IVO();
	try {
			ivo.set("version", "20150331152746");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 查询关于我们
	 */
	public static void queryRuleMessageAboutUs()
	{
//	String url ="http://localhost:8080/hslg/api/rule_message/about_us.do";
	String url ="http://120.25.253.240:8080/hslg/api/rule_message/about_us.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询免责申明
	 */
	public static void queryRuleMessageStatement()
	{
	String url ="http://localhost:8080/hslg/api/rule_message/statement.do";
//	String url ="http://120.25.253.240:8080/hslg/api/evaluation/list.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询房东租客的二维码分享信息
	 */
	public static void queryRuleMessageCoreQrCode()
	{
	String url ="http://localhost:8080/hslg/api/rule_message_qrcode.do";
//	String url ="http://120.25.253.240:8080/hslg/api/evaluation/list.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *中介版二维码
	 */
	public static void queryRuleMessageAgentQrCode()
	{
	String url ="http://localhost:8080/hslg/api/rule_message/agent_qrcode.do";
//	String url ="http://120.25.253.240:8080/hslg/api/evaluation/list.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 *收货地址列表
	 */
	public static void queryUserAddressPaeg()
	{
//		String url ="http://localhost:8080/hslg/api/user/address/list.do";
		String url ="http://120.25.253.240:8080/hslg/api/user/address/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *收货地址列表
	 */
	public static void queryUserDefaultAddress()
	{
//		String url ="http://localhost:8080/hslg/api/user/address/default.do";
		String url ="http://120.25.253.240:8080/hslg/api/user/address/default.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *添加收货地址
	 */
	public static void addUserAddress()
	{
//		String url ="http://localhost:8080/hslg/api/user/address/add.do";
		String url ="http://120.25.253.240:8080/hslg/api/user/address/add.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
//			ivo.set("province_id", "19");
//			ivo.set("city_id", "202");
//			ivo.set("region_id", "1956");
			ivo.set("address", "福田车公庙");
			ivo.set("receive_name", "小童");
			ivo.set("receive_tel", "1382653136");
			ivo.set("door_no", "105");
			ivo.set("tag", "家庭地址");
			ivo.set("is_default","1");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 *删除收货地址
	 */
	public static void deleteUserAddress()
	{
		String url ="http://localhost:8080/hslg/api/user/address/delete.do";
//		String url ="http://120.25.253.240:8080/hslg/api/user/address/delete.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "2");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//更换手机号码时发送短信验证码
	public static void sendSmsWhenChangeTelephone()
	{
		String url ="http://localhost:8080/hslg/api/sms/send_change_telephone.do";
//		String url ="http://120.25.253.240:8080/hslg/api/sms/send_change_telephone.do";
		IVO ivo =new IVO();
		   try {
			   //手机号
				ivo.set("user_id", "1");
				ivo.set("telephone", "13826531130");
				//1 发送短信 0不发送短信
				ivo.set("type", "0");
//				ivo.set("type", "1");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n 加密前 ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	//更换手机号码
	public static void changeTelephone()
	{
		String url ="http://localhost:8080/hslg/api/user/changeTelephone.do";
//		String url ="http://120.25.253.240:8080/hslg/api/user/changeTelephone.do";
		IVO ivo =new IVO();
		try {
			//手机号
			ivo.set("id", "1");
			ivo.set("telephone", "13826531130");
			ivo.set("sms_code", "295517");
//				ivo.set("type", "1");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//设置支付密码
	public static void setPayPassword()
	{
		String url ="http://localhost:8080/hslg/api/user/setPayPassword.do";
//		String url ="http://120.25.253.240:8080/hslg/api/user/setPayPassword.do";
		IVO ivo =new IVO();
		try {
			//手机号
			ivo.set("id", "1");
			ivo.set("pay_password", "96E79218965EB72C92A549DD5A330112");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//添加意见反馈
	public static void addCustomerTip()
	{
		String url ="http://localhost:8080/hslg/api/customer_tips/add.do";
//		String url ="http://120.25.253.240:8080/hslg/api/customer_tips/add.do";
		IVO ivo =new IVO();
		try {
			//手机号
			ivo.set("user_id", "1");
			ivo.set("title", "意见反馈");
			ivo.set("content", "app使用意见反馈");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//分页查询用户收藏
	public static void queryPageUserCollection()
	{
//		String url ="http://localhost:8080/hslg/api/user/collection/list.do";
		String url ="http://120.25.253.240:8080/hslg/api/user/collection/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//用户添加收藏
	public static void userAddCollection()
	{
//		String url ="http://localhost:8080/hslg/api/user/collection/add.do";
		String url ="http://120.25.253.240:8080/hslg/api/user/collection/add.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("goods_id", "1");
			ivo.set("is_collect", "1");
			ivo.set("is_collect", "0");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//用户批量删除收藏
	public static void userDeleteCollection()
	{
		String url ="http://localhost:8080/hslg/api/user/collection/delete.do";
//		String url ="http://120.25.253.240:8080/hslg/api/user/collection/delete.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "1,2,3");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//查询商家分类
	public static void queryGoodsType()
	{
		String url ="http://localhost:8080/hslg/api/goodstype/list.do";
//		String url ="http://120.25.253.240:8080/hslg/api/goodstype/list.do";
		IVO ivo =new IVO();
		try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//分页查询商家分类
	public static void queryGoodsPage()
	{
//		String url ="http://localhost:8080/hslg/api/goods/profile/list.do";
		String url ="http://120.25.253.240:8080/hslg/api/goods/profile/list.do";
		IVO ivo =new IVO();
		try {
//			ivo.set("type_id", "1");
//			ivo.set("is_hot", "1");
//			ivo.set("team_buy", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//首页搜索商品
	public static void IndexPageSearchGoods()
	{
		String url ="http://localhost:8080/hslg/api/goods/profile/search.do";
//		String url ="http://120.25.253.240:8080/hslg/api/goods/profile/search.do";
		IVO ivo =new IVO();
		try {
			ivo.set("key_words", "香满园");
			ivo.set("page", "1");
			ivo.set("page_size", "50");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查询商家详情
	public static void queryGoodsDetail()
	{
//		String url ="http://localhost:8080/hslg/api/goods/profile/detail.do";
		String url ="http://120.25.253.240:8080/hslg/api/goods/profile/detail.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "1");
			ivo.set("user_id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//查询商家参数
	public static void queryGoodsAttribute()
	{
		String url ="http://localhost:8080/hslg/api/goods/attribute/list.do";
//		String url ="http://120.25.253.240:8080/hslg/api/goods/attribute/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("goods_id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查询商品评论
	public static void queryGoodsJudgement()
	{
//		String url ="http://localhost:8080/hslg/api/goods/judgement/list.do";
		String url ="http://120.25.253.240:8080/hslg/api/goods/judgement/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("goods_id", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//添加商品评论
	public static void addGoodsJudgement()
	{
		String url ="http://localhost:8080/hslg/api/goods/judgement/add.do";
//		String url ="http://120.25.253.240:8080/hslg/api/goods/judgement/add.do";
		IVO ivo =new IVO();
		try {
			ivo.set("goods_id", "1");
			ivo.set("user_id", "1");
			ivo.set("content", "发货太慢，差评!!!");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//分页查询商家优惠券
	public static void queryShopCouponPage()
	{
//		String url ="http://localhost:8080/hslg/api/shop/coupon/list.do";
		String url ="http://120.25.253.240:8080/hslg/api/shop/coupon/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("shop_id", "1");
			ivo.set("shop_id", "19");
			ivo.set("page", "1");
			ivo.set("page_size", "1");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//分页查询商家优惠券图文详情
	public static void queryShopCouponRemark()
	{
//		String url ="http://localhost:8080/hslg/api/shop/coupon/remark.do";
		String url ="http://120.25.253.240:8080/hslg/api/shop/coupon/remark.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "3");
			ivo.set("id", "3000");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//创建订单
	public static void createOrder()
	{
		String url ="http://localhost:8080/hslg/api/order/profile/add.do";
//		String url ="http://120.25.253.240:8080/hslg/api/order/profile/add.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("address_id", "1");	
			ivo.set("money", "137.60");
			ivo.set("transfer_fee", "0");
			ivo.set("order_message", "请尽快发货");	
			ivo.set("order_type", "3");	//1在线支付2货到付款3水票支付
			DataSet items =new DataSet();
			Row irow =new Row();
			irow.put("goods_id", "1");
			irow.put("goods_price", "68.80");
			irow.put("goods_num", "1");
			items.add(irow);
			irow =new Row();
			irow.put("goods_id", "2");
			irow.put("goods_price", "68.80");
			irow.put("goods_num", "1");
			items.add(irow);
			ivo.set("items", items);
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//用户分页查询自己的订单
	public static void queryUserOrder()
	{
//		String url ="http://localhost:8080/hslg/api/order/profile/list.do";
		String url ="http://120.25.253.240:8080/hslg/api/order/profile/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "4");
			ivo.set("user_id", "1");
			ivo.set("state", "");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//订单项
	public static void queryOrderItems()
	{
		String url ="http://localhost:8080/hslg/api/order/profile/detail.do";
//		String url ="http://120.25.253.240:8080/hslg/api/order/profile/detail.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//用户查询自己的优惠券
	public static void queryUserCoupon()
	{
//		String url ="http://localhost:8080/hslg/api/user/coupon/list.do";
		String url ="http://120.25.253.240:8080/hslg/api/user/coupon/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("user_id", "207");
			ivo.set("user_id", "493");
			ivo.set("user_id", "206");
//			ivo.set("state", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//创建用户消费优惠券订单
	public static void createCouponOrder()
	{
		String url ="http://localhost:8080/hslg/api/user/coupon/order/add.do";
//			String url ="http://120.25.253.240:8080/hslg/api/user/coupon/order/add.do";
		IVO ivo =new IVO();
		try {
			ivo.set("shop_id", "1");
			ivo.set("user_id", "1");
			ivo.set("money", "80");
			DataSet items =new DataSet();
			Row irow =new Row();
			irow.put("coupon_id", "1");
			irow.put("price", "80");
			irow.put("num", "1");
			items.add(irow);
			ivo.set("items", items);
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//用户消费优惠券记录汇总页面
	public static void userCouponSummary()
	{
//		String url ="http://localhost:8080/hslg/api/user/coupon/order/summary.do";
			String url ="http://120.25.253.240:8080/hslg/api/user/coupon/order/summary.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
//			ivo.set("month", "2015-06");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//查询指定月份的用户消费优惠券的详情
	public static void userCouponMonthList()
	{
		String url ="http://localhost:8080/hslg/api/user/coupon/order/month_list.do";
//			String url ="http://120.25.253.240:8080/hslg/api/user/coupon/order/month_list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("month", "2015-06");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//上传头像
	public static void uploadAvatar()
	{
//		String url ="http://localhost:8080/hslg/api/user/upload_avatar.do";
			String url ="http://120.25.253.240:8080/hslg/api/user/upload_avatar.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "1");
			String pictrue = Base64Util.GetImageStr("/Users/TongJianbo/Desktop/123.jpg");
			ivo.set("picture_base64_str", Base64Util.encode(pictrue.getBytes()));
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//用户分页查询自己的礼品
	public static void queryUserGiftPage()
	{
		String url ="http://localhost:8080/hslg/api/user/gift/list.do";
//		String url ="http://120.25.253.240:8080/hslg/api/user/gift/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("user_id", "7");
//			ivo.set("state", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//用户查询礼品详情
	public static void queryUserGiftDetail()
	{
		String url ="http://localhost:8080/hslg/api/user/gift/detail.do";
//		String url ="http://120.25.253.240:8080/hslg/api/user/gift/detail.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//用户兑换礼品
	public static void queryExchangeGift()
	{
		String url ="http://localhost:8080/hslg/api/user/gift/exchange.do";
//		String url ="http://120.25.253.240:8080/hslg/api/user/gift/exchange.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("gift_id", "1");
			ivo.set("exchange_num", "1");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//微信app支付
	public static void weixinAppPay()
	{
		String url ="http://localhost:8080/hslg/api/pay/weixin/app/validate.do";
//		String url ="http://120.25.253.240:8080/hslg/api/pay/weixin/app/validate.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("order_id", "1");
			ivo.set("app_ip", "192.168.11.99");
			ivo.set("service_name", "cxhlWeiXinAppPayService");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//微信app支付
	public static void alipayAppPay()
	{
//		String url ="http://localhost:8080/hslg/api/pay/alipay/app/validate.do";
		String url ="http://120.25.253.240:8080/hslg/api/pay/alipay/app/validate.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "4");
			ivo.set("order_id", "8");
			ivo.set("service_name", "hslgAlipayAppPayService");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
//	用户分享
	public static void userShare()
	{
//		String url ="http://localhost:8080/hslg/api/user/share/add.do";
		String url ="http://120.25.253.240:8080/hslg/api/user/share/add.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("type", "1");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	优惠券抽奖
	public static void lotteryCoupon()
	{
//		String url ="http://localhost:8080/hslg/api/user/lottery/coupon.do";
		String url ="http://120.25.253.240:8080/hslg/api/user/lottery/coupon.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("user_id", "493");
//			ivo.set("user_id", "2");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
//	二维码抽奖
	public static void lotteryQRCode()
	{
		String url ="http://localhost:8080/hslg/api/user/lottery/qrcode.do";
//		String url ="http://120.25.253.240:8080/hslg/api/user/lottery/qrcode.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
//	电台抽奖
	public static void lotteryRadio()
	{
		String url ="http://localhost:8080/hslg/api/user/lottery/radio.do";
//		String url ="http://120.25.253.240:8080/hslg/api/user/lottery/radio.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void monitorS2WX()
	{
		String url ="https://api.mch.weixin.qq.com/pay/unifiedorder";
		String postStr ="<xml><appid>wx44e3ee46a26f4e21</appid><mch_id>1251662201</mch_id><device_info></device_info><nonce_str>RGDJ6RQ62Y5MMHFYEHVFU5RUKTTL</nonce_str><sign>60D59E27413821B92EE1C4F2798774D3</sign><body>订单:2015062217343610001支付备注</body><detail></detail><attach></attach><out_trade_no>2015062217343610001</out_trade_no><fee_type>CNY</fee_type><total_fee>8000</total_fee><spbill_create_ip>192.168.11.99</spbill_create_ip><time_start></time_start><time_expire></time_expire><goods_tag></goods_tag><notify_url>http://localhost:8080/hslg/notify/weixin/pay/app.do?order_no=3kLVUn/XTRB1bpSLU+EXAKhrD+UkGtrb1FIqwzqTASU=</notify_url><trade_type>APP</trade_type><product_id></product_id><openid></openid></xml>";
		String response ="";
		try {
			response =NetUtil.getNetResponse(url, postStr);
			response =new String(response.getBytes("utf-8"),"GBK");
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("response====>> \n"+response);
	}
	
	//查询用户的抽奖次数
	public static void lotteryNum()
	{
//		String url ="http://localhost:8080/hslg/api/user/lottery/lottery_num.do";
		String url ="http://120.25.253.240:8080/hslg/api/user/lottery/lottery_num.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查询用户的抽奖记录
	public static void lotteryList()
	{
//		String url ="http://localhost:8080/hslg/api/user/lottery/list.do";
		String url ="http://120.25.253.240:8080/hslg/api/user/lottery/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//查询爱心捐赠界面数据
	public static void queryLovePage()
	{
//		String url ="http://localhost:8080/hslg/api/convenient/love/profile/list.do";
		String url ="http://120.25.253.240:8080/hslg/api/convenient/love/profile/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "4");
//			ivo.set("user_id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//爱心捐赠
	public static void loveDonateByAlipay()
	{
		String url ="http://localhost:8080/hslg/api/pay/alipay/app/validate.do";
//		String url ="http://120.25.253.240:8080/hslg/api/pay/alipay/app/validate.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("order_id", "1");
			ivo.set("money", "100");
			ivo.set("service_name", "hslgAlipayAppPayLoveDonateService");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//爱心捐赠
	public static void loveDonateByAlipayNotify()
	{
		String url ="http://localhost:8080/hslg/api/convenient/love/profile/notify.do";
//		String url ="http://120.25.253.240:8080/hslg/api/convenient/love/profile/notify.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("order_no", "2015091321022701915");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//公益项目捐赠
	public static void projectDonateByAlipay()
	{
		String url ="http://localhost:8080/hslg/api/pay/alipay/app/validate.do";
//		String url ="http://120.25.253.240:8080/hslg/api/pay/alipay/app/validate.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("order_id", "1");
			ivo.set("money", "50");
			ivo.set("service_name", "hslgAlipayAppPayProjectDonateService");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//公益项目捐赠
	public static void projectDonateByAlipayNotify()
	{
		String url ="http://localhost:8080/hslg/api/convenient/project/profile/notify.do";
//		String url ="http://120.25.253.240:8080/hslg/api/convenient/project/profile/notify.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("order_no", "2015091322311101915");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//善小档案捐赠
	public static void archiveDonateByAlipay()
	{
		String url ="http://localhost:8080/hslg/api/pay/alipay/app/validate.do";
//		String url ="http://120.25.253.240:8080/hslg/api/pay/alipay/app/validate.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("order_id", "1");
			ivo.set("money", "150");
			ivo.set("service_name", "hslgAlipayAppPayArchiveDonateService");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//善小档案捐赠
	public static void archiveDonateByAlipayNotify()
	{
		String url ="http://localhost:8080/hslg/api/convenient/archive/profile/notify.do";
//		String url ="http://120.25.253.240:8080/hslg/api/convenient/archive/profile/notify.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("order_no", "2015091322321801915");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//分页查询档案
	public static void queryArchivePage()
	{
		String url ="http://localhost:8080/hslg/api/convenient/archive/profile/list.do";
//		String url ="http://120.25.253.240:8080/hslg/api/convenient/archive/profile/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//分页查询公益项目
	public static void queryProjectPage()
	{
		String url ="http://localhost:8080/hslg/api/convenient/project/profile/list.do";
//		String url ="http://120.25.253.240:8080/hslg/api/convenient/project/profile/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//分页查询便民信息
	public static void queryConvennientInfoPage()
	{
		String url ="http://localhost:8080/hslg/api/convennient/info/profile/list.do";
//			String url ="http://120.25.253.240:8080/hslg/api/convennient/info/profile/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			ivo.set("type_id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//分页查询便民信息
	public static void queryCommonServiceTypePage()
	{
//		String url ="http://localhost:8080/hslg/api/convennient/commonservice/type/list.do";
		String url ="http://120.25.253.240:8080/hslg/api/convennient/commonservice/type/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//分页查询便民信息
	public static void queryCommonServicePage()
	{
//		String url ="http://localhost:8080/hslg/api/convennient/commonservice/profile/list.do";
		String url ="http://120.25.253.240:8080/hslg/api/convennient/commonservice/profile/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			ivo.set("type_id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查询分享的图片与标题
	public static void queryShare()
	{
		String url ="http://localhost:8080/hslg/api/promsg/share.do";
//			String url ="http://120.25.253.240:8080/hslg/api/promsg/share.do";
		IVO ivo =new IVO();
		try {
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查询分享的图片与标题
	public static void querySysInfoPage()
	{
		String url ="http://localhost:8080/hslg/api/mailbox/sysinfo/list.do";
//			String url ="http://120.25.253.240:8080/hslg/api/mailbox/sysinfo/list.do";
		IVO ivo =new IVO();
		try {
			String json =  VOConvert.ivoToJson(ivo);
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查询分享的图片与标题
	public static void querySysinfoDetail()
	{
		String url ="http://localhost:8080/hslg/api/mailbox/sysinfo/detail.do";
//			String url ="http://120.25.253.240:8080/hslg/api/mailbox/sysinfo/detail.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查询分享的图片与标题
	public static void queryUserInfoPage()
	{
		String url ="http://localhost:8080/hslg/api/mailbox/userletter/list.do";
//			String url ="http://120.25.253.240:8080/hslg/api/mailbox/userletter/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前============@@@ ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查询分享的图片与标题
	public static void queryUserInfoDetail()
	{
		String url ="http://localhost:8080/hslg/api/mailbox/userletter/detail.do";
//			String url ="http://120.25.253.240:8080/hslg/api/mailbox/userletter/detail.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//首页查询系统广播
	public static void indexPageQueryBroadcast()
	{
//		String url ="http://localhost:8080/hslg/api/mailbox/sysbroadcast/top-page-broadcast.do";
			String url ="http://120.25.253.240:8080/hslg/api/mailbox/sysbroadcast/top-page-broadcast.do";
		IVO ivo =new IVO();
		try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//分页查询系统广播
	public static void querySysBroadcastPage()
	{
		String url ="http://localhost:8080/hslg/api/mailbox/sysbroadcast/list.do";
//			String url ="http://120.25.253.240:8080/hslg/api/mailbox/sysbroadcast/list.do";
		IVO ivo =new IVO();
		try {
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查询分享的图片与标题
	public static void querySysBroadcastDetail()
	{
		String url ="http://localhost:8080/hslg/api/mailbox/sysbroadcast/detail.do";
//			String url ="http://120.25.253.240:8080/hslg/api/mailbox/sysbroadcast/detail.do";
		IVO ivo =new IVO();
		try {
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查询善小财务汇总
	public static void queryFinance()
	{
//		String url ="http://localhost:8080/hslg/api/convenient/finance/summary.do";
//			String url ="http://120.25.253.240:8080/hslg/api/convenient/finance/summary.do";
			String url ="http://120.25.253.240:8080/hslg/api/convenient/finance/summary.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//微信app支付－爱心捐款
	public static void weixinAppPayLoveDonate()
	{
//			String url ="http://localhost:8080/hslg/api/pay/weixin/app/validate.do";
		String url ="http://120.25.253.240:8080/hslg/api/pay/weixin/app/validate.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("user_id", "2");
			ivo.set("money", "1");
			ivo.set("service_name", "hslgWeiXinAppPayService");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n 加密前 ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			OVO ovo =VOConvert.jsonToOvo(res);
			System.out.println("\n decode response json ===========>>\n"+res);
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static void main(String args[])
	{
		System.out.println("\n==========request start=============");
//获取最新版本		
//		getVersion();
//		发送短信验证码
//		sendSms();
//		注册
//		register();
//		登陆
//		login();
//		注销
//		logout();
//		登陆
//		login_with_incrypt_str();
//		修改密码
//		changePassword();
//		找回密码时发送短信验证码
//		sendSmsResetPwd();
//		重置密码
//		resetPassword();
//		根据用户编号查询用户信息
//		queryProfile();
//		修改个人信息
//		updateProfile();
//		上传头像
//		uploadAvatar();
//		mailtest();
//		查询广告列表
//		queryAllAds();
//		查询广告详情
//		queryAdDetail();
//		查询所有的城市和城市所辖区域
//		queryCityAndZone();
//		检查地理位置文件是否要更新
//		checkGeographyFileVersion();
//		收货地址列表
//		queryUserAddressPaeg();
//		查询默认收获地址
//		queryUserDefaultAddress();
//		添加收货地址
//		addUserAddress();
//		删除收货地址
//		deleteUserAddress();
//		更换手机号码时发送短信验证码
//		sendSmsWhenChangeTelephone();
//		更换手机号码
//		changeTelephone();
//		设置支付密码
//		setPayPassword();
//		添加意见反馈
//		addCustomerTip();
//		分页查询用户收藏
//		queryPageUserCollection();
//		用户添加收藏
//		userAddCollection();
//		用户批量删除收藏
//		userDeleteCollection();
//		查询商品分类
//		queryGoodsType();
//		分页查询商品
//		queryGoodsPage();
//		查询商品详情
//		queryGoodsDetail();
//		查询商品参数
//		queryGoodsAttribute();
//		查询商品评论
//		queryGoodsJudgement();
//		添加商品评论
//		addGoodsJudgement();
//		创建订单
//		createOrder();
//		用户分页查询自己的订单
//		queryUserOrder();
//		订单项
//		queryOrderItems();
//		微信app支付
//		weixinAppPay();
//		支付宝app支付
//		alipayAppPay();
//		模拟调用统一支付接口
//		monitorS2WX();
//		用户分享
//		userShare();
//		查询爱心捐赠界面数据
//		queryLovePage();
//		爱心捐赠
//		loveDonateByAlipay();
//		爱心捐赠通知
//		loveDonateByAlipayNotify();
//		分页查询档案
//		queryArchivePage();
//		档案捐赠
//		archiveDonateByAlipay();
//		档案捐赠通知
//		archiveDonateByAlipayNotify();
//		分页查询公益项目
//		queryProjectPage();
//		项目捐赠
//		projectDonateByAlipay();
//		项目捐赠通知
//		projectDonateByAlipayNotify();
//		查询分享的图片与标题
//		queryShare();
//		分页查询系统信息
//		querySysInfoPage();
//		查询系统信息详情
//		querySysinfoDetail();
//		分页查询系统广播
//		querySysBroadcastPage();
//		查询系统广播详情
//		querySysBroadcastDetail();
//		查询善小财务汇总
//		queryFinance();
//		分页查询便民信息
//		queryConvennientInfoPage();
//		首页搜索商品
//		IndexPageSearchGoods();
//		indexPageQueryBroadcast();
//		分页用户信息
//		queryUserInfoPage();
//		queryUserInfoDetail();
//		分页查询通用服务分类
//		queryCommonServiceTypePage();
//		分页查询通用服务
//		queryCommonServicePage();
//		爱心捐款
		weixinAppPayLoveDonate();
//		System.out.println("\n==========request  end=============");
	}
	
}
