package searchengine;

import java.util.HashSet;
import java.util.Set;

/** Matches a query with a database.
 * @author
 * @version
 * 
 */
public class QueryMatcher {
    private static Database database;

    /** This method sets the database for query matching.
     * @param database
     */

    public static void setDatabase(Database database){
        QueryMatcher.database = database;
    }

    /** This method is designed to match a given query with the database and returns a set of pages that match the query.
     * @param q
     * @return set<Page> which is a HashSet.
     */
    public static Set<Page> matchQuery(Query q){
        Set<Page> results = new HashSet<>();
        for (Set<String> ANDSet : q.getORSet()){
            results.addAll(matchANDSet(ANDSet));
        }
        if (q.getURLFilter() != ""){
            results.removeIf(n -> !n.getURL().contains(q.getURLFilter()));
        }
        return results;
    }

    /** This method matches a set of words (ANDSet) with the database and returns pages that contain all the words.
     * @param ANDSet
     * @return The method returns a set of Page objects that match the query.
     */

    private static Set<Page> matchANDSet(Set<String> ANDSet) {
        Set<Page> result = new HashSet<>();
        boolean firstWord = true;
        for (String word : ANDSet){
            if (firstWord == true) result = matchWord(word);
            else result.retainAll(matchWord(word));
            if ((result.isEmpty() )) break;
            firstWord = false;
        } return result;
    }

    /** This method matches a single word with the database and returns pages that contain the word.
     * @param word
     * @return a set of pages which contain the search word. 
     */

    public static Set<Page> matchWord(String word) {
        Set<Page> match = database.getPages(word);
        return match == null ? new HashSet<>() : match;
    }

}
