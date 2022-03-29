import React, { Component, useState,useEffect } from "react";
import "../../node_modules/bootstrap/dist/css/bootstrap.css";
import "../../node_modules/@fortawesome/fontawesome-free/css/all.css";

class SearchBlock extends Component{
    state={
        historyData:[],
        value:""
    }
    fileration(data){
        return(data.search.toString().toLocaleLowerCase().startsWith(this.state.value));
    }
    submit=(e)=>{
        if(!(e.target.value===""))
        {
            const doc=
            {
                search:e.target.value
            }
            console.log(doc);
            fetch(" http://localhost:3000/History",{
                method:"POST",
                body:JSON.stringify(doc),
                headers:{"Content-Type":"application/json"}});
            }
        
    }
    add=(suggest)=>{
        setTimeout(()=>{
        this.setState({value:suggest});
        },);
        
    }   
    inc=(e)=>{
        this.setState({value:e.target.value});
        if(e.target.value!=="")
        {
        fetch(" http://localhost:3000/History").then(response=>{
                if(response.ok)
                {
                    return response.json();
                }
            }).then(data=>{
                console.log(e.target.value);
                this.setState({historyData:data.filter
                    ((data)=>(data.search.toLocaleLowerCase().startsWith
                    (e.target.value.toLowerCase())))});
                
         } )
        }
        else{
            this.setState({historyData:[]});
        }

    }
    render(){

        return(
            <div className="container d-flex justify-content-center align-items-center " style={{"background-color": "transparent"}}>
                <div className="card mb-3 center-block" style={{"margin-top": "20%","width": "100%","border":"none","background-color": "transparent"}}>
                    <div className="row g-0">
                        <div className=" col-md-12">
                            <h1 className="text-center"style={{"color":"#198754","font-size":"5rem"}} >K3M</h1>
                            <div className="card-body">
                                <form onKeydown={this.submit} >
                                <input className="form-control me-2"  type="search" onClick={this.submit} 
                                value={this.state.value} 
                                placeholder="Search" aria-label="Search" 
                                style={{"padding": "20px","border-radius": "30px"
                                ,"font-size": "21px","background-color": "#fff"
                                ,"border": "none","color":"#212529"}} 
                                onChange={this.inc} />
                                </form>
                                <div className="Slider mt-2 " style={{"border-radius":"10px" , "scrollbar-width": "none"}}>
                                    {this.state.historyData.map((record)=>{return (<p className="p-2" style={{"cursor":"pointer"}}  onClick={()=>this.add(record.search)}>{record.search}</p>)})}
                                </div>
                            </div>
                        </div>
                </div>
            </div>
            </div>
        )
    }
}
export default SearchBlock;