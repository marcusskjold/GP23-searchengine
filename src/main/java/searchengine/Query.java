package searchengine;

public class Query {

    private String searchTerm;

    public Query(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchTerm() {
        return searchTerm;
    }
}
