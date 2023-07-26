#1.赋值
# 使用= 和 <-都可以
a <- 123
b <- 456
print(a + b)
#这样也是可以的
c = 20
b = 40
print(c + b)


#2.运算符的优先级
#优先级    符号     含义
#1        （）     括号
#2          ^      乘方预算
#3         %%      整除求余
#         %/%      整除
#4        *        乘法
#         /        除法
#5        +        加法
#         -        减法 
1 + 2 * 3 
(1 + 2) * 3
3 / 4
3.4 - 1.2
1 - 4 * 0.5 ^ 3
8 / 3 %% 2
8 / 4 %% 2
3 %% 2^2
10 / 3 %% 2

#3.关系运算符 返回的是布尔值 TRUE FALSE
v <- c(2,4,6,9)
t <- c(1,4,7,9)
print(v>t)
print(v<t)
print(v==t)
print(v!=t)
print(v>=t)
print(v<=t)

#4.逻辑运算符
#& 元素逻辑余运算符,将第一个向量的每个元素与第二个向量的相对应元素进行组合,如果两个元素都为TRUE,则结果为TURE,否则为FALSE
#| 元素逻辑或运算符,将第一个向量的每个元素与第二个向量的相对应元素进行组合，如果两个元素中有一个为 TRUE,则结果为 TRUE,如果都为 FALSE,则返回 FALSE
#! 逻辑非运算符，返回向量每个元素相反的逻辑值，如果元素为 TRUE 则返回 FALSE,如果元素为 FALSE 则返回 TRUE
#&& 逻辑与运算符，只对两个向量对第一个元素进行判断，如果两个元素都为 TRUE,则结果为 TRUE,否则为 FALSE
#|| 逻辑或运算符，只对两个向量对第一个元素进行判断，如果两个元素中有一个为 TRUE,则结果为 TRUE,如果都为 FALSE,则返回 FALSE
v <- c(3,1,TRUE,2 + 3i)
t <- c(4,1,FALSE,2 + 3i)
print(v&t)
print(v|t)
print(!v)

# &&、||只对第一个元素进行比较
# v <- c(3,0,TRUE,2+2i)
# t <- c(1,3,TRUE,2+3i)
# print(v&&t)

# v <- c(0,0,TRUE,2+2i)
# t <- c(0,3,TRUE,2+3i)
# print(v||t)


#5.赋值运算符
# <-  =  <<- 向左赋值
# -> ->> 向右赋值
#向左赋值
v1 <- c(3,1,TRUE,"runoob")
v2 <<- c(3,1,TRUE,"runoob")  
v3 =  c(3,1,TRUE,"runoob")
print(v1)
print(v2)
print(v3)


# 向右赋值
c(3,1,TRUE,"runoob") -> v1
c(3,1,TRUE,"runoob") ->> v2
print(v1)
print(v2)

#6.其他运算符
# ： 冒号运算符，用于创建一系列数字的向量
# %in% 用于判断元素是否在向量里，返回布尔值，有的话返回 TRUE，没有返回 FALSE
# %*% 用于矩阵与它转置的矩阵相乘
#1 到 10的向量
v <- 1:10
print(v)
#判断数字是否在向量v中
v1 <- 3
v2 <- 15
print(v1 %in% v)
print(v2 %in% v)

# 矩阵与它转置的矩阵相乘
M = matrix( c(2,6,5,1,10,4), nrow = 2,ncol = 3,byrow = TRUE)
t = M %*% t(M)
print(t)


#7.数学函数
#sqrt(n)  n的平方根
#exp(n)   自然常数e的n次方
#log(m,n) m的对数函数，返回n的几次方等于m
#log10(m) 相等于log(m,10)
sqrt(4)
exp(1)
exp(2)
log(2,4)
log10(10000)

#8.取整函数
# round	(n)	对 n 四舍五入取整
#	    (n, m)	对 n 保留 m 位小数四舍五入
# ceiling	(n)	对 n 向上取整
# floor	 (n)	对 n 向下取整
round(1.5)
round(2.5)
round(3.5)
round(4.5)
#R 中的 round 函数有些情况下可能会"舍掉五"。 当取整位是偶数的时候，五也会被舍去

#9.R的三角函数是弧度制
sin(pi/6)
cos(pi/4)
tan(pi/3)
#反三角函数
asin(0.5)
acos(0.7071068)
atan(1.732051)

#d - 概率密度函数
#p - 概率密度积分函数（从无限小到 x 的积分）
#q - 分位数函数
#r - 随机数函数（常用于概率仿真）
dnorm(0)
pnorm(0)
qnorm(0.95)
rnorm(3, 5, 2) # 产生 3 个平均值为 5，标准差为 2 的正态随机数