package searchengine;

import java.util.List;

/** Represents a web page
 * Pages have a title, URL and some content
 * @author Marcus Skjold, Andreas Riget Bagge, Sean Weston
 * @version 0.1
 */

public class Page {
    private String title;
    private String URL;
    private List<String> content;

    /**
     * Creates a new Page with the specified information.
     * @param title the full title of the webpage
     * @param URL the full URL of the webpage, e.g. "https://google.com"
     * @param content the String content of the page, each word being a String
     */
    public Page(String title, String URL, List<String> content){
        this.title = title;
        this.URL = URL;
        this.content = content;
    }

    public List<String> getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }
    
    public String getURL() {
        return URL;
    }
}
