package IndexerPackages.Indexer;
//<--------------------- Import -------------------------------------------->
// data base
import DataBasePackages.DataBase.DataBase;
// Helpers
import HelpersPackages.Helpers.HelperClass;
import HelpersPackages.Helpers.WorkingFiles;
// Files
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Indexer implements Runnable {

    private String url;
    private Map<Character, Vector<String>> stopWords;
    DataBase myDB;
    File workingFile;
    int wordCount;

    // constructor
    public Indexer(String url, Map<Character, Vector<String>> stopWords, DataBase dbObjReference)
    {
        System.out.println(Thread.currentThread().getName() + " My Page is : " + url);

        // initialization
        wordCount = 0;
        this.stopWords = stopWords;
        myDB = dbObjReference;
        this.url = url;
    }

    // running function
    public void run()
    {
        this.startIndexing(url);
    }

    // Indexing Function
    public void startIndexing(String url)
    {
        titleProcessing(url);
        headingProcessing(url);
        paragraphProcessing(url);
        descriptionProcessing(url);

        // add word count to the database
        myDB.addWordsCount(url, wordCount);

        // add content to the file
        int id = myDB.getID(url);
        String content = myDB.getContent(id);
        WorkingFiles.addToContentToFile(id, content);
    }

    // String Processing
    private void singleStringProcessing(String str, char tag, String url)    // tag --> ('t' = page title, 'h' = "heading", 'p' = "paragraph)
    {
        if (str == null)
            return;

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
            tempWord = tempWord.trim();
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
            wordInfo = "[" + url + "," + tag + ']';

            // <--- inserting the word into the file ---->
            String fileName = "";
            // first, preparing the file name based on the word
            if(HelperClass.isProbablyArabic(tempWord))
            {
                fileName = "arabic";
            }
            else if (tempWord.length() == 2)
            {
                fileName = "two";
            }
            else if(tempWord.length() > 2)
            {
                fileName = "_";
                fileName += tempWord.charAt(0) ;
                fileName += tempWord.charAt(1) ;
                fileName += tempWord.charAt(2) ;

                // if the word is something like that => UK's
                File tempFile = new File(HelperClass.invertedFilePath_V3(fileName));
                if (! tempFile.exists())
                {
                    fileName = "others";
                }
            }

            // second, inserting the word
            String filePath = null;
            try {
                // add the word to its file
                removeSymbols(tempWord);
                filePath = HelperClass.invertedFilePath_V3(fileName);
                addInfoToInvertedFile(tempWord, filePath, wordInfo);
                wordCount++;
            }
            catch (IOException e) {
                System.out.println("Error in adding ( "+ tempWord + '|' + wordInfo +" ) to its inverted file ");
                System.out.println("Path : " + filePath);
                System.out.println("Lentgth = " + tempWord.length());
                continue;
            }
        }
    }

    // Title Processing
    private void titleProcessing(String url)
    {
        String title = myDB.getTitle(url);

        if (title == null || title.equals("[]"))
            return;

        // indexing step
        singleStringProcessing(title, 't', url);
    }

    // headings processing  ( h1, h2, h3 )
    private void headingProcessing(String url)
    {
        // Headers
        String headers = myDB.getHeaders(url);

        if (headers == null || headers.equals("[]"))
            return;

        // indexing
        singleStringProcessing(headers, 'h', url);

        // <strong>
        String strongs = myDB.getStrongs(url);

        // indexing
        singleStringProcessing(strongs, 's', url);

    }

    // paragraph processing
    private void paragraphProcessing(String url)
    {
        // for <p> tags
        String data = myDB.getParagraphs(url);

        if (data == null || data.equals("[]"))
            return;

        // indexing
        singleStringProcessing(data, 'p', url);
    }
    // paragraph processing
    private void descriptionProcessing(String url)
    {
        String data = myDB.getDescription(url);

        if (data == null || data.equals("[]"))
            return;

        // indexing
        singleStringProcessing(data, 'p', url);
    }

    // checking whether stop word or not
    private boolean isStopWord(String word){
        try {
            if (word.charAt(0) >= 'A' && word.charAt(0) <= 'z')
            {
                return stopWords.get(word.charAt(0)).contains(word);
            }else
                return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    // remove non-important symbols
    private String removeSymbols(String str)
    {
        str = str.replaceAll("[\\[`~@#$%^&*(\")“\\-{}|_=<–>+:,.!;?'”/1234567890\\]]", "");  // replaced with a space, to use the space as a separator in splitting the string
        str = str.replaceAll("[؟.,ٍـ،/ًٌَُ‘÷×؛’ْ~]", "");
        str = str.replaceAll("\\\"","\"");
        str = str.replaceAll("\\\'","\'");
        str = str.replaceAll(" ", "");  // remove spaces
        str = str.replaceAll("\\s+", "&");  // remove another type of the spaces

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
            System.out.println("Paht : " + filePath);
            return;
        }

        Scanner read = new Scanner(this.workingFile);
        String tempInput;

        while(read.hasNextLine())
        {
            tempInput = read.nextLine();

            // check if this line is for a word or just an extension for the previous line
            // System.out.println(tempInput);
            if (tempInput.charAt(0) == '<')
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
                    myWriter.write('<' + word + '|' + info + "::1;" + '\n');
                    myWriter.close();
                }
            }
            return;
        }

        // if don't return, then the file was empty --> so this is the first line to insert in it
        FileWriter myWriter = new FileWriter(filePath);
        myWriter.write('<' + word + '|' + info + "::1;" + '\n');
        myWriter.close();
    }

}
