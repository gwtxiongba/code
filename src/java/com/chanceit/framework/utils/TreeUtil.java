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

import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.TeamTree;

/**
 * Tree解析工具类
 * 
 * @author Administrator
 * @version 1.0
 */

public class TreeUtil {
	
	private  List<TeamTree> treeNodeList = new ArrayList<TeamTree>();
	
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
				
			root.getTreeList().add(node);
			
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


	public TreeUtil(List<TeamTree> treeNodeList) {
		super();
		this.treeNodeList = treeNodeList;
	}
	
	
}