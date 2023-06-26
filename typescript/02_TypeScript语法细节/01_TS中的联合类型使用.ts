// 1.联合类型基本使用
let foo: number | string = "abc"
foo = 123
function printID(id:number | string){
    console.log("您的ID:", id)

    // 类型缩小
    if (typeof id === "string") {
        console.log(id.length)
    } else {
        console.log(id)
    }
}
printID("abc")
printID(123)