#!/bin/bash
ZK_PATH=$1
ZK_PAK=$2
ZK_VERSION=$3
MY_ID=$4
SERVER_ID=0

REP_PATH=${ZK_PATH//\//\\/}
echo ${REP_PATH}
#首先解压软件包
cd ${ZK_PATH}
tar -zxvf ${ZK_PAK} > ${ZK_PATH}/zookeeper.log
#查看是否有数据目录 没有就创建
if [ ! -d ${ZK_PATH}/${ZK_VERSION}/data ];then
  echo "document is not exist and will be created"
  mkdir -p ${ZK_PATH}/${ZK_VERSION}/data
fi
#替换zoo.cfg中的内容
sed "s/dataDir=\/tmp\/zookeeper/dataDir=${REP_PATH}\/${ZK_VERSION}\/data/g" ${ZK_PATH}/${ZK_VERSION}/conf/zoo_sample.cfg > ${ZK_PATH}/${ZK_VERSION}/conf/zoo.cfg
#myid中加入值
echo ${MY_ID} > ${ZK_PATH}/${ZK_VERSION}/data/myid
for host in $5 $6 $7
do
  SERVER_ID=`expr ${SERVER_ID} + 1`
  echo $SERVER_ID
  echo server.${SERVER_ID}=${host}:2888:3888 >> ${ZK_PATH}/${ZK_VERSION}/conf/zoo.cfg
done

