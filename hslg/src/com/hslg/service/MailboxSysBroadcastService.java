package com.hslg.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-1-1 下午4:24:59  
 * 类说明:规则消息业务处理类 
 */
@Component("hslgMailboxSysBroadcastService")
public class MailboxSysBroadcastService extends Service {

	public MailboxSysBroadcastService() 
	{
		
	}

	public int insert(Row row) 
	{
		int rowNum =0;
		int id =getTableSequence("hslg_sys_broadcast", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		rowNum =insert("hslg_sys_broadcast", row);
		return rowNum;
	}
	
	public Row findById(String id)
	{
		Row row =null;
		sql ="select * from hslg_sys_broadcast where id='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	public int update(Row row)
	{
		int rowNum =0;
		String id=row.getString("id");
		row.put("modify_time", DateUtil.getCurrentDateTime());
		update("hslg_sys_broadcast", row, " id='"+id+"'");
		return rowNum;
	}
	
	/*********************************管理后台**********************************/
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPage(Pageable pageable) {
		Page page = null;
		sql ="select * from hslg_sys_broadcast  where 1=1 "; 
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from hslg_sys_broadcast  where 1=1 "; 
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
	
	
	
	/**
	 * 删除
	 * 
	 * @Title: delete
	 * @param @param ids
	 * @return void
	 */
	public void delete(Long... ids) {
		String id = "";
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (id.length() > 0) {
					id += ",";
				}
				id += "'" + String.valueOf(ids[i]) + "'";
			}
			sql = "delete from hslg_sys_broadcast where id in(" + id + ")";
			update(sql);
		}
	}
	
	/*********************************管理后台**********************************/
	public DataSet list(String page,String page_size)
	{
		DataSet ds =new DataSet();
		int iStart =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		String sql ="select id,title,create_time,icon_url from hslg_sys_broadcast where state='1' order by create_time desc limit "+iStart+" , "+page_size;
		ds =queryDataSet(sql);
		return ds;
	}
	
	public DataSet indexPageBroadcast()
	{
		DataSet ds =new DataSet();
		String sql ="select id,title from hslg_sys_broadcast where state='1' and is_top='1' order by create_time desc ";
		ds =queryDataSet(sql);
		return ds;
	}
}