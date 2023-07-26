#install.packages("xlsx", repos = "https://mirrors.ustc.edu.cn/CRAN/")
# 读取 sites.xlsx 第一个工作表数据
data <- read.xlsx("sites.xlsx", sheetIndex = 1)
print(data)