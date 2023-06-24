import {Component} from "react";
import AddCounter from "./AddCounter";

export class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            counter: 100
        }
    }
    changeCounter(count){
        this.setState({counter:this.state.counter + count})
    }

    render() {
        const {counter} = this.state
        return (
            <div>
                <h2>当前计数: {counter}</h2>
                <AddCounter addClick={(count) => this.changeCounter(count)}/>
            </div>
        )

    }
}
export default App