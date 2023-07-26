# factor(x = character(), levels, labels = levels,
#        exclude = NA, ordered = is.ordered(x), nmax = NA)
#x：向量。
#levels：指定各水平值, 不指定时由x的不同值来求得。
#labels：水平的标签, 不指定时用各水平值的对应字符串。
#exclude：排除的字符。
#ordered：逻辑值，用于指定水平是否有序。
#nmax：水平的上限数量。
x <- c("男", "女", "男", "男", "女")
sex <- factor(x)
print(sex)
print(is.factor(sex))
#设置因子水平为 c('男','女')
x <- c("男", "女", "男", "男",  "女",levels=c('男','女'))
sex <- factor(x)
print(sex)
print(is.factor(sex))

#因子水平标签
sex=factor(c('f','m','f','f','m'),levels=c('f','m'),labels=c('female','male'),ordered=TRUE)
print(sex)

#生成因子水平
#gl(n, k, length = n*k, labels = seq_len(n), ordered = FALSE)
# n: 设置 level 的个数
# k: 设置每个 level 重复的次数
# length: 设置长度
# labels: 设置 level 的值
# ordered: 设置是否 level 是排列好顺序的，布尔值
v <- gl(3, 4, labels = c("Google", "Runoob","Taobao"))
print(v)