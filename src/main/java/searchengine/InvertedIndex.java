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
        invertedIndex = new HashMap<>();
        pageNumber = 0;
        int firstIndex = lines.indexOf(lines.stream().filter(w -> w.startsWith("*PAGE")).findFirst().orElse(null)); 
        if (firstIndex == -1) throw new Exception("Faulty data file, no pages correctly marked as ”*PAGE”");
        //Set first index equal to first instance of *PAGE. Returns index -1 if no instance
        for (int i = firstIndex+1; i < lines.size(); ++i) { 
            //Starting from the index succeeding first index of page till the end of the list.
                if ((lines.get(i).startsWith("*PAGE") || i==lines.size()-1) ) { 
                    //If it reaches a page or the end of the list.
                    if(lines.subList(firstIndex, i).size()>2) { 
                        // If not erroneous page
                        Page page = convertToPage(lines.subList(firstIndex, i)); 
                        //Convert part of list to a page
                        pageNumber++;
                        addToInvertedIndex(page); 
                        //Add to inverted index.
                    }
                    firstIndex = i; //First index for conversion now at next instance of *PAGE.
            }
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

    public void addToInvertedIndex(Page page) {
        for (String word : page.getContent()) { 
            // page.getContent() returns a List<String>. 
            invertedIndex.computeIfAbsent(
                word.toLowerCase(), k -> new HashSet<>()).add(page);
                /* Returns the value associated with the key 'word' 
                * (computes the value as a new, empty ArrayList, if key is not already present) 
                * and then adds the page to that value (List).
                */
        }
    }
    
    public Set<Page> getPages(String key) {
        return invertedIndex.get(key)==null ? 
            new HashSet<Page>() : new HashSet<Page>(invertedIndex.get(key));
    }

    public Map<String, Set<Page>> getInvertedIndex() { return invertedIndex; }

    public int getPageNumber () { return pageNumber; }
}
