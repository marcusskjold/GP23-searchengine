package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Comparator;

public class PageRanker {
    private static Database database;

    public static List<Page> rankPages(Set<Page> pages, Query q, Database db){
        return pages.stream()
                    .sorted(Comparator.comparing(p -> rankPage(p, q)))
                    .toList();

    }

    public static void setDatabase(Database db){
        database = db;
    }
        
    public static double computeTF (String term, Page page){
        double termInDoc  = page.getContent().stream().filter(s -> s.equals(term)).count(); 
            //number of times term is in document
        double totalTerms = page.getContent().size();
            //Number of terms (repetitions allowed) in document
        return termInDoc/totalTerms; 
            //Used double to avoid casting and avoiding int division
    }

    public static double computeIDF (Database database, String term){
        double totalDocs = database.pagesInDataBase(); 
            //Total number of documents in database
        double docsWithTerm = database.matchWord(term).size(); 
            //Total number of documents in database with searchterm
        return Math.log(totalDocs/docsWithTerm); 
            //Computation of IDF
    }

    public static double computeTFIDF (Page page, String term, Database database) { 
            //computes the TF-IDF value for the given page in a given database, for the given search term
        return (computeIDF(database, term))*computeTF(term, page);
    }


    public static double rankPageFromQuery (Query query, Page page, Database database) {
        //Creates list for ranks of each OR-sequence
        List<Integer> orRanks = new ArrayList<>();
        //For each AND-set
        for (Set<String> ANDSet : query.getORSet()){
            //Creates variable for its rank
            int queryRank = 0;
            //And then for each word in the andset
            for (String word : ANDSet) {
                //Compute its TFIDF and add it to the rank
                queryRank += PageRanker.computeTFIDF(page, word, database);
            }
            //And add that rank to the list of ranks
            orRanks.add(queryRank);
        }
        //Lastly return the highest of those ranks
        return Collections.max(orRanks);
    }
}
