package searchengine;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/** A class for ranking Page objects. Will be connected to a given Database-object and will compute
 * scores for Pages from a given computation-style. 
 * <p>
 * The default computation-style is TFIDF of type double (term frequency times inverted document frequency),
 * but it is possible to switch to TF as computationstyle, by using setComputationStyle-method("TF"). 
 * <p>
 * Term frequency (of type double) is here computed as the number of times a term i present in a document divided by the total number of terms in the document.
 * <p>
 * Inverse document frequency (of type double) is computed as the natural logarithm of the inverse fraction of documents in the database that contain a given word.  
 * @author Marcus Skjold, Andreas Riget Bagge, Sean Weston
 */
public final class PageRanker {
    private static Database database;
    private static String computationStyle = "TFIDF";


   public static void setComputationStyle(String computationStyle) {
        PageRanker.computationStyle = computationStyle;
    }

/** Assigns a given database as the one to rank pages from, when invoking the PageRanker-class
    * @param database the database to be used.
    */
    public static void setDatabase(Database database){
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

    private static double computeTFIDF (Page page, String term) { 
        return (computeIDF(term)*computeTF(page, term));
    }


    /**Ranks a given Page object according to a given query.
     * <p>
     * First assigns a rank to a Page object for each word separated by an OR-string in the query, and then adds them all together.
     * <p>
     * Computes the final rank of the Page object as the greatest rank among the sums of ranks.
     * @param page the Page object to rank
     * @param query the Query object to rank the Page object according to.
     */
    public static void rankPage (Page page, Query query) {
        Set<Double> orRanks = new HashSet<>();
        for (Set<String> ANDSet : query.getORSet()){
            double queryRank = 0;
            for (String word : ANDSet) {
                if(computationStyle.equals("TFIDF")) queryRank += PageRanker.computeTFIDF(page, word);
                if(computationStyle.equals("TF")) queryRank += PageRanker.computeTF(page, word);
            }
            orRanks.add(queryRank);
        }
        page.setRank(Collections.max(orRanks));
    }
}
