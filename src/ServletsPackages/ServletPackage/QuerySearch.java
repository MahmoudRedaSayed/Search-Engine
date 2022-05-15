//
//package ServletsPackages.ServletPackage;
//
//import java.io.IOException;
//import javax.servlet.*;
//import javax.servlet.http.*;
//
//
//
//import org.json.JSONException;
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.*;
//import java.sql.ResultSet;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import org.json.*;
//import com.mysql.jdbc.*;
//import org.tartarus.snowball.ext.PorterStemmer;
//
//
//public class QuerySearch extends HttpServlet {
//    public String searchingQuery;
//    public ArrayList<String> rankerArray = new ArrayList<String>();
//    public Map<String, Integer> phraSearchingMap = new HashMap<String, Integer>();
//    public boolean isPhraseSearching = false;
//    public JSONArray dividedQuery = new JSONArray();
//    public Ranker rankerObject ;
//    int count = 0;
//
//    public void init()
//            throws ServletException
//    {
//        rankerObject=new Ranker();
//
//    }
//
//
//    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
//        rankerObject=new Ranker();
//
//        res.addHeader("Access-Control-Allow-Origin", "*");
//        System.out.println("any thing here ");
//        String searchingQuery = req.getParameter("query");
//        res.setContentType("text/html");
//        String results = "";
//
//        String[] wordsOfquery = QueryProcessing.SplitQuery(searchingQuery);
//        wordsOfquery = QueryProcessing.removeStopWords(wordsOfquery);
//
//        if (searchingQuery.startsWith("\"") && searchingQuery.endsWith("\"")) {
//
//            //call the function of the phrase searching
//            PhraseSearching obj = new PhraseSearching();
//
//            try {
//                 phraSearchingMap  =obj.run(searchingQuery,rankerArray,dividedQuery);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                isPhraseSearching = true;
//                results = rankerObject.calculateRelevance(rankerArray, phraSearchingMap, isPhraseSearching, wordsOfquery);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            res.getWriter().println(results);
//        }
//        else {
//
//
//            QueryProcessing obj = new QueryProcessing();
//
//
//            try {
//              obj.run(searchingQuery, rankerArray, dividedQuery);
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println(e);
//            }
//            try {
//               isPhraseSearching = false;
//               results = rankerObject.calculateRelevance(rankerArray, phraSearchingMap, isPhraseSearching, wordsOfquery);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            System.out.println(searchingQuery);
//            res.getWriter().println(results);
//
//        }
//
//    }
//
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  static class PhraseSearching {
//
//        DataBase dataBaseObject;
//        public PorterStemmer stemObject = new PorterStemmer();
//        public String[] stopWords;
//        Map<String,Integer> IDs;
//
//        public PhraseSearching() throws FileNotFoundException {
//
//            WorkingFiles.readStopWords();
//            dataBaseObject = new DataBase();
//            stopWords = WorkingFiles.getStopWordsAsArray();
//            IDs = dataBaseObject.getIDsAsMap();
//            System.out.println("Phrase Searching consturctor");
//        }
//
//        //--------------------------Function SplitQuery--------------------------//
//   /*
//       * Explanation:
//           Utility Function to divide the search query into the words constituting it
//   */
//
//        private String[] SplitQuery(String searchQuery) {
//            String[] subStrings = searchQuery.trim().split("\\s+");
//            return subStrings;
//        }
//
//
//        //--------------------------Function removeElement--------------------------//
//   /*
//       * Explanation:
//           Utility Function for removeStopWords, used to remove elements from array
//   */
//        private static String[] removeElement(String[] arr, int[] index) {
//            List<String> list = new ArrayList<>(Arrays.asList(arr));
//            for (int i = 0; i < index.length; i++) {
//                list.remove(new String(arr[index[i]]));
//            }
//            return list.toArray(String[]::new);
//        }
//
//
//        //--------------------------Function removeStopWords--------------------------//
//   /*
//       * Explanation:
//           Function used to remove all stop words from the Search Query
//   */
//
//        private String[] removeStopWords(String[] searchQuery) {
//            int length = searchQuery.length;
//            ArrayList<Integer> indeces = new ArrayList<Integer>();
//            for (int i = 0; i < length; i++) {
//                System.out.println(searchQuery[i].toLowerCase());
//                if (Arrays.asList(this.stopWords).contains(searchQuery[i].toLowerCase())) {
//                    indeces.add(i);
//                }
//            }
//            searchQuery = removeElement(searchQuery, indeces.stream().mapToInt(Integer::intValue).toArray());
//            return searchQuery;
//        }
//
//        //--------------------------Function searchInInvertedFiles--------------------------//
//    /*
//        * Explanation:
//            Function used to search inverted Files,to fetch results for Search Queries.
//    */
//        public static void searchInInvertedFiles(String word, File myFile, ArrayList<String> results, boolean stemmingFlag) throws FileNotFoundException {
//            Scanner read = new Scanner(myFile);
//            String tempInput,
//                    stemmedVersion = " ";
//
//            // stemming the word
//            if (stemmingFlag)
//                stemmedVersion = HelperClass.stemTheWord(word);
//
//            boolean wordIsFound = false;
//
//            int stopIndex, counter;
//
//            results.add(0, "");     // if the targeted word is not found, replace empty in its index
//            while(read.hasNextLine())
//            {
//                tempInput = read.nextLine();
//                if (tempInput.equals(""))
//                    continue;
//
//                // check if this line is for a word or just an extension for the previous line
//                if (tempInput.charAt(0) == '<')
//                // compare to check if this tempWord = ourWord ?
//                {
//                    // extract the word from the line that read by the scanner
//                    stopIndex = tempInput.indexOf('|');
//                    String theWord = tempInput.substring(1, stopIndex);
//
//                    // this condition for the targeted word
//                    if(!wordIsFound && theWord.equals(word.toLowerCase()))
//                    {
//                        results.set(0, tempInput);     // target word will have the highest priority
//                        wordIsFound = true;
//                        continue;
//                    }
//
//                    counter = 1;
//                    // comparing the stemmed version of the target word by the stemmed version of the word in the inverted file
//                    if (stemmingFlag)
//                    {
//                        if (stemmedVersion.equals(HelperClass.stemTheWord(theWord)))
//                            results.add(counter++, tempInput);
//                    }
//                }
//            }
//        }
//
//
//        //--------------------------Function run--------------------------//
//   /*
//       * Explanation:
//           Returns a Json Array of all results,
//           * prepares for ranking by sending results
//           * Prepares for Highlighting websites content by dividing the query into its constituents
//   */
//
//        public Map<String, Integer> run(String message, ArrayList<String> queryLinesResult, JSONArray dividedQuery) throws FileNotFoundException, JSONException {
//
//            System.out.println("Phrase Searching Run Function");
//            boolean[] indexProcessed;  //Used for Links map, to not add links over and over again
//            Map<String, Integer> allLinks = new HashMap<String, Integer>(); //Has links that are repeated for each word of the search query
//            JSONObject divide = new JSONObject();             //Used for divided Query servlet to highlight content in results
//            StringBuffer cleanedMessage = new StringBuffer(message);
//            cleanedMessage.deleteCharAt(cleanedMessage.length()-1);   //To remove Quotations \"
//            cleanedMessage.deleteCharAt(0);
//            message = cleanedMessage.toString();
//            divide.put("Results", message);
//            dividedQuery.put(divide);                    //Populating the array using the whole search query
//
//
//            String[] result = SplitQuery(message);  //Splitting for words
//            result = removeStopWords(result);     // Remove Stop Words from the query
//            indexProcessed = new boolean[result.length];  //Initializing indexes array
//            JSONArray finalJsonFile = new JSONArray();   //For final results
//
//
//            // Loop over words
//            int length = result.length;
//            for (int i = 0; i < length; i++) {
//
//                // Results for one word.
//                ArrayList<String> oneWordResult = new ArrayList<String>();
//
//
//                // Search for proper file name for each word
//                String fileName = "";
//                if (HelperClass.isProbablyArabic(result[i]))
//                    fileName = "arabic";
//                else if (result[i].length() == 2)
//                    fileName = "two";
//
//                else if (result[i].length() > 2) {
//                    fileName = "_" + result[i].substring(0, 3);
//
//                    // if the word is something like that => UK's
//                    File tempFile = new File(HelperClass.invertedFilePath_V3(fileName));
//                    if (!tempFile.exists()) {
//                        fileName = "others";
//                    }
//                }
//
//                String filePath = "D:\\College\\Second_Year\\Second Term\\APT\\Project\\Final Version\\Final\\Sreach-Engine";
//
//                filePath += File.separator + "InvertedFiles_V3" + File.separator;
//
//                filePath += fileName + ".txt";
//                //System.out.println(finalFilePath + "From Search Inverted Files");
//                File targetFile = new File(filePath);
//
//
//                //false to sepcify it's Phrase Searching not Query Processing
//                searchInInvertedFiles(result[i].toLowerCase(), targetFile, oneWordResult, false);
//
//
//                // Loop over versions of Words
//                // And splitting for the same line to prepare for fetching links
//                int length_2 = oneWordResult.size();
//                for (int j = 0; j < length_2; j++) {
//
//                    //Don't send to ranker
//                    if (oneWordResult.get(j).equals("")) {
//                        continue;
//                    }
//
//
//                    // Should we let this be like that? Or should it be just links from map? I don't know
//                    queryLinesResult.add(oneWordResult.get(j));
//
//
//                    String[] splitLine = oneWordResult.get(j).split("\\[");
//
//
//                    // Loop over links of the same version of each Word
//                    int length_3 = splitLine.length;
//                    boolean [] wordProcessed = new boolean[length_3];
//                    for (int k = 1; k < length_3; k++) {
//
//                        //Split Each part of the line to get the links, split over ','
//                        int End = splitLine[k].indexOf(']');
//                        String temp = splitLine[k].substring(0, End);
//
//                        String[] finalID = temp.split(",");
//                        //int ID = Integer.parseInt(finalID[0]);
//                        String Link = finalID[0];
//
//                        // Populating Links Map
//
//                        if (i == 0 && !indexProcessed[i]  && !allLinks.containsKey(Link) ) {
//                            // For First word, add links
//                            allLinks.put(Link, 1);
//                            if (k == length_3 - 1) {
//                                indexProcessed[0] = true; //To not add again
//                            }
//                        }
//                        //Then, only increment those already in the map
//                        else if (!indexProcessed[i] && allLinks.containsKey(Link)) {
//                            if(allLinks.get(Link)==i) {
//                                allLinks.put(Link, 1 + allLinks.get(Link));
//                            }
//                            if (k == length_3 - 1) {
//                                indexProcessed[i] = true;  //To not add again
//                            }
//                        }
//                    }
//                }
//
//            }
//
//            //Removing links that aren't repeated with every single word.
//            for (Iterator<Map.Entry<String, Integer>> it = allLinks.entrySet().iterator(); it.hasNext(); ) {
//                Map.Entry<String, Integer> entry = it.next();
//                if (entry.getValue() < length) {
//                    it.remove();
//                }
//            }
//
//            // Removing links that don't contain the actual search Query.
//            for (Iterator<Map.Entry<String, Integer>> it = allLinks.entrySet().iterator(); it.hasNext(); ) {
//                Map.Entry<String, Integer> entry = it.next();
//                if(IDs.containsKey(entry.getKey())) {
//                    int currentLinkId = IDs.get(entry.getKey());
//                    String content = HelperClass.readContent(currentLinkId);
//                    if (!content.contains(message)) {
//                        it.remove();
//                    }
//                }
//            }
//            return allLinks;
//        }
//    }
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    static class Ranker
//    {
//        private DataBase connect = new DataBase();
//        JSONArray dividedQuery = new JSONArray();
//        Map<String,Long> wordsCount;
//        Map<String,Integer> IDs;
//        String[] completedLinks;
//        int completeCount;
//
//        public Ranker()
//        {
//            System.out.printf("From ranker constructor");
//            wordsCount = connect.getWordsCountAsMap();
//            completedLinks = connect.getAllUrls();
//            completeCount=connect.getCompleteCount();
//            IDs = connect.getIDsAsMap();
//
//        }
//
//        public String calculateRelevance(ArrayList<String> tempLines , Map<String, Integer> allLinks , boolean isPhraseSearching, String[] queryWords) throws FileNotFoundException, JSONException {
//            System.out.printf("from calculate\n");
//            JSONArray finalJsonFile = new JSONArray();   //For final results
//            Map<String, Double> pagesRanks = new HashMap<String, Double>();
//            double tf = 0.0,
//                    idf = 0.0,
//                    tf_idf = 0.0,
//                    numOfOccerrencesInCurrentDocument = 0.0, // this value will be weighted
//                    numOfOccerrencesInAllDocuments = 0.0;
//            int counterForWords = 0;
//            //tempMap is used to store each link and its tf-idf value
//            Map<String, Double> allLinksTf_Idf = new HashMap<String, Double>();
//            ArrayList<String> uniqueLinks = new ArrayList<String>();
//
//
//            //
//            System.out.println("before the loop "+tempLines.size()+"\n");
//            ArrayList<String> snipptes = new ArrayList<String>();
//            for (int i = 0; i < tempLines.size(); i++) {
//                Map<String, Double> Links_numOfOccurrences = new HashMap<String, Double>();
//                //to make priority between title,header,paragraph
//                double coeff = 0.0;
//
//                int startIndex = tempLines.get(i).indexOf('|');
//                String lineWithoutTheWord = tempLines.get(i).substring(startIndex + 1);
//                String[] linksWithWordPosition = lineWithoutTheWord.split(";");
//
//                System.out.println("from the loop"+linksWithWordPosition.length);
//
//                //array to store all links of the current query
//                ArrayList<String> arr = new ArrayList<String>();
//                int counter =0;
//                //iterate over the links of each word in the query
//                for (int j = 0; j < linksWithWordPosition.length; j++) {
//
//
//                    //to get id of current page
//                    int bracePosition = linksWithWordPosition[j].indexOf('[');
//                    String linkOfCurrentPage = linksWithWordPosition[j].substring(bracePosition + 1, linksWithWordPosition[j].indexOf(','));
//
//                    if((isPhraseSearching && allLinks.containsKey(linkOfCurrentPage)) || !isPhraseSearching)
//                    {
//                        //get the length of the page
//                        Long lengthOfPage = wordsCount.get(linkOfCurrentPage);
//                        System.out.println("the link from the if  "+ linkOfCurrentPage +"\n");
//
//                        //to get the type of the word ==> paragraph or title or strong or header
//                        int separetorPosition = linksWithWordPosition[j].indexOf(',');
//                        char wordType = linksWithWordPosition[j].charAt(separetorPosition + 1);
//
//                        if (wordType == 't')                                   //title
//                            coeff = 1.0 / 2.0;
//                        else if (wordType == 'h' || wordType == 's')         //header or strong
//                            coeff = 1.0 / 4.0;
//                        else {//paragraph
//                            snipptes.add(linkOfCurrentPage);
//                            coeff = 1.0 / 8.0;
//                        }
//
//                        //to get number of occurrences of each word
//                        int countSeperator = linksWithWordPosition[j].indexOf("]::");
//                        String wordCount = linksWithWordPosition[j].substring(countSeperator + 3);
//                        numOfOccerrencesInCurrentDocument = coeff * Integer.parseInt(wordCount);
//                        numOfOccerrencesInAllDocuments += coeff * Integer.parseInt(wordCount);
//
//                        if (lengthOfPage != null && lengthOfPage != 0) {
//                            tf = Double.valueOf(numOfOccerrencesInCurrentDocument) / lengthOfPage;
//                            if (Links_numOfOccurrences.containsKey(linkOfCurrentPage)) {
//                                double tempTf = Links_numOfOccurrences.get(linkOfCurrentPage).doubleValue();
//                                tf += tempTf;
//                            } else {
//                                arr.add(linkOfCurrentPage);
//                            }
//                            Links_numOfOccurrences.put(linkOfCurrentPage, tf);
//                            System.out.println("the map of the links is "+Links_numOfOccurrences+"\n");
//                        }
//                        tf = 0;
//                    }
//                    //
//                }
//
//
//
//
//                counterForWords++;
//
//                //calculate the idf value of the page
//                idf = completeCount / Double.valueOf(numOfOccerrencesInAllDocuments);                                      // 5100 ==> number of indexed web pages
//
//                // the map will contain the link with its tf_idf
//                for (int h = 0; h < arr.size(); h++) {
//                    tf_idf=0;
//                    if(allLinksTf_Idf.containsKey(arr.get(h)))
//                    {
//                        tf_idf+=allLinksTf_Idf.get(arr.get(h));
//                    }
//                    else
//                    {
//                        uniqueLinks.add(arr.get(h));
//                    }
//
//                    tf_idf += idf * Links_numOfOccurrences.get(arr.get(h));
//                    allLinksTf_Idf.put(arr.get(h), tf_idf);
//                }
//                // to the new word
//                numOfOccerrencesInAllDocuments=0;
//
//            }
//
//
//            // snippets part
//            Map<String, String> linksParagraphs = connect.getLinksParagraphs(snipptes);
//
//
//
//            Map<String, String> UI_snippets = HelperClass.getSnippet(queryWords, snipptes, linksParagraphs);
//            ////
//            System.out.println("the map "+ allLinksTf_Idf+"\n \n \n");
//
//            int id;
//            double popularityResult;
//            for(int i=0;i<uniqueLinks.size();i++)
//            {
//                id=IDs.get(uniqueLinks.get(i));
//                popularityResult=HelperClass.readPopularity(id);
//                if(popularityResult!=-1) {
//                    allLinksTf_Idf.put(uniqueLinks.get(i), (0.7 * allLinksTf_Idf.get(uniqueLinks.get(i)) + 0.3 * popularityResult));
//                }
//            }
//            // will need to use the popularty here
//            allLinksTf_Idf.entrySet()
//                    .stream()
//                    .sorted(Map.Entry.comparingByValue())
//                    .forEachOrdered(x -> allLinksTf_Idf.put(x.getKey(), x.getValue()));
//            System.out.println("the map "+ allLinksTf_Idf+"\n \n \n");
//
//
//            for (Iterator<Map.Entry<String, Double>> iter = allLinksTf_Idf.entrySet().iterator(); iter.hasNext(); ) {
//
//
//                Map.Entry<String, Double> LinkEntry = iter.next();
//
//
//                String link = LinkEntry.getKey();
//
//                // Get description and populate Json Array
//                String description=" ";
//
//                JSONObject Jo = new JSONObject();
//                if(IDs.containsKey(link)) {
//                    int currentLinkID = IDs.get(link);
//                    description = HelperClass.readDescription(currentLinkID);
//                    Jo.put("Link", link);
//                }
//                if(description==null) description=" ";
//                Jo.put("Description", description);
//                finalJsonFile.put(Jo);
//            }
//
//
//            return finalJsonFile.toString();
//        }
//
//
//
//    }
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//  static class QueryProcessing{
//
//        //--------------------- The Data Members-------------------------//
//        DataBase dataBaseObject;
//        public String[] stopWords;
//
//        //--------------------- Constructor-----------------------------//
//
///*
//        * Explanation:
//            Constructor to initialize the object of the Database
//            Read Stop Words to use in further functions
//    */
//
//        public QueryProcessing() throws FileNotFoundException {
//            WorkingFiles.readStopWords();
//            dataBaseObject = new DataBase();
//            this.stopWords = WorkingFiles.getStopWordsAsArray();
//
//            System.out.println("The consturctor");
//
//        }
//
//        //--------------------------Function SplitQuery--------------------------//
//
///*
//        * Explanation:
//            Utility Function to divide the search query into the words constituting it
//    */
//
//        public static String[] SplitQuery(String searchQuery)
//        {
//            String[] subStrings = searchQuery.trim().split("\\s+");
//            return subStrings;
//        }
//
//
//        //--------------------------Function removeElement--------------------------//
//
///*
//        * Explanation:
//            Utility Function for removeStopWords, used to remove elements from array
//    */
//
//        public static String[] removeElement(String[] arr, int[] index) {
//            List<String> list = new ArrayList<>(Arrays.asList(arr));
//            for (int i=0; i<index.length;i++)
//            {
//                list.remove(new String(arr[index[i]]));
//            }
//
//            return list.toArray(String[]::new);
//        }
//
//        //--------------------------Function removeStopWords--------------------------//
//
///*
//        * Explanation:
//            Function used to remove all stop words from the Search Query
//    */
//
//        public static String[] removeStopWords(String[] searchQuery)
//        {
//            int length =searchQuery.length;
//            ArrayList<Integer> indeces = new ArrayList<Integer>();
//            for(int i = 0; i< length; i++)
//            {
//                if (Arrays.asList(this.stopWords).contains(searchQuery[i].toLowerCase()))
//                {
//                    indeces.add(i);
//                }
//            }
//            searchQuery = removeElement(searchQuery, indeces.stream().mapToInt(Integer::intValue).toArray());
//            System.out.printf("after remove \n");
//            return searchQuery;
//        }
//
//
//        //--------------------------Function searchInInvertedFiles--------------------------//
//
///*
//        * Explanation:
//            Function used to search inverted Files,to fetch results for Search Queries.
//    */
//
//        public static void searchInInvertedFiles(ArrayList<String> word, File myFile, ArrayList<String> results, boolean stemmingFlag) throws FileNotFoundException {
//            Scanner read = new Scanner(myFile);
//            System.out.printf("start search \n");
//            int counter = 0;
//
//            String tempInput,
//                    stemmedVersion = " ";
//
//
//            boolean wordIsFound = false;
//
//            int stopIndex;
//            int counter1=0;
//
//            while(read.hasNextLine())
//            {
//                counter1++;
//                System.out.printf("file line"+counter1);
//                tempInput = read.nextLine();
//                int i=0;
//                boolean anyWordFound = false;
//                while(!anyWordFound && i<word.size()) {
//                    anyWordFound = false;
//
//                    // stemming the word
//                    if (stemmingFlag)
//                        stemmedVersion = HelperClass.stemTheWord(word.get(i));
//
//                    if (tempInput.equals(""))
//                        continue;
//
//                    // check if this line is for a word or just an extension for the previous line
//                    if (tempInput.charAt(0) == '<')
//                    // compare to check if this tempWord = ourWord ?
//                    {
//                        // extract the word from the line that read by the scanner
//                        stopIndex = tempInput.indexOf('|');
//                        String theWord = tempInput.substring(1, stopIndex);
//
//                        // this condition for the targeted word
//                        if (!wordIsFound && theWord.equals(word.get(i).toLowerCase())) {
//                            results.add(counter++, tempInput);     // target word will have the highest priority
//                            wordIsFound = true;
//                            anyWordFound = true;                   //To not search with other words on same line
//                            continue;
//                        }
//
//
//                        //counter = 1;
//                        // comparing the stemmed version of the target word by the stemmed version of the word in the inverted file
//                        if (stemmingFlag) {
//                            if (stemmedVersion.equals(HelperClass.stemTheWord(theWord))) {
//                                results.add(counter++, tempInput);
//                                anyWordFound = true;
//                            }
//                        }
//                    }
//                    else
//                    {
//                        break;
//                    }
//                    i++;
//                }
//            }
//            System.out.printf("end search \n");
//        }
//
//        //--------------------------Function sortByValue--------------------------//
//
///*
//        * Explanation:
//            Static Function to sort a map descendingly by its values
//    */
//
//
//        public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm)
//        {
//            // Create a list from elements of HashMap
//            List<Map.Entry<String, Double> > list =
//                    new LinkedList<Map.Entry<String, Double> >(hm.entrySet());
//
//            // Sort the list
//            Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
//                public int compare(Map.Entry<String, Double> o1,
//                                   Map.Entry<String, Double> o2)
//                {
//                    return (o2.getValue()).compareTo(o1.getValue());
//                }
//            });
//
//            // put data from sorted list to hashmap
//            HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
//            for (Map.Entry<String, Double> aa : list) {
//                temp.put(aa.getKey(), aa.getValue());
//            }
//            return temp;
//        }
//
//
//        //--------------------------Function run--------------------------//
///*
//        * Explanation:
//            Returns a Json Array of all results,
//            * prepares for ranking by sending results
//            * Prepares for Highlighting websites content by dividing the query into its constituents
//    */
//
//
//        public void run(String message, ArrayList<String> queryLinesResult, JSONArray dividedQuery)
//                throws FileNotFoundException, JSONException {
//
//            //Used to save File names of current words
//            HashMap<String,ArrayList<String>> fileNames=new HashMap<String,ArrayList<String>>();
//
////Used to add each word together with the whole query; to populate dividedQuery array
//            ArrayList<String> words = new ArrayList<String>();
//            words.add(message);                    //First part of dividedQuery array
//            JSONObject divide = new JSONObject();  //Used for divided Query servlet to highlight content in results
//
//
//            String[] result = SplitQuery(message); //Splitting for words
//            result  = removeStopWords(result);     // Remove Stop Words from the query
//
//            JSONArray finalJsonFile = new JSONArray(); //For final results
//
//
//            // Loop over words
//            int length = result.length;
//            for(int i=0; i<length;i++) {
//
//
//                // Search for proper file name for each word
//                String fileName = "";
//                if (HelperClass.isProbablyArabic(result[i]))
//                    fileName = "arabic";
//                else if (result[i].length() == 2)
//                    fileName = "two";
//                else if (result[i].length() > 2) {
//                    fileName = "_" + result[i].substring(0, 3);
//
//                    // if the word is something like that => UK's
//                    File tempFile = new File(HelperClass.invertedFilePath_V3(fileName));
//                    if (!tempFile.exists()) {
//                        fileName = "others";
//                    }
//                }
//
//                if(!fileNames.containsKey(fileName)) {
//                    ArrayList<String> currentFileWords = new ArrayList<String>();
//                    currentFileWords.add(result[i]);
//                    fileNames.put(fileName, currentFileWords);
//                }
//                else{
//
//                    ArrayList<String> currentFileWords = fileNames.get(fileName);
//                    currentFileWords.add(result[i]);
//                    fileNames.put(fileName , currentFileWords);
//                }
//            }
//            //filenames has all words and file names it's assigned to.
//
//            //Loop over File names map to access all file names.
//            for (Iterator<Map.Entry<String, ArrayList<String>>> it = fileNames.entrySet().iterator(); it.hasNext(); ) {
//
//                Map.Entry<String, ArrayList<String>> currFile = it.next();
//
//                // Results for one file.
//                ArrayList<String> oneFileResult = new ArrayList<String>();
//
//                String filePath = "D:\\College\\Second_Year\\Second Term\\APT\\Project\\Final Version\\Final\\Sreach-Engine";   // get the directory of the project
//
//
//                filePath += File.separator + "InvertedFiles_V3" + File.separator;
//
//                filePath += currFile.getKey() + ".txt";    //To get File Name
//
//                File targetFile = new File(filePath);
//
//                //true to sepcify it's Query Processing not Phrase Searching
//                System.out.printf("before search \n");
//                searchInInvertedFiles(currFile.getValue(), targetFile,oneFileResult, true);
//                System.out.printf("after search \n");
//
//                // Loop over versions of Words
//                // Adding words results to ranker Array
//
//                int length_2 = oneFileResult.size();
//                for(int j = 0; j<length_2; j++)
//                {
//                    //Don't send to ranker
//                    if(oneFileResult.get(j).equals(""))
//                    {continue;}
//
//                    queryLinesResult.add(oneFileResult.get(j));
//                }
//
//            }
//            // Populate DividedQuery Array
//        }
//    }
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    static class DataBase {
//        private Connection connect;
//        private Statement stmt;
//
//        public DataBase() {
//            try {
//                try {
//                    Class.forName("com.mysql.cj.jdbc.Driver");
//                } catch (Exception e) {
//
//                }
//                connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/search-engine", "root", "");
//                this.stmt = connect.createStatement();
//                if (connect != null) {
//                    System.out.println("Connected to database");
//                } else {
//                    System.out.println("Cannot connect to database");
//                }
//
//            } catch (SQLException e) {
//
//            }
//        }
//// ---------------------------------------------------------------------------------------------------------------------//
//
//
//        //---------------------------------------get link Description  -------------------------------------------------------------//
//        public synchronized Boolean getDescription(String linkUrl, StringBuffer description) {
//            try {
//                //String query = "Select Link FROM links WHERE Id= " + ID +" ";
//                String query = "Select * FROM links";
//                ResultSet resultSet = this.stmt.executeQuery("Select Descripation FROM links WHERE Link= '" + linkUrl + "';");
//                resultSet.next();
//                String descriptionResult = resultSet.getString("Descripation");
//                description.append(descriptionResult);
//                return true;
//
//            } catch (SQLException e) {
//                return false;
//            }
//
//        }
//
//
//// ---------------------------------------------------------------------------------------------------------------------//
//
////----------------------------------------------------------------------------------------------------------------------//
//
//
//        //------------------------------------------get the completed urls------------------------------------------------------//
//        public synchronized int getCompleteCount() {
//            try {
//                ResultSet result = this.stmt.executeQuery("SELECT count(Link) as Number FROM links WHERE  Completed=1 ;");
//                int count = 0;
//                while (result.next()) {
//                    count = result.getInt("Number");
//                }
//                return count;
//            } catch (SQLException e) {
//            }
//            return 0;
//        }
////----------------------------------------------------------------------------------------------------------------------//
//
//
//        //---------------------------------------------get url and its related ID-------------------------------------------//
//        public String[] getAllUrls() {
//            int linksCount = getCompleteCount();
//            String[] completedLinks = new String[linksCount];
//            int i = 0;
//            try {
//                ResultSet rs = this.stmt.executeQuery("SELECT * FROM links where Completed = 1;");
//                while (rs.next()) {
//                    completedLinks[i++] = rs.getString("Link");
//                }
//                return completedLinks;
//            } catch (SQLException e) {
//                System.out.println(e);
//                return completedLinks;
//            }
//        }
//        //---------------------------------------------------------------------------------------------------------------------//
//
//
//        // get map of words count for all websites
//        public Map<String, Long> getWordsCountAsMap() {
//            Map<String, Long> resultMap = new HashMap<>();
//            try {
//                ResultSet resultSet = this.stmt.executeQuery("SELECT Link , wordCounts FROM links;");
//
//                while (resultSet.next()) {
//                    resultMap.put(resultSet.getString("Link"), resultSet.getLong("wordCounts"));
//                }
//                return resultMap;
//
//            } catch (SQLException e) {
//                return null;
//            }
//
//        }
//
//        //////////////////////////////////////////////////////////////////////////////////////////////////////
//        //-----------------------------------------------get the number of links out from the parent link-----------------------//
//        public int getParentLinksNum(String url)
//        {
//
//            try{
//                String qq= "SELECT LinkParent FROM links  where Link='"+url+"' ;";
//                ResultSet resultSet=this.stmt.executeQuery(qq );
//                while(resultSet.next())
//                {
//                    int parentId=resultSet.getInt("LinkParent");
//                    String q = "SELECT count(*) as Number FROM links  where LinkParent="+parentId+";";
//                    ResultSet resultSet2=this.stmt.executeQuery(qq );
//                    while (resultSet2.next() )
//                    {
//                        return resultSet2.getInt("Number");
//                    }
//                }
//            }
//            catch(SQLException e)
//            {
//                return -1;
//            }
//            return -1;
//        }
//        // ---------------------------------------------------------------------------------------------------------------------//
//        //--------------------------------------------------function to get the parent id----------------------------------------//
//        public synchronized String getParentLink(String url)
//        {
//            try{
//                ResultSet resultSet=this.stmt.executeQuery("SELECT LinkParent FROM links  where Link='"+url+"' ;" );
//                while(resultSet.next())
//                {
//                    int parentId=resultSet.getInt("LinkParent");
//                    ResultSet resultSet2 =this.stmt.executeQuery("SELECT * FROM links  where Id="+parentId+" ;" );
//                    while( resultSet2.next() )
//                    {
//                        String linkParent = resultSet2.getString("Link");
//                        return linkParent;
//                    }
//                    return "-1";
//                }
//
//            }
//            catch(SQLException e)
//            {
//                return null;
//            }
//            return null;
//        }
//        //-----------------------------------------------------------------------------------------------------------------------//
//
//        //--------------------------------------------------function to get the IDs of All Links----------------------------------------/
//        public Map<String, Integer> getIDsAsMap()
//        {
//            Map<String, Integer> resultMap = new HashMap<>();
//            try {
//                ResultSet resultSet = this.stmt.executeQuery("SELECT Link, Id FROM links;");
//
//                while (resultSet.next())
//                {
//                    resultMap.put(resultSet.getString("Link"), resultSet.getInt("Id"));
//                }
//                return resultMap;
//
//            } catch (SQLException e) {
//                return null;
//            }
//
//        }
//        //-----------------------------------------------------------------------------------------------------------------------//
//
//        //--------------------------------------------------function to get map of the needed links and its paragraphs----------------------------------------/
//
//        public Map<String, String> getLinksParagraphs(ArrayList<String> links)
//        {
//            int size = links.size();
//            Map<String, String> resultMap = new HashMap<>();
//            try {
//                String query = "SELECT Link, Paragraph FROM links where Link IN (";
//                int i;
//                for (i = 0; i < size - 1; i++)
//                    query += "'" + links.get(i) + "',";
//
//                // last link
//                query += "'" + links.get(i) + "');";
//
//                ResultSet resultSet = this.stmt.executeQuery(query);
//
//                while (resultSet.next())
//                {
//                    resultMap.put(resultSet.getString("Link"), resultSet.getString("Paragraph"));
//                }
//                return resultMap;
//
//            } catch (SQLException e) {
//                return null;
//            }
//        }
//
//
//    }
//    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    static class HelperClass {
//
//        // get the path of the inverted Files_V3
//        public static String invertedFilePath_V3(String fileName) {
////        String filePath = Paths.get("").normalize().toAbsolutePath().toString();
//            String filePath="D:\\College\\Second_Year\\Second Term\\APT\\Project\\Final Version\\Final\\Sreach-Engine";
//            //filePath = filePath.substring(0, .lastIndexOf("\\"));
//            filePath += File.separator + "InvertedFiles_V3" + File.separator + fileName + ".txt";
//            return filePath;
//        }
//
//        public static String populairtyFilesPath()
//        {
//            String filePath = "D:\\College\\Second_Year\\Second Term\\APT\\Project\\Final Version\\Final\\Sreach-Engine";
//            // filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
//            filePath += File.separator + "PopularityFiles";
//            return filePath;
//        }
//
//        // get the path of the content files
//        public static String contentFilesPath()
//        {
//            String filePath="D:\\College\\Second_Year\\Second Term\\APT\\Project\\Final Version\\Final\\Sreach-Engine";
//            // filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
//            filePath += File.separator + "ContentFiles";
//            return filePath;
//        }
//
//        // get the path of the description files
//        public static String descriptionFilesPath()
//        {
//            String filePath="D:\\College\\Second_Year\\Second Term\\APT\\Project\\Final Version\\Final\\Sreach-Engine";
//            // filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
//            filePath += File.separator + "descriptionFiles";
//            return filePath;
//        }
//
//        // get the content which stored in a file
//        public static String readContent(int fileName)
//        {
//            Path filePath = Path.of(contentFilesPath() + File.separator + fileName + ".txt");
//            String content = null;
//            try {
//                content = Files.readString(filePath);
//                return content;
//            } catch (IOException e) {
//                return null;
//            }
//        }
//
//        // get the description which stored in a file
//        public static String readDescription(int fileName)
//        {
//            Path filePath = Path.of(descriptionFilesPath() + File.separator + fileName + ".txt");
//            String content = null;
//            try {
//                content = Files.readString(filePath);
//                return content;
//            } catch (IOException e) {
//                return null;
//            }
//        }
//
//        // function to read popularity
//        public static double readPopularity(int fileName)
//        {
//            Path filePath = Path.of(populairtyFilesPath() + File.separator + fileName + ".txt");
//            String content = null;
//            try {
//                content = Files.readString(filePath);
//            return Double.parseDouble(content);
//            } catch (IOException e) {
//                return -1;
//            }
//        }
//
//
//        // stem the word using Porter Stemmer Lib
//        public static String stemTheWord(String word) {
//            PorterStemmer stemObject = new PorterStemmer();
//            stemObject.setCurrent(word);
//            stemObject.stem();
//            return stemObject.getCurrent();
//        }
//
//        // check if the word is arabic
//        public static boolean isProbablyArabic(String s) {
//            for (int i = 0; i < s.length(); ) {
//                int c = s.codePointAt(i);
//                if (c >= 0x0600 && c <= 0x06E0)
//                    return true;
//                i += Character.charCount(c);
//            }
//            return false;
//        }
//
//        // this function gets the snippets of the result links based on the query that the user entered
//        public static Map<String, String> getSnippet(String[] queryWords, ArrayList<String> resultLinks, Map<String, String> linkParagraphs)
//        {
//            Map<String, String> result = new HashMap<>();
//            int wordsSize = queryWords.length,
//                    linksSize = resultLinks.size();
//
//            String currentParagraph = null,
//                    snippetParagraph = null,
//                    fullParagraphs   = null;
//
//            for (int i = 0; i < wordsSize; i++)
//            {
//                for (int j = 0; j < linksSize; j++)
//                {
//                    String currentLink = resultLinks.get(j);
//                    if (result.containsKey(currentLink)) // we need just one snippet for each link, so if we already get a snippet ,then continue
//                        continue;
//
//                    fullParagraphs = linkParagraphs.get(currentLink);
//                    // split paragraphs
//                    if (fullParagraphs != null)
//                    {
//                        String[] separatedParagraphs = fullParagraphs.split("\\S.&\\S");
//
//                        int size = separatedParagraphs.length;
//
//                        for (int k = 0; k < size; k++)
//                        {
//                            currentParagraph = separatedParagraphs[k];
//                            if (currentParagraph.contains(queryWords[i]))
//                            {
//                                if(currentParagraph.charAt(0) == '[')
//                                    currentParagraph = currentParagraph.substring(1);
//
//                                result.put(currentLink, splitTo30Words(currentParagraph));
//                                break;      // because i need just one snippet, if found don't continue to the other paragraphs in this link
//                            }
//                        }
//
//                    }
//                }
//            }
//            return result;
//        }
//
//        // this functions get only 30 words from the text
//        public static String splitTo30Words(String str)
//        {
//            String[] arr = str.split(" ");
//
//            if (arr.length <= 30)
//                return str;
//
//            String result = arr[0];
//            for (int i = 1; i < 30; i++)
//                result += " " + arr[i];
//
//            return result;
//        }
//
//
//    }
//
//    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    static class WorkingFiles {
//        private static String[] stopWords;
//
//        //--------------------------Function readStopWords--------------------------//
//    /*
//        * Explanation:
//            Utility Function to divide the search query into the words constituting it
//    */
//        public static void readStopWords() throws FileNotFoundException {
//            // open the file that contains stop words
//            String filePath="D:\\College\\Second_Year\\Second Term\\APT\\Project\\Final Version\\Final\\Sreach-Engine";   // get the directory of the project
//            //filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
//            filePath += File.separator + "helpers" + File.separator + "stop_words.txt";
//            File myFile = new File(filePath);
//
//            stopWords = new String[851];
//
//            // read from the file
//            Scanner read = new Scanner(myFile);
//            String tempInput;
//            int counter = 0;
//            while (read.hasNextLine()) {
//                tempInput = read.nextLine();
//                stopWords[counter++] = tempInput;
//            }
//            read.close();
//
//        }
//
//        //get Stop Words as Array
//        public static String[] getStopWordsAsArray() {
//            return stopWords;
//        }
//    }
//
//
//
//}