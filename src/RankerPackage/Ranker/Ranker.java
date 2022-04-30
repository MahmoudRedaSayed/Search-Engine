package RankerPackage.Ranker;
import IndexerPackages.Indexer.*;
import DataBasePackages.DataBase.DataBase;
import QueryProcessingPackages.Query.*;
import HelpersPackages.Helpers.*;
import java.lang.reflect.Array;
import java.util.*;
import org.json.*;
import java.io.*;
import java.lang.Math.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.sql.*;
import java.sql.ResultSet;

public class Ranker {
    private DataBase connect = new DataBase();
    //private PageParsing pageParsing = new PageParsing();
    private QueryProcessing queryProcessingObject;
    JSONArray dividedQuery = new JSONArray();
    String message;
    WorkingFiles workingFilesObject = new WorkingFiles();
    Map<String,Long> wordsCount;

    {
        try {
            queryProcessingObject = new QueryProcessing();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //To Remove Duplicates from an array
    public static String[] removeDuplicates(String [] arr) {
        int end = arr.length;
        for (int i = 0; i < end; i++) {
            for (int j = i + 1; j < end; j++) {
                if (arr[i] == arr[j]) {
                    int shiftLeft = j;
                    for (int k = j + 1; k < end; k++, shiftLeft++) {
                        arr[shiftLeft] = arr[k];
                    }
                    end--;
                    j--;
                }
            }
        }

        String [] whitelist = new String[end];
        for (int i = 0; i < end; i++) {
            whitelist[i] = arr[i];
        }
        return whitelist;                           //Array after remove Duplicates
    }


    //To calculate the Popularity between pages
    public Map<String, Double> calculatePopularity(double totalNodes)             //Popularity
    {
        // get links from db
        String[] completedLinks = connect.getAllUrls();

        //pagesRank1 ==> store the final results of popularity ( each page and its popularity )
        Map<String, Double> pagesRank1 = new HashMap<String, Double>();

        //TempPageRank is used to store values of pagesRank1 in it temporarily
        Map<String, Double> TempPageRank = new HashMap<String, Double>();

        //calculate the initial value of popularity for all pages
        double InitialPageRank = 1.0 / totalNodes;

        // initialize the rank of each page //
        for (int k = 0; k < totalNodes; k++)
            pagesRank1.put(completedLinks[k], InitialPageRank);

        //ITERATION_STEP is used to iterate twice following PageRank Algorithm steps
        int ITERATION_STEP = 1;
        while (ITERATION_STEP <= 2) {

            // Store the PageRank for All Nodes in Temporary Map
            for (int k = 0; k < totalNodes; k++) {
                TempPageRank.put(completedLinks[k], pagesRank1.get(completedLinks[k]));
                pagesRank1.put(completedLinks[k], 0.0);
            }

            //tempSum is the difference between all pages popularity and 1 ==> the difference is divided by the No. of links that didn't have parents
            double tempSum = 0;
            int counter=0;
            /*
            //iterate over total links to calculate the popularity
            for (int currentPage = 0; currentPage < 4424 ; currentPage++)      //ToDo: chang it later
            {
                //I will send child link ang I must get Number of OutgoingLinks of the parent
                //get outgoing links of the parent
                int OutgoingLinks = connect.getParentLinksNum(completedLinks[currentPage]);         //Get it from From ==> (Reda) to recieve the number of outgoing links from parent link

                //in case of the child link don't have parents
                if ( OutgoingLinks == -1 )
                {
                    pagesRank1.put(completedLinks[currentPage], -1.0);
                    counter++;
                    continue;
                }

                //I will send child link and get parent link ==> it will be changed later
                //get the popularity of the parent link and calculate the popularity of the child by this equation
                double temp = TempPageRank.get(connect.getParentLink(completedLinks[currentPage])) * (1.0 / OutgoingLinks) ;

                //put each page link and its current popularity
                pagesRank1.put(completedLinks[currentPage], temp);

                //sum all popularity of pages to diff it from 1 ==> the difference is divided by the No. of links that didn't have parents
                tempSum += pagesRank1.get(completedLinks[currentPage]);
            }
            */

            //Special handling for the pages only as there is no outgoing links to it
            double temp = 1 - tempSum;

            //slice of each link of them
            double slice = temp / 4424;//ToDo: chang it later

            //if the link don't have parents
            for ( int i=0 ; i<4424 ; i++ )//ToDo: chang it later
            {
                //if ( pagesRank1.get(completedLinks[i]).equals(-1.0) )
                //{
                pagesRank1.put(completedLinks[i] , slice);
                //}
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

    public static double log2(double num) {
        return (Math.log(num) / Math.log(2));
    }

    //To calculate the Relevance ( tf-idf )
    public Map<String, Double> calculateRelevance(ArrayList<String> tempLines) throws FileNotFoundException, JSONException
    {
        wordsCount = connect.getWordsCountAsMap();

        // get completed links from db
        String[] completedLinks = connect.getAllUrls();

        //pagesRank2 ==> store final result ( each page with its rank )
        Map<String , Double> pagesRank2 = new HashMap<String, Double>();

        //tf value of the page
        double tf = 0.0;

        //idf value of the page
        double idf = 0.0;

        //tf-idf value of the page
        double tf_idf = 0.0;

        //numOfOccerrencesInCurrentDocument ==> used to store type of each word (p/h/t/s) multiplied by No. of occurrences in current document( in this link)
        double numOfOccerrencesInCurrentDocument = 0.0;

        //numOfOccerrencesInAllDocuments ==> used to store No. of occurrences in all documents( in all links) where the word appear
        double numOfOccerrencesInAllDocuments = 0.0;

        //used for TF_IDF as it is an array of map
        int count = 0;

        //just for testing
        //consultancy|[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;
        //contract|[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.sky.com,p]:1;[http://www.nowtv.com/promo/sky-sports?dcmp=ilc_SSNTV_skysports_hardcode_moredropdownlink,p]:1;
        //tempLines.add(0,"consultancy|[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;");
        //tempLines.add(1,"contract|[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.skysports.com/,h]:1;[https://www.sky.com,p]:1;[http://www.nowtv.com/promo/sky-sports?dcmp=ilc_SSNTV_skysports_hardcode_moredropdownlink,p]:1;");

        //This is an Array of Map to store tf-idf of each word in each document
        List<Map<String, Double>> TF_IDF = new ArrayList<Map<String, Double>>();

        //Links_numOfOccurrences ==> used to store each link and its tf value
        Map<String, Double> Links_numOfOccurrences = new HashMap<String, Double>();

        //uniqueLinks ==> used to store unique links after combining links that are the same
        HashMap<Integer, String[]> uniqueLinks = new HashMap<Integer, String[]>();

        //counter used to put unigue links without any gap
        int counterForWords = 0;

        //iterate over each word in the words that the Query has
        for (int i = 0; i < tempLines.size(); i++)
        {
            //to make priority between title,header,paragraph
            double coeff = 0.0;

            //to split the query of each word to be like this ==> look just below
            int startIndex = tempLines.get(i).indexOf('|');
            String sub = tempLines.get(i).substring(startIndex + 1);
            String[] stringSplits = sub.split(";");

            //for word consultancy
            // [https://www.skysports.com/,h]:1
            // [https://www.skysports.com/,h]:1
            // [https://www.skysports.com/,h]:1
            // [https://www.skysports.com/,h]:1
            // [https://www.skysports.com/,h]:1

            //for word contract
            // [https://www.skysports.com/,h]:1
            // [https://www.skysports.com/,h]:1
            // [https://www.skysports.com/,h]:1
            // [https://www.skysports.com/,h]:1
            // [https://www.skysports.com/,h]:1
            // [https://www.skysports.com/,h]:1
            // [https://www.sky.com,p]:1
            // [http://www.nowtv.com/promo/sky-sports?dcmp=ilc_SSNTV_skysports_hardcode_moredropdownlink,p]:1

            //id of the previous page
            int idOfPreviosPage = -1;

            //array to store all links of the current query
            String arr[] = new String[stringSplits.length];

            //iterate over the links of each word in the query
            for (int j = 1; j <= stringSplits.length; j++) {

                //to get id of current page
                int charTempType22 = stringSplits[j - 1].indexOf('[');
                String linkOfCurrentPage = stringSplits[j - 1].substring(charTempType22 + 1, stringSplits[j - 1].indexOf(','));
                arr[j - 1] = linkOfCurrentPage;

                /*
                //to get the length of the page
                int charTempType77 = linkOfCurrentPage.indexOf("//") + 2;
                String tempLinkOfCurrentPage = linkOfCurrentPage.substring(charTempType77);
                tempLinkOfCurrentPage = tempLinkOfCurrentPage.replaceAll("[/]", "");
                */

                //get the length of the page
                Long lengthOfPage = wordsCount.get(linkOfCurrentPage);


                //to get the type of the word ==> paragraph or title or strong or header
                int charTempType = stringSplits[j - 1].indexOf(',');
                char charTemp = stringSplits[j - 1].charAt(charTempType + 1);

                if (charTemp == 't')                                   //title
                    coeff = 1.0 / 2.0;
                else if (charTemp == 'h' || charTemp == 's')         //header or strong
                    coeff = 1.0 / 4.0;
                else                                                    //paragraph
                    coeff = 1.0 / 8.0;

                //to get number of occurrences of each word
                int countTempType = stringSplits[j - 1].indexOf("]:");
                String countTemp = stringSplits[j - 1].substring(countTempType + 2);

                //numOfOccerrencesInCurrentDocument ==> used to store type of each word (p/h/t/s) multiplied by No. of occurrences in current document( in this link)
                numOfOccerrencesInCurrentDocument += coeff * Double.parseDouble(countTemp);

                //numOfOccerrencesInAllDocuments ==> used to store No. of occurrences in all documents( in all links) where the word appear
                numOfOccerrencesInAllDocuments += Integer.parseInt(countTemp);                      //total occurrences of a word in all documents

                //get the links of the previous and next pages
                int charTempType55;
                String linkOfPreviousPage;                //to get id of current page
                int charTempType33;
                String linkOfNextPage = "-1";                //to get id of current page

                //in case of last page I want to know if the previous page is same or not
                if (j == stringSplits.length && j>1) {
                        charTempType55 = stringSplits[j - 2].indexOf('[');
                        linkOfPreviousPage = stringSplits[j - 2].substring(charTempType55 + 1, stringSplits[j - 2].indexOf(',')); //to get id of current page
                }
                //in case of any page except last one I want to know if the previous page is same or not
                else {
                    charTempType33 = stringSplits[j].indexOf('[');
                    linkOfNextPage = stringSplits[j].substring(charTempType33 + 1, stringSplits[j].indexOf(','));                //to get id of current page
                }


                //calculate tf of the page
                if (! linkOfNextPage.equals(linkOfCurrentPage) && lengthOfPage != null && lengthOfPage != 0) {
                    tf = Double.valueOf(numOfOccerrencesInCurrentDocument) / lengthOfPage;
                    Links_numOfOccurrences.put(linkOfCurrentPage, tf);          //put id
                }
                tf = 0;
            }

            //temp array used to store links after remove duplicates ones
            String tempArr[] = removeDuplicates(arr);

            //put this array in unique links
            uniqueLinks.put(counterForWords, tempArr);

            //increment the counterForWords that described above
            counterForWords++;

            //calculate the idf value of the page
            idf = 5100.0 / Double.valueOf(numOfOccerrencesInAllDocuments);                                      // 5100 ==> number of indexed web pages

            //tempMap is used to store each link and its tf-idf value
            Map<String , Double> tempMap = new HashMap<String, Double>();

            for (int h = 0; h < tempArr.length; h++) {
                tf_idf = idf * Links_numOfOccurrences.get(tempArr[h]);
                tempMap.put(tempArr[h], tf_idf);
            }
            TF_IDF.add(tempMap);
        }

        //Results after all above operations will be like this
        //===========> 41,0.25 , 42,0.1 , 43,0.8 , 45,0.0486      the values of TF-IDF of word experience
        //===========> 41,0.25 , 42,0.1 , 43,0.8 , 44,0.0486      the values of TF-IDF of word encyclopedia
        //===========> 41,0.25 , 43,0.1 , 45,0.8 , 48,0.0486      the values of TF-IDF of word phenomena

        //array of maps to store each word pages and their tf-idf values
        Map[] maps = TF_IDF.toArray(new HashMap[TF_IDF.size()]);

        //calling function of calculatePopularity
        Map<String, Double> getPagesRank1 = new HashMap<String , Double>();
        getPagesRank1 = this.calculatePopularity(connect.getCompleteCount());



        ArrayList<String> arr = new ArrayList<String>();
        int counter55 = 0;
        double temptf_idf = 0;                //Used for pageRank

        //iterate over each word in the query
        for (int q = 0; q < tempLines.size(); q++) {

            //store the first word pages and their tf-idf values
            Map<String , Double> tempMap1 = new HashMap<String , Double>();
            tempMap1 = maps[q];
            String tempArr1[] = new String[uniqueLinks.get(counter55).length];

            //get the links of the current word
            tempArr1 = uniqueLinks.get(counter55);

            //in case of last word in the query
            if (counter55 == tempLines.size() - 1) {
                for (int y = 0; y < tempArr1.length; y++)
                {
                    // in case of the current link in the last word in the query doesn't calculate before ==> calculate it
                    if (!pagesRank2.containsKey(tempArr1[y])) {
                        pagesRank2.put(tempArr1[y], (0.7 * temptf_idf) + (0.3 * getPagesRank1.get(tempArr1[y])));              //summation of Relevance (tf_idf) and Popularity
                        arr.add(tempArr1[y]);
                    }
                }
            }
            // otherwise ==> Not the last word in the query
            else
            {
                //iterate over the second word in the query to combine if there is similar links
                for (int k = 0; k < tempArr1.length; k++)
                {
                    //iterate over the next words of the current one in the query
                    for (int j = 1; j < tempLines.size(); j++)
                    {
                        //store the second word pages and their tf-idf values
                        Map<String , Double> tempMap2 = new HashMap<String , Double>();
                        tempMap2 = maps[j];
                        String tempArr2[] = new String[uniqueLinks.get(counter55 + 1).length];

                        //get the links of the current word
                        tempArr2 = uniqueLinks.get(counter55 + 1);


                        //===========> 41,0.25 , 42,0.1 , 43,0.8 , 45,0.0486      the values of TF-IDF of word experience
                        //===========> 41,0.25 , 42,0.1 , 43,0.8 , 44,0.0486      the values of TF-IDF of word encyclopedia
                        //===========> 41,0.25 , 43,0.1 , 45,0.8 , 48,0.0486      the values of TF-IDF of word phenomena


                        //experience    ==>    41,42,43   ,45
                        //encyclopedia  ==> 40,   42,43,44

                        //iterate over all pages of the next word pages in the query and combine tf-idf values
                        for (int u = 0; u < tempArr2.length; u++) {
                            if (tempArr1[k] == tempArr2[u]) {
                                temptf_idf += tempMap1.get(tempArr1[k]) + tempMap2.get(tempArr2[u]);
                            }
                        }
                    }
                    //summation of Relevance (tf_idf) and Popularity
                    pagesRank2.put(tempArr1[k], (0.7 * temptf_idf) + (0.3 * getPagesRank1.get(tempArr1[k])));
                    arr.add(tempArr1[k]);
                }
                counter55++;
            }
        }

        //remove duplicates links
        String[] array = arr.toArray(new String[0]);
        String tempArray[] = removeDuplicates(array);


        /*
        //just for print the final results
        for (int t = 0; t < tempArray.length; t++)
        {
            System.out.println(tempArray[t]);
            System.out.println(pagesRank2.get(tempArray[t]));
        }
        */

        //return the final map that includes each link and each Rank
        return pagesRank2;  // get final results
    }
}