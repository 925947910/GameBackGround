/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2017 © yangxiaobing, 873559947@qq.com
 *
 * This file is part of contentManagerSystem.
 * contentManagerSystem is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * contentManagerSystem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with contentManagerSystem.  If not, see <http://www.gnu.org/licenses/>.
 *
 * 这个文件是contentManagerSystem的一部分。
 * 您可以单独使用或分发这个文件，但请不要移除这个头部声明信息.
 * contentManagerSystem是一个自由软件，您可以自由分发、修改其中的源代码或者重新发布它，
 * 新的任何修改后的重新发布版必须同样在遵守GPL3或更后续的版本协议下发布.
 * 关于GPL协议的细则请参考COPYING文件，
 * 您可以在contentManagerSystem的相关目录中获得GPL协议的副本，
 * 如果没有找到，请连接到 http://www.gnu.org/licenses/ 查看。
 *
 * - Author: yangxiaobing
 * - Contact: 873559947@qq.com
 * - License: GNU Lesser General Public License (GPL)
 * - source code availability: http://git.oschina.net/yangxiaobing_175/contentManagerSystem
 */
package com.yxb.cms.architect.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis配置<br>
 * 读取jedis.properties配置文件,配置不同环境的redis配置信息
 *
 * @author yangxiaobing
 * @date 2018/4/30
 */
@Component
@ConfigurationProperties(prefix = "redis-pool")
public class JedisProperties {

    //Redis服务器地址
	 @Value("${redis-pool.host}")
    public  String host;
    //Redis服务器端口
	 @Value("${redis-pool.port}")
    public  int port;

    //Redis服务器连接密码（默认为空）
	 @Value("${redis-pool.password}")
    public  String password;
    //连接超时时间（毫秒）
	 @Value("${redis-pool.timeOut}")
    public  int timeOut;

    //连接池中的最大空闲连接
	 @Value("${redis-pool.maxIdle}")
    public  int maxIdle;
    //连接池最大阻塞等待时间（使用负值表示没有限制）
	 @Value("${redis-pool.maxWaitMillis}")
    public  int maxWaitMillis;

    //连接池最大实例
	 @Value("${redis-pool.maxTotal}")
    public  int maxTotal;


    public  void setHost(String host) {
        this.host = host;
    }

    public  void setPort(int port) {
    	this.port = port;
    }

    public  void setPassword(String password) {
    	this.password = password;
    }

    public  void setTimeOut(int timeOut) {
    	this.timeOut = timeOut;
    }

    public  void setMaxIdle(int maxIdle) {
    	this.maxIdle = maxIdle;
    }

    public  void setMaxWaitMillis(int maxWaitMillis) {
    	this.maxWaitMillis = maxWaitMillis;
    }

    public  void setMaxTotal(int maxTotal) {
    	this.maxTotal = maxTotal;
    }
}
