import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import Navbar from './Navbar';
import SearchBlock from './SearchBlock';
import Background from './Background';


class SearchPage extends Component{
    render(){
        return(

            <div>
                    <Background></Background>
                    <Navbar showField={false}/>
                    <SearchBlock></SearchBlock>
            </div>

        );
    }
}
export default SearchPage