package com.meicilly.example;

import com.meicilly.watcher.ZKConnectionWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class GloballyUniqueId implements Watcher {
    private static String ZK_URL = "192.168.233.16:2181,192.168.233.17:2181,192.168.233.18:2181";
    ZooKeeper zooKeeper;

    // TODO: 2023/7/3 计数器对
    CountDownLatch countDownLatch = new CountDownLatch(1);
    String defaultPath = "/uniqueId";
    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Watcher.Event.EventType.None) {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                System.out.println("连接成功");
                countDownLatch.countDown();
            } else if (event.getState() == Watcher.Event.KeeperState.Disconnected) {
                System.out.println("连接断开!");
            } else if (event.getState() == Watcher.Event.KeeperState.Expired) {
                System.out.println("连接超时!");
                // 超时后服务器端已经将连接释放，需要重新连接服务器端
                try {
                    zooKeeper = new ZooKeeper(ZK_URL, 6000,
                            new ZKConnectionWatcher());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (event.getState() == Watcher.Event.KeeperState.AuthFailed) {
                System.out.println("验证失败!");
            }
        }
    }
    // TODO: 2023/7/3 构造方法
    public GloballyUniqueId() throws IOException {
        try {
            zooKeeper = new ZooKeeper(ZK_URL,50000,this);
            // TODO: 2023/7/3 阻塞等待线程连接成功
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
