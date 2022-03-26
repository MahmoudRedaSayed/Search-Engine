package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Indexer {

    PageParsing page;
    String[] Documents;
    Map<Character, File> invertedFiles;
    // constructor
    public Indexer()
    {
        page = new PageParsing();
        invertedFiles = new HashMap<Character, File>();

    }

    private void initializeFile()
    {
        invertedFiles.put('a', new File("a.txt"));
    }
    // get Urls from Data Base
    /*String[] getUrls()
    {
        Documents =
    }*/

    // setter for the page           ( will be modified )
    public void setPage(String url) {
        try {
            page.parseDocument(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // title Processing
    public void prepareTitle()
    {
        String title = page.getTitleTag();

        // remove stop words
        title = removeSymbols(title);
        try {
            title = removeStopWords(title);
        } catch (FileNotFoundException e) {
            System.out.println("File is not exist\n");
            e.printStackTrace();
        }

        // explode the title
        String[] titleWords = title.split(" ", 0);

        // stemming the words

        // store the words to its inverted file

    }

    // remove stop words
    private String removeStopWords(String text) throws FileNotFoundException {

        // open the file that contains stop words
        String filePath = System.getProperty("user.die");   // get the directory of the project
        filePath += filePath + File.separator + "helpers" + File.separator + "stop_words.txt";
        File myFile = new File(filePath);

        // read from the file
        Scanner read = new Scanner(myFile);
        String tempInput;
        while(read.hasNextLine())
        {
            tempInput = read.nextLine();
            text = text.replace(tempInput, ""); //remove the stop words
        }
        read.close();
        return text;
    }

    // remove non-important symbols
    private String removeSymbols(String str)       // NOTE : pass str by ref
    {
        str = str.replaceAll("[:,!%;/]", " ");  // replaced with a space, to use the space as a separator in splitting the string
        return str;
    }

    // add to the file
    private void addToFile(String word, File fileName)
    {
        // open the file
        String filePath = System.getProperty("user.die");   // get the directory of the project
        filePath += filePath + File.separator + "InvertedFiles" + File.separator + "stop_words.txt";
        File myFile = new File(filePath);

        // insert into the file

    }
}
