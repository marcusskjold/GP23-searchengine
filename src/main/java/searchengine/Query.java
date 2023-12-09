package searchengine;

import java.util.Set;
import java.util.HashSet;

/**
 * The query class represents a query in a search engine.It allows the construction of complex queries using sets of strings. 
 * @author 
 * @version
 */

public class Query {

    /**
     * The field private Set<Set<String>> orSet represents a set of OR conditions for the search query. 
     * The field private String URLFilter stores a filter for URLs. 
     */
    private Set<Set<String>> orSet;
    private String URLFilter;

    /**
     * This constructor constructs a Query object through a set of sets representing OR conditions.
     * Each set within the main set represents an AND condition for search terms.
     * @param query The set of sets of strings representing the search terms. 
     */

    public Query(Set<Set<String>> query){
        orSet = query;
        URLFilter = "";
    }

    /**
     * Constructs a Query object with a single search term.
     * This term is used to create a basic AND condition.
     * @param searchTerm
     */
    public Query(String searchTerm){
        Set<String> andSet = new HashSet<String>();
        andSet.add(searchTerm);
        orSet = new HashSet<Set<String>>();
        orSet.add(andSet);
    }

    /**
     * The method retrieves the OR condition(s) of the query. 
     * @return the method returns a set of sets of Strings, which is representative of the OR condition. 
     */
    public Set<Set<String>> getORSet() { return orSet; }

    /**
     * This method retrieves the URLFilter of the query. 
     * @return the method returns the URL filter String. 
     */

    public String getURLFilter() { return URLFilter; }

    /**
     * This method sets a filter for the URLs to be included in the search results.
     * @param URLsegment The string to filter URLs.
     */

    public void setURLFilter(String URLsegment) { URLFilter = URLsegment; }

}
