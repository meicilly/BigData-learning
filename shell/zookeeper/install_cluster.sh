#!/bin/bash
#需要部署节点的ip地址
IP_ADDR="192.168.88.11 192.168.88.12 192.168.88.13"
#当前目录 这个目录必须保证有
CUR_PATH=/opt/meicilly
#zookeeper安装的路径
ZK_PATH=${CUR_PATH}/software
#zookeeper的版本
ZK_VERSION=apache-zookeeper-3.6.3-bin
#ZK安装包的存储的路径
ZK_PAK=${ZK_PATH}/${ZK_VERSION}.tar.gz
#myid里的值
MY_ID=0
function install_component() {
    echo "login in $1 host"
    ssh -o StrictHostKeyChecking=no root@$@
}
function scp_pak_and_file() {
    echo "is copying $1 to $2"
    scp $1 root@$2:$3
}
for host in $IP_ADDR
do
  #myid的值
  MY_ID=`expr $MY_ID + 1`
  #拷贝文件zk_init初始化文件 并执行
  scp_pak_and_file ${CUR_PATH}/zk_init.sh $host $CUR_PATH
  #执行文件
  install_component $host "sh ${CUR_PATH}/zk_init.sh ${ZK_PATH}"
  #拷贝软件包
  scp_pak_and_file ${CUR_PATH}/${ZK_VERSION}.tar.gz $host $ZK_PATH
  #拷贝zk_install安装文件
  scp_pak_and_file ${CUR_PATH}/zk_install.sh $host ${CUR_PATH}
  #执行安装zookeeper脚本
  install_component $host "sh ${CUR_PATH}/zk_install.sh ${ZK_PATH} ${ZK_PAK} ${ZK_VERSION} ${MY_ID} $IP_ADDR"
done