# curve(expr, from = NULL, to = NULL, n = 101, add = FALSE,
#       type = "l", xname = "x", xlab = xname, ylab = NULL,
#       log = NULL, xlim = NULL, …)

# # S3 函数的方法
# plot(x, y = 0, to = 1, from = y, xlim = NULL, ylab = NULL, …)


# expr：函数表达式
# from 和 to：绘图的起止范围
# n：一个整数值，表示 x 取值的数量
# add：是一个逻辑值，当为 TRUE 时，表示将绘图添加到已存在的绘图中。
# type：绘图的类型，p 为点、l 为直线， o 同时绘制点和线，且线穿过点。
# xname：用于 x 轴变量的名称。
# xlim 和 ylim 表示x轴和y轴的范围。
# xlab，ylab：x 轴和 y 轴的标签名称。
curve(sin(x), -2 * pi, 2 * pi)

# 定义函数 f
f = function (x) {
    if (x >= 0) {
        x
    } else {
        x ^ 2
    }
}

# 生成自变量序列
x = seq(-2, 2, length=100)

# 生成因变量序列
y = rep(0, length(x))
j = 1
for (i in x) {
    y[j] = f(i)
    j = j + 1
}

# 绘制图像
plot(x, y, type='l')


# 向量数据
v <- c(7,12,28,3,41)

# 生成图片
png(file = "line_chart_label_colored.jpg")

# 绘图、线图颜色为红色，main 参数用于设置标题
plot(v,type = "o", col = "red", xlab = "Month", ylab = "Rain fall",
   main = "Rain fall chart")