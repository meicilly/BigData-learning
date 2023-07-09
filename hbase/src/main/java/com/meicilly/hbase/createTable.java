package com.meicilly.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class createTable {
    public static void main(String[] args) throws IOException {
        //MC TODO:判断表是否存在
        //MC TODO:创建配置对象
        Configuration conf = new Configuration();
        //MC TODO:添加配置参数
        conf.set("hbase.zookeeper.quorum","192.168.233.11,192.168.233.12,192.168.233.13");
        //MC TODO:创建hbase的连接 默认使用的是同步连接
        Connection connection =
                ConnectionFactory.createConnection(conf);
        //MC TODO:可以使用异步连接 主要影响后续的DML操作
        //CompletableFuture<AsyncConnection> asyncConnection = ConnectionFactory.createAsyncConnection(conf);
        //MC TODO:获取admin
        Admin admin = connection.getAdmin();
        //MC TODO:创建表格描述建造者
        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(TableName.valueOf("bigdata", "test1"));
        //MC TODO:添加参数
        //MC TODO:创建列族描述建造者
        ColumnFamilyDescriptorBuilder info = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("info"));
        //MC TODO:对列族添加版本
        info.setMaxVersions(5);
        //MC TODO:创建添加完参数的列族描述
        tableDescriptorBuilder.setColumnFamily(info.build());
        //MC TODO:创建对应的表格
        admin.createTable(tableDescriptorBuilder.build());
        //MC TODO:使用连接
        //System.out.println(connection);
        //MC TODO:关闭连接
        connection.close();
    }
}
