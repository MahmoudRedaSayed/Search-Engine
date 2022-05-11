import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import Navbar from './Navbar';
import Result from './ShowResults';
import {useParams} from "react-router-dom";


function ResultPage(){
let {query}= useParams();
console.log(query);
        return(

            <div style={{    "background-color": "#0b0606e3","minHeight":"100vh","padding-top":'40px'}} >
                <Navbar showField={true}></Navbar>
                <Result query={query} ></Result>
            </div>
        );
}
export default ResultPage