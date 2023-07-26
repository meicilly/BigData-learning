#1.可以使用单引号或双引号来创建字符串。
a <- '使用单引号'
print(a)

b <- "使用双引号"
print(b)

c <- "双引号字符串中可以包含单引号（'） "
print(c)

d <- '单引号字符串中可以包含双引号（"） '
print(d)

#2.paste() 函数用于使用指定对分隔符来对字符串进行连接，默认对分隔符为空格。
#sep ： 分隔符，默认为空格
#collapse ： 两个或者更多字符串对象根据元素对应关系拼接到一起，在字符串进行连接后，再使用 collapse 指定对连接符进行连接
a <- "Google"
b <- 'Runoob'
c <- "Taobao"

print(paste(a,b,c))

print(paste(a,b,c, sep = "-"))

print(paste(letters[1:6],1:6, sep = "", collapse = "="))
paste(letters[1:6],1:6, collapse = ".")

#format() 函数
#x ： 输入对向量
#digits ： 显示的位数
#nsmall ： 小数点右边显示的最少位数
#scientific ： 设置科学计数法
#width ： 通过开头填充空白来显示最小的宽度
#justify：设置位置，显示可以是左边、右边、中间等。

# 显示 9 位，最后一位四舍五入
result <- format(23.123456789, digits = 9)
print(result)

# 使用科学计数法显示
result <- format(c(6, 13.14521), scientific = TRUE)
print(result)

# 小数点右边最小显示 5 位，没有的以 0 补充
result <- format(23.47, nsmall = 5)
print(result)

# 将数字转为字符串
result <- format(6)
print(result)

# 宽度为 6 位，不够的在开头添加空格
result <- format(13.7, width = 6)
print(result)

# 左对齐字符串
result <- format("Runoob", width = 9, justify = "l")
print(result)

# 居中显示
result <- format("Runoob", width = 10, justify = "c")
print(result)

#nchar() 函数
result <- nchar("Google Runoob Taobao")
print(result)

#toupper() & tolower() 函数
result <- toupper("Runoob")
print(result)

# 转小写
result <- tolower("Runoob")
print(result)

#substring() 函数
#substring(x,first,last)
#x ： 向量或字符串
#first ： 开始截取的位置
#last： 结束截取的位置
# 从第 2 位截取到第 5 位
result <- substring("Runoob", 2, 5)
print(result)

#字符串替换
str <- "Hello, World!"
new_str <- gsub("World", "R", str)
# 输出： "Hello, R!"

#字符串拆分
str <- "Hello, World!"
split_str <- strsplit(str, ",")
# 输出： List of 1
#         [[1]]
#         [1] "Hello"   " World!"