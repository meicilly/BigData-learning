#barplot(H,xlab,ylab,main, names.arg,col,beside)
# H 向量或矩阵，包含图表用的数字值，每个数值表示矩形条的高度。
# xlab x 轴标签。
# ylab y 轴标签。
# main 图表标题。
# names.arg 每个矩形条的名称。
# col 每个矩形条的颜色。
# 准备一个向量
cvd19 = c(83534,2640626,585493)

# 显示条形图
barplot(cvd19)

cvd19 = c(83534,2640626,585493)

barplot(cvd19,
    main="新冠疫情条形图",
    col=c("#ED1C24","#22B14C","#FFC90E"),
    names.arg=c("中国","美国","印度"),
    family='GB1'
)

library(showtext);
font_add("SyHei", "SourceHanSansSC-Bold.otf");
cvd19 = matrix(
  c(83017, 83534, 1794546, 2640626, 190535, 585493),
  2, 3
)

# 设置文件名，输出为 png
png(file = "runoob-bar-1.png")

#加载字体
showtext_begin();

colnames(cvd19) = c("中国", "美国", "印度")
rownames(cvd19) = c("6月", "7月")
barplot(cvd19, main = "新冠疫情条形图", beside=TRUE, legend=TRUE,  family='SyHei')

# 去掉字体
showtext_end();

library(plotrix)
library(showtext);
font_add("SyHei", "SourceHanSansSC-Bold.otf");
cvd19 = matrix(
  c(83017, 83534, 1794546, 2640626, 190535, 585493),
  2, 3
)

# 设置文件名，输出为 png
png(file = "runoob-bar-2.png")
#加载字体
showtext_begin();
colnames(cvd19) = c("中国", "美国", "印度")
rownames(cvd19) = c("6月", "7月")

barplot(cvd19, main = "新冠疫情条形图", beside=TRUE, legend=TRUE,col=c("blue","green"),  family='SyHei')
# 去掉字体
showtext_end();

library(showtext);
font_add("SyHei", "SourceHanSansSC-Bold.otf");
cvd19 = matrix(
  c(83017, 83534, 1794546, 2640626, 190535, 585493),
  2, 3
)

# 设置文件名，输出为 png
png(file = "runoob-bar-3.png")
#加载字体
showtext_begin();
colnames(cvd19) = c("中国", "美国", "印度")
rownames(cvd19) = c("6月", "7月")

barplot(cvd19, main = "新冠疫情条形图", beside=FALSE, legend=TRUE,col=c("blue","green"),  family='SyHei')
# 去掉字体