/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Sep 27, 2013
 * Id: ClientFactory.java,v 1.0 Sep 27, 2013 3:25:28 PM Administrator
 */
package com.chanceit.framework.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.xfire.client.Client;

/**
 * @ClassName ClientFactory
 * @author Administrator
 * @date Sep 27, 2013 3:25:28 PM
 * @Description 客户端连接器管理
 */
public class ClientFactory {
	
	public static Map<String, Client> clientMap = new HashMap<String, Client>();
	
	/**
	 * @author Administrator
	 * @date Sep 27, 2013
	 * @param url
	 * @return
	 * @Description 获得client对象
	 */
	public static synchronized Client getClient(String url){
		if(StringUtils.isEmpty(url)) return null;
		Client client = clientMap.get(url);
		if(client == null){
			client = createCurrentClient(url);
		}
		return client;
	}
	
	private static Client createCurrentClient(String url){
		URL url2 = null;
		Client client = null;
		try {
			url2 = new URL(url);
			client = new Client(url2);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(client != null) clientMap.put(url, client);
		return client;
	}
}
