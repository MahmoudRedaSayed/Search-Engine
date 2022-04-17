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
    WorkingFiles working = new WorkingFiles();
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

//    private void initializeFile()
//    {
//        String letters = "qwertyuiopasdfghjklzxcvbnm";
//        for (int i = 0; i < 26; i++){
//
//            invertedFiles.put(letters.charAt(i), new File(HelperClass.invertedFilePath(letters.charAt(i))));
//        }
//    }

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
    private static String[] removeElement(String[] arr, int index) {
        List<String> list = new ArrayList<>(Arrays.asList(arr));
        list.remove(index);
        return list.toArray(String[]::new);
    }


    private String[] removeStopWords(String[] searchQuery)
    {
        for(int i = 0; i< searchQuery.length; i++)
        {
            if (Arrays.asList(this.stopWords).contains(searchQuery[i].toLowerCase()))
            {
                searchQuery = removeElement(searchQuery, i);
            }
        }
        return searchQuery;
    }

    //What remains: Search for word in file and create array for each word in the search query:
    //First element is the actual word if present
    //The rest are the words with same root in that file


    public static void searchInInvertedFiles(String word, File myFile, ArrayList<String> results) throws FileNotFoundException {
        Scanner read = new Scanner(myFile);
        String tempInput,
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
                if (stemmedVersion.equals(HelperClass.stemTheWord(theWord)))
                    results.add(counter++, tempInput);
            }
        }
    }



    public JSONArray run(String message) throws FileNotFoundException, JSONException {
        invertedFiles = working.getInvertedFiles();

        /*try {
            readStopWords();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open Stop words file");
            e.printStackTrace();
        }*/

       // String message = "Is Egypt in Africa?";

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



            searchInInvertedFiles(result[i], invertedFiles.get(result[i].substring(0,2)),oneWordResult);

            int length_2 = oneWordResult.size();
            for(int j = 0; j<length_2; j++)
            {
                // Loop over versions of Words

//                int Start = oneWordResult.get(j).indexOf('|');
//                int End = oneWordResult.get(j).indexOf(']');
//
//                String temp = oneWordResult.get(j).substring(Start+2, End);
                String[] splitLine= oneWordResult.get(j).split("\\[");
                int length_3 = splitLine.length;
                for (int k=1; k<length_3; k+=2)
                {

                    // Loop over links of the same version of each Word

                    int End = splitLine[k].indexOf(']');
                    String temp = splitLine[k].substring(0, End);
                    String[] finalID = temp.split(",");
                    int ID = Integer.parseInt(finalID[0]);
                    JSONObject Jo = new JSONObject();
                    Jo.put("Link",dataBaseObject.getLinkByID(ID) );
                    finalJsonFile.put(Jo);
                }
            }

        }
        return finalJsonFile;

    }
}