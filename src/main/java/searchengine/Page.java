package searchengine;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.stream.Stream;

/** Represents a web page
 * Pages have a title, URL, a rank, a mapping of words to their frequencies in the page.
 * Pagerank must be set and updated by the PageRanker class. 
 * This behavior also means that the pageRank is not considered when evaluating equals or hashcode,
 * as pageRank should only be used as a set of pages are evaluated.
 * Otherwise identical Pages will generate identical pageRank if they are ranked simultaneously
 * But if not, they may have different ranks while being identical in practice.
 * Is comparable by pageRank and hashcode.
 */
public class Page implements Comparable<Page> {
    private String title;
    private String URL;
    private double pageRank;
    private Map<String, Integer> frequencyMap;
    private int totalTerms;

    /** Creates a new Page with the specified information.
     * Used for testing purposes.
     * @param title the full title of the webpage
     * @param URL the full URL of the webpage, e.g. "https://google.com"
     * @param content the String content of the page, each word being a String
     */
    protected Page(String title, String URL, List<String> content) throws Exception{
        this(Stream.concat((List.of("*PAGE:" +URL, title)).stream(), content.stream())
                   .toList());
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

    public void setRank(double pageRank) { this.pageRank = pageRank; }

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

    /** Compares this Page to another Page 
     * Overrides the equals method of the Object class.
     * The order is calculated depending on the pageRank of the two objects. 
     * Comparison will only return 0 if the Pages are equal and currently have the same pageRank.
     * This is to ensure consistent behavior with collections such as TreeSet.
     * If pageRank is equal, comparison is made by hashcode.<p>
     * Results are made in reverse: If this Page has a higher pageRank
     * the comparison returns a values less than 0.
     * This is to have the page with the highest pageRank be first 
     * in a collection sorted by natural order.
     * 
     * @return 0 if the objects are equal and have the same pageRank.
     * A value less than zero if this object has a righer pageRank than the other.
     * A value over zero if this object has a lower pageRank than the other.
     */
    public int compareTo(Page o){
        int rankComparison = Double.compare(o.pageRank, this.pageRank);
        if (rankComparison != 0){ return rankComparison;}
        return Integer.compare(this.hashCode(), o.hashCode());
    }

    /** Returns the Hashcode of the Page-object. 
     * The HashCode is generated depending on the URL-field of the Page-object. 
     * Overrides the hashCode method of the Object-class.
     * @return the hashcode value of this Page.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((URL == null) ? 0 : URL.hashCode());
        return result;
    }

    /** Compares this Page to another object. 
     * Returns true if the other object is a non-null Page-object with the 
     * same URL as this Page. Overrides the equals method of the Object-class.
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
