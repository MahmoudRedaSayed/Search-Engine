
package ServletsPackages.ServletPackage;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import DataBasePackages.DataBase.DataBase;
import HelpersPackages.Helpers.HelperClass;
import org.json.JSONException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
    int count=0;


    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.addHeader("Access-Control-Allow-Origin", "*");
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
            res.getWriter().println(results);

        }
//        //Ranker
//        res.getWriter().println(results.toString());
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//    static class PhraseSearching {
//        DataBase dataBaseObject = new DataBase();
//        private Map<String, File> invertedFiles;
//        PorterStemmer stemObject = new PorterStemmer();
//        String[] stopWords;
//
//
//        public PhraseSearching() {
////            working = files;
////            stopWords = files.getStopWordsAsArr();
//            System.out.println("The phrase");
//            stopWords[0]="name";
//        }
//
//
//        private String[] SplitQuery(String searchQuery) {
//            String[] subStrings = searchQuery.trim().split("\\s+");
//            return subStrings;
//        }
//
//        private static String[] removeElement(String[] arr, int[] index) {
//            List<String> list = new ArrayList<>(Arrays.asList(arr));
//            for (int i = 0; i < index.length; i++) {
//                list.remove(new String(arr[index[i]]));
//            }
//            return list.toArray(String[]::new);
//        }
//
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
//
//        public JSONArray run(String message, ArrayList<String> queryLinesResult, JSONArray dividedQuery) throws FileNotFoundException, JSONException {
////            invertedFiles = working.getInvertedFiles();
//            boolean[] indexProcessed;
//            Map<Integer, Integer> allIDs = new HashMap<Integer, Integer>();
//            JSONObject divide = new JSONObject();
//            divide.put("Results", message);
//            dividedQuery.put(divide);
//
//
//            ArrayList<String> allWordsResult = new ArrayList<String>();
//
//
//            String[] result = SplitQuery(message);
//            result = removeStopWords(result);
//            indexProcessed = new boolean[result.length];
//            String json = "{ [";
//            StringBuffer jsonFile = new StringBuffer(json);
//            JSONArray finalJsonFile = new JSONArray();
//            int length = result.length;
//            for (int i = 0; i < length; i++) {
//                // Loop over words
//                ArrayList<String> oneWordResult = new ArrayList<String>();
//
//            String path="D:\\Study\\Second Year\\Second Sem\\APT\\New folder (2)\\New folder (2)\\Sreach-Engine\\InvertedFiles_V3\\";
//            path+=result[i].substring(0,2)+".txt";
//            File pathFile=new File(path);
//                QueryProcessing.searchInInvertedFiles(result[i], pathFile,
//                        oneWordResult, false);
//
//                int length_2 = oneWordResult.size();
//                for (int j = 0; j < length_2; j++) {
//                    queryLinesResult.add(oneWordResult.get(j));
//                    // Loop over versions of Words
//
//
//                    String[] splitLine = oneWordResult.get(j).split("\\[");
//                    int length_3 = splitLine.length;
//                    for (int k = 1; k < length_3; k += 2) {
//
//                        // Loop over links of the same version of each Word
//
//                        int End = splitLine[k].indexOf(']');
//                        String temp = splitLine[k].substring(0, End);
//
//                        String[] finalID = temp.split(",");
//                        int ID = Integer.parseInt(finalID[0]);
//                        if (i == 0 && !indexProcessed[i]) {
//                            allIDs.put(ID, 1);
//                            indexProcessed[0] = true;
//                        } else if (!indexProcessed[i] && allIDs.containsKey(ID)) {
//                            allIDs.put(ID, 1 + allIDs.get(ID));
//                            indexProcessed[i] = true;
//                        }
//                    }
//                }
//
//            }
//
//            for (Iterator<Map.Entry<Integer, Integer>> it = allIDs.entrySet().iterator(); it.hasNext(); ) {
//                Map.Entry<Integer, Integer> entry = it.next();
//                if (entry.getValue() < length) {
//                    it.remove();
//                }
//
//                for (Iterator<Map.Entry<Integer, Integer>> iter = allIDs.entrySet().iterator(); it.hasNext(); ) {
//
//                    Map.Entry<Integer, Integer> IDEntry = iter.next();
//
//                    StringBuffer link = new StringBuffer("");
//                    StringBuffer description = new StringBuffer("");
//                    JSONObject Jo = new JSONObject();
//                    dataBaseObject.getLinkByID(IDEntry.getKey(), link, description);
//                    Jo.put("Link", link);
//                    Jo.put("Description", description);
//                    finalJsonFile.put(Jo);
//                }
//
//
//            }
//            return finalJsonFile;
//        }
//    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

static class QueryProcessing{
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
                for (int k=1; k<length_3; k++)
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

//        public class PhraseSearching {
//            static DataBase dataBaseObject = new DataBase();
//            //WorkingFiles working;
//            private Map<String, File> invertedFiles;
//            PorterStemmer stemObject = new PorterStemmer();
//            String[] stopWords;
//
//
//            public PhraseSearching() {
//                //working = files;
//                stopWords[0] = "test";      // "will be edited"
//                stopWords[1] = "test";      // "will be edited"
//            }
//
//
//            private String[] SplitQuery(String searchQuery) {
//                String[] subStrings = searchQuery.trim().split("\\s+");
//                return subStrings;
//            }
//
//            private static String[] removeElement(String[] arr, int[] index) {
//                List<String> list = new ArrayList<>(Arrays.asList(arr));
//                for (int i = 0; i < index.length; i++) {
//                    list.remove(new String(arr[index[i]]));
//                }
//                return list.toArray(String[]::new);
//            }
//
//
//            private String[] removeStopWords(String[] searchQuery) {
//                int length = searchQuery.length;
//                ArrayList<Integer> indeces = new ArrayList<Integer>();
//                for (int i = 0; i < length; i++) {
//                    System.out.println(searchQuery[i].toLowerCase());
//                    if (Arrays.asList(this.stopWords).contains(searchQuery[i].toLowerCase())) {
//                        indeces.add(i);
//                    }
//                }
//                searchQuery = removeElement(searchQuery, indeces.stream().mapToInt(Integer::intValue).toArray());
//                return searchQuery;
//            }
//
//
//            public JSONArray run(String message, ArrayList<String> queryLinesResult, JSONArray dividedQuery) throws FileNotFoundException, JSONException {
//                //invertedFiles = working.getInvertedFiles();
//                boolean[] indexProcessed;
//                Map<Integer, Integer> allIDs = new HashMap<Integer, Integer>();
//                JSONObject divide = new JSONObject();
//                divide.put("Results", message);
//                dividedQuery.put(divide);
//
//
//                ArrayList<String> allWordsResult = new ArrayList<String>();
//
//
//                String[] result = SplitQuery(message);
//                result = removeStopWords(result);
//                indexProcessed = new boolean[result.length];
//                String json = "{ [";
//                StringBuffer jsonFile = new StringBuffer(json);
//                JSONArray finalJsonFile = new JSONArray();
//                int length = result.length;
//                for (int i = 0; i < length; i++) {
//                    // Loop over words
//                    ArrayList<String> oneWordResult = new ArrayList<String>();
//
//                    // Mustafa : I edited this code
//
//                    String fileName = result[i].substring(0, 2);
//                    String filePath = "D:\\Study\\Second Year\\Second Sem\\APT\\New folder (2)\\New folder (2)\\Sreach-Engine\\InvertedFiles_V3\\";
//                    filePath += fileName + ".txt";
//                    File targetFile = new File(filePath);
//                    searchInInvertedFiles(result[i], targetFile,oneWordResult, true);
//
////                    QueryProcessingPackages.Query.QueryProcessing.searchInInvertedFiles(result[i], targetFile,
////                            oneWordResult, false);
//
//                    int length_2 = oneWordResult.size();
//                    for (int j = 0; j < length_2; j++) {
//                        queryLinesResult.add(oneWordResult.get(j));
//                        // Loop over versions of Words
//
//
//                        String[] splitLine = oneWordResult.get(j).split("\\[");
//                        int length_3 = splitLine.length;
//                        for (int k = 1; k < length_3; k += 2) {
//
//                            // Loop over links of the same version of each Word
//
//                            int End = splitLine[k].indexOf(']');
//                            String temp = splitLine[k].substring(0, End);
//
//                            String[] finalID = temp.split(",");
//                            int ID = Integer.parseInt(finalID[0]);
//                            if (i == 0 && !indexProcessed[i]) {
//                                allIDs.put(ID, 1);
//                                indexProcessed[0] = true;
//                            }
//                            else if (!indexProcessed[i] && allIDs.containsKey(ID)) {
//                                allIDs.put(ID, 1 + allIDs.get(ID));
//                                indexProcessed[i] = true;
//                            }
//                        }
//                    }
//
//                }
//
//                for (Iterator<Map.Entry<Integer, Integer>> it = allIDs.entrySet().iterator(); it.hasNext(); ) {
//                    Map.Entry<Integer, Integer> entry = it.next();
//                    if (entry.getValue() < length) {
//                        it.remove();
//                    }
//
//                    for (Iterator<Map.Entry<Integer, Integer>> iter = allIDs.entrySet().iterator(); it.hasNext(); ) {
//
//                        Map.Entry<Integer, Integer> IDEntry = iter.next();
//
//                        StringBuffer link = new StringBuffer("");
//                        StringBuffer description = new StringBuffer("");
//                        JSONObject Jo = new JSONObject();
//                        dataBaseObject.getLinkByID(IDEntry.getKey(), link, description);
//                        Jo.put("Link", link);
//                        Jo.put("Description", description);
//                        finalJsonFile.put(Jo);
//                    }
//
//
//
//                }
//                return finalJsonFile;
//            }
//        }

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
        public synchronized Boolean getLinkByID (Integer ID, StringBuffer linkUrl, StringBuffer description)
        {
            try{
                //String query = "Select Link FROM links WHERE Id= " + ID +" ";
                String query = "Select * FROM links";
                ResultSet resultSet = this.stmt.executeQuery("Select Link, Descripation FROM links WHERE Id= " + ID +";");
                resultSet.next();
                String linkResult = resultSet.getString("Link");
                linkUrl.append(linkResult);
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
            try{
                if(Layer==1)
                {
                    ResultSet resultSet= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+";");
                    while(resultSet.next())
                    {
                        grandLink.append(resultSet.getString("Link"));
                    }
                    return this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+";");
                }
                else if(Layer==2)
                {
                    ResultSet resultSet= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=0;");
                    while(resultSet.next())
                    {
                        resultSet=this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
                        while(resultSet.next())
                        {
                            parentLink.append(resultSet.getString("Link"));
                            return this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
                        }

                    }
                }
                else if (Layer==3)
                {
                    ResultSet resultSet= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=0;");
                    while(resultSet.next())
                    {
                        resultSet =this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
                        while(resultSet.next())
                        {
                            parentLink.append(resultSet.getString("Link"));
                            Layer=resultSet.getInt("Layer");
                            resultSet =this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
                            while(resultSet.next())
                            {
                                grandLink.append(resultSet.getString("Link"));
                                return this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
                            }

                        }


                    }
                }
            }
            catch(SQLException e)
            {
                return null;

            }
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
        public ResultSet getAllUrls()
        {
            try{
                return this.stmt.executeQuery("SELECT Link, Id FROM links where Completed=1;" );
            }
            catch(SQLException e)
            {
                return null;
            }
        }

        // ---------------------------------------------------------------------------------------------------------------------//
        //-----------------------------------------------get the number of links out from the parent link-----------------------//
        public int getParentLinksNum(int childId)
        {

            try{
                ResultSet resultSet=this.stmt.executeQuery("SELECT LinkParent FROM links  where Id="+childId+" ;" );
                while(resultSet.next())
                {
                    int parentId=resultSet.getInt("LinkParent");
                    return this.stmt.executeQuery("SELECT count(Id) as Number FROM links  where LinkParent="+parentId+" ;" ).getInt("Number");
                }
            }
            catch(SQLException e)
            {
                return -1;
            }
            return -1;
        }
        // ---------------------------------------------------------------------------------------------------------------------//
        //-----------------------------------------------Add Link descripation--------------------------------------------------//
        public void addDesc(int id,String desc)
        {
            try {
                this.stmt.executeUpdate("UPDATE links SET Descripation='" + desc + "' WHERE Id=" + id + ";");
            }
            catch(SQLException e)
            {

            }
        }
        // ---------------------------------------------------------------------------------------------------------------------//




    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static class HelperClass {


        // get the path of the inverted Files
        public static String invertedFilePath(String fileName)
        {
            String filePath = System.getProperty("user.dir");   // get the directory of the project
            filePath += File.separator + "InvertedFiles" + File.separator + fileName + ".txt";
            return filePath;
        }

        // get the path of the inverted Files_V2
        public static String invertedFilePath_V2(String fileName)
        {
            String filePath = System.getProperty("user.dir");   // get the directory of the project
            filePath += File.separator + "InvertedFiles_V2" + File.separator + fileName + ".txt";
            return filePath;
        }

        // get the path of the inverted Files_V3
        public static String invertedFilePath_V3(String fileName)
        {
            String filePath = System.getProperty("user.dir");   // get the directory of the project
            filePath += File.separator + "InvertedFiles_V3" + File.separator + fileName + ".txt";
            return filePath;
        }

        // get the path of the page content files
        public static String pageContentFilesPath(String fileName)
        {
            String filePath = System.getProperty("user.dir");   // get the directory of the project
            filePath += File.separator + "PageContentFiles" + File.separator + fileName + ".txt";
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


    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}