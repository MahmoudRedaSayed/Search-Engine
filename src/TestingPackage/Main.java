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
//import PhraseSearchingPackages.PhraseSearching.*;
//import ServletsPackages.ServletPackage.QuerySearch;
import IndexerPackages.Indexer.Indexer;
import com.mysql.cj.xdevapi.DatabaseObject;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonString;
//import QueryProcessingPackages.Query.QueryProcessing;
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





        /*JSONArray dividedQuery =  new JSONArray();
        Ranker rankerObj = new Ranker();
        String finalJSONARRAY;
        QueryProcessing obj = new QueryProcessing();
        PhraseSearching phraseSearchingObj = new PhraseSearching();
        String searchingQuery;
        ArrayList<String> rankerArray=new ArrayList<String>();
        searchingQuery = "Additional is am additional";
        System.out.println(searchingQuery);
        finalJSONARRAY = obj.run(searchingQuery, rankerArray, dividedQuery);*/

//        Map<Integer,Double> rankingResult= rankerObj.calculateRelevance(rankerArray);
//        HashMap<Integer,Double> toBeSorted = new HashMap<Integer,Double>(rankingResult);
//        HashMap<Integer,Double> sortedRankerMap = QueryProcessing.sortByValue(toBeSorted);
//        HashMap<String,Double> linksRankedMap = QueryProcessing.replaceIDByLink(toBeSorted);

        /*System.out.println(finalJSONARRAY);
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
//        /*---------------     Start Indexing ----------------------*/
//
//
//
//
//        // connect to db



//        DataBase connect = new DataBase();
//
//        // get links from db
//        ResultSet links = connect.getAllUrls();
////        int numberOfRecords = 0;
////        try {
////            if(links.next()){
////                numberOfRecords = links.getRow();
////            }
////        } catch (SQLException e) {
////        }
//        int ID = 0;
//        String myLink = "";
//        String[][] linksInfo = new String[6000][2];
//        int i = 0,size = 0;
//
//        // extracting the links from the result set
//        try{
//        while (links.next()) {
//            try {
//                myLink= links.getString("Link");
//                size++;
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            try {
//                ID = links.getInt("Id");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            linksInfo[i][0] = myLink;
//            linksInfo[i++][1] = String.valueOf(ID);
//        }
//        } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        // needed files
//        WorkingFiles files = new WorkingFiles(size);
//
//
//        // Threading
//        int threadCount = 5,
//                counter = 0,
//                threadsCounter = 0;
//        boolean done = false;
//
//        while (! done)
//        {
//            // creating Threads
//            Thread[] threadsArr = new Thread[threadCount];
//            while (counter < size && threadsCounter < threadCount)
//            {
//                threadsArr[threadsCounter] = new Thread(new Indexer(linksInfo[counter][0], linksInfo[counter][1], files));
//                threadsArr[threadsCounter].start();;
//                counter++;
//                threadsCounter++;
//            }
//            for (int j = 0; j < threadsCounter; j++)
//            {
//                try {
//                    threadsArr[j].join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            done = counter == size;
//            threadsCounter = 0;
//        }
//
//
//        System.out.println("DONE !\n");
//
//

//
//
//
//        /*---------------     End Of Indexing ----------------------*/


    }
}
