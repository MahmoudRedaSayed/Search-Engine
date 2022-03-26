package com.company;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;

public class Main {

    public static void main(MysqlxDatatypes.Scalar.String[] args) {
	// write your code here

        UrlThread ThreadsArray[]=new UrlThread[50];
        for(int i=0;i<36;i++)
        {
            ThreadsArray[i]=new UrlThread();
            ThreadsArray[i].setName("Thread"+i);
            ThreadsArray[i].start();
        }

    }
}
