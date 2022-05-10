import React, { Component } from "react";
import "../../node_modules/bootstrap/dist/css/bootstrap.css"
import "../../node_modules/bootstrap/dist/js/bootstrap.bundle";
import SearchBlock from "./SearchBlock";
import SearchField from "./SearchField";

function Navbar (show){
    console.log(show);
        return(
            <div style={{"z-index":"2"}}>
                <nav className="navbar navbar-light bg-light">
                    <div className="container-fluid">
                        <a className="navbar-brand" style={{"font-size":"2rem" , "color":" rgb(11 149 212)", "font-family": "cursive"}} href="/K3M">K<i style={{"color":"#c7c2c2"}}>3</i>M</a>
                    </div>
                </nav>
            </div>
        );
}
export default Navbar