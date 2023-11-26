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

    //Compares the title and URL fields when using the equals-method, as of now. (Doesn't include the content-fields, since they are objects themselves)
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((URL == null) ? 0 : URL.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Page other = (Page) obj;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (URL == null) {
            if (other.URL != null)
                return false;
        } else if (!URL.equals(other.URL))
            return false;
        return true;
    }

    
}
