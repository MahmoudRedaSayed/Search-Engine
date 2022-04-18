package HelpersPackages.Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class WorkingFiles {
    private Map<String, File> invertedFiles;
    private String[] stopWords;
    private Map<String, File> pageContentFiles;
    public WorkingFiles(int countOfPageContentFiles)
    {
        // create the files of pages content
        String path = "";
        for (int i = 1; i <= countOfPageContentFiles; i++)
        {
            path = HelperClass.pageContentFilesPath(String.valueOf(i));
            File myObj = new File(path);
            try {
                myObj.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to create the file");
            }
        }
        // page content files
        createPagesContentFiles(countOfPageContentFiles);
        // inverted files
        initializeFiles();

        // stop words
        try {
            readStopWords();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open Stop words file");
            e.printStackTrace();
        }


    }

    // initialization of inverted files
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
                invertedFiles.put(currentFileName, new File(HelperClass.invertedFilePath_V2(currentFileName)));
                currentFileName = "";
                currentFileName += letters.charAt(i);
            }
        }
    }

    // initialization of page content files
    private void createPagesContentFiles(int count)
    {
        // initialization map of the files
        pageContentFiles = new HashMap<String,File>();

        for (int i = 1; i <= count; i++)
        {
            pageContentFiles.put(String.valueOf(i), new File(HelperClass.pageContentFilesPath(String.valueOf(i))));
        }
    }

    // read the stop words
    private void readStopWords() throws FileNotFoundException {
        // open the file that contains stop words
        String filePath = System.getProperty("user.dir");   // get the directory of the project
        filePath += File.separator + "helpers" + File.separator + "stop_words.txt";
        File myFile = new File(filePath);

        stopWords = new String[851];

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

    // get stop words
    public String[] getStopWordsAsArr()
    {
        return stopWords;
    }

    // get stop words
    public Map<Character, Vector<String>> getStopWordsAsMap()
    {
        // hold stop words in arr
        String[] myStopWords = this.getStopWordsAsArr();

        // creating Map
        Map<Character, Vector<String>> wordsMap = new HashMap<>();
        String letters = "qwertyuiopasdfghjklzxcvbnm'";
        // initialize map
        for (int i = 0; i < 27; i++){

            wordsMap.put(letters.charAt(i), new Vector<String>());
        }

        // fill the map
        int x = 0;
        for (String word : myStopWords)
        {
            if (wordsMap.get(word.charAt(0)) != null)
                wordsMap.get(word.charAt(0)).add(word);
        }

        return wordsMap;
    }

    // get inverted files
    public Map<String, File> getInvertedFiles()
    {
        return invertedFiles;
    }

    // add to page content file
    public void addToPageContentFile(String fileName, String content)
    {
        FileWriter myWriter = null;

        try {
            myWriter = new FileWriter(HelperClass.pageContentFilesPath(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            myWriter.write(content);
            System.out.println("Successfully added the content to the file " + fileName +".txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to add the content to the file " + fileName +".txt");
        }

        try {
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
