package IndexerPackages.Indexer;

import HelpersPackages.Helpers.HelperClass;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import java.io.IOException;
import java.util.Scanner;

public class PageParsing {

    private Document webPage;
    private Elements elements;

    // get the whole structure of the web page
//    public void parseDocument(String url) throws IOException {
//        webPage = Jsoup.connect(url).get();
//    }
    public PageParsing(Document Doc) throws  IOException
    {
        webPage = Doc;

    }
    // get the title of a page
    public String getTitleTag()
    {
        return webPage.title().toLowerCase();
    }

    // get text ( paragraphs ) of the page
    public String[] getParagraphs()
    {
        elements = webPage.select("p");  // get all paragraphs in the web page
        int count = elements.size(),
                i = 0;
        String[] data = new String[count];

        // fill the data
        for(Element item : elements)
            data[i++] = item.text().toLowerCase();

        return data;
    }
    //get length of page content
    /*public static long getLengthOfPageContent(int pageId)
    {
        String path= HelperClass.pageContentFilesPath(String.valueOf(pageId));
        File target = new File(path);
        String content = "";
        Scanner reader = null;
        try {
            reader = new Scanner(target);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(reader.hasNextLine())
        {
            content+= reader.nextLine();
        }



        return content.length();
    }*/


    // get headings
    public String[] getHeaders()
    {
        elements = webPage.select("h1, h2, h3");  // get all paragraphs in the web page
        int count = elements.size(),
                i = 0;
        String[] data = new String[count];

        // fill the data
        for(Element item : elements)
            data[i++] = item.text().toLowerCase();

        return data;
    }

    // get <strong> --> the same priority of the h1, h2
    public String[] getStrongs()
    {
        elements = webPage.select("strong");  // get all paragraphs in the web page
        int count = elements.size(),
                i = 0;
        String[] data = new String[count];

        // fill the data
        for(Element item : elements)
            data[i++] = item.text().toLowerCase();

        return data;
    }

    // get list items ( for ordered list & unordered list )
    public String[] getListItems()
    {
        elements = webPage.select("li");  // get all paragraphs in the web page
        int count = elements.size(),
                i = 0;
        String[] data = new String[count];

        // fill the data
        for(Element item : elements)
            data[i++] = item.text().toLowerCase();

        return data;
    }

    // get the content of a table
    public String[] getTableData()
    {
        elements = webPage.select("td");  // get all paragraphs in the web page
        int count = elements.size(),
                i = 0;
        String[] data = new String[count];

        // fill the data
        for(Element item : elements)
            data[i++] = item.text().toLowerCase();

        return data;
    }

    public String getAllContentsAsSingleString()
    {
        String result       = "";
        String title        = this.getTitleTag();
        String[] headers    = this.getHeaders();
        String[] paragraphs = this.getParagraphs();
        String[] ListItems  = this.getListItems();

        // concat the title
        result += title;

        // concat the headers
        for (String head : headers)
        {
            result += " " + head;
        }

        // concat the paragraphs
        for (String p : paragraphs)
        {
            result += " " + p;
        }

        // concat the Li
        for (String li : ListItems)
        {
            result += " " + li;
        }


        return result;
    }

}
