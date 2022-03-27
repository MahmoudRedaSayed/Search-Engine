package com.company;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;

public class Main {

    public static void main(MysqlxDatatypes.Scalar.String[] args) {

        UrlThread ThreadsArray[]=new UrlThread[36];
        for(int i=1;i<37;i++)
        {
            ThreadsArray[i] = new UrlThread();
            ThreadsArray[i].setName("Thread" + i);
            ThreadsArray[i].start();
        }
    }
}
