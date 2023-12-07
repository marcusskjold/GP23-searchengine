package searchengine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InvertedIndex {
    
    private Map<String, Set<Page>> invertedIndex;
    private int pageNumber;

    public InvertedIndex(List<String> lines) throws Exception {
        if (lines.isEmpty()) throw new Exception("Lines are empty");
        invertedIndex = new HashMap<>();
        pageNumber = 0;
        int firstIndex = lines.indexOf(lines.stream().filter(w -> w.startsWith("*PAGE")).findFirst().orElse(null)); 
        if (firstIndex == -1) throw new Exception("Faulty data file, no pages correctly marked as ”*PAGE”");
        //Set first index equal to first instance of *PAGE. Returns index -1 if no instance
        for (int i = firstIndex+1; i < lines.size(); ++i) { 
            //Starting from the index succeeding first index of page till the end of the list.
                if ((lines.get(i).startsWith("*PAGE")) ) { 
                    //If it reaches a page or the end of the list.
                    if(lines.subList(firstIndex, i).size()>2) { 
                        // If not erroneous page
                        Page page = new Page(lines.subList(firstIndex, i)); 
                        //Convert part of list to a page
                        pageNumber++;
                        addToInvertedIndex(page); 
                        //Add to inverted index.
                    }
                    firstIndex = i; //First index for conversion now at next instance of *PAGE.
            }
        }
        addToInvertedIndex(new Page(lines.subList(firstIndex, lines.size())));
        pageNumber++;
    }

    private void addToInvertedIndex(Page page) {
        for (String word : page.getContent()) { 
            // page.getContent() returns a List<String>. 
            invertedIndex.computeIfAbsent(
                word.toLowerCase(), k -> new HashSet<>()).add(page);
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

    public Map<String, Set<Page>> getInvertedIndex() { return invertedIndex; }

    public int getPageNumber () { return pageNumber; }
}
