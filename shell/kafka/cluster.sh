#!/bin/bash
#初始化变量
HOST_LIST="192.168.233.16 192.168.233.17 192.168.233.18"
CMD_NUM=0
if [ -e ./kafka_install.log ];then 
      rm -f ./kafka_install.log
fi
# 指令结果返回1追加到output.log 2也是
exec 1 >> ./kafka_install.log 2>&1
# 多主机执行指令函数封装
function remote_execute
{
  for host in $HOST_LIST;
  do
     CMD_NUM=`expr $CMD_NUM + 1`
     ssh -o StrictHostKeyChecking=no root@$host $@
     #前面命令执行成功就返回的0
     if [ $? -eq 0 ];then
        echo "$CMD_NUM Congratulation. Command < $@ > excute success" 
     else
        echo "$CMD_NUM Sorry.Command < $@ > excute failed" 
     fi
  done
}

#第一步  关闭防火墙和selinux
#remote_execute "systemctl stop firewalld"
#remote_execute "systemctl disable firewalld"
#sed -i 's/SELINUX=enforcing/SELINUX=disabled/g' /etc/sysconfig/selinux
#第二步  安装配置JDk
#第三步  安装配置zookeeper,并启动服务
#第四步  安装配置kafka,并启动服务

