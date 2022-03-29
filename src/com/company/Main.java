package com.company;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Date;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;

public class Main {

    public static void main(String[] args) {

        UrlThread ThreadsArray=new UrlThread();
        DataBase DataBaseObject=new DataBase();
        UrlThread.Limit+=DataBaseObject.getCompleteCount();
        System.out.printf(" the limit %d",UrlThread.Limit);
        ThreadsArray.setName("Thread1");
        ThreadsArray.start();
//        UrlThread ThreadsArray[]=new UrlThread[36];
//        for(int i=1;i<37;i++)
//        {
//            ThreadsArray[i] = new UrlThread();
//            ThreadsArray[i].setName("Thread" + i);
//            ThreadsArray[i].start();
//        }
    }
}
