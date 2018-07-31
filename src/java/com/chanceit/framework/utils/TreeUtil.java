package com.chanceit.framework.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.CarTeamTree;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.pojo.TeamTree;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.ITeamService;

/**
 * Tree解析工具类
 * 
 * @author Administrator
 * @version 1.0
 */

public class TreeUtil {
	@Autowired
	@Qualifier("jedisService")
	private JedisService jedisService;
	
	private  List<TeamTree> treeNodeList = new ArrayList<TeamTree>();
	private  List<CarTeamTree> treeNodeListCar = new ArrayList<CarTeamTree>();
	public Map<Integer,Object> result = new HashMap<Integer, Object>();
	public Map<String,Integer> onlineMap = new HashMap<String, Integer>();
	public Map<Integer,Object> TonlineMap = new HashMap<Integer, Object>();
	public TreeUtil(List<TeamTree> list,List<CarTeamTree> lists) {
		treeNodeList = list;
		treeNodeListCar = lists;
	}
	
   public void clear(){
	   result.clear();
	   onlineMap.clear();
	   TonlineMap.clear();
   }
	/**
	 * 
	 * @param nodeId
	 * @return
	 */
	public  TeamTree getNodeById(int nodeId) {
		TeamTree treeNode = new TeamTree();
		for (TeamTree item : treeNodeList) {
			if (item.getTeamId() == nodeId) {
				treeNode = item;
				break;
			}
		}
		return treeNode;
	}
	
		public  CarTeamTree getNodeByIdCar(int nodeId) {
			CarTeamTree treeNode = new CarTeamTree();
			for (CarTeamTree item : treeNodeListCar) {
				if (item.getTeamId() == nodeId) {
					treeNode = item;
					break;
				}
			}
			return treeNode;
		}

	/**
	 * 
	 * @param nodeId
	 * @return
	 */
	public  List<TeamTree> getChildrenNodeById(int nodeId) {
		List<TeamTree> childrenTreeNode = new ArrayList<TeamTree>();
		for (TeamTree item : treeNodeList) {
			if (item.getPid() == nodeId) {
				childrenTreeNode.add(item);
			}
		}
		return childrenTreeNode;
	}
	
	public  List<CarTeamTree> getChildrenNodeByIdCar(int nodeId) {
		List<CarTeamTree> childrenTreeNode = new ArrayList<CarTeamTree>();
		for (CarTeamTree item : treeNodeListCar) {
			if (item.getPid() == nodeId) {
				childrenTreeNode.add(item);
			}
		}
		return childrenTreeNode;
	}

	/**
	 * 递归生成Tree结构数据
	 * 
	 * @param rootId
	 * @return
	 */
	public  TeamTree generateTreeNode(int rootId) {
		TeamTree root = getNodeById(rootId);
		//root.setTeamName(root.getTeamName()+"("+result.get(rootId)+")");
		//System.out.println(TonlineMap);
		List<TeamTree> childrenTreeNode =getChildrenNodeById(rootId);
		for (TeamTree item : childrenTreeNode) {
			TeamTree node = generateTreeNode(item.getTeamId());
			if(!node.isLeaf()){
				Integer c = (Integer) TonlineMap.get(node.getTeamId());
				if(c == null){
					c=0;
				}
				
			node.setTeamName(node.getTeamName()+"("+c+"/"+result.get(node.getTeamId())+")");
			root.getTreeList().add(node);
			}else{
				Integer c = (Integer) onlineMap.get(node.getTeamId()+"");
				if(c!=null && c==1){
					node.setIconCls("car_ic_o");
					root.getTreeList().add(0,node);
					
				}else{
					root.getTreeList().add(node);
				}
			}
			
		}
		return root;
	}
	public  TeamTree generateTreeNodeForapp(int rootId) {
		TeamTree root = getNodeById(rootId);
		//root.setTeamName(root.getTeamName()+"("+result.get(rootId)+")");
		//System.out.println(TonlineMap);
		List<TeamTree> childrenTreeNode =getChildrenNodeById(rootId);
		for (TeamTree item : childrenTreeNode) {
			TeamTree node = generateTreeNodeForapp(item.getTeamId());
			root.getTreeList().add(node);
		}
		return root;
	}
	
	public  CarTeamTree generateTreeNodecar(int rootId) {
		CarTeamTree root = getNodeByIdCar(rootId);
		List<CarTeamTree> childrenTreeNode =getChildrenNodeByIdCar(rootId);
		for (CarTeamTree item : childrenTreeNode) {
			CarTeamTree node = generateTreeNodecar(item.getTeamId());
			root.getTreeList().add(node);
		}
		return root;
	}
	public  int[] doCount(int rootId){  
        int onlinrC = 0;
         int count = 0;
         int[] aar = new int[2];
        List<TeamTree> list =getChildrenNodeById(rootId);  
        if(list==null ||list.size()==0){  
            return aar;  
        }  
      
        for (TeamTree child : list) {  
            //统计当前元素的子节点个数  
        	if(child.isLeaf()){
           // count++; 
            aar[0] += 1;
            if(onlineMap.get(child.getTeamId()+"")!=null){
            	if(onlineMap.get(child.getTeamId()+"")==1){
            		//System.out.println(child.getTeamId());
            		//onlinrC+=1;
            	 aar[1] += 1;
            	}
            }
        	}  
            //统计子节点的孩子总数  
        	else{ 
        		int[] cur_cnt=doCount(child.getTeamId());  
            result.put(child.getTeamId(), cur_cnt[0]);  
           // TonlineMap.put(rootId, cur_cnt[1]);
            aar[1] += cur_cnt[1];
            aar[0] += cur_cnt[0];  
        	}
        }  
        //System.out.println(aar[1]);
        TonlineMap.put(rootId, aar[1]);
        //返回前记录当前节点的统计个数  
        result.put(rootId, aar[0]);  
//        aar[0] = count;
//        aar[1] = onlinrC;
       // System.out.println(TonlineMap);
        return aar;  
    }  
  
	public void getOnlineCount(Account account,String uidss){
		try {
			//jedisService = new JedisService();
			Jedis jedis = new Jedis("10.173.188.217", 6379);// 连接redis
			//Jedis jedis = getJedis("192.168.1.18");
			String[] uidAry = uidss.split(",");
			List<String> list2 = jedis.mget(uidAry);
			for(int i = 0; i < list2.size(); i++){
				if(list2.get(i)!=null){
					JSONObject json = new JSONObject(list2.get(i));
						long time = Long.parseLong(String.valueOf(json.get("time")));
						long interval = DateUtil.sub(time);
						if (interval > 180) {// 大于3mins 为离线
							onlineMap.put(json.getString("uid"), 0);
						} else {
							onlineMap.put(json.getString("uid"), 1);
						}
					}
			}
//		String res = jedisService.getLocationByUidsMe(uidss);
//		System.out.println(res);
//		if(res != null && res != ""){
//		
//			JSONObject json = new JSONObject(res);
//			JSONArray array = (JSONArray) json.get("res");
//			
//			for (int i = 0; i < array.length(); i++) {
//				JSONObject json_tem = array.getJSONObject(i);
//			    int online = json_tem.getInt("online");
//			    if(online == 1){
//			    	onlineMap.put(json_tem.getString("uid"), online);
//			    }
//			}
//		}
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println(onlineMap);
		}
}