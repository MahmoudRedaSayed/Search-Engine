package HelpersPackages.Helpers;

import org.tartarus.snowball.ext.PorterStemmer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HelperClass {


    // get the path of the inverted Files
    public static String invertedFilePath(String fileName)
    {
        String filePath = Paths.get("").normalize().toAbsolutePath().toString();
        filePath += File.separator + "InvertedFiles_V3" + File.separator + fileName + ".txt";
        return filePath;
    }

    // get the path of the inverted Files_V2
    public static String invertedFilePath_V2(String fileName)
    {
        String filePath = Paths.get("").normalize().toAbsolutePath().toString();
        filePath += File.separator + "InvertedFiles_V3" + File.separator + fileName + ".txt";
        return filePath;
    }

    // get the path of the inverted Files_V3
    public static String invertedFilePath_V3(String fileName)
    {
        String filePath = Paths.get("").normalize().toAbsolutePath().toString();
        filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
        filePath += File.separator + "InvertedFiles_V3" + File.separator + fileName + ".txt";
        return filePath;
    }

    // get the path of the inverted Files_V3 folder
    public static String invertedFilePathDirectoryPath()
    {
        String filePath = Paths.get("").normalize().toAbsolutePath().toString();
        filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
        filePath += File.separator + "InvertedFiles_V3";
        return filePath;
    }

    // get the path of the content length files
    public static String contentLengthFiles(String fileName)
    {
        String filePath = Paths.get("").normalize().toAbsolutePath().toString();
        filePath += File.separator + "ContentLength" + File.separator + fileName + ".txt";
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
            if (tempInput.equals(""))
                continue;

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

    // stem the word using Porter Stemmer Lib
    public static String stemTheWord(String word)
    {
        PorterStemmer stemObject = new PorterStemmer();
        stemObject.setCurrent(word);
        stemObject.stem();
        return stemObject.getCurrent();
    }

    // check if the word is arabic
    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }

    // this function checks if the info is already exist or not,
    // and if exists, just increment the counter of occurrences
    public static String updateInfoOfWord(String line, String oldInfo) {

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
}
