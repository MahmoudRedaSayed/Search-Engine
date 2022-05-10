
package ServletsPackages.ServletPackage;


import HelpersPackages.Helpers.HelperClass;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class QueryDivide extends HttpServlet {
    public String results;
    public String searchingQuery;
    public ArrayList<String> rankerArray=new ArrayList<String>();
    public JSONArray dividedQuery=new JSONArray();
    int count=0;
    public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
    {
        res.addHeader("Access-Control-Allow-Origin","*");
        res.setContentType("text/html");
        System.out.println("print in query divide");
        String searchingQuery = req.getParameter("query");
        if(count==0)
        {
            QueryProcessing obj = new QueryProcessing();
            try {
                results  =obj.run(searchingQuery,rankerArray,dividedQuery);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
            count++;
        }

        res.getWriter().println(dividedQuery.toString());
    }

    static class QueryProcessing{

        //--------------------- The Data Members-------------------------//
        WorkingFiles workingFilesObject;
        DataBasePackages.DataBase.DataBase dataBaseObject;
        public String[] stopWords;

        //--------------------- Constructor-----------------------------//
    /*
        * Explanation:
            Constructor to initialize the object of the Database
            Read Stop Words to use in further functions
    */
        public QueryProcessing() throws FileNotFoundException {
            workingFilesObject = new WorkingFiles();
            WorkingFiles.readStopWords();
            dataBaseObject = new DataBasePackages.DataBase.DataBase();
            this.stopWords = workingFilesObject.getStopWordsAsArray();
            System.out.println("The consturctor");

        }

        //--------------------------Function SplitQuery--------------------------//
    /*
        * Explanation:
            Utility Function to divide the search query into the words constituting it
    */
        private String[] SplitQuery(String searchQuery)
        {
            String[] subStrings = searchQuery.trim().split("\\s+");
            return subStrings;
        }


        //--------------------------Function removeElement--------------------------//
    /*
        * Explanation:
            Utility Function for removeStopWords, used to remove elements from array
    */
        private static String[] removeElement(String[] arr, int[] index) {
            List<String> list = new ArrayList<>(Arrays.asList(arr));
            for (int i=0; i<index.length;i++)
            {
                list.remove(new String(arr[index[i]]));
            }
            return list.toArray(String[]::new);
        }

        //--------------------------Function removeStopWords--------------------------//
    /*
        * Explanation:
            Function used to remove all stop words from the Search Query
    */
        private String[] removeStopWords(String[] searchQuery)
        {
            int length =searchQuery.length;
            ArrayList<Integer> indeces = new ArrayList<Integer>();
            for(int i = 0; i< length; i++)
            {
                System.out.println(searchQuery[i].toLowerCase());
                if (Arrays.asList(this.stopWords).contains(searchQuery[i].toLowerCase()))
                {
                    indeces.add(i);
                }
            }
            searchQuery = removeElement(searchQuery, indeces.stream().mapToInt(Integer::intValue).toArray());
            return searchQuery;
        }


        //--------------------------Function searchInInvertedFiles--------------------------//
    /*
        * Explanation:
            Function used to search inverted Files,to fetch results for Search Queries.
    */
        public static void searchInInvertedFiles(String word, File myFile, ArrayList<String> results, boolean stemmingFlag) throws FileNotFoundException {
            Scanner read = new Scanner(myFile);
            String tempInput,
                    stemmedVersion = " ";

            // stemming the word
            if (stemmingFlag)
                stemmedVersion = HelpersPackages.Helpers.HelperClass.stemTheWord(word);

            boolean wordIsFound = false;

            int stopIndex, counter;

            results.add(0, "");     // if the targeted word is not found, replace empty in its index
            while(read.hasNextLine())
            {
                tempInput = read.nextLine();
                if (tempInput.equals(""))
                    continue;

                // check if this line is for a word or just an extension for the previous line
                if (tempInput.charAt(0) == '/')
                // compare to check if this tempWord = ourWord ?
                {
                    // extract the word from the line that read by the scanner
                    stopIndex = tempInput.indexOf('|');
                    String theWord = tempInput.substring(1, stopIndex);

                    // this condition for the targeted word
                    if(!wordIsFound && theWord.equals(word.toLowerCase()))
                    {
                        results.set(0, tempInput);     // target word will have the highest priority
                        wordIsFound = true;
                        continue;
                    }

                    counter = 1;
                    // comparing the stemmed version of the target word by the stemmed version of the word in the inverted file
                    if (stemmingFlag)
                    {
                        if (stemmedVersion.equals(HelpersPackages.Helpers.HelperClass.stemTheWord(theWord)))
                            results.add(counter++, tempInput);
                    }
                }
            }
        }

        //--------------------------Function sortByValue--------------------------//
    /*
        * Explanation:
            Static Function to sort a map descendingly by its values
    */

        public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm)
        {
            // Create a list from elements of HashMap
            List<Map.Entry<String, Double> > list =
                    new LinkedList<Map.Entry<String, Double> >(hm.entrySet());

            // Sort the list
            Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
                public int compare(Map.Entry<String, Double> o1,
                                   Map.Entry<String, Double> o2)
                {
                    return (o2.getValue()).compareTo(o1.getValue());
                }
            });

            // put data from sorted list to hashmap
            HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
            for (Map.Entry<String, Double> aa : list) {
                temp.put(aa.getKey(), aa.getValue());
            }
            return temp;
        }


        //--------------------------Function run--------------------------//
    /*
        * Explanation:
            Returns a Json Array of all results,
            * prepares for ranking by sending results
            * Prepares for Highlighting websites content by dividing the query into its constituents
    */

        public String run(String message, ArrayList<String> queryLinesResult, JSONArray dividedQuery)
                throws FileNotFoundException, JSONException {

            System.out.println("The running function");

//Used to add each word together with the whole query; to populate dividedQuery array
            ArrayList<String> words = new ArrayList<String>();
            words.add(message);                    //First part of dividedQuery array
            JSONObject divide = new JSONObject();  //Used for divided Query servlet to highlight content in results


            String[] result = SplitQuery(message); //Splitting for words
            result  = removeStopWords(result);     // Remove Stop Words from the query

            JSONArray finalJsonFile = new JSONArray(); //For final results


            // Loop over words
            int length = result.length;
            for(int i=0; i<length;i++)
            {
                //Add each word to words Array
                System.out.println(result[i]);
                words.add(result[i]);

                // Results for one word.
                ArrayList<String> oneWordResult = new ArrayList<String>();


                // Search for proper file name for each word
                String fileName = "";
                if (HelpersPackages.Helpers.HelperClass.isProbablyArabic(result[i]))
                    fileName = "arabic";
                else if(result[i].length() == 2)
                    fileName = "two";
                else if(result[i].length() > 2)
                {
                    fileName = "_" + result[i].substring(0,3);

                    // if the word is something like that => UK's
                    File tempFile = new File(HelpersPackages.Helpers.HelperClass.invertedFilePath_V3(fileName));
                    if (! tempFile.exists())
                    {
                        fileName = "others";
                    }
                }


                String filePath = "D:\\Study\\Second Year\\Second Sem\\APT\\New folder (2)\\New folder (2)\\Sreach-Engine";   // get the directory of the project

                // Delete last Directory to get path of Inverted Files, root folder src
                //  filePath = filePath.substring(0, filePath.lastIndexOf("\\"));

                filePath += File.separator + "InvertedFiles_V3" + File.separator;

                filePath += fileName + ".txt";

                File targetFile = new File(filePath);

                //true to sepcify it's Query Processing not Phrase Searching
                searchInInvertedFiles(result[i], targetFile,oneWordResult, true);




                // Loop over versions of Words
                // Adding words results to ranker Array
                // And splitting for the same line to prepare for fetching links
                int length_2 = oneWordResult.size();
                for(int j = 0; j<length_2; j++)
                {
                    //Don't send to ranker
                    if(oneWordResult.get(j).equals(""))
                    {continue;}

                    queryLinesResult.add(oneWordResult.get(j));



                    String[] splitLine= oneWordResult.get(j).split("\\[");



                    // Loop over links of the same version of each Word
                    int length_3 = splitLine.length;
                    for (int k=1; k<length_3; k++)
                    {

                        //Split Each part of the line to get the links, split over ','
                        int End = splitLine[k].indexOf(']');
                        String temp = splitLine[k].substring(0, End);

                        String[] finalID = temp.split(",");

                        String link = finalID[0];



                        // Get description and populate Json Array
                        StringBuffer description = new StringBuffer("");
                        JSONObject Jo = new JSONObject();
                        dataBaseObject.getDescription(link, description);
                        Jo.put("Link", link);
                        Jo.put("Description", description);
                        finalJsonFile.put(Jo);

                    }
                }

            }




            // Populate DividedQuery Array
            divide.put("Result", words);
            dividedQuery.put(divide);
            return finalJsonFile.toString();

        }
    }



    static class WorkingFiles {
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
            String filePath = "D:\\Study\\Second Year\\Second Sem\\APT\\New folder (2)\\New folder (2)\\Sreach-Engine";   // get the directory of the project
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
        public String[] getStopWordsAsArray()
        {
            return this.stopWords;
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

        // get the count of the website words
//    public static long getWordsContent(String url)
//    {
//        String path = HelperClass.contentLengthFiles(url);
//        System.out.println(path);
//        Scanner read = null;
//        try {
//            read = new Scanner(new File(path));
//        } catch (FileNotFoundException e) {
//            System.out.println("Failed to read the words count");
//            return -1;
//        }
//
//        while(read.hasNextLine())
//        {
//            return Long.parseLong(read.nextLine());
//        }
//        return -1;
//    }

    }

    static class HelperClass {


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
//        String filePath = Paths.get("").normalize().toAbsolutePath().toString();
            String filePath = "D:\\Study\\Second Year\\Second Sem\\APT\\New folder (2)\\New folder (2)\\Sreach-Engine";
            //filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
            filePath += File.separator + "InvertedFiles_V3" + File.separator + fileName + ".txt";
            return filePath;
        }

        // get the path of the inverted Files_V3 folder
        public static String invertedFilePathDirectoryPath()
        {
            String filePath = Paths.get("").normalize().toAbsolutePath().toString();
            // filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
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
                List<String> tempList = new ArrayList<>(List.of(info.split("::", 0)));

                // check if the same info is existing or not
                if (tempList.get(0).equals(oldInfo)) {
                    String frequency = tempList.get(1);
                    int integerFrequency = Integer.parseInt(frequency);
                    theNewInfo = tempList.get(0) + "::" + String.valueOf(integerFrequency + 1); /* convert the ( int freq + 1 ) to string here */
                    oldInfo = oldInfo + "::" + frequency;
                    line = line.replace(oldInfo , theNewInfo);
                    return line;
                }
            }

            // if not returned, then the info is not exist
            theNewInfo = oldInfo + "::1";
            line += theNewInfo + ';';
            return line;

        }
    }


}






