package com.meicilly;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class connectionRedis {
    public static void main(String[] args) {
        // TODO: 2023-04-18 构建连接对象
        Jedis jedis = null;
        // TODO: 2023-04-18 方式一 直接构建Jedis对象
        //jedis = new Jedis("192,168.88.11",6379);
        // TODO: 2023-04-18 方法二 构建连接池对象
        JedisPoolConfig config = new JedisPoolConfig();
        //config.setMaxTotal(2);//总连接构建
        //config.setMaxIdle(2);//最大空闲连接
        //config.setMinIdle(1);//最小空闲连接
        //构建连接池对象
        JedisPool jedisPool = new JedisPool(config,"192.168.88.11",6379);
        //获取连接
        jedis = jedisPool.getResource();
        jedis.set("s3","hadoop");
        System.out.println(jedis.exists("s3"));
        System.out.println(jedis.get("s3"));
        jedis.close();
    }
}
