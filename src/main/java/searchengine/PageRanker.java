package searchengine;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/** A class for ranking Page objects relative to a Query. 
 * Has two ranking algorithms: TF and TFIDF.
 * Is set to TF by default. 
 * To switch to TF-IDF it is required that PageRanker refer to a Database.
 * <p>
 * TF: Term frequency (of type double) is here computed as the number of times 
 * a term i present in a document divided by the total number of terms in the document.
 * <p>
 * IDF: Inverse document frequency (of type double) is computed by the database as the natural logarithm 
 * of the inverse fraction of documents in the database that contain a given word.  
 * <p>
 * TFIDF: The product of TF & IDF for a given page.
 */
public final class PageRanker {
    private static Database database;
    private static RANKMETHOD rankMethod = RANKMETHOD.TF;

    public enum RANKMETHOD{TF,TFIDF}

    /** Assigns a given database as the one to rank pages from, when invoking the PageRanker-class
    * @param database the database to be used. Database may be null.
    */
    public static void setDatabase(Database database){
        PageRanker.database = database;
    }

    /** Sets the ranking algorithm for the PageRanker.
     * @param method enum that can be either TF or TFIDF.
     * @throws Exception If database is not set and method is TFIDF
     */
    public static void setRankMethod(RANKMETHOD method) throws Exception{
        switch (method) {
            case TFIDF:
                if (database == null) throw new Exception("Cannot use TFIDF, there is no database");
                PageRanker.rankMethod = method;
                break;
            default:
                PageRanker.rankMethod = method;
                break;
        }
    }

    /**Ranks a given Page object according to a given query.<p>
     * First assigns a rank to a Page object for each word separated by an OR-string in the query, 
     * and then adds them all together.
     * Computes the final rank of the Page object as the greatest rank among the sums of ranks.
     * @param page the Page object to rank
     * @param query the Query object to rank the Page object according to.
     */
    public static void rankPage (Page page, Query query) {
        Set<Double> orRanks = new HashSet<>();
        for (Set<String> ANDSet : query.getORSet()){
            double queryRank = 0;
            for (String word : ANDSet) {
                switch (rankMethod) {
                    case TFIDF:
                        queryRank += PageRanker.computeTFIDF(page, word);
                        break;
                    default:
                        queryRank += PageRanker.computeTF(page, word);
                        break;
                }
            }
            orRanks.add(queryRank);
        }
        page.setRank(Collections.max(orRanks));
    }

    // ____________________________________________________
    // Private methods

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
}
