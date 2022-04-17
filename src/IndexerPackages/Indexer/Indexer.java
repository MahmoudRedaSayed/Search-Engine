package IndexerPackages.Indexer;

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

    private PageParsing page;
    private String[] myInfo;        // to store the url and its id
    // private String[] Documents;
    private Map<String, File> invertedFiles;
    //private PorterStemmer stemmingObject;
    //private String[] stopWords;
    private Map<Character, Vector<String>> stopWords;

    // constructor
    public Indexer(String url, String urlId, WorkingFiles filesReference)
    {
        // initialization
        page = new PageParsing();
        stopWords = new HashMap<>();
        //invertedFiles = new HashMap<Character, File>();
        //stemmingObject = new PorterStemmer();
        /*initializeFiles();
        try {
            readStopWords();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open Stop words file");
            e.printStackTrace();
        }*/
        //stopWords     = filesReference.getStopWordsAsArr();
        stopWords     = filesReference.getStopWordsAsMap();
        invertedFiles = filesReference.getInvertedFiles();
        // set some needed info
        myInfo = new String[2];
        myInfo[0] = url;
        myInfo[1] = urlId;

        System.out.println("My Page is : " + url);
    }

    // running function
    public void run()
    {
        this.startIndexing(myInfo[0], myInfo[1]);
    }

    // Indexing Function ( the most important one )
    public void startIndexing(String url, String doc_id)
    {
        this.setPage(url);
        titleProcessing(doc_id);
        headingProcessing(doc_id);
        paragraphProcessing(doc_id);
    }

   /* // initialization of inverted files
    private void initializeFiles()
    {
        invertedFiles = new HashMap<String, File>();
        String letters = "qwertyuiopasdfghjklzxcvbnm";
        String currentFileName = "";

        for (int i = 0; i < 26; i++){
            currentFileName += letters.charAt(i);
            for (int j = 0; j < 26; j++)
            {
                currentFileName += letters.charAt(j);
                invertedFiles.put(currentFileName, new File(HelperClass.invertedFilePath(currentFileName)));
            }
        }
    }*/

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
 /*   private void readStopWords() throws FileNotFoundException {
        // open the file that contains stop words
        String filePath = System.getProperty("user.dir");   // get the directory of the project
        filePath += File.separator + "helpers" + File.separator + "stop_words.txt";
        File myFile = new File(filePath);

        //this.stopWords = new String[851];

        // read from the file
        Scanner read = new Scanner(myFile);
        String tempInput;
        int counter = 0;
        while(read.hasNextLine())
        {
            tempInput = read.nextLine();
            //stopWords[counter++] = tempInput;
        }
        read.close();

    }*/

    // String Processing
    private void singleStringProcessing(String str, char tag, String doc_ic)    // tag --> ('t' = page title, 'h' = "heading", 'p' = "paragraph)
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

            // insert the word into the file
            String fileName = "";
            fileName += tempWord.charAt(0) ;
            fileName += tempWord.charAt(1) ;
            try {
                addToFile(tempWord, fileName, wordInfo);

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
        // Headers
        String[] headings = page.getHeaders();

        for(String header : headings)
            singleStringProcessing(header, 'h', doc_id);

        // <strong>
        String[] strongs = page.getStrongs();

        for(String strong : strongs)
            singleStringProcessing(strong, 's', doc_id);

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
        /*int size = stopWords.length;

        for(int i = 0; i < size; i++)
            if (stopWords[i].equals(word))
                return true;

        return false;*/
        return stopWords.get(word.charAt(0)).contains(word);
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
    private synchronized void addToFile(String word, String fileName, String info) throws IOException  // fileName is the first letter
    {
        String filePath = System.getProperty("user.dir") + File.separator + "InvertedFiles_V2" + File.separator + fileName + ".txt";

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
                    // check whether the info is already existing in the file --> ex: info : 1,t .... in the file [1,t,3]
                    String theNewLine = updateInfoOfWord(tempInput, info);

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

    // this function checks if the info is already exist or not,
    // and if exists, just increment the counter of occurrences
    String updateInfoOfWord(String line, String oldInfo) {

        // substring the line to get the needed information
        int separationIndex = line.indexOf('|');
        String allInfo = line.substring(separationIndex + 1);

        // explode the info
        List<String> infoList = new ArrayList<>(List.of(allInfo.split(";", 0)));
        String theNewInfo;

        for (String info : infoList) {

            // split the frequency counter from the info of the word
            List<String> tempList = new ArrayList<>(List.of(info.split(":", 0)));

            // check if the same info is existing or not
            if (tempList.get(0).equals(oldInfo)) {
                String frequency = tempList.get(1);
                int integerFrequency = Integer.parseInt(frequency);
                theNewInfo = tempList.get(0) + ":" + String.valueOf(integerFrequency + 1); /* convert the ( int freq + 1 ) to string here */
                oldInfo = oldInfo + ":" + frequency;
                line = line.replace(oldInfo , theNewInfo);
                return line;
            }
        }

        // if not returned, then the info is not exist
        theNewInfo = oldInfo + ":1";
        line += theNewInfo + ';';
        return line;

    }

    /*// stem the word using Porter Stemmer Lib
    private String stemTheWord(String word)
    {
        stemmingObject.setCurrent(word);
        stemmingObject.stem();
        return stemmingObject.getCurrent();
    }*/
}
