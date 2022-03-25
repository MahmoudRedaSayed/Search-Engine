package com.company;
import com.mysql.cj.xdevapi.Result;

import java.sql.*;

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
                            System.out.printf("Constructor\n");
                            System.out.println(e);
                        }
                    connect=DriverManager.getConnection("jdbc:mysql://localhost:3306/search-engine","root","");
                    stmt=connect.createStatement();
                    ResultSet rs=stmt.executeQuery("select * from links");
                    if (connect != null) {
                        System.out.println("Connected to database");
                    } else {
                        System.out.println("Cannot connect to database");
                    }

                    while(rs.next())
                        System.out.println(rs.getString("Link")+"  "+rs.getInt("Id")+"  "+rs.getString("Layer"));
                    connect.close();
                }catch(SQLException e)
                { System.out.println(e);}
    }

    // The CURD operations
    // Creation to the link
    public void createLink(String Link,int Layer,String ThreadName,int ParentId)
    {
         //connect=DriverManager.getConnection("jdbc:mysql://localhost:3306/search-engine","root","");
        try{
                stmt.executeQuery("INSERT INTO `links` (`Link`, `Layer`, `ThreadName`, `LinkParent`,'Completed') VALUES ('"+Link+"', '"+Layer+"', '"+ThreadName+"', "+ParentId+",'"+0+"')");
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
            stmt.executeQuery("UPDATE 'links' SET Completed=1 WHERE link='"+Link+"'");
            }
        catch(SQLException e)
            {
                System.out.println(e);
            }
    }



    public void setThreadPosition(String ThreadName,int Layer,int Index)
    {
        try{
            stmt.executeQuery("UPDATE 'threads' SET Layer="+Layer+" and Index="+Index+" WHERE ThreadName='"+ThreadName+"'");
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }
    public ResultSet getThreadPosition(String ThreadName)
    {
        try{
            return stmt.executeQuery("SELECT 'Layer','UrlIndex' FROM 'threads' WHERE ThreadName='"+ThreadName+"'");
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
            return stmt.executeQuery("SELECT * FROM 'links' WHERE Link='"+Url+"' AND Completed = 1");
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getId (String Url,String ThreadName)
    {
        try{
            return stmt.executeQuery("SELECT 'Id' FROM 'links' WHERE Link='"+Url+"' AND ThreadName='"+ThreadName+"' AND Completed=0");
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet getParentUrl (String ThreadName,int Layer,int Index)
    {
        try{
           int Url = stmt.executeQuery("SELECT LinkParent FROM 'links' WHERE Layer= "+Layer+" AND ThreadName='"+ThreadName+"'").getInt("LinkParent");
           return stmt.executeQuery("SELECT Link , ParentId FROM 'links' WHERE Layer= "+Layer+" AND ThreadName='"+ThreadName+"'");
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
            return stmt.executeQuery("SELECT count(Link) as 'Number' FROM 'links' WHERE  Complete=1").getInt("Number");
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }
}
