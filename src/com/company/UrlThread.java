package com.company;

//-------------------------- Imports Section--------------------------//
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.UnsupportedEncodingException;

//Net Imports
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URISyntaxException;


// Jsoup Imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//-------------------------------------------------------------------//













//-------------------------- Class UrlThread--------------------------//
/*
* This class extends from Thread


* Data member:
    1-Limit : it is Static data member used to count the number of the urls visited and to stop the crawler
    * Small hint : if the Crawling is interrupted i will know the position of the interrruption by using the Layer and FirstUrl
        2-FirstUrl: it is the first url must be visit to crawling the data it is retriving from the database
        3-Layer : to know the layer of the Thread to start from it by using the FirstUrl

*Functions :
    1-run
    2-incrementLimit
    3-linkProcessing
* */

public class UrlThread extends Thread {

    //--------------------- The Data Members-------------------------//
    private static int Limit=0;
    private int FirstUrl;
    private int layer;
    private DataBase DataBaseObject;
    //---------------------------------------------------------------//

    //--------------------- Constructor-----------------------------//
    /*
        * Explanation:
            Constructor to initialize the object of the Database
    */

    public UrlThread()
    {
        DataBaseObject=new DataBase();
    }
    //---------------------------------------------------------------//




    //--------------------------Function run--------------------------//
    /*
        * Explanation:
            This function will get the position of the Thread from the database
            and get the parent link of the required link
            and will call the linkProcessing function
    */
    public void run()
    {

    try
        {

            // query to get the url index and layer to start from
            ResultSet Position =DataBaseObject.getThreadPosition(Thread.currentThread().getName());
            // give it the link of the parent and in the inner loop will skip until reach to the target link
            ٍResultSet ParentData=DataBaseObject.getParentUrl(Thread.currentThread().getName(),(Integer)Position.getInt("Layer"),(Integer)Position.getInt("UrlIndex"));
            // get the parent link
            FirstUrl=(Integer)Position.getInt("UrlIndex");
            Limit+=DataBaseObject.getCompleteCount();
            linkProcessing(ParentData.getString("Link"),(Integer)Position.getInt("Layer"),(Integer)Position.getInt("UrlIndex"),ParentData.getInt("ParentId"));
        }
    catch(SQLException e)
        {
            System.out.println(e);
        }

    }

    //---------------------------------------------------------------//



    //--------------------------Function IncrementLimit--------------------------//
    /*
        * Explanation:
            This function static function  will increment the Limit and it will be Synchronized Function
    */
    public static synchronized void IncrementLimit()
    {
        Limit++;
    }
    //---------------------------------------------------------------//


    //--------------------------Function Normalized an repeated--------------------------//
    /*
        * Explanation:

    */
    public String Normalized(String Url)
    {
        URL url = null;
        try {
            url = new URL(Url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URI uri = null;
        try {
            uri = new URI(url.getProtocol(),
                    url.getUserInfo(),
                    url.getHost(),
                    url.getPort(),
                    url.getPath(),
                    url.getQuery(),
                    url.getRef());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        /////// UPPERCASE TRIPLETS: AFTER % ENCODING///////
        StringBuffer sb = new StringBuffer(uri.toString());
        int index = sb.indexOf("%");
        while (index >= 0) {
            if(sb.charAt(index)>=97 && sb.charAt(index)<=122)
            {
                sb.replace(index, index+1, Character.toString(sb.charAt(index)-32));
            }
            if(sb.charAt(index+1)>=97 && sb.charAt(index+1)<=122)
            {
                sb.replace(index+1, index+2,  Character.toString(sb.charAt(index+1)-32));
            }
            index = sb.indexOf("%", index + 1);
        }

        sb = new StringBuffer(uri.toString());



        ////////// NORMALIZE AND REMOVE UNRESERVED CHARACTERS////////////
        String result;
        uri = uri.normalize();
        try{result = URLDecoder.decode(sb.toString(), "UTF-8").replaceAll("\\+", "%20")
                .replaceAll("\\%7E", "~").replaceAll("%2D", "-").replaceAll("%2E", ".").replaceAll("%5F", "_");} catch (UnsupportedEncodingException e)
        {
            result = sb.toString();
        }
        try
        {
            url = new URL (result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            uri = new URI(url.getProtocol(),
                    url.getUserInfo(),
                    url.getHost(),
                    url.getPort(),
                    url.getPath(),
                    url.getQuery(),
                    url.getRef());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        uri = uri.normalize();


        //---------------------------------------------------------//
            String NormalizedUrl=uri.toString();
        try {
                if(DataBaseObject.getUrls(NormalizedUrl).next())
                {
                    return "-1";
                }
           }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return NormalizedUrl;
    }
    //---------------------------------------------------------------//



    //--------------------------Function linkProcessing--------------------------//
    /*
        * Explanation:
            This function will check first if the Limit is equal to 5000 or not
            then is the Limit equal to 5000 the current thread will be interrupted and terminated and will set the position of the Thread to layer and Urlindex to zero
            then if not it will set the position of the current Thread in the database
            then will check if the current link if repeated or not or normalized
            if not will call function to insert the link in the database
            and then will create object from Document called 'doc'  to use it in retriving the page content
            and then extract the important things from the page
            and then will extract the links from the page
            and then will check if the current layer is less than or equal 2 or not to limit the Number of layers to Three Layers
            if less than or equal will loop on the links and call recursively function linkProcessing
            the checking if the FirstUrl is =0 or not to reach the target link if it is interrupted
            The counter variable to set the Index in the Function to Know the position of the Thread
            and then after the loop will mark the url as Completed
    */
    public void linkProcessing(String Url,int Layer,int Index,int ParentId)
    {
        // query to set the current thread with the layer and the index
        if(Limit!=5000)
        {
            // query to set the current layer and the current index
            DataBaseObject.setThreadPosition(Thread.currentThread().getName(), Layer,Index);

            // query to check if the current link is not repeated or not  and if it is normalized by using one function

            if(Normalized(Url)!="-1")
            {
                try
                {
                    // call function to insert the link into the database
                    DataBaseObject.createLink(Url,Layer,Thread.currentThread().getName(), ParentId);
                    int parentId=0;
                    try {
                        parentId =(Integer)DataBaseObject.getId(Url,Thread.currentThread().getName()).getInt("Id");
                    }
                    catch(SQLException ex){
                        ex.printStackTrace();
                    }

                    // call connect the current url and get the content
                    Document doc= Jsoup.connect(Url).get();

                    // call function
                    // get the urls from the site
                    Elements links = doc.select("a[href]");
                    //check if the current layer is 2 or less than to go to the next layer if not i will stop
                    if (Layer <=2)
                    {

                        // counter is used to count the number of the links in the array and to tell to me the current  position
                        int counter=0;
                        // loop on the links
                        for(Element link: links)
                        {
                            if(FirstUrl==0)
                            {
                                // check if the current link not contain zip string to avoid the links of the download

                                if(!link.attr("href").contains("zip"))
                                {

                                    // recursive call with increment of the layer and the counter
                                    linkProcessing(link.attr("href"),Layer+1,counter,parentId);
                                    counter++;
                                    // call function to increment the limit counter
                                    IncrementLimit();
                                }

                            }
                            else
                            {
                                FirstUrl--;
                            }
                        }
                    }
                    // query mark the current link as completed
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Thread.currentThread().interrupt();
                // query to set the layer and the index to 0 setThread Position
                DataBaseObject.setThreadPosition(Thread.currentThread().getName(),0,0);

            }

    }
}
