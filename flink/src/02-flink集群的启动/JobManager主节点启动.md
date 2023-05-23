#### Flink 主节点 StandaloneSessionClusterEntrypoint 启动源码分析
ClusterEntrypoint：集群启动入口类

Flink 主从架构： 主节点：JobManager + 从节点：TaskManager

JobManager是Flink集群的主节点，它包含三大重要的组件:
```
1.ResourceManager
    Flink的集群资源管理器，只有一个，关于slot的管理和申请等工作，都由它负责
2.Dispatcher
    负责接收用户提交的JobGraph,然后启动一个JobMaster，JobMaster类似于YARN集群中的AppMaster角色，类似于Spark Job中的Driver角色
    内部有一个持久服务：JobGraphStore，用来存储提交到JobManager中的Job信息，也是可以用作主节点宕机之后作job恢复用的
3.WebMonitorEndpoint rest服务器 = netty服务端
    里面维护了很多很多的Handler，也还会启动一个Netty服务端，用来接收外部的rest请求
    如果客户端通过flink run的方式来提交一个job到flink集群，最终是由WebMonitorEndpoint来接收处理，经过路由解析处理之后决定使用哪一个Handler来执行处理
    例如：submitJob ==> JobSubmitHandler
    Router路由器 绑定了一大堆Handler
```
总结一下：
```
1.Flink集群的主节点内部运行着：WebMonitorEndpoint，ResourceManager 和 Dispatcher。
2.当client通过rest方式提交一个job到集群运行的时候（客户端会把该Job构建成一个JobGraph对象）是由WebMonitorEndpoint来接受处理的，webMonitorEndpoint内部会通过Router进行路由解析找到对应的Handler来执行处理，处理完毕之后转交Dispatcher，Dispatcher负责拉起JobMaster来负责这个Job的Slot资源申请和Task的部署执行，关于Job执行过程中，所需要的Slot资源，由JobMaster向ResourceManager申请。 
```
对应的要启动这三个组件，都有一个对应的 Factory，也就说，如果需要创建这些组件实例，那么都是通过这些 Factory 来创建！
这三个 Facotry 最终，都会被封装在一个 ComponentFactory 中！
根据以上的启动脚本分析：JobManager 的启动主类：StandaloneSessionClusterEntrypoint
```shell
# 入口，解析命令行参数 和 配置文件 flink-conf.yaml
StandaloneSessionClusterEntrypoint.main()

    ClusterEntrypoint.runClusterEntrypoint(entrypoint);
        #通过父类方法来启动
        clusterEntrypoint.startCluster();
          #继续启动
          runCluster(configuration, pluginManager);
          #第一步：初始化各种服务(8个基础服务)
          #初始化了主节点对外提供服务的时候所需要的三大核心组件启动时所需要的基础服务
          #1、commonRpcService:    基于 Akka 的 RpcService 实现。RPC 服务启动 Akka 参与者来接收从 RpcGateway 调用 RPC
          #2、JMXService:          启动一个 JMXService
          #3、ioExecutor:          启动一个线程池
          #4、haServices: 	   提供对高可用性所需的所有服务的访问注册，分布式计数器和领导人选举
          #5、blobServer: 	   负责侦听传入的请求生成线程来处理这些请求。它还负责创建要存储的目录结构 blob 或临时缓存它们
          #6、heartbeatServices:   提供心跳所需的所有服务。这包括创建心跳接收器和心跳发送者
          #7、metricRegistry:  	   跟踪所有已注册的 Metric，它作为连接 MetricGroup 和 MetricReporter
          #8、archivedExecutionGraphStore:  存储执行图ExecutionGraph的可序列化形式
            #比较重要的：HAService， RpcServices， HeatbeatServices，....
            initializeServices(configuration, pluginManager);
                 #1.创建一个 Akka rpc 服务 commonRpcService： 基于 Akka 的 RpcService 实现
                 #commonRpcService 其实是一个基于 akka 得 actorSystem，其实就是一个 tcp 的 rpc 服务，端口为：6123
                 commonRpcService = AkkaRpcServiceUtils
                    .createRemoteRpcService(configuration, configuration.getString(JobManagerOptions.ADDRESS), getRPCPortRange(configuration),
                            configuration.getString(JobManagerOptions.BIND_HOST), configuration.getOptional(JobManagerOptions.RPC_BIND_PORT));
                      #生成一个构建器
                      final AkkaRpcServiceBuilder akkaRpcServiceBuilder = AkkaRpcServiceUtils.remoteServiceBuilder(configuration, externalAddress, externalPortRange);
                      #绑定地址
                      akkaRpcServiceBuilder.withBindAddress(bindAddress);
                      #绑定端口
                      bindPort.ifPresent(akkaRpcServiceBuilder::withBindPort);
                      return akkaRpcServiceBuilder.createAndStart();
                          #初始化ActorSystem
                          final ActorSystem actorSystem;
                          #返回ActorSystem
                           return new AkkaRpcService(actorSystem, AkkaRpcServiceConfiguration.fromConfiguration(configuration));
                           
                           
                 #2.启动一个 JMXService 用于客户端链接 JobManager JVM 进行监控           
                 JMXService.startInstance(configuration.getString(JMXServerOptions.JMX_SERVER_PORT));
                      # jmxServer ：  9123
                      # while(true){
                      #    try{
                      #       正经业务代码
                      #    break;
                      #    }catch(){
                      #     异常处理
                      #    }
                      #  }
                      jmxServer = startJMXServerWithPortRanges(ports);
                          JMXServer server = new JMXServer();
                            internalStart(port);
                            
                            
                            
                 #3.启动一个线程池
                 #初始化一个负责 IO 的线程池, Flink 大量使用了 异步编程
                 # 这个线程池的线程的数量，默认是：cpu core 个数 * 4
                 #如果你当前节点有 32 个 cpu ,那么当前这个 ioExecutor 启动的线程的数量为：128
                 #因为整个Flink 集群很多的地方的代码都是异步编程
                 ioExecutor = Executors.newFixedThreadPool(ClusterEntrypointUtils.getPoolSize(configuration), new ExecutorThreadFactory("cluster-io"));
                 
                 
                 #4.初始化 HA 高可用服务
                 #一般都搭建 基于 zk 的 HA 服务： ZooKeeperHaServices
                 haServices = createHaServices(configuration, ioExecutor);
                    #返回
                    return HighAvailabilityServicesUtils
                    .createHighAvailabilityServices(configuration, executor, HighAvailabilityServicesUtils.AddressResolution.NO_ADDRESS_RESOLUTION);
                        #在 fink-conf.yaml 配置文件中，我们会去配置： high-availability = zookeeper
                        HighAvailabilityMode highAvailabilityMode = HighAvailabilityMode.fromConfig(configuration);
                        #如果不是 HA 集群，则使用 StandaloneHaServices
                        case NONE:
                        return new StandaloneHaServices(resourceManagerRpcUrl, dispatcherRpcUrl, webMonitorAddress);
                        #得到一个 ZooKeeperHaServices ，内部封装了一个 ZK 客户端
                        #所谓的选举 和 监听，其实都是基于这个 HASservice 来实现的!
                        #在这个 ZooKeeperHaServices 的内部，有一个成员变量： ZooKeeper客户端（CuratorFramework： Curator实现）
                        case ZOOKEEPER:
                        return new ZooKeeperHaServices(ZooKeeperUtils.startCuratorFramework(configuration), executor, configuration, blobStoreService);
                        
                        
                 #5.初始化 BlobServer 服务端
                 #主要管理一些大文件的上传等，比如用户作业的 jar 包、TaskManager 上传 log 文件等
                 #BlobService（C/S架构： BlobServer（BIO服务端）  BlobClient（BIO客户端））
                 blobServer = new BlobServer(configuration, haServices.createBlobStore());
                    #初始化存储路径
                    this.storageDir = BlobUtils.initLocalStorageDirectory(config);
                    #配置最大连接数
                    final int maxConnections = config.getInteger(BlobServerOptions.FETCH_CONCURRENT);
                    #backlog 的最大链接数，默认 1000
                    int backlog = config.getInteger(BlobServerOptions.FETCH_BACKLOG);
                    #启动一个定时任务，执行 TransientBlobCleanupTask
                    #一个小时执行一次 TransientBlobCleanupTask 任务
                    this.cleanupInterval = config.getLong(BlobServerOptions.CLEANUP_INTERVAL) * 1000;
                    this.cleanupTimer.schedule(new TransientBlobCleanupTask(blobExpiryTimes, readWriteLock.writeLock(), storageDir, LOG)
                    #获取一个用于创建 ServerSocket 的 Factory
                    final ServerSocketFactory socketFactory;
                    #在 BlobServer 的内部，启动了一个 BIO 的服务端 ServerSocket
                    this.serverSocket = NetUtils
                      .createSocketFromPorts(ports, (port) -> socketFactory.createServerSocket(port, finalBacklog, InetAddress.getByName(bindHost)));
                 #启动
                 blobServer.start();
                    #BlobServer中的run方法
                    #BlobServer 每接收到一个客户端的链接，就使用一个 线程来专门提供服务
                    BlobServerConnection conn = new BlobServerConnection(serverSocket.accept(), this);
                    #保证最多活跃连接数，不超过 maxConnections，默认 50
                    synchronized(activeConnections) {
                        while(activeConnections.size() >= maxConnections) {
                            activeConnections.wait(2000);
                        }
                        activeConnections.add(conn);
                    }
                    #BlobServerConnection 是一个线程，线程启动
                    conn.start();
            
            
                 #6.初始化心跳服务 具体实现类是：HeartBeatImpl 
                 #后续的运行的一些心跳服务 都是基于这个 基础心跳服务来构建的
                 heartbeatServices = createHeartbeatServices(configuration);
                    return HeartbeatServices.fromConfiguration(configuration);
                      #ResourceManager 和 TaskExecutor
                      #ResourceManager 和 JobMaster
                      #TaskExecutor 和 JobMaster
                      #10s
                      long heartbeatInterval = configuration.getLong(HeartbeatManagerOptions.HEARTBEAT_INTERVAL);
                      #50s
                      long heartbeatTimeout = configuration.getLong(HeartbeatManagerOptions.HEARTBEAT_TIMEOUT);
                      #心跳间隔时间，默认 10s
                      protected final long heartbeatInterval;
                      #心跳超时时间，默认 50s
                      protected final long heartbeatTimeout;
                      
                      
                 #7.metrics（性能监控） 相关的服务
                 metricRegistry = createMetricRegistry(configuration, pluginManager);     
                      
                 
                 #8.ArchivedExecutionGraphStore: 存储 ExecutionGraph 的服务， 默认有两种实现
                 #  1、MemoryArchivedExecutionGraphStore 主要是在内存中缓存
                 #  2、FileArchivedExecutionGraphStore 会持久化到文件系统，也会在内存中缓存
                 #默认实现是基于 File 的： FileArchivedExecutionGraphStore
                 #1、per-job 模式在内存中
                 #2、session 模式在磁盘中
                 #StreamGraph  JobGraph  ExecutionGraph  物理执行图
                 archivedExecutionGraphStore = createSerializableExecutionGraphStore(configuration, commonRpcService.getScheduledExecutor());
                      #基于 File 的 ExecutionGraphStore
                      return new FileArchivedExecutionGraphStore(tmpDir, expirationTime, maximumCapacity, maximumCacheSizeBytes, scheduledExecutor, Ticker.systemTicker());
                 #Flink Graph 有四层：
                 #1.StreamGraph
                 #2.JobGraph
                 #前面这两个东西，都是客户端创建的，所以其实，client提交job给server，提交的就是 JobGraph
                 #3、ExecutionGraph
                 #4、物理执行图
                 #后面这两个东西，都是在服务端创建的。
                 
                 
            #JobManager默认端口：6123
            configuration.setString(JobManagerOptions.ADDRESS, commonRpcService.getAddress());
            configuration.setInteger(JobManagerOptions.PORT, commonRpcService.getPort());
            
            
            #以Flink Standalone为例子
            #初始化一个 DefaultDispatcherResourceManagerComponentFactory 工厂实例化
            #内部初始化了四大工厂实力
            #1、Dispatcher = DefaultDispatcherRunnerFactory，生产 DefaultDispatcherRunner， 具体实现是： DispatcherRunnerLeaderElectionLifecycleMana
            #2、ResourceManager = StandaloneResourceManagerFactory，生产 StandaloneResourceManager
            #3、WebMonitorEndpoint = SessionRestEndpointFactory，生产 DispatcherRestEndpoint
            final DispatcherResourceManagerComponentFactory dispatcherResourceManagerComponentFactory =
                    createDispatcherResourceManagerComponentFactory(
                    configuration); 
                # 第一个工厂  StandaloneResourceManagerFactory
                return DefaultDispatcherResourceManagerComponentFactory.createSessionComponentFactory(
                StandaloneResourceManagerFactory.getInstance())
                     #第二个工厂：DefaultDispatcherRunnerFactory
                     #用来创建Dispatcher
                     DefaultDispatcherRunnerFactory.createSessionRunner(SessionDispatcherFactory.INSTANCE)
                     #第一个工厂：StandaloneResourceManagerFactory
                     #用来创建 ResourceManager
                     resourceManagerFactory
                     #第三个工厂：SessionRestEndpointFactory
                     #用来创建 WebMonitorEndpoint
                     SessionRestEndpointFactory.INSTANCE
                     
                     
            #创建JobManager的三大核心角色实例 
            #1、webMonitorEndpoint：用于接收客户端发送的执行任务的rest请求 
            #2、resourceManager：负责资源的分配和记帐
            #3、dispatcher：负责用于接收作业提交，持久化它们，生成要执行的作业管理器任务，并在主任务失败时恢复它们
            clusterComponent = dispatcherResourceManagerComponentFactory
                    .create(configuration, ioExecutor, commonRpcService, haServices, blobServer, heartbeatServices, metricRegistry, archivedExecutionGraphStore,
                            new RpcMetricQueryServiceRetriever(metricRegistry.getMetricQueryServiceRpcService()), this);
                            #最重要的就是这三个组件
                            WebMonitorEndpoint<?> webMonitorEndpoint = null;
                            ResourceManager<?> resourceManager = null;
                            DispatcherRunner dispatcherRunner = null;
                            #DefaultLeaderRetrievalService 监听 Dispatcher 地址
                            dispatcherLeaderRetrievalService = highAvailabilityServices.getDispatcherLeaderRetriever();
                            #DefaultLeaderRetrievalService 监听 ResourceManager 地址
                            resourceManagerRetrievalService = highAvailabilityServices.getResourceManagerLeaderRetriever();
                            #Dispatcher 的 GatewayRetriever
                            final LeaderGatewayRetriever<DispatcherGateway> dispatcherGatewayRetriever = new RpcGatewayRetriever<>(rpcService, DispatcherGateway.class,
                            DispatcherId::fromUuid, new ExponentialBackoffRetryStrategy(12, Duration.ofMillis(10), Duration.ofMillis(50)));
                            #ResourceManager 的 GatewayRetriever
                            final LeaderGatewayRetriever<ResourceManagerGateway> resourceManagerGatewayRetriever = new RpcGatewayRetriever<>(rpcService,
                            ResourceManagerGateway.class, ResourceManagerId::fromUuid,
                            new ExponentialBackoffRetryStrategy(12, Duration.ofMillis(10), Duration.ofMillis(50)));
                            #创建线程池，用于执行 WebMonitorEndpoint 所接收到的 client 发送过来的请求
                            final ScheduledExecutorService executor = WebMonitorEndpoint.createExecutorService(
                            configuration.getInteger(RestOptions.SERVER_NUM_THREADS),
                            configuration.getInteger(RestOptions.SERVER_THREAD_PRIORITY),
                            "DispatcherRestEndpoint"
                            #初始化 MetricFetcher，间隔时间是：10s
                            final long updateInterval = configuration.getLong(MetricOptions.METRIC_FETCHER_UPDATE_INTERVAL);
                            
            );
```