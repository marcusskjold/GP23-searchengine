package searchengine;

import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;
import java.util.HashSet;

/** This class handles queries from a web server. <p>
 * The QueryHandler treats "%20" as the symbol for whitespace.
 * The QueryHandler splits a string into substrings separated by "OR"
 * and then those substrings into individual words.
 */
public class QueryHandler{

    /** Searches the database with the given search string and returns a set of pages sorted by rank.
     * The search string is split and processed to match against the database entries. 
     * @param searchString A string of search terms separated by "%20" or "OR".
     * @return a sorted Set of Page objects, ranked based on relevance to the search string.
     */
    public static Set<Page> search(String searchString) {
        Query q = new Query(splitSearchString(searchString));
        Set<Page> result = QueryMatcher.matchQuery(q);
        Set<Page> l = new TreeSet<Page>();
        for (Page page:result){
            PageRanker.rankPage(page, q);
            l.add(page);
        }
        return l;
    }

    /** Splits the input search string into a set of string sets based on OR. 
     * Each set represents a part of the query that is processed independently.
     * As an example, a searchString 'denmark OR sweden' would be 
     * split into two sets containing 'denmark' and 'sweden'.
     * @param searchString the search string to be split.
     * @return a set conatining sets representing a part of the query
     */
    private static Set<Set<String>> splitSearchString(String searchString) {
        Set<Set<String>> returnSets = new HashSet<>();
        String[] s = searchString.split("^OR%20|%20OR%20|%20OR$");
        for (String string : s) returnSets.add(splitString(string));
        return returnSets;
    }

    /** Splits a given string into a set of strings. <p>
     * Splits a given search string into a set of individual terms.
     * Splits by "%20" as that is the symbol for whitespace that the web server passes.
     * The set is case insensitive. Empty strings are removed.
     * @param searchString the search string which is being split
     * @return a set containing the individual nonempty search terms from the input string.
     */
    private static Set<String> splitString(String searchString) {
        searchString = searchString.toLowerCase(); 
        Set<String> returnSets = new HashSet<>();
        Collections.addAll(returnSets, searchString.split("(%20)++"));
        returnSets.removeIf(s -> (s.isBlank()));
        return returnSets;
    }
}
