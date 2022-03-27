package com.company;
import com.mysql.cj.xdevapi.Result;

import java.sql.*;
import java.sql.ResultSet;
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
    public void createLink(String Link,int Layer,String ThreadName,int ParentId)
    {
        try{
               this.stmt.executeUpdate("INSERT INTO links (Link, Layer, ThreadName, LinkParent,Completed) VALUES ('"+Link+"', '"+Layer+"', '"+ThreadName+"', "+ParentId+",'"+0+"');");
            ResultSet resultSet=this.stmt.executeQuery("SELECT * FROM links WHERE Link='"+Link+"' AND ThreadName='"+ThreadName+"' AND Completed=0 ;");
            while (resultSet.next())
            {
                int Id=-1;
                Id=resultSet.getInt("Id");
                System.out.printf("%d the id of the link \n",Id);
            }
           }
        catch(SQLException e)
            {
                System.out.println(e);
            }
    }

    // Update
    public void urlCompleted(String Link)
    {
        try{
            this.stmt.executeUpdate("UPDATE links SET Completed=1 WHERE link='"+Link+"'");
            }
        catch(SQLException e)
            {
                System.out.println(e);
            }
    }



    public void setThreadPosition(String ThreadName,int Layer,int Index)
    {
        try{
           this.stmt.executeUpdate("UPDATE threads SET Layer="+Layer+" and UrlIndex="+Index+" WHERE ThreadName='"+ThreadName+"';");
            System.out.printf("Done");
            ResultSet res=this.stmt.executeQuery("SELECT * FROM threads WHERE ThreadName='"+ThreadName+"';");
            while (res.next())
            {
                System.out.printf("Layer %d index %d",res.getInt("Layer"),res.getInt("UrlIndex"));
            }
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }
    public ResultSet getThreadPosition(String ThreadName)
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
    public ResultSet getUrls(String Url)
    {
        try{
            System.out.printf("get urls function \n");
            return this.stmt.executeQuery("SELECT * FROM links WHERE Link='"+Url+"' AND Completed = 1");
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return null;
        }
    }

    public int getId (String Url,String ThreadName)
    {
        try{
            System.out.printf("the link from the function %s \n",Url);
            ResultSet resultSet=this.stmt.executeQuery("SELECT * FROM links WHERE Link='"+Url+"' AND ThreadName='"+ThreadName+"' AND Completed=0 ;");
            while (resultSet.next())
            {
                int Id=-1;
                Id=resultSet.getInt("Id");
                System.out.printf("%d the id of the link \n",Id);
                return  Id;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e);

        }
        return -1;
    }

    public ResultSet getParentUrl (String ThreadName,int Layer,int Index)
    {
        try{

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
    public int getCompleteCount ()
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
}
