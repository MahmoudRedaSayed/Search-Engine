package TestingPackage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.util.*;
import CrawlerPackages.Crawler.*;
import DataBasePackages.DataBase.*;
import  IndexerPackages.Indexer.*;
import HelpersPackages.Helpers.*;
import RankerPackage.Ranker.*;
import java.util.Map;
import org.json.JSONException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, JSONException {
        //----------------------------------------Crawler-----------------------------------------//

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        int numberOfThreads;

        System.out.println("Enter the number of Threads :");
        String str = null;
        try {
            str = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str);
        numberOfThreads=Integer.parseInt(str);
        //first get the number of the inserted links
        //then check if it equals to 5100
        DataBase dataBaseObj=new DataBase();
        UrlThread.Limit+=dataBaseObj.getCompleteCount();
        if(UrlThread.Limit>=5100)
        {
            //new crawling delete the data base and set Limit=0
            dataBaseObj.deleteLinks();
            dataBaseObj.updateThreads();
            UrlThread.Limit=0;

        }
        Thread ThreadsArray[]= new Thread[72];
        int count=72,threadCounter=0;

        while(count>=0)
        {

            for(int i=threadCounter;i<numberOfThreads+threadCounter;i++)
            {
                ThreadsArray[i]=new Thread(new UrlThread());
                ThreadsArray[i].setName("Thread"+(i+1));
            }
            for(int i=threadCounter;i<numberOfThreads+threadCounter;i++)
            {
                ThreadsArray[i].start();
            }
            for(int i=threadCounter;i<numberOfThreads+threadCounter;i++)
            {
                try {
                    ThreadsArray[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            threadCounter+=numberOfThreads;
            count-=numberOfThreads;
        }
        //-------------------------------------------------------------------------------------------------------------//

            /*---------------     Start Indexing ----------------------*/
        WorkingFiles.createInvertedFiles();

        // connect to db
        DataBase connect = new DataBase();

        // get stop words
        Map<Character, Vector<String>> stopWords = WorkingFiles.getStopWordsAsMap();

        // get links from db
        int linksCount = connect.getNotIndexed();
        String[] completedLinks = connect.getAllUrls();
        int i = completedLinks.length;

        // Threading
        int threadCount = threadCounter,
                counter = 0,
                threadsCounter = 0,
                finished = 0;
        boolean done = false;

        while (! done)
        {
            // creating Threads
            Thread[] threadsArr = new Thread[threadCount];
            while (counter < i && threadsCounter < threadCount)
            {
                threadsArr[threadsCounter] = new Thread(new Indexer(completedLinks[counter], stopWords, connect));
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
            finished += threadCount;
            System.out.println("finished Indexing : " + finished);
            System.out.println("finish " +finished);

        }


        System.out.println("Indexing is finished :)\n");

        // removing the empty files
        WorkingFiles.removeEmptyFiles();
        System.out.println("Removed empty files");
            /*---------------     End Of Indexing ----------------------*/

        /*-------------------------------popularity secation------------------------*/
        Ranker rankerObj=new Ranker();


    }
}
