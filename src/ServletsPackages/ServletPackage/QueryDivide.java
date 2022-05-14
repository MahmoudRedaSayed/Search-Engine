
package ServletsPackages.ServletPackage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class QueryDivide extends HttpServlet {

    public JSONArray dividedQuery=new JSONArray();
    int count=0;
    public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
    {
        res.addHeader("Access-Control-Allow-Origin","*");
        res.setContentType("text/html");
        String searchingQuery = req.getParameter("query");
        if(count==0)
        {
            QueryProcessing obj = new QueryProcessing();
            try {
                obj.run(searchingQuery, dividedQuery);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
            count++;
        }
        res.getWriter().println(dividedQuery.toString());
    }

    static class QueryProcessing{

        //--------------------- The Data Members-------------------------//
        public String[] stopWords;
        //--------------------- Constructor-----------------------------//
    /*
        * Explanation:
            Constructor to:
            Read Stop Words to use in further functions
    */
        public QueryProcessing() throws FileNotFoundException {

            readStopWords();
            System.out.println("The consturctor");

        }

        public void readStopWords() throws FileNotFoundException {
            // open the file that contains stop words
            String filePath = "D:\\Study\\Second Year\\Second Sem\\APT\\New folder (2)\\New folder (2)\\Sreach-Engine";   // get the directory of the project
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


        //--------------------------Function run--------------------------//
        /*
         * Explanation:
         * Prepares for Highlighting websites content by dividing the query into its constituents
         */

        public void run(String message, JSONArray dividedQuery)
                throws FileNotFoundException, JSONException {

            System.out.println("The running function");

            //Used to add each word together with the whole query; to populate dividedQuery array
            ArrayList<String> words = new ArrayList<String>();
            words.add(message.trim());                    //First part of dividedQuery array
            JSONObject divide = new JSONObject();  //Used for divided Query servlet to highlight content in results


            String[] result = SplitQuery(message); //Splitting for words
            result  = removeStopWords(result);     // Remove Stop Words from the query

            // Loop over words
            int length = result.length;
            for(int i=0; i<length;i++)
            {
                //Add each word to words Array
                words.add(result[i]);

            }
            // Populate DividedQuery Array
            divide.put("Result", words);
            dividedQuery.put(divide);
        }
    }
}