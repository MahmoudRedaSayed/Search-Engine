
package ServletsPackages.ServletPackage;

import org.json.JSONArray;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.ArrayList;

public class QueryDivide extends HttpServlet {
    public String results;
    public void setDivided(String Query)
    {
        results=Query;
    }
    public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
    {
        res.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        res.setContentType("application/json");
        res.getWriter().write("Mahmoud");
    }

}





//
