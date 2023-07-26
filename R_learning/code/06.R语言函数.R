#1.定义函数
#定义一个加法函数
add_numbers <- function(x,y) {
    result <- x + y
    return(result)
}
#调用函数
sum_result <- add_numbers(3,4)
print(sum_result)

#2.自定义函数
# 定义一个函数，用于计数一个系列到平方值
new.function <- function(a) {
  for(i in 1:a) {
    b <- i^2
    print(b)
 }
}
#调用函数
new.function(6)

#3.带有参数值的函数
# 创建函数
new.function <- function(a,b,c) {
  result <- a * b + c
  print(result)
}

# 不带参数名
new.function(5,3,11)

# 带参数名
new.function(a = 11, b = 5, c = 3)


# 创建带默认参数的函数
#函数创建时也可以为参数指定默认值，如果调用的时候不传递参数就会使用默认值
new.function <- function(a = 3, b = 6) {
  result <- a * b
  print(result)
}

# 调用函数，但不传递参数，会使用默认的
new.function()

# 调用函数，传递参数
new.function(9,5)

#4.懒惰计算的函数
#懒惰计算将推迟计算工作直到系统需要这些计算的结果。如果不需要结果，将不用进行计算。
#默认情况下，R 函数对参数的计算是懒惰的，就是只有我们在计算它的时候才会调用：
f <- function(x) {
     10
}
f()

#5.内置函数
# 输出  32 到 44 到的所有数字
print(seq(32,44))
# 计算两个数的平均数
print(mean(25:82))
# 计算 41 到 68 所有数字之和
print(sum(41:68))

#sum() 计算向量或矩阵的总和
# 向量求和
x <- c(1, 2, 3, 4, 5)
total <- sum(x)
print(total) # 输出 15

# 矩阵求和
matrix <- matrix(1:9, nrow = 3)
total <- sum(matrix)
print(total) # 输出 45

#mean(): 计算向量或矩阵的平均值。
# 向量平均值
x <- c(1, 2, 3, 4, 5)
avg <- mean(x)
print(avg) # 输出 3

# 矩阵平均值
matrix <- matrix(1:9, nrow = 3)
avg <- mean(matrix)
print(avg) # 输出 5

#paste(): 将多个字符串连接成一个字符串。
x <- "Hello"
y <- "World"
result <- paste(x, y)
print(result) # 输出 "Hello World"

#length(): 返回向量的长度或对象的元素个数。
x <- c(1, 2, 3, 4, 5)
length_x <- length(x)
print(length_x) # 输出 5

matrix <- matrix(1:9, nrow = 3)
length_matrix <- length(matrix)
print(length_matrix) # 输出 9

#str(): 显示对象的结构和内容摘要。
x <- c(1, 2, 3, 4, 5)
str(x)
# 输出：
# num [1:5] 1 2 3 4 5

matrix <- matrix(1:9, nrow = 3)
str(matrix)
# 输出：
#  int [1:3, 1:3] 1 2 3 4 5 6 7 8 9