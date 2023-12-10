package searchengine;

import java.util.Set;
import java.util.HashSet;

/** The query class represents a query in a search engine.
 * It allows the construction of complex queries using sets of strings.
 */
public class Query {

    /** 
     * orSet represents a set of OR conditions for the search query. 
     * URLFilter stores a filter for URLs.
     * URLFilter is currently not used by the application.
     */
    private Set<Set<String>> orSet;
    private String URLFilter;

    /** Constructs a new Query object through a set of sets representing OR conditions.
     * Each set within the main set represents an AND condition for search terms.
     * <p>
     * Query constructor makes no check for correctness, so it is the 
     * responsibility of the those creating the Query to ensure that correct data is passed to it.
     * @param query The set of sets of strings representing the search terms. 
     */
    public Query(Set<Set<String>> query){
        orSet = query;
        URLFilter = "";
    }

    /** Constructs a Query object with a single search term.
     * Used for testing purposes.
     * @param searchTerm the single word term to be searched for
     */
    protected Query(String searchTerm){
        Set<String> andSet = new HashSet<String>();
        andSet.add(searchTerm);
        orSet = new HashSet<Set<String>>();
        orSet.add(andSet);
        URLFilter = "";
    }

    /** Retrieves the OR condition(s) of the query. 
     * @return the full query expressed as an "OR" set of "AND" conditions. 
     */
    public Set<Set<String>> getORSet() { return orSet; }

    /** Retrieves the URLFilter of the query. 
     * Currently unused by the application.
     * @return the method returns the URL filter String. 
     */

    public String getURLFilter() { return URLFilter; }

    /** This method sets a filter for the URLs to be included in the search results.
     * Currently unused by the application.
     * @param URLsegment The string to filter URLs.
     */

    public void setURLFilter(String URLsegment) { URLFilter = URLsegment; }

}
