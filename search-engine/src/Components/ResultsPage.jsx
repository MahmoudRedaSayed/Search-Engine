import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import Navbar from './Navbar';
import Result from './ShowResults';


class ResultPage extends Component{
    render(){
        return(

            <div>
                <Navbar></Navbar>
                <Result></Result>
            </div>
        );
    }
}
export default ResultPage