package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class UrlThread extends Thread {
    public static int Limit;
    private String Url;
    private List<String> arrlinks;
    private int count;
    private int layer;
    public UrlThread(String Url)
    {
        this.Url=Url;
         this.arrlinks=new ArrayList<String>();
    }
    public void run()
    {

        // query to get the url index and layer to start from
        // give it the link of the parent and in the inner loop will skip until reach to the target link
        
        //linkProcessing(Url,Layer,count);

    }

    public synchronized void incrementLimit()
    {
        Limit++;
    }

    public void linkProcessing(String Url,int Layer,int Index)
    {
        // query to set the current thread with the layer and the index
        if(Limit!=5000)
        {
            // query to set the current layer and the current index
            // query to check if the current link is not repeated or not  and if it is normalized by using one function
            if(true)
            {
                try
                {
                    // call function to insert the link into the database

                    // call connect the current url and get the content
                    Document doc= Jsoup.connect(Url).get();
                    // el7gat
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
                            if(count==0)
                            {
                                // check if the current link not contain zip string to avoid the links of the download

                                if(!link.attr("href").contains("zip"))
                                {

                                    // recureive call with increment of the layer and the counter
                                    linkProcessing(link.attr("href"),Layer+1,counter);
                                    counter++;
                                    // call function to increment the limit counter
                                    incrementLimit();
                                }

                            }
                            else
                            {
                                count--;
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

            }

            return;

    }
}
