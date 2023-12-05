package searchengine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InvertedIndex {
    private Map<String, Set<Page>> invertedIndex;

    public InvertedIndex(List<String> lines) throws Exception {
        invertedIndex = new HashMap<>();
        int firstIndex = lines.indexOf(lines.stream().filter(w -> w.startsWith("*PAGE")).findFirst().orElse(null)); 
        if (firstIndex == -1) throw new Exception("Faulty data file, no pages correctly marked as ”*PAGE”");
        //Set first index equal to first instance of *PAGE. Returns index -1 if no instance
        for (int i = firstIndex+1; i < lines.size(); ++i) { 
            //Starting from the index succeeding first index of page till the end of the list.
                if ((lines.get(i).startsWith("*PAGE") || i==lines.size()-1) ) { 
                    //If it reaches a page or the end of the list.
                    if(lines.subList(firstIndex, i).size()>2) { 
                        // If not erroneous page
                        Page page = Database.convertToPage(lines.subList(firstIndex, i)); 
                        //Convert part of list to a page
                        addToInvertedIndex(page); 
                        //Add to inverted index.
                    }
                    firstIndex = i; //First index for conversion now at next instance of *PAGE.
            }
        }
    }

    public void addToInvertedIndex(Page page) {
        for (String word : page.getContent()) { 
            // page.getContent() returns a List<String>. 
            invertedIndex.computeIfAbsent(
                word.toLowerCase(), k -> new HashSet<>()).add(page);
                /* Returns the value associated with the key 'word' 
                * (computes the value as a new, empty ArrayList, if key is not already present) 
                * and then adds the page to that value (List). 
                * Normalize to lowercase. 
                * Ensures that the search is case-insensitive: 
                * 'Word' and 'word' will be treated as the same word. 
                * TODO: check if we want that kind of case-insensitivity
                */
        }
    }

    public Set<Page> getPages(String key) {
        return invertedIndex.get(key)==null ? 
            new HashSet<Page>() : new HashSet<Page>(invertedIndex.get(key));
    }

    public Map<String, Set<Page>> getInvertedIndex() {
        return invertedIndex;
    }
}
