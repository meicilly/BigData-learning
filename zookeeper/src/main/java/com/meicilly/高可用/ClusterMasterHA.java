package com.meicilly.高可用;


import org.apache.zookeeper.ZooKeeper;

/**
 * 这个程序是模拟主从架构的服务器主节点
 *
 * 核心业务
 * 如果A是第一个上线的master 它会自动成为active状态
 * 如果B是第二个上线的master 它会自动称为standby状态
 * 如果C是第三个上线的master 它会自动称为standby状态
 * 如果A宕机了之后 B和C去竞选谁称为active状态
 * 然后A上线 应该要自动称为standby
 * 然后如果standby中的任何一个节点宕机，剩下的节点的active和standby的状态不用改变
 * 然后如果active中的任何一个节点宕机，那么剩下的standby节点就要去竞选active状态
 */
public class ClusterMasterHA {
    private static ZooKeeper zk = null;
    private static final String CONNECT_STRING = "192.168.233.16:2181,192.168.233.17:2181,192.168.233.18:2181";
    private static final int Session_TimeOut = 4000;

    private static final String PARENT = "/cluster_ha";
    private static final String ACTIVE = PARENT + "/active";
    private static final String STANDBY = PARENT + "/standby";
    private static final String LOCK = PARENT + "/lock";
    /**
     * 模拟生成的主机名称 如果在正式企业环境中 肯定是通过换进变量来进行获取的
     */
    private static final String HOSTNAME = "hadoop3";
}
