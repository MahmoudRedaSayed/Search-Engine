package CrawlerPackages.Crawler;

//-------------------------- Imports Section--------------------------//
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.UnsupportedEncodingException;

//Net Imports
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;


// Jsoup Imports
import DataBasePackages.DataBase.DataBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
// indexing
import IndexerPackages.Indexer.PageParsing;
import org.json.*;
//-------------------------------------------------------------------//













//-------------------------- Class UrlThread--------------------------//
/*
* This class extends from Thread


* Data member:
    //

*Functions :
    1-run
    2-incrementLimit
    3-getLimit
    4-DisallowedCheck
    5-Normalized
    6-linkProcessing
    7-robotSafe
* */

public class UrlThread implements  Runnable {

    //--------------------- The Data Members-------------------------//
    public static int Limit=0;
    public static int inserted=0;
    private  int FirstUrlLayer1;
    private  int FirstUrlLayer2;
    private  int FirstUrlLayer3;
    private StringBuffer parentLink=new StringBuffer("");
    private StringBuffer grandLink=new StringBuffer("");
    private String currentLink=new String("");
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
        System.out.printf("From the constructor\n");
        DataBaseObject=new DataBase();
        //Limit+=DataBaseObject.getCompleteCount();
    }
    //---------------------------------------------------------------//




    //--------------------------Function run--------------------------//
    /*
        * Explanation:
            This function will get the position of the Thread from the database
            and get the parent link of the required link
            and will call the linkProcessing function
    */
    public void run() {

//        synchronized (this) {
        try {
                // query to get the url index and layer to start from
                ResultSet Position = DataBaseObject.getThreadPosition(Thread.currentThread().getName());

                // give it the link of the parent and in the inner loop will skip until reach to the target link
                int Layer1 = -1;
                while (Position.next()) {
                    FirstUrlLayer1 = Position.getInt("UrlIndex1");
                    FirstUrlLayer2 = Position.getInt("UrlIndex2");
                    FirstUrlLayer3 = Position.getInt("UrlIndex3");
                    Layer1 = Position.getInt("Layer");
                }

                parentLink.delete(0, parentLink.length());
                grandLink.delete(0, parentLink.length());
                currentLink = "";
                ResultSet ParentData = DataBaseObject.getParentUrl(Thread.currentThread().getName(), parentLink, grandLink, currentLink, Layer1);
                String ParentLink = "";
                int Id = -1;
                if (ParentData == null) {
                    Thread.currentThread().interrupt();
                    return;
                }
                while (ParentData!= null && ParentData.next()) {
                    Id = ParentData.getInt("LinkParent");
                    Layer1 = ParentData.getInt("Layer");
                }
                Layer1=1;
                if (Layer1 == 1) {
                    linkProcessing(grandLink.toString(), Layer1, FirstUrlLayer1, FirstUrlLayer2, FirstUrlLayer3, Id);
                } else if (Layer1 == 2) {
                    linkProcessing(parentLink.toString(), Layer1, FirstUrlLayer1, FirstUrlLayer2, FirstUrlLayer3, Id);
                } else {
                    linkProcessing(currentLink, Layer1, FirstUrlLayer1, FirstUrlLayer2, FirstUrlLayer3, Id);
                }

            }
    catch(SQLException e)
            {
                System.out.println(e);
            }

        }
    //}

    //---------------------------------------------------------------//



    //--------------------------Function IncrementLimit and getLimit----------------------------------------------------//
    /*
        * Explanation:
            This two functions static functions  responsible for the Limit
    */
    public static synchronized void IncrementLimit()
    {
        UrlThread.Limit++;
    }

    public static synchronized int getLimit()
    {
        return UrlThread.Limit;
    }
    //------------------------------------------------------------------------------------------------------------------//


    public static synchronized void IncrementInserted()
    {
        UrlThread.inserted++;
    }




        public static ArrayList<String> robotSafe(String url1,ArrayList<String> Allowed)  {
            System.out.println(Thread.currentThread().getName());
            ArrayList<String> Disallowed = new ArrayList<>();
            URL url;
            try { url =new URL(url1);
            } catch (MalformedURLException e) {
                // something weird is happening, so don't trust it
                return Disallowed;
            }
            String strHost = url.getHost();

            String strRobot = "https://" + strHost + "/robots.txt";
            URL urlRobot;
            try { urlRobot = new URL(strRobot);
            } catch (MalformedURLException e) {
                // something weird is happening, so don't trust it
                return Disallowed;
            }

            String strCommands="";
            try
            {
                InputStream urlRobotStream = urlRobot.openStream();
                byte b[] = new byte[1000];
                int numRead = urlRobotStream.read(b);
                if(numRead!=-1)
                {
                    strCommands = new String(b, 0, numRead);
                }
                else
                {
                    strRobot = "http://" + strHost + "/robots.txt";
                    try { urlRobot = new URL(strRobot);
                    } catch (MalformedURLException e) {
                        // something weird is happening, so don't trust it
                        return Disallowed;
                    }
                    urlRobotStream = urlRobot.openStream();
                    numRead = urlRobotStream.read(b);
                    if(numRead!=-1)
                    {
                        strCommands = new String(b, 0, numRead);
                    }
                }
                while (numRead != -1) {
                    numRead = urlRobotStream.read(b);
                    if (numRead != -1)
                    {
                        String newCommands = new String(b, 0, numRead);
                        strCommands += newCommands;
                    }
                }
                urlRobotStream.close();
            }
            catch (IOException e)
            {
                return Disallowed; // if there is no robots.txt file, it is OK to search
            }

            if (strCommands.contains("Disallow")) // if there are no "disallow" values, then they are not blocking anything.
            {
                String[] split = strCommands.split("\n");
                ArrayList<RobotRule> robotRules = new ArrayList<>();
                String mostRecentUserAgent = null;
                for (int i = 0; i < split.length; i++)
                {
                    String line = split[i].trim();
                    if (line.toLowerCase().startsWith("user-agent"))
                    {
                        int start = line.indexOf(":") + 1;
                        int end   = line.length();
                        mostRecentUserAgent = line.substring(start, end).trim();
                    }
                    else if (line.contains("Disallow")) {
                        if (mostRecentUserAgent != null) {
                            RobotRule r = new RobotRule();
                            r.userAgent = mostRecentUserAgent;
                            int start = line.indexOf(":") + 1;
                            int end   = line.length();
                            r.rule = line.substring(start, end).trim();
                            robotRules.add(r);
                        }
                    }
                    else if (line.contains("Allow")) {
                        if (mostRecentUserAgent != null) {
                            RobotRule r = new RobotRule();
                            r.userAgent = mostRecentUserAgent;
                            int start = line.indexOf(":") + 1;
                            int end   = line.length();
                            r.allowRule = line.substring(start, end).trim();
                            robotRules.add(r);
                        }
                    }
                }
                for (RobotRule robotRule : robotRules)
                {
                    if(robotRule.userAgent.equals("Java 17.0.2") ||robotRule.userAgent.equals("*") )
                    {
                        String [] urls=null;
                        if(robotRule.rule!=null)
                        {
                           urls= robotRule.rule.split("\n");
                            for(int i=0;i<urls.length;i++)
                            {
                                if(!urls[i].equals(""))
                                Disallowed.add(urls[i]);
                            }
                        }

                        if(robotRule.allowRule!=null)
                        {
                            urls= robotRule.allowRule.split("\n");
                            for(int i=0;i<urls.length;i++)
                            {
                                Allowed.add(urls[i]);
                            }
                        }

                    }


                }
            }
            return Disallowed;
        }
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
            return "-1";
        }
        URI uri = null;
        try {
            if(url!=null) {
                uri = new URI(url.getProtocol(),
                        url.getUserInfo(),
                        url.getHost(),
                        url.getPort(),
                        url.getPath(),
                        url.getQuery(),
                        url.getRef());
            }
        } catch (URISyntaxException e) {
            return "-1";
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
            if(url!=null)
            {
                try {
                    if (url.getProtocol().toLowerCase() == "https" || url.getProtocol().toLowerCase() == "http") {
                        return "-1";
                    }
                }
                catch (Exception e)
                {
                    return "-1";
                }
                try {
                    uri = new URI(url.getProtocol(),
                            url.getUserInfo(),
                            url.getHost(),
                            url.getPort(),
                            url.getPath(),
                            url.getQuery(),
                            url.getRef());

                }
                catch (URISyntaxException e) {
                    return "-1";

                }
            }

        } catch (MalformedURLException e) {
            return "-1";
        }

        uri = uri.normalize();

        sb = new StringBuffer(uri.toString());
        String NormalizedUrl=sb.toString();
        if(sb.charAt(sb.length()-1) == '/')
        {
            NormalizedUrl=NormalizedUrl.substring(0,NormalizedUrl.length()-1);
        }


//        ---------------------------------------------------------//
        try {
            if(DataBaseObject.getUrls(NormalizedUrl) != null) {
                if (DataBaseObject.getUrls(NormalizedUrl).next()) {
                    System.out.printf("before calling the function \n");
                    return "-1";
                }
            }
           }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return NormalizedUrl;
    }
    //---------------------------------------------------------------//

    //------------------------------------------------------------------------------------------------------------------//
    /*
    * Explanation:-
    *   is responsible for checking if the link is available by the robot.txt or not , and it returns boolean variable
    * */
    public boolean DisallowedCheck(ArrayList<String> Disallowed,ArrayList<String> Allowed,String link){
        for(int i=0;i<Disallowed.size();i++)
        {
            if(link.contains(Disallowed.get(i)))
            {
                for(int j=0;j<Allowed.size();j++)
                {
                    if(link.contains(Disallowed.get(i)))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    //------------------------------------------------------------------------------------------------------------------//


    //--------------------------Function linkProcessing-----------------------------------------------------------------//
    /*
    * Explanation:-
    *   this function is responsible for the extracting the links and manage the limit of the links
    * */
    public synchronized void linkProcessing(String Url,int Layer,int Index1,int Index2,int Index3,int ParentId)
    {
        //-----------------------------------------------------------------------------------------------------------------//
        /*
        * Explanation:-
        *   This block of code will ckeck if the link is in layer 1 or not
        *   and will ckeck if the link is exist or not and the existence of the link here (mean in this block ) means that is interrupted
        *   if the link is not exist it will insert the link in the database
        *   and then get the id of the inserted link to use it in the parent link with the child
        * */
        // query to check if the current link is not repeated or not  and if it is normalized by using one function
                    // call function to insert the link into the database
                    if (!(ParentId==-1))
                    {
                        boolean found=false;
                        ResultSet data =DataBaseObject.getUrls2(Url);
                        try {
                            while (data != null && data.next()) {
                                found = true;
                            }
                            if (!found) {
                                DataBaseObject.createLink(Url, Layer, Thread.currentThread().getName(), ParentId);
                                IncrementInserted();

                            }
                        }
                        catch( SQLException e)
                        {

                        }
                    }
                    int parentId = 0;
                    parentId = DataBaseObject.getId(Url, Thread.currentThread().getName());
        //-----------------------------------------------------------------------------------------------------------------//



        //-----------------------------------------------------------------------------------------------------------------//
        /*
        * explanation:-
        *   we will divide the code into three layers to handle the interrupt part
        *   and it will help to know which variable i will use to skip the completed links in every Layer
        *       the interruption handling:-
        *           Layer one : will use the grandLink (which means Grand parent Link) and will use FirstUrl1 to skip the completed links
        *           Layer two : will use the parentLink (which means Parent Link) and will use FirstUrl2 to skip the completed links
        *           Layer three : will use the currentLink (which means the link in layer three) and will use FirstUrl3 to skip the completed links
        * */
                    if(Layer==1)
                    {
                        DataBaseObject.setThreadPosition(Thread.currentThread().getName(), Layer,0);

                        int counter = 0;
                        try {

                            //-----------------------------------------------------------------------------------------------------------------//
                            // get the document and get the links from it

                            Document doc = Jsoup.connect(Url).get();

                            // ------------------------------------------The data of the links the content and the paragraphs and the header and title -------------------//
                            /*
                            * this block to add the content of the link into the database put separeted
                            * */
                            PageParsing pageContent=new PageParsing(doc);
                            String headers=Arrays.toString(pageContent.getHeaders()).replace("'","\\\'").replace("\"","\\\"");
                            String title=pageContent.getTitleTag().replace("'","\\\'").replace("\"","\\\"");
                            String paragraphs= Arrays.toString(pageContent.getParagraphs()).replace("'","\\\'").replace("\"","\\\"");
                            String listItems=Arrays.toString(pageContent.getListItems()).replace("'","\\\'").replace("\"","\\\"");
                            String strongWords=Arrays.toString(pageContent.getStrongs()).replace("'","\\\'").replace("\"","\\\"");
                            DataBaseObject.addElements(parentId,paragraphs,title,headers,listItems,strongWords);

                            //----------------------------------------------------------------------------------------------------------------------------------------------//
                            // check if its content is same content to another link in the database
                            String content= DataBaseObject.getContent(parentId);
                            ResultSet contentResultSet=DataBaseObject.getContents(content,parentId);

//                            try {
//                                if ((contentResultSet!=null&&contentResultSet.next())) return;
//                            }
//                            catch (SQLException e) {
//                                e.printStackTrace();
//                            }
                            //-----------------------------------------------------------------------------------------------------------------------------------------------//
                            try {
                                String desc = doc.select("meta[name=description]").get(0)
                                        .attr("content").replace("'","\\\'").replace("\"", "\\\"");
                                DataBaseObject.addDesc(parentId, desc);
                            }
                            catch (IndexOutOfBoundsException e)
                            {
                                DataBaseObject.addDesc(parentId, "");
                            }
                            Elements links = doc.select("a[href]");
                            //-----------------------------------------------------------------------------------------------------------------//

                            //-----------------------------------------------------------------------------------------------------------------//
                            // call function robotSafe
                            // this part will be extracted from the robot.txt file the Allowed and Disallowed the Allowed will be sent by reference
                            ArrayList<String> Allowed=new ArrayList<>();
                            ArrayList<String> Disallowed = robotSafe(Url,Allowed);
                            //-----------------------------------------------------------------------------------------------------------------//

                            boolean forbidden=false;
                            int flag=FirstUrlLayer1;
                            for (Element link : links)
                            {
                                counter++;
                                if (FirstUrlLayer1 == 1) {

                                    String result = Normalized(link.attr("href"));

                                    forbidden=DisallowedCheck(Disallowed,Allowed,link.attr("href"));

                                    if (getLimit() < 5000 && result != "-1"&&!forbidden) {
                                        try {
                                            //-----------------------------------------------------------------------------------------------------------------//
                                            // this part to check if the link is inserted by another thread or not
                                            if(flag==1)
                                            {
                                                ResultSet resultSet=DataBaseObject.getUrls2(link.attr("href"));
                                                if (resultSet!=null&&resultSet.next())
                                                {
                                                    continue;
                                                }

                                            }
                                            else{
                                                flag=1;
                                            }
                                        //-----------------------------------------------------------------------------------------------------------------//

                                            linkProcessing(result, Layer + 1, counter,Index2,Index3, parentId);
                                            ResultSet resultSet=DataBaseObject.getUrls2(result);
                                            while (resultSet.next())
                                            {
                                                IncrementLimit();
                                            }
                                        }
                                        catch( Exception e)
                                        {

                                        }

                                    } else if (getLimit() >= 5000) {
                                        // query to set the layer and the index to 0 setThread Position
                                        DataBaseObject.setThreadPosition(Thread.currentThread().getName(), -1, 0);
                                        Thread.currentThread().interrupt();
                                        break;
                                    }
                                } else {
                                    FirstUrlLayer1--;
                                }
                            }
                            DataBaseObject.urlCompleted(Url);
                        }
                        catch (IOException e)
                        {

                        }


                    }
                    else if (Layer==2)
                    {
                        DataBaseObject.setThreadPosition(Thread.currentThread().getName(), Layer, Index1);

                        int counter = 0;
                        try {
                            //-----------------------------------------------------------------------------------------------------------------//
                            // get the document and get the links from it
                            Document doc = Jsoup.connect(Url).get();
                            // ------------------------------------------The data of the links the content and the paragraphs and the header and title -------------------//
                            /*
                             * this block to add the content of the link into the database put separeted
                             * */
                            PageParsing pageContent=new PageParsing(doc);
                            String headers=Arrays.toString(pageContent.getHeaders()).replace("'","\\\'").replace("\"","\\\"");
                            String title=pageContent.getTitleTag().replace("'","\\\'").replace("\"","\\\"");
                            String paragraphs= Arrays.toString(pageContent.getParagraphs()).replace("'","\\\'").replace("\"","\\\"");
                            String listItems=Arrays.toString(pageContent.getListItems()).replace("'","\\\'").replace("\"","\\\"");
                            String strongWords=Arrays.toString(pageContent.getStrongs()).replace("'","\\\'").replace("\"","\\\"");
                            DataBaseObject.addElements(parentId,paragraphs,title,headers,listItems,strongWords);

                            //----------------------------------------------------------------------------------------------------------------------------------------------//
                            // check if its content is same content to another link in the database
                            String content= DataBaseObject.getContent(parentId);
                            ResultSet contentResultSet=DataBaseObject.getContents(content,parentId);

//                            try {
//                                if ((contentResultSet!=null&&contentResultSet.next())) return;
//                            }
//                            catch (SQLException e) {
//                                e.printStackTrace();
//                            }
                            //-----------------------------------------------------------------------------------------------------------------------------------------------//

                            try {
                                String desc = doc.select("meta[name=description]").get(0)
                                        .attr("content").replaceAll("'"," ").replace('"', ' ');
                                DataBaseObject.addDesc(parentId, desc);
                            }
                            catch (IndexOutOfBoundsException e)
                            {
                                DataBaseObject.addDesc(parentId, "");
                            }
                            Elements links = doc.select("a[href]");
                            //-----------------------------------------------------------------------------------------------------------------//

                            //-----------------------------------------------------------------------------------------------------------------//
                            // call function robotSafe
                            // this part will be extracted from the robot.txt file the Allowed and Disallowed the Allowed will be sent by reference
                            ArrayList<String> Allowed=new ArrayList<>();
                            ArrayList<String> Disallowed = robotSafe(Url,Allowed);
                            //-----------------------------------------------------------------------------------------------------------------//

                            boolean forbidden=false;
                            int flag=FirstUrlLayer2;
                            for (Element link : links)
                            {
                                counter++;
                                if (FirstUrlLayer2 == 1) {
                                    String result = Normalized(link.attr("href"));

                                    forbidden=DisallowedCheck(Disallowed,Allowed,link.attr("href"));

                                    if (getLimit() < 5000 && result != "-1"&&!forbidden) {
                                        try {
                                            //-----------------------------------------------------------------------------------------------------------------//
                                            // this part to check if the link is inserted by another thread or not
                                            if(flag==1)
                                            {
                                                ResultSet resultSet=DataBaseObject.getUrls2(link.attr("href"));
                                                if (resultSet!=null&&resultSet.next())
                                                {
                                                    continue;
                                                }

                                            }
                                            else{
                                                flag=1;
                                            }
                                            //-----------------------------------------------------------------------------------------------------------------//

                                            linkProcessing(result, Layer + 1,Index1, counter,Index3, parentId);
                                            ResultSet resultSet=DataBaseObject.getUrls2(result);
                                            while (resultSet.next())
                                            {
                                                IncrementLimit();
                                            }
                                        }
                                        catch( Exception e)
                                        {

                                        }

                                    } else if (getLimit() >= 5000) {
                                        // query to set the layer and the index to 0 setThread Position
                                        DataBaseObject.setThreadPosition(Thread.currentThread().getName(), -1, 0);
                                        Thread.currentThread().interrupt();
                                        break;
                                    }
                                } else {
                                    FirstUrlLayer2--;
                                }
                            }
                            DataBaseObject.urlCompleted(Url);
                        }
                        catch (IOException e)
                        {

                        }
                    }
                    else if(Layer==3)
                    {
                        DataBaseObject.setThreadPosition(Thread.currentThread().getName(), Layer, Index2);

                        int counter = 0;
                        try {
                            //-----------------------------------------------------------------------------------------------------------------//
                            // get the document and get the links from it
                            Document doc = Jsoup.connect(Url).get();
                            // ------------------------------------------The data of the links the content and the paragraphs and the header and title -------------------//
                            /*
                             * this block to add the content of the link into the database put separeted
                             * */
                            PageParsing pageContent=new PageParsing(doc);
                            String headers=Arrays.toString(pageContent.getHeaders()).replace("'","\\\'").replace("\"","\\\"");
                            String title=pageContent.getTitleTag().replace("'","\\\'").replace("\"","\\\"");
                            String paragraphs= Arrays.toString(pageContent.getParagraphs()).replace("'","\\\'").replace("\"","\\\"");
                            String listItems=Arrays.toString(pageContent.getListItems()).replace("'","\\\'").replace("\"","\\\"");
                            String strongWords=Arrays.toString(pageContent.getStrongs()).replace("'","\\\'").replace("\"","\\\"");
                            DataBaseObject.addElements(parentId,paragraphs,title,headers,listItems,strongWords);

                            //----------------------------------------------------------------------------------------------------------------------------------------------//
                            // check if its content is same content to another link in the database
                            String content= DataBaseObject.getContent(parentId);
                            ResultSet contentResultSet=DataBaseObject.getContents(content,parentId);

//                            try {
//                                if ((contentResultSet!=null&&contentResultSet.next())) return;
//                            }
//                            catch (SQLException e) {
//                                e.printStackTrace();
//                            }
                            //-----------------------------------------------------------------------------------------------------------------------------------------------//
                            try {
                                String desc = doc.select("meta[name=description]").get(0)
                                        .attr("content").replaceAll("'"," ").replace('"', ' ');
                                DataBaseObject.addDesc(parentId, desc);
                            }
                            catch (IndexOutOfBoundsException e)
                            {
                                DataBaseObject.addDesc(parentId, "");
                            }
                            Elements links = doc.select("a[href]");
                            //-----------------------------------------------------------------------------------------------------------------//

                            //-----------------------------------------------------------------------------------------------------------------//
                            // call function robotSafe
                            // this part will be extracted from the robot.txt file the Allowed and Disallowed the Allowed will be sent by reference
                            ArrayList<String> Allowed=new ArrayList<>();
                            ArrayList<String> Disallowed = robotSafe(Url,Allowed);
                            //-----------------------------------------------------------------------------------------------------------------//

                            boolean forbidden=false;
                            int flag=FirstUrlLayer3;
                            for (Element link : links)
                            {
                                counter++;
                                if (FirstUrlLayer3 == 1) {
                                    String result = Normalized(link.attr("href"));

                                    forbidden=DisallowedCheck(Disallowed,Allowed,link.attr("href"));

                                    if (getLimit() < 5000 && result != "-1"&&!forbidden) {
                                        try {
                                            //-----------------------------------------------------------------------------------------------------------------//
                                            // this part to check if the link is inserted by another thread or not
                                            if(flag==1)
                                            {
                                                ResultSet resultSet=DataBaseObject.getUrls2(link.attr("href"));
                                                if (resultSet!=null&&resultSet.next())
                                                {
                                                    continue;
                                                }

                                            }
                                            else{
                                                flag=1;
                                            }
                                            //-----------------------------------------------------------------------------------------------------------------//

                                            linkProcessing(result, Layer + 1,Index1 ,Index2,counter, parentId);
                                            ResultSet resultSet=DataBaseObject.getUrls2(result);
                                            while (resultSet.next())
                                            {
                                                IncrementLimit();
                                            }
                                        }
                                        catch( Exception e)
                                        {

                                        }

                                    } else if (getLimit() >= 5000) {
                                        // query to set the layer and the index to 0 setThread Position
                                        DataBaseObject.setThreadPosition(Thread.currentThread().getName(), -1, 0);
                                        Thread.currentThread().interrupt();
                                        break;
                                    }
                                    else
                                    {
                                        flag=1;
                                    }
                                } else {
                                    FirstUrlLayer3--;
                                }
                            }
                            DataBaseObject.urlCompleted(Url);
                        }
                        catch (IOException e)
                        {

                        }

                    }
                    else
                    {
                        DataBaseObject.setThreadPosition(Thread.currentThread().getName(), Layer, Index3);
                        // query mark the current link as completed and it if it over layer 3
                        DataBaseObject.urlCompleted(Url);
                    }
    }
    //------------------------------------------------------------------------------------------------------------------//

}
