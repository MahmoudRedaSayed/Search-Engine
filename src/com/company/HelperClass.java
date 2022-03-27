package com.company;

import java.io.File;

public class HelperClass {


    public static String invertedFilePath(char fileName)
    {
        String filePath = System.getProperty("user.dir");   // get the directory of the project
        filePath += File.separator + "InvertedFiles" + File.separator + fileName + ".txt";
        return filePath;
    }


}
