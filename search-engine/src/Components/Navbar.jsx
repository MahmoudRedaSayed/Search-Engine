import React, { Component } from "react";
import "../../node_modules/bootstrap/dist/css/bootstrap.css"
import "../../node_modules/bootstrap/dist/js/bootstrap.bundle";

function Navbar (show){
    console.log(show);
        return(
            <div>
                <nav className="navbar navbar-light bg-light">
                    <div className="container-fluid">
                        <a className="navbar-brand" style={{"font-size":"2rem" , "color":"#198754"}} href="#">K3M</a>
                        <form className="d-flex">
                        {(show.showField)?<input className="form-control me-2" type="search" placeholder="Search" aria-label="Search"></input>:""}
                        </form>
                    </div>
                </nav>
            </div>
        );
}
export default Navbar