package com.meicilly;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperConnection {
    private static String ZK_URL = "192.168.233.16:2181,192.168.233.17:2181,192.168.233.18:2181";

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            // TODO: 2023/7/2 服务器ip和端口  客户端与服务器之间的会话超时时间  以毫秒为单位
            ZooKeeper zooKeeper = new ZooKeeper(ZK_URL, 5000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if(event.getState() == Event.KeeperState.SyncConnected){
                        System.out.println("创建连接成功");
                        countDownLatch.countDown();
                    }
                }
            });

            // TODO: 2023/7/2 主线程等待连接对象的创建成功
            countDownLatch.await();
            // TODO: 2023/7/2 会话编号
            System.out.println(zooKeeper.getSessionId());

            zooKeeper.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
