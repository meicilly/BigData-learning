#!/bin/bash
#规划集群的ip地址
IP_ADDR="192.168.233.16 192.168.233.17 192.168.233.18"
#当前目录 必须有这个目录
CUR_PATH=/opt/meicilly
#kafka的安装的路径
KAFKA_PATH=${CUR_PATH}/software
#KAFKA的版本
KAFKA_VER=kafka_2.12-3.0.0
KAFKA_PAK=${KAFKA_VER}.tgz
function install_component() {
    echo "login in $1 host"
    ssh -o StrictHostKeyChecking=no root@$@
}
function scp_package_and_file() {
    scp $1 root@$2:$3
}
for host in ${IP_ADDR}
do
  #把初始化文件拷贝到 各个节点
  scp_package_and_file ${CUR_PATH}/kafka_init.sh $host ${CUR_PATH}
  #执行脚本
  install_component $host "sh ${CUR_PATH}/kafka_init.sh ${KAFKA_PATH}"
  #分发软件包到各个节点
  scp_package_and_file ${CUR_PATH}/${KAFKA_VER}.tgz $host ${KAFKA_PATH}
  #分发kafka_install.sh 文件到各个节点
  scp_package_and_file ${CUR_PATH}/kafka_install.sh $host ${CUR_PATH}
  #执行kafka_install.sh脚本
  install_component $host "sh ${CUR_PATH}/kafka_install.sh ${KAFKA_PAK} ${KAFKA_PATH}"
done

