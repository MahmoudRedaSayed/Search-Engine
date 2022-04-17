package DataBasePackages.DataBase;
import java.time.format.DateTimeFormatter;

import com.mysql.cj.xdevapi.Result;

import java.sql.*;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
                            System.out.println(e);
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
                { System.out.println(e);}
    }

//--------------------------------------Create Link --------------------------------------------------------------------//
    public synchronized void createLink(String Link,int Layer,String ThreadName,int ParentId)
    {
        try{
               this.stmt.executeUpdate("INSERT INTO links (Link, Layer, ThreadName, LinkParent,Completed) VALUES ('"+Link+"', '"+Layer+"', '"+ThreadName+"', "+ParentId+",'"+0+"');");
           }
        catch(SQLException e)
            {
                System.out.println(e);
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
                System.out.println(e);
            }
    }
//----------------------------------------------------------------------------------------------------------------------//


// --------------------------------------Set and Get Thread Position -------------------------------------------------------//

    public synchronized void setThreadPosition(String ThreadName,int Layer,int Index)
    {
        try{
            if(Layer==1)
            {
                System.out.printf("UPDATE threads SET Layer="+Layer+" and UrlIndex="+Index+" WHERE ThreadName='"+ThreadName+"';");
                this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
                this.stmt.executeUpdate("UPDATE threads SET UrlIndex="+Index+" WHERE ThreadName='"+ThreadName+"';");

            }
            else if (Layer==2)
            {
                System.out.printf("UPDATE threads SET Layer="+Layer+" and UrlIndex1="+Index+" WHERE ThreadName='"+ThreadName+"';");

                this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
                this.stmt.executeUpdate("UPDATE threads SET UrlIndex1="+Index+" WHERE ThreadName='"+ThreadName+"';");

            }
            else if (Layer==3)
            {
                System.out.printf("UPDATE threads SET Layer="+Layer+" and UrlIndex2="+Index+" WHERE ThreadName='"+ThreadName+"';");

                this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
                this.stmt.executeUpdate("UPDATE threads SET UrlIndex2="+Index+" WHERE ThreadName='"+ThreadName+"';");
            }
            else if (Layer==4)
            {
                this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" WHERE ThreadName='"+ThreadName+"';");
                this.stmt.executeUpdate("UPDATE threads SET UrlIndex3="+Index+" WHERE ThreadName='"+ThreadName+"';");
            }
            else{
                this.stmt.executeUpdate("UPDATE threads SET Layer=1 and UrlIndex=0 and UrlIndex1=0 and UrlIndex2=0 UrlIndex3=0 WHERE ThreadName='"+ThreadName+"';");

            }
        }
        catch(SQLException e)
        {
            System.out.println(e);
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
            System.out.println(e);
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
            System.out.println(e);
            return null;
        }
    }
//---------------------------------------------get the url similar to the url-------------------------------------------//
    public synchronized ResultSet getUrls2(String Url)
    {
        try{
            return this.stmt.executeQuery("SELECT * FROM links WHERE Link='"+Url+"'");
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return null;
        }
    }
// ---------------------------------------------------------------------------------------------------------------------//


//---------------------------------------get link by ID  -------------------------------------------------------------//
public synchronized String getLinkByID (Integer ID)
{
    try{
        //String query = "Select Link FROM links WHERE Id= " + ID +" ";
        String query = "Select * FROM links";
        ResultSet resultSet = this.stmt.executeQuery("Select Link FROM links WHERE Id= " + ID +";");
        resultSet.next();
        String result = resultSet.getString("Link");
        return result;

    } catch (SQLException e) {
        e.printStackTrace();
        return null;
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
            System.out.println(e);

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
                System.out.printf("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+"AND Completed=0;");
                ResultSet resultSet= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+";");
                while(resultSet.next())
                {
                    grandLink.append(resultSet.getString("Link"));
                }
                return this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+";");
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
            }
            else if (Layer==3)
            {
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
            }
        }
        catch(SQLException e)
        {
            System.out.println(e);
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
            System.out.println(e);
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
            System.out.println(e);
        }
        return null;
    }

    //---------------------------------------------get url and its related ID-------------------------------------------//
    public ResultSet getAllUrls()
    {
        try{
            return this.stmt.executeQuery("SELECT LINK, ID FROM links;" );
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return null;
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------//
}
