package ServletsPackages.ServletPackage;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class QuerySearch extends HttpServlet {
    public String searchingQuery;
    public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
    {
        res.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        searchingQuery=req.getParameter("query");
        if(searchingQuery.startsWith("\"") && searchingQuery.endsWith("\""))
        {
            //call the function of the phrase searching
        }
        else
        {
            //call function of query processing
        }



    }
}
