package PhraseSearchingPackages.PhraseSearching;


import com.mysql.cj.xdevapi.JsonArray;
import org.jsoup.select.Evaluator;
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
import QueryProcessingPackages.Query.*;
import org.json.*;


public class PhraseSearching {
    DataBase dataBaseObject = new DataBase();
    WorkingFiles working;
    private Map<String, File> invertedFiles;
    PorterStemmer stemObject = new PorterStemmer();
    String[] stopWords;


    public PhraseSearching(WorkingFiles files) {
        working = files;
        stopWords = files.getStopWordsAsArr();
    }


    private String[] SplitQuery(String searchQuery) {
        String[] subStrings = searchQuery.trim().split("\\s+");
        return subStrings;
    }

    private static String[] removeElement(String[] arr, int[] index) {
        List<String> list = new ArrayList<>(Arrays.asList(arr));
        for (int i = 0; i < index.length; i++) {
            list.remove(new String(arr[index[i]]));
        }
        return list.toArray(String[]::new);
    }


    private String[] removeStopWords(String[] searchQuery) {
        int length = searchQuery.length;
        ArrayList<Integer> indeces = new ArrayList<Integer>();
        for (int i = 0; i < length; i++) {
            System.out.println(searchQuery[i].toLowerCase());
            if (Arrays.asList(this.stopWords).contains(searchQuery[i].toLowerCase())) {
                indeces.add(i);
            }
        }
        searchQuery = removeElement(searchQuery, indeces.stream().mapToInt(Integer::intValue).toArray());
        return searchQuery;
    }


    public JSONArray run(String message, ArrayList<String> queryLinesResult) throws FileNotFoundException, JSONException {
        invertedFiles = working.getInvertedFiles();
        boolean[] indexProcessed;
        Map<Integer, Integer> allIDs = new HashMap<Integer, Integer>();


        ArrayList<String> allWordsResult = new ArrayList<String>();


        String[] result = SplitQuery(message);
        result = removeStopWords(result);
        indexProcessed = new boolean[result.length];
        String json = "{ [";
        StringBuffer jsonFile = new StringBuffer(json);
        JSONArray finalJsonFile = new JSONArray();
        int length = result.length;
        for (int i = 0; i < length; i++) {
            // Loop over words
            ArrayList<String> oneWordResult = new ArrayList<String>();


            QueryProcessing.searchInInvertedFiles(result[i], invertedFiles.get(result[i].substring(0, 2)),
                    oneWordResult, false);

            int length_2 = oneWordResult.size();
            for (int j = 0; j < length_2; j++) {
                queryLinesResult.add(oneWordResult.get(j));
                // Loop over versions of Words


                String[] splitLine = oneWordResult.get(j).split("\\[");
                int length_3 = splitLine.length;
                for (int k = 1; k < length_3; k += 2) {

                    // Loop over links of the same version of each Word

                    int End = splitLine[k].indexOf(']');
                    String temp = splitLine[k].substring(0, End);

                    String[] finalID = temp.split(",");
                    int ID = Integer.parseInt(finalID[0]);
                    if (i == 0 && !indexProcessed[i]) {
                        allIDs.put(ID, 1);
                        indexProcessed[0] = true;
                    }
                    else if (!indexProcessed[i] && allIDs.containsKey(ID)) {
                        allIDs.put(ID, 1 + allIDs.get(ID));
                        indexProcessed[i] = true;
                    }
                }
            }

        }

        for (Iterator<Map.Entry<Integer, Integer>> it = allIDs.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, Integer> entry = it.next();
            if (entry.getValue() < length) {
                it.remove();
            }

            for (Iterator<Map.Entry<Integer, Integer>> iter = allIDs.entrySet().iterator(); it.hasNext(); ) {

                Map.Entry<Integer, Integer> IDEntry = iter.next();

                StringBuffer link = new StringBuffer("");
                StringBuffer description = new StringBuffer("");
                JSONObject Jo = new JSONObject();
                dataBaseObject.getLinkByID(IDEntry.getKey(), link, description);
                Jo.put("Link", link);
                Jo.put("Description", description);
                finalJsonFile.put(Jo);
            }



        }
        return finalJsonFile;
    }
}


