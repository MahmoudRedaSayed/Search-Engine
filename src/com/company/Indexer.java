package com.company;

import org.tartarus.snowball.ext.PorterStemmer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
    public void startIndexing()
    {
        titleProcessing();
        headingProcessing();
        paragraphProcessing();
    }

    // initialization
    private void initializeFile()
    {
        String letters = "qwertyuiopasdfghjklzxcvbnm";
        for (int i = 0; i < 26; i++){

            invertedFiles.put(letters.charAt(i), new File(HelperClass.invertedFilePath(letters.charAt(i))));
        }
    }

    // get Urls from Data Base
    private void getUrls()
    {
        // get it from database         <<< Karim >>>
    }

    // setter for the page           ( will be modified )
    public void setPage(String url) {
        try {
            page.parseDocument(url);
        } catch (IOException e) {
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
    private void singleStringProcessing(String str, char tag) // tag --> ('t' = page title, 'h' = "heading", 'p' = "paragraph)
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
            }

            // prepare the info of the word ( doc_id, position, paragraph or heading)
            wordInfo = tempWord;        // just for testing, will be changed later

            // insert the word into the file
            try {
                addToFile(tempWord, tempWord.charAt(0), wordInfo);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Title Processing
    private void titleProcessing()
    {
        String title = page.getTitleTag();

        singleStringProcessing(title, 'p');
    }

    // headings processing  ( h1, h2, h3 )
    private void headingProcessing()
    {
        String[] headings = page.getHeaders();

        for(String header : headings)
            singleStringProcessing(header, 'h');
    }

    // paragraph processing
    private void paragraphProcessing()
    {
        // for <p> tags
        String[] data = page.getParagraphs();
        for(String p : data)
            singleStringProcessing(p, 'p');

        // for <li>     ( list item  )
        data = page.getListItems();
        for(String li : data)
            singleStringProcessing(li, 'p');

        // for <td>     ( table data )
        data = page.getTableData();
        for(String li : data)
            singleStringProcessing(li, 'p');

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
    private String removeSymbols(String str)       // NOTE : pass str by ref
    {
        str = str.replaceAll("[:,!%;/]", "");  // replaced with a space, to use the space as a separator in splitting the string
        str = str.replaceAll("\\s+", "&");  // remove spaces
        return str;
    }

    // ######################################## Karim ##################################################
    // add to the file
    private void addToFile(String word, char fileName, String info) throws IOException  // fileName is the first letter
    {
        // insert into the file
        FileWriter write = new FileWriter(HelperClass.invertedFilePath(fileName));

        // check if the word is already exist in the file or not
        /*
        code
        */
        write.close();

    }

    // stem the word using Porter Stemmer Lib
    private String stemTheWord(String word)
    {
        stemObject.setCurrent(word);
        stemObject.stem();
        return stemObject.getCurrent();
    }
}
