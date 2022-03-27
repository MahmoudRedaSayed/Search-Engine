import React, { Component, useState,useEffect } from "react";
import "../../node_modules/bootstrap/dist/css/bootstrap.css";
import "../../node_modules/@fortawesome/fontawesome-free/css/all.css";

class SearchBlock extends Component{
    state={
        products:["one","two","three"]
    }
    inc=()=>{
        let state1=[...this.state.products,"four"];
        this.setState({products:state1});
        console.log(this.state);
        fetch("/api").then(response=>{
                if(response.ok)
                {
                    return response.json();
                }
            }).then(data=>{
                this.setState(data);
                console.log(this.state);
            })
    }
    render(){

        return(
            <div className="container d-flex justify-content-center align-items-center " style={{"background-color": "transparent"}}>
                <div className="card mb-3 center-block" style={{"margin-top": "20%","width": "100%","border":"none","background-color": "transparent"}}>
                    <div className="row g-0">
                        <div className=" col-md-12">
                            <h1 className="text-center"style={{"color":"#198754","font-size":"5rem"}} >K3M</h1>
                            <div className="card-body">
                                <i className="fa-solid fa-microphone" />
                                <input className="form-control me-2" type="search" placeholder="Search" aria-label="Search" style={{"padding": "20px","border-radius": "30px","font-size": "21px","background-color": "#fff","border": "none","color":"#212529"}} onChange={this.inc}></input>
                                {/* <div className="Slider">
                                    {this.state.products.map((product)=>{return (<li>product</li>)})}
                                </div> */}
                            </div>
                        </div>
                </div>
            </div>
            </div>
        )
    }
}
export default SearchBlock;