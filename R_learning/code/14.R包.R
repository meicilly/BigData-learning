#查看 R 包的安装目录
.libPaths()
#查看已安装的包
library()
#查看已载入的包
search()
#安装新的包
install.packages("要安装的包名")
#直接在 CRAN 上下载相关包，直接在本地安装：
install.packages("./XML_3.98-1.3.zip")
#使用清华源进行安装：
# 安装 XML 包
install.packages("XML", repos = "https://mirrors.ustc.edu.cn/CRAN/")
#CRAN (The Comprehensive R Archive Network) 镜像源配置文件之一是 .Rprofile (linux 下位于 ~/.Rprofile )。
options("repos" = c(CRAN="https://mirrors.tuna.tsinghua.edu.cn/CRAN/"))
#使用包
library("XML")