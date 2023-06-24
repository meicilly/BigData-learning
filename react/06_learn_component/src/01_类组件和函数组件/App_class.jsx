import React from "react"
// 类组件
class App extends React.Component {
    constructor(){
        super()
        this.state = {
            message:"App component"
        }
    }
    render() {
        return <h2>{this.state.message}</h2>
    }
}

export  default  App;