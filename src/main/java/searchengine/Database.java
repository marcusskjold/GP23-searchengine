package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/** Represents the database for the search engine.
 * Keeps all the web pages, and is able to return a sublist of those pages in response to a query
 * @author Marcus Skjold, Andreas Riget Bagge, Sean Weston
 * @version 0.1
 */
public class Database {
    private PageList pages;
    private Map<String, List<Page>> invertedIndex;
  
    /** Creates a new database, generating a main list of web pages from the specified file.
     * The file must be formatted correctly as a flat text file with each page separated by "*PAGE:"
     * the URL on the line immediatly succeeding, 
     * the title as the next line and each word of the content of the webpages as a separate line
     * @param filename the text file to generate the database from
     * @throws IOException
     */
    public Database(String filename) throws IOException {
        pages = new PageList();
        invertedIndex = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename)); 
            int lastIndex = lines.size();
            for (int i = lines.size() - 1; i >= 0; --i) {
                if (lines.get(i).startsWith("*PAGE")) {
                    if(lines.subList(i, lastIndex).size()>2) { // Only add pages with content
                        Page page = new Page(lines.subList(i, lastIndex).get(1) , 
                                             lines.subList(i, lastIndex).get(0).substring(6), 
                                             lines.subList(i, lastIndex));
                        //Page page = convertToPage(lines, i, lastIndex);
                        pages.add(page);
                    }
                lastIndex = i;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Collections.reverse(pages);
    }

    /** Converts part of a list of String-objects to a Page-object, 
     * starting from a given first index (inclusive) and ending at a given index (exclusive).
     * The file must be formatted correctly as a flat text file with each page separated by "*PAGE:"
     * the URL on the line immediatly succeeding, 
     * the title as the next line and each word of the content of the webpages as a separate line.
     * @param lines the List of String-objects to convert from.
     * @param firstIndex the index (inclusive) from where the conversion starts.
     * @param lastIndex the index (exclusive) where the converion ends.
     * @return a Page-object corresponding to the lines read.
     */
    public static Page convertToPage(List<String> lines, int firstIndex, int lastIndex) {
      String title = lines.subList(firstIndex, lastIndex).get(1);
      String URL = lines.subList(firstIndex, lastIndex).get(0).substring(6);
      List<String> content = lines.subList(firstIndex, lastIndex);
      Page page = new Page(title, URL, content);
      return page;
    }

    /** Matches the main database of web pages with the search term
     * @param searchTerm the query to be answered. TODO: change to a Query type
     * @return a List<Page> containing the matching pages
     */
     public void invertedIndex() {

      for (Page page : pages.getPageList()) { // This is a "for-each" loop that iterates over a collection of Page objects.
          for (String line : page.getContent()) { // page.getContent() returns a List<String>. 
              String[] words = line.split("\\W+"); // Split the line into words. 
              for (String word : words) {
                  word = word.toLowerCase(); // Normalize to lowercase. Ensures that the search is case-insensitive: 'Word' and 'word' will be treated as the same word.
                  if (!word.isEmpty()) { // Check if the word is not empty after splitting. 
                      invertedIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(page);
                  }
              }
          }
      }
  }
  
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

    /**
     * Returns the number of Page-objects in the page-field of the Database
     * @return the number of Page-objects in the pages field
     */
      public int getNumberOfPages() {
      return pages.size();
      }
}
