package com.meicilly.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class modifyTable {
    public static void main(String[] args) throws IOException {
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
        //MC TODO:调用修改表格 获取之前的表格表述
        TableDescriptor descriptor = admin.getDescriptor(TableName.valueOf("bigdata", "test1"));
        //MC TODO:创建一个表格描述 如果使用填写 tableName 的方法 相当于创建了一个新的表格描述建者 没有之前的信息   如果想要修改之前的信息 必须调用方法填写一个旧的表格描述
        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(descriptor);
        //MC TODO:对应建造者进行表格数据的修改
        ColumnFamilyDescriptor info = descriptor.getColumnFamily(Bytes.toBytes("info"));
        //MC TODO:创建列族描述建造者 需要填写旧的列族
        ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder(info);
        //MC TODO:修改对应版本
        columnFamilyDescriptorBuilder.setMaxVersions(4);
        // 此处修改的时候 如果填写的新创建 那么别的参数会初始
        tableDescriptorBuilder.modifyColumnFamily(columnFamilyDescriptorBuilder.build());
        admin.modifyTable(tableDescriptorBuilder.build());
        //MC TODO:使用连接
        System.out.println(connection);
        //MC TODO:关闭连接
        connection.close();
    }
}
