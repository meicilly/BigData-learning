### R语言基础
新语言从hello world开始
```R
myString <- "Hello, World!"
print ( myString )
```
以上实例将字符串 "Hello, World!" 赋值给 myString 变量，然后使用 print() 函数输出。

*注意*：R 语言赋值使用的是左箭头 <- 符号，不过一些新版本也支持等号 =。
#### 变量
R语言的有效的变量名称由字母，数字以及点号.或下划线_组成。
#### 变量赋值
```R
# 使用等号 = 号赋值
var.1 = c(0,1,2,3)
print(var.1)
[1] 0 1 2 3

# 使用左箭头 <- 赋值
var.2 <- c("learn","R")  
print(var.2)
[1] "learn" "R"


# 使用右箭头 -> 赋值
c(TRUE,1) -> var.3
print(var.3)
[1] 1 1         
```
查看已定义的变量可以使用ls()函数：
```R
 print(ls())
```
删除变量可以使用rm()函数：
```R
rm(var.3)
print(ls())
```