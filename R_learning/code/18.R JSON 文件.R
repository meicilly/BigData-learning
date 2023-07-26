#install.packages("rjson", repos = "https://mirrors.ustc.edu.cn/CRAN/")
any(grepl("rjson",installed.packages()))
# 载入 rjson 包
library("rjson")

# 获取 json 数据
result <- fromJSON(file = "sites.json")

# 输出结果
print(result)

print("===============")

# 输出第 1 列的结果
print(result[1])

print("===============")
# 输出第 2 行第 2 列的结果
print(result[[2]][[2]])
# 转为数据框
json_data_frame <- as.data.frame(result)

print(json_data_frame)