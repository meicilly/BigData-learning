x <- 50L
if(is.integer(x)){
    print("X 是一个整数")
}

x <- c("google","runoob","taobao")

if("runoob" %in% x) {
 print("包含 runoob")
} else {
 print("不包含 runoob")
}

x <- c("google","runoob","taobao")

if("weibo" %in% x) {
 print("第一个 if 包含 weibo")
} else if ("runoob" %in% x) {
 print("第二个 if 包含 runoob")
} else {
 print("没有找到")
}

#switch
x <- switch(
3,
"google",
"runoob",
"taobao",
"weibo"
)
print(x)

you.like<-"runoob"
switch(you.like, google="www.google.com", runoob = "www.runoob.com", taobao = "www.taobao.com")
#如果整数不在范围内的则返回 NULL
x <- switch(4,"google","runoob","taobao")
X
