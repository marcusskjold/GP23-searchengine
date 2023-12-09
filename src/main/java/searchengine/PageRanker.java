package searchengine;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public final class PageRanker {
    private static InvertedIndex database;

    public static void setDatabase(InvertedIndex database){
        PageRanker.database = database;
    }

    private static double computeTF (Page page, String term){
        double termInDoc = page.getFrequency(term);
        double totalTerms = page.getTotalTerms();
        return termInDoc/totalTerms; 
    }

    private static double computeIDF (String term){
        return database.getIDF(term);
    }

    public static double computeTFIDF (Page page, String term) { 
        return (computeIDF(term)*computeTF(page, term));
    }

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
