package com.hslg.controller.app;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.service.system.SystemUpload;
import com.ezcloud.framework.util.ResponseVO;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.hslg.service.GoodsAttributeService;
import com.hslg.service.GoodsJudgementService;
import com.hslg.service.GoodsService;
import com.hslg.service.GoodsTypeService;

@Controller("hslgPlatformAppGoodsController")
@RequestMapping({"/hslgpage/app/front/goods"})
public class GoodsContrller extends BaseController
{

  @Resource(name = "hslgGoodsTypeService")
  private GoodsTypeService goodsTypeService;

  @Resource(name="hslgGoodsService")
  private GoodsService goodsService;
  
  @Resource(name="hslgGoodsAttributeService")
  private GoodsAttributeService goodsAttributeService;
  
  @Resource(name="hslgGoodsJudgementService")
  private GoodsJudgementService goodsJudgementService;

  @Resource(name="frameworkUploadService")
  private SystemUpload systemUploadService;

  

  /**
   * 商品缩略图、图文详情、属性、评价
   * @param id
   * @param model
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  @RequestMapping("/detail")
  public String queryGoodsDetail(String goods_id,String user_id, ModelMap model) throws Exception
  {
	Assert.notNull(goods_id, "goods null");
	//goods icons 
    DataSet icon_ds = new DataSet();
    if (!StringUtils.isEmptyOrNull(goods_id))
    {
    	icon_ds = this.systemUploadService.getAttachList(goods_id, "goods_icon");
    }
    Setting setting = SettingUtils.get();
    String site_url = setting.getSiteUrl();
    if (StringUtils.isEmptyOrNull(site_url))
    {
      site_url = "";
    }
    if (icon_ds != null)
    {
      for (int i = 0; i < icon_ds.size(); i++)
      {
        Row row = (Row)icon_ds.get(i);
        String path = row.getString("path", "");
        if (!StringUtils.isEmptyOrNull(path))
        {
          path = site_url + path.replace("..", "");
          row.put("path", path);
          icon_ds.set(i, row);
        }
      }
    }
    Row icon_row =null;
    if(icon_ds != null && icon_ds.size()>0)
    {
    	icon_row =(Row)icon_ds.get(0);
    }
    Row goodsRow =goodsService.find(goods_id);
    //goods attribute
    Row attr_row = goodsAttributeService.findByGoodsId(goods_id);
    // goods judgement
    DataSet judgeDs =goodsJudgementService.findByGoodsId(goods_id);
    model.addAttribute("icon_row", icon_row);
    model.addAttribute("goods_row", goodsRow);
    model.addAttribute("attr_row", attr_row);
    model.addAttribute("judge_list", judgeDs);
    model.addAttribute("goods_id", goods_id);
    model.addAttribute("user_id", user_id);
    return "/hslgpage/app/front/goods/goods-detail";
  }
  
  @RequestMapping("/judge-page")
  @ResponseBody	
  public ResponseVO queryJudgePage(String goods_id,String page,String page_size, ModelMap model) throws Exception
  {
	  ResponseVO ovo =new ResponseVO(0);
	// goods judgement
	  DataSet judgeDs =goodsJudgementService.findPageByGoodsId(goods_id,page,page_size);
	  ovo.put("list", judgeDs);
	  return ovo;
	  
  }
  
}