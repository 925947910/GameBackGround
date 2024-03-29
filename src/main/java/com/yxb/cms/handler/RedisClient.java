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
package com.yxb.cms.handler;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 配置一些常用的 redis 的操作
 * @author yangxiaobing
 * @date 2018/4/30
 */
public class RedisClient {

    private Logger log = LogManager.getLogger(RedisClient.class);

    private JedisPool jedisPool;


    public void set(int db,String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(db);
            jedis.set(key, value);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw e;
        }finally {
            jedis.close();
        }
    }

    public String get(int db,String key) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(db);
            return jedis.get(key);
        } catch (Exception e){
            log.error(e.getMessage(),e);
            throw e;
        }finally {
            jedis.close();
        }

    }

    public Long del(int db,String key) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(db);
            return jedis.del(key);
        } catch (Exception e){
            log.error(e.getMessage(),e);
            throw e;
        }finally {
            jedis.close();
        }

    }
    public String hget(int db,String key,String field) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(db);
            return jedis.hget(key, field);
        } catch (Exception e){
            log.error(e.getMessage(),e);
            throw e;
        }finally {
            jedis.close();
        }

    }
    public void hset(int db,String key,String field, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(db);
            jedis.hset(key, field, value);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw e;
        }finally {
            jedis.close();
        }
    }

    public boolean exists(int db,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(db);
            return jedis.exists(key);
        } catch (Exception e){
            log.error(e.getMessage(),e);
            throw e;
        }finally {
            jedis.close();
        }

    }
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
