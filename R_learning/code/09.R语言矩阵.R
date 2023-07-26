#matrix(data = NA, nrow = 1, ncol = 1, byrow = FALSE,dimnames = NULL)
#data 向量，矩阵的数据
#nrow 行数
#ncol 列数
#byrow 逻辑值，为 FALSE 按列排列，为 TRUE 按行排列
#dimname 设置行和列的名称

#byrow为TRUE元素按行排列
M <- matrix(c(3:14),nrow = 4,byrow = TRUE)
print(M)

# Ebyrow 为 FALSE 元素按列排列
N <- matrix(c(3:14), nrow = 4, byrow = FALSE)
print(N)

# 定义行和列的名称
rownames = c("row1", "row2", "row3", "row4")
colnames = c("col1", "col2", "col3")
P <- matrix(c(3:14), nrow = 4, byrow = TRUE, dimnames = list(rownames, colnames))
print(P)

#转置矩阵
# 创建一个 2 行 3 列的矩阵
M = matrix( c(2,6,5,1,10,4), nrow = 2,ncol = 3,byrow = TRUE)
print(M)
# 转换为 3 行 2 列的矩阵
print(t(M))

#访问矩阵元素
# 定义行和列的名称
rownames = c("row1", "row2", "row3", "row4")
colnames = c("col1", "col2", "col3")

# 创建矩阵
P <- matrix(c(3:14), nrow = 4, byrow = TRUE, dimnames = list(rownames, colnames))
print(P)
# 获取第一行第三列的元素
print(P[1,3])

# 获取第四行第二列的元素
print(P[4,2])

# 获取第二行
print(P[2,])

# 获取第三列
print(P[,3])

#矩阵加减法
# 创建 2 行 3 列的矩阵
matrix1 <- matrix(c(7, 9, -1, 4, 2, 3), nrow = 2)
print(matrix1)

matrix2 <- matrix(c(6, 1, 0, 9, 3, 2), nrow = 2)
print(matrix2)

# 两个矩阵相加
result <- matrix1 + matrix2
cat("相加结果：","\n")
print(result)

# 两个矩阵相减
result <- matrix1 - matrix2
cat("相减结果：","\n")
print(result)
#矩阵乘除法
# 创建 2 行 3 列的矩阵
matrix1 <- matrix(c(7, 9, -1, 4, 2, 3), nrow = 2)
print(matrix1)

matrix2 <- matrix(c(6, 1, 0, 9, 3, 2), nrow = 2)
print(matrix2)

# 两个矩阵相乘
result <- matrix1 * matrix2
cat("相乘结果：","\n")
print(result)

# 两个矩阵相除
result <- matrix1 / matrix2
cat("相除结果：","\n")
print(result)
