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
import IndexerPackages.Indexer.PageParsing;
//import PhraseSearchingPackages.PhraseSearching.*;
//import ServletsPackages.ServletPackage.QuerySearch;
import IndexerPackages.Indexer.Indexer;
import com.mysql.cj.xdevapi.DatabaseObject;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonString;
//import QueryProcessingPackages.Query.QueryProcessing;
//import RankerPackage.Ranker.*;
import org.json.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.print.Doc;
import javax.xml.crypto.Data;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, JSONException {

        //----------------------------------------Crawler-----------------------------------------//

        Thread ThreadsArray[]= new Thread[70];

        UrlThread.Limit=0;
        for(int i=0;i<25;i++)
        {
            ThreadsArray[i]= new Thread(new UrlThread());
            ThreadsArray[i].setName("Thread"+(i+1));
        }
        for(int i=0;i<25;i++)
        {
            ThreadsArray[i].start();
        }
        for(int i=0;i<25;i++)
        {
            try {
                ThreadsArray[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        UrlThread.Limit=200;
        for(int i=25;i<50;i++)
        {
            ThreadsArray[i]= new Thread(new UrlThread());
            ThreadsArray[i].setName("Thread"+(i+1));
        }
        for(int i=25;i<50;i++)
        {
            ThreadsArray[i].start();
        }
        for(int i=25;i<50;i++)
        {
            try {
                ThreadsArray[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        UrlThread.Limit=100;
        for(int i=50;i<70;i++)
        {
            ThreadsArray[i]= new Thread(new UrlThread());
            ThreadsArray[i].setName("Thread"+(i+1));
        }
        for(int i=50;i<70;i++)
        {
            ThreadsArray[i].start();
        }
        for(int i=50;i<70;i++)
        {
            try {
                ThreadsArray[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        //-------------------------------------------------------------------------------------------------------------//
        // DataBase DataBaseObject = new DataBase();
        // UrlThread.Limit+=DataBaseObject.getCompleteCount();
//         Thread ThreadsArray[]=new Thread[51];
//
//        JSONArray dividedQuery = new JSONArray();
//        Ranker rankerObj = new Ranker();
//        String finalJSONARRAY;
//        QueryProcessing obj = new QueryProcessing();
//        PhraseSearching phraseSearchingObj = new PhraseSearching();
//        String searchingQuery;
//        ArrayList<String> rankerArray = new ArrayList<String>();
//        searchingQuery = " provide programs";
//        System.out.println(searchingQuery);
//        finalJSONARRAY = obj.run(searchingQuery, rankerArray, dividedQuery);
////        HashMap<String, Double> toBeSorted = new HashMap<String, Double>();
////        toBeSorted.put("link1", 0.25);
////        toBeSorted.put("Link2", 0.5);
////        toBeSorted.put("Link3", 0.45);
//
//        Map<String, Double> rankingResult = rankerObj.calculateRelevance(rankerArray);
//        HashMap<String, Double> toBeSorted = new HashMap<String, Double>(rankingResult);
//        HashMap<String, Double> sortedRankerMap = QueryProcessing.sortByValue(toBeSorted);
//       // HashMap<String,Double> linksRankedMap = QueryProcessing.replaceIDByLink(toBeSorted);
//
//        for (Map.Entry<String, Double> entry : sortedRankerMap.entrySet()) {
//            System.out.println(entry.getKey() /*+ "   :" + entry.getValue().toString()*/);
//          }
//
//            System.out.println(finalJSONARRAY);
//            System.out.println(dividedQuery.toString());
//            System.out.println(rankerArray.toString());

//        DataBase databaseObj  = new DataBase();
//        System.out.println(databaseObj.getCompleteCount());
//           Thread ThreadsArray=new Thread(new UrlThread());
//             Thread.currentThread().setName("Thread1");
////             Thread.currentThread().start();
//           ThreadsArray.setName("Thread2");
//           ThreadsArray.run();
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

//        ///////////////////////////////////////////////////////
//
            /*---------------     Start Indexing ----------------------*/
      //   create files
//        WorkingFiles.createInvertedFiles();
//
//        // connect to db
//        DataBase connect = new DataBase();
//
//        // get stop words
//        Map<Character, Vector<String>> stopWords = WorkingFiles.getStopWordsAsMap();
//
//        // get links from db
//        int linksCount = connect.getCompleteCount();
//        String[] completedLinks = connect.getAllUrls();
//        int i = completedLinks.length;
//
//        // Threading
//        int threadCount = 10,
//                counter = 0,
//                threadsCounter = 0,
//                finished = 0;
//        boolean done = false;
//
//        while (! done)
//        {
//            // creating Threads
//            Thread[] threadsArr = new Thread[threadCount];
//            while (counter < i && threadsCounter < threadCount)
//            {
//                threadsArr[threadsCounter] = new Thread(new Indexer(completedLinks[counter], stopWords, connect));
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
//            done = counter == i;
//            threadsCounter = 0;
//            finished += threadCount;
//            System.out.println("finished Indexing : " + finished);
////            if (finished == 1000)
////                done = true;
//        }
//
//
//        System.out.println("Indexing is finished :)\n");
//
//        // removing the empty files
//        WorkingFiles.removeEmptyFiles();
//        System.out.println("Removed empty files");
//
//            /*---------------     End Of Indexing ----------------------*/

        /*-------------------------------popularity secation------------------------*/

//        DataBase con = new DataBase();
//        String[] arr = {"wayback", "machine"};
//        ArrayList<String>links = new ArrayList<>();
//        links.add("https://web.archive.org/");
//        links.add("https://cancer.osu.edu");
//        Map<String, String> d = con.getLinksParagraphs(links);
//
//
//        Map<String, String> res =  HelperClass.getSnippet(arr, links, d);
//        System.out.println("herh");





    }
}
