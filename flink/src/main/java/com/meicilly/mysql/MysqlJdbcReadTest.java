package com.meicilly.mysql;

import java.sql.*;

public class MysqlJdbcReadTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // TODO: 2023-03-23 step1 加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // TODO: 2023-03-23 step2 获取连接Connection
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://192.168.88.11:3306/?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "123456"
        );
        // TODO: 2023-03-23 step3 创建Statement对象，设置语句(INSERT,SELECT)
        PreparedStatement pstmt = conn.prepareStatement("SELECT id, name, age FROM db_flink.t_student");
        // step4、执行操作，获取ResultSet对象
        ResultSet result = pstmt.executeQuery();
        // step5、遍历获取数据
        while (result.next()){
            // 获取每个字段的值
            int stuId = result.getInt("id");
            String stuName = result.getString("name");
            int stuAge = result.getInt("age");
            System.out.println("id = " + stuId + ", name = " + stuName + ", age = " + stuAge);
        }
        // step6、关闭连接
        result.close();
        pstmt.close();
        conn.close();
    }
}
