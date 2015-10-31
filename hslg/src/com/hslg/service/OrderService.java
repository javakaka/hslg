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
 * 类说明: order 
 */

@Component("hslgOrderService")
public class OrderService extends Service{

	public OrderService() {
		
	}
	
	/**
	 * 点击进入商家详情
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql ="select * from hslg_order a  "
				+" where a.id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row findDetail(String id)
	{
		Row row =null;
		String sSql ="select a.*,b.username,b.telephone from hslg_order a  left join hslg_users b on a.user_id=b.id "
				+" where a.id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row findByOrderNo(String order_no)
	{
		Row row =null;
		String sSql ="select * from hslg_order where order_no='"+order_no+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("hslg_order", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		num =insert("hslg_order", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		Assert.notNull(id);
		num =update("hslg_order", row, " id='"+id+"'");
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
		+" (select a.*,b.username,b.telephone from hslg_order a " 
		+" left join hslg_users b on a.user_id=b.id "
		+" order by a.create_time desc "
		+" ) as tab "
		+" where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql =" select count(*) from "
		+" (select a.*,b.username,b.telephone from hslg_order a " 
		+" left join hslg_users b on a.user_id=b.id "
		+" order by a.create_time desc "
		+" ) as tab "
		+" where 1=1 ";
		countSql += restrictions;
		countSql += orders;
		long total = count(countSql);
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		int startPos = (pageable.getPageNumber() - 1) * pageable.getPageSize();
		sql += " limit " + startPos + " , " + pageable.getPageSize();
		dataSet = queryDataSet(sql);
		page = new Page(dataSet, total, pageable);
		return page;
	}
	
	
	public DataSet list(String user_id,String state,String page,String page_size)
	{
		int iStart =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		DataSet ds =new DataSet();
		String sSql ="select * from hslg_order "
				+" where user_id='"+user_id+"' " ;
		if( !StringUtils.isEmptyOrNull(state))
		{
			sSql +=" and state='"+state+"' ";
		}
		sSql +=" order by create_time desc ";
		sSql +=" limit "+iStart+" , "+page_size;	
		ds =queryDataSet(sSql);
		return ds;
	}
	
	@SuppressWarnings("unchecked")
	public DataSet listWithOneGoods(String user_id,String state,String page,String page_size)
	{
		int iStart =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		DataSet ds =new DataSet();
		String sSql ="select a.id,a.state,a.user_id,a.create_time,a.order_no,"
		+ "a.transfer_state,b.goods_id,b.goods_num,b.goods_price,fu.file_path ,"
		+ "d.name as goods_name,d.unit,a.order_type from hslg_order a "
		+" left join hslg_order_item b on b.order_id=a.id "
		+" left join file_attach_control fc on fc.DEAL_CODE=b.goods_id and fc.DEAL_TYPE='goods_icon' " 
		+" left join file_attach_upload fu on fc.CONTROL_ID=fu.CONTROL_ID "
		+" left join hslg_goods d on b.goods_id=d.id "
		+" where a.user_id='"+user_id+"' " ;
		if( !StringUtils.isEmptyOrNull(state))
		{
			sSql +=" and a.state='"+state+"' ";
		}
		sSql +=" group by a.id ";
		sSql +=" order by a.create_time desc ";
		sSql +=" limit "+iStart+" , "+page_size;	
		ds =queryDataSet(sSql);
		if(ds != null)
		{
			Setting setting =SettingUtils.get();
			String site_url =setting.getSiteUrl();
			int iPos =-1;
			for(int i =0; i<ds.size(); i++)
			{
				Row row =(Row)ds.get(i);
				String file_path =row.getString("file_path","");
				if(!StringUtils.isEmptyOrNull(file_path))
				{
					iPos =file_path.indexOf("/resources");
					if(iPos != -1)
					{
						file_path =file_path.substring(iPos,file_path.length());
						file_path =site_url+file_path;
						row.put("file_path", file_path);
						ds.set(i, row);
					}
				}
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
			sql = "delete from hslg_order_item where order_id in(" + id + ")";
			update(sql);
			sql = "delete from hslg_order where id in(" + id + ")";
			update(sql);
		}
	}
	
	//精确到秒的订单数量
	public int getOrderNumByCreateTime(String time)
	{
		int num =0;
		String sql ="select count(*) from hslg_order where create_time='"+time+"' ";
		num =Integer.parseInt(queryField(sql));
		return num;
	}
}