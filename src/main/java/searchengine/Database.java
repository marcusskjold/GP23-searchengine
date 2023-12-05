package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/** Represents the database for the search engine.
 * Keeps all the web pages, and is able to return a sublist of those pages in response to a query
 * @author Marcus Skjold, Andreas Riget Bagge, Sean Weston
 * @version 0.1
 */
public class Database {
    private InvertedIndex invertedIndex;
  
    /** Creates a new database, generating a main list of web pages from the specified file.
     * The file must be formatted correctly as a flat text file with each page separated by "*PAGE:"
     * the URL on the line immediatly succeeding, 
     * the title as the next line and each word of the content of the webpages as a separate line
     * @param filename the text file to generate the database from
     * @throws IOException
     */
    public Database(String filename) throws IOException {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename)); 
            invertedIndex = new InvertedIndex(lines);
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /** Converts part of a list of String-objects to a Page-object, 
     * the URL must be at the first line, preceded by "*PAGE:"
     * the title as the next line 
     * and each word of the content of the webpages as a separate line.
     * @param lines the List of String-objects to convert from.
     * @return a Page-object corresponding to the lines read.
     */
    public static Page convertToPage(List<String> lines) {
      String title = lines.get(1);
      String URL = lines.get(0).substring(6); //Will throw error if no URL is listed after Page as of right now?
      List<String> content = lines.subList(2,lines.size());
      Page page = new Page(title, URL, content);
      return page;
    }
    
    public Set<Page> matchQuery(Query q){
        Set<Page> results = new HashSet<>();
        for (Set<String> ANDSet : q.getORSet()){
            results.addAll(matchANDSet(ANDSet));
        }
        if (q.getURLFilter() != ""){
            results.removeIf(n -> !n.getURL().contains(q.getURLFilter()));
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

    public Set<Page> matchWord(String word) { //Made public for invertedIndex
      Set<Page> match = invertedIndex.getPages(word);
      return match == null ? new HashSet<>() : match;
    }

    public int pagesInDataBase () {
      return invertedIndex.getPageNumber();
    }
}
