#!/bin/bash
KAFKA_PAK=$1
KAFKA_PATH=$2
echo $1
echo $2
#解压kafka
cd ${KAFKA_PATH}
tar -zxvf ${KAFKA_PAK}
