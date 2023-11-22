package searchengine;

import java.io.IOException;
import java.util.List;

public class QueryHandler{
    private Database database;

    public QueryHandler(String filename) throws IOException{
        this.database = new Database(filename); 
    }

    public PageList search(String searchTerm) throws QueryStringException { //Replaced return type Pagelist with list of list till pagelist is
        return database.search(searchTerm);
        //return new PageList();
    }
}
