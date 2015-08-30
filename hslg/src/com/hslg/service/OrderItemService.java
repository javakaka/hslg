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

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2014-12-26 下午3:14:51  
 * 类说明: order item
 */

@Component("hslgOrderItemService")
public class OrderItemService extends Service{

	public OrderItemService() {
		
	}
	
	/**
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql ="select a.*,b.order_no,c.name as goods_name from hslg_order_item a "
		+" left join hslg_order b on a.order_id=b.id "
		+" left join hslg_goods c on a.goods_id=c.id "
		+" where a.id='"+id+"'"; 
		row =queryRow(sSql);
		return row;
	}
	/**
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public DataSet findOrderItems(String order_id)
	{
		DataSet ds =new DataSet();
		String sSql ="select a.*,b.`name` as goods_name from hslg_order_item a "
				+" left join hslg_goods b on a.goods_id=b.id  "
				+" where a.order_id='"+order_id+"' ";
		ds =queryDataSet(sSql);
		return ds;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("hslg_order_item", "id", 1);
		row.put("id", id);
		num =insert("hslg_order_item", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		Assert.notNull(id);
		num =update("hslg_order_item", row, " id='"+id+"'");
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
		String sql =" select * from "
		+" ( "
		+" select a.*,b.order_no,c.name as goods_name from hslg_order_item a " 
		+" left join hslg_order b on a.order_id=b.id "
		+" left join hslg_goods c on a.goods_id=c.id "
		+" ) as tab  "
		+" where 1=1  ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql  =" select count(*) from "
				+" ( "
				+" select a.*,b.order_no,c.name as goods_name from hslg_order_item a " 
				+" left join hslg_order b on a.order_id=b.id "
				+" left join hslg_goods c on a.goods_id=c.id "
				+" ) as tab  "
				+" where 1=1  ";
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
	public DataSet list(String type,String key_word,String page,String page_size)
	{
		int iStart =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		
		DataSet ds =new DataSet();
		String sSql ="select a.id,a.type,a.c_name,c.FILE_PATH from hslg_order_item a "
				+" left join file_attach_control b on a.id=b.DEAL_CODE and b.DEAL_TYPE='shop_icon' "
				+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID "
				+" where 1=1 ";
		if( !StringUtils.isEmptyOrNull(type))
		{
			sSql +=" and a.type='"+type+"' ";
		}
		if( !StringUtils.isEmptyOrNull(key_word))
		{
			sSql +=" and a.c_name like '%"+key_word+"%' ";
		}
		sSql +=" limit "+iStart+" , "+page_size;	
		ds =queryDataSet(sSql);
		Setting setting =SettingUtils.get();
		String url =setting.getSiteUrl();
		if(StringUtils.isEmptyOrNull(url))
		{
			url ="";
		}
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
						file_path =url+"/"+file_path.substring(iPos);
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
			sql = "delete from hslg_order_item where id in(" + id + ")";
			update(sql);
		}
	}
	
}