/**
 * 
 */
package com.hslg.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2014-12-26 下午3:14:51  
 * 类说明: 商品
 */

@Component("hslgGoodsService")
public class GoodsService extends Service{

	public GoodsService() {
		
	}

	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql ="select * from hslg_goods  "
				+" where id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	public Row findDetailWithoutPicture(String id)
	{
		Row row =null;
		String sSql ="select a.id,a.type_id,a.`name`,a.summary,a.weight,a.unit,"
				+ "a.raw_price,a.coupon_price,a.is_coupon,a.left_num,a.sale_num,a.score,a.detail,a.state,a.total_num, "
				+ "d.type_name "
				+" from hslg_goods a "
				+" left join hslg_goods_type d on a.type_id=d.id "
				+" where a.id='"+id+"' ";
		row =queryRow(sSql);
		Setting setting =SettingUtils.get();
		String site_url =setting.getSiteUrl();
		String file_path =row.getString("file_path","");
		int iPos =-1;
		if(!StringUtils.isEmptyOrNull(file_path))
		{
			iPos =file_path.indexOf("resources");
			if(iPos != -1)
			{
				file_path =file_path.substring(iPos);
				file_path =site_url+"/"+file_path;
			}
		}
		else
		{
			file_path ="";
		}
		row.put("file_path", file_path);
		return row;
	}
	public Row findDetail(String id)
	{
		Row row =null;
		String sSql ="select a.id,a.type_id,a.`name`,a.summary,a.weight,a.unit,"
				+ "a.raw_price,a.coupon_price,a.is_coupon,a.left_num,a.sale_num,a.score,a.detail,a.state,a.total_num, "
				+ "d.type_name,c.FILE_PATH "
				+" from hslg_goods a "
				+" left join file_attach_control b on b.DEAL_CODE=a.id and b.DEAL_TYPE='goods_icon' "
				+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID "
				+" left join hslg_goods_type d on a.type_id=d.id "
				+" where a.id='"+id+"' ";
		row =queryRow(sSql);
		Setting setting =SettingUtils.get();
		String site_url =setting.getSiteUrl();
		String file_path =row.getString("file_path","");
		int iPos =-1;
		if(!StringUtils.isEmptyOrNull(file_path))
		{
			iPos =file_path.indexOf("resources");
			if(iPos != -1)
			{
				file_path =file_path.substring(iPos);
				file_path =site_url+"/"+file_path;
			}
		}
		else
		{
			file_path ="";
		}
		row.put("file_path", file_path);
		return row;
	}
	
	@SuppressWarnings("unchecked")
	public DataSet findPictureList(String id)
	{
		DataSet ds =null;
		String sSql ="select c.FILE_PATH "
				+" from file_attach_control b  "
				+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID "
				+" where  b.DEAL_CODE='"+id+"' and b.DEAL_TYPE='goods_icon'";
		ds =queryDataSet(sSql);
		Setting setting =SettingUtils.get();
		String site_url =setting.getSiteUrl();
		for(int i=0;i<ds.size(); i++ )
		{
			Row temp =(Row)ds.get(i);
			String file_path =temp.getString("file_path","");
			int iPos =-1;
			if(!StringUtils.isEmptyOrNull(file_path))
			{
				iPos =file_path.indexOf("resources");
				if(iPos != -1)
				{
					file_path =file_path.substring(iPos);
					file_path =site_url+"/"+file_path;
				}
			}
			else
			{
				file_path ="";
			}
			temp.put("file_path", file_path);
			ds.set(i, temp);
		}
		return ds;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isExisted(String id)
	{
		boolean bool =true;
		String sSql ="select count(*) from hslg_goods  "
				+" where id='"+id+"' ";
		int num =Integer.parseInt(queryField(sSql));
		if(num == 0)
		{
			bool =false;
		}
		return bool;
	}
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row findRemark(String id)
	{
		Row row =null;
		String sSql ="select detail from hslg_goods  "
				+" where id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("hslg_goods", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		num =insert("hslg_goods", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		Assert.notNull(id);
		num =update("hslg_goods", row, " id='"+id+"'");
		return num;
	}
	
	
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Page queryPage() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		String sql ="select * from ( "
		+" select a.id,a.type_id,a.`name`,a.summary,a.weight,a.unit,a.raw_price,a.coupon_price,a.is_coupon, "
		+" a.total_num,a.left_num,a.sale_num,a.hot,a.team_buy,a.state,a.create_time,b.type_name from hslg_goods a "
		+" left join hslg_goods_type b on a.type_id=b.id "
		+" ) as tab  "
		+" where 1=1 and state != '-1' ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from ( "
				+" select a.id,a.type_id,a.`name`,a.summary,a.weight,a.unit,a.raw_price,a.coupon_price,a.is_coupon, "
				+" a.total_num,a.left_num,a.sale_num,a.hot,a.team_buy,a.state,a.create_time,b.type_name from hslg_goods a "
				+" left join hslg_goods_type b on a.type_id=b.id "
				+" ) as tab  "
				+" where 1=1 and state != '-1' ";
		countSql += restrictions;
		countSql += orders;
		long total = count(countSql);
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		int startPos = (pageable.getPageNumber() - 1) * pageable.getPageSize();
		sql += " limit " + startPos + " , " + pageable.getPageSize();
		DataSet dataSet = queryDataSet(sql);
		page = new Page(dataSet, total, pageable);
		return page;
	}
	
	@SuppressWarnings("unchecked")
	public DataSet findSimilarityGoods(String goods_id,String type_id)
	{
		DataSet ds =new DataSet();
		String sSql ="select a.id,a.type_id,a.`name`,a.summary,a.weight,a.unit,"
				+ "a.raw_price,a.coupon_price,a.is_coupon,a.left_num,a.sale_num,a.score,c.FILE_PATH "
				+" from hslg_goods a "
				+" left join file_attach_control b on b.DEAL_CODE=a.id and b.DEAL_TYPE='goods_icon' "
				+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID "
				+" where 1=1 and a.state='1' and a.left_num > 0 ";
				if(! StringUtils.isEmptyOrNull(type_id))
				{
					sSql +=" and a.type_id='"+type_id+"' ";
				}
				sSql +=" and a.id > "+goods_id +" group by a.id limit 0,2 ";
		ds =queryDataSet(sSql);
		Setting setting =SettingUtils.get();
		String site_url =setting.getSiteUrl();
		if(ds != null && ds.size() >0 )
		{
			for(int i=0;i<ds.size(); i++)
			{
				Row row =(Row)ds.get(i);
				String file_path =row.getString("file_path","");
				int iPos =-1;
				if(!StringUtils.isEmptyOrNull(file_path))
				{
					iPos =file_path.indexOf("resources");
					if(iPos != -1)
					{
						file_path =file_path.substring(iPos);
						file_path =site_url+"/"+file_path;
					}
				}
				else
				{
					file_path ="";
				}
				row.put("file_path", file_path);
				ds.set(i, row);
			}
		}
		return ds;
	}
	
	@SuppressWarnings("unchecked")
	public DataSet list(String type_id,String is_hot, String team_buy,String page,String page_size)
	{
		int iStart =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		DataSet ds =new DataSet();
		String sSql ="select a.id,a.type_id,a.`name`,a.summary,a.weight,a.unit,"
		+ "a.raw_price,a.coupon_price,a.is_coupon,a.left_num,a.sale_num,a.score,c.FILE_PATH "
		+" from hslg_goods a "
		+" left join file_attach_control b on b.DEAL_CODE=a.id and b.DEAL_TYPE='goods_icon' "
		+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID "
		+" where 1=1 and a.state='1' and a.left_num > 0 ";
		if(! StringUtils.isEmptyOrNull(type_id))
		{
			sSql +=" and a.type_id='"+type_id+"' ";
		}
		if(! StringUtils.isEmptyOrNull(is_hot) && is_hot.equals("1"))
		{
			sSql +=" and a.hot='1' ";
		}
		if(! StringUtils.isEmptyOrNull(team_buy) && team_buy.equals("1"))
		{
			sSql +=" and a.team_buy='1' ";
		}
		sSql +=" group by a.id limit "+iStart+" , "+page_size;	
		ds =queryDataSet(sSql);
		Setting setting =SettingUtils.get();
		String site_url =setting.getSiteUrl();
		if(ds != null && ds.size() >0 )
		{
			for(int i=0;i<ds.size(); i++)
			{
				Row row =(Row)ds.get(i);
				String file_path =row.getString("file_path","");
				int iPos =-1;
				if(!StringUtils.isEmptyOrNull(file_path))
				{
					iPos =file_path.indexOf("resources");
					if(iPos != -1)
					{
						file_path =file_path.substring(iPos);
						file_path =site_url+"/"+file_path;
					}
				}
				else
				{
					file_path ="";
				}
				row.put("file_path", file_path);
				ds.set(i, row);
			}
		}
		return ds;
	}
	@SuppressWarnings("unchecked")
	public DataSet search(String key_words,String page,String page_size)
	{
		int iStart =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		DataSet ds =new DataSet();
		String sSql ="select a.id,a.type_id,a.`name`,a.summary,a.weight,a.unit,"
				+ "a.raw_price,a.coupon_price,a.is_coupon,a.left_num,a.sale_num,a.score,c.FILE_PATH "
				+" from hslg_goods a "
				+" left join file_attach_control b on b.DEAL_CODE=a.id and b.DEAL_TYPE='goods_icon' "
				+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID "
				+" where 1=1 and a.state='1' and a.left_num > 0 ";
		if(! StringUtils.isEmptyOrNull(key_words))
		{
			sSql +=" and a.name like '%"+key_words+"%' ";
		}
		sSql +=" limit "+iStart+" , "+page_size;	
		ds =queryDataSet(sSql);
		Setting setting =SettingUtils.get();
		String site_url =setting.getSiteUrl();
		if(ds != null && ds.size() >0 )
		{
			for(int i=0;i<ds.size(); i++)
			{
				Row row =(Row)ds.get(i);
				String file_path =row.getString("file_path","");
				int iPos =-1;
				if(!StringUtils.isEmptyOrNull(file_path))
				{
					iPos =file_path.indexOf("resources");
					if(iPos != -1)
					{
						file_path =file_path.substring(iPos);
						file_path =site_url+"/"+file_path;
					}
				}
				else
				{
					file_path ="";
				}
				row.put("file_path", file_path);
				ds.set(i, row);
			}
		}
		return ds;
	}
	
	/**
	 * 删除
	 * 
	 * @Title: delete
	 * @param @param ids
	 * @return void
	 */
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public void delete(String... ids) {
		String id = "";
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (id.length() > 0) {
					id += ",";
				}
				id += "'" + String.valueOf(ids[i]) + "'";
			}
			sql = "update hslg_goods set state='-1' where  id in(" + id + ")";
			update(sql);
		}
	}
	
	public boolean isGoodsNumEnough(String goods_id,String num)
	{
		boolean bool =true;
		String sql ="select left_num from hslg_goods where id='"+goods_id+"' ";
		String left_num =queryField(sql);
		if(StringUtils.isEmptyOrNull(left_num))
		{
			left_num ="0";
		}
		int ileft_num =Integer.parseInt(left_num);
		int inum =Integer.parseInt(num);
		if(inum >ileft_num)
		{
			bool =false;
		}
		return bool;
	}
	
}