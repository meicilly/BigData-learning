install.packages("RMySQL", repos = "https://mirrors.ustc.edu.cn/CRAN/")
any(grepl("RMySQL",installed.packages()))
"
--
-- 表的结构 `runoob`
--
 
CREATE TABLE `runoob` (
  `id` int(11) NOT NULL,
  `name` char(20) NOT NULL,
  `url` varchar(255) NOT NULL,
  `likes` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
--
-- 转存表中的数据 `runoob`
--
 
INSERT INTO `runoob` (`id`, `name`, `url`, `likes`) VALUES
(1, 'Google', 'www.google.com', 111),
(2, 'Runoob', 'www.runoob.com', 222),
(3, 'Taobao', 'www.taobao.com', 333);
"
library(RMySQL)

# dbname 为数据库名，这边的参数请根据自己实际情况填写
mysqlconnection = dbConnect(MySQL(), user = 'root', password = '', dbname = 'test',host = 'localhost')

# 查看数据
dbListTables(mysqlconnection)
# 查询 sites 表，增删改查操作可以通过第二个参数的 SQL 语句来实现
result = dbSendQuery(mysqlconnection, "select * from sites")

# 获取前面两行数据
data.frame = fetch(result, n = 2)
print(data.frame)