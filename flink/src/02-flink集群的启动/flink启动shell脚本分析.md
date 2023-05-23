#### Flink集群启动脚本分析
Flink集群的启动脚本在：flink-dist子项目中，位于flink-bin下的bin目录：启动脚本为：
```
start-clussrer.sh
```
该脚本会先调用config.sh来获取masters和workers:
```
1、masters 的信息，是从 conf/masters 配置文件中获取的
2、workers 的信息，是从 conf/workers 配置文件中获取的
```
然后分别：
```shell
通过jobmanager.sh来启动JobManager
  if [ ${MASTERS_ALL_LOCALHOST} = true ] ; then
            "${FLINK_BIN_DIR}"/jobmanager.sh start "${master}" "${webuiport}"
        else
            ssh -n $FLINK_SSH_OPTS $master -- "nohup /bin/bash -l \"${FLINK_BIN_DIR}/jobmanager.sh\" start ${master} ${webuiport} &"
  fi
```
```shell
通过taskmanager.sh来启动TaskManager
# Start TaskManager instance(s)
TMWorkers start
```
它们的内部，都通过flink-daemon.sh脚本来启动相应的JVM进程，分析flink-daemon.sh脚本发现；
```shell
#1、JobManager 的启动代号：standalonesession，实现类是：StandaloneSessionClusterEntrypoint
#jobmanager中
ENTRYPOINT=standalonesession
"${FLINK_BIN_DIR}"/flink-daemon.sh $STARTSTOP $ENTRYPOINT "${args[@]}"
#flink-deamon.sh中
(standalonesession)
        CLASS_TO_RUN=org.apache.flink.runtime.entrypoint.StandaloneSessionClusterEntrypoint
    ;;
#2、TaskManager 的启动代号：taskexecutor，实现类是：TaskManagerRunner
#taskmanager.sh中
ENTRYPOINT=taskexecutor
"${FLINK_BIN_DIR}"/flink-daemon.sh $STARTSTOP $ENTRYPOINT "${ARGS[@]}"
#flink-daemon.sh
   (taskexecutor)
        CLASS_TO_RUN=org.apache.flink.runtime.taskexecutor.TaskManagerRunner
    ;;
```
最终发现通过java命令来启动对应的JVM进程
其实发现，HDFS集群启动的shell编写方式也是一样的，Spark的shell脚本编写方式也差不多。这里以HDFS举例：
```shell
# 第一步：执行下面脚本启动集群
start-all.sh / start-dfs.sh
# 第二步：启动集群启动脚本的时候，内部其实会根据集群模式来启动 namenode，datanode，等
# 不管启动什么角色，最终都是通过 hadoop-daemon.sh 来启动
hadoop-daemon.sh start namenode/datanode/zkfc/journalnode
# 第三步：在 hadoop-daemon.sh 脚本的内部的最后，是通过 java 命令来启动
java org.apache.hadoop.server.namenode.NameNode
```


