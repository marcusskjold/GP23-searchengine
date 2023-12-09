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
 * Pages have a title, URL, a rank, a map of wordfrequenices and a number of terms.
 * @author Marcus Skjold, Andreas Riget Bagge, Sean Weston
 * @version 0.1
 */
public class Page implements Comparable<Page> {
    private String title;
    private String URL;
    private double pageRank;
    private Map<String, Integer> frequencyMap;
    private int totalTerms;

    /**
     * Creates a new Page with the specified information.
     * @param title the full title of the webpage
     * @param URL the full URL of the webpage, e.g. "https://google.com"
     * @param content the String content of the page, each word being a String
     */
    public Page(String title, String URL, List<String> content){
        this.title = title;
        this.URL = URL;
        this.frequencyMap = new HashMap<>();
        totalTerms = setUpFrequencyMap(content);
        
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
            if (lines.get(0).length()<6) throw new Exception("Failed Page creation: No URL");
            title = lines.get(1);
            frequencyMap = new HashMap<String,Integer>();
            URL = lines.get(0).substring(6); 
            List<String> content = lines.subList(2,lines.size());
            totalTerms = setUpFrequencyMap(content);
    }

    private int setUpFrequencyMap(List<String> content) {
        totalTerms = 0;
        if (content == null) return totalTerms;
        for (String word : content){
            if (word.isBlank()) continue;
            frequencyMap.merge(word, 1, Integer::sum);
            totalTerms++;
        }
        return totalTerms;
    }

    public String getTitle() { return title; }
    
    public String getURL() { return URL; }

    public double getPageRank(){return pageRank;}

    public void setRank(double rank) {
        pageRank = rank;
    }

    public int getFrequency(String word) {
       return frequencyMap.get(word)==null ? 0 : frequencyMap.get(word);
    }

    public Set<String> getWordSet(){
        return frequencyMap.keySet();
    }

    public int getTotalTerms(){return totalTerms;}

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

    public int compareTo(Page o){
        int rankComparison = Double.compare(o.pageRank, this.pageRank);
        if (rankComparison != 0){ return rankComparison;}
        return Integer.compare(this.hashCode(), o.hashCode());
    }
}
