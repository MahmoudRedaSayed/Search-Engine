package IndexerPackages.Indexer;

import DataBasePackages.DataBase.DataBase;
import HelpersPackages.Helpers.HelperClass;
import HelpersPackages.Helpers.WorkingFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Indexer implements Runnable {

    private int urlID;
    private Map<Character, Vector<String>> stopWords;
    DataBase myDB;
    File workingFile;

    // constructor
    public Indexer(int urlId, DataBase dbObjReference)
    {
        System.out.println("My Page ID is : " + urlId);

        // initialization
        stopWords = WorkingFiles.getStopWordsAsMap();
        myDB = dbObjReference;
        this.urlID = urlId;
    }

    // running function
    public void run()
    {
        this.startIndexing(urlID);
    }

    // Indexing Function
    public void startIndexing(int doc_id)
    {
        titleProcessing(doc_id);
        headingProcessing(doc_id);
        paragraphProcessing(doc_id);
    }

    // String Processing
    private void singleStringProcessing(String str, char tag, int doc_ic)    // tag --> ('t' = page title, 'h' = "heading", 'p' = "paragraph)
    {
        // remove stop words
        str = removeSymbols(str);

        // explode the title
        List<String> stringWords = new ArrayList<>(List.of(str.split("&", 0)));

        // stemming & storing into the file
        String wordInfo,tempWord;

        int size = stringWords.size();

        for (int i = 0; i < size; i++)
        {
            tempWord = stringWords.get(i);
            // stemming the words
            //tempWord = HelperClass.stemTheWord(tempWord);

            // check if it is a stopping word
            if (isStopWord(tempWord))
            {
                stringWords.remove(i);
                i--;
                size--;
                continue;
            }

            // prepare the info of the word ( doc_id, paragraph or heading)
            wordInfo = "[" + doc_ic + "," + tag + ']';

            // <--- inserting the word into the file ---->
            String fileName = "";
            // first, preparing the file name based on the word
            if(HelperClass.isProbablyArabic(tempWord))
            {
                fileName = "arabic";
            }
            else if(tempWord.length() > 2)
            {
                fileName = "_";
                fileName += tempWord.charAt(0) ;
                fileName += tempWord.charAt(1) ;
                fileName += tempWord.charAt(2) ;
            }else if (tempWord.length() == 2)
            {
                fileName = "two";
            }

            // second, inserting the word
            try {
                // here we need the link of the server path     ( Mustafa )
                String filePath = HelperClass.invertedFilePath_V3(fileName);
                addInfoToInvertedFile(tempWord, filePath, wordInfo);
            }
            catch (IOException e) {
                System.out.println("Error in adding ( "+ tempWord + '|' + wordInfo +" ) to its inverted file ");
            }
        }
    }

    // Title Processing
    private void titleProcessing(int doc_id)
    {
        String title = myDB.getTitle(doc_id);
        singleStringProcessing(title, 't', doc_id);
    }

    // headings processing  ( h1, h2, h3 )
    private void headingProcessing(int doc_id)
    {
        // Headers
        String headers = myDB.getHeaders(doc_id);
        singleStringProcessing(headers, 'h', doc_id);

        // <strong>
        String strongs = myDB.getStrongs(doc_id);
        singleStringProcessing(strongs, 's', doc_id);

    }

    // paragraph processing
    private void paragraphProcessing(int doc_id)
    {
        // for <p> tags
        String data = myDB.getParagraphs(doc_id);
        singleStringProcessing(data, 'p', doc_id);
    }

    // checking whether stop word or not
    private boolean isStopWord(String word){
        try {
            return stopWords.get(word.charAt(0)).contains(word);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    // remove non-important symbols
    private String removeSymbols(String str)
    {
        str = str.replaceAll("[~@#$%^&*(){}|+:,.!;/1234567890]", "");  // replaced with a space, to use the space as a separator in splitting the string
        str = str.replaceAll("\\s+", "&");  // remove spaces
        return str;
    }

    // add to the file
    private synchronized void addInfoToInvertedFile(String word, String filePath, String info) throws IOException
    {
        this.workingFile = new File(filePath);

        // if the file is not exist
        if (this.workingFile == null)
        {
            System.out.println("Failed to add ^" + word + "^ to the inverted file file");
            return;
        }

        Scanner read = new Scanner(this.workingFile);
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
                    // check whether the info is already existing in the file --> ex: info : 1,t .... in the file [1,t,3]
                    String theNewLine = HelperClass.updateInfoOfWord(tempInput, info);

                    // replace this line in the file
                    Path path = Paths.get(filePath);
                    HelperClass.replaceLineInFile(path, tempInput, theNewLine);

                }else               // then, this is the first time to add this word
                {
                    FileWriter myWriter = new FileWriter(filePath, true);   // true to activate the appending mode
                    myWriter.write('/' + word + '|' + info + ":1;" + '\n');
                    myWriter.close();
                }
            }
            return;
        }

        // if don't return, then the file was empty --> so this is the first line to insert in it
        FileWriter myWriter = new FileWriter(filePath);
        myWriter.write('/' + word + '|' + info + ":1;" + '\n');
        myWriter.close();
    }

}
