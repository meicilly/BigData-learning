import {Component} from "react";

class AddCounter extends Component {
    constructor() {
        super();
        this.state = {

        }
    }
    addCount(count) {
        this.props.addClick(count)
    }
    render() {
        return (
            <div>
                <button onClick={e => this.addCount(1)}>+1</button>
                <button onClick={e => this.addCount(5)}>+5</button>
                <button onClick={e => this.addCount(10)}>+10</button>
            </div>
        )
    }
}
export default AddCounter