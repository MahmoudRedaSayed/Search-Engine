package QueryProcessingPackages.Query;



import com.mysql.cj.xdevapi.JsonArray;
import org.tartarus.snowball.ext.PorterStemmer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import HelpersPackages.Helpers.*;
import DataBasePackages.DataBase.*;
import org.json.*;


//-------------------------- Class QueryProcessing--------------------------//
/*


* Data member:
    1-workingFilesObject
    2-dataBaseObject
    3-stopWords Array

*Functions :
    1-run
    2-SplitQuery
    3-removeElement
    4-removeStopWords
    5-searchInInvertedFiles
    6-sortByValue Should be for Ranker

* */

public class QueryProcessing{

    //--------------------- The Data Members-------------------------//
    DataBase dataBaseObject;
    public String[] stopWords;

    //--------------------- Constructor-----------------------------//

/*
        * Explanation:
            Constructor to initialize the object of the Database
            Read Stop Words to use in further functions
    */

    public QueryProcessing() throws FileNotFoundException {
        WorkingFiles.readStopWords();
        dataBaseObject = new DataBase();
        this.stopWords = WorkingFiles.getStopWordsAsArray();

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

    public static void searchInInvertedFiles(ArrayList<String> word, File myFile, ArrayList<String> results, boolean stemmingFlag) throws FileNotFoundException {
        Scanner read = new Scanner(myFile);
        int counter = 0;

        String tempInput,
                stemmedVersion = " ";


        boolean wordIsFound = false;

        int stopIndex;

        while(read.hasNextLine())
        {
            tempInput = read.nextLine();
            int i=0;
            boolean anyWordFound = false;
            while(!anyWordFound && i<word.size()) {
                anyWordFound = false;

                // stemming the word
                if (stemmingFlag)
                    stemmedVersion = HelperClass.stemTheWord(word.get(i));

                if (tempInput.equals(""))
                    continue;

                // check if this line is for a word or just an extension for the previous line
                if (tempInput.charAt(0) == '<')
                // compare to check if this tempWord = ourWord ?
                {
                    // extract the word from the line that read by the scanner
                    stopIndex = tempInput.indexOf('|');
                    String theWord = tempInput.substring(1, stopIndex);

                    // this condition for the targeted word
                    if (!wordIsFound && theWord.equals(word.get(i).toLowerCase())) {
                        results.add(counter++, tempInput);     // target word will have the highest priority
                        wordIsFound = true;
                        anyWordFound = true;                   //To not search with other words on same line
                        continue;
                    }

                    //counter = 1;
                    // comparing the stemmed version of the target word by the stemmed version of the word in the inverted file
                    if (stemmingFlag) {
                        if (stemmedVersion.equals(HelperClass.stemTheWord(theWord))) {
                            results.add(counter++, tempInput);
                            anyWordFound = true;
                        }
                    }
                }
                i++;
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


    public void run(String message, ArrayList<String> queryLinesResult, JSONArray dividedQuery)
            throws FileNotFoundException, JSONException {

        //Used to save File names of current words
        HashMap<String,ArrayList<String>> fileNames=new HashMap<String,ArrayList<String>>();

//Used to add each word together with the whole query; to populate dividedQuery array
        ArrayList<String> words = new ArrayList<String>();
        words.add(message);                    //First part of dividedQuery array
        JSONObject divide = new JSONObject();  //Used for divided Query servlet to highlight content in results


        String[] result = SplitQuery(message); //Splitting for words
        result  = removeStopWords(result);     // Remove Stop Words from the query

        JSONArray finalJsonFile = new JSONArray(); //For final results


        // Loop over words
        int length = result.length;
        for(int i=0; i<length;i++) {


            // Search for proper file name for each word
            String fileName = "";
            if (HelperClass.isProbablyArabic(result[i]))
                fileName = "arabic";
            else if (result[i].length() == 2)
                fileName = "two";
            else if (result[i].length() > 2) {
                fileName = "_" + result[i].substring(0, 3);

                // if the word is something like that => UK's
                File tempFile = new File(HelperClass.invertedFilePath_V3(fileName));
                if (!tempFile.exists()) {
                    fileName = "others";
                }
            }

            if(!fileNames.containsKey(fileName)) {
                ArrayList<String> currentFileWords = new ArrayList<String>();
                currentFileWords.add(result[i]);
                fileNames.put(fileName, currentFileWords);
            }
            else{

                ArrayList<String> currentFileWords = fileNames.get(fileName);
                currentFileWords.add(result[i]);
                fileNames.put(fileName , currentFileWords);
            }
        }
        //filenames has all words and file names it's assigned to.

        //Loop over File names map to access all file names.
        for (Iterator<Map.Entry<String, ArrayList<String>>> it = fileNames.entrySet().iterator(); it.hasNext(); ) {

            Map.Entry<String, ArrayList<String>> currFile = it.next();

            // Results for one file.
            ArrayList<String> oneFileResult = new ArrayList<String>();

            String filePath = System.getProperty("user.dir");   // get the directory of the project

            // Delete last Directory to get path of Inverted Files, root folder src
            filePath = filePath.substring(0, filePath.lastIndexOf("\\"));

            filePath += File.separator + "InvertedFiles_V3" + File.separator;

            filePath += currFile.getKey() + ".txt";    //To get File Name

            File targetFile = new File(filePath);

            //true to sepcify it's Query Processing not Phrase Searching
            searchInInvertedFiles(currFile.getValue(), targetFile,oneFileResult, true);


            // Loop over versions of Words
            // Adding words results to ranker Array

            int length_2 = oneFileResult.size();
            for(int j = 0; j<length_2; j++)
            {
                //Don't send to ranker
                if(oneFileResult.get(j).equals(""))
                {continue;}

                queryLinesResult.add(oneFileResult.get(j));
            }

        }
        // Populate DividedQuery Array
        divide.put("Result", words);
        dividedQuery.put(divide);
    }
}