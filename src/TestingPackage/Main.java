package TestingPackage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import CrawlerPackages.Crawler.*;
import CrawlerPackages.Crawler.UrlThread.*;
import DataBasePackages.DataBase.*;
import HelpersPackages.Helpers.HelperClass;
import HelpersPackages.Helpers.WorkingFiles;
import IndexerPackages.Indexer.Indexer;
import PhraseSearchingPackages.PhraseSearching.*;
//import ServletsPackages.ServletPackage.QuerySearch;
import IndexerPackages.Indexer.Indexer;
import RankerPackage.Ranker.Ranker;
import com.mysql.cj.xdevapi.DatabaseObject;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonString;
import QueryProcessingPackages.Query.QueryProcessing;
//import RankerPackage.Ranker.*;
import org.json.*;

import javax.xml.crypto.Data;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, JSONException {

       /* UrlThread obj=new UrlThread();
        Thread.currentThread().setName("Thread1");
        Thread newthread=new Thread(new UrlThread());
        newthread.setName("Thread2");
        newthread.run();*/

        //----------------------------------------Crawler-----------------------------------------//
        // DataBase DataBaseObject = new DataBase();
        // UrlThread.Limit+=DataBaseObject.getCompleteCount();
        // Thread ThreadsArray[]=new Thread[44];
        // Thread.currentThread().setName("Thread1");
        // UrlThread obj=new UrlThread();
        // for(int i=1;i<44;i++)
        // {
        //     ThreadsArray[i] = new Thread(new UrlThread());
        //     ThreadsArray[i].setName("Thread" + (i+1));
        //     ThreadsArray[i].start();

        // }
        //     for(int i=1;i<44;i++)
        // {

        //     try {
        //         ThreadsArray[i].join();
        //     } catch (InterruptedException e) {

        //     }
        // }
        //------------------------------------------------------------------------------------------//


        JSONArray dividedQuery =  new JSONArray();
        Ranker rankerObj = new Ranker();
        String finalJSONARRAY;
        QueryProcessing obj = new QueryProcessing();
        PhraseSearching phraseSearchingObj = new PhraseSearching();
        String searchingQuery;
        ArrayList<String> rankerArray=new ArrayList<String>();
        searchingQuery = "I want resources of egypt and infection";
        System.out.println(searchingQuery);
        finalJSONARRAY = obj.run(searchingQuery, rankerArray, dividedQuery);
//        HashMap<String, Double> toBeSorted = new HashMap<String, Double>();
//        toBeSorted.put("link1", 0.25);
//        toBeSorted.put("Link2", 0.5);
//        toBeSorted.put("Link3", 0.45);

        Map<String,Double> rankingResult= rankerObj.calculateRelevance(rankerArray);
        HashMap<String,Double> toBeSorted = new HashMap<String,Double>(rankingResult);
         HashMap<String,Double> sortedRankerMap = QueryProcessing.sortByValue(toBeSorted);
//        HashMap<String,Double> linksRankedMap = QueryProcessing.replaceIDByLink(toBeSorted);

        for (Map.Entry<String, Double> entry : sortedRankerMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }

       /* System.out.println(finalJSONARRAY);
        System.out.println(dividedQuery.toString());
        System.out.println(rankerArray.toString());*/

//        DataBase databaseObj  = new DataBase();
//        System.out.println(databaseObj.getCompleteCount());
////           Thread ThreadsArray=new Thread(new UrlThread());
////             Thread.currentThread().setName("Thread1");
//////             Thread.currentThread().start();
////           ThreadsArray.setName("Thread2");
////           ThreadsArray.run();
//
//
//
////         Thread ThreadsArray2=new Thread(new UrlThread());
////        ThreadsArray2.setName("Thread2");
////        ThreadsArray2.start();
////        ArrayList<String> Disallowed =ThreadsArray.robotSafe("https://www.bbc.co.uk/");
////        System.out.println("Disallowed");
////        DataBase DataBaseObject=new DataBase();
////        UrlThread.Limit+=DataBaseObject.getCompleteCount();
////        System.out.printf(" the limit %d",UrlThread.Limit);
//
        
//
//
//
//        ///////////////////////////////////////////////////////
//
        /*---------------     Start Indexing ----------------------*/


/*


        // create files
       // WorkingFiles.createInvertedFiles();

        // connect to db
        DataBase connect = new DataBase();

        // get stop words
        Map<Character, Vector<String>> stopWords = WorkingFiles.getStopWordsAsMap();

        // get links from db
       // ResultSet links = connect.getAllUrls();

        int ID = 0;
        String myLink = "";
        int linksCount = connect.getCompleteCount();
       // String[] completedLinks = new String[linksCount];
        int i = 0;

        // extracting the links from the result set

        */
/*try{
            while (links.next()) {
                try {
                    myLink= links.getString("Link");
                } catch (SQLException e) {
                    continue;
                }
                completedLinks[i++] = myLink;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*//*




       */
/* // Threading
        int threadCount = 5,
                counter = 0,
                threadsCounter = 0;
        boolean done = false;

        while (! done)
        {
            // creating Threads
            Thread[] threadsArr = new Thread[threadCount];
            while (counter < i && threadsCounter < threadCount)
            {
                threadsArr[threadsCounter] = new Thread(new Indexer(completedLinks[0], stopWords, connect));
                threadsArr[threadsCounter].start();;
                counter++;
                threadsCounter++;
            }
            for (int j = 0; j < threadsCounter; j++)
            {
                try {
                    threadsArr[j].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            done = counter == i;
            threadsCounter = 0;
        }


        System.out.println("DONE !\n");*//*




*/


        /*---------------     End Of Indexing ----------------------*/
 /*WorkingFiles.createInvertedFiles();

        // connect to db
        DataBase connect = new DataBase();

        // get stop words
        Map<Character, Vector<String>> stopWords = WorkingFiles.getStopWordsAsMap();

        String[] completedLinks = {
                "https://www.javatpoint.com",
                "https://www.marca.com",
                "https://developer.mozilla.org",
                "https://cplusplus.com"
        };

        for (String link : completedLinks)
        {
            Indexer test = new Indexer(link, stopWords, connect);
            test.run();
        }

        System.out.println("domne");




*/

    }
}
