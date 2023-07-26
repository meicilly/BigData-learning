#创建列表
list_data <- list("runoob", "google", c(11,22,33), 123, 51.23, 119.1)
print(list_data)

#使用 c() 函数来创建列表，也可以使用该函数将多个对象合并为一个列表
# 创建包含数字的向量
numbers <- c(1, 2, 3, 4, 5)

# 创建包含字符的向量
characters <- c("apple", "banana", "orange")

# 合并两个数字向量
merged_vector <- c(numbers, c(6, 7, 8))

# 合并两个字符向量
merged_characters <- c(characters, c("grape", "melon"))

#使用 names() 函数给列表的元素命名
# 列表包含向量、矩阵、列表
list_data <- list(c("Google","Runoob","Taobao"), matrix(c(1,2,3,4,5,6), nrow = 2),
list("runoob",12.3))

# 给列表元素设置名字
names(list_data) <- c("Sites", "Numbers", "Lists")

# 显示列表
print(list_data)

#访问列表
# 列表包含向量、矩阵、列表
list_data <- list(c("Google","Runoob","Taobao"), matrix(c(1,2,3,4,5,6), nrow = 2),
list("runoob",12.3))

# 给列表元素设置名字
names(list_data) <- c("Sites", "Numbers", "Lists")

# 显示列表
print(list_data[1])

# 访问列表的第三个元素
print(list_data[3])

# 访问第一个向量元素
print(list_data$Numbers)

#操作列表元素
# 列表包含向量、矩阵、列表
list_data <- list(c("Google","Runoob","Taobao"), matrix(c(1,2,3,4,5,6), nrow = 2),
list("runoob",12.3))

# 给列表元素设置名字
names(list_data) <- c("Sites", "Numbers", "Lists")

# 添加元素
list_data[4] <- "新元素"
print(list_data[4])

# 删除元素
list_data[4] <- NULL

# 删除后输出为 NULL
print(list_data[4])

# 更新元素
list_data[3] <- "我替换来第三个元素"
print(list_data[3])

#使用for循环遍历
# 创建一个包含数字和字符的列表
my_list <- list(1, 2, 3, "a", "b", "c")

# 使用 for 循环遍历列表中的每个元素
for (element in my_list) {
 print(element)
}

#合并列表
# 创建两个列表
list1 <- list(1,2,3)
list2 <- list("Google","Runoob","Taobao")

# 合并列表
merged.list <- c(list1,list2)

# 显示合并后的列表
print(merged.list)

#列表转换成为向量
# 创建列表
list1 <- list(1:5)
print(list1)

list2 <-list(10:14)
print(list2)

# 转换为向量
v1 <- unlist(list1)
v2 <- unlist(list2)

print(v1)
print(v2)

# 两个向量相加
result <- v1+v2
print(result)