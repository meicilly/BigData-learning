### 创建数据库
![建表语法](./img/DDL创建数据库语法.png)
```sql
# 创建一个数据库不指定路径
create database db_hive1;
# 创建一个数据库 指定路径 是hdfs上的路径
create database db_hive2 location '/db_hive2';
# 创建一个数据库 带有dbproperties
create database db_hive3 with dbproperties('create_date'='2023-6-17');
```
### 查询数据库
#### 展示所有数据库
```sql
show databases like 'db_hive*';
```
#### 查看数据库信息
```sql
# 查看基本信息
desc database db_hive3;
# 查看更多信息
desc database extended db_hive3;
```
#### 修改数据库
用户可以使用alter database命令修改数据库某些信息，其中能够修改的信息包括dbproperties、location、owner user。需要注意的是：修改数据库location，不会改变当前已有表的路径信息，而只是改变后续创建的新表的默认的父目录。
```sql
# 语法
-- 修改dbproperties
ALTER DATABASE database_name SET DBPROPERTIES (property_name=property_name,...);
    
-- 修改location
ALTER DATABASE database_name SET LOCATION hdfs_path;
    
-- 修改owner user
ALTER DATABASE database_name SET OWNER USER user_name;
    
# 修改dbproperties
ALTER DATABASE db_hive3 SET DBPROPERTIES ('create_date'='2022-6-17');
```
#### 删除数据库
```sql
DROP DATABASE [IF EXISTS] database_name [RESTRICT|CASCADE];
```
注：RESTRICT：严格模式，若数据库不为空，则会删除失败，默认为该模式。
CASCADE：级联模式，若数据库不为空，则会将库中的表一并删除。
```sql
# 删除空数据库
drop database db_hive2;
# 删除非空数据库
drop database db_hive3 cascade;
```
#### 切换当前数据库
```sql
USE database_name;
```
### 表
```sql
# 完整语法
CREATE [TEMPORARY] [EXTERNAL] TABLE [IF NOT EXISTS] 
[db_name.]table_name
[(col_name data_type [COMMENT col_comment], ...)]
[COMMENT table_comment]
[PARTITIONED BY (col_name data_type [COMMENT col_comment], ...)]
[CLUSTERED BY (col_name, col_name, ...) 
[SORTED BY (col_name [ASC|DESC], ...)] INTO num_buckets BUCKETS]
[ROW FORMAT row_format]
[STORED AS file_format]
[LOCATION hdfs_path]
[TBLPROPERTIES (property_name=property_value, ...)]
-- TEMPORARY 临时表 该表只在当前会话可见 会话结束 表会被删除
    
-- EXTERNAL 外部表 与之相对应的是内部表（管理表）。管理表意味着Hive会完全接管该表，包括元数据和HDFS中的数据。而外部表则意味着Hive只接管元数据，而不完全接管HDFS中的数据。
-- PARTITIONED BY 创建分区
    
-- CLUSTERED BY ... SORTED BY...INTO...BUCKETS(重点) 创建分桶表
    
-- ROW FORMAT 指定SERDE，SERDE是Serializer and Deserializer的简写。Hive使用SERDE序列化和反序列化每行数据。详情可参考 Hive-Serde。语法说明如下：
-- 语法一：DELIMITED关键字表示对文件中的每个字段按照特定分割符进行分割，其会使用默认的SERDE对每行数据进行序列化和反序列化。
-- fields terminated by ：列分隔符
-- collection items terminated by ： map、struct和array中每个元素之间的分隔符
-- map keys terminated by ：map中的key与value的分隔符
-- lines terminated by ：行分隔符
-- 语法二：SERDE关键字可用于指定其他内置的SERDE或者用户自定义的SERDE。例如JSON SERDE，可用于处理JSON字符串。
    
-- STORED AS 指定文件格式，常用的文件格式有，textfile（默认值），sequence file，orc file、parquet file等等。
    
-- LOCATION 指定表所对应的HDFS路径，若不指定路径，其默认值为 ${hive.metastore.warehouse.dir}/db_name.db/table_name
    
-- TBLPROPERTIES 用于配置表的一些KV键值对参数
```
```sql
基本数据类型
-- data_type hive中的字段可分为基本数据类型和复杂数据类型
-- tinyint  1byte有符号整数
-- smallint 2byte有符号整数
-- int      4byte有符号整数
-- bigint   8byte有符号整数
-- boolean  布尔类型 true或者false
-- float    单精度浮点数
-- double   双精度浮点数
-- decimal  十进制精准数字类型
-- varchar  字符序列，需指定最大长度，最大长度的范围是[1,65535]
-- string   字符串 无需指定最大长度
-- timestamp  时间类型
-- binary   二进制数据
复制数据类型
-- array 数组是一组相同类型的值的集合 array<string>  arr[0]
-- map   map是一组相同类型的键-值对集合 map<string,int> map['key']
-- struct 结构体由多个属性组成，每个属性都有自己的属性名和数据类型 struct<id:int,name:string> struct.id
```
类型转换
```
hive的基本数据类型都可以做类型转换 转换的方式包括隐式转换以及显示转换
```
隐式转换
```
a.任何整数类型都可以转换为一个范围更广的类型，如tinyint可以转换成int int可以转换成bigint
b.所有整数类型、float和string类型都可以隐式转换成double
c.tinyint、smallint、int都可以转换成float
d.boolean类型不可以转换为任何其它的类型
```
显示转换
```sql
select '1' + 2, cast('1' as int) + 2;
```
#### 内部表语外部表
##### 内部表
Hive中默认创建的表都是的内部表，有时也被称为管理表。对于内部表，Hive会完全管理表的元数据和数据文件。
```sql
create table if not exists student(
    id int,
    name string
)
row format delimited fields terminated by '\t'
location '/opt/software/student.txt';
```
##### 外部表
外部表通常可用于处理其他工具上传的数据文件，对于外部表，Hive只负责管理元数据，不负责管理HDFS中的数据文件。
```sql
create external table if not exists student2(
    id int,
    name string
)
row format delimited fields terminated by '\t'
location '/user/hive/warehouse714/student'; --此目录是文件夹目录
```
##### SERDE和复杂数据类型
数据
```json
{"name":"dasongsong","friends":["bingbing","lili"],"students":{"xiaohaihai":18,"xiaoyangyang":16},"address":{"street":"hui long guan","city":"beijing","postal_code":10010}}
```
```sql
create external table teacher
(
    name string,
    friends array<string>,
    students map<string,int>,
    address struct<city:string,street:string,postal_code:int>
)
row format serde 'org.apache.hadoop.hive.serde2.JsonSerDe'
location '/user/hive/warehouse714/teacher';
```
```sql
create table teacher1 as select * from teacher;
create table teacher2 like teacher;
```
#### 查看表
```sql
show tables like 'stu*';
-- 查看基本信息
desc stu;
-- 查看更多信息
desc formatted stu;
```
#### 修改表
##### 重命名表
```sql
alter table student1 rename to student11;
```
##### 修改信息列表
```sql
-- 增加列 ALTER TABLE table_name ADD COLUMNS (col_name data_type [COMMENT col_comment], ...)
-- 更新列 ALTER TABLE table_name CHANGE [COLUMN] col_old_name col_new_name column_type [COMMENT col_comment] [FIRST|AFTER column_name]
-- 替换列 ALTER TABLE table_name REPLACE COLUMNS (col_name data_type [COMMENT col_comment], ...)

-- 查看表结构
desc stu;
-- 添加列
alter table stu add columns(age int);
-- 更新列
alter table stu change column age ages double;
-- 替换列
alter table stu replace columns(id int, name string);
```
#### 删除表
```sql
drop table stu;
```
#### 清空表
```sql
truncate table student;
```
### DML数据操作
#### Load
Load语句可将文件导入到hive中
```sql
LOAD DATA [LOCAL] INPATH 'filepath' [OVERWRITE] INTO TABLE tablename [PARTITION (partcol1=val1, partcol2=val2 ...)];
-- local 表示从本地加载数据到Hive表；否则从HDFS加载数据到Hive表。
-- overwrite：表示覆盖表中已有数据，否则表示追加。
-- partition：表示上传到指定分区，若目标是分区表，需指定分区。
create table student_load(
    id int,
    name string
)
row format delimited fields terminated by '\t';

-- 加载本地文件到hive
load data local inpath '/opt/student.txt' into table student_load;
    
-- 加载hdfs上的数据
load data  inpath '/opt/student.txt' into table student_load;
    
-- 加载数据覆盖表中已有的数据
load data  inpath '/opt/student.txt' overwrite into table student_load;
```
#### Insert
##### 将查询结插入表中
```sql
INSERT (INTO | OVERWRITE) TABLE tablename [PARTITION (partcol1=val1, partcol2=val2 ...)] select_statement;
-- INTO:将结果追加到目标表
-- OVERWRITE:用结果覆盖原有数据
create table student_insert(
    id int,
    name string
)
row format delimited fields terminated by '\t';

-- 根据查询结果插入数据
insert overwrite table student_insert 
select id,name from student_load; 
```
##### 将给定Values插入表中
```sql
INSERT (INTO | OVERWRITE) TABLE tablename [PARTITION (partcol1[=val1], partcol2[=val2] ...)] VALUES values_row [, values_row ...]
    
insert into table student_insert values(1,"meicilly"),(2,"hihi");
```
##### 将查询结果写入目标路径
```sql
INSERT OVERWRITE [LOCAL] DIRECTORY directory
[ROW FORMAT row_format] [STORED AS file_format] select_statement;
    
insert overwrite local directory '/opt/module/datas/student' ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.JsonSerDe'
select id,name from student;
```
#### Export&Import
```sql
--导出
EXPORT TABLE tablename TO 'export_target_path'

--导入
IMPORT [EXTERNAL] TABLE new_or_original_tablename FROM 'source_path' [LOCATION 'import_target_path']
    
--导出
hive>
export table default.student to '/user/hive/warehouse/export/student';

--导入
hive>
import table student2 from '/user/hive/warehouse/export/student';
```
### 查询
#### 基础语法
```sql
SELECT [ALL | DISTINCT] select_expr, select_expr, ...
FROM table_reference       -- 从什么表查
[WHERE where_condition]   -- 过滤
[GROUP BY col_list]        -- 分组查询
   [HAVING col_list]          -- 分组后过滤
[ORDER BY col_list]        -- 排序
[CLUSTER BY col_list
| [DISTRIBUTE BY col_list] [SORT BY col_list]
[LIMIT number]                -- 限制输出的行数
```
#### 基本查询
```sql
-- 创建部门表
create table if not exists dept(
    deptno int comment '部门编号',
    dname string comment '部门名称',
    loc int comment '部门位置'
)
row format delimited fields terminated by '\t';

-- 创建员工表
create table if not exists emp(
    empno int comment "员工编号",
    ename string comment "员工姓名",
    job string comment "员工岗位",
    sal double comment "员工薪资",
    deptno int comment "部门编号"
)
row format delimited fields terminated by "\t";

load data local inpath "/opt/dept.txt" into table dept;
load data local inpath "/opt/emp.txt" into table emp;
```
#### 全表和特定列查询
```sql
-- 全表查询
select * from emp;
-- 选择特定列查询
select empno,ename from emp;
-- 列别名
select ename AS name, deptno dn from emp;
-- limit语句
select * from emp limit 5;
select * from emp limit 2,3; --表示从第二行开始 向下抓取3行
-- where语句
select * from emp where sal > 1000;
-- 操作符	支持的数据类型	描述
-- A=B	基本数据类型	如果A等于B则返回true，反之返回false
-- A<=>B	基本数据类型	如果A和B都为null或者都不为null，则返回true，如果只有一边为null，返回false
-- A<>B, A!=B	基本数据类型	A或者B为null则返回null；如果A不等于B，则返回true，反之返回false
-- A<B	基本数据类型	A或者B为null，则返回null；如果A小于B，则返回true，反之返回false
-- A<=B	基本数据类型	A或者B为null，则返回null；如果A小于等于B，则返回true，反之返回false
-- A>B	基本数据类型	A或者B为null，则返回null；如果A大于B，则返回true，反之返回false
-- A>=B	基本数据类型	A或者B为null，则返回null；如果A大于等于B，则返回true，反之返回false
-- A [not] between B and C	基本数据类型	如果A，B或者C任一为null，则结果为null。如果A的值大于等于B而且小于或等于C，则结果为true，反之为false。如果使用not关键字则可达到相反的效果。
-- A is null	所有数据类型	如果A等于null，则返回true，反之返回false
-- A is not null	所有数据类型	如果A不等于null，则返回true，反之返回false
-- in（数值1，数值2）	所有数据类型	使用 in运算显示列表中的值
-- A [not] like B	string 类型	B是一个SQL下的简单正则表达式，也叫通配符模式，如果A与其匹配的话，则返回true；反之返回false。B的表达式说明如下：‘x%’表示A必须以字母‘x’开头，‘%x’表示A必须以字母‘x’结尾，而‘%x%’表示A包含有字母‘x’,可以位于开头，结尾或者字符串中间。如果使用not关键字则可达到相反的效果。
-- A rlike B, A regexp B	string 类型	B是基于java的正则表达式，如果A与其匹配，则返回true；反之返回false。匹配使用的是JDK中的正则表达式接口实现的，因为正则也依据其中的规则。例如，正则表达式必须和整个字符串A相匹配，而不是只需与其字符串匹配。

-- 查询薪水大于1000 部门是30
select * from emp where sal > 1000 and deptno = 30;
-- 查询薪水大于1000 或者部门是30
select * from emp where sal > 1000 or deptno=30;
```
#### 聚合函数
```
count(*) 表示统计所有行数 包含null值
count(某列)，表示该列一共有多少行，不包含null值
max()，求最大值，不包含null，除非所有值都是null
min()，求最小值，不包含null，除非所有值都是null
sum()，求和，不包含null
avg()，求平均值，不包含null
```
```sql
-- 求总行数
select count(*) cnt from emp;
-- 求工资最大值
select max(sal) max_sal from emp;
-- 求工资的最小值
select min(sal) min_sal from emp;
-- 求工资的总和
select sum(sal) sum_sal from emp;
-- 求工资的平均值
select avg(sal) avg_sal from emp;

-- 求每个部门的平均工资
select deptno,avg(sal) from emp group by deptno;
-- 求每个部门的平均薪水大于2000的部门
select deptno,avg(sal) avg_sal from emp group by deptno having avg_sal > 2000;
```
#### Join语句
```sql

```
