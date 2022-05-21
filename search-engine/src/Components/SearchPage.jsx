import React,  { Component, useState,useEffect }  from 'react';
import ReactDOM from 'react-dom';
import Navbar from './Navbar';
import SearchBlock from './SearchBlock';
import Background from './Background';
import { useParams,useNavigate } from 'react-router-dom';


function SearchPage (){
        const [counter,setCounter]=useState(0);

                if(counter==0)
                {
                fetch("http://localhost:8080/api?query= ").then(response=>{
                if(response.ok)
                {
                        console.log("ok");
                        return response.json();
                }
                }).then(data=>{
                })
                setCounter(counter+1);
                }
        
        return(

            <div>
                    <Background></Background>
                    <Navbar showField={false}/>
                    <SearchBlock></SearchBlock>
            </div>

        );
}
export default SearchPage