package searchengine;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.stream.Stream;

/** Represents a web page
 * Pages have a title, URL, a rank, a mapping of word to their frequencies in the page, and a number of terms.
 * Implements the Comparable interface since Page-objects order will be determined by their pagerank.
 * @author Marcus Skjold, Andreas Riget Bagge, Sean Weston
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
    public Page(String title, String URL, List<String> content) throws Exception{
        this(Stream.concat((List.of(URL, title)).stream(), content.stream())
                   .toList());
        // this.title = title;
        // this.URL = URL;
        // this.frequencyMap = new HashMap<>();
        // totalTerms = setUpFrequencyMap(content);
        
    }

    /** Creates a new Page object from part of a list of String objects.
     * <p> 
     * the URL must be at the first line, preceded by "*PAGE:"
     * the title as the next line 
     * and each word of the content of the webpages as a separate line.
     * The Page object then initialize its fields from these lines.
     * <p>
     * The words of the lines preceding the title and URL will be stored in a frequency-Map, which maps a given word
     * to the number of times, it is present among the lines.
     * @param lines the List of String-objects to convert from.
     * @return a Page-object corresponding to the lines read.
     * @throws Exception either if no words and/or title is present in the listed lines, or if
     * no URL is provided after "*PAGE:"
     */
    public Page(List<String> lines) throws Exception{
            if (lines.size()<=2) throw new Exception("Failed Page creation: Entry has no content");
            if (lines.get(0).length()<7) throw new Exception("Failed Page creation: No URL");
            title = lines.get(1);
            frequencyMap = new HashMap<String,Integer>();
            URL = lines.get(0).substring(6); 
            List<String> content = lines.subList(2,lines.size());
            if (content.isEmpty()) throw new Exception("Failed Page creation: Entry has no content");
            totalTerms = setUpFrequencyMap(content);
    }

    public void setRank(double rank) { pageRank = rank; }

    public String getTitle() { return title; }
    
    public String getURL() { return URL; }

    public double getPageRank() { return pageRank; }

    public int getTotalTerms() { return totalTerms; }

    /** Gets the number of times a given word is present in the Page. 
     * @param word the word to find the frequency for
     * @return the number of times the word is present in the Page.
     */
    public int getFrequency(String word) {
       return frequencyMap.get(word)==null ? 0 : frequencyMap.get(word);
    }

    /** Gets the different words present in the Page. 
     * @return a Set of words present in the Page.
     */
    public Set<String> getWordSet(){
        return frequencyMap.keySet();
    }

    /** Compares this Page to another object for their order. The order is calculated depending on the pageRank of the two objects. If the
     * two objects have equal ranks or if their Overrides the equals
     * method of the Object-class.
     * 
     * @return an integer, representing the Page-object.
     */
    public int compareTo(Page o){
        int rankComparison = Double.compare(o.pageRank, this.pageRank);
        if (rankComparison != 0){ return rankComparison;}
        return Integer.compare(this.hashCode(), o.hashCode());
    }

    /** Returns the Hashcode of the Page-object. The HashCode is generated depending 
     * on the URL-field of the Page-object. Overrides the hashCode
     * method of the Object-class.
     * @return an integer, representing the Page-object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((URL == null) ? 0 : URL.hashCode());
        return result;
    }

    /** Compares this Page to another object. Returns true if the other object is a non-null Page-object with
     * the same URL as this Page. Overrides the equals
     * method of the Object-class.
     * 
     * @return an integer, representing the Page-object.
     */
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

}
