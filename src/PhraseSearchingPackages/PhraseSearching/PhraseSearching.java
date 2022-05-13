//package PhraseSearchingPackages.PhraseSearching;
//
//
//import com.mysql.cj.xdevapi.JsonArray;
//import org.jsoup.select.Evaluator;
//import org.tartarus.snowball.ext.PorterStemmer;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.*;
//import HelpersPackages.Helpers.*;
//import DataBasePackages.DataBase.*;
//import QueryProcessingPackages.Query.*;
//import org.json.*;
//
//
//
////-------------------------- Class PhraseSearching--------------------------//
///*
//
//
//* Data member:
//    1-workingFilesObject
//    2-dataBaseObject
//    3-stopWords Array
//
//*Functions :
//    1-run
//    2-SplitQuery
//    3-removeElement
//    4-removeStopWords
//    5-searchInInvertedFiles
//    6-sortByValue Should be for Ranker
//
//* */
//
//public class PhraseSearching {
//    WorkingFiles workingFilesObject;
//    DataBase dataBaseObject;
//    public  PorterStemmer stemObject = new PorterStemmer();
//    public String[] stopWords;
//
//
//    public PhraseSearching() throws FileNotFoundException {
//
//        workingFilesObject = new WorkingFiles();
//        WorkingFiles.readStopWords();
//        dataBaseObject = new DataBase();
//        this.stopWords = workingFilesObject.getStopWordsAsArray();
//        System.out.println("Phrase Searching consturctor");
//    }
//
//    //--------------------------Function SplitQuery--------------------------//
//    /*
//        * Explanation:
//            Utility Function to divide the search query into the words constituting it
//    */
//
//    private String[] SplitQuery(String searchQuery) {
//        String[] subStrings = searchQuery.trim().split("\\s+");
//        return subStrings;
//    }
//
//
//    //--------------------------Function removeElement--------------------------//
//    /*
//        * Explanation:
//            Utility Function for removeStopWords, used to remove elements from array
//    */
//    private static String[] removeElement(String[] arr, int[] index) {
//        List<String> list = new ArrayList<>(Arrays.asList(arr));
//        for (int i = 0; i < index.length; i++) {
//            list.remove(new String(arr[index[i]]));
//        }
//        return list.toArray(String[]::new);
//    }
//
//
//    //--------------------------Function removeStopWords--------------------------//
//    /*
//        * Explanation:
//            Function used to remove all stop words from the Search Query
//    */
//
//    private String[] removeStopWords(String[] searchQuery) {
//        int length = searchQuery.length;
//        ArrayList<Integer> indeces = new ArrayList<Integer>();
//        for (int i = 0; i < length; i++) {
//            System.out.println(searchQuery[i].toLowerCase());
//            if (Arrays.asList(this.stopWords).contains(searchQuery[i].toLowerCase())) {
//                indeces.add(i);
//            }
//        }
//        searchQuery = removeElement(searchQuery, indeces.stream().mapToInt(Integer::intValue).toArray());
//        return searchQuery;
//    }
//
//
//    //--------------------------Function run--------------------------//
//    /*
//        * Explanation:
//            Returns a Json Array of all results,
//            * prepares for ranking by sending results
//            * Prepares for Highlighting websites content by dividing the query into its constituents
//    */
//
//    public String run(String message, ArrayList<String> queryLinesResult, JSONArray dividedQuery) throws FileNotFoundException, JSONException {
//
//        System.out.println("Phrase Searching Run Function");
//        boolean[] indexProcessed;  //Used for Links map, to not add links over and over again
//        HashMap<String, Integer> allLinks = new HashMap<String, Integer>(); //Has links that are repeated for each word of the search query
//        JSONObject divide = new JSONObject();             //Used for divided Query servlet to highlight content in results
//        divide.put("Results", message);
//        dividedQuery.put(divide);                    //Populating the array using the whole search query
//
//
//
//        String[] result = SplitQuery(message);  //Splitting for words
//        result  = removeStopWords(result);     // Remove Stop Words from the query
//        indexProcessed = new boolean[result.length];  //Initializing indexes array
//        JSONArray finalJsonFile = new JSONArray();   //For final results
//
//
//        // Loop over words
//        int length = result.length;
//        for (int i = 0; i < length; i++) {
//
//            // Results for one word.
//            ArrayList<String> oneWordResult = new ArrayList<String>();
//
//
//            // Search for proper file name for each word
//            String fileName = "";
//            if (HelperClass.isProbablyArabic(result[i]))
//                fileName = "arabic";
//            else if(result[i].length() == 2)
//                fileName = "two";
//
//            else if(result[i].length() > 2)
//            {
//                fileName = "_" + result[i].substring(0,3);
//
//                // if the word is something like that => UK's
//                File tempFile = new File(HelperClass.invertedFilePath_V3(fileName));
//                if (! tempFile.exists())
//                {
//                    fileName = "others";
//                }
//            }
//
//            String filePath = System.getProperty("user.dir");   // get the directory of the project
//
//            // Delete last Directory to get path of Inverted Files
//            filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
//
//            filePath += File.separator + "InvertedFiles_V3" + File.separator;
//
//            filePath += fileName + ".txt";
//            //System.out.println(finalFilePath + "From Search Inverted Files");
//            File targetFile = new File(filePath);
//
//
//            //false to sepcify it's Phrase Searching not Query Processing
//            QueryProcessing.searchInInvertedFiles(result[i], targetFile,oneWordResult, false);
//
//
//            // Loop over versions of Words
//            // And splitting for the same line to prepare for fetching links
//            int length_2 = oneWordResult.size();
//            for (int j = 0; j < length_2; j++) {
//
//                //Don't send to ranker
//                if(oneWordResult.get(j).equals(""))
//                {continue;}
//
//
//                // Should we let this be like that? Or should it be just links from map? I don't know
//                queryLinesResult.add(oneWordResult.get(j));
//
//
//                String[] splitLine = oneWordResult.get(j).split("\\[");
//
//
//                // Loop over links of the same version of each Word
//                int length_3 = splitLine.length;
//                for (int k = 1; k < length_3; k++) {
//
//                    //Split Each part of the line to get the links, split over ','
//                    int End = splitLine[k].indexOf(']');
//                    String temp = splitLine[k].substring(0, End);
//
//                    String[] finalID = temp.split(",");
//                    //int ID = Integer.parseInt(finalID[0]);
//                    String Link = finalID[0];
//
//                    // Populating Links Map
//
//                    if (i == 0 && !indexProcessed[i]) {
//                        // For First word, add links
//                        allLinks.put(Link, 1);
//                        if(k == length_3-1)
//                        {
//                            indexProcessed[0] = true; //To not add again
//                        }
//                    }
//                    //Then, only increment those already in the map
//                    else if (!indexProcessed[i] && allLinks.containsKey(Link)) {
//                        allLinks.put(Link, 1 + allLinks.get(Link));
//                        if(k == length_3-1)
//                        {
//                            indexProcessed[i] = true;  //To not add again
//                        }
//                    }
//                }
//            }
//
//        }
//        // Removing links that aren't repeated with every single word.
//        for (Iterator<Map.Entry<String, Integer>> it = allLinks.entrySet().iterator(); it.hasNext(); ) {
//            Map.Entry<String, Integer> entry = it.next();
//            if (entry.getValue() < length) {
//                it.remove();
//            }
//        }
//
//        for (Iterator<Map.Entry<String, Integer>> iter = allLinks.entrySet().iterator(); iter.hasNext(); ) {
//
//
//            Map.Entry<String, Integer> IDEntry = iter.next();
//
//
//            String link = IDEntry.getKey();
//
//            // Get description and populate Json Array
//            StringBuffer description = new StringBuffer("");
//            JSONObject Jo = new JSONObject();
//            dataBaseObject.getDescription(link, description);
//            Jo.put("Link", link);
//            Jo.put("Description", description);
//            finalJsonFile.put(Jo);
//        }
//
//        return finalJsonFile.toString();
//    }
//}
//
//
