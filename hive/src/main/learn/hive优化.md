### Explain查看执行计划
#### 创建测试用表
```sql
-- 创建大表 小表和join后表的语句
-- 大表
create table bigtable(id bigint, t bigint, uid string, keyword string,
url_rank int, click_num int, click_url string) row format delimited 
fields terminated by '\t';

-- 小表
create table smalltable(id bigint, t bigint, uid string, keyword string,
url_rank int, click_num int, click_url string) row format delimited 
fields terminated by '\t';

-- join后的表
create table jointable(id bigint, t bigint, uid string, keyword string,
url_rank int, click_num int, click_url string) row format delimited 
fields terminated by '\t';

-- 分别向大表和小表中导入数据
load data local inpath '/opt/bigtable' into table bigtable;
load data local inpath '/opt/smalltable' into table smalltable;
    
-- 基本语法
EXPLAIN [EXTENDED | DEPENDENCY | AUTHORIZATION] query-sql

-- 查看执行计划
explain select * from bigtable;
explain select click_url,count(*) ct from bigtable group by click_url;

-- 查看详细的执行计划
explain extended select * from bigtable;
explain extended select click_url,count(*) ct from bigtable group by click_url;
```
### Hive建表优化
#### 分区表
- 分区表实际上就是对应一个HDFS文件系统上的独立的文件夹，该文件夹下是该分区所有的数据文件
- Hive中的分区就是分目录，把一个大的数据集根据业务需要分割成小的数据集
- 在查询是通过WHERE子句中的表达式选择查询所需要的指定的分区，这样的查询效率会提高很多，所以通常在where语句中指定为表的分区字段
```sql
-- 创建分区表
create table dept_partition(
    deptno int, dname string, loc string
)
partitioned by (day string)
row format delimited fields terminated by '\t';
-- 加载数据
load data local inpath '/opt/dept_20200401.log' into table dept_partition partition(day='20200401');
load data local inpath '/opt/dept_20200402.log' into table dept_partition partition(day='20200402');
load data local inpath '/opt/dept_20200403.log' into table dept_partition partition(day='20200403');
-- 单分区查询
select * from dept_partition where day='20200401';
-- 多分区查询
select * from dept_partition where day='20200401'
union
select * from dept_partition where day='20200402'
union
select * from dept_partition where day='20200403';

select * from dept_partition where day='20200401' or
    day='20200402' or day='20200403';
```
### HQL语法优化
#### 列裁剪与分区裁剪
列裁剪就是在查询时只读需要的列，分区裁剪就是只读取需要的分区。当列很多或者数据量很大时，如果select *或者不指定分区，全列扫描效率都很低。
Hive 在读数据的时候，可以只读取查询中所需要用到的列，而忽略其他的列。这样做可以节省读取开销：中间表存储开销和数据整合开销。
#### Group By
- 默认情况下，Map 阶段同一 Key 数据分发给一个 Reduce，当一个 key 数据过大时就倾斜了。
- 并不是所有的聚合操作都需要在 Reduce 端完成，很多聚合操作都可以先在 Map 端进行部分聚合，最后在 Reduce 端得出最终结果。
开启 Map 端聚合参数设置
-- 是否在Map端进行聚合 默认为True
```sql
set hive.map.agrr = true;
```
-- 在Map端进行聚合操作的条目数目
```sql
set hive.groupby.mapagrr.checkinterval = 100000;
```
-- 有数据倾斜的时候进行负载均衡(默认是false)
```sql
set hive.groupby.skewindata = true;
```
- 当选项设定为 true，生成的查询计划会有两个 MR Job。
- 第一个 MR Job 中，Map 的输出结果会随机分布到 Reduce 中，每个 Reduce 做部分聚合操作，并输出结果，这样处理的结果是相同的 Group By Key 有可能被分发到不同的 Reduce
中，从而达到负载均衡的目的
- 第二个 MR Job 再根据预处理的数据结果按照 Group By Key 分布到 Reduce 中（这个过程可以保证相同的 Group By Key 被分布到同一个 Reduce 中），最后完成最终的聚合操作（虽然
  能解决数据倾斜，但是不能让运行速度的更快）。
#### Vectorization
- vectorization : 矢量计算的技术，在计算类似scan, filter, aggregation的时候，vectorization
技术以设置批处理的增量大小为 1024 行单次来达到比单条记录单次获得更高的效率。
```sql
set hive.vectorized.execution.enabled = true;
set hive.vectorized.execution.reduce.enabled = true;
```
#### 多重模式
- 如果你碰到一堆 SQL，并且这一堆 SQL 的模式还一样。都是从同一个表进行扫描，做不同的逻辑。有可优化的地方：如果有 n 条 SQL，每个 SQL 执行都会扫描一次这张表。
```sql
insert .... select id,name,sex, age from student where age > 17;
insert .... select id,name,sex, age from student where age > 18;
insert .... select id,name,sex, age from student where age > 19;
```
- 隐藏了一个问题：这种类型的 SQL 有多少个，那么最终。这张表就被全表扫描了多少次
```sql
insert int t_ptn partition(city=A). select id,name,sex, age from student 
where city= A;
insert int t_ptn partition(city=B). select id,name,sex, age from student 
where city= B;
insert int t_ptn partition(city=c). select id,name,sex, age from student 
where city= c;
-- 修改为：
from student
insert int t_ptn partition(city=A) select id,name,sex, age where city= A
insert int t_ptn partition(city=B) select id,name,sex, age where city= B
```
如果一个 HQL 底层要执行 10 个 Job，那么能优化成 8 个一般来说，肯定能有所提高，多重插入就是一个非常实用的技能。一次读取，多次插入，有些场景是从一张表读取数据后，要多次利用
#### in/exists 语句
- 在 Hive 的早期版本中，in/exists 语法是不被支持的，但是从 hive-0.8x 以后就开始支持这个语法。但是不推荐使用这个语法。虽然经过测验，Hive-2.3.6 也支持 in/exists 操作，但
- 还是推荐使用 Hive 的一个高效替代方案：left semi join
- 比如说：-- in / exists 实现
- select a.id, a.name from a where a.id in (select b.id from b);
- select a.id, a.name from a where exists (select id from b where a.id = b.id);
- 可以使用 join 来改写：
- select a.id, a.name from a join b on a.id = b.id;
- 应该转换成：
-- left semi join 实现
- select a.id, a.name from a left semi join b on a.id = b.id;
#### CBO优化
- join 的时候表的顺序的关系：前面的表都会被加载到内存中。后面的表进行磁盘扫描
```sql
select a.*, b.*, c.* from a join b on a.id = b.id join c on a.id = c.id;
```
- Hive 自 0.14.0 开始，加入了一项 "Cost based Optimizer" 来对 HQL 执行计划进行优化，这个功能通过 "hive.cbo.enable" 来开启。在 Hive 1.1.0 之后，这个 feature 是默认开启的，它可以 自动优化 HQL 中多个 Join 的顺序，并选择合适的 Join 算法。
- CBO，成本优化器，代价最小的执行计划就是最好的执行计划。传统的数据库，成本优化器做出最优化的执行计划是依据统计信息来计算的。
- Hive 的成本优化器也一样，Hive 在提供最终执行前，优化每个查询的执行逻辑和物理执行计划。这些优化工作是交给底层来完成的。根据查询成本执行进一步的优化，从而产生潜在的不同决策：如何排序连接，执行哪种类型的连接，并行度等等。
```sql
-- 要使用基于成本的优化（也称为 CBO），请在查询开始设置以下参数：
set hive.cbo.enable=true;
set hive.compute.query.using.stats=true;
set hive.stats.fetch.column.stats=true;
set hive.stats.fetch.partition.stats=true;
```
#### 谓词下推
- 将 SQL 语句中的 where 谓词逻辑都尽可能提前执行，减少下游处理的数据量。对应逻辑优化器是 PredicatePushDown，配置项为 hive.optimize.ppd，默认为 true。
- 打开谓词下推优化属性
```sql
 set hive.optimize.ppd = true; -- 谓词下推，默认是 true
```
- 查看先关联两张表，再用 where 条件过滤的执行计划
```sql
 explain select o.id from bigtable b join bigtable o on o.id = b.id where o.id <= 10;
```
- 查看子查询后，再关联表的执行计划
```sql
 explain select b.id from bigtable b join (select id from bigtable where id <= 10) o on b.id = o.id;
```
#### MapJoin
- MapJoin 是将 Join 双方比较小的表直接分发到各个 Map 进程的内存中，在 Map 进程中进行 Join 操 作，这样就不用进行 Reduce 步骤，从而提高了速度。如果不指定 MapJoin或者不符合 MapJoin 的条件，那么 Hive 解析器会将 Join 操作转换成 Common Join，即：在Reduce 阶段完成 Join。容易发生数据倾斜。可以用 MapJoin 把小表全部加载到内存在 Map端进行 Join，避免 Reducer 处理。
- 开启 MapJoin 参数设置
```sql
-- 设置自动选择 MapJoin
set hive.auto.convert.join=true; #默认为 true
-- 大表小表的阈值设置（默认 25M 以下认为是小表）：
set hive.mapjoin.smalltable.filesize=25000000;
```
- MapJoin 工作机制
```
MapJoin 是将 Join 双方比较小的表直接分发到各个 Map 进程的内存中，在 Map 进
程中进行 Join 操作，这样就不用进行 Reduce 步骤，从而提高了速度。
```
#### 大表 大表SMB Join