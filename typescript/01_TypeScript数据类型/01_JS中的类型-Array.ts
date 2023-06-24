// 明确的指定<数组>的类型注解: 两种写法
// 1. string[]: 数组类型, 并且数组中存放的字符串类型
// 2. Array<string>: 数组类型, 并且数组中存放的是字符串类型
let names:string[] = ['abc','cba','nba']
console.log(names)
let nums:Array<number> = [123,321,111]
console.log(nums)
export {}