import React, { Component, useState,useEffect } from "react";
import { ReactDOM } from "react";
import { render } from "react-dom";
import {BrowserRouter,Route,Link} from "react-router-dom"
import "../../node_modules/bootstrap/dist/css/bootstrap.css";

class Result extends Component{
    state={
            Results:['One',"Two","Three"],
            sliders:3
        };
    render()
    {
        return (
            
            this.state.Results.map((result)=>{
                return(
                    <div>
                    <div class="card mb-3">
                        {/* <img src="..." class="card-img-top" alt="..."> */}
                        <div class="card-body">
                            <h3 class="card-title">{result} link</h3>
                        </div>
                        </div>
                        <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">content</h5>
                            <p class="card-text">link descripation.</p>
                            <p class="card-text"><small class="text-muted">Last updated 3 mins ago</small></p>
                        </div>
                        {/* <img src="..." class="card-img-bottom" alt="..."></img> */}
                    </div>
                    </div>
                )
                
            })
           
        )
    }
}
export default Result;