package com.meicilly.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.AsyncConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class createNamespace {
    //MC TODO:添加静态属性connection指向单例连接
    public static Connection connection = HBaseConnect.connection;
    public static void main(String[] args) throws IOException {
        //MC TODO:创建配置对象
        Configuration conf = new Configuration();
        //MC TODO:添加配置参数
        conf.set("hbase.zookeeper.quorum","192.168.233.11,192.168.233.12,192.168.233.13");
        //MC TODO:创建hbase的连接 默认使用的是同步连接
        Connection connection =
                ConnectionFactory.createConnection(conf);
        //MC TODO:可以使用异步连接 主要影响后续的DML操作
        CompletableFuture<AsyncConnection> asyncConnection = ConnectionFactory.createAsyncConnection(conf);
        //MC TODO:使用连接
        System.out.println(connection);
        //MC TODO:获取admin  admin 的连接是轻量级的 不是线程安全的 不推荐池化或者缓存这个连接
        Admin admin = connection.getAdmin();
        //MC TODO:创建命名空间
        NamespaceDescriptor.Builder builder = NamespaceDescriptor.create("test1");
        //MC TODO:给命名空间添加需求
        builder.addConfiguration("user","root");
        //MC TODO:使用 builder 构造出对应的添加完参数的对象 完成创建
        try {
            admin.createNamespace(builder.build());
        } catch (IOException e) {
            System.out.println("命令空间已经存在");
            e.printStackTrace();
        }
        //MC TODO:关闭
        admin.close();
    }
}
