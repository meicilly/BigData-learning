# pie(x, labels = names(x), edges = 200, radius = 0.8,
#     clockwise = FALSE, init.angle = if(clockwise) 90 else 0,
#     density = NULL, angle = 45, col = NULL, border = NULL,
#     lty = NULL, main = NULL, …)

# x: 数值向量，表示每个扇形的面积。
# labels: 字符型向量，表示各扇形面积标签。
# edges: 这个参数用处不大，指的是多边形的边数（圆的轮廓类似很多边的多边形）。
# radius: 饼图的半径。
# main: 饼图的标题。
# clockwise: 是一个逻辑值,用来指示饼图各个切片是否按顺时针做出分割。
# angle: 设置底纹的斜率。
# density: 底纹的密度。默认值为 NULL。
# col: 是表示每个扇形的颜色，相当于调色板。

# 数据准备
info = c(1, 2, 4, 8)

# 命名
names = c("Google", "Runoob", "Taobao", "Weibo")

# 涂色（可选）
cols = c("#ED1C24","#22B14C","#FFC90E","#3f48CC")

# 绘图
pie(info, labels=names, col=cols)

#png()、jpeg()、bmp() 函数设置输出的文件格式为图片
# 数据准备
info = c(1, 2, 4, 8)

# 命名
names = c("Google", "Runoob", "Taobao", "Weibo")

# 涂色（可选）
cols = c("#ED1C24","#22B14C","#FFC90E","#3f48CC")

# 设置输出图片
png(file='runoob-pie.png', height=300, width=300)
# 绘图
pie(info, labels=names, col=cols)
#3D绘图
#install.packages("plotrix", repos = "https://mirrors.ustc.edu.cn/CRAN/")
# 命名
# 载入 plotrix
# library(plotrix)
# info = c(1, 2, 4, 8)
# names = c("Google", "Runoob", "Taobao", "Weibo")

# # 涂色（可选）
# cols = c("#ED1C24","#22B14C","#FFC90E","#3f48CC")

# # 设置文件名，输出为 png
# png(file = "3d_pie_chart.png")

# # 绘制 3D 图，family 要设置你系统支持的中文字体库
# pie3D(info,labels = names,explode = 0.1, main = "3D 图",family = "STHeitiTC-Light")