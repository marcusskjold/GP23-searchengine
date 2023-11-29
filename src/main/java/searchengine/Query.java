package searchengine;

import java.util.HashSet;
import java.util.Set;

public class Query {
   
    private String searchTerm;
    private Set<Set<String>> orSet;

    public Query(String searchTerm) {
        this.searchTerm = searchTerm;
        Set<String> andSet = new HashSet<String>();
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
}
