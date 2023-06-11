### mapreduce的任务提交源码详解
开始执行用户的主类的main方法，执行 job.waitForCompetetion() 来等待任务执行完成。
在 Job.submit() 方法中，最重要的是：
```javascript
//设置mapreduce的状态为： DEFINE 状态是define任务才能运行
ensureState(JobState.DEFINE);
//设置启用new API
// 启用新API, 即org.apache.hadoop.mapreduce下的Mapper和Reducer
// TODO_MA 注释： mapreduce-2.x以前的API： oldAPI， mapreduce-2.x以后的版本的API：newAPI
// TODO_MA 注释： oldAPI: class MyMapper implements Mapper
// TODO_MA 注释： newAPI: class MyMapper extends Mapper
// TODO_MA 注释： 老版本包：org.apache.hadoop.mapred.xxx
// TODO_MA 注释： 新版本包：org.apache.hadoop.mapreduce.xxxx
setUseNewAPI();
//获取提交客户端，链接YARN集群
/*************************************************
 *   注释： 这个 connect(); 主要完成链接 YARN 集群并获取 提交客户端的任务
 *   ----------
 *   1、主要作用就是初始化一个 Runner, 用来和 ResourceManager通信
 *   初始化工作，为 cluster 赋值， 最主要的就是为 cluster 中的 client 赋值，client 即为提交器，分为
 *   local 提交器 和 YARN 提交器，具体创建哪个由配置信息决定。
 *   ---------
 *   2、MapReduce 作业提交时连接集群是通过 Job 的 connect() 方法实现的，它实际上是构造集群 Cluster 实例 cluster。
 *   Cluster 为连接 MapReduce 集群的一种工具，提供了一种获取 MapReduce 集群信息的方法。
 *   ---------
 *   3、在 Cluster 内部，有一个与集群进行通信的客户端通信协议 ClientProtocol 实例 client，
 *   它由 ClientProtocolProvider 的静态 create() 方法构造，而 Hadoop2.x中提供了两种模式的 ClientProtocol，
 *   分别为 Yarn 模式的 YARNRunner 和 Local 模式的 LocalJobRunner，Cluster 实际上是由它们负责与集群进行通信的，
 *   而Yarn模式下，ClientProtocol 实例 YARNRunner 对象内部有一个 ResourceManager 代理 ResourceMgrDelegate 实例 resMgrDelegate，
 *   Yarn 模式下整个 MapReduce 客户端就是由它负责与 Yarn 集群进行通信，完成诸如作业提交、作业状态查询等过程，通过它获取集群的信息。
 *   ---------
 *   4、总结： connect(); 该方法的执行，在job中，初始化了一个 cluster 对象， cluster 对象中，
 *   又初始化了一个 client 对象， client 就是 YarnRunner 对象。
 *   -
 *   0、connect() 方法的目的，就是初始化一个 cluster 对象，对象中有一个成员变量： ClientProtocol
 *              有两种实现：  LocalJobRunner + YARNRunner
 *   1、YARNRunner 有一个成员变量： ResourceMgrDelegate resMgrDelegate
 *   2、resMgrDelegate 中有一个成员变量： YarnClientImpl yarnClient
 */
connect();
//获取提交器
/*************************************************
 *   注释： 构建一个提交器，submitter = JobSubmitter
 *   1、cluster.getFileSystem() = HDFS = DistributedFileSystem
 *   2、cluster.getClient() = YARNRunner
 */
submitter = getJobSubmitter(cluster.getFileSystem(), cluster.getClient());
//提交任务
submitter.submitJobInternal(Job.this, cluster);
//提交之后，设置 mapreduce 的状态为： RUNNING
state = JobState.RUNNING;
```
connect()方法做的主要的事情:
```
1、Job 的内部有一个 Cluster cluster 成员变量
2、Cluster 的内部有一个 YARNRunner client 的成员变量
3、YARNRunner 的内部有一个 ResourceMgrDelegate resMgrDelegate 成员变量
4、ResourceMgrDelegate 的内部有一个 YarnClientImpl client 成员变量
5、YarnClientImpl 的内部有一个 ApplicationClientProtocol rmClient 的成员变量
```
最终：rmClient 这个东西，就是 RM 的代理对象！
```
最终提交任务是通过：rmClient.submitApplication();
```
以下是提交过程
```javascript
//第一步：通过 JobSubmitter 提交 Job
//在这一步，会做很多一些细节操作。
JobSubmitter.submitJobInternal(Job.this, cluster);
// 当进入 JobSubmitter 的提交步骤的时候，内部做了很多细节操作
// 生成了 JobID
// 重要的事情：包括：1、Task启动所需要的资源生成好并且防止在HDFS上， 2、执行逻辑切片
//第二步：通过 YARNRunner 来提交 Job
submitClient.submitJob(jobId, submitJobDir.toString(), job.getCredentials());
//第三步：通过 ResourceMgrDelegate 来提交 Job, 获取了 ApplicationID
resMgrDelegate.submitApplication(appContext);
//第四步：通过 YarnClientImpl 提交 ApplicationID
client.submitApplication(appContext);
//第五步：通过 ResrouceManager 的代理对象：ApplicationClientProtocol 来提交 ApplicationID
rmClient.submitApplication(request);
```
最终：ResourceManager 组件中的 ClientRMService 来执行 submitApplication() 的 RPC 服务处理