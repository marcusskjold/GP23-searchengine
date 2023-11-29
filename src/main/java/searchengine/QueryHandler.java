package searchengine;


import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList; 



public class QueryHandler{
    //Initially assigned it a database as a field
    private Database database;

    
    //The QueryHandler initially initializes a Database-object

    public QueryHandler(String filename) throws IOException{
        this.database = new Database(filename); 
    }
    /*The initial search-method makes the database do the search. Other features can be added here */

    public Set<String> getAndSet(String searchTerm) { //throws QueryStringException

        String[] words = searchTerm.split("\\s+"); // Split the search term into individual words
        Set<String> andSet = new HashSet<>(); // Create a set to store the individual words

        for(String w : words) {
            andSet.add(w);
        }

    //andSet.addAll(words); // Add each word to the set

        //Query q = new Query(searchTerm);
        return andSet;
        //return database.matchQuery(q);
    }

    public Set<Set<String>> splitOR(String searchString) {
        Set<Set<String>> andSets = new HashSet<>();
        String[] orStrings = searchString.split("OR");
        for (String string : orStrings) {
            andSets.add(getAndSet(string));
        }
        return andSets;
    }

    public Set<Page> search(String searchString) {
        Query q = new Query(splitOR(searchString));
        return database.matchQuery(q);
        
    }

    // Needs to split search String(Searchterm) into sets so each word is an element in the set
    // Needs to take OR into account 
    // Change constructor in query class so that it can receive and store set<Set> of String 


    //  Method for receiving queries with "OR" 
    //  * Set of sets? uhh how does that work? 
    //  * Probably should tinker with a constructor, but which? 
    //  * 
    //  * 
    //  * getMatchingWebPages is a public method that takes a query of type string as input and returns a Set of WebPage objects. 
    //  * This method is intended to be the primary entry point for processing a search query.
    //  * 

    
    

    // public  getMatchingWebPages(String query){

        // if (query.contains("OR")){ // First we want to check if a query contains OR
        //     return (query); 
        // }
        // else { // If there isn't an OR in the query, then treat it as a normal query. 
        //     return (query)
        // } 
        // public handleOrQueries(String query)


}
