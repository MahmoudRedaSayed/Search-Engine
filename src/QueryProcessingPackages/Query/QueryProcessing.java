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



public class QueryProcessing{

    DataBase dataBaseObject = new DataBase();
    WorkingFiles working;
    private Map<String, File> invertedFiles;
    PorterStemmer stemObject = new PorterStemmer();
    String[] stopWords;


    public QueryProcessing(WorkingFiles files)
    {
        working = files;
        stopWords = files.getStopWordsAsArr();
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



    public JSONArray run(String message, ArrayList<String> queryLinesResult) throws FileNotFoundException, JSONException {
        invertedFiles = working.getInvertedFiles();

        ArrayList<String> allWordsResult = new ArrayList<String>();


        String[] result = SplitQuery(message);
        result  = removeStopWords(result);
        String json = "{ [";
        StringBuffer jsonFile = new StringBuffer(json);
        JSONArray finalJsonFile = new JSONArray();
        int length = result.length;
        for(int i=0; i<length;i++)
        {
            // Loop over words
            ArrayList<String> oneWordResult = new ArrayList<String>();



            searchInInvertedFiles(result[i], invertedFiles.get(result[i].substring(0,2)),oneWordResult, true);

            int length_2 = oneWordResult.size();
            for(int j = 0; j<length_2; j++)
            {
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
                    dataBaseObject.getLinkByID(ID,link,description);
                    Jo.put("Link", link);
                    Jo.put("Description", description);
                    finalJsonFile.put(Jo);
                }
            }

        }
        return finalJsonFile;

    }




}