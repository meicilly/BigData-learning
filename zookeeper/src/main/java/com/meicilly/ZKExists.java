package com.meicilly;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKExists {
    private static String ZK_URL = "192.168.233.16:2181,192.168.233.17:2181,192.168.233.18:2181";
    ZooKeeper zooKeeper;
    @Before
    public void before() throws IOException, InterruptedException {
        // TODO: 2023/7/2 计数器对象
        CountDownLatch countDownLatch = new CountDownLatch(1);
        zooKeeper = new ZooKeeper(ZK_URL, 500000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
                    System.out.println("连接创建成功");
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
    }
    @After
    public void after() throws InterruptedException {
        zooKeeper.close();
    }

    @Test
    public void exists1() throws Exception {
        // arg1:节点的路径
        Stat stat=zooKeeper.exists("/exists1",false);
        // 节点的版本信息
        System.out.println(stat.getVersion());
    }

    @Test
    public void exists2() throws Exception {
        // 异步方式
        zooKeeper.exists("/exists1", false, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                // 0 代表方式执行成功
                System.out.println(rc);
                // 节点的路径
                System.out.println(path);
                // 上下文参数
                System.out.println(ctx);
                // 节点的版本信息
                System.out.println(stat.getVersion());
            }
        },"I am Context");
        Thread.sleep(10000);
        System.out.println("结束");
    }
}
