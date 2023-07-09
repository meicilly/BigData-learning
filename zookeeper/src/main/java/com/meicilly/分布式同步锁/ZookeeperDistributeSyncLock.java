package com.meicilly.分布式同步锁;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class ZookeeperDistributeSyncLock {
    // TODO: 2023/7/7 zk的地址
    private static final String CONNECT_INFO = "192.168.233.16:2181,192.168.233.17:2181,192.168.233.18:2181";
    private static final int TIME_OUT = 4000;

    // TODO: 2023/7/7 备用父节点
    private static final String LOCK_PARENT_NODE = "/parent_synclock";
    private static final String LOCK_SUB_NODE = LOCK_PARENT_NODE + "/sub_sync_lock";
    private static final String CURRENT_NODE = "192.168.233.18";

    private static final Random random = new Random();
    private static ZooKeeper zookeeper = null;

    private static ArrayList<ACL> acls = ZooDefs.Ids.OPEN_ACL_UNSAFE;
    private static CreateMode mode = CreateMode.PERSISTENT;
    //MC TODO:latch就相当于一个对象锁 当latch.await()方法执行时，方法所在的线程会等待
    //MC TODO:当latch的count减为0时，将会唤醒等待的线程
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zookeeper = new ZooKeeper(CONNECT_INFO, TIME_OUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                //MC TODO:确保连接信息
                if(countDownLatch.getCount() > 0 && event.getState() == Event.KeeperState.SyncConnected){
                    System.out.println("创建会话成功");
                    countDownLatch.countDown();
                }
                String listen_path = event.getPath();
                Event.EventType eventType = event.getType();
                System.out.println(listen_path + "\t" + eventType);

                /**
                 * 如果某个任务做完了之后把同步锁给删掉了，则所有的等待任务都要收到通知，然后去争抢注册锁
                 */
                if(listen_path.equals(LOCK_SUB_NODE) && eventType.equals(Event.EventType.NodeDeleted)){
                    //MC TODO:模拟去抢资源锁 创建的是临时节点 好处是任务
                    // 掉线 自动释放锁
                    try {
                        String node = zookeeper.create(LOCK_SUB_NODE, LOCK_SUB_NODE.getBytes(), acls, CreateMode.PERSISTENT);
                        //MC TODO:继续注册监听
                        try {
                            zookeeper.exists(LOCK_SUB_NODE,true);
                        } catch (KeeperException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        //MC TODO:执行业务逻辑
                        handleBusiness(zookeeper, CURRENT_NODE);
                    } catch (KeeperException | InterruptedException e) {
                        System.out.println("我没有抢到独占锁，等下一次吧");
                    }
                }else if(listen_path.equals(LOCK_SUB_NODE) && eventType.equals(Event.EventType.NodeCreated)){
                    System.out.println("节点创建");
                }
                //MC TODO:继续注册监听
                try {
                    zookeeper.exists(LOCK_SUB_NODE,true);
                } catch (KeeperException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        /**
         * 第二步 zookeeper会话创建成功 主线程恢复执行
         */
        countDownLatch.await();
        /**
         * 第三步 确保父节点的存在
         */
        Stat exists_parent = zookeeper.exists(LOCK_PARENT_NODE, false);
        if (exists_parent == null) {
            zookeeper.create(LOCK_PARENT_NODE, LOCK_PARENT_NODE.getBytes(), acls, mode);
        }
        /**
         * 第四步 注册监听
         */
        zookeeper.exists(LOCK_SUB_NODE, true);
        /**
         * 第五步争抢锁
         * 模拟去抢资源锁, 创建的是临时节点，好处是任务掉线，自动释放锁
         */
        try{
            zookeeper.create(LOCK_SUB_NODE, LOCK_SUB_NODE.getBytes(), acls, CreateMode.EPHEMERAL);

            /**
             * 第六步：执行业务逻辑
             */
            handleBusiness(zookeeper, CURRENT_NODE);
        } catch (Exception e){
            System.out.println("锁已经被别人持有了。等下一次抢吧");
        }
        /**
         * 第七步：保持程序一直运行
         */
        Thread.sleep(Integer.MAX_VALUE);
    }

    private static void handleBusiness(ZooKeeper zookeeper, String server) throws InterruptedException, KeeperException {
        int sleepTime = 10000;
        System.out.println(server + "is working ....." + System.currentTimeMillis());
        //MC TODO:线程睡眠0-4秒钟 是模拟业务代码处理所消耗的时间
        Thread.sleep(random.nextInt(sleepTime));
        //MC TODO:模拟业务处理完成
        zookeeper.delete(LOCK_SUB_NODE,-1);
        System.out.println(server + "is done -------"+  + System.currentTimeMillis());
        //MC TODO:线程睡眠0-4秒，是为了模拟客户端每次处理完了之后再次处理业务的一个时间间隔
        //MC TODO:最终的目的就是用来打乱你运行的多台服务器抢注该子节点的顺序
        Thread.sleep(random.nextInt(sleepTime));
    }
}
