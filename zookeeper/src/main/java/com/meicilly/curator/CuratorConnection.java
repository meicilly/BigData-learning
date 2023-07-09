package com.meicilly.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.retry.RetryUntilElapsed;

public class CuratorConnection {
    private static final String ZK_URL = "192.168.233.16:2181,192.168.233.17:2181,192.168.233.18:2181";
    public static void main(String[] args) {
        //meicilly TODO:3秒重连一次 只重连1次
        //RetryOneTime retryOneTime = new RetryOneTime(3000);

        //meicilly TODO:每3秒重连一次 重连3次
        //RetryNTimes retryNTimes = new RetryNTimes(3, 3000);

        //meicilly TODO:每3秒重连一次 总等待时间超过10秒停止重连
        //RetryUntilElapsed retryUntilElapsed = new RetryUntilElapsed(10000, 3000);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        //meicilly TODO:创建连接对象
        CuratorFramework client = CuratorFrameworkFactory.builder()
                //meicilly TODO:IP地址端口号
                .connectString(ZK_URL)
                //meicilly TODO:会话超时时间
                .sessionTimeoutMs(5000)
                //meicilly TODO:重连机制
                .retryPolicy(retryPolicy)
                //meicilly TODO:命名空间
                .namespace("create")
                //meicilly TODO:构建对象
                .build();
        //meicilly TODO:打开连接
        client.start();
        System.out.println(client.checkExists());
        //meicilly TODO:关闭连接
        client.close();
    }
}
