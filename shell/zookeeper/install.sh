#!/bin/bash
declare step=0
#为myid计数
MY_ID=0
#自动化部署zookeeper
IP_ADDR="192.168.233.16 192.168.233.17 192.168.233.18"
CUR_PATH="pwd"
#包方的路径
PACKAGE_PATH="/opt/mycluster"
#创建文件夹
mkdirDIR="mkdir -p $PACKAGE_PATH"
#zookeeper的的版本
ZK_VERSION=apache-zookeeper-3.5.7-bin
#修改zoo.cfg中数据存储目录
EDIT_DATA="sed 's/dataDir=\/tmp\/zookeeper/dataDir=\/opt\/mycluster${step}\/apache-zookeeper-3.5.7-bin\/data/g' ${PACKAGE_PATH}/${ZK_VERSION}/conf/zoo_sample.cfg"
#登录主机函数的封装
function install_zookeeper {
    #echo $1
    #先创建目录
    ssh -o StrictHostKeyChecking=no root@$@
    #echo $?
}
for host in $IP_ADDR
do
  #echo $host
  #为myid计数
  MY_ID=`expr $MY_ID + 1`
  echo $MY_ID
  #登录每个节点创建目录
  install_zookeeper $host $mkdirDIR
  #给每个节点分发软件包
  scp $PACKAGE_PATH/$ZK_VERSION.tar.gz root@$host:$PACKAGE_PATH
  #解压zk 进入目录 修改配置文件
  EDIT_CONF="${EDIT_DATA} > zoo.cfg"
  TAR_ZK="cd ${PACKAGE_PATH} && tar -zxvf ${ZK_VERSION}.tar.gz >> zookeeper.log && cd ${PACKAGE_PATH}/${ZK_VERSION}/conf/ && ${EDIT_CONF}"
  #修改zoo.cfg中的文件的配置
  install_zookeeper $host  $TAR_ZK
done
