package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;

public class PageRanker {
    private static InvertedIndex invertedIndex;

    public static List<Page> rankPages(Set<Page> pages, Query q){
        Objects.requireNonNull(pages);
        Objects.requireNonNull(q);
        return pages.stream()
                    .sorted(Comparator.comparing(p -> rankPage(p, q)))
                    .toList();
    }

    public static void setInvertedIndex(InvertedIndex index){
        invertedIndex = index;
    }
       

    private static double computeTF (Page page, String term){
        double termInDoc = page.getFrequency(term);
        double totalTerms = page.getTotalTerms();
        return termInDoc/totalTerms; 
    }

    private static double computeIDF (String term){
        return invertedIndex.getIDF(term);
        // double totalDocs = invertedIndex.getPageNumber(); 
        // double docsWithTerm = invertedIndex.getPages(term).size(); 
        // return Math.log(totalDocs/docsWithTerm); 
    }

    public static double computeTFIDF (Page page, String term) { 
        return (computeIDF(term)*computeTF(page, term));
    }

    //Also does something like this when a query is received by a queryhandler. Could this diminish effectivity?
    public static double rankPage (Page page, Query query) {
        Set<Double> orRanks = new HashSet<>();
        for (Set<String> ANDSet : query.getORSet()){
            double queryRank = 0;
            for (String word : ANDSet) {
                queryRank += PageRanker.computeTFIDF(page, word); // TODO Note how to change in report
            }
            orRanks.add(queryRank);
        }
        return Collections.max(orRanks);
    }
}
