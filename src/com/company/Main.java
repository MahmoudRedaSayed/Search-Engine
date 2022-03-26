package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here

        UrlThread ThreadsArray[]=new UrlThread[50];
        for(int i=0;i<50;i++)
        {
            ThreadsArray[i]=new UrlThread();
            ThreadsArray[i].setName("Thread"+i);
            ThreadsArray[i].start();
        }

    }
}
