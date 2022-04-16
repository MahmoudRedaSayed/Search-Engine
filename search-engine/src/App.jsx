import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import SearchPage from './Components/SearchPage';
import {Route,Link,Routes} from "react-router-dom"
import ResultPage from './Components/ResultsPage';

function App (){
        return(
            < Routes>
                <Route path="/K3M" exact element={<SearchPage />}/>
                <Route path="K3M/Results/:query"  element={<ResultPage/>} />
            </Routes>

        );
}
export default App;