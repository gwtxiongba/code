/**
 * Copyright (c) 2011-2012 RoadRover Technology Company LTD.
 * All rights reserved.
 * 
 * Created on May 23, 2013
 * Id: ISocketConfig.java,v 1.0 May 23, 2013 5:20:10 PM yehao
 */
package com.chanceit.framework.interfaces;

/**
 * @ClassName ISocketConfig
 * @author yehao
 * @date May 23, 2013 5:20:10 PM
 * @Description 获取信息配置外部接口
 */
public interface ISocketConfig {
	
	/**
	 * @author yehao
	 * @date May 23, 2013
	 * @param configString
	 * @Description 重新获取配置信息
	 */
	public String reloadSocket(String configString);

}
