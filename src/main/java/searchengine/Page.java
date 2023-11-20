package searchengine;

import java.util.List;


public class Page {
    private String title;
    private String URL;
    private List<String> content;

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
