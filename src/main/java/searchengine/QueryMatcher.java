package searchengine;

import java.util.HashSet;
import java.util.Set;

/** Matches a query with a database.
 */
public class QueryMatcher {
    private static Database database;

    public static void setDatabase(Database database){
        QueryMatcher.database = database;
    }

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

    private static Set<Page> matchWord(String word) {
        return database.getPages(word);
    }

}
