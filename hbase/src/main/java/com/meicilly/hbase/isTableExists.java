package com.meicilly.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.AsyncConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class isTableExists {
    /**
     * 判断表是否存在
     * @param args
     */
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

        //MC TODO:获取admin
        Admin admin = connection.getAdmin();
        //MC TODO:判断表格是否存在
        boolean b = admin.tableExists(TableName.valueOf("bigdata", "student"));
        System.out.println(b);

        //MC TODO:使用连接
        System.out.println(connection);
        //MC TODO:关闭连接
        connection.close();
    }
}
