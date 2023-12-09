package searchengine;

import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;
import java.util.HashSet;

public final class QueryHandler{

    public static Set<Page> search(String searchString) {
        Query q = new Query(splitSearchString(searchString));
        Set<Page> result = QueryMatcher.matchQuery(q);
        Set<Page> l = new TreeSet<Page>();
        for (Page page:result){
            page.rank(q);
            l.add(page);
        }
        return l;
    }

    public static Set<Set<String>> splitSearchString(String searchString) {
        Set<Set<String>> returnSets = new HashSet<>();
        String[] s = searchString.split("^OR%20|%20OR%20|%20OR$");
        for (String string : s) returnSets.add(splitString(string));
        return returnSets;
    }

    private static Set<String> splitString(String searchString) {
        searchString = searchString.toLowerCase(); 
        Set<String> returnSets = new HashSet<>();
        Collections.addAll(returnSets, searchString.split("(%20)++"));
        returnSets.removeIf(s -> (s.isBlank()));
        return returnSets;
    }
}
