package QueryProcessingPackages.Query;


import ServletsPackages.ServletPackage.QuerySearch;
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

import javax.xml.crypto.Data;


public class QueryProcessing{
    DataBase dataBaseObject = new DataBase();
    //WorkingFiles working;
    private Map<String, File> invertedFiles;
    public  PorterStemmer stemObject = new PorterStemmer();
    public String[] stopWords=new String[2];


    public QueryProcessing()
    {
        //working = files;
        //stopWords = files.getStopWordsAsArr();
        System.out.println("The consturctor");

        stopWords[0] = "test";      // "will be edited"
        stopWords[1] = "tested";      // "will be edited"
    }

    private String[] SplitQuery(String searchQuery)
    {
        String[] subStrings = searchQuery.trim().split("\\s+");
        return subStrings;
    }



    private void readStopWords() throws FileNotFoundException {
        // open the file that contains stop words
        String filePath = System.getProperty("user.dir");   // get the directory of the project
        filePath += File.separator + "helpers" + File.separator + "stop_words.txt";
        File myFile = new File(filePath);

        this.stopWords = new String[851];

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

    private String stemGivenWord(String word)
    {
        stemObject.setCurrent(word);
        stemObject.stem();
        return stemObject.getCurrent();
    }

    //Utility Function for removeStopWords()
    private static String[] removeElement(String[] arr, int[] index) {
        List<String> list = new ArrayList<>(Arrays.asList(arr));
        for (int i=0; i<index.length;i++)
        {
            list.remove(new String(arr[index[i]]));
        }
        return list.toArray(String[]::new);
    }


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

    //What remains: Search for word in file and create array for each word in the search query:
    //First element is the actual word if present
    //The rest are the words with same root in that file


    public static void searchInInvertedFiles(String word, File myFile, ArrayList<String> results, boolean stemmingFlag) throws FileNotFoundException {
        Scanner read = new Scanner(myFile);
        String tempInput,
                stemmedVersion = " ";

        // stemming the word
        if (stemmingFlag)
            stemmedVersion = HelperClass.stemTheWord(word);

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
                if(!wordIsFound && theWord.equals(word))
                {
                    results.set(0, tempInput);     // target word will have the highest priority
                    wordIsFound = true;
                    continue;
                }

                counter = 1;
                // comparing the stemmed version of the target word by the stemmed version of the word in the inverted file
                if (stemmingFlag)
                {
                    if (stemmedVersion.equals(HelperClass.stemTheWord(theWord)))
                        results.add(counter++, tempInput);
                }
            }
        }
    }

    public static HashMap<Integer, Double> sortByValue(HashMap<Integer, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Double> > list =
                new LinkedList<Map.Entry<Integer, Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double> >() {
            public int compare(Map.Entry<Integer, Double> o1,
                               Map.Entry<Integer, Double> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<String, Double> replaceIDByLink(HashMap<Integer, Double> hm)
    {
        StringBuffer link = new StringBuffer("");
        DataBase dataBaseObject = new DataBase();
        StringBuffer description = new StringBuffer("");
        HashMap<String, Double> temp = new HashMap<String, Double>();
        for (Iterator<Map.Entry<Integer, Double>> it = hm.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<Integer, Double> IDEntry = it.next();
            dataBaseObject.getLinkByID(IDEntry.getKey(), link, description);
            temp.put(link.toString(), IDEntry.getValue());
        }

        return temp;
    }

    public String run(String message, ArrayList<String> queryLinesResult, JSONArray dividedQuery)
            throws FileNotFoundException, JSONException {
        //invertedFiles = working.getInvertedFiles();
        System.out.println("The running function");

        boolean [] indexProcessed;
        Map<Integer, Integer> allIDs = new HashMap<Integer, Integer>();
        ArrayList<String> words = new ArrayList<String>();
        words.add(message);
        JSONObject divide = new JSONObject();
        ArrayList<String> allWordsResult = new ArrayList<String>();


        String[] result = SplitQuery(message);
        result  = removeStopWords(result);
        indexProcessed = new boolean[result.length];
        String json = "{ [";
        StringBuffer jsonFile = new StringBuffer(json);
        JSONArray finalJsonFile = new JSONArray();
        int length = result.length;
        for(int i=0; i<length;i++)
        {

            // Loop over words
            words.add(result[i]);
            ArrayList<String> oneWordResult = new ArrayList<String>();

            String fileName = "";
            if (HelperClass.isProbablyArabic(result[i]))
                fileName = "arabic";
            else if(result[i].length() == 2)
                fileName = "two";

            else
                fileName = "_" + result[i].substring(0,3);

            // Mustafa : I edited this code

            String filePath = "F:\\Servlets with Database\\Sreach-Engine\\InvertedFiles_V3\\";
            filePath += fileName + ".txt";
            File targetFile = new File(filePath);
            searchInInvertedFiles(result[i], targetFile,oneWordResult, true);

            int length_2 = oneWordResult.size();
            for(int j = 0; j<length_2; j++)
            {
                if(oneWordResult.get(j).equals(""))
                {continue;}

                queryLinesResult.add(oneWordResult.get(j));
                // Loop over versions of Words


                String[] splitLine= oneWordResult.get(j).split("\\[");
                int length_3 = splitLine.length;
                for (int k=1; k<length_3; k+=2)
                {

                    // Loop over links of the same version of each Word

                    int End = splitLine[k].indexOf(']');
                    String temp = splitLine[k].substring(0, End);

                    String[] finalID = temp.split(",");
                    int ID = Integer.parseInt(finalID[0]);

                    StringBuffer link = new StringBuffer("");
                    StringBuffer description = new StringBuffer("");
                    JSONObject Jo = new JSONObject();
                    dataBaseObject.getLinkByID(ID, link, description);
                    Jo.put("Link", link);
                    Jo.put("Description", description);
                    finalJsonFile.put(Jo);

                }
            }

        }





        divide.put("Result", words);
        dividedQuery.put(divide);
        return finalJsonFile.toString();

    }
}