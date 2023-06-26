import {Component} from "react";

import Home from "./Home"
import ThemeContext from "./context/theme-context";
import UserContext from "./context/user-context";

class App extends Component {
    constructor() {
        super();
        this.state = {
            info: {name:"kobe",age:30}
        }
    }
    render() {
        const {info} = this.state
        return (
            <div>
                <h2>App</h2>
                {/*<Home name={info.name} age={info.age}></Home>*/}
                {/*<Home {...info}></Home>*/}
                <UserContext.Provider value={{nickname: "kobe", age: 30}}>
                    <ThemeContext.Provider value={{color: "red", size: "30"}}>
                        <Home {...info}/>
                    </ThemeContext.Provider>
                </UserContext.Provider>
            </div>
        )
    }
}

export default  App