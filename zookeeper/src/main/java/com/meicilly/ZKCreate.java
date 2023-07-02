package com.meicilly;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZKCreate {
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
    public void create1() throws InterruptedException, KeeperException {
        // TODO: 2023/7/2 arg1:节点的路径
        // TODO: 2023/7/2 arg2:节点数据
        // TODO: 2023/7/2 arg3:权限列表 world:anyone:cdrwa
        // TODO: 2023/7/2 arg4:节点类型 持久化节点
        /**
         *   int READ = 1;
         *   int WRITE = 2;
         *   int CREATE = 4;
         *   int DELETE = 8;
         *   int ADMIN = 16;
         *   int ALL = 31;
         */
        zooKeeper.create("/create/node1","node1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void create2() throws InterruptedException, KeeperException {
        // TODO: 2023/7/2 READ_ACL_UNSAFE world:anyone:r
        zooKeeper.create("/create/node3","node2".getBytes(),ZooDefs.Ids.READ_ACL_UNSAFE,CreateMode.PERSISTENT);
    }

    @Test
    public void create3() throws InterruptedException, KeeperException {
        // TODO: 2023/7/2 world授权模式
        ArrayList<ACL> acls = new ArrayList<>();
        // TODO: 2023/7/2 授权模式和授权对象
        Id id = new Id("world", "anyone");
        // TODO: 2023/7/2 权限设置
        acls.add(new ACL(ZooDefs.Perms.READ,id));
        acls.add(new ACL(ZooDefs.Perms.WRITE,id));
        zooKeeper.create("/create/node4","node4".getBytes(),acls,CreateMode.PERSISTENT);
    }

    @Test
    public void create4() throws InterruptedException, KeeperException {
        // TODO: 2023/7/2 ip授权模式 权限列表
        ArrayList<ACL> acls = new ArrayList<ACL>();
        Id id = new Id("ip", "192.168.233.16");
        // TODO: 2023/7/2 权限设置
        acls.add(new ACL(ZooDefs.Perms.ALL,id));
        zooKeeper.create("/create/node5","node5".getBytes(),acls,CreateMode.PERSISTENT);
    }

    @Test
    public void create5() throws InterruptedException, KeeperException {
        // TODO: 2023/7/2 auth授权模式
        zooKeeper.addAuthInfo("digest","meicilly:123456".getBytes());
        zooKeeper.create("/create/node6","node6".getBytes(),ZooDefs.Ids.CREATOR_ALL_ACL,CreateMode.PERSISTENT);
    }
    @Test
    public void create6() throws Exception {
        // auth授权模式
        // 添加授权用户
        zooKeeper.addAuthInfo("digest", "itcast:123456".getBytes());
        // 权限列表
        List<ACL> acls = new ArrayList<ACL>();
        // 授权模式和授权对象
        Id id = new Id("auth", "itcast");
        // 权限设置
        acls.add(new ACL(ZooDefs.Perms.READ, id));
        zooKeeper.create("/create/node6", "node6".getBytes(), acls, CreateMode.PERSISTENT);
    }

    @Test
    public void create7() throws Exception {
        // digest授权模式
        // 权限列表
        List<ACL> acls = new ArrayList<ACL>();
        // 授权模式和授权对象
        Id id = new Id("digest", "itheima:qlzQzCLKhBROghkooLvb+Mlwv4A=");
        // 权限设置
        acls.add(new ACL(ZooDefs.Perms.ALL, id));
        zooKeeper.create("/create/node7", "node7".getBytes(), acls, CreateMode.PERSISTENT);
    }

    @Test
    public void create8() throws Exception {
        // 持久化顺序节点
        // Ids.OPEN_ACL_UNSAFE world:anyone:cdrwa
        String result = zooKeeper.create("/create/node8", "node8".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println(result);
    }

    @Test
    public void create9() throws Exception {
        //  临时节点
        // Ids.OPEN_ACL_UNSAFE world:anyone:cdrwa
        String result = zooKeeper.create("/create/node9", "node9".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(result);
    }

    @Test
    public void create10() throws Exception {
        // 临时顺序节点
        // Ids.OPEN_ACL_UNSAFE world:anyone:cdrwa
        String result = zooKeeper.create("/create/node10", "node10".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(result);
    }

    @Test
    public void create11() throws Exception {
        // 异步方式创建节点
        zooKeeper.create("/create/node11", "node11".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, String name) {
                // 0 代表创建成功
                System.out.println(rc);
                // 节点的路径
                System.out.println(path);
                // 节点的路径
                System.out.println(name);
                // 上下文参数
                System.out.println(ctx);

            }
        }, "I am context");
        Thread.sleep(10000);
        System.out.println("结束");
    }
}
