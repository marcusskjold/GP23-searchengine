package searchengine;

public class QueryHandler{
    private Database database;

    public QueryHandler(Database database){
        this.database = database;
    }
    public PageList search(String searchTerm) throws QueryStringException {
        return new PageList();
    }
}
