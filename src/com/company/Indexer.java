package com.company;

import org.tartarus.snowball.ext.PorterStemmer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
// Karim --> database & multiThreading&removeSymbols
public class Indexer {

    private PageParsing page;
    private String[] Documents;
    private Map<Character, File> invertedFiles;
    private PorterStemmer stemObject;
    private String[] stopWords;

    // constructor
    public Indexer()
    {
        // initialization
        page = new PageParsing();
        invertedFiles = new HashMap<Character, File>();
        stemObject = new PorterStemmer();
        initializeFile();
        try {
            readStopWords();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open Stop words file");
            e.printStackTrace();
        }
        // get the URLs from Database, store it in this.Documents
        // Karim
    }

    // Indexing Function ( the most important one )
    public void startIndexing(String url, String doc_id)
    {
        this.setPage(url);
        titleProcessing(doc_id);
        headingProcessing(doc_id);
        paragraphProcessing(doc_id);
    }

    // initialization
    private void initializeFile()
    {
        String letters = "qwertyuiopasdfghjklzxcvbnm";
        for (int i = 0; i < 26; i++){

            invertedFiles.put(letters.charAt(i), new File(HelperClass.invertedFilePath(letters.charAt(i))));
        }
    }

    /*// get Urls from Data Base
    private void setUrls()
    {
        // get it from database
    }*/

    // setter for the page           ( will be modified )
    private void setPage(String url) {
        try {
            page.parseDocument(url);
        } catch (IOException e) {
            System.out.println("Error in setting the URL (Error Location : Class Indexer --> setPage function\n");
            e.printStackTrace();
        }
    }

    // read the stop words
    private void readStopWords() throws FileNotFoundException {
        // open the file that contains stop words
        String filePath = System.getProperty("user.dir");   // get the directory of the project
        filePath += File.separator + "helpers" + File.separator + "stop_words.txt";
        File myFile = new File(filePath);

        this.stopWords = new String[851];

        // read from the file
        Scanner read = new Scanner(myFile);
        String tempInput;
        int counter = 0;
        while(read.hasNextLine())
        {
            tempInput = read.nextLine();
            stopWords[counter++] = tempInput;
        }
        read.close();

    }

    // String Processing
    private void singleStringProcessing(String str, char tag, String doc_ic)    // tag --> ('t' = page title, 'h' = "heading", 'p' = "paragraph)
    {
        // remove stop words
        str = removeSymbols(str);

        // explode the title
        List<String> titleWords = new ArrayList<>(List.of(str.split("&", 0)));

        // stemming & storing into the file
        String wordInfo,tempWord;

        int size = titleWords.size();

        for (int i = 0; i < size; i++)
        {
            tempWord = titleWords.get(i);
            // stemming the words
            tempWord = stemTheWord(tempWord);

            // check if it is a stopping word
            if (isStopWord(tempWord))
            {
                titleWords.remove(i);
                i--;
                size--;
                continue;
            }

            // prepare the info of the word ( doc_id, paragraph or heading)
            wordInfo = "1:" + tag + ';';        // karim , set the doc_id

            // insert the word into the file
            try {
                addToFile(tempWord, tempWord.charAt(0), wordInfo);

            }
            catch (IOException e) {
                System.out.println("Error in adding ( "+ tempWord + '|' + wordInfo +" ) to its inverted file ");
                e.printStackTrace();
            }
        }
    }

    // Title Processing
    private void titleProcessing(String doc_id)
    {
        String title = page.getTitleTag();
        singleStringProcessing(title, 't', doc_id);
    }

    // headings processing  ( h1, h2, h3 )
    private void headingProcessing(String doc_id)
    {
        String[] headings = page.getHeaders();

        for(String header : headings)
            singleStringProcessing(header, 'h', doc_id);
    }

    // paragraph processing
    private void paragraphProcessing(String doc_id)
    {
        // for <p> tags
        String[] data = page.getParagraphs();
        for(String p : data)
            if(! p.equals(""))
                singleStringProcessing(p, 'p', doc_id);

        // for <li>     ( list item  )
        data = page.getListItems();
        for(String li : data)
            singleStringProcessing(li, 'p', doc_id);

        // for <td>     ( table data )
        data = page.getTableData();
        for(String li : data)
            singleStringProcessing(li, 'p', doc_id);

    }

    // checking whether stop word or not
    private boolean isStopWord(String word){
        int size = stopWords.length;

        for(int i = 0; i < size; i++)
            if (stopWords[i].equals(word))
                return true;

        return false;
    }

    // remove non-important symbols
    private String removeSymbols(String str)
    {
        // Karim --> more symbols [] \
        str = str.replaceAll("[~@#$%^&*(){}|+:,.!;/1234567890]", "");  // replaced with a space, to use the space as a separator in splitting the string
        str = str.replaceAll("\\s+", "&");  // remove spaces
        return str;
    }

    // add to the file
    // NOTE : info must be = doc_ic,h or p;    Karim --> this function must be synchronized
    private void addToFile(String word, char fileName, String info) throws IOException  // fileName is the first letter
    {
        String filePath = System.getProperty("user.dir") + File.separator + "InvertedFiles" + File.separator + fileName + ".txt";

        // check if the word is already exists or not
        File workingFile = this.invertedFiles.get(fileName);
        Scanner read = new Scanner(workingFile);
        String tempInput;
        while(read.hasNextLine())
        {
            tempInput = read.nextLine();

            // check if this line is for a word or just an extension for the previous line
            // System.out.println(tempInput);
            if (tempInput.charAt(0) == '/')
            // compare to check if this word = ourWord ?
            {
                // get the word
                tempInput = HelperClass.isExistingInFile(word, workingFile);

                if (! tempInput.equals(""))       // the word is already exists
                {
                    // replace this line in the file
                    Path path = Paths.get(filePath);
                    HelperClass.replaceLineInFile(path, tempInput, tempInput + info);

                }else               // then, this is the first time to add this word
                {
                    FileWriter myWriter = new FileWriter(filePath, true);   // true to activate the appending mode
                    myWriter.write('/' + word + '|' + info + '\n');
                    myWriter.close();
                }
            }
            return;
        }

        // if don't return, then the file was empty --> so this is the first line to insert in it
        FileWriter myWriter = new FileWriter(filePath);
        myWriter.write('/' + word + '|' + info + '\n');
        myWriter.close();
    }

    // stem the word using Porter Stemmer Lib
    private String stemTheWord(String word)
    {
        stemObject.setCurrent(word);
        stemObject.stem();
        return stemObject.getCurrent();
    }
}
