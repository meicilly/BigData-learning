#array(data = NA, dim = length(data), dimnames = NULL)
#data - 指定数组的数据源，可以是一个向量、矩阵或列表。
#dim - 指定数组的维度，可以是一个整数向量或一个表示维度的元组，默认是一维数组。例如，dim = c(2, 3, 4) 表示创建一个 2x3x4 的三维数组。
#dimnames - 可选参数，用于指定数组每个维度的名称，可以是一个包含维度名称的列表。
# 创建一个3x3的矩阵
my_matrix <- matrix(c(1, 2, 3, 4, 5, 6, 7, 8, 9), nrow = 3, ncol = 3)
print(my_matrix)
# 创建一个包含矩阵和向量的列表
my_list <- list(matrix(c(1, 2, 3, 4), nrow = 2), c(5, 6, 7))
print(my_list)

#创建一维数组
my_vector <- c(1, 2, 3, 4)
my_array <- array(my_vector, dim = c(4))
print(my_array)
#创建一个 3 行 3 列的的二维数组
# 创建两个不同长度的向量
vector1 <- c(5,9,3)
vector2 <- c(10,11,12,13,14,15)

# 创建数组
result <- array(c(vector1,vector2),dim = c(3,3,2))
print(result)

# 创建两个不同长度的向量
vector1 <- c(5,9,3)
vector2 <- c(10,11,12,13,14,15)
column.names <- c("COL1","COL2","COL3")
row.names <- c("ROW1","ROW2","ROW3")
matrix.names <- c("Matrix1","Matrix2")

# 创建数组，并设置各个维度的名称
result <- array(c(vector1,vector2),dim = c(3,3,2),dimnames = list(row.names,column.names,matrix.names))
print(result)

#访问数组元素
my_array <- array(1:12, dim = c(2, 3, 2))  # 创建一个3维数组
element <- my_array[1, 2, 1]  # 访问第一个维度为1，第二个维度为2，第三个维度为1的元素
print(element)  # 输出：2

my_array <- array(1:12, dim = c(2, 3, 2))  # 创建一个3维数组
elements <- my_array[c(1, 2), c(2, 3), c(1, 2)]  # 访问多个元素，其中每个维度的索引分别为1和2
print(elements)  # 输出：2 6

# 创建两个不同长度的向量
vector1 <- c(5,9,3)
vector2 <- c(10,11,12,13,14,15)
column.names <- c("COL1","COL2","COL3")
row.names <- c("ROW1","ROW2","ROW3")
matrix.names <- c("Matrix1","Matrix2")

# 创建数组
result <- array(c(vector1,vector2),dim = c(3,3,2),dimnames = list(row.names, column.names, matrix.names))

# 显示数组第二个矩阵中第三行的元素
print(result[3,,2])

# 显示数组第一个矩阵中第一行第三列的元素
print(result[1,3,1])

# 输出第二个矩阵
print(result[,,2])

#使用逻辑筛选
my_array <- array(1:12, dim = c(2, 3, 2))  # 创建一个3维数组
filtered_elements <- my_array[my_array > 5]  # 选择大于5的元素
print(filtered_elements)  # 输出：6 7 8 9 10 11 12

#操作数组元素
# 创建两个不同长度的向量
vector1 <- c(5,9,3)
vector2 <- c(10,11,12,13,14,15)

# 创建数组
array1 <- array(c(vector1,vector2),dim = c(3,3,2))

# 创建两个不同长度的向量
vector3 <- c(9,1,0)
vector4 <- c(6,0,11,3,14,1,2,6,9)
array2 <- array(c(vector3,vector4),dim = c(3,3,2))

# 从数组中创建矩阵
matrix1 <- array1[,,2]
matrix2 <- array2[,,2]

# 矩阵相加
result <- matrix1+matrix2
print(result)

# 创建两个不同长度的向量
vector1 <- c(5,9,3)
vector2 <- c(10,11,12,13,14,15)

# 创建数组
new.array <- array(c(vector1,vector2),dim = c(3,3,2))
print(new.array)

# 计算数组中所有矩阵第一行的数字之和
result <- apply(new.array, c(1), sum)
print(result)

#对矩阵的行或列应用内置函数
# 创建一个3x3的矩阵
my_matrix <- matrix(1:9, nrow = 3)
# 对每列应用sum函数
col_sums <- apply(my_matrix, 2, sum)
print(col_sums)

#对矩阵的行或列应用自定义函数
# 创建一个3x3的矩阵
my_matrix <- matrix(1:9, nrow = 3)
# 自定义函数：计算每行的平均值
row_mean <- function(x) {
 return(mean(x))
}
# 对每行应用row_mean函数
row_means <- apply(my_matrix, 1, row_mean)
print(row_means)

#对数组的多个维度同时应用函数
# 创建一个3维数组
my_array <- array(1:12, dim = c(2, 3, 2))
# 对第一个和第三个维度同时应用mean函数
result <- apply(my_array, c(1, 3), mean)
print(result)