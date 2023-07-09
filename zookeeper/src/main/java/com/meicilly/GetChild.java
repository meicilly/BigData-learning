package com.meicilly;

import org.apache.zookeeper.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GetChild {
    private static final String ZK_URL = "192.168.233.16:2181,192.168.233.17:2181,192.168.233.18:2181";
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
    public void get1() throws InterruptedException, KeeperException {
        // TODO: 2023/7/3 节点路径
        List<String> lists = zooKeeper.getChildren("/create", false);
        for (String list: lists) {
            System.out.println(list);
        }
    }

    @Test
    public void get2() throws Exception {
        // 异步用法
        zooKeeper.getChildren("/get", false, new AsyncCallback.ChildrenCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children) {
                // 0代表读取成功
                System.out.println(rc);
                // 节点的路径
                System.out.println(path);
                // 上下文参数对象
                System.out.println(ctx);
                // 子节点信息
                for (String str : children) {
                    System.out.println(str);
                }
            }
        },"I am Context");
        Thread.sleep(10000);
        System.out.println("结束");
    }
}
