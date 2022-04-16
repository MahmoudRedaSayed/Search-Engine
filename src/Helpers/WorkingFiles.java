package Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WorkingFiles {
    private Map<Character, File> invertedFiles;
    private String[] stopWords;

    public WorkingFiles()
    {
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
        invertedFiles = new HashMap<Character, File>();

        String letters = "qwertyuiopasdfghjklzxcvbnm";
        for (int i = 0; i < 26; i++){

            invertedFiles.put(letters.charAt(i), new File(HelperClass.invertedFilePath(letters.charAt(i))));
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
    public String[] getStopWords()
    {
        return stopWords;
    }

    // get inverted files
    public Map<Character, File> getInvertedFiles()
    {
        return invertedFiles;
    }
}
