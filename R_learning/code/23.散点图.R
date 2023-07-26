# plot(x, y, type="p", main, xlab, ylab, xlim, ylim, axes)
# x 横坐标 x 轴的数据集合

# y 纵坐标 y 轴的数据集合

# type：绘图的类型，p 为点、l 为直线， o 同时绘制点和线，且线穿过点。
# main 图表标题。

# xlab、ylab x 轴和 y 轴的标签名称。

# xlim、ylim x 轴和 y 轴的范围。

# axes 布尔值，是否绘制两个 x 轴。
x<-c(10,40)
y<-c(20,60)
# 生成 png 图片
png(file = "runnob-test-plot2.png")

plot(x, y, "l")

x<-c(10,40)
y<-c(20,60)
# 生成 png 图片
png(file = "runnob-test-plot.png")

plot(x, y, "o")
#mtcars 数据集的 wt 和 mpg 列
input <- mtcars[,c('wt','mpg')]
print(head(input))

# 数据
input <- mtcars[,c('wt','mpg')]

# 生成 png 图片
png(file = "scatterplot.png")

# 设置坐标 x 轴范围 2.5 到 5, y 轴范围 15 到 30.
plot(x = input$wt,y = input$mpg,
   xlab = "Weight",
   ylab = "Milage",
   xlim = c(2.5,5),
   ylim = c(15,30),              
   main = "Weight vs Milage"
)

# 输出图片
png(file = "scatterplot_matrices.png")

# 4 个变量绘制矩阵，12 个图

pairs(~wt+mpg+disp+cyl,data = mtcars, main = "Scatterplot Matrix")