package searchengine;

import java.io.IOException;
import java.util.List;

public class QueryHandler{
    //Initially assigned it a database as a field
    private Database database;

    //The QueryHandler initially initializes a Database-object
    public QueryHandler(String filename) throws IOException{
        this.database = new Database(filename); 
    }

    //The initial search-method makes the database do the search
    //Other features can be added here
    public PageList search(String searchTerm) throws QueryStringException {
        return database.search(searchTerm);
    }

    /*Make a method which receives a query from the webserver and sends it to the database. */ 



}
