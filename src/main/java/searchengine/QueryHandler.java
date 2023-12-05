package searchengine;

import java.io.IOException;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;

public class QueryHandler{
    private Database database;

    public QueryHandler(String filename) throws IOException{
        this.database = new Database(filename); 
    }

    public Set<Page> search(String searchString) {
        Query q = new Query(splitSearchString(searchString));
        return database.matchQuery(q);
    }

    public Set<Set<String>> splitSearchString(String searchString) {
        Set<Set<String>> returnSets = new HashSet<>();
        String[] s = searchString.split("^OR%20|%20OR%20|%20OR$");
        for (String string : s) returnSets.add(splitString(string));
        return returnSets;
    }

    public Set<String> splitString(String searchString) {
        searchString = searchString.toLowerCase(); 
        Set<String> returnSets = new HashSet<>();
        Collections.addAll(returnSets, searchString.split("(%20)++"));
        returnSets.removeIf(s -> (s.isBlank()));
        return returnSets;
    }
}
