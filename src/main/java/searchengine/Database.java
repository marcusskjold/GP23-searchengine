package searchengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Database {
    
    private Map<String, Set<Page>> invertedIndex;
    private Map<String, Double> IDFindex;
    private int pageNumber;

    public Database(String fileName) throws Exception {
        invertedIndex = new HashMap<>();
        IDFindex = new HashMap<>();
        pageNumber = 0;
        List<String> pageLines = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            lines.forEach(line -> {
                if (line.startsWith("*PAGE")) {
                    addNewPage(pageLines);
                    pageLines.clear();
                }
                pageLines.add(line);
            });
            addNewPage(pageLines);
        } catch (Exception e) {
            throw new Exception("Error constructing database from file");
        }
    }

    /** Returns a Set from the inverted index associated with a given key String. 
     *  If that index is null, it just returns an empty Set, to avoid a NullPointerException
     *  from creating set with 'null'. Creates new Set to avoid altering the entries
     *  of the inverted index.
     *  @param key the key to fetch the mapping for.
     *  @return a Set<Page> corresponding to the mapping of the key.
     */
    public Set<Page> getPages(String key) {
        return invertedIndex.get(key)==null ? 
            new HashSet<Page>() : new HashSet<Page>(invertedIndex.get(key));
    }

    public double getIDF (String key){
        if (IDFindex.containsKey(key)) return IDFindex.get(key);
        double docsWithTerm = getPages(key).size(); 
        if (docsWithTerm == 0) return -1;
        double totalDocs = pageNumber; 
        double IDF = Math.log(totalDocs/docsWithTerm);
        IDFindex.put(key, IDF);
        return IDF;
    }

    protected Map<String, Set<Page>> getInvertedIndex() { return invertedIndex; }

    private void addPage(Page page) {
        for (String word : page.getWordSet()) { 
            invertedIndex.computeIfAbsent(
                word.toLowerCase(), k -> new HashSet<>()).add(page);
        }
        pageNumber++;
    }

    private void addNewPage(List<String> pageLines) {
        try {addPage(new Page(pageLines));} catch (Exception e) {}
    }

}
