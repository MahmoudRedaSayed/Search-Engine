package TestingPackage;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import CrawlerPackages.Crawler.*;
import CrawlerPackages.Crawler.UrlThread.*;
import DataBasePackages.DataBase.DataBase;
import HelpersPackages.Helpers.WorkingFiles;
import IndexerPackages.Indexer.Indexer;
import QueryProcessingPackages.Query.QueryProcessing;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonString;
import org.json.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, JSONException {

//       Thread ThreadsArray=new Thread(new UrlThread());
//        Thread.currentThread().setName("Thread1");
//        ThreadsArray.setName("Thread2");
//        ThreadsArray.run();



//         Thread ThreadsArray2=new Thread(new UrlThread());
//        ThreadsArray2.setName("Thread2");
//        ThreadsArray2.start();
//        ArrayList<String> Disallowed =ThreadsArray.robotSafe("https://www.bbc.co.uk/");
//        System.out.println("Disallowed");
//        DataBase DataBaseObject=new DataBase();
//        UrlThread.Limit+=DataBaseObject.getCompleteCount();
//        System.out.printf(" the limit %d",UrlThread.Limit);

//        Thread ThreadsArray[]=new Thread[43];
//        ThreadsArray[0]=Thread.currentThread();
//        ThreadsArray[0].setName("Thread1");
//        for(int i=1;i<43;i++)
//        {
//            ThreadsArray[i] = new Thread(new UrlThread());
//            ThreadsArray[i].setName("Thread" + (i+1));
//            ThreadsArray[i].start();
//        }
//        for(int i=1;i<43;i++)
//        {
//
//            try {
//                ThreadsArray[i].join();
//            } catch (InterruptedException e) {
//
//            }
//        }
//
//        try {
//            ThreadsArray.join();
//            ThreadsArray2.join();
//
//        } catch (InterruptedException e) {
//
//            }
        System.out.println("The limit  "+UrlThread.Limit+" The inserted  "+UrlThread.inserted);



        ///////////////////////////////////////////////////////

        /*---------------     Start Indexing ----------------------*/

        /*WorkingFiles files = new WorkingFiles();

        DataBase connect = new DataBase();

        ResultSet links = connect.getAllUrls();

        int ID = 0;
        String myLink = "";
        String[][] linksInfo = new String[5100][2];

        int i = 0,size = 0;
        while (true) {
            try {
                if (!links.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                myLink= links.getString("LINK");
                size++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ID = links.getInt("ID");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            linksInfo[i][0] = myLink;
            linksInfo[i++][1] = String.valueOf(ID);
        }

        int threadCount = 5,
                counter = 0,
                threadsCounter = 0;
        boolean done = false;

        while (! done)
        {
            // creating Threads
            Thread[] threadsArr = new Thread[threadCount];
            while (counter < size && threadsCounter < threadCount)
            {
                threadsArr[threadsCounter] = new Thread(new Indexer(linksInfo[counter][0], linksInfo[counter][1], files));
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
            done = counter == size;
            threadsCounter = 0;
        }

        System.out.println("DONE !\n");*/

        /*---------------     End Of Indexing ----------------------*/




        /*---------------     Query Processing  ----------------------*/
        WorkingFiles files = new WorkingFiles();
        QueryProcessing test = new QueryProcessing(files);

        String query = "Computed is our life";

        JSONArray jsonFile  = test.run(query);
        String json = jsonFile.toString() ;
        System.out.println(json);

        /*---------------     End Of Query Processing ----------------------*/

    }
}
