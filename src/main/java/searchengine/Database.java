package searchengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** An object representing a collection of web pages <p>
 * The database reads from a flat text file and is unmodifiable after construction. 
 * It keeps an inverted index of the set of words appearing in the contents of the web pages,
 * It also keeps an index of the inverse document frequency (IDFindex) for those words. <p>
 * The inverted index is generated and populated by the constructer,
 * while the IDFindex is calculated and cached as it is called.
 * A database can return the IDF or the set of pages associated with a given word. <p>
 * If the same URL is defined multiple times in the database folder, the database may 
 * have unintended behavior. Later occurances of pages with the same URL will only be
 * mapped to words that are not already mapped to previous occurances.
 * @author Marcus Skjold
 * @author Andreas Riget Bagge
 * @author Sean Weston
 */
public class Database {
    private Map<String, Set<Page>> invertedIndex;
    private Map<String, Double> IDFindex;
    private int pageNumber;

    /** Constructs a populated Database from the provided filename. <p>
     * The database requires a correctly formatted flat text file.
     * The file must separate each web page with a line starting with "*PAGE" 
     * followed by the full URL to the web page.
     * The first line of the text file must be the start of a web page.
     * Any exceptions are propagated.
     * @param fileName the relative file path to the database file.
     * @throws IOException if I/O error occurs during opening the file.
     * @throws InvalidPathExcetion if fileName is invalid.
     * @throws NoSuchElementException if the file has no content.
     * @throws InvalidDataFormat if data file is incorrectly formatted or if it
     * only contains pages without content.
     */
    public Database(String fileName) 
    throws IOException, InvalidPathException, NoSuchElementException, InvalidDataFormatException {
        invertedIndex = new HashMap<>();
        IDFindex = new HashMap<>();
        pageNumber = 0;
        Path path = Paths.get(fileName);
        if (!Files.lines(path).findFirst().get().startsWith("*PAGE:")) 
            throw new InvalidDataFormatException(
            "Data file incorrectly formatted, first line does not start with '*PAGE'");
        constructInvertedIndex(path);
        if (pageNumber == 0) throw new InvalidDataFormatException(
            "Data file contains no correct pages with content");
    }

    /** Returns a Set from the inverted index associated with a given word string. 
     * <p> If that index is null, it returns an empty Set.
     * Always returns a new Set to keep the inverted index immutable.
     * @param word the key to fetch the mapping for.
     * @return a Set<Page> corresponding to the mapping of the key.
     */
    public Set<Page> getPages(String word) {
        return invertedIndex.get(word) == null ? 
            new HashSet<Page>() : new HashSet<Page>(invertedIndex.get(word));
    }

    /** Returns the inverse document frequency (IDF) associated with a given word string.
     * <p> The IDF is calculated by taking the natural log of the total number of 
     * documents in the database divided by the number of documents associated with 
     * the word in the inverted index.
     * The result is cached, providing faster subsequent retrieval. This is done to 
     * provide a balance between bootup time of the database and speed of retrieval.
     * @param word the key to calculate the IDF for.
     * @return a double representing the IDF of the given word. 
     * Returns -1 if no documents contains the word. 
     * Returns 0 if all documents contains the word.
     */
    public double getIDF (String word){
        if (IDFindex.containsKey(word)) return IDFindex.get(word);
        double docsWithTerm = getPages(word).size(); 
        if (docsWithTerm == 0) return -1;
        double totalDocs = pageNumber; 
        double IDF = Math.log(totalDocs/docsWithTerm);
        IDFindex.put(word, IDF);
        return IDF;
    }

    /** Retrieves the inverted index of this database for testing purposes.
     * @return the invertedIndex stored by this database.
     */
    protected Map<String, Set<Page>> getInvertedIndex() { return invertedIndex; }

    private void addPage(Page page) {
        for (String word : page.getWordSet()) { 
            invertedIndex.computeIfAbsent(
                word.toLowerCase(), k -> new HashSet<>()).add(page);
        } pageNumber++;
    }

    private void addNewPage(List<String> pageLines) {
        try {addPage(new Page(pageLines));} catch (Exception e) {}
    }

    private void constructInvertedIndex(Path path) throws IOException {
        List<String> pageLines = new ArrayList<>();
        Stream<String> lines = Files.lines(path);
        lines.forEach(line -> {
            if (line.startsWith("*PAGE:")) {
                addNewPage(pageLines);
                pageLines.clear();
            }
            pageLines.add(line);
        });
        addNewPage(pageLines);
        lines.close();
    }
}
