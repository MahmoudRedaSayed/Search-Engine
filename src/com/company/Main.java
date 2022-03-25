package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        DataBase obj=new DataBase();
        UrlThread obj2=new UrlThread("https://www.koimoi.com/bollywood-news/shah-rukh-khans-film-with-atlee-kumar-has-a-major-update-its-related-to-nayanthara/");
        obj2.start();

    }
}
