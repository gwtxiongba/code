/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jul 26, 2013
 * Id: UCFactory.java,v 1.0 Jul 26, 2013 10:13:02 AM Administrator
 */
package com.chanceit.framework.ucenter;

/**
 * @ClassName UCFactory
 * @author Administrator
 * @date Jul 26, 2013 10:13:02 AM
 * @Description UCFactory
 */
public class UCFactory {
	
	private static Client client = null;
	
	public synchronized static Client getClient() {
		if(client == null){
			client = new Client();
		}
		return client;
	}
	
}
