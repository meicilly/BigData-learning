#读取csv文件
data <- read.csv("sites.csv", encoding="UTF-8")
print(data)
print(is.data.frame(data))  # 查看是否是数据框
print(ncol(data))  # 列数
print(nrow(data))  # 行数

# likes 最大的数据
like <- max(data$likes)
print(like)
#指定查找条件，类似 SQL where 子句一样查询数据，需要用到到函数是 subset()。
# likes 为 222 的数据
retval <- subset(data, likes == 222)
print(retval)
#多个条件使用 & 分隔符，以下实例查找 likes 大于 1 name 为 Runoob 的数据：
# likes 大于 1 name 为 Runoob 的数据
retval <- subset(data, likes > 1 & name=="Runoob")
print(retval)
# 写入新的文件
write.csv(retval,"runoob.csv")
newdata <- read.csv("runoob.csv")
print(newdata)

#可以通过参数 row.names = FALSE 来删除它
# 写入新的文件
write.csv(retval,"runoob.csv", row.names = FALSE)
newdata <- read.csv("runoob.csv")
print(newdata)