//
//package ServletsPackages.ServletPackage;
//
//import java.io.IOException;
//import javax.servlet.*;
//import javax.servlet.http.*;
//
//import DataBasePackages.DataBase.DataBase;
//import HelpersPackages.Helpers.HelperClass;
//import IndexerPackages.Indexer.PageParsing;
//import QueryProcessingPackages.Query.QueryProcessing;
//import org.json.JSONException;
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
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
//    public ArrayList<String> rankerArray=new ArrayList<String>();
//    public JSONArray dividedQuery=new JSONArray();
//    int count=0;
//
//
//    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
//        res.addHeader("Access-Control-Allow-Origin", "*");
//        String searchingQuery = req.getParameter("query");
//        res.setContentType("text/html");
//        String results="";
//        DataBase dataBaseObj = new DataBase();
//        count=dataBaseObj.getCompleteCount();
//
//        //WorkingFiles workingFilesObj = new WorkingFiles(5615);
//        if (searchingQuery.startsWith("\"") && searchingQuery.endsWith("\"")) {
//
//            //call the function of the phrase searching
//            res.getWriter().println("phrase"+count);
//
//
////            PhraseSearching obj = new PhraseSearching();
////
////            try {
//////                 results  =obj.run(searchingQuery,rankerArray,dividedQuery);
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
//        } else {
//            //call function of query processing
////            res.getWriter().println("query"+count);
//
//            QueryProcessing obj = new QueryProcessing();
//            //Ranker rankerObject = new Ranker();
//            try {
//                 results  =obj.run(searchingQuery,rankerArray,dividedQuery);
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println(e);
//            }
//            //Trying Ranker but There's an error from file path not found, needs to be fixed
//            //The error is for Mustafa to check
//
////            try {
////                Map<Integer,Double> rankingResult= rankerObject.calculateRelevance(rankerArray);
////
////                //Passed Map to HashMap constructor, Probably an error
////                HashMap<Integer,Double> toBeSorted = new HashMap<Integer,Double>(rankingResult);
////                HashMap<Integer,Double> sortedRankerMap = QueryProcessing.sortByValue(toBeSorted);
////                HashMap<String,Double> linksRankedMap = QueryProcessing.replaceIDByLink(toBeSorted);
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
//
//            res.getWriter().println(results);
//
//        }
////        //Ranker
////        res.getWriter().println(results.toString());
//    }
//
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    static class PhraseSearching {
//        DataBasePackages.DataBase.DataBase dataBaseObject = new DataBasePackages.DataBase.DataBase();
//
//        private Map<String, File> invertedFiles;
//        PorterStemmer stemObject = new PorterStemmer();
//        String[] stopWords;
//
//
//        public PhraseSearching() throws FileNotFoundException {
//            readStopWords();
//            System.out.println("Phrase Searching consturctor");
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
//        private void readStopWords() throws FileNotFoundException {
//            // open the file that contains stop words
//            String filePath = System.getProperty("user.dir");   // get the directory of the project
//            System.out.println(filePath);
//            String finalfilePath = filePath.substring(0, filePath.lastIndexOf("\\")+1);
//            System.out.println(finalfilePath);
//            finalfilePath += File.separator + "helpers" + File.separator + "stop_words.txt";
//            File myFile = new File(finalfilePath);
//
//            this.stopWords = new String[851];
//
//            // read from the file
//            Scanner read = new Scanner(myFile);
//            String tempInput;
//            int counter = 0;
//            while(read.hasNextLine())
//            {
//                tempInput = read.nextLine();
//                stopWords[counter++] = tempInput;
//            }
//            read.close();
//
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
//        public String run(String message, ArrayList<String> queryLinesResult, JSONArray dividedQuery) throws FileNotFoundException, JSONException {
//
//            System.out.println("Phrase Searching Run Function");
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
//
//                String fileName = "";
//                if (HelpersPackages.Helpers.HelperClass.isProbablyArabic(result[i]))
//                    fileName = "arabic";
//                else if(result[i].length() == 2)
//                    fileName = "two";
//
//                else
//                    fileName = "_" + result[i].substring(0,3);
//
//
//                // Mustafa : I edited this code
//                String filePath = System.getProperty("user.dir");   // get the directory of the project
//
//                // Delete last Directory to get path of Inverted Files
//                String finalFilePath = filePath.substring(0, filePath.lastIndexOf("\\"));
//
//                finalFilePath += File.separator + "InvertedFiles_V3" + File.separator;
//
//                finalFilePath += fileName + ".txt";
//                //System.out.println(finalFilePath + "From Search Inverted Files");
//                File targetFile = new File(finalFilePath);
//
//                QueryProcessingPackages.Query.QueryProcessing.searchInInvertedFiles(result[i], targetFile,oneWordResult, false);
//
//                int length_2 = oneWordResult.size();
//                for (int j = 0; j < length_2; j++) {
//
//                    if(oneWordResult.get(j).equals(""))
//                    {continue;}
//                    // Should we let this be like that? Or should it be just links from map? I don't know
//                    queryLinesResult.add(oneWordResult.get(j));
//                    // Loop over versions of Words
//
//
//                    String[] splitLine = oneWordResult.get(j).split("\\[");
//                    int length_3 = splitLine.length;
//                    for (int k = 1; k < length_3; k++) {
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
//                            if(k == length_3-1)
//                            {
//                                indexProcessed[0] = true;
//                            }
//                        }
//                        else if (!indexProcessed[i] && allIDs.containsKey(ID)) {
//                            allIDs.put(ID, 1 + allIDs.get(ID));
//                            if(k == length_3-1)
//                            {
//                                indexProcessed[i] = true;
//                            }
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
//            }
//
//            for (Iterator<Map.Entry<Integer, Integer>> iter = allIDs.entrySet().iterator(); iter.hasNext(); ) {
//
//
//                Map.Entry<Integer, Integer> IDEntry = iter.next();
//
//                StringBuffer link = new StringBuffer("");
//                StringBuffer description = new StringBuffer("");
//                JSONObject Jo = new JSONObject();
//                //dataBaseObject.getLinkByID(IDEntry.getKey(), link, description);
//                Jo.put("Link", link);
//                Jo.put("Description", description);
//                finalJsonFile.put(Jo);
//            }
//
//            return finalJsonFile.toString();
//        }
//    }
//
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    static class QueryProcessing{
//        DataBasePackages.DataBase.DataBase dataBaseObject = new DataBasePackages.DataBase.DataBase();
//
//        private Map<String, File> invertedFiles;
//        public  PorterStemmer stemObject = new PorterStemmer();
//        public String[] stopWords;
//
//
//        public QueryProcessing() throws FileNotFoundException {
//
//            readStopWords();
//            System.out.println("The consturctor");
//        }
//
//        private String[] SplitQuery(String searchQuery)
//        {
//            String[] subStrings = searchQuery.trim().split("\\s+");
//            return subStrings;
//        }
//
//
//
//        private void readStopWords() throws FileNotFoundException {
//            // open the file that contains stop words
//            String filePath = System.getProperty("user.dir");   // get the directory of the project
//            System.out.println(filePath);
//            String finalfilePath = filePath.substring(0, filePath.lastIndexOf("\\")+1);
//            System.out.println(finalfilePath);
//            finalfilePath += File.separator + "helpers" + File.separator + "stop_words.txt";
//            File myFile = new File(finalfilePath);
//
//            this.stopWords = new String[851];
//
//            // read from the file
//            Scanner read = new Scanner(myFile);
//            String tempInput;
//            int counter = 0;
//            while(read.hasNextLine())
//            {
//                tempInput = read.nextLine();
//                stopWords[counter++] = tempInput;
//            }
//            read.close();
//
//        }
//
//        private String stemGivenWord(String word)
//        {
//            stemObject.setCurrent(word);
//            stemObject.stem();
//            return stemObject.getCurrent();
//        }
//
//        //Utility Function for removeStopWords()
//        private static String[] removeElement(String[] arr, int[] index) {
//            List<String> list = new ArrayList<>(Arrays.asList(arr));
//            for (int i=0; i<index.length;i++)
//            {
//                list.remove(new String(arr[index[i]]));
//            }
//            return list.toArray(String[]::new);
//        }
//
//
//        private String[] removeStopWords(String[] searchQuery)
//        {
//            int length =searchQuery.length;
//            ArrayList<Integer> indeces = new ArrayList<Integer>();
//            for(int i = 0; i< length; i++)
//            {
//                System.out.println(searchQuery[i].toLowerCase());
//                if (Arrays.asList(this.stopWords).contains(searchQuery[i].toLowerCase()))
//                {
//                    indeces.add(i);
//                }
//            }
//            searchQuery = removeElement(searchQuery, indeces.stream().mapToInt(Integer::intValue).toArray());
//            return searchQuery;
//        }
//
//        //What remains: Search for word in file and create array for each word in the search query:
//        //First element is the actual word if present
//        //The rest are the words with same root in that file
//
//
//        public static void searchInInvertedFiles(String word, File myFile, ArrayList<String> results, boolean stemmingFlag) throws FileNotFoundException {
//            Scanner read = new Scanner(myFile);
//            String tempInput,
//                    stemmedVersion = " ";
//
//            // stemming the word
//            if (stemmingFlag)
//                stemmedVersion = HelpersPackages.Helpers.HelperClass.stemTheWord(word);
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
//                if (tempInput.charAt(0) == '/')
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
//                        if (stemmedVersion.equals(HelpersPackages.Helpers.HelperClass.stemTheWord(theWord)))
//                            results.add(counter++, tempInput);
//                    }
//                }
//            }
//        }
//
//        public static HashMap<Integer, Double> sortByValue(HashMap<Integer, Double> hm)
//        {
//            // Create a list from elements of HashMap
//            List<Map.Entry<Integer, Double> > list =
//                    new LinkedList<Map.Entry<Integer, Double> >(hm.entrySet());
//
//            // Sort the list
//            Collections.sort(list, new Comparator<Map.Entry<Integer, Double> >() {
//                public int compare(Map.Entry<Integer, Double> o1,
//                                   Map.Entry<Integer, Double> o2)
//                {
//                    return (o2.getValue()).compareTo(o1.getValue());
//                }
//            });
//
//            // put data from sorted list to hashmap
//            HashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>();
//            for (Map.Entry<Integer, Double> aa : list) {
//                temp.put(aa.getKey(), aa.getValue());
//            }
//            return temp;
//        }
//
//        public static HashMap<String, Double> replaceIDByLink(HashMap<Integer, Double> hm)
//        {
//            StringBuffer link = new StringBuffer("");
//            DataBasePackages.DataBase.DataBase dataBaseObject = new DataBasePackages.DataBase.DataBase();
//            StringBuffer description = new StringBuffer("");
//            HashMap<String, Double> temp = new HashMap<String, Double>();
//            for (Iterator<Map.Entry<Integer, Double>> it = hm.entrySet().iterator(); it.hasNext(); )
//            {
//                Map.Entry<Integer, Double> IDEntry = it.next();
//                //dataBaseObject.getLinkByID(IDEntry.getKey(), link, description);
//                temp.put(link.toString(), IDEntry.getValue());
//            }
//
//            return temp;
//        }
//
//        public String run(String message, ArrayList<String> queryLinesResult, JSONArray dividedQuery)
//                throws FileNotFoundException, JSONException {
//            //invertedFiles = working.getInvertedFiles();
//            System.out.println("The running function");
//
//            boolean [] indexProcessed;
//            Map<Integer, Integer> allIDs = new HashMap<Integer, Integer>();
//            ArrayList<String> words = new ArrayList<String>();
//            words.add(message);
//            JSONObject divide = new JSONObject();
//            ArrayList<String> allWordsResult = new ArrayList<String>();
//
//
//            String[] result = SplitQuery(message);
//            result  = removeStopWords(result);
//            indexProcessed = new boolean[result.length];
//            String json = "{ [";
//            StringBuffer jsonFile = new StringBuffer(json);
//            JSONArray finalJsonFile = new JSONArray();
//            int length = result.length;
//            for(int i=0; i<length;i++)
//            {
//
//                // Loop over words
//                words.add(result[i]);
//                ArrayList<String> oneWordResult = new ArrayList<String>();
//
//                String fileName = "";
//                if (HelpersPackages.Helpers.HelperClass.isProbablyArabic(result[i]))
//                    fileName = "arabic";
//                else if(result[i].length() == 2)
//                    fileName = "two";
//
//                else
//                    fileName = "_" + result[i].substring(0,3);
//
//                // Mustafa : I edited this code
//
//                String filePath = "F:\\Servlets with Database\\Sreach-Engine\\InvertedFiles_V3\\";
//                filePath += fileName + ".txt";
//                File targetFile = new File(filePath);
//                searchInInvertedFiles(result[i], targetFile,oneWordResult, true);
//
//                int length_2 = oneWordResult.size();
//                for(int j = 0; j<length_2; j++)
//                {
//                    if(oneWordResult.get(j).equals(""))
//                    {continue;}
//
//                    queryLinesResult.add(oneWordResult.get(j));
//                    // Loop over versions of Words
//
//
//                    String[] splitLine= oneWordResult.get(j).split("\\[");
//                    int length_3 = splitLine.length;
//                    for (int k=1; k<length_3; k++)
//                    {
//
//                        // Loop over links of the same version of each Word
//
//                        int End = splitLine[k].indexOf(']');
//                        String temp = splitLine[k].substring(0, End);
//
//                        String[] finalID = temp.split(",");
//                        int ID = Integer.parseInt(finalID[0]);
//
//                        StringBuffer link = new StringBuffer("");
//                        StringBuffer description = new StringBuffer("");
//                        JSONObject Jo = new JSONObject();
//                        //dataBaseObject.getLinkByID(ID, link, description);
//                        Jo.put("Link", link);
//                        Jo.put("Description", description);
//                        finalJsonFile.put(Jo);
//
//                    }
//                }
//
//            }
//
//
//
//
//
//            divide.put("Result", words);
//            dividedQuery.put(divide);
//            return finalJsonFile.toString();
//
//        }
//    }
//
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    static class DataBase {
//        private Connection connect;
//        private Statement stmt;
//        public DataBase()
//        {
//            try{
//                try{
//                    Class.forName("com.mysql.cj.jdbc.Driver");
//                }
//                catch(Exception e)
//                {
//
//                }
//                connect=DriverManager.getConnection("jdbc:mysql://localhost:3306/search-engine","root","");
//                this.stmt=connect.createStatement();
//                if (connect != null) {
//                    System.out.println("Connected to database");
//                } else {
//                    System.out.println("Cannot connect to database");
//                }
//
//            }
//            catch(SQLException e)
//            {
//
//            }
//        }
//
//        //--------------------------------------Create Link --------------------------------------------------------------------//
//        public synchronized void createLink(String Link,int Layer,String ThreadName,int ParentId)
//        {
//            try{
//                this.stmt.executeUpdate("INSERT INTO links (Link, Layer, ThreadName, LinkParent,Completed) VALUES ('"+Link+"', '"+Layer+"', '"+ThreadName+"', "+ParentId+",'"+0+"');");
//            }
//            catch(SQLException e)
//            {
//            }
//        }
////----------------------------------------------------------------------------------------------------------------------//
//
//// --------------------------------------Update Link to Complete -------------------------------------------------------//
//
//        public synchronized void urlCompleted(String Link)
//        {
//            try{
//                this.stmt.executeUpdate("UPDATE links SET Completed=1 WHERE link='"+Link+"'");
//            }
//            catch(SQLException e)
//            {
//            }
//        }
////----------------------------------------------------------------------------------------------------------------------//
//
//        // --------------------------------------Update Link to Complete -------------------------------------------------------//
//
//// --------------------------------------Set and Get Thread Position -------------------------------------------------------//
//
//        public synchronized void setThreadPosition(String ThreadName,int Layer,int Index)
//        {
//            try{
//                if(Layer==1)
//                {
//                    this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
//                    this.stmt.executeUpdate("UPDATE threads SET UrlIndex="+Index+" WHERE ThreadName='"+ThreadName+"';");
//
//                }
//                else if (Layer==2)
//                {
//
//                    this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
//                    this.stmt.executeUpdate("UPDATE threads SET UrlIndex1="+Index+" WHERE ThreadName='"+ThreadName+"';");
//
//                }
//                else if (Layer==3)
//                {
//
//
//                    this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
//                    this.stmt.executeUpdate("UPDATE threads SET UrlIndex2="+Index+" WHERE ThreadName='"+ThreadName+"';");
//                }
//                else if (Layer==4)
//                {
//
//                    this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
//                    this.stmt.executeUpdate("UPDATE threads SET UrlIndex3="+Index+" WHERE ThreadName='"+ThreadName+"';");
//                }
//                else{
//                    this.stmt.executeUpdate("UPDATE threads SET Layer=1 WHERE ThreadName='"+ThreadName+"';");
//                    this.stmt.executeUpdate("UPDATE threads SET  UrlIndex=0 WHERE ThreadName='"+ThreadName+"';");
//                    this.stmt.executeUpdate("UPDATE threads SET  UrlIndex1=0  WHERE ThreadName='"+ThreadName+"';");
//                    this.stmt.executeUpdate("UPDATE threads SET  UrlIndex2=0 WHERE ThreadName='"+ThreadName+"';");
//                    this.stmt.executeUpdate("UPDATE threads SET   UrlIndex3=0 WHERE ThreadName='"+ThreadName+"';");
//
//
//
//                }
//            }
//            catch(SQLException e)
//            {
//            }
//        }
//
//        public synchronized ResultSet getThreadPosition(String ThreadName)
//        {
//            try{
//                ResultSet resultSet=this.stmt.executeQuery("SELECT * FROM threads WHERE ThreadName='"+ThreadName+"'");
//                return resultSet;
//            }
//            catch(SQLException e)
//            {
//                return null;
//            }
//        }
////----------------------------------------------------------------------------------------------------------------------//
//
//        public synchronized ResultSet getUrls(String Url)
//        {
//            try{
//                return this.stmt.executeQuery("SELECT * FROM links WHERE Link='"+Url+"' AND Completed = 1");
//            }
//            catch(SQLException e)
//            {
//                return null;
//            }
//        }
//        //---------------------------------------------get the url similar to the url-------------------------------------------//
//        public synchronized ResultSet getUrls2(String Url)
//        {
//            try{
//                return this.stmt.executeQuery("SELECT * FROM links WHERE Link='"+Url+"';");
//            }
//            catch(SQLException e)
//            {
//                return null;
//            }
//        }
//// ---------------------------------------------------------------------------------------------------------------------//
//
//
//        //---------------------------------------get link by ID  -------------------------------------------------------------//
//        public synchronized Boolean getLinkByID (Integer ID, StringBuffer linkUrl, StringBuffer description)
//        {
//            try{
//                //String query = "Select Link FROM links WHERE Id= " + ID +" ";
//                String query = "Select * FROM links";
//                ResultSet resultSet = this.stmt.executeQuery("Select Link, Descripation FROM links WHERE Id= " + ID +";");
//                resultSet.next();
//                String linkResult = resultSet.getString("Link");
//                linkUrl.append(linkResult);
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
//
//// ---------------------------------------------------------------------------------------------------------------------//
//
//
//// --------------------------------------get the id of the link  -------------------------------------------------------//
//
//        public synchronized int getId (String Url,String ThreadName)
//        {
//            try{
//                ResultSet resultSet=this.stmt.executeQuery("SELECT * FROM links WHERE Link='"+Url+"' AND ThreadName='"+ThreadName+"' AND Completed=0 ;");
//                while (resultSet.next())
//                {
//                    int Id=-1;
//                    Id=resultSet.getInt("Id");
//                    return  Id;
//                }
//            }
//            catch(SQLException e)
//            {
//
//            }
//            return -1;
//        }
////----------------------------------------------------------------------------------------------------------------------//
//
//        //-----------------------------------------get the family of the link --------------------------------------------------//
//        public synchronized ResultSet getParentUrl (String ThreadName,StringBuffer parentLink , StringBuffer grandLink , String link,int Layer)
//        {
//            try{
//                if(Layer==1)
//                {
//                    ResultSet resultSet= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+";");
//                    while(resultSet.next())
//                    {
//                        grandLink.append(resultSet.getString("Link"));
//                    }
//                    return this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+";");
//                }
//                else if(Layer==2)
//                {
//                    ResultSet resultSet= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=0;");
//                    while(resultSet.next())
//                    {
//                        resultSet=this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
//                        while(resultSet.next())
//                        {
//                            parentLink.append(resultSet.getString("Link"));
//                            return this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
//                        }
//
//                    }
//                }
//                else if (Layer==3)
//                {
//                    ResultSet resultSet= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=0;");
//                    while(resultSet.next())
//                    {
//                        resultSet =this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
//                        while(resultSet.next())
//                        {
//                            parentLink.append(resultSet.getString("Link"));
//                            Layer=resultSet.getInt("Layer");
//                            resultSet =this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
//                            while(resultSet.next())
//                            {
//                                grandLink.append(resultSet.getString("Link"));
//                                return this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
//                            }
//
//                        }
//
//
//                    }
//                }
//            }
//            catch(SQLException e)
//            {
//                return null;
//
//            }
//            return null;
//        }
////----------------------------------------------------------------------------------------------------------------------//
//
//
//
//
//        //------------------------------------------get the completed urls------------------------------------------------------//
//        public synchronized int getCompleteCount ()
//        {
//            try
//            {
//                ResultSet result =this.stmt.executeQuery("SELECT count(Link) as Number FROM links WHERE  Completed=1 ;");
//                int count=0;
//                while(result.next())
//                {
//                    count=result.getInt("Number");
//                }
//                return count;
//            }
//            catch(SQLException e)
//            {
//            }
//            return 0;
//        }
////----------------------------------------------------------------------------------------------------------------------//
//
//        public java.sql.Date getMaxDate ()
//        {
//            try
//            {
//                ResultSet result =this.stmt.executeQuery("SELECT max(LastTime) as Time FROM links;");
//                java.sql.Date count=null;
//                while(result.next())
//                {
//                    count = result.getDate("columnName");
//                }
//                return count;
//            }
//            catch(SQLException e)
//            {
//            }
//            return null;
//        }
//
//        //---------------------------------------------get url and its related ID-------------------------------------------//
//        public ResultSet getAllUrls()
//        {
//            try{
//                return this.stmt.executeQuery("SELECT Link, Id FROM links where Completed=1;" );
//            }
//            catch(SQLException e)
//            {
//                return null;
//            }
//        }
//
//        // ---------------------------------------------------------------------------------------------------------------------//
//        //-----------------------------------------------get the number of links out from the parent link-----------------------//
//        public int getParentLinksNum(int childId)
//        {
//
//            try{
//                ResultSet resultSet=this.stmt.executeQuery("SELECT LinkParent FROM links  where Id="+childId+" ;" );
//                while(resultSet.next())
//                {
//                    int parentId=resultSet.getInt("LinkParent");
//                    return this.stmt.executeQuery("SELECT count(Id) as Number FROM links  where LinkParent="+parentId+" ;" ).getInt("Number");
//                }
//            }
//            catch(SQLException e)
//            {
//                return -1;
//            }
//            return -1;
//        }
//        // ---------------------------------------------------------------------------------------------------------------------//
//        //-----------------------------------------------Add Link descripation--------------------------------------------------//
//        public void addDesc(int id,String desc)
//        {
//            try {
//                this.stmt.executeUpdate("UPDATE links SET Descripation='" + desc + "' WHERE Id=" + id + ";");
//            }
//            catch(SQLException e)
//            {
//
//            }
//        }
//        // ---------------------------------------------------------------------------------------------------------------------//
//
//
//
//
//    }
//
//    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    static class HelperClass {
//
//
//        // get the path of the inverted Files
//        public static String invertedFilePath(String fileName)
//        {
//            String filePath = System.getProperty("user.dir");   // get the directory of the project
//            filePath += File.separator + "InvertedFiles" + File.separator + fileName + ".txt";
//            return filePath;
//        }
//
//        // get the path of the inverted Files_V2
//        public static String invertedFilePath_V2(String fileName)
//        {
//            String filePath = System.getProperty("user.dir");   // get the directory of the project
//            filePath += File.separator + "InvertedFiles_V2" + File.separator + fileName + ".txt";
//            return filePath;
//        }
//
//        // get the path of the inverted Files_V3
//        public static String invertedFilePath_V3(String fileName)
//        {
//            String filePath = System.getProperty("user.dir");   // get the directory of the project
//            filePath += File.separator + "InvertedFiles_V3" + File.separator + fileName + ".txt";
//            return filePath;
//        }
//
//        // get the path of the page content files
//        public static String pageContentFilesPath(String fileName)
//        {
//            String filePath = System.getProperty("user.dir");   // get the directory of the project
//            filePath += File.separator + "PageContentFiles" + File.separator + fileName + ".txt";
//            return filePath;
//        }
//
//
//        // check if a given word is existing in a given inverted file or not
//        // returns the whole line that contains this word
//        public static String isExistingInFile(String word, File myFile) throws IOException {
//            Scanner read = new Scanner(myFile);
//            String tempInput;
//
//            while(read.hasNextLine())
//            {
//                tempInput = read.nextLine();
//                if (tempInput.equals(""))
//                    continue;
//
//                // check if this line is for a word or just an extension for the previous line
//                if (tempInput.charAt(0) == '/')
//                // compare to check if this word = ourWord ?
//                {
//                    // get the word
//                    int wordSize = word.length();
//                    char ch = tempInput.charAt(1);      // just initialization
//                    boolean matchingFlag = true;
//
//                    int i;
//                    for (i = 0; i < wordSize; i++)
//                        if(tempInput.charAt(i+1) != word.charAt(i))
//                            break;
//
//                    if(i == wordSize)
//                        return tempInput;
//                }
//            }
//            return "";      // if not found, return empty
//        }
//
//        // this function replaces a line in a given inverted file
//        public static void replaceLineInFile(Path path, String oldLine, String newLine) throws IOException {
//            List<String>fileContents = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
//            int ContentSize = fileContents.size();
//
//            for (int i = 0; i < ContentSize; i++)
//            {
//                if(fileContents.get(i).equals(oldLine)) {
//                    fileContents.set(i, newLine);
//                    break;
//                }
//            }
//            Files.write(path, fileContents, StandardCharsets.UTF_8);
//        }
//
//        // stem the word using Porter Stemmer Lib
//        public static String stemTheWord(String word)
//        {
//            PorterStemmer stemObject = new PorterStemmer();
//            stemObject.setCurrent(word);
//            stemObject.stem();
//            return stemObject.getCurrent();
//        }
//
//        // check if the word is arabic
//        public static boolean isProbablyArabic(String s) {
//            for (int i = 0; i < s.length();) {
//                int c = s.codePointAt(i);
//                if (c >= 0x0600 && c <= 0x06E0)
//                    return true;
//                i += Character.charCount(c);
//            }
//            return false;
//        }
//
//
//    }
//
//    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    static class Ranker {
//        private DataBasePackages.DataBase.DataBase dataBaseObject = new DataBasePackages.DataBase.DataBase();
////        private PageParsing pageParsing = new PageParsing();
//        private QueryProcessingPackages.Query.QueryProcessing queryProcessingObject;
//
//        {
//            try {
//                queryProcessingObject = new QueryProcessingPackages.Query.QueryProcessing();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//
//        JSONArray dividedQuery = new JSONArray();
//        String message;
//
//        //To Remove Duplicates from an array
//        public static int[] removeDuplicates(int[] arr) {
//            int end = arr.length;
//            for (int i = 0; i < end; i++) {
//                for (int j = i + 1; j < end; j++) {
//                    if (arr[i] == arr[j]) {
//                        int shiftLeft = j;
//                        for (int k = j + 1; k < end; k++, shiftLeft++) {
//                            arr[shiftLeft] = arr[k];
//                        }
//                        end--;
//                        j--;
//                    }
//                }
//            }
//
//            int[] whitelist = new int[end];
//            for (int i = 0; i < end; i++) {
//                whitelist[i] = arr[i];
//            }
//            return whitelist;                           //Array after remove Duplicates
//        }
//
//        //To Remove Duplicates from an array
//        public static int[] removeDuplicates(Integer[] arr) {
//            int end = arr.length;
//            for (int i = 0; i < end; i++) {
//                for (int j = i + 1; j < end; j++) {
//                    if (arr[i] == arr[j]) {
//                        int shiftLeft = j;
//                        for (int k = j + 1; k < end; k++, shiftLeft++) {
//                            arr[shiftLeft] = arr[k];
//                        }
//                        end--;
//                        j--;
//                    }
//                }
//            }
//
//            int[] whitelist = new int[end];
//            for (int i = 0; i < end; i++) {
//                whitelist[i] = arr[i];
//            }
//            return whitelist;                       //Array after remove Duplicates
//        }
//
//
//        //To calculate the Popularity between pages
//        public Map<Integer, Double> calculatePopularity(double totalNodes)             //Popularity
//        {
//            Map<Integer, Double> pagesRank1 = new HashMap<Integer, Double>();
//            Map<Integer, Double> TempPageRank = new HashMap<Integer, Double>();
//
//            double InitialPageRank = 1.0 / totalNodes;
//
//            // initialize the rank of each page //
//            for (int k = 1; k <= totalNodes; k++)
//                pagesRank1.put(k, InitialPageRank);
//
//
//            int ITERATION_STEP = 1;
//            while (ITERATION_STEP <= 2) {
//                // Store the PageRank for All Nodes in Temporary Array
//                for (int k = 1; k <= totalNodes; k++) {
//                    TempPageRank.put(k, pagesRank1.get(k));
//                    pagesRank1.put(k, 0.0);
//                }
//
//                double tempSum = 0;
//                for (int currentPage = 1; currentPage <= totalNodes; currentPage++) {
//                    double OutgoingLinks = dataBaseObject.getParentLinksNum(currentPage);         //Get it from From ==> (Reda) to recieve the number of outgoing links from parent link
//                    double temp = TempPageRank.get(dataBaseObject.getParentId(currentPage)) * (1.0 / OutgoingLinks);
//                    pagesRank1.put(currentPage, temp);
//                    tempSum += pagesRank1.get(currentPage);
//
//                }
//
//                //Special handling for the first page only as there is no outgoing links to it
//                double temp = 1 - tempSum;
//                pagesRank1.put(1, temp);
//                ITERATION_STEP++;
//            }
//
//            // Add the Damping Factor to PageRank
//            double DampingFactor = 0.75;
//            double temp = 0;
//            for (int k = 40; k <= 40 + totalNodes; k++) {
//                temp = (1 - DampingFactor) + DampingFactor * pagesRank1.get(k);
//                pagesRank1.put(k, temp);
//            }
//
//            return pagesRank1;
//        }
//
//        public static double log2(double num) {
//            return (Math.log(num) / Math.log(2));
//        }
//
//        //To calculate the Relevance ( tf-idf )
//        public Map<Integer, Double> calculateRelevance(ArrayList<String> tempLines) throws FileNotFoundException, JSONException {
//            Map<Integer, Double> pagesRank2 = new HashMap<Integer, Double>();
//            // Get it from (Mustafa) the length of the page
//            double tf = 0.0;
//            double idf = 0.0;
//            double tf_idf = 0.0;
//            double numOfOccerrencesInCurrentDocument = 0.0;
//            double numOfOccerrencesInAllDocuments = 0.0;
//            int count = 0;                                     //used for TF_IDF as it is an array of map
//
//            //getWordsResult from QueryProcessing (Waleed)  ==> Array contains all words of the search query after processing
//
//            //Not needed currently, sent by servlet
//            //ArrayList<String> tempLines = new ArrayList<String>();
//            // {"/experience|[41,t]:1;[41,h]:1;[41,p]:1;[42,h]:2;[42,h]:2;[43,h]:2;[45,h]:2;", "/encyclopedia|[40,t]:1;[42,t]:1;[42,t]:1;[43,h]:5;[44,s]:5;[44,p]:3;"};
//
//
//            //This is an Array of Map to store tf-idf of each word in each document
//            List<Map<Integer, Double>> TF_IDF = new ArrayList<Map<Integer, Double>>();
//            Map<Integer, Double> Ids_numOfOccurrences = new HashMap<Integer, Double>();
//
//            HashMap<Integer, int[]> uniqueIds = new HashMap<Integer, int[]>();
//
//            //experience|[41,t]:1;[41,h]:1;[41,p]:1;[42,h]:2;[42,h]:2;[43,h]:2;[45,h]:2;
//            //encyclopedia|[40,t]:1;[42,t]:1;[43,t]:1;[42,h]:5;[44,s]:5;[44,p]:3;
//
//            int counterForWords = 0;
//            for (int i = 0; i < tempLines.size(); i++) {
//                double coeff = 0.0;                                     //to make priority between title,header,paragraph
//                int startIndex = tempLines.get(i).indexOf('|');
//                String sub = tempLines.get(i).substring(startIndex + 1);
//                String[] stringSplits = sub.split(";");
//
//                //for word ==> experience
//                //After Splitting
//                // [41,t]:1
//                // [41,h]:1
//                // [41,p]:1
//                // [42,h]:2
//                // [42,h]:2
//                // [43,h]:2
//                // [45,h]:2
//
//                //for word ==> encyclopedia
//                // After splitting
//                // [40,t]:1
//                // [42,t]:1
//                // [43,t]:1
//                // [44,h]:5
//                // [44,s]:5
//                // [44,p]:3
//
//                int idOfPreviosPage = -1;
//                int arr[] = new int[stringSplits.length];
//
//                for (int j = 1; j <= stringSplits.length; j++) {
//                    int charTempType22 = stringSplits[j - 1].indexOf('[');
//                    String idOfCurrentPage = stringSplits[j - 1].substring(charTempType22 + 1, stringSplits[j - 1].indexOf(','));                //to get id of current page
//                    arr[j - 1] = Integer.parseInt(idOfCurrentPage);
////                    long lengthOfPage = pageParsing.getLengthOfPageContent(Integer.parseInt(idOfCurrentPage));   //try to get pageID
//                    int charTempType = stringSplits[j - 1].indexOf(',');
//                    char charTemp = stringSplits[j - 1].charAt(charTempType + 1);
//
//                    if (charTemp == 't')                                   //title
//                        coeff = 1.0 / 2.0;
//                    else if (charTemp == 'h' || charTemp == 's')         //header or strong
//                        coeff = 1.0 / 4.0;
//                    else                                                    //paragraph
//                        coeff = 1.0 / 8.0;
//
//                    //to get number of occurrences of each word
//                    int countTempType = stringSplits[j - 1].indexOf(':');
//                    String countTemp = stringSplits[j - 1].substring(countTempType + 1);
//                    numOfOccerrencesInCurrentDocument += coeff * Double.parseDouble(countTemp);
//                    numOfOccerrencesInAllDocuments += Integer.parseInt(countTemp);                      //total occurrences of a word in all documents
//
//                    int charTempType55;
//                    String idOfPreviousPage;                //to get id of current page
//                    int charTempType33;
//                    String idOfNextPage = "-1";                //to get id of current page
//
//                    if (j == stringSplits.length) {
//                        charTempType55 = stringSplits[j - 2].indexOf('[');
//                        idOfPreviousPage = stringSplits[j - 2].substring(charTempType55 + 1, stringSplits[j - 2].indexOf(','));                //to get id of current page
//                    } else {
//                        charTempType33 = stringSplits[j].indexOf('[');
//                        idOfNextPage = stringSplits[j].substring(charTempType33 + 1, stringSplits[j].indexOf(','));                //to get id of current page
//                    }
//
//                    int qw = Integer.parseInt(idOfNextPage);
//                    int qe = Integer.parseInt(idOfCurrentPage);
//                    if (qw != qe) {
////                        tf = Double.valueOf(numOfOccerrencesInCurrentDocument) / Double.valueOf(lengthOfPage);
//                        Ids_numOfOccurrences.put(Integer.parseInt(idOfCurrentPage), tf);          //put id and numOfOccerrencesInCurrentDocument into the map
//                        numOfOccerrencesInCurrentDocument = 0;
//                    }
//                    tf = 0;
//                }
//                int tempArr[] = removeDuplicates(arr);
//
//                uniqueIds.put(counterForWords, tempArr);
//                counterForWords++;
//
//                idf = 5100.0 / Double.valueOf(numOfOccerrencesInAllDocuments);                                      // 5100 ==> number of indexed web pages
//                Map<Integer, Double> tempMap = new HashMap<Integer, Double>();
//
//                for (int h = 0; h < tempArr.length; h++) {
//                    tf_idf = idf * Ids_numOfOccurrences.get(tempArr[h]);
//                    tempMap.put(tempArr[h], tf_idf);
//                }
//                TF_IDF.add(tempMap);
//            }
//
//            //===========> 41,0.25 , 42,0.1 , 43,0.8 , 45,0.0486      the values of TF-IDF of word experience
//            //===========> 41,0.25 , 42,0.1 , 43,0.8 , 44,0.0486      the values of TF-IDF of word encyclopedia
//            //===========> 41,0.25 , 43,0.1 , 45,0.8 , 48,0.0486      the values of TF-IDF of word phenomena
//
//            Map[] maps = TF_IDF.toArray(new HashMap[TF_IDF.size()]);
//
//
//            Map<Integer, Double> getpagesRank1 = new HashMap<Integer, Double>();
//            getpagesRank1 = this.calculatePopularity(dataBaseObject.getCompleteCount());
//
//            int lengthForArray = 0;
//            for (int i = 0; i < maps.length; i++)
//                lengthForArray += maps[i].size();
//
//            ArrayList<Integer> arr = new ArrayList<Integer>();
//            int counter55 = 0;
//            double temptf_idf = 0;                //Used for pageRank
//
//            for (int q = 0; q < tempLines.size(); q++) {
//
//                Map<Integer, Double> tempMap1 = new HashMap<Integer, Double>();
//                tempMap1 = maps[q];
//                int tempArr1[] = new int[uniqueIds.get(counter55).length];
//                tempArr1 = uniqueIds.get(counter55);
//
//                if (counter55 == tempLines.size() - 1) {
//                    for (int y = 0; y < tempArr1.length; y++) {
//                        if (!pagesRank2.containsKey(tempArr1[y])) {
//                            pagesRank2.put(tempArr1[y], (0.7 * temptf_idf) + (0.3 * getpagesRank1.get(tempArr1[y])));              //summation of Relevance (tf_idf) and Popularity
//                            arr.add(tempArr1[y]);
//                        }
//                    }
//                } else {
//                    for (int k = 0; k < tempArr1.length; k++) {
//                        for (int j = 1; j < tempLines.size(); j++) {
//                            Map<Integer, Double> tempMap2 = new HashMap<Integer, Double>();
//                            tempMap2 = maps[j];
//                            int tempArr2[] = new int[uniqueIds.get(counter55 + 1).length];
//                            tempArr2 = uniqueIds.get(counter55 + 1);
//                            //===========> 41,0.25 , 42,0.1 , 43,0.8 , 45,0.0486      the values of TF-IDF of word experience
//                            //===========> 41,0.25 , 42,0.1 , 43,0.8 , 44,0.0486      the values of TF-IDF of word encyclopedia
//                            //===========> 41,0.25 , 43,0.1 , 45,0.8 , 48,0.0486      the values of TF-IDF of word phenomena
//
//                            //experience    ==>    41,42,43   ,45
//                            //encyclopedia  ==> 40,   42,43,44
//
//                            for (int u = 0; u < tempArr2.length; u++) {
//                                if (tempArr1[k] == tempArr2[u]) {
//                                    temptf_idf += tempMap1.get(tempArr1[k]) + tempMap2.get(tempArr2[u]);
//                                } else if (tempArr1[k] < tempArr2[u]) {
//                                    break;
//                                }
//                            }
//                        }
//                        pagesRank2.put(tempArr1[k], (0.7 * temptf_idf) + (0.3 * getpagesRank1.get(tempArr1[k])));              //summation of Relevance (tf_idf) and Popularity
//                        arr.add(tempArr1[k]);
//                    }
//                    counter55++;
//                }
//            }
//
//            Integer[] array = arr.toArray(new Integer[0]);
//            int tempArray[] = removeDuplicates(array);
//
//            for (int t = 0; t < tempArray.length; t++) {
//                System.out.println(tempArray[t]);
//                System.out.println(pagesRank2.get(tempArray[t]));
//            }
//            return pagesRank2;
//        }
//    }
//
//}