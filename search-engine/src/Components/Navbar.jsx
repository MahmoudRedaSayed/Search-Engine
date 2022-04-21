import React, { Component } from "react";
import "../../node_modules/bootstrap/dist/css/bootstrap.css"
import "../../node_modules/bootstrap/dist/js/bootstrap.bundle";
import SearchBlock from "./SearchBlock";
import SearchField from "./SearchField";

function Navbar (show){
    console.log(show);
        return(
            <div>
                <nav className="navbar navbar-light bg-light">
                    <div className="container-fluid">
                        <a className="navbar-brand" style={{"font-size":"2rem" , "color":"#198754"}} href="/K3M">K3M</a>
                    </div>
                </nav>
            </div>
        );
}
export default Navbar