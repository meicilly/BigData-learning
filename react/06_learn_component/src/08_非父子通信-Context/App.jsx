import {Component} from "react";

class App extends Component {
    constructor() {
        super();
        this.state = {
            info: {name:"kobe",age:30}
        }
    }
    render() {
        const {info} = this.state
    }
}