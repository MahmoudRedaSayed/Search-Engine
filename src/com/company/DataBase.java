package com.company;
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

    // The CURD operations
    // Creation to the link
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

    // Update
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



    public synchronized void setThreadPosition(String ThreadName,int Layer,int Index)
    {
        try{
           this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" and UrlIndex="+Index+" WHERE ThreadName='"+ThreadName+"';");
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }
    public synchronized ResultSet getThreadPosition(String ThreadName)
    {
        try{
            ResultSet Position=stmt.executeQuery("SELECT * FROM threads  where ThreadName='"+ThreadName+"'");

           return this.stmt.executeQuery("SELECT Layer,UrlIndex FROM threads WHERE ThreadName='"+ThreadName+"'");
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return null;
        }
    }
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

    public synchronized ResultSet getParentUrl (String ThreadName,int Layer,int Index)
    {
        try{
            System.out.println("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+";");
            ResultSet resultSet= this.stmt.executeQuery("SELECT * FROM links WHERE  ThreadName='"+ThreadName+"' AND Layer="+Layer+";");
            while(resultSet.next())
            {
                return this.stmt.executeQuery("SELECT Link , LinkParent FROM links WHERE Layer= "+Layer+" AND ThreadName='"+ThreadName+"' AND LinkParent="+resultSet.getInt("LinkParent")+";");
            }
            return null;
            }
        catch(SQLException e)
        {
            System.out.println(e);
            return null;

        }
    }
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
}
