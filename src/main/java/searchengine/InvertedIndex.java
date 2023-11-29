package searchengine;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InvertedIndex {
    private Map<String, Set<Page>> invertedIndex;

    public InvertedIndex(List<String> lines) {
        int firstIndex = 0;
        for (int i = 0; i < lines.size(); ++i) {
                if ((lines.get(i).startsWith("*PAGE") || i==lines.size()-1) && firstIndex!=0) { //If it reaches a page or the end of the list. And if it is not the first entry of the list.
                    if(lines.subList(firstIndex, i).size()>2) { // If not erroneous page
                        Page page = Database.convertToPage(lines.subList(firstIndex, i)); //Convert part of list to a page
                        addToInvertedIndex(page); //Add to inverted index.
                    }
                firstIndex = i;
            }
        }
    }

    public void addToInvertedIndex(Page page) {
        for (String word : page.getContent()) { // page.getContent() returns a List<String>. 
                            invertedIndex.computeIfAbsent(word.toLowerCase(), k -> new HashSet<>()).add(page); //Returns the value associated with the key 'word' (computes the value as a new, empty ArrayList, if key is not already present) and then adds the page to that value (List). Normalize to lowercase. Ensures that the search is case-insensitive: 'Word' and 'word' will be treated as the same word. TO-DO-check if we want that kind of case-insensitivity
        }
    }

    public Set<Page> getPages(String key) {
        return invertedIndex.get(key);
    }
}
