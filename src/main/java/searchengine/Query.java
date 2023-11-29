package searchengine;

import java.util.HashSet;
import java.util.Set;

public class Query {
   
    private String searchTerm;
    private Set<Set<String>> orSet;
    private String URLFilter;

    public Query(String searchTerm) {
        this.searchTerm = searchTerm;
        Set<String> andSet = new HashSet<String>();
        URLFilter = "";
    }

    public String getSearchTerm() {
        return searchTerm;

    }

    public void TESTaddQuery(Set<Set<String>> query){
        orSet = query;
    }

    public Set<Set<String>> getORSet(){
        return orSet;
    }

    public String getURLFilter() {
        return null;
    }

    public void addURLFilter(String URLsegment){
        URLFilter = URLsegment;
    }

}
