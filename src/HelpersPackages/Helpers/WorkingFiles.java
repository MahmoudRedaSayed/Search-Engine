package HelpersPackages.Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WorkingFiles {
    private static String[] stopWords;

    // Creation of inverted files
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
            System.out.println("Failed to create the file Arabic.txt");
        }

        // create a file for others words ( uk's )
        currentFileName = "others";
        path = HelperClass.invertedFilePath_V3(currentFileName);
        File myObj_3 = new File(path);
        try {
            myObj_3.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to create the file others.txt");
        }

        // print
        System.out.println("Inverted Files are Created Successfully");
    }

    // Creation of content length files
    public static void createPageLengthFiles(int count)
    {
        for(int k = 1; k <= count; k++)
        {

            String path = HelperClass.contentLengthFiles(String.valueOf(k));
            File myObj = new File(path);
            try {
                myObj.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to create the file");
            }
        }
    }

    //--------------------------Function readStopWords--------------------------//
    /*
        * Explanation:
            Utility Function to divide the search query into the words constituting it
    */
    public static void readStopWords() throws FileNotFoundException {
        // open the file that contains stop words
        String filePath = "F:\\Current\\Sreach-Engine";   // get the directory of the project
        //filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
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

    //get Stop Words as Array
    public static String[] getStopWordsAsArray()
    {
        return stopWords;
    }

    // get stop words as Map
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

    // add the passed count to the file with name id.txt
    public static void addToContentLengthFile(String url, int count)
    {
        String path = HelperClass.contentLengthFiles(url);
        System.out.println(path);
        File targetFile = new File(path);
        try {
            targetFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Failed to create this file -->" + url + ".txt");
        }

        // if don't return, then the file was empty --> so this is the first line to insert in it
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(path, false);// false to re-new the content not append
        } catch (IOException e) {
            System.out.println("this file (" + url + ".txt) is not found");
            return;
        }
        try {
            myWriter.write(String.valueOf(count));
        } catch (IOException e) {
            System.out.println("error in writting the words count to the file");
            return;
        }
        try {
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Can't close the file");
            return;
        }
    }

    // add the content of the webPage to its file
    public static void addToContentToFile(int pageID, String content)
    {
        String path = HelperClass.contentFilesPath();
        path += File.separator + pageID + ".txt";

        File targetFile = new File(path);
        try {
            targetFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Failed to create this file -->" + pageID + ".txt");
        }


        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(path);// false to re-new the content not append
        } catch (IOException e) {
            System.out.println("this file (" + pageID + ".txt) is not found");
            return;
        }
        try {
            myWriter.write(content);
        } catch (IOException e) {
            System.out.println("error in writting the content to the file");
            return;
        }
        try {
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Can't close the file");
            return;
        }
    }



    public static void addPopularityToFile(int pageID, double value)
    {
        String path = HelperClass.populairtyFilesPath();
        path += File.separator + pageID + ".txt";

        File targetFile = new File(path);
        try {
            targetFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Failed to create this file -->" + pageID + ".txt");
        }


        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(path);// false to re-new the content not append
        } catch (IOException e) {
            System.out.println("this file (" + pageID + ".txt) is not found");
            return;
        }
        try {
            myWriter.write(String.valueOf(value));
        } catch (IOException e) {
            System.out.println("error in writting the content to the file");
            return;
        }
        try {
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Can't close the file");
            return;
        }
    }

    // add the description of the webPage to its file
    public static void addToDescriptionToFile(int pageID, String desc)
    {
        String path = HelperClass.descriptionFilesPath();
        path += File.separator + pageID + ".txt";

        File targetFile = new File(path);
        try {
            targetFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Failed to create this file -->" + pageID + ".txt");
        }


        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(path);// false to re-new the content not append
        } catch (IOException e) {
            System.out.println("this file (" + pageID + ".txt) is not found");
            return;
        }
        try {
            myWriter.write(desc);
        } catch (IOException e) {
            System.out.println("error in writting the description to the file");
            return;
        }
        try {
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Can't close the file");
            return;
        }
    }

    // remove the empty files after finishing indexing
    public static void removeEmptyFiles()
    {
        File targetFolder = new File(HelperClass.invertedFilePathDirectoryPath());
        File[] allFiles = targetFolder.listFiles();

        for (File currentFile : allFiles)
        {
            if (currentFile.length() == 0)
                currentFile.delete();
        }
    }

}
