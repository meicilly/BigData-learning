import React from "react";


class HelloWorld extends React.Component {
    constructor() {
        console.log("HelloWorld constructor")
        super();
        this.state = {
            message: "Hello World"
        }
    }
    changeText(){
        this.setState({message: "你好啊！meicilly"})
    }

    render() {
        console.log("HelloWorld render")
        const {message} = this.state

        return (
            <div>
                <h2>{message}</h2>
                <button onClick={e => this.changeText()}>修改文本</button>
            </div>
        )
    }
    // 3.组件被渲染到DOM 被挂载到DOM
    componentDidMount(){
        console.log("helloWorld componentDidMount")
    }
    // 4.组件的DOM被更新完成： DOM发生更新
    componentDidUpdate(prevProps, prevState, snapshot) {
        console.log("HelloWorld componentDidUpdate:", prevProps, prevState, snapshot)
    }
    // 5.组件从DOM中卸载掉： 从DOM移除掉
    componentWillUnmount() {
        console.log("HelloWorld componentWillUnmount")
    }

    // 不常用的生命周期补充
    shouldComponentUpdate() {
        return true
    }
    getSnapshotBeforeUpdate() {
        console.log("getSnapshotBeforeUpdate")
        return {
            scrollPosition: 1000
        }
    }
}
export default HelloWorld

