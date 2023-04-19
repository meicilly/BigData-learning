package com.meicilly;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class connectionRedis {
    // TODO: 2023-04-18 构建连接对象
    Jedis jedis = null;
    @Before
    public void getConnection(){
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
    }
    @Test
    public void testString(){
        System.out.println(jedis.exists("s2"));
        //        jedis.set("s1","hadoop");
//        System.out.println(jedis.exists("s1"));
//        System.out.println(jedis.exists("s2"));
//        System.out.println(jedis.get("s1"));
//        jedis.set("s2","3");
//        jedis.incr("s2");
//        System.out.println(jedis.get("s2"));
//        jedis.expire("s2",10);
//        while(true){
//            System.out.println(jedis.ttl("s2"));
//        }
        //setex：构建KV时，直接设置生命周期
        jedis.setex("s3",10,"oozie");
    }
    @Test
    public void testHash(){
        //hset/hmset/hget/hgetall/hdel/hlen/hexists
        jedis.hset("m1","name","zhangsan");
        System.out.println(jedis.hget("m1","name"));
        Map<String,String> maps = new HashMap<>();
        maps.put("age","18");
        maps.put("phone","110");
        jedis.hmset("m1",maps);
        List<String> hmget = jedis.hmget("m1", "name", "age");
        System.out.println(hmget);
        System.out.println("=");
        Map<String, String> m1 = jedis.hgetAll("m1");
        for(Map.Entry map : m1.entrySet()){
            System.out.println(map.getKey()+"\t"+map.getValue());
        }
        System.out.println("=");
        System.out.println(jedis.hlen("m1"));
        jedis.hdel("m1","name");
        System.out.println(jedis.hlen("m1"));
        System.out.println(jedis.hexists("m1","name"));
        System.out.println(jedis.hexists("m1","age"));
    }
    @Test
    public void testList(){
        //lpush/rpush/lrange/llen/lpop/rpop
        jedis.lpush("list1","1","2","3");
        System.out.println(jedis.lrange("list1",0,-1));
        jedis.rpush("list1","4","5","6");
        System.out.println(jedis.lrange("list1",0,-1));
        System.out.println(jedis.llen("list1"));
        jedis.lpop("list1");
        jedis.rpop("list1");
        System.out.println(jedis.lrange("list1",0,-1));
    }
    @Test
    public void testSet(){
        //sadd/smembers/sismember/scard/srem
        jedis.sadd("set1","1","2","3","1","2","3","4","5","6");
        System.out.println("长度："+jedis.scard("set1"));
        System.out.println("内容："+jedis.smembers("set1"));
        System.out.println(jedis.sismember("set1","1"));
        System.out.println(jedis.sismember("set1","7"));
        jedis.srem("set1","2");
        System.out.println("内容："+jedis.smembers("set1"));

    }
    @Test
    public void testZset(){
        //zadd/zrange/zrevrange/zcard/zrem
        jedis.zadd("zset1",20.9,"yuwen");
        jedis.zadd("zset1",10.5,"yinyu");
        jedis.zadd("zset1",70.9,"shuxue");
        jedis.zadd("zset1",99.9,"shengwu");
        Set<String> zset1 = (Set<String>) jedis.zrange("zset1", 0, -1);
        System.out.println(zset1);
        System.out.println(jedis.zrevrange("zset1",0,-1));
        System.out.println(jedis.zcard("zset1"));
        jedis.zrem("zset1","yuwen");
        System.out.println(jedis.zrangeWithScores("zset1",0,-1));
    }
    @After
    public void closeConnection(){
        jedis.close();
    }

}
