import React, { Component } from "react";
import "../../node_modules/bootstrap/dist/css/bootstrap.css"
import "../../node_modules/bootstrap/dist/js/bootstrap.bundle";
import img from "../Image/NotFound.gif";
import ClipLoader from "react-spinners/CircleLoader"
// import ReactHtmlParser from 'react-html-parser';
class Result extends Component{
    state={
        posts:this.props.Posts,
        query:this.props.query,
        queryArray:this.props.array,
        loading:true,
    }

// this function will take the array of the query words and the content
 makeBold=(input, wordsToBold)=>{
     if(input!==undefined)
        return input.replace(new RegExp('(\\b)(' + wordsToBold.join('|') + ')(\\b)','ig'), '$1<b>$2</b>$3');
}
    render(){
        return(
            <div className="container">
                {this.props.Posts.length!==0&&this.props.Posts.map((Post)=>
                
                        <div className="col-md-12 card mt-5 bg-light p-4 " style={{"width": "100%"}}>
                            <a className="card-title"target="_blank" href={Post.Link}>{Post.Link}</a>
                            <p className="card-text" dangerouslySetInnerHTML={{ __html: this.makeBold(Post.snip,this.state.queryArray) }}></p>
                        </div>
                )}
                {(this.props.Posts.length===0)?<div className="container mt-5 row" ><img className="col-md-12 mt-5" src={img} style={{"border-radius":"50px"}} alt="" /></div>:""}
        </div>);
    }
}
export default Result;