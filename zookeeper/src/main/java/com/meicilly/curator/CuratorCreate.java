package com.meicilly.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class CuratorCreate {
    private static final String ZK_URL = "192.168.233.16:2181,192.168.233.17:2181,192.168.233.18:2181";
    CuratorFramework client;
    @Before
    public void before(){
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(ZK_URL)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace("create")
                .build();
        client.start();
    }
    @After
    public void after(){client.close();}
    @Test
    public void create1() throws Exception {
        //MC TODO:新增节点
        client.create()
                //MC TODO:节点的类型
                .withMode(CreateMode.PERSISTENT)
                //MC TODO:节点的权限列表 world:anyone:cdrwa
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                //MC TODO:节点的路径 节点的数据
                .forPath("/node21","node1".getBytes());
        System.out.println("结束");
    }

    @Test
    public void create2() throws Exception {
        //MC TODO:自定义权限列表
        //MC TODO:权限列表
        ArrayList<ACL> list = new ArrayList<ACL>();
        //MC TODO:权限模式和授权模式
        Id id = new Id("ip", "192.168.233.16");
        list.add(new ACL(ZooDefs.Perms.ALL, id));
        client.create().withMode(CreateMode.PERSISTENT).withACL(list).forPath("/node23","node22".getBytes());
        System.out.println("结束");
    }

    @Test
    public void create3() throws Exception {
        //MC TODO:递归创建节点树
        client.create()
                //MC TODO:递归节点创建
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/node25/node22","node31".getBytes());
        System.out.println("结束");
    }

    @Test
    public void create4() throws Exception {
        // 异步方式创建节点
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                // 异步回调接口
                .inBackground(new BackgroundCallback() {
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        // 节点的路径
                        System.out.println(curatorEvent.getPath());
                        // 时间类型
                        System.out.println(curatorEvent.getType());
                    }
                })
                .forPath("/node4","node4".getBytes());
        Thread.sleep(5000);
        System.out.println("结束");
    }
}

