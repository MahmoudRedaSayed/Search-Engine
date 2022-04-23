package DataBasePackages.DataBase;

import java.sql.*;
import java.sql.ResultSet;

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

    // --------------------------------------Update Link to Complete -------------------------------------------------------//

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


//---------------------------------------get link by ID  -------------------------------------------------------------//
public synchronized Boolean getDescription (String linkUrl, StringBuffer description)
{
    try{
        //String query = "Select Link FROM links WHERE Id= " + ID +" ";
        String query = "Select * FROM links";
        ResultSet resultSet = this.stmt.executeQuery("Select Descripation FROM links WHERE Link= '" + linkUrl +"';");
        resultSet.next();
        String descriptionResult = resultSet.getString("Descripation");
        description.append(descriptionResult);
        return true;

    } catch (SQLException e) {
        return false;
    }

}



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
    public synchronized ResultSet getParentUrl (String ThreadName,StringBuffer parentLink , StringBuffer grandLink , String link,int Layer)
    {
        try{
            if(Layer==1)
            {
                ResultSet resultSet= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=0;");
                System.out.printf("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=0;");
                while(resultSet.next())
                {
                    grandLink.append(resultSet.getString("Link"));
                    return this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=0;");
                }

//                ResultSet resultSet2= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=1;");
//                while(resultSet2.next())
//                {
//                    grandLink.append(resultSet2.getString("Link"));
//                    return this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=1;");
//                }
                //If the parent  link is completed
                Thread.currentThread().interrupt();
            }
            else if(Layer==2)
            {
                ResultSet resultSet= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=0;");
                while(resultSet.next())
                {
                    resultSet=this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
                    while(resultSet.next())
                    {
                        parentLink.append(resultSet.getString("Link"));
                        return this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
                    }

                }
                //If the parent  link is completed
                Thread.currentThread().interrupt();
            }
            else if (Layer==3||Layer-1==3)
            {
                Layer-=1;
                ResultSet resultSet= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+" AND Completed=0;");
                while(resultSet.next())
                {
                    resultSet =this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
                    while(resultSet.next())
                    {
                        parentLink.append(resultSet.getString("Link"));
                        Layer=resultSet.getInt("Layer");
                        resultSet =this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
                        while(resultSet.next())
                        {
                            grandLink.append(resultSet.getString("Link"));
                            return this.stmt.executeQuery("SELECT  k.Link  , k.LinkParent , k.Layer FROM links as e , links as k WHERE e.Layer= "+Layer+" AND e.ThreadName='"+ThreadName+"' AND k.Id=e.LinkParent;");
                        }

                    }


                }
                //If the parent  link is completed
                Thread.currentThread().interrupt();
            }
        }
        catch(SQLException e)
        {
            return null;

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
    public ResultSet getAllUrls()
    {
        try{
            return this.stmt.executeQuery("SELECT Link, Id FROM links where Completed=1;" );
        }
        catch(SQLException e)
        {
            return null;
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------//
    //-----------------------------------------------get the number of links out from the parent link-----------------------//
    public int getParentLinksNum(int childId)
    {

        try{
             ResultSet resultSet=this.stmt.executeQuery("SELECT LinkParent FROM links  where Id="+childId+" ;" );
            while(resultSet.next())
            {
                int parentId=resultSet.getInt("LinkParent");
                return this.stmt.executeQuery("SELECT count(Id) as Number FROM links  where LinkParent="+parentId+" ;" ).getInt("Number");
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
    public int getParentId(int childId)
    {
        try{
            ResultSet resultSet=this.stmt.executeQuery("SELECT LinkParent FROM links  where Id="+childId+" ;" );
            while(resultSet.next())
            {
                int parentId=resultSet.getInt("LinkParent");
                return parentId;
            }
        }
        catch(SQLException e)
        {
            return -1;
        }
        return -1;
    }
    //-----------------------------------------------------------------------------------------------------------------------//
    //-----------------------------------------------Add Link descripation--------------------------------------------------//
    public void addDesc(int id,String desc)
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
    public void addElements(int id,String paragraphs,String title,String headers,String itemLists,String strong)
    {
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
    public String getContent(int id)
    {
        try {
            System.out.println("SELECT CONCAT(Paragraph,Headers,Title,Strong,ListItems) as 'content' FROM `links` WHERE Id="+id+";");
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
    public ResultSet getContents(String content)
    {
        try {
           ResultSet resultSet=this.stmt.executeQuery("Select * From links where CONCAT(Paragraph,Headers,Title,Strong,ListItems)='"+content+"';");
           return resultSet;
        }
        catch(SQLException e)
        {

        }
        return null;
    }
    //----------------------------------------------------------------------------------------------------------------------//


    //////////////////////////////////////////////// Mustafa /////////////////////////////
    public String getTitle(String url)
    {
        try {
            ResultSet resultSet=this.stmt.executeQuery("Select Title From links where Link = '" + url + "'");
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

    public String getParagraphs(String url)
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

    public String getHeaders(String url)
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

    public String getListItems(String url)
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

    public String getStrongs(String url)
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////



}
