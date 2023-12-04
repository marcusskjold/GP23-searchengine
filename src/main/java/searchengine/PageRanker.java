package searchengine;

public class PageRanker {
    
   public static double computeTF (String term, Page page){
    double termInDoc = page.getContent().stream().filter(s -> s.equals(term)).count(); //number of times term is in document
    double totalTerms = page.getContent().size(); //Number of terms (repetitions allowed) in document
    return termInDoc/totalTerms; //Used double to avoid casting and avoiding int division
    }

    public static double computeIDF (Database database, String term){
        double totalDocs = database.pagesInDataBase(); //Total number of documents in database
        double docsWithTerm = database.matchWord(term).size(); //Total number of documents in database with searchterm
        return Math.log(totalDocs/docsWithTerm); //Computation of IDF
    }

    public static double computeTFIDF (Page page, String term, Database database) { //computes the TF-IDF value for the given page in a given database, for the given search term
        return (computeIDF(database, term))*computeTF(term, page);
    }

}
