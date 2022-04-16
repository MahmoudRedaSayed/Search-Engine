package com.company;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) {

        Thread ThreadsArray=new Thread(new UrlThread());
        Thread.currentThread().setName("Thread1");
        ThreadsArray.setName("Thread2");
        ThreadsArray.run();
////        Thread ThreadsArray2=new Thread(new UrlThread());
//        ThreadsArray2.setName("Thread2");
//        ThreadsArray2.start();
//        ArrayList<String> Disallowed =ThreadsArray.robotSafe("https://www.bbc.co.uk/");
//        System.out.println(Disallowed);
//        DataBase DataBaseObject=new DataBase();
//        UrlThread.Limit+=DataBaseObject.getCompleteCount();
//        System.out.printf(" the limit %d",UrlThread.Limit);

//        Thread ThreadsArray[]=new Thread[43];
//        ThreadsArray[0]=Thread.currentThread();
//        ThreadsArray[0].setName("Thread1");
//        for(int i=1;i<43;i++)
//        {
//            ThreadsArray[i] = new Thread(new UrlThread());
//            ThreadsArray[i].setName("Thread" + (i+1));
//            ThreadsArray[i].start();
//        }
//        for(int i=1;i<43;i++)
//        {
//
//            try {
//                ThreadsArray[i].join();
//            } catch (InterruptedException e) {
//
//            }
//        }

//        try {
//            ThreadsArray.join();
//            ThreadsArray2.join();
//
//        } catch (InterruptedException e) {
//
//            }
        System.out.println("The limit  "+UrlThread.Limit+" The inserted  "+UrlThread.inserted);
//
//

        ///////////////////////////////////////////////////////

    }
}
