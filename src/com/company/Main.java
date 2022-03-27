package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
//        System.out.printf("from the main");

//        UrlThread ThreadsArray[]=new UrlThread[36];
        UrlThread ThreadsArray=new UrlThread();
        ThreadsArray.setName("Thread1");
        ThreadsArray.start();

//        for(int i=1;i<37;i++)
//        {
//            System.out.printf("loop main \n");
////            ThreadsArray[i]=new UrlThread();
////            ThreadsArray[i].setName("Thread"+i);
////            ThreadsArray[i].start();
//        }



    }
}
