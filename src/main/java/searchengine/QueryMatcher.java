package searchengine;

import java.util.HashSet;
import java.util.Set;

/** Matches a Query with a Database.
 * Requires a database to function. 
 * Will give empty matches otherwise.
 */
public class QueryMatcher {
    private static Database database;

    /** Sets the database for query matching.
     * @param database the database to use for query matching.
     */
    public static void setDatabase(Database database){
        QueryMatcher.database = database;
    }

    /** Match a given query with the database and returns a set of pages that match the query.
     * Can also filter result by a URL filter, but this is not implemented in the application yet
     * @param q the query to match against.
     * @return a unsorted set of Page objects matching the query.
     */
    public static Set<Page> matchQuery(Query q){
        if (database == null) return new HashSet<>();
        Set<Page> results = new HashSet<>();
        for (Set<String> ANDSet : q.getORSet()){
            results.addAll(matchANDSet(ANDSet));
        }
        if (q.getURLFilter() != ""){
            results.removeIf(n -> !n.getURL().contains(q.getURLFilter()));
        }
        return results;
    }

    /** Matches a set of words (ANDSet) with the database 
     * and returns pages that contain all the words.
     * @return a set of Page objects that match the query.
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

    /** Matches a single word with the database and returns pages that contain the word.
     * @return a set of pages which contain the search word. 
     */
    private static Set<Page> matchWord(String word) {
        return database.getPages(word);
    }

}
