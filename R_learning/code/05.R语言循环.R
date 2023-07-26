#1.循环类型
#repeat
v <- c("Google","Runoob")
cnt <- 2
repeat {
    print(v)
    cnt <- cnt + 1
    if(cnt > 5){
        break#退出循环
    }
}

#while
v <- c('Google','Runoob')
cnt <- 2
while(cnt < 7){
    print(v)
    cnt <- cnt + 1
}

#for
v <- LETTERS[1:4]
for ( i in v) {
   print(i)
}

#next
v <- LETTERS[1:6]
for ( i in v) {
   
   if (i == "D") {  # D 不会输出，跳过这次循环，进入下一次
      next
   }
   print(i)
}