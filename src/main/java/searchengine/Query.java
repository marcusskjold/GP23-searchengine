package searchengine;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Query {
    private Set<Set<String>> orSet;
    private String URLFilter;

    public Query(Set<Set<String>> query){
        orSet = query;
        URLFilter = "";
    }

    public Query(String searchTerm){
        Set<String> andSet = new HashSet<String>();
        andSet.add(searchTerm);
        orSet = new HashSet<Set<String>>();
        orSet.add(andSet);
    }

    public Set<Set<String>> getORSet() { return orSet; }

    public String getURLFilter() { return URLFilter; }

    public void setURLFilter(String URLsegment) { URLFilter = URLsegment; }

}
