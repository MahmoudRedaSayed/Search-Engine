package com.company;
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
            this.stmt.executeQuery("INSERT INTO `links` (`Link`, `Layer`, `ThreadName`, `LinkParent`,'Completed') VALUES ('"+Link+"', '"+Layer+"', '"+ThreadName+"', "+ParentId+",'"+0+"')");
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

    public void urlSearch(String Link)
    {
        try{
            stmt.executeQuery("SELECT * FROM 'links'  WHERE link='"+Link+"'");
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }


    public void setThreadPosition(String ThreadName,int Layer,int Index)
    {
        try{
            stmt.executeQuery("UPDATE 'Threads' SET Layer="+Layer+" and Index="+Index+" WHERE ThreadName='"+ThreadName+"'");
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }
}
