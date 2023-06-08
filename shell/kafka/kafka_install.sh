#!/bin/bash
KAFKA_PAK=$1
CUR_PATH=$2
KAFKA_PATH=$3
BROKER_ID=$4
function sed_param(){
    mv ${CUR_PATH}/${KAFKA_PATH}/config/server.properties ${CUR_PATH}/${KAFKA_PATH}/config/server.properties.bak
    sed "s/${1}/${2}/g" ${CUR_PATH}/${KAFKA_PATH}/config/server.properties.bak > ${CUR_PATH}/${KAFKA_PATH}/config/server.properties
}
echo $4
#解压kafka
cd ${CUR_PATH}
tar -zxvf ${KAFKA_PAK} > kafka.log
#进入kafka的目录 创建数据存储目录
cd ${KAFKA_PATH}
if [ ! -d  data ];then
  mkdir data
fi
#cat ${CUR_PATH}/${KAFKA_PATH}/config/server.properties
#修改broker.id
EDIT_BROKER_ID=broker.id=0
EDITED_BROKER_ID=broker.id=${BROKER_ID}
sed_param  ${EDIT_BROKER_ID} ${EDITED_BROKER_ID}
cat ${CUR_PATH}/${KAFKA_PATH}/config/server.properties
#修改数据目录
REP_PATH=${CUR_PATH//\//\\/}
EDIT_LOG_DIR="\/tmp\/kafka-logs"
EDITED_LOG_DIR=${REP_PATH}\\/${KAFKA_PATH}\\/data
#echo ${EDITED_LOG_DIR}
#echo ${EDIT_LOG_DIR}
sed_param ${EDIT_LOG_DIR} ${EDITED_LOG_DIR}


