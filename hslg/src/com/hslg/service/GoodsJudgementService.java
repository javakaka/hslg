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
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2014-12-26 下午3:14:51  
 * 类说明: 商品评论
 */

@Component("hslgGoodsJudgementService")
public class GoodsJudgementService extends Service{

	public GoodsJudgementService() {
		
	}

	
	/**
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql ="select a.* from hslg_goods_judgement a "
				+" where a.id ='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public DataSet findByGoodsId(String goods_id)
	{
		DataSet ds =new DataSet();
		String sSql ="select a.*,c.`name`,c.telephone,c.avatar from hslg_goods_judgement a "
				+" left join hslg_users c on a.user_id =c.id "
				+" where a.goods_id ='"+goods_id+"' "
				+" order by a.create_time desc ";
		ds =queryDataSet(sSql);
		return ds;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public DataSet findPageByGoodsId(String goods_id,String page,String page_size)
	{
		int iStart =(Integer.parseInt(page) -1)*Integer.parseInt(page_size);
		DataSet ds =new DataSet();
		String sSql ="select a.*,c.`name`,c.telephone,c.avatar from hslg_goods_judgement a "
				+" left join hslg_users c on a.user_id =c.id "
				+" where a.goods_id ='"+goods_id+"' "
				+" order by a.create_time desc limit "+iStart+" ,"+page_size;
		ds =queryDataSet(sSql);
		String name_lable ="";
		for( int i=0;i<ds.size(); i++ )
		{
			Row temp =(Row)ds.get(i);
			String name =temp.getString("name","");
			String telephone =temp.getString("telephone","");
			if(!StringUtils.isEmptyOrNull(name))
			{
				name_lable =name.substring(0,1)+"***";
				if(name.length() > 1)
				{
					name_lable +=name.substring(name.length()-1,name.length());
				}
			}
			else
			{
				if(! StringUtils.isEmptyOrNull(telephone))
				{
					name_lable =telephone.substring(0,1)+"***"+telephone.substring(telephone.length()-1,telephone.length());
				}
				else
				{
					name_lable ="***";
				}
			}
			temp.put("name", name_lable);
			ds.set(i, temp);
		}
		return ds;
	}
	
	
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("hslg_goods_judgement", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		row.put("judge_date", DateUtil.getCurrentDate());
		num =insert("hslg_goods_judgement", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		Assert.notNull(id);
		num =update("hslg_goods_judgement", row, " id='"+id+"'");
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
		sql ="select a.* from hslg_goods_judgement a "
		+" order by a.seq ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from hslg_goods_judgement a "
				+" order by a.seq ";
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
	
	public DataSet list(String goods_id,String page,String page_size)
	{
		DataSet ds =null;
		String sSql =" select a.id,a.goods_id,a.user_id,a.content,a.judge_date , "
				+" b.username from hslg_goods_judgement a "
				+" left join hslg_users b on a.user_id=b.id "
				+" where a.state='2' and goods_id='"+goods_id+"' ";
		int iStart =(Integer.parseInt(page) -1 )*Integer.parseInt(page_size);
		sSql += " order by a.create_time desc ";
		sSql += " limit "+iStart+" , "+page_size;
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
			sql = "delete from hslg_goods_judgement where id in(" + id + ")";
			update(sql);
		}
	}
	
	
}