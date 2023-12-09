package searchengine;

import java.util.HashSet;
import java.util.Set;

/** Represents the database for the search engine.
 * Keeps all the web pages, and is able to return a sublist of those pages in response to a query
 * @author Marcus Skjold, Andreas Riget Bagge, Sean Weston
 * @version 0.1
 */
public class QueryMatcher {
    private static Database database;

    public static void setDatabase(Database database){
        QueryMatcher.database = database;
    }

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

    public static Set<Page> matchWord(String word) {
        Set<Page> match = database.getPages(word);
        return match == null ? new HashSet<>() : match;
    }

}
