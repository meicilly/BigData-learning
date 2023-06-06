#!/bin/bash
declare step=0
#为myid计数
MY_ID=0
#自动化部署zookeeper
#IP_ADDR="192.168.233.16 192.168.233.17 192.168.233.18"
IP_ADDR="192.168.88.11 192.168.88.12 192.168.88.13"
#包方的路径
PACKAGE_PATH="/opt/mycluster"
#${变量/查找/替换值}  一个'/'表示替换第一个'//'表示替换所有，当查找出中出现了："/"需要转移成"\/"
REP_PATH=${PACKAGE_PATH//\//\\/}
#创建文件夹
mkdirDIR="mkdir -p ${PACKAGE_PATH}"
#zookeeper的的版本
ZK_VERSION=apache-zookeeper-3.6.3-bin
#修改zoo.cfg中数据存储目录
EDIT_DATA="sed 's/dataDir=\/tmp\/zookeeper/dataDir=${REP_PATH}\/${ZK_VERSION}\/data/g' ${PACKAGE_PATH}/${ZK_VERSION}/conf/zoo_sample.cfg"
#当前的目录
CUR_PATH="${PACKAGE_PATH}/${ZK_VERSION}"
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
  #echo $MY_ID
  #登录每个节点创建目录
  install_zookeeper $host $mkdirDIR
  #给每个节点分发软件包
  scp $PACKAGE_PATH/$ZK_VERSION.tar.gz root@$host:$PACKAGE_PATH
  #解压zk 进入目录 修改zookeeper数据存储的配置
  EDIT_CONF="${EDIT_DATA} > zoo.cfg"
  TAR_ZK="cd ${PACKAGE_PATH} && tar -zxvf ${ZK_VERSION}.tar.gz >> zookeeper.log && cd ${PACKAGE_PATH}/${ZK_VERSION}/conf/ && ${EDIT_CONF}"
  #判断zookeeper解压路径下是佛data文件夹
  #IS_ZK_DATA="&& ls ${CUR_PATH}/data && if [$? -eq 2];then rm -rf ${CUR_PATH/data} fi"
  #进入目录创建myid文件 追加值进去
  TOUCH_MY_ID="touch ${CUR_PATH}/data/myid"
  ECHO_ID="echo ${MY_ID} >> ${CUR_PATH}/data/myid"
  #创建数据目录 进入目录创建myid
  MK_ZK_DATA="&& mkdir  ${CUR_PATH}/data/ && ${TOUCH_MY_ID} && ${ECHO_ID}"
  #修改zoo.cfg中的文件的配置
  install_zookeeper $host  $TAR_ZK $IS_ZK_DATA #$MK_ZK_DATA
done
