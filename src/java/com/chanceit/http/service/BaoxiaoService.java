package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.ILevelDao;
import com.chanceit.http.dao.IBaoxiaoDao;
import com.chanceit.http.pojo.Baoxiao;
import com.chanceit.http.pojo.Member;

@Transactional
@Component("baoxiaoService")
public class BaoxiaoService implements IBaoxiaoService {
	@Autowired
	@Qualifier("baoxiaoDao")
	private IBaoxiaoDao baoxiaoDao;

	@Override
	public Baoxiao getBaoxiao(int baoxiaoId) {
		return baoxiaoDao.get(baoxiaoId);
	}

	@Override
	public String save(Baoxiao baoxiao) {
		try {
			return baoxiaoDao.save(baoxiao);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean saveBaoxiao(Baoxiao baoxiao) {
		try {
			baoxiaoDao.saveBaoxiao(baoxiao);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean delete(String ids, int accountId) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for (String id : idsAry) {
			list.add(Integer.parseInt(id));
		}
		return baoxiaoDao.delete(list, accountId);
	}

	@Override
	public boolean update(Baoxiao baoxiao) {
		baoxiaoDao.update(baoxiao);
		return true;
	}

	@Override
	public Baoxiao get(int baoxiaoId) {
		return baoxiaoDao.get(baoxiaoId);
	}

	@Override
	public Page getPageList(Page page, Object[] values) {
		String sql = "select b.*,(b.glf+b.jyf+b.byf+b.wxf+b.xcf+b.lqf+b.tcf+b.njf+b.bxf+b.qtf) as fee,"
				+ "d.driver_name as name,u.plate as plate from baoxiao b left join driver d on b.user_id=d.driver_id left join user u on u.user_id = b.car_id  where 1=1 ";
		// if(values[0]!=null && !values[0].equals("")){
		// sql.append(" and l.account.accountId
		// in(").append(values[0]).append(")");
		// }
		// if(values[1]!=null && !values[1].equals("")){
		// sql.append(" and l.baoxiaoName like
		// '%").append(values[1]).append("%'");
		// }
		if (values[0] != null && !values[0].equals("")) {
			sql += " and b.status in (" + values[0] + ")";
		}
		if (values[1] != null && !values[1].equals("")) {
			sql += " and d.team_id = " + values[1];
		}
		if(values[2]!=null && !values[2].equals("")){
			sql += " and d.driver_name like '%"+values[2]+"%'";//根据 报销人姓名 来搜索
		}
		sql += " order by b.id desc";
		return baoxiaoDao.getPageList(page, sql);
	}

	@Override
	public boolean deleteBaoxiao(String ids, int accountId) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for (String id : idsAry) {
			list.add(Integer.parseInt(id));
		}
		return baoxiaoDao.delete(list, accountId);
	}

	@Override
	public Baoxiao getByName(String baoxiaoName) throws Exception {
		String hql = " from Baoxiao where baoxiaoName=?";
		List list = baoxiaoDao.getList(hql, new Object[] { baoxiaoName });
		if (list.size() > 0) {
			return (Baoxiao) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 通过登录名来获取司机对象
	 * 
	 * @param loginName
	 * @return
	 * @throws Exception
	 */

	@Override
	public List getList(int type, int uid, String status) {
		String sql = "select b.*,(b.glf+b.jyf+b.byf+b.wxf+b.xcf+b.lqf+b.tcf+b.njf+b.bxf+b.qtf) as fee,"
				+ "d.driver_name as name ,d.driver_tel as driverTel,u.plate as plate from baoxiao b left join driver d on b.user_id=d.driver_id left join user u on u.user_id = b.car_id where 1=1 ";
		// if(values[0]!=null && !values[0].equals("")){
		// sql.append(" and l.account.accountId
		// in(").append(values[0]).append(")");
		// }
		// if(values[1]!=null && !values[1].equals("")){
		// sql.append(" and l.baoxiaoName like
		// '%").append(values[1]).append("%'");
		// }
		if (type == 1) {
			sql += " and b.user_id = " + uid;

		} else {
			sql += " and d.team_id = " + uid;
		}
		if (status.length() > 0) {
			sql += " and b.status in (" + status + ")";
		}
		sql += " order by b.id desc";
		return baoxiaoDao.getListSql(sql, null);
	}

	@Override
	public List getBaoxiaoBId(int baoxiaoId) {
		String sql = "select * from baoxiao d left join user u on u.baoxiao_id = d.baoxiao_id where d.baoxiao_id ="
				+ baoxiaoId;
		return baoxiaoDao.getListSql(sql, null);
	}
}
