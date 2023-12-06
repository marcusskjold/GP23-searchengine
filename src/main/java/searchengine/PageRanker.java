package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Comparator;
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
        
    private static double computeTF (String term, Page page){
        double termInDoc  = page.getContent().stream().filter(s -> s.equals(term)).count(); 
            //number of times term is in document
        double totalTerms = page.getContent().size();
            //Number of terms (repetitions allowed) in document
        return termInDoc/totalTerms; 
            //Used double to avoid casting and avoiding int division
    }

    private static double computeIDF (String term){
        double totalDocs = invertedIndex.getPageNumber(); 
            //Total number of documents in database
        double docsWithTerm = invertedIndex.getPages(term).size(); 
            //Total number of documents in database with searchterm
        return Math.log(totalDocs/docsWithTerm); 
            //Computation of IDF
    }

    private static double computeTFIDF (Page page, String term) { 
            //computes the TF-IDF value for the given page in a given database, for the given search term
        return (1+computeIDF(term))*computeTF(term, page);
    }

    public static double rankPage (Page page, Query query) {
        //Creates list for ranks of each OR-sequence
        List<Double> orRanks = new ArrayList<>();
        //For each AND-set
        for (Set<String> ANDSet : query.getORSet()){
            //Creates variable for its rank
            double queryRank = 0;
            //And then for each word in the andset
            for (String word : ANDSet) {
                //Compute its TFIDF and add it to the rank
                queryRank += PageRanker.computeTFIDF(page, word);
            }
            //And add that rank to the list of ranks
            orRanks.add(queryRank);
        }
        //Lastly return the highest of those ranks
        return Collections.max(orRanks);
    }
}
