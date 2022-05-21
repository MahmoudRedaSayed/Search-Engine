import React, { Component, useState,useEffect } from "react";
import { ReactDOM } from "react";
import { render } from "react-dom";
import {BrowserRouter,Route,Link} from "react-router-dom"
import "../../node_modules/bootstrap/dist/css/bootstrap.css";
import Result from "./Result";
import Spinner from "react-spinkit";
import { toBeEnabled } from "@testing-library/jest-dom/dist/matchers";

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
            pagesNumbersCount:0,
            searchTime:0,
        };
        componentDidMount(){
            this.setState({load:true});
            const d = new Date();
            console.log(d);
            var startMill,endMill,startSec,endSec;
            fetch("http://localhost:8080/api?query="+this.state.query).then(response=>{
                startMill=d.getMilliseconds() ;
                startSec=d.getSeconds() ;
                console.log("the start sec :"+startSec);
                console.log("the start mill :"+startMill);
                if(response.ok)
                {
                    console.log("ok");
                    return response.json();
                }
                else{
                this.setState({load:false});
                const d2 = new Date();
                endMill=d2.getMilliseconds();
                endSec=d2.getSeconds()*1000;
                var secs=Math.abs(endSec-startSec)*1000;
                var mills=Math.abs(endMill-startMill);
                this.setState({searchTime:(secs+mills)});

                }
            }).then(data=>{
                console.log(data);
                this.setState({Results:data.Results});
                this.setState({queryArray:data.queryArray});
                console.log(this.state.Results);
                console.log(this.state.queryArray);
                const Numbers=[];
                this.setState({pagesNumbersCount:Math.ceil(this.state.Results.length/this.state.ResultsPerPage)})
                for(let i=1;i<=this.state.pagesNumbersCount;i++)
                {
                    Numbers.push(i);
                }
                this.setState({PagesNumbers:Numbers});
                //Get the start 
                let LastIndex = this.state.CurrentPage*this.state.ResultsPerPage;
                let FirstIndex= LastIndex-this.state.ResultsPerPage;
                const Posts=this.state.Results.slice(FirstIndex,LastIndex);
                this.setState({PagePosts:Posts});
                this.setState({load:false});
                const d2 = new Date();
                console.log(d2);
                endMill=d2.getMilliseconds();
                endSec=d2.getSeconds();
                console.log("the end mill :"+endMill);
                console.log("the end sec :"+endSec);
                if(endSec===0)
                {
                    endSec=60;
                }
                var secs=Math.abs(endSec-startSec)*1000;
                console.log("the all secs :"+secs);
                var mills=Math.abs(endMill-startMill);
                this.setState({searchTime:(secs+mills)});
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
                    <p  className="text-center fs-5 mt-5" style={{color:"white"}}>the total search time is <i style={{color:"#0b95d4"}}>{this.state.searchTime}</i> ms</p>
                    {this.state.pagesNumbersCount>0&&<p className="text-center  fs-5" style={{color:"white"}}>The Number of Pages <i style={{color:"#0b95d4"}}>{this.state.pagesNumbersCount}</i>  and the Current Page is <i style={{color:"#0b95d4"}}>{this.state.CurrentPage}</i></p>}
                    {this.state.PagesNumbers.length>1&&<section aria-label="Page navigation example" style={{position:"relative","z-index":"1","padding-top":"40px"}}>
                    
                    <ul className="pagination justify-content-center mt-5">
                        <li className="page-item" style={{"cursor":"pointer"}}>
                        <a class="page-link" onClick={this.prePage}  PreventDefault>Previous</a>
                        </li>
                        {this.state.CurrentPage+9<this.state.pagesNumbersCount&&this.state.pagesNumbersCount>=10&&this.state.PagesNumbers.slice(this.state.CurrentPage,this.state.CurrentPage+9).map(PageNumber=>(<li style={{"cursor":"pointer"}} className={(this.state.CurrentPage==PageNumber)? " page-item active" :'"page-item"'} ><a className="page-link " onClick={()=>this.ChangePage(PageNumber)} PreventDefault>{PageNumber}</a></li>))}
                        {this.state.pagesNumbersCount<=10&&this.state.PagesNumbers.map(PageNumber=>(<li style={{"cursor":"pointer"}} className={(this.state.CurrentPage==PageNumber)? " page-item active" :'"page-item"'} ><a className="page-link " onClick={()=>this.ChangePage(PageNumber)} PreventDefault>{PageNumber}</a></li>))}
                        <li className="page-item" style={{"cursor":"pointer"}}>
                        <a className="page-link"  onClick={this.nextPage} PreventDefault>Next</a>
                        </li>
                    </ul>
                </section>}
                    <Result Posts={this.state.PagePosts} query={this.state.query} array={this.state.queryArray}/>
                </div>
                }

            </div>
        ) ;
    }
}
export default Results;