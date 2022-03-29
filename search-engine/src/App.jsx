import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import SearchPage from './Components/SearchPage';
import {BrowserRouter,Route,Link} from "react-router-dom"
import ResultPage from './Components/ResultsPage';



class App extends Component{
    render(){
        return(
            // <BrowserRouter >
            //     <Route path="/home" exact >
            //         <SearchPage></SearchPage>
            //     </Route>
            //     <Route path="/results" exact  >
            //         <ResultPage></ResultPage>
            //     </Route>
            // </ BrowserRouter >
            // <SearchPage></SearchPage>
            <ResultPage></ResultPage>

        );
    }
}
export default App