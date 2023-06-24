import React from "react";
import HelloWorld from "./HelloWorld";

class App extends React.Component {
    constructor() {
        super();
        this.state = {
            isShowHW: true
        }
    }
    switchHWShow(){
        this.setState({isShowHW: !this.state.isShowHW})
    }

    render() {
        const {isShowHW} = this.state;
        return(
            <div>
                <button onClick={e => this.switchHWShow()}>切换</button>
                <div>{isShowHW && <HelloWorld/>}</div>
            </div>
        )
    }
}

export  default  App