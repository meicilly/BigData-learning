# Rscript 01.R语言基础语法.R
#sink("F:\\大数据资料\\learning\\BigData-learning\\R_learning\\code\\test.txt")
#1.hello world例子
myString <- "Hello, World!"
print ( myString )

#2.变量
#R 语言的有效的变量名称由字母，数字以及点号 . 或下划线 _ 组成。
#变量名称已字母或点开头


#3.变量赋值
#在最新版的R语言的赋值可以使用左箭头 <- 、等号 = 、右箭头 -> 赋值
var.1  = c(0,1,2,3)
print(var.1)
#使用左箭头 <- 赋值
var.2 <- c("learn","R")
print(var.2)
# 使用右箭头 -> 赋值
c(TRUE,1) -> var.3
print(var.3)

#4.查看已定义的变量可以使用ls()函数
print(ls())

#5.删除变量可以使用rm()函数
rm(var.3)
print(ls())

#6.输入输出函数 print
print("RUNOOB")
print(123)
print(3e2) #不用特别关注这个

#7.cat()函数
#如果需要输出结果的拼接 
cat(2,"加",2,"等于",4,'\n')#\n是换行符
#输入结果到文件中  如果想要换行就加上\n
cat("meicilly\n",file="F:\\大数据资料\\learning\\BigData-learning\\R_learning\\code\\test.txt")#windows环境中
#mac linux操作系统中 cat("meicilly",file="/Users/runoob/runoob-test/r_test.txt")
#如果是想追加文件
cat("meicilly2",file="F:\\大数据资料\\learning\\BigData-learning\\R_learning\\code\\test.txt",append=TRUE)

#8.sink()函数可以把控制台输出的文字直接输出到文件中去
#sink("F:\\大数据资料\\learning\\BigData-learning\\R_learning\\code\\test.txt")
#如果想取消输出到文件可以调用无参的sink
sink("r_test.txt", split=TRUE)  # 控制台同样输出
for (i in 1:5)
    print(i)
sink()   # 取消输出到文件

sink("r_test.txt", append=TRUE) # 控制台不输出，追加写入文件
print("RUNOOB")

#9.从文件读入文字
readLines("r_test.txt")

#10.工作目录
# 当前工作目录
print(getwd())

# 设置当前工作目录
setwd("/Users/runoob/runoob-test2")

# 查看当前工作目录
print(getwd())