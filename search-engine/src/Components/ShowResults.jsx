import React, { Component, useState,useEffect } from "react";
import { ReactDOM } from "react";
import { render } from "react-dom";
import {BrowserRouter,Route,Link} from "react-router-dom"
import "../../node_modules/bootstrap/dist/css/bootstrap.css";
import Result from "./Result";
import Spinner from "react-spinkit";

class Results extends Component{
    state={
            Results:[],
            ResultsPerPage:10,
            PagesNumbers:[],
            CurrentPage:1,
            PagePosts:[],
            query:this.props.query,
            queryArray:[],
            load:true,
        };
        componentDidMount(){
            this.setState({load:true});
            console.log(this.state.load);
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
                this.setState({Results:data});
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
                this.setState({load:false});
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
                {this.state.load&&<Spinner name="chasing-dots" style={{ width: 100, height: 100 , position:"absolute", top:"50%",left:"45%",color:"rgb(11 149 212)" }} />}
                {!this.state.load&&
                <div>
                    {this.state.PagesNumbers.length>1&&<section aria-label="Page navigation example" style={{position:"relative","z-index":"1","padding-top":"40px"}}>
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
                    <Result Posts={this.state.PagePosts} query={this.state.query}/>
                </div>
                }

            </div>
        ) ;
    }
}
export default Results;