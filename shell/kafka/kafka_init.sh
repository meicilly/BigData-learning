#kafka安装的路径
KAFKA_PATH=$1
#判断文件夹是否已经存在
if [ ! -d ${KAFKA_PATH} ];then
  echo "document is not exist and will be created"
  mkdir -p ${KAFKA_PATH}
fi

