package searchengine;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/** Represents a web page
 * Pages have a title, URL and some content
 * @author Marcus Skjold, Andreas Riget Bagge, Sean Weston
 * @version 0.1
 */
public class Page implements Comparable<Page> {
    private String title;
    private String URL;
    // private List<String> content;
    private double pageRank;
    private Map<String, Integer> wordMap;
    private int totalTerms;

    public static Page newPage(List<String> lines){
        try{
            return new Page(lines);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * Creates a new Page with the specified information.
     * @param title the full title of the webpage
     * @param URL the full URL of the webpage, e.g. "https://google.com"
     * @param content the String content of the page, each word being a String
     */
    public Page(String title, String URL, List<String> content){
        this.title = title;
        this.URL = URL;
        // this.content = content;
        this.wordMap = null; // TODO implement
        this.totalTerms = content.size(); // TODO remove empty lines
    }

    /** Creates a new Page from part of a list of String-objects, 
     * the URL must be at the first line, preceded by "*PAGE:"
     * the title as the next line 
     * and each word of the content of the webpages as a separate line.
     * @param lines the List of String-objects to convert from.
     * @return a Page-object corresponding to the lines read.
     */
    public Page(List<String> lines) throws Exception{
            if (lines.size()<=2) throw new Exception("Failed Page creation: Entry has no content");
            title = lines.get(1);
            totalTerms = 0;
            wordMap = new HashMap<String,Integer>();
            URL = lines.get(0).substring(6); 
            //Will throw error if no URL is listed after Page as of right now?
            // content = lines.subList(2,);
            // content.removeIf(s -> s.isBlank());
            List<String> content = lines.subList(2,lines.size());
            for (String word : content){
                if (word.isBlank()) continue;
                // wordMap.putIfAbsent(word, 0);
                // wordMap.put(word, wordMap.get(word)+1);
                wordMap.merge(word, 1, Integer::sum);
                totalTerms++;
            }
    }

    public double getPageRank(){
        return pageRank;
    }

    public int getTotalTerms(){
        return totalTerms;
    }

    public Set<String> getWordSet(){
        return wordMap.keySet();
    }

    public void rank(Query q){
        pageRank = PageRanker.rankPage(this, q);
    }

    // public List<String> getContent() { return content; }

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

    public int getFrequency(String word) {
       return wordMap.get(word)==null ? 0 : wordMap.get(word);
    }

    public int compareTo(Page o){
        int rankComparison = Double.compare(o.pageRank, this.pageRank);
        if (rankComparison != 0){ return rankComparison;}
        return Integer.compare(this.hashCode(), o.hashCode());
    }
}
