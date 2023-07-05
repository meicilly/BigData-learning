package com.meicilly.watcher;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKConnectionWatcher  implements Watcher {
    // TODO: 2023/7/3 计算器对象
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    // TODO: 2023/7/3 连接对象
    static ZooKeeper zooKeeper;
    private static String ZK_URL = "192.168.233.16:2181,192.168.233.17:2181,192.168.233.18:2181";
    @Override
    public void process(WatchedEvent event) {
        try {
            // 事件类型
            if (event.getType() == Event.EventType.None) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("连接创建成功!");
                    countDownLatch.countDown();
                } else if (event.getState() == Event.KeeperState.Disconnected) {
                    System.out.println("断开连接！");
                } else if (event.getState() == Event.KeeperState.Expired) {
                    System.out.println("会话超时!");
                    zooKeeper = new ZooKeeper("192.168.60.130:2181", 5000, new ZKConnectionWatcher());
                } else if (event.getState() == Event.KeeperState.AuthFailed) {
                    System.out.println("认证失败！");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(ZK_URL,50000,new ZKConnectionWatcher());
        // TODO: 2023/7/3 阻塞等待线程被创建
        countDownLatch.await();
        // TODO: 2023/7/3 会话id
        System.out.println(zooKeeper.getSessionId());
        // 添加授权用户
        //zooKeeper.addAuthInfo("digest1","meicilly:1234561".getBytes());
        byte [] bs=zooKeeper.getData("/create",false,null);
        System.out.println(new String(bs));
        Thread.sleep(50000);
        zooKeeper.close();
        System.out.println("结束");
    }
}
