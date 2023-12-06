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

    /** Creates a new Page from part of a list of String-objects, 
     * the URL must be at the first line, preceded by "*PAGE:"
     * the title as the next line 
     * and each word of the content of the webpages as a separate line.
     * @param lines the List of String-objects to convert from.
     * @return a Page-object corresponding to the lines read.
     */
    public Page(List<String> lines) {
            title = lines.get(1);
            URL = lines.get(0).substring(6); //Will throw error if no URL is listed after Page as of right now?
            content = lines.subList(2,lines.size());
            content.removeIf(s -> s.isBlank());
    }

    public List<String> getContent() { return content; }

    public String getTitle() { return title; }
    
    public String getURL() { return URL; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        if (URL == null) {
            if (other.URL != null)
                return false;
        } else if (!URL.equals(other.URL))
            return false;
        return true;
    }

    
}
