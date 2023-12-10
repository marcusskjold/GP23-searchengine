package searchengine;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

/** Main class for the search engine application.
 * 
 * 
 * This class can be viewed as the entry point into the the search engine. It initializes necessary the database and the web server, and sets up the environment for handling search queries.
 * Run this class to start the web server and initialize the search engine.
 * 
 * @author
 * @version
 */

public class Main {

    // Port number to our web server.
    static final int PORT = 8080;

    /**
     * Main method to launch the application. 
     * The method seads the database configuration filename from 'config.txt'.
     * The method then Initializes the database with the configuration file.
     * The method then set's the database for the PageRanker and QueryMatcher components.
     * It is the main method that starts the web server on the specified port.
     * @param args 
     * @throws IOException If there is an error reading the configuration file.
     */

    public static void main(final String... args) throws IOException {

        String filename = Files.readString(Paths.get("config.txt")).strip();
        try {
            Database database = new ImmutableDatabase(filename);
            PageRanker.setDatabase(database);
            QueryMatcher.setDatabase(database);
            new WebServer(PORT);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
