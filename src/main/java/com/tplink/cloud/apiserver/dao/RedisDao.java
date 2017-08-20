/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.com.cn>
 * Created: 2017-08-18
 */

package com.tplink.cloud.apiserver.dao;

import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.tplink.cloud.common.database.RedisClient;

/**
 * This class extends {@link} RedisClient
 */
public class RedisDao extends RedisClient {

    public RedisDao(String host, int port) {
        super(host, port);
    }

    public Long zadd(final String key, final double score, final String member){
        return excuteCommand(new JedisCommand<Long>() {
            @Override
            public Long execute(JedisCommands jedis) {
                return jedis.zadd(key, score, member);
            }
        });
    }

    public Set<String> zrangBySore(final String key, final double min, final double max){
        return excuteCommand(new JedisCommand<Set<String>>() {
            @Override
            public Set<String> execute(JedisCommands jedis) {
                return jedis.zrangeByScore(key, min, max);
            }
        });
    }

    public Long zrem(final String key, final String... members){
        return excuteCommand(new JedisCommand<Long>() {
            @Override
            public Long execute(JedisCommands jedis) {
                return jedis.zrem(key, members);
            }
        });
    }

    public Long zremrangeByRank(final String key, final long start, final long end){
        return excuteCommand(new JedisCommand<Long>() {
            @Override
            public Long execute(JedisCommands jedis) {
                return jedis.zremrangeByRank(key, start, end);
            }
        });
    }

}
