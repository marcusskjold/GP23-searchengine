package searchengine;

import java.util.Set;

/** An object representing a collection of Pages.
 * Can return the pages associated with a given word. 
 * Can calculate the inverse document frequency (IDF) 
 * of a word in relation to the total number of pages in the database.
 * Is implemented by ImmutableDatabase.
 * @see ImmutableDatabase
 */
public interface Database {
    public Set<Page> getPages(String word);
    public double getIDF(String word);
}
