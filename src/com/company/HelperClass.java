package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HelperClass {


    // get the path of the inverted Files
    public static String invertedFilePath(char fileName)
    {
        String filePath = System.getProperty("user.dir");   // get the directory of the project
        filePath += File.separator + "InvertedFiles" + File.separator + fileName + ".txt";
        return filePath;
    }

    // check if a given word is existing in a given inverted file or not
    // returns the whole line that contains this word
    public static String isExistingInFile(String word, File myFile) throws IOException {
        Scanner read = new Scanner(myFile);
        String tempInput;

        while(read.hasNextLine())
        {
            tempInput = read.nextLine();

            // check if this line is for a word or just an extension for the previous line
            if (tempInput.charAt(0) == '/')
            // compare to check if this word = ourWord ?
            {
                // get the word
                int wordSize = word.length();
                char ch = tempInput.charAt(1);      // just initialization
                boolean matchingFlag = true;

                int i;
                for (i = 0; i < wordSize; i++)
                    if(tempInput.charAt(i+1) != word.charAt(i))
                        break;

                if(i == wordSize)
                    return tempInput;
            }
        }
        return "";      // if not found, return empty
    }

    // this function replaces a line in a given inverted file
    public static void replaceLineInFile(Path path, String oldLine, String newLine) throws IOException {
        List<String>fileContents = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
        int ContentSize = fileContents.size();

        for (int i = 0; i < ContentSize; i++)
        {
            if(fileContents.get(i).equals(oldLine)) {
                fileContents.set(i, newLine);
                break;
            }
        }
        Files.write(path, fileContents, StandardCharsets.UTF_8);
    }




}
