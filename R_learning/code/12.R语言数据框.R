# data.frame(…, row.names = NULL, check.rows = FALSE,
#            check.names = TRUE, fix.empty.names = TRUE,
#            stringsAsFactors = default.stringsAsFactors())
# …: 列向量，可以是任何类型（字符型、数值型、逻辑型），一般以 tag = value 的形式表示，也可以是 value。
# row.names: 行名，默认为 NULL，可以设置为单个数字、字符串或字符串和数字的向量。
# check.rows: 检测行的名称和长度是否一致。
# check.names: 检测数据框的变量名是否合法。
# fix.empty.names: 设置未命名的参数是否自动设置名字。
# stringsAsFactors: 布尔值，字符是否转换为因子，factory-fresh 的默认值是 TRUE，可以通过设置选项（stringsAsFactors=FALSE）来修改。

table = data.frame(
   姓名 = c("张三", "李四"),
   工号 = c("001","002"),
   月薪 = c(1000, 2000)
)
print(table) # 查看 table 数据

#数据框的数据结构可以通过 str() 函数来展示
table = data.frame(
   姓名 = c("张三", "李四"),
   工号 = c("001","002"),
   月薪 = c(1000, 2000)
)
# 获取数据结构
str(table)

#summary() 可以显示数据框的概要信息
table = data.frame(
   姓名 = c("张三", "李四"),
   工号 = c("001","002"),
   月薪 = c(1000, 2000)
)
# 显示概要
print(summary(table))

#提取指定的列
table = data.frame(
   姓名 = c("张三", "李四"),
   工号 = c("001","002"),
   月薪 = c(1000, 2000)
)
# 提取指定的列
result <- data.frame(table$姓名,table$月薪)
print(result)

#显示前面两行
table = data.frame(
   姓名 = c("张三", "李四","王五"),
   工号 = c("001","002","003"),
   月薪 = c(1000, 2000,3000)
)
print(table)
# 提取前面两行
print("---输出前面两行----")
result <- table[1:2,]
print(result)

#读取第 2 、3 行的第 1 、2 列数据
table = data.frame(
   姓名 = c("张三", "李四","王五"),
   工号 = c("001","002","003"),
   月薪 = c(1000, 2000,3000)
)
# 读取第 2 、3 行的第 1 、2 列数据：
result <- table[c(2,3),c(1,2)]
print(result)

#扩展数据框
table = data.frame(
   姓名 = c("张三", "李四","王五"),
   工号 = c("001","002","003"),
   月薪 = c(1000, 2000,3000)
)
# 添加部门列
table$部门 <- c("运营","技术","编辑")

print(table)

#使用 cbind() 函数将多个向量合成一个数据框
# 创建向量
sites <- c("Google","Runoob","Taobao")
likes <- c(222,111,123)
url <- c("www.google.com","www.runoob.com","www.taobao.com")

# 将向量组合成数据框
addresses <- cbind(sites,likes,url)

# 查看数据框
print(addresses)

#两个数据框进行合并可以使用 rbind() 函数
table = data.frame(
   姓名 = c("张三", "李四","王五"),
   工号 = c("001","002","003"),
   月薪 = c(1000, 2000,3000)
)
newtable = data.frame(
   姓名 = c("小明", "小白"),
   工号 = c("101","102"),
   月薪 = c(5000, 7000)
)
# 合并两个数据框
result <- rbind(table,newtable)
print(result)