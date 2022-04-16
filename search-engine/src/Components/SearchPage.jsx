import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import Navbar from './Navbar';
import SearchBlock from './SearchBlock';
import Background from './Background';
import { useParams,useNavigate } from 'react-router-dom';


function SearchPage (){
        return(

            <div>
                    <Background></Background>
                    <Navbar showField={false}/>
                    <SearchBlock></SearchBlock>
            </div>

        );
}
export default SearchPage