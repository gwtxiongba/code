package com.chanceit.http.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.User;

public interface IUserService {

	/**
	 * @author dj
	 * @date Jun 21, 2013
	 * @param userType
	 * @param userInfo
	 * @return
	 * @Description save user
	 */
	public boolean save(short userType, String userInfo) ;

	public boolean save(User user) throws Exception ;
	public List listBrand(Account account ,int  styleId) throws Exception;
	public boolean delete(String ids,int accountId) ;
	public boolean update(User user);
	public User get(int userId);
	public List getList(String accountId,int teamId) throws Exception;
//	@SuppressWarnings("unchecked")
//	public JSONObject getJson(String accountId) throws Exception;
	public Page getPageList(Page page,Object[] values ) throws Exception;
	public boolean ifExist(String userName) throws Exception;
	public User getByName(String userName) throws Exception;
//	public boolean sendMessage(String uids,String content);
//	public boolean sendPoint(String uids, double pointX, double pointY ,String sendType ,String type ,String address ,String desc);

	public boolean ifExist(String identifier, Integer accountId,String cid);
	/**
	 * @param accountId
	 * @param levelId
	 * @return
	 * @Description ������ӵĳ����Ƿ��ѵ�����
	 */
	public boolean acountCar(Integer accountId,Integer levelId);
	/**
	 * @param identifier
	 * @param user
	 * @return
	 * @throws JSONException
	 * @description ���λ�õ㣬����user����
	 */
//	public User getPointUser(String identifier,User user) throws JSONException;
	
	public boolean fakeDelete(String ids,int accountId);
	public boolean bindTeam(String ids, int accountId,int teamId) throws Exception;
	public boolean unBindTeam(String ids, int accountId,int teamId);
	public List getList(Object[] values) throws Exception;
	public List getListById(String accountId,int teamId) throws Exception;
	/**
	 * @des �����༭�����Ƿ���
	 * @param ifMonitor
	 * @param vehicleIds
	 * @param accountId
	 * @return
	 */
	public boolean saveMonitor(Integer ifMonitor, String vehicleIds,int accountId);
	/**
	 * @des idsΪ�յ�ʱ��������
	 * @param accountId
	 * @param teamId
	 * @return
	 */
	public boolean unBindTeamAll(int accountId,int teamId);
//	public boolean sendPoint(String uids,String content,String coordinate);
	public JSONObject getOnlineAmount(Account account, String accountId) throws Exception;
	public JSONObject getRelayAmount(Object[] values) throws Exception;
	/**
	 * @des �ϵ����ͼ�õ� ������ȡλ�õ�
	 * @param accountId
	 * @param time
	 * @param pt0
	 * @param pt1
	 * @return
	 * @throws Exception
	 */
//	public String getJson(String accountId,String time,String pt0,String pt1) throws Exception;
	public boolean ifExistPlate(String plate, Integer companyId,String vehicleId);
	/**
	 * @des ��ȡonline�ĳ����б�
	 * @param page
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public List getOnlineList(Object[] values ) throws Exception;

	public Page getRelayList(Page page,Object[] values ) throws Exception;
	
	public List getListForOperator(Object[] values) throws Exception;
	
	public Long getCount(Object[] values)throws Exception;
	public Object getUserId(Object[] values);
	
	public List getDriverDetailed(Object[] values);
	
	public void synRedisTeamId(int accountId,int teamId) throws Exception;
	
	public boolean checkOffPwd(String identifier, String pwd);
	
	public boolean checkIfOff(int identifier);
	
	public void setOff(int carId, int state);
	
	public List getList();
	
	public List getList1(String accountId) throws Exception;
	public List getList2(int teamId,String teamIds) throws Exception;
	public List<User> getUidByPlate(String plate);
	public List<User> getUidById(String  ids);
	public String httpSave(String uiserid,String discc);
	public Page getUsersPage(Page page,Object[] values );
	public List<User> getUserListToMap(String uids);
	
	public List getListForExcel(Object[] values);
	
	public List getListForReport(Object[] values);
	public List getListMonthForReport(Object[] values);
	public List getListMonthDetailForReport(Object[] values);
}