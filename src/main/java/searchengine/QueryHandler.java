package searchengine;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * This class represents our query handler.It is designed to receive more complext queries by being able to deal with queries seperated by an OR. 
 * 
 * @author
 * @version 
 * 
 * The constructor for QueryHandler receives a String filename and throws and IOexception.
 * @param filename 
 * @throws IOexception
 *
 *
 * The method public Set<Page> search(String searchString) Searches the database with the given search string and returns a set of ranked pages.
 * The search string is split and processed to match against the database entries.
 * 
 * @param SeachString
 * @return l 
 * 
 * The method public Set<Set<String>> splitSearchString Splits the input search string into a set of string sets based on OR. 
 * @param SeachString
 * @return returnSets 
 * 
 * The method private Set<String> splitString(String searchString) splits a given string into a set of strings, removing any empty strings.
 * @param SearchString
 * @return returnSets 
 * 
 * 
 * 
 * 
 */

public class QueryHandler{
    private Database database;

    public QueryHandler(String filename) throws IOException{
        this.database = new Database(filename); 
    }

    public Set<Page> search(String searchString) {
        Query q = new Query(splitSearchString(searchString));
        Set<Page> result = database.matchQuery(q);
        // result.forEach(p -> p.rank(q));
        Set<Page> l = new TreeSet<Page>();
        for (Page page:result){
            page.rank(q);
            l.add(page);
        }
        // l.forEach();
        // Collections.sort(l);
        return l;
        // return result;
        // Set<Page> result = database.matchQuery(q);
        // return PageRanker.rankPages(result, q);
    }

    public Set<Set<String>> splitSearchString(String searchString) {
        Set<Set<String>> returnSets = new HashSet<>();
        String[] s = searchString.split("^OR%20|%20OR%20|%20OR$");
        for (String string : s) returnSets.add(splitString(string));
        return returnSets;
    }

    private Set<String> splitString(String searchString) {
        searchString = searchString.toLowerCase(); 
        Set<String> returnSets = new HashSet<>();
        Collections.addAll(returnSets, searchString.split("(%20)++"));
        returnSets.removeIf(s -> (s.isBlank()));
        return returnSets;
    }
}
