import React, { Component, useState,useEffect } from "react";
import "../../node_modules/bootstrap/dist/css/bootstrap.css";
import "../../node_modules/@fortawesome/fontawesome-free/css/all.css";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { fa4, faMicrophoneAlt,faMicrophoneAltSlash } from '@fortawesome/free-solid-svg-icons';
import {useNavigate} from "react-router-dom";
import SpeechRecognition, { useSpeechRecognition } from 'react-speech-recognition';

function SearchField(Style){
    const nav=useNavigate();
    const [historyData,setHistoryData]=useState([]);
    const [value,setValue]=useState("");
    const {
        transcript,
        listening,
        resetTranscript,
        browserSupportsSpeechRecognition
      } = useSpeechRecognition();

      // Every rerender
    useEffect(() => {
        if(listening==true)
        {
            console.log("inside use effect")
            if(transcript!="")
            {
            setValue(transcript);
            if(value.trim()!=="")
            {
            
                fetch(" http://localhost:8000/History").then(response=>{
                    if(response.ok)
                    {
                        return response.json();
                    }
                    }).then(data=>{
                    console.log(data);
                    console.log(value);
                    setHistoryData(data.filter
                        ((data)=>(data.search.toLocaleLowerCase().startsWith
                        (value.trim().toLowerCase()))));
                })
            }
            else{
                setHistoryData([]);
            }
        }

        }
    });
    
      if (!browserSupportsSpeechRecognition) {
        return <span>Browser doesn't support speech recognition.</span>;
      }
    function submit(e){
        if(e.code==="Enter"||e.code==="NumpadEnter")
        {
            e.preventDefault();
            console.log(
                "the enter key is pressed"
            );
            var found=false;
            if(!(e.target.value.trim()===""))
            {
                fetch(" http://localhost:8000/History").then(response=>{
                    if(response.ok)
                    {
                        return response.json();
                    }
                    }).then(data=>{
                    for(let i =0;i<data.length;i++)
                    {
                        console.log(data[i].search.toLowerCase()+" === "+e.target.value.trim().toLowerCase());
                        if(e.target.value.trim().toLowerCase()===data[i].search.toLowerCase())
                        {
                            found=true;
                            break;
                        }
                        
                    }
                    
                })
                setTimeout(()=>{if(!found)
                {
                    const doc=
                    {
                        search:e.target.value.trim()
                    }
                console.log(doc);
                fetch("http://localhost:8000/History",{
                    method:"POST",
                    body:JSON.stringify(doc),
                    headers:{"Content-Type":"application/json"}});
                    console.log("submitedd");
                    console.log(doc);
                }
                console.log("nava");
                nav("Results/"+value);
                console.log("nava2");
            },500)
                
            
            }

        }
        
    }
    function add(suggest){
        setTimeout(()=>{
        setValue(suggest);
        },);
        
    }   
    function inc(e){
        
        if(e.code!=="Enter"||e.code!=="NumpadEnter")
        {
            console.log("the function is ready");
        setValue(e.target.value);
        console.log(value);
        }
        
        if(e.target.value.trim()!=="")
        {
        
            fetch(" http://localhost:8000/History").then(response=>{
                if(response.ok)
                {
                    return response.json();
                }
                }).then(data=>{
                console.log(data);
                console.log(e.target.value);
                setHistoryData(data.filter
                    ((data)=>(data.search.toLocaleLowerCase().startsWith
                    (e.target.value.trim().toLowerCase()))));
            })
        }
        else{
            setHistoryData([]);
        }
    }

    function listen(e){
        e.preventDefault();
        console.log("testing");
        SpeechRecognition.startListening();
    }

    function cutListen()
    {
        SpeechRecognition.stopListening();
        listening=false;
        transcript="";
    }

    return(
        <div>
        <form >
        {(listening==true)?<div className="row ">
        <FontAwesomeIcon icon={faMicrophoneAltSlash}  onClick={cutListen} style={{"font-size":"40px","margin-bottom":"10px"}}/>

                                <input className="form-control me-2"  type="search"  
                                onKeyDown={submit}
                                onChange={inc}
                                value={value} 
                                placeholder="Search" aria-label="Search" 
                                style={Style.Style}
                                />
                                </div>
                                :<div className="row ">
                                <FontAwesomeIcon  icon={faMicrophoneAlt} onClick={listen} style={{"font-size":"40px","margin-bottom":"10px"}} />
                                <input className="form-control me-2 col-md-4 "  type="search"  
                                onKeyDown={submit}
                                onChange={inc}
                                value={value} 
                                placeholder="Search" aria-label="Search" 
                                style={Style.Style}
                                />
                                </div>
                                }
                                </form>
                                <div className="Slider mt-2 " style={{"border-radius":"10px" , "scrollbar-width": "none"}}>
                                    {historyData.map((record)=>{return (<p className="p-2" style={{"cursor":"pointer"}}  onClick={()=>add(record.search)}>{record.search}</p>)})}
                                </div>
    </div>
    );
}
export default SearchField;