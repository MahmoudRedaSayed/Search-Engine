package HelpersPackages.Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WorkingFiles {
    private static String[] stopWords;

    // initialization of inverted files
    public static void createInvertedFiles()
    {
        String letters = "qwertyuiopasdfghjklzxcvbnm";
        String currentFileName = "";

        for (int i = 0; i < 26; i++){
            for (int j = 0; j < 26; j++)
            {
                for(int k = 0; k < 26; k++)
                {
                    currentFileName = "_";
                    currentFileName += letters.charAt(i);
                    currentFileName += letters.charAt(j);
                    currentFileName += letters.charAt(k);

                    String path = HelperClass.invertedFilePath_V3(currentFileName);
                    File myObj = new File(path);
                    try {
                        myObj.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Failed to create the file");
                    }
                    currentFileName = "";
                }

            }
        }

        // create a file for two-letter words
        currentFileName = "two";
        String path = HelperClass.invertedFilePath_V3(currentFileName);
        File myObj = new File(path);
        try {
            myObj.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to create the file");
        }

        // create a file for Arabic words
        currentFileName = "arabic";
        path = HelperClass.invertedFilePath_V3(currentFileName);
        File myObj_2 = new File(path);
        try {
            myObj_2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to create the file");
        }

        // print
        System.out.println("Inverted Files Created Successfully");
    }

    // read the stop words
    private static void readStopWords() throws FileNotFoundException {
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
    public static Map<Character, Vector<String>> getStopWordsAsMap()
    {
        try {
            readStopWords();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to read the stop words");
            e.printStackTrace();
        }
        // hold stop words in arr
        String[] myStopWords = stopWords;

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

}
