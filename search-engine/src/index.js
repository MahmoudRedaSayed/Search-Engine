import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from "./App"
import SearchPage from './Components/SearchPage';
import {BrowserRouter,Route,Link,useParams} from "react-router-dom"
import ResultPage from './Components/ResultsPage';



ReactDOM.render(
  <div>
    <BrowserRouter >
        <App></App>
    </ BrowserRouter >
  </div>
    ,document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
