package DataBasePackages.DataBase;

import IndexerPackages.Indexer.Indexer;

import java.sql.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataBase {
    private Connection connect;
    private Statement stmt;
    public DataBase()
    {
        try{
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
            catch(Exception e)
            {

            }
            connect=DriverManager.getConnection("jdbc:mysql://localhost:3306/search-engine","root","");
            this.stmt=connect.createStatement();
            if (connect != null) {
                System.out.println("Connected to database");
            } else {
                System.out.println("Cannot connect to database");
            }

        }
        catch(SQLException e)
        {

        }
    }

    //--------------------------------------Create Link --------------------------------------------------------------------//
    public synchronized void createLink(String Link,int Layer,String ThreadName,int ParentId)
    {
        try{
            this.stmt.executeUpdate("INSERT INTO links (Link, Layer, ThreadName, LinkParent,Completed) VALUES ('"+Link+"', '"+Layer+"', '"+ThreadName+"', "+ParentId+",'"+0+"');");
        }
        catch(SQLException e)
        {
        }
    }
//----------------------------------------------------------------------------------------------------------------------//

// --------------------------------------Update Link to Complete -------------------------------------------------------//

    public synchronized void urlCompleted(String Link)
    {
        try{
            this.stmt.executeUpdate("UPDATE links SET Completed=1 WHERE link='"+Link+"'");
        }
        catch(SQLException e)
        {
        }
    }
//----------------------------------------------------------------------------------------------------------------------//



// --------------------------------------Set and Get Thread Position -------------------------------------------------------//

    public synchronized void setThreadPosition(String ThreadName,int Layer,int Index)
    {
        try{
            if(Layer==1)
            {
                this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
                this.stmt.executeUpdate("UPDATE threads SET UrlIndex="+Index+" WHERE ThreadName='"+ThreadName+"';");

            }
            else if (Layer==2)
            {

                this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
                this.stmt.executeUpdate("UPDATE threads SET UrlIndex1="+Index+" WHERE ThreadName='"+ThreadName+"';");

            }
            else if (Layer==3)
            {


                this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
                this.stmt.executeUpdate("UPDATE threads SET UrlIndex2="+Index+" WHERE ThreadName='"+ThreadName+"';");
            }
            else if (Layer==4)
            {

                this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
                this.stmt.executeUpdate("UPDATE threads SET UrlIndex3="+Index+" WHERE ThreadName='"+ThreadName+"';");
            }
            else{
                this.stmt.executeUpdate("UPDATE threads SET Layer=1 WHERE ThreadName='"+ThreadName+"';");
                this.stmt.executeUpdate("UPDATE threads SET  UrlIndex=0 WHERE ThreadName='"+ThreadName+"';");
                this.stmt.executeUpdate("UPDATE threads SET  UrlIndex1=0  WHERE ThreadName='"+ThreadName+"';");
                this.stmt.executeUpdate("UPDATE threads SET  UrlIndex2=0 WHERE ThreadName='"+ThreadName+"';");
                this.stmt.executeUpdate("UPDATE threads SET   UrlIndex3=0 WHERE ThreadName='"+ThreadName+"';");



            }
        }
        catch(SQLException e)
        {
        }
    }

    public synchronized ResultSet getThreadPosition(String ThreadName)
    {
        try{
            ResultSet resultSet=this.stmt.executeQuery("SELECT * FROM threads WHERE ThreadName='"+ThreadName+"'");
            return resultSet;
        }
        catch(SQLException e)
        {
            return null;
        }
    }
//----------------------------------------------------------------------------------------------------------------------//

    public synchronized ResultSet getUrls(String Url)
    {
        try{
            return this.stmt.executeQuery("SELECT * FROM links WHERE Link='"+Url+"' AND Completed = 1");
        }
        catch(SQLException e)
        {
            return null;
        }
    }
    //---------------------------------------------get the url similar to the url-------------------------------------------//
    public synchronized ResultSet getUrls2(String Url)
    {
        try{
            return this.stmt.executeQuery("SELECT * FROM links WHERE Link='"+Url+"';");
        }
        catch(SQLException e)
        {
            return null;
        }
    }
// ---------------------------------------------------------------------------------------------------------------------//



// ---------------------------------------------------------------------------------------------------------------------//


// --------------------------------------get the id of the link  -------------------------------------------------------//

    public synchronized int getId (String Url,String ThreadName)
    {
        try{
            ResultSet resultSet=this.stmt.executeQuery("SELECT * FROM links WHERE Link='"+Url+"' AND ThreadName='"+ThreadName+"' AND Completed=0 ;");
            while (resultSet.next())
            {
                int Id=-1;
                Id=resultSet.getInt("Id");
                return  Id;
            }
        }
        catch(SQLException e)
        {

        }
        return -1;
    }
//----------------------------------------------------------------------------------------------------------------------//

    //-----------------------------------------get the family of the link --------------------------------------------------//
    public synchronized ResultSet getParentUrl (String ThreadName, StringBuffer grandLink )
    {
        try {
                ResultSet resultSet = this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='" + ThreadName + "' AND Layer=1 AND Completed=0;");
                while (resultSet.next()) {
                    grandLink.append(resultSet.getString("Link"));
                    return this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='" + ThreadName + "' AND Layer=1 AND Completed=0;");
                }
                //If the parent  link is completed
                Thread.currentThread().interrupt();
        }
        catch (SQLException e)
        {

        }

        return null;
    }
//----------------------------------------------------------------------------------------------------------------------//




    //------------------------------------------get the completed urls------------------------------------------------------//
    public synchronized int getCompleteCount ()
    {
        try
        {
            ResultSet result =this.stmt.executeQuery("SELECT count(Link) as Number FROM links WHERE  Completed=1 ;");
            int count=0;
            while(result.next())
            {
                count=result.getInt("Number");
            }
            return count;
        }
        catch(SQLException e)
        {
        }
        return 0;
    }
//----------------------------------------------------------------------------------------------------------------------//

    public java.sql.Date getMaxDate ()
    {
        try
        {
            ResultSet result =this.stmt.executeQuery("SELECT max(LastTime) as Time FROM links;");
            java.sql.Date count=null;
            while(result.next())
            {
                count = result.getDate("columnName");
            }
            return count;
        }
        catch(SQLException e)
        {
        }
        return null;
    }

    //---------------------------------------------get url and its related ID-------------------------------------------//
    public String[] getAllUrls()
    {
        int linksCount = getCompleteCount();
        String[] completedLinks = new String[linksCount];
        int i = 0;
        try{
            ResultSet rs = this.stmt.executeQuery("SELECT * FROM links where Completed = 1;" );
            while (rs.next())
            {
                completedLinks[i++] = rs.getString("Link");
            }
            return completedLinks;
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return completedLinks;
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------//
    //-----------------------------------------------get the number of links out from the parent link-----------------------//
    public int getOutGoingLinksNum(int id)
    {

        try{
            String qq= "SELECT Counter FROM links  where Id="+id+" ;";
            ResultSet resultSet=this.stmt.executeQuery(qq );
            while(resultSet.next())
            {
                return resultSet.getInt("Counter");
            }
        }
        catch(SQLException e)
        {
            return -1;
        }
        return -1;
    }
    // ---------------------------------------------------------------------------------------------------------------------//
    //--------------------------------------------------function to get the parent id----------------------------------------//
    public synchronized String getParentLink(String url)
    {
        try{
            ResultSet resultSet=this.stmt.executeQuery("SELECT LinkParent FROM links  where Link='"+url+"' ;" );
            while(resultSet.next())
            {
                int parentId=resultSet.getInt("LinkParent");
                ResultSet resultSet2 =this.stmt.executeQuery("SELECT * FROM links  where Id="+parentId+" ;" );
                while( resultSet2.next() )
                {
                    String linkParent = resultSet2.getString("Link");
                    return linkParent;
                }
                return "-1";
            }

        }
        catch(SQLException e)
        {
            return null;
        }
        return null;
    }
    //-----------------------------------------------------------------------------------------------------------------------//
    //-----------------------------------------------Add Link descripation--------------------------------------------------//
    public  synchronized void addDesc(int id,String desc)
    {
        try {
            this.stmt.executeUpdate("UPDATE links SET Descripation='" + desc + "' WHERE Id=" + id + ";");
        }
        catch(SQLException e)
        {

        }
    }
    // ---------------------------------------------------------------------------------------------------------------------//
    //------------------------------------------function to add paragraphs and headers and title and itemlists-------------//
    public synchronized void addElements(int id,String paragraphs,String title,String headers,String itemLists,String strong)
    {
       // System.out.printf("UPDATE links SET Paragraph='" + paragraphs + "' WHERE Id=" + id + ";");
        try {
            this.stmt.executeUpdate("UPDATE links SET Paragraph='" + paragraphs + "' WHERE Id=" + id + ";");
            this.stmt.executeUpdate("UPDATE links SET Title='" + title + "' WHERE Id=" + id + ";");
            this.stmt.executeUpdate("UPDATE links SET Headers='" + headers + "' WHERE Id=" + id + ";");
            this.stmt.executeUpdate("UPDATE links SET ListItems='" + itemLists + "' WHERE Id=" + id + ";");
            this.stmt.executeUpdate("UPDATE links SET Strong='" + strong + "' WHERE Id=" + id + ";");
        }
        catch (SQLException e)
        {

        }
    }
    //---------------------------------------------------------------------------------------------------------------------//
    //-----------------------------------------------get Link Content--------------------------------------------------//
    public synchronized String getContent(int id)
    {
        try {
//            System.out.println("SELECT CONCAT(Paragraph,Headers,Title,Strong,ListItems) as 'content' FROM `links` WHERE Id="+id+";");
            ResultSet resultSet=this.stmt.executeQuery("SELECT CONCAT(Paragraph,Headers,Title,Strong,ListItems) as 'content' FROM `links` WHERE Id="+id+";");
            while(resultSet.next())
            {
                return resultSet.getString("content");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        return "none";
    }
    // ---------------------------------------------------------------------------------------------------------------------//
    //------------------------------------------get Links Contents----------------------------------------------------------//
    public synchronized ResultSet getContents(String content , int id)
    {
        try {
            ResultSet resultSet=this.stmt.executeQuery("Select * From links as K ,links as J where CONCAT(K.Paragraph,K.Headers,K.Strong,K.ListItems)=CONCAT(J.Paragraph,J.Headers,J.Strong,J.ListItems) AND K.Id="+id+" AND K.Id!=J.Id;");
            while(resultSet.next())
            {
                this.stmt.executeUpdate("Delete from links where Id="+id+" AND LinkParent != -1;");
            }
            return null;
        }
        catch(SQLException e)
        {

        }
        return null;
    }
    //----------------------------------------------------------------------------------------------------------------------//

    // get the title of a website
    public synchronized String getTitle(String url)
    {
        try {
            String q = "Select Title From links where Link = '" + url + "'";
            ResultSet resultSet=this.stmt.executeQuery(q);
            while(resultSet.next())
            {
                return resultSet.getString("Title");
            }
        }
        catch(SQLException e)
        {

        }
        return null;
    }

    // get the Paragraphs of a website
    public synchronized String getParagraphs(String url)
    {
        try {
            ResultSet resultSet=this.stmt.executeQuery("Select Paragraph From links where Link = '" + url+ "'");
            while(resultSet.next())
            {
                return resultSet.getString("Paragraph");
            }
        }
        catch(SQLException e)
        {

        }
        return null;
    }

    // get the Description of a website
    public synchronized String getDescription(String url)
    {
        try {
            ResultSet resultSet=this.stmt.executeQuery("Select Lower(Descripation) as Descripation From links where Link = '" + url+ "'");
            while(resultSet.next())
            {
                return resultSet.getString("Descripation");
            }
        }
        catch(SQLException e)
        {

        }
        return null;
    }

    // get the Headers of a website
    public synchronized String getHeaders(String url)
    {
        try {
            ResultSet resultSet=this.stmt.executeQuery("Select Headers From links where Link = '" + url+ "'");
            while(resultSet.next())
            {
                return resultSet.getString("Headers");
            }
        }
        catch(SQLException e)
        {

        }
        return null;
    }
    // get the ListItems of a website
    public synchronized String getListItems(String url)
    {
        try {
            ResultSet resultSet=this.stmt.executeQuery("Select ListItems From links where Link = '" + url+ "'");
            while(resultSet.next())
            {
                return resultSet.getString("ListItems");
            }
        }
        catch(SQLException e)
        {

        }
        return null;
    }
    // get the Strongs of a website
    public synchronized String getStrongs(String url)
    {
        try {
            ResultSet resultSet=this.stmt.executeQuery("Select Strong From links where Link = '" + url+ "'");
            while(resultSet.next())
            {
                return resultSet.getString("Strong");
            }
        }
        catch(SQLException e)
        {

        }
        return null;
    }
    // Add Words Count of a website
    public synchronized void addWordsCount(String link, long count)
    {
        String query = "UPDATE links SET WordCounts = " + count + " WHERE Link = '" + link + "';";

        try {
            this.stmt.executeUpdate(query);
        }
        catch (SQLException e)
        {
            System.out.println("Error while adding the words count of the website : " + link);
        }
    }

    // get map of words count for all websites
    public Map<String, Long> getWordsCountAsMap()
    {
        Map<String, Long> resultMap = new HashMap<>();
        try {
            ResultSet resultSet = this.stmt.executeQuery("SELECT * FROM links;");

            while (resultSet.next())
            {
                resultMap.put(resultSet.getString("Link"), resultSet.getLong("wordCounts"));
            }
            return resultMap;

        } catch (SQLException e) {
            return null;
        }

    }


    // get map of words count for all websites
    public Map<String, Integer> getIDsAsMap()
    {
        Map<String, Integer> resultMap = new HashMap<>();
        try {
            ResultSet resultSet = this.stmt.executeQuery("SELECT Link, Id FROM links;");

            while (resultSet.next())
            {
                resultMap.put(resultSet.getString("Link"), resultSet.getInt("Id"));
            }
            return resultMap;

        } catch (SQLException e) {
            return null;
        }

    }

    // get the id of a link
    public synchronized int getID (String Url)
    {
        try{
            ResultSet resultSet=this.stmt.executeQuery("SELECT Id FROM links WHERE Link='"+Url+"';");
            while (resultSet.next())
            {
                int Id=-1;
                Id=resultSet.getInt("Id");
                return  Id;
            }
        }
        catch(SQLException e)
        {

        }
        return -1;
    }

    // get map of the needed links and its paragraphs
    public Map<String, String> getLinksParagraphs(ArrayList<String> links)
    {
        int size = links.size();
        Map<String, String> resultMap = new HashMap<>();
        try {
            String query = "SELECT Link, Paragraph FROM links where Link IN (";
            int i;
            for (i = 0; i < size - 1; i++)
                query += "'" + links.get(i) + "',";

            // last link
            query += "'" + links.get(i) + "');";

            ResultSet resultSet = this.stmt.executeQuery(query);

            while (resultSet.next())
            {
                resultMap.put(resultSet.getString("Link"), resultSet.getString("Paragraph"));
            }
            return resultMap;

        } catch (SQLException e) {
            return null;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addIdParent(String Url,int parentId)
    {
        try {
            ResultSet resultSet=this.stmt.executeQuery("Select from links where Link='"+Url+"';");
            while (resultSet.next())
            {
                String Counter=resultSet.getString("CounterIds");
                Counter=Counter+","+parentId;
                this.stmt.executeUpdate("Update links set CounterIds='"+Counter+"' where Url='"+Url+"';");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getLinknumParent(int id)
    {
        try {
            ResultSet resultSet=this.stmt.executeQuery("Select * from links where Id="+id+";");
            while (resultSet.next())
            {
                return resultSet.getString("CounterIds");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return " ";
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<Integer> getIds()
    {
        try {
            ResultSet resultSet=this.stmt.executeQuery("select Id from links where Completed=1");
            ArrayList<Integer> ids=new ArrayList<Integer>();
            while (resultSet.next())
            {
             ids.add(resultSet.getInt("Id"));
            }
            return ids;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return null;
    }

}
