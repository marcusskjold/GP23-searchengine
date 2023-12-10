package searchengine;

import java.nio.file.Files;
import java.nio.file.Paths;

import searchengine.PageRanker.RANKMETHOD;

import java.io.IOException;

/** Main class for the search engine application.
 * The main method constructs a web server and a database.
 * The database is generated from a data file specified by the config.txt
 * Main class sets the database for PageRanker and QueryMatcher static methods.
 * 
 * @author Andreas Riget Bagge
 * @author Marcus Skjold
 * @author Sean Weston
 */
public class Main {
    static final int PORT = 8080;

    /** Main method to launch the application. 
     * Initializes the database from the configuration file.
     * Sets the database for the PageRanker and QueryMatcher.
     * Starts the web server on the specified port.
     * @param args are not used.
     * @throws IOException If there is an error reading the configuration file. 
     * Any other errors are printed to terminal.
     */
    public static void main(final String... args) throws IOException {

        String filename = Files.readString(Paths.get("config.txt")).strip();
        try {
            Database database = new ImmutableDatabase(filename);
            PageRanker.setDatabase(database);
            PageRanker.setRankMethod(RANKMETHOD.TFIDF);
            QueryMatcher.setDatabase(database);
            new WebServer(PORT);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
