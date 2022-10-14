/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.hz.server.service;


import com.hz.server.cloud.CloudStorageConfig;
import com.hz.server.entity.SysOss;

import java.util.List;

/**
 * 系统配置信息
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysConfigService /*extends IService<SysConfigEntity>*/ {
	/**
	 * 保存配置信息
	 */
	void saveConfig(SysOss sysOss);

	/**
	 * 更新配置信息
	 */
	void update(SysOss sysOss);

	/**
	 * 根据key，更新value
	 */
	void updateValueByKey(String key, String value);

	/**
	 * 删除配置信息
	 */
	void deleteBatch(String sysName);

	SysOss getSysOss(String sysName);

	List<SysOss> getSysOssList();

	/**
	 * 根据key，获取配置的value值
	 *
	 * @param key           key
	 */
	String getValue(String key);

	/**
	 * 根据key，获取value的Object对象
	 * @param key    key

	 */
	CloudStorageConfig/*<T> T*/ getConfigObject(String key/*, Class<T> clazz*/);

}
