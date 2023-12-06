package searchengine;

import java.util.Set;

public class Query {
    private Set<Set<String>> orSet;
    private String URLFilter;

    public Query(Set<Set<String>> query){
        orSet = query;
        URLFilter = "";
    }

    public Set<Set<String>> getORSet() { return orSet; }

    public String getURLFilter() { return URLFilter; }

    public void setURLFilter(String URLsegment) { URLFilter = URLsegment; }

}
