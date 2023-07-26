#1.向量
#c()是创造向量的函数
a = c(3,4)
b = c(5,0)
a + b

#2.向量中的每一个元素可以通过下标单独取出
a = c(10,20,30,40,50)
a[2]

#3.也可以取出向量的一部分
a[1:4] # 取出第 1 到 4 项，包含第 1 和第 4 项
a[c(1, 3, 5)] # 取出第 1, 3, 5 项
a[c(-1, -5)] # 去掉第 1 和第 5 项

#4.向量支持标量计算
c(1.1,1.2,1.3) - 0.5
a = c(1,2)
a ^ 2

#5.向量排序
a = c(1,3,5,2,4,6)
sort(a)
rev(a)
order(a)#返回的是一个向量排序之后的下标向量
a[order(a)]

#6.向量统计
#sum  求和
#mean 求平均值
#var  方差
#sd   标准差
#min  最小值
#max  最大值
#range 取值范围(二维向量，最大值和最小值)
sum(1:5)
sd(1:5)
range(1:5)

#7.向量生成
#向量的生成可以用 c() 函数生成，也可以用 min:max 运算符生成连续的序列
seq(1, 9, 2)
#seq 还可以生成从 m 到 n 的等差数列，只需要指定 m, n 以及数列的长度
seq(0, 1, length.out=3)
#rep 是 repeat（重复）的意思，可以用于产生重复出现的数字序列
rep(0, 5)
#向量中常会用到 NA 和 NULL ，这里介绍一下这两个词语与区别：
#    NA 代表的是"缺失"，NULL 代表的是"不存在"。
#    NA 缺失就像占位符，代表这里没有一个值，但位置存在。
#    NULL 代表的就是数据不存在。

length(c(NA, NA, NULL))
c(NA, NA, NULL, NA)

#8.逻辑型
c(11,12,13) > 12
#which 函数可以用于筛选我们需要的数据的下标
a = c(11, 12, 13)
b = a > 12
print(b)
which(b)

all(c(TRUE, TRUE, TRUE))
all(c(TRUE, TRUE, FALSE))
any(c(TRUE, FALSE, FALSE))
any(c(FALSE, FALSE, FALSE))

#9.字符串
toupper("Runoob") # 转换为大写
tolower("Runoob") # 转换为小写
nchar("中文", type="bytes") # 统计字节长度
nchar("中文", type="char") # 总计字符数量
substr("123456789", 1, 5) # 截取字符串，从 1 到 5
substring("1234567890", 5) # 截取字符串，从 5 到结束
as.numeric("12") # 将字符串转换为数字
as.character(12.34) # 将数字转换为字符串
strsplit("2019;10;1", ";") # 分隔符拆分字符串
gsub("/", "-", "2019/10/1") # 替换字符串


#10.矩阵
vector=c(1,2,3,4,5,6)
matrix(vector,2,3)
#向量中的值会一列一列的填充到矩阵中。如果想按行填充，需要指定 byrow 属性：
matrix(vector, 2, 3, byrow=TRUE)
#矩阵中的每一个值都可以被直接访问：
m1 = matrix(vector, 2, 3, byrow=TRUE)
m1[1,1] # 第 1 行 第 1 列
m1[1,3] # 第 1 行 第 3 列

#R 中的矩阵的每一个列和每一行都可以设定名称，这个过程通过字符串向量批量完成：
colnames(m1) = c("x", "y", "z")
rownames(m1) = c("a", "b")
m1
m1["a", ]

#矩阵的四则运算与向量基本一致，既可以与标量做运算，也可以与同规模的矩阵做对应位置的运算。
m1 = matrix(c(1, 2), 1, 2)
m2 = matrix(c(3, 4), 2, 1)
m1 %*% m2

#solve() 函数用于求解线性代数方程，基本用法是 solve(A,b)，其中，A 为方程组的系数矩阵，b 方程的向量或矩阵。
#apply() 函数可以将矩阵的每一行或每一列当作向量来进行操作：
(A = matrix(c(1, 3, 2, 4), 2, 2))
apply(A, 1, sum) # 第二个参数为 1 按行操作，用 sum() 函数
apply(A, 2, sum) # 第二个参数为 2 按列操作