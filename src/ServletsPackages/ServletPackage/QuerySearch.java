
package ServletsPackages.ServletPackage;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;


import org.json.JSONException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.json.*;
import com.mysql.jdbc.*;
import org.tartarus.snowball.ext.PorterStemmer;


public class QuerySearch extends HttpServlet {
    public String searchingQuery;
    public ArrayList<String> rankerArray=new ArrayList<String>();
    public JSONArray dividedQuery=new JSONArray();
    Ranker rankerObject = new Ranker();
    int count=0;


    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.addHeader("Access-Control-Allow-Origin", "*");
        System.out.println("any thing here ");
        String searchingQuery = req.getParameter("query");
        res.setContentType("text/html");
        String results="";
        DataBase dataBaseObj = new DataBase();
        count=dataBaseObj.getCompleteCount();

        //WorkingFiles workingFilesObj = new WorkingFiles(5615);
        if (searchingQuery.startsWith("\"") && searchingQuery.endsWith("\"")) {

            //call the function of the phrase searching
            res.getWriter().println("phrase"+count);


//            PhraseSearching obj = new PhraseSearching();
//
//            try {
////                 results  =obj.run(searchingQuery,rankerArray,dividedQuery);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        } else {
            //call function of query processing
//            res.getWriter().println("query"+count);

            QueryProcessing obj = new QueryProcessing();
            try {
                 results  =obj.run(searchingQuery,rankerArray,dividedQuery);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
            //Trying Ranker but There's an error from file path not found, needs to be fixed
            //The error is for Mustafa to check

//            try {
//                Map<Integer,Double> rankingResult= rankerObject.calculateRelevance(rankerArray);
//
//                //Passed Map to HashMap constructor, Probably an error
//                HashMap<Integer,Double> toBeSorted = new HashMap<Integer,Double>(rankingResult);
//                HashMap<Integer,Double> sortedRankerMap = QueryProcessing.sortByValue(toBeSorted);
//                HashMap<String,Double> linksRankedMap = QueryProcessing.replaceIDByLink(toBeSorted);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            res.getWriter().println(results);

        }
//        //Ranker
//        res.getWriter().println(results.toString());
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    static class PhraseSearching {
        DataBase dataBaseObject = new DataBase();

        private Map<String, File> invertedFiles;
        PorterStemmer stemObject = new PorterStemmer();
        String[] stopWords;


        public PhraseSearching() throws FileNotFoundException {
            readStopWords();
            System.out.println("Phrase Searching consturctor");
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

        private void readStopWords() throws FileNotFoundException {
            // open the file that contains stop words
            String filePath = "D:\\Study\\Second Year\\Second Sem\\APT\\New folder (2)\\New folder (2)\\Sreach-Engine";   // get the directory of the project
            System.out.println(filePath);
            String finalfilePath = filePath.substring(0, filePath.lastIndexOf("\\")+1);
            System.out.println(finalfilePath);
            finalfilePath += File.separator + "helpers" + File.separator + "stop_words.txt";
            File myFile = new File(finalfilePath);

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


        public String run(String message, ArrayList<String> queryLinesResult, JSONArray dividedQuery) throws FileNotFoundException, JSONException {

            System.out.println("Phrase Searching Run Function");
            boolean[] indexProcessed;
            Map<Integer, Integer> allIDs = new HashMap<Integer, Integer>();
            JSONObject divide = new JSONObject();
            divide.put("Results", message);
            dividedQuery.put(divide);


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


                String fileName = "";
                if (HelpersPackages.Helpers.HelperClass.isProbablyArabic(result[i]))
                    fileName = "arabic";
                else if(result[i].length() == 2)
                    fileName = "two";

                else
                    fileName = "_" + result[i].substring(0,3);


                // Mustafa : I edited this code
                String filePath ="D:\\Study\\Second Year\\Second Sem\\APT\\New folder (2)\\New folder (2)\\Sreach-Engine";   // get the directory of the project

                // Delete last Directory to get path of Inverted Files
                String finalFilePath = filePath.substring(0, filePath.lastIndexOf("\\"));

                finalFilePath += File.separator + "InvertedFiles_V3" + File.separator;

                finalFilePath += fileName + ".txt";
                //System.out.println(finalFilePath + "From Search Inverted Files");
                File targetFile = new File(finalFilePath);

                QueryProcessingPackages.Query.QueryProcessing.searchInInvertedFiles(result[i], targetFile,oneWordResult, false);

                int length_2 = oneWordResult.size();
                for (int j = 0; j < length_2; j++) {

                    if(oneWordResult.get(j).equals(""))
                    {continue;}
                    // Should we let this be like that? Or should it be just links from map? I don't know
                    queryLinesResult.add(oneWordResult.get(j));
                    // Loop over versions of Words


                    String[] splitLine = oneWordResult.get(j).split("\\[");
                    int length_3 = splitLine.length;
                    for (int k = 1; k < length_3; k++) {

                        // Loop over links of the same version of each Word

                        int End = splitLine[k].indexOf(']');
                        String temp = splitLine[k].substring(0, End);

                        String[] finalID = temp.split(",");
                        int ID = Integer.parseInt(finalID[0]);
                        if (i == 0 && !indexProcessed[i]) {
                            allIDs.put(ID, 1);
                            if(k == length_3-1)
                            {
                                indexProcessed[0] = true;
                            }
                        }
                        else if (!indexProcessed[i] && allIDs.containsKey(ID)) {
                            allIDs.put(ID, 1 + allIDs.get(ID));
                            if(k == length_3-1)
                            {
                                indexProcessed[i] = true;
                            }
                        }
                    }
                }

            }

            for (Iterator<Map.Entry<Integer, Integer>> it = allIDs.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Integer, Integer> entry = it.next();
                if (entry.getValue() < length) {
                    it.remove();
                }
            }

            for (Iterator<Map.Entry<Integer, Integer>> iter = allIDs.entrySet().iterator(); iter.hasNext(); ) {


                Map.Entry<Integer, Integer> IDEntry = iter.next();

                StringBuffer link = new StringBuffer("");
                StringBuffer description = new StringBuffer("");
                JSONObject Jo = new JSONObject();
                //dataBaseObject.getLinkByID(IDEntry.getKey(), link, description);
                Jo.put("Link", link);
                Jo.put("Description", description);
                finalJsonFile.put(Jo);
            }

            return finalJsonFile.toString();
        }
    }



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    static class Ranker
    {
        private DataBasePackages.DataBase.DataBase connect = new DataBasePackages.DataBase.DataBase();
        JSONArray dividedQuery = new JSONArray();
        Map<String,Long> wordsCount;
        String[] completedLinks;
        Map<String, Double> popularityResult;
        int completeCount;

        public Ranker()
        {
            System.out.printf("From ranker constructor");
            wordsCount = connect.getWordsCountAsMap();
            completedLinks = connect.getAllUrls();
            completeCount=connect.getCompleteCount();
            popularityResult=calculatePopularity(completeCount);
        }


        public Map<String, Double> calculatePopularity(double totalNodes)             //Popularity
        {

            //pagesRank1 ==> store the final results of popularity ( each page and its popularity )
            Map<String, Double> pagesRank1 = new HashMap<String, Double>();

            //TempPageRank is used to store values of pagesRank1 in it temporarily
            Map<String, Double> TempPageRank = new HashMap<String, Double>();

            //calculate the initial value of popularity for all pages
            double InitialPageRank = 1.0 / totalNodes;

            // initialize the rank of each page //
            for (int k = 1; k <= totalNodes; k++)
                pagesRank1.put(completedLinks[k], InitialPageRank);

            //ITERATION_STEP is used to iterate twice following PageRank Algorithm steps
            int ITERATION_STEP = 1;
            while (ITERATION_STEP <= 2) {

                // Store the PageRank for All Nodes in Temporary Map
                for (int k = 71; k <= totalNodes; k++) {
                    TempPageRank.put(completedLinks[k], pagesRank1.get(completedLinks[k]));
                    pagesRank1.put(completedLinks[k], 0.0);
                }

                //tempSum is the difference between all pages popularity and 1 ==> the difference is divided by the No. of links that didn't have parents
                double tempSum = 0;
                int counter=0;

                //Special handling for the pages only as there is no outgoing links to it
                double temp = 1 - tempSum;

                //slice of each link of them
                double slice = temp / 70;//ToDo: chang it later

                //if the link don't have parents
                for ( int i=1 ; i<=70 ; i++ )//ToDo: chang it later
                {
                    pagesRank1.put(completedLinks[i] , slice);
                }

                //increase the ITERATION_STEP
                ITERATION_STEP++;
            }

            // Add the Damping Factor to PageRank
            double DampingFactor = 0.75;
            double temp = 0;
            for (int k = 0; k < totalNodes; k++) {
                temp = (1 - DampingFactor) + DampingFactor * pagesRank1.get(completedLinks[k]);
                pagesRank1.put(completedLinks[k], temp);
            }

            return pagesRank1;
        }



        public Map<String, Double> calculateRelevance(ArrayList<String> tempLines) throws FileNotFoundException, JSONException

        {
            Map<String , Double> pagesRanks = new HashMap<String, Double>();
            double  tf = 0.0,
                    idf=0.0,
                    tf_idf=0.0,
                    numOfOccerrencesInCurrentDocument = 0.0, // this value will be weighted
                    numOfOccerrencesInAllDocuments=0.0;
            int counterForWords=0;
            //tempMap is used to store each link and its tf-idf value
            Map<String , Double> allLinksTf_Idf = new HashMap<String, Double>();
            ArrayList<String> uniqueLinks = new ArrayList<String>();



            for (int i = 0; i < tempLines.size(); i++) {
                Map<String, Double> Links_numOfOccurrences = new HashMap<String, Double>();
                //to make priority between title,header,paragraph
                double coeff = 0.0;

                int startIndex = tempLines.get(i).indexOf('|');
                String lineWithoutTheWord = tempLines.get(i).substring(startIndex + 1);
                String[] linksWithWordPosition = lineWithoutTheWord.split(";");


                //array to store all links of the current query
                ArrayList<String> arr = new ArrayList<String>();
                int counter =0;
                //iterate over the links of each word in the query
                for (int j = 0; j < linksWithWordPosition.length; j++) {

                    //to get id of current page
                    int bracePosition = linksWithWordPosition[j].indexOf('[');
                    String linkOfCurrentPage = linksWithWordPosition[j ].substring(bracePosition + 1, linksWithWordPosition[j ].indexOf(','));


                    //get the length of the page
                    Long lengthOfPage = wordsCount.get(linkOfCurrentPage);


                    //to get the type of the word ==> paragraph or title or strong or header
                    int separetorPosition = linksWithWordPosition[j ].indexOf(',');
                    char wordType = linksWithWordPosition[j].charAt(separetorPosition + 1);

                    if (wordType == 't')                                   //title
                        coeff = 1.0 / 2.0;
                    else if (wordType == 'h' || wordType == 's')         //header or strong
                        coeff = 1.0 / 4.0;
                    else                                                    //paragraph
                        coeff = 1.0 / 8.0;

                    //to get number of occurrences of each word
                    int countSeperator = linksWithWordPosition[j].indexOf("]:");
                    String wordCount = linksWithWordPosition[j].substring(countSeperator + 2);
                    numOfOccerrencesInCurrentDocument=  coeff * Integer.parseInt(wordCount);
                    numOfOccerrencesInAllDocuments+=coeff * Integer.parseInt(wordCount);

                    if (lengthOfPage != null && lengthOfPage != 0) {
                        tf = Double.valueOf(numOfOccerrencesInCurrentDocument) / lengthOfPage;
                        if(Links_numOfOccurrences.containsKey(linkOfCurrentPage))
                        {
                            double tempTf=Links_numOfOccurrences.get(linkOfCurrentPage).doubleValue();
                            tf+=tempTf;
                        }
                        else
                        {
                            arr.add(linkOfCurrentPage);
                        }
                        Links_numOfOccurrences.put(linkOfCurrentPage, tf);
                    }
                    tf = 0;
                }

                counterForWords++;

                //calculate the idf value of the page
                idf = completeCount / Double.valueOf(numOfOccerrencesInAllDocuments);                                      // 5100 ==> number of indexed web pages

                // the map will contain the link with its tf_idf
                for (int h = 0; h < arr.size(); h++) {
                    tf_idf=0;
                    if(allLinksTf_Idf.containsKey(arr.get(h)))
                    {
                        tf_idf+=allLinksTf_Idf.get(arr.get(h));
                    }
                    else
                    {
                        uniqueLinks.add(arr.get(h));
                    }

                    tf_idf += idf * Links_numOfOccurrences.get(arr.get(h));
                    allLinksTf_Idf.put(arr.get(h), tf_idf);
                }
                // to the new word
                numOfOccerrencesInAllDocuments=0;

            }
            System.out.println("the map "+ allLinksTf_Idf+"\n \n \n");


            for(int i=0;i<uniqueLinks.size();i++)
            {
                allLinksTf_Idf.put(uniqueLinks.get(i),(0.7*allLinksTf_Idf.get(uniqueLinks.get(i))+0.3*popularityResult.get(uniqueLinks.get(i))));
            }
            // will need to use the popularty here
            allLinksTf_Idf.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .forEachOrdered(x -> allLinksTf_Idf.put(x.getKey(), x.getValue()));
            System.out.println("the map "+ allLinksTf_Idf+"\n \n \n");


            return allLinksTf_Idf;

        }

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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

            // Results for one word.
            ArrayList<String> oneWordResult = new ArrayList<String>();


            // Search for proper file name for each word
            String fileName = "";
            if (HelperClass.isProbablyArabic(result[i]))
                fileName = "arabic";
            else if(result[i].length() == 2)
                fileName = "two";
            else if(result[i].length() > 2)
            {
                fileName = "_" + result[i].substring(0,3);

                // if the word is something like that => UK's
                File tempFile = new File(HelperClass.invertedFilePath_V3(fileName));
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
            Map<String,ArrayList<String>> strings=new HashMap<String,ArrayList<String>>();

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


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    static class DataBase {
        private Connection connect;
        private Statement stmt;
        public DataBase()
        {
            try{
                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                }
                catch(Exception e)
                {

                }
                connect=DriverManager.getConnection("jdbc:mysql://localhost:3306/search-engine","root","");
                this.stmt=connect.createStatement();
                if (connect != null) {
                    System.out.println("Connected to database");
                } else {
                    System.out.println("Cannot connect to database");
                }

            }
            catch(SQLException e)
            {

            }
        }

        //--------------------------------------Create Link --------------------------------------------------------------------//
        public synchronized void createLink(String Link,int Layer,String ThreadName,int ParentId)
        {
            try{
                this.stmt.executeUpdate("INSERT INTO links (Link, Layer, ThreadName, LinkParent,Completed) VALUES ('"+Link+"', '"+Layer+"', '"+ThreadName+"', "+ParentId+",'"+0+"');");
            }
            catch(SQLException e)
            {
            }
        }
//----------------------------------------------------------------------------------------------------------------------//

// --------------------------------------Update Link to Complete -------------------------------------------------------//

        public synchronized void urlCompleted(String Link)
        {
            try{
                this.stmt.executeUpdate("UPDATE links SET Completed=1 WHERE link='"+Link+"'");
            }
            catch(SQLException e)
            {
            }
        }
//----------------------------------------------------------------------------------------------------------------------//

        // --------------------------------------Update Link to Complete -------------------------------------------------------//

// --------------------------------------Set and Get Thread Position -------------------------------------------------------//

        public synchronized void setThreadPosition(String ThreadName,int Layer,int Index)
        {
            try{
                if(Layer==1)
                {
                    this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
                    this.stmt.executeUpdate("UPDATE threads SET UrlIndex="+Index+" WHERE ThreadName='"+ThreadName+"';");

                }
                else if (Layer==2)
                {

                    this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
                    this.stmt.executeUpdate("UPDATE threads SET UrlIndex1="+Index+" WHERE ThreadName='"+ThreadName+"';");

                }
                else if (Layer==3)
                {


                    this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
                    this.stmt.executeUpdate("UPDATE threads SET UrlIndex2="+Index+" WHERE ThreadName='"+ThreadName+"';");
                }
                else if (Layer==4)
                {

                    this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
                    this.stmt.executeUpdate("UPDATE threads SET UrlIndex3="+Index+" WHERE ThreadName='"+ThreadName+"';");
                }
                else{
                    this.stmt.executeUpdate("UPDATE threads SET Layer=1 WHERE ThreadName='"+ThreadName+"';");
                    this.stmt.executeUpdate("UPDATE threads SET  UrlIndex=0 WHERE ThreadName='"+ThreadName+"';");
                    this.stmt.executeUpdate("UPDATE threads SET  UrlIndex1=0  WHERE ThreadName='"+ThreadName+"';");
                    this.stmt.executeUpdate("UPDATE threads SET  UrlIndex2=0 WHERE ThreadName='"+ThreadName+"';");
                    this.stmt.executeUpdate("UPDATE threads SET   UrlIndex3=0 WHERE ThreadName='"+ThreadName+"';");



                }
            }
            catch(SQLException e)
            {
            }
        }

        public synchronized ResultSet getThreadPosition(String ThreadName)
        {
            try{
                ResultSet resultSet=this.stmt.executeQuery("SELECT * FROM threads WHERE ThreadName='"+ThreadName+"'");
                return resultSet;
            }
            catch(SQLException e)
            {
                return null;
            }
        }
//----------------------------------------------------------------------------------------------------------------------//

        public synchronized ResultSet getUrls(String Url)
        {
            try{
                return this.stmt.executeQuery("SELECT * FROM links WHERE Link='"+Url+"' AND Completed = 1");
            }
            catch(SQLException e)
            {
                return null;
            }
        }
        //---------------------------------------------get the url similar to the url-------------------------------------------//
        public synchronized ResultSet getUrls2(String Url)
        {
            try{
                return this.stmt.executeQuery("SELECT * FROM links WHERE Link='"+Url+"';");
            }
            catch(SQLException e)
            {
                return null;
            }
        }
// ---------------------------------------------------------------------------------------------------------------------//


        //---------------------------------------get link by ID  -------------------------------------------------------------//
        public synchronized Boolean getDescription (String linkUrl, StringBuffer description)
        {
            try{
                //String query = "Select Link FROM links WHERE Id= " + ID +" ";
                String query = "Select * FROM links";
                ResultSet resultSet = this.stmt.executeQuery("Select Descripation FROM links WHERE Link= '" + linkUrl +"';");
                resultSet.next();
                String descriptionResult = resultSet.getString("Descripation");
                description.append(descriptionResult);
                return true;

            } catch (SQLException e) {
                return false;
            }

        }



// ---------------------------------------------------------------------------------------------------------------------//


// --------------------------------------get the id of the link  -------------------------------------------------------//

        public synchronized int getId (String Url,String ThreadName)
        {
            try{
                ResultSet resultSet=this.stmt.executeQuery("SELECT * FROM links WHERE Link='"+Url+"' AND ThreadName='"+ThreadName+"' AND Completed=0 ;");
                while (resultSet.next())
                {
                    int Id=-1;
                    Id=resultSet.getInt("Id");
                    return  Id;
                }
            }
            catch(SQLException e)
            {

            }
            return -1;
        }
//----------------------------------------------------------------------------------------------------------------------//

        //-----------------------------------------get the family of the link --------------------------------------------------//
        public synchronized ResultSet getParentUrl (String ThreadName,StringBuffer parentLink , StringBuffer grandLink , String link,int Layer)
        {
            try {
//            if (Layer == 1) {
                ResultSet resultSet = this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='" + ThreadName + "' AND Layer=1 AND Completed=0;");
                System.out.printf("SELECT * FROM links WHERE  ThreadName='" + ThreadName + "' AND Layer=" + Layer + " AND Completed=0;");
                while (resultSet.next()) {
                    grandLink.append(resultSet.getString("Link"));
                    return this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='" + ThreadName + "' AND Layer=1 AND Completed=0;");
                }

//                ResultSet resultSet2= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=1;");
//                while(resultSet2.next())
//                {
//                    grandLink.append(resultSet2.getString("Link"));
//                    return this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=1;");
//                }
                //If the parent  link is completed
                Thread.currentThread().interrupt();
//            }
            }
            catch (SQLException e)
            {

            }
//            else if(Layer==2)
//            {
//                ResultSet resultSet= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=0;");
//                while(resultSet.next())
//                {
//                    resultSet=this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
//                    while(resultSet.next())
//                    {
//                        parentLink.append(resultSet.getString("Link"));
//                        return this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
//                    }
//
//                }
//                //If the parent  link is completed
//                Thread.currentThread().interrupt();
//            }
//            else if (Layer==3||Layer-1==3)
//            {
//                Layer-=1;
//                ResultSet resultSet= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=0;");
//                while(resultSet.next())
//                {
//                    resultSet =this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
//                    while(resultSet.next())
//                    {
//                        parentLink.append(resultSet.getString("Link"));
//                        Layer=resultSet.getInt("Layer");
//                        resultSet =this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
//                        while(resultSet.next())
//                        {
//                            grandLink.append(resultSet.getString("Link"));
//                            return this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
//                        }
//
//                    }
//
//
//                }
//                //If the parent  link is completed
//                Thread.currentThread().interrupt();
//            }
//        }
//        catch(SQLException e)
//        {
//            return null;
//
//        }
            return null;
        }
//----------------------------------------------------------------------------------------------------------------------//




        //------------------------------------------get the completed urls------------------------------------------------------//
        public synchronized int getCompleteCount ()
        {
            try
            {
                ResultSet result =this.stmt.executeQuery("SELECT count(Link) as Number FROM links WHERE  Completed=1 ;");
                int count=0;
                while(result.next())
                {
                    count=result.getInt("Number");
                }
                return count;
            }
            catch(SQLException e)
            {
            }
            return 0;
        }
//----------------------------------------------------------------------------------------------------------------------//

        public java.sql.Date getMaxDate ()
        {
            try
            {
                ResultSet result =this.stmt.executeQuery("SELECT max(LastTime) as Time FROM links;");
                java.sql.Date count=null;
                while(result.next())
                {
                    count = result.getDate("columnName");
                }
                return count;
            }
            catch(SQLException e)
            {
            }
            return null;
        }

        //---------------------------------------------get url and its related ID-------------------------------------------//
        public String[] getAllUrls()
        {
            int linksCount = getCompleteCount();
            String[] completedLinks = new String[linksCount];
            int i = 0;
            try{
                ResultSet rs = this.stmt.executeQuery("SELECT * FROM links where Completed = 1;" );
                while (rs.next())
                {
                    completedLinks[i++] = rs.getString("Link");
                }
                return completedLinks;
            }
            catch(SQLException e)
            {
                System.out.println(e);
                return completedLinks;
            }
        }

        // ---------------------------------------------------------------------------------------------------------------------//
        //-----------------------------------------------get the number of links out from the parent link-----------------------//
        public int getParentLinksNum(String url)
        {

            try{
                String qq= "SELECT LinkParent FROM links  where Link='"+url+"' ;";
                ResultSet resultSet=this.stmt.executeQuery(qq );
                while(resultSet.next())
                {
                    int parentId=resultSet.getInt("LinkParent");
                    String q = "SELECT count(*) as Number FROM links  where LinkParent="+parentId+";";
                    ResultSet resultSet2=this.stmt.executeQuery(qq );
                    while (resultSet2.next() )
                    {
                        return resultSet2.getInt("Number");
                    }
                }
            }
            catch(SQLException e)
            {
                return -1;
            }
            return -1;
        }
        // ---------------------------------------------------------------------------------------------------------------------//
        //--------------------------------------------------function to get the parent id----------------------------------------//
        public synchronized String getParentLink(String url)
        {
            try{
                ResultSet resultSet=this.stmt.executeQuery("SELECT LinkParent FROM links  where Link='"+url+"' ;" );
                while(resultSet.next())
                {
                    int parentId=resultSet.getInt("LinkParent");
                    ResultSet resultSet2 =this.stmt.executeQuery("SELECT * FROM links  where Id="+parentId+" ;" );
                    while( resultSet2.next() )
                    {
                        String linkParent = resultSet2.getString("Link");
                        return linkParent;
                    }
                }

            }
            catch(SQLException e)
            {
                return null;
            }
            return null;
        }
        //-----------------------------------------------------------------------------------------------------------------------//
        //-----------------------------------------------Add Link descripation--------------------------------------------------//
        public  synchronized void addDesc(int id,String desc)
        {
            try {
                this.stmt.executeUpdate("UPDATE links SET Descripation='" + desc + "' WHERE Id=" + id + ";");
            }
            catch(SQLException e)
            {

            }
        }
        // ---------------------------------------------------------------------------------------------------------------------//
        //------------------------------------------function to add paragraphs and headers and title and itemlists-------------//
        public synchronized void addElements(int id,String paragraphs,String title,String headers,String itemLists,String strong)
        {
            // System.out.printf("UPDATE links SET Paragraph='" + paragraphs + "' WHERE Id=" + id + ";");
            try {
                this.stmt.executeUpdate("UPDATE links SET Paragraph='" + paragraphs + "' WHERE Id=" + id + ";");
                this.stmt.executeUpdate("UPDATE links SET Title='" + title + "' WHERE Id=" + id + ";");
                this.stmt.executeUpdate("UPDATE links SET Headers='" + headers + "' WHERE Id=" + id + ";");
                this.stmt.executeUpdate("UPDATE links SET ListItems='" + itemLists + "' WHERE Id=" + id + ";");
                this.stmt.executeUpdate("UPDATE links SET Strong='" + strong + "' WHERE Id=" + id + ";");
            }
            catch (SQLException e)
            {

            }
        }
        //---------------------------------------------------------------------------------------------------------------------//
        //-----------------------------------------------get Link Content--------------------------------------------------//
        public synchronized String getContent(int id)
        {
            try {
//            System.out.println("SELECT CONCAT(Paragraph,Headers,Title,Strong,ListItems) as 'content' FROM `links` WHERE Id="+id+";");
                ResultSet resultSet=this.stmt.executeQuery("SELECT CONCAT(Paragraph,Headers,Title,Strong,ListItems) as 'content' FROM `links` WHERE Id="+id+";");
                while(resultSet.next())
                {
                    return resultSet.getString("content");
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            return "none";
        }
        // ---------------------------------------------------------------------------------------------------------------------//
        //------------------------------------------get Links Contents----------------------------------------------------------//
        public synchronized ResultSet getContents(String content , int id)
        {
            try {
                ResultSet resultSet=this.stmt.executeQuery("Select * From links as K ,links as J where CONCAT(K.Paragraph,K.Headers,K.Strong,K.ListItems)=CONCAT(J.Paragraph,J.Headers,J.Strong,J.ListItems) AND K.Id="+id+" AND K.Id!=J.Id;");
                while(resultSet.next())
                {
                    this.stmt.executeUpdate("Delete from links where Id="+id+";");
                }
                return null;
            }
            catch(SQLException e)
            {

            }
            return null;
        }
        //----------------------------------------------------------------------------------------------------------------------//

        // get the title of a website
        public synchronized String getTitle(String url)
        {
            try {
                System.out.println(Thread.currentThread().getName()+   ":   Title: "  + url);
                String q = "Select Title From links where Link = '" + url + "'";
                ResultSet resultSet=this.stmt.executeQuery(q);
                while(resultSet.next())
                {
                    return resultSet.getString("Title");
                }
            }
            catch(SQLException e)
            {

            }
            return null;
        }

        // get the Paragraphs of a website
        public synchronized String getParagraphs(String url)
        {
            try {
                System.out.println(Thread.currentThread().getName()+   ":   Paragraph: " + url);
                ResultSet resultSet=this.stmt.executeQuery("Select Paragraph From links where Link = '" + url+ "'");
                while(resultSet.next())
                {
                    return resultSet.getString("Paragraph");
                }
            }
            catch(SQLException e)
            {

            }
            return null;
        }
        // get the Headers of a website
        public synchronized String getHeaders(String url)
        {
            try {
                System.out.println(Thread.currentThread().getName()+   ":   Headers: " + url);
                ResultSet resultSet=this.stmt.executeQuery("Select Headers From links where Link = '" + url+ "'");
                while(resultSet.next())
                {
                    return resultSet.getString("Headers");
                }
            }
            catch(SQLException e)
            {

            }
            return null;
        }
        // get the ListItems of a website
        public synchronized String getListItems(String url)
        {
            try {
                ResultSet resultSet=this.stmt.executeQuery("Select ListItems From links where Link = '" + url+ "'");
                while(resultSet.next())
                {
                    return resultSet.getString("ListItems");
                }
            }
            catch(SQLException e)
            {

            }
            return null;
        }
        // get the Strongs of a website
        public synchronized String getStrongs(String url)
        {
            try {
                ResultSet resultSet=this.stmt.executeQuery("Select Strong From links where Link = '" + url+ "'");
                while(resultSet.next())
                {
                    return resultSet.getString("Strong");
                }
            }
            catch(SQLException e)
            {

            }
            return null;
        }
        // Add Words Count of a website
        public synchronized void addWordsCount(String link, long count)
        {
            String query = "UPDATE links SET WordCounts = " + count + " WHERE Link = '" + link + "';";

            try {
                this.stmt.executeUpdate(query);
            }
            catch (SQLException e)
            {
                System.out.println("Error while adding the words count of the website : " + link);
            }
        }

        // get map of words count for all websites
        public Map<String, Long> getWordsCountAsMap()
        {
            Map<String, Long> resultMap = new HashMap<>();
            try {
                ResultSet resultSet = this.stmt.executeQuery("SELECT * FROM links;");

                while (resultSet.next())
                {
                    resultMap.put(resultSet.getString("Link"), resultSet.getLong("wordCounts"));
                }
                return resultMap;

            } catch (SQLException e) {
                return null;
            }

        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////



    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static class HelperClass {



        // get the path of the inverted Files_V3
        public static String invertedFilePath_V3(String fileName)
        {
//        String filePath = Paths.get("").normalize().toAbsolutePath().toString();
            String filePath = "D:\\Study\\Second Year\\Second Sem\\APT\\New folder (2)\\New folder (2)\\Sreach-Engine";
            //filePath = filePath.substring(0, .lastIndexOf("\\"));
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

                        String path = QueryDivide.HelperClass.invertedFilePath_V3(currentFileName);
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
            String path = QueryDivide.HelperClass.invertedFilePath_V3(currentFileName);
            File myObj = new File(path);
            try {
                myObj.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to create the file");
            }

            // create a file for Arabic words
            currentFileName = "arabic";
            path = QueryDivide.HelperClass.invertedFilePath_V3(currentFileName);
            File myObj_2 = new File(path);
            try {
                myObj_2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to create the file Arabic.txt");
            }

            // create a file for others words ( uk's )
            currentFileName = "others";
            path = QueryDivide.HelperClass.invertedFilePath_V3(currentFileName);
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

                String path = QueryDivide.HelperClass.contentLengthFiles(String.valueOf(k));
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
            String path = QueryDivide.HelperClass.contentLengthFiles(url);
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
            File targetFolder = new File(QueryDivide.HelperClass.invertedFilePathDirectoryPath());
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



}