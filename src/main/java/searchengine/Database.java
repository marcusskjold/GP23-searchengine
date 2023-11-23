package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/** Represents the database for the search engine.
 * Keeps all the web pages, and is able to return a sublist of those pages in response to a query
 * @author Marcus Skjold, Andreas Riget Bagge, Sean Weston
 * @version 0.1
 */
public class Database {
    private PageList pages;
  
    /** Creates a new database, generating a main list of web pages from the specified file.
     * The file must be formatted correctly as a flat text file with each page separated by "*PAGE:"
     * the URL on the line immediatly succeeding, 
     * the title as the next line and each word of the content of the webpages as a separate line
     * @param filename the text file to generate the database from
     * @throws IOException
     */
    public Database(String filename) throws IOException {
        pages = new PageList();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename)); 
            var lastIndex = lines.size();
            for (var i = lines.size() - 1; i >= 0; --i) {
                if (lines.get(i).startsWith("*PAGE")) {
                    if(lines.subList(i, lastIndex).size()>2) { // Only add pages with content
                        Page page = new Page(lines.subList(i, lastIndex).get(1) , 
                                             lines.subList(i, lastIndex).get(0).substring(6), 
                                             lines.subList(i, lastIndex));
                        pages.addPage(page);
                    }
                lastIndex = i;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Collections.reverse(pages.getPageList());
    }

    /** Matches the main database of web pages with the search term
     * @param searchTerm the query to be answered. TODO: change to a Query type
     * @return a PageList containing the matching pages
     */
    public PageList search(String searchTerm) { //Iterates through the stored pages and try to find one where the word exists
      PageList result = new PageList();

      if(pages!=null) { //Checks that pages are not empty. Probably not necessary
        for (Page page : pages.getPageList()) {
          if (page.getContent().contains(searchTerm)) {
            result.addPage(page);
          }
        }
      }
        return result;
      }
}
