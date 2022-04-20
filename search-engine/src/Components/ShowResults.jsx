import React, { Component, useState,useEffect } from "react";
import { ReactDOM } from "react";
import { render } from "react-dom";
import {BrowserRouter,Route,Link} from "react-router-dom"
import "../../node_modules/bootstrap/dist/css/bootstrap.css";
import Result from "./Result";

class Results extends Component{
    state={
            Results:[],
            ResultsPerPage:10,
            PagesNumbers:[],
            CurrentPage:1,
            PagePosts:[],
            query:this.props.query,
            queryArray:[],
        };
        componentDidMount(){
            fetch(" http://localhost:8000/History").then(response=>{
                if(response.ok)
                {
                    return response.json();
                }
                }).then(data=>{
                console.log(data);
                this.setState({query:data.currentSearch});
            })
            fetch("http://localhost:8080/api?query="+this.state.query).then(response=>{
                if(response.ok)
                {
                    return response.json();
                }
            }).then(data=>{
                console.log("the data recs");
                console.log(data);
                this.setState({Results:data.Results});
                const Numbers=[];
                for(let i=1;i<=Math.ceil(this.state.Results.length/this.state.ResultsPerPage);i++)
                {
                    Numbers.push(i);
                }
                this.setState({PagesNumbers:Numbers});
                //Get the start 
                let LastIndex = this.state.CurrentPage*this.state.ResultsPerPage;
                let FirstIndex= LastIndex-this.state.ResultsPerPage;
                const Posts=this.state.Results.slice(FirstIndex,LastIndex);
                this.setState({PagePosts:Posts});
                console.log(data);
            })
            
        }
        ChangePage=(pageNumber)=>{
            setTimeout(()=>{
                console.log(pageNumber);
                this.setState({CurrentPage:pageNumber});
                let LastIndex = this.state.CurrentPage*this.state.ResultsPerPage;
                let FirstIndex= LastIndex-this.state.ResultsPerPage;
                const Posts=this.state.Results.slice(FirstIndex,LastIndex);
                this.setState({PagePosts:Posts});
                console.log(this.state.PagePosts)
            }
            ,); 
        }

        nextPage=()=>{
            setTimeout(()=>{
                if(this.state.CurrentPage<this.state.PagesNumbers.length)
                {
                    this.setState({CurrentPage:this.state.CurrentPage+1});
                    let LastIndex = this.state.CurrentPage*this.state.ResultsPerPage;
                    let FirstIndex= LastIndex-this.state.ResultsPerPage;
                    const Posts=this.state.Results.slice(FirstIndex,LastIndex);
                    this.setState({PagePosts:Posts});
                }
            },);
            
            
        }
        prePage=()=>{
            setTimeout(()=>{
                if(this.state.CurrentPage<=this.state.PagesNumbers.length&& this.state.CurrentPage>=2)
            {
                this.setState({CurrentPage:this.state.CurrentPage-1});
                let LastIndex = this.state.CurrentPage*this.state.ResultsPerPage;
                let FirstIndex= LastIndex-this.state.ResultsPerPage;
                const Posts=this.state.Results.slice(FirstIndex,LastIndex);
                this.setState({PagePosts:Posts});
            }
        },);
            
        }
    render()
    {
        
        
        return(
            <div >
                <Result Posts={this.state.PagePosts}/>

                {this.state.PagePosts.length!==0&&<section aria-label="Page navigation example">
                    <ul className="pagination justify-content-center mt-5">
                        <li className="page-item" style={{"cursor":"pointer"}}>
                        <a class="page-link" onClick={this.prePage}  PreventDefault>Previous</a>
                        </li>
                        {this.state.PagesNumbers.map(PageNumber=>(<li style={{"cursor":"pointer"}} className={(this.state.CurrentPage==PageNumber)? " page-item active" :'"page-item"'} ><a className="page-link " onClick={()=>this.ChangePage(PageNumber)} PreventDefault>{PageNumber}</a></li>))}
                        <li className="page-item" style={{"cursor":"pointer"}}>
                        <a className="page-link"  onClick={this.nextPage} PreventDefault>Next</a>
                        </li>
                    </ul>
                </section>}

            </div>
        ) ;
    }
}
export default Results;