import React, { Component } from "react";
import "../../node_modules/bootstrap/dist/css/bootstrap.css"
import "../../node_modules/bootstrap/dist/js/bootstrap.bundle";
import img from "../Image/NotFound.gif";
// import ReactHtmlParser from 'react-html-parser';
class Result extends Component{
    state={
        posts:this.props.Posts,
    }
componentDidMount(){
    console.log("from did mount");
    console.log(this.props.Posts);
    console.log(this.state.posts);

}
// this function will take the array of the query words and the content
 makeBold=(input, wordsToBold)=>{
    console.log( input.replace(new RegExp('(\\b)(' + wordsToBold.join('|') + ')(\\b)','ig'), '$1<h1>$2</h1>$3'));
    return input.replace(new RegExp('(\\b)(' + wordsToBold.join('|') + ')(\\b)','ig'), '$1<b>$2</b>$3');
}
    render(){
        return(
            <div className="container">
                {this.props.Posts&&this.props.Posts.map((Post)=>
                
                        <div className="col-md-12 card mt-5 bg-light p-4 " style={{"width": "100%"}}>
                            <h5 className="card-title">{Post.Post1.Title}</h5>
                    
                            <p className="card-text" dangerouslySetInnerHTML={{ __html: this.makeBold("Some quick example text to build on the card title and make up the bulk of the card's content.",["some"]) }}></p>
                        </div>
                )}
                {(this.props.Posts.length===0)?<div className="container mt-5 row" ><img className="col-md-12 mt-5" src={img} style={{"border-radius":"50px"}} alt="" /></div>:""}
        </div>);
    }
}
export default Result;