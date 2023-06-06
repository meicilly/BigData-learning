#!/bin/bash
#zookeeper安装的路径
ZK_PATH=$1
echo $1
#zookeeper的版本
#判断文件夹是否已经存在
if [ ! -d ${ZK_PATH} ];then
  echo "document is not exist and will be created"
  mkdir -p ${ZK_PATH}
fi

