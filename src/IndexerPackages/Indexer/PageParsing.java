package IndexerPackages.Indexer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class PageParsing {

    private Document webPage;
    private Elements elements;

    // get the whole structure of the web page
    public void parseDocument(String url) throws IOException {
        webPage = Jsoup.connect(url).get();
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

}
