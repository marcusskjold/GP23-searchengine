package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

/** Represents the database for the search engine.
 * Keeps all the web pages, and is able to return a sublist of those pages in response to a query
 * @author Marcus Skjold, Andreas Riget Bagge, Sean Weston
 * @version 0.1
 */
public class Database {
    private List<Page> pages;
    private Map<String, Set<Page>> invertedIndex;
  
    /** Creates a new database, generating a main list of web pages from the specified file.
     * The file must be formatted correctly as a flat text file with each page separated by "*PAGE:"
     * the URL on the line immediatly succeeding, 
     * the title as the next line and each word of the content of the webpages as a separate line
     * @param filename the text file to generate the database from
     * @throws IOException
     */
    public Database(String filename) throws IOException {
        invertedIndex = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename)); 
            int firstIndex = 0;
            for (int i = 0; i < lines.size(); ++i) {
                if (lines.get(i).startsWith("*PAGE")|| i==lines.size()-1) {
                    if(firstIndex!=0 && lines.subList(firstIndex, i).size()>2) { // Only add pages with content
                        Page page = convertToPage(lines.subList(firstIndex, i));
                        for (String word : page.getContent()) { // page.getContent() returns a List<String>. 
                            invertedIndex.computeIfAbsent(word.toLowerCase(), k -> new ArrayList<>()).add(page); //Returns the value associated with the key 'word' (computes the value as a new, empty ArrayList, if key is not already present) and then adds the page to that value (List). Normalize to lowercase. Ensures that the search is case-insensitive: 'Word' and 'word' will be treated as the same word. TO-DO-check if we want that kind of case-insensitivity?
                        }
                        firstIndex = i;
                    } else {
                        firstIndex = i;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
    public static Page convertToPage(List<String> lines) {
      String title = lines.get(1);
      String URL = lines.get(0).substring(6);
      List<String> content = lines.subList(2,lines.size());
      Page page = new Page(title, URL, content);
      return page;
    }

    public void invertedIndex() {
        for (Page page : pages) { // This is a "for-each" loop that iterates over a collection of Page objects.
            for (String word : page.getContent()) { // page.getContent() returns a List<String>. 
                        invertedIndex.computeIfAbsent(word.toLowerCase(), k -> new ArrayList<>()).add(page); //Returns the value associated with the key 'word' (computes the value as a new, empty ArrayList, if key is not already present) and then adds the page to that value (List). Normalize to lowercase. Ensures that the search is case-insensitive: 'Word' and 'word' will be treated as the same word. TO-DO-check if we want that kind of case-insensitivity?
            }
        }
    }
  
    /** Matches the main database of web pages with the search term
     * @param word the query to be answered. TODO: change to a Query type
     * @return a Set<Page> containing the matching pages
     */
    private Set<Page> matchWord(String word) {
      Set<Page> match = invertedIndex.get(word);
      return match == null ? new HashSet<>() : match;
    }
    
    /**
     * Returns the number of Page-objects in the page-field of the Database
     * @return the number of Page-objects in the pages field
     */
    public int getNumberOfPages() {
        return pages.size();
    }

    public Set<Page> matchQuery(Query q){
        Set<Page> results = new HashSet<>();
        for (Set<String> ANDSet : q.getORSet()){
            results.addAll(matchANDSet(ANDSet));
        }
        return results;
    }

    private Set<Page> matchANDSet(Set<String> ANDSet) {
        Set<Page> result = new HashSet<>();
        boolean firstWord = true;
        for (String word : ANDSet){
            if (firstWord == true) result = matchWord(word);
            else result.retainAll(matchWord(word));
            if ((result.isEmpty() )) break;
            firstWord = false;
        }
        return result;
    }
}
