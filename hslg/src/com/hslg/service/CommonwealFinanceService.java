/**
 * 
 */
package com.hslg.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2014-12-26 下午3:14:51  
 * 类说明: 善小财务
 */

@Component("hslgCommonwealFinanceService")
public class CommonwealFinanceService extends Service{

	public CommonwealFinanceService() {
		
	}
	
	/**
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql ="select a.* from hslg_love_donate a "
				+" where a.id ='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("hslg_love_donate", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		num =insert("hslg_love_donate", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		Assert.notNull(id);
		num =update("hslg_love_donate", row, " id='"+id+"'");
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
		String sql ="select * from hslg_love_donate "
		+" where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from hslg_love_donate "
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
	
	public DataSet list()
	{
		DataSet ds =new DataSet();
		String sSql ="select * from hslg_love_donate  ";
		ds =queryDataSet(sSql);
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
			sql = "delete from hslg_love_donate where id in(" + id + ")";
			update(sql);
		}
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isNameExisted(String name)
	{
		boolean bool =true;
		String sql ="select count(*) from hslg_love_donate where name ='"+name+"'";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isExtraNameExisted(String id, String name)
	{
		boolean bool =true;
		String sql ="select count(*) from hslg_love_donate where type_name ='"+name+"' and id !='"+id+"'";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	
}