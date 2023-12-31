package searchengine;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import searchengine.PageRanker.RANKMETHOD;

public class PageRankerTest {

    class TestDatabase implements Database{
        TestDatabase(){}
        public Set<Page> getPages(String word){ return null;}
        public double getIDF(String word){
            double rarity = Integer.parseInt(word.substring(word.length()-1));
            return rarity/3;}
    }

    // ____________________________________________________
    // Helper methods

    Page easyPage(int titleID, List<Integer> contentID){
        List<String> content = new ArrayList<>();
        contentID.forEach(e -> content.add("word" + e));
        String URL = "http://page" + titleID + ".com";
        try{return new Page("test" + titleID, URL, content);}
        catch (Exception e){fail(e); return null;}
    }

    void setUpIndex(RANKMETHOD method) {
            Database database = new TestDatabase();
            PageRanker.setDatabase(database);
            try{PageRanker.setRankMethod(method);}
            catch (Exception e){fail(e);}
    }

    // ____________________________________________________
    // Tests

    // ____________________________________________________
    // setRankMethod

    @Test void setRankMethod_TFIDFWithNoDatabase_throwsError(){
        PageRanker.setDatabase(null);
        assertDoesNotThrow(() -> PageRanker.setRankMethod(RANKMETHOD.TF));
        assertThrows(Exception.class, () -> {
            PageRanker.setRankMethod(RANKMETHOD.TFIDF);
        });
    }

    // ____________________________________________________
    // rankPage

    @Test void rankPage_queryNotContainedInPage_ranksZero(){
        setUpIndex(RANKMETHOD.TFIDF);
        Page testPage2 = easyPage(2, List.of(1, 2, 2, 2, 2));
        Query q = new Query("word5");
        PageRanker.rankPage(testPage2, q);
        assertEquals(0, testPage2.getPageRank());
    }

    @Test void rankPage_differentPagesAndOneWordQuery_ranksInCorrectOrder(){
        setUpIndex(RANKMETHOD.TFIDF);
        Page testPage2 = easyPage(2, List.of(1, 2, 2, 2, 2));
        Page testPage3 = easyPage(3, List.of(1, 2, 2, 2));
        Page testPage4 = easyPage(4, List.of(1, 2, 2));
        Query q = new Query("word2");
        PageRanker.rankPage(testPage2, q);
        PageRanker.rankPage(testPage3, q);
        PageRanker.rankPage(testPage4, q);
        assert (testPage2.getPageRank() > testPage3.getPageRank());
        assert (testPage3.getPageRank() > testPage4.getPageRank());
    }

    @Test void rankPage_differentPagesAndMultipleWordQuery_ranksCorrectValue() {
        setUpIndex(RANKMETHOD.TFIDF);
        Page testPage1 = easyPage(1, List.of(3, 6, 6, 6, 6, 6, 6, 6));
        Page testPage4 = easyPage(4, List.of(3, 6, 6, 8));
        Page testPage5 = easyPage(5, List.of(6, 6, 3, 9));
        Page testPage6 = easyPage(6, List.of(3, 6, 6, 3, 6, 6, 6, 6, 7));
        Query q = new Query(Set.of(Set.of("word3", "word6", "word9", "word8")));
        PageRanker.rankPage(testPage1, q);
        PageRanker.rankPage(testPage4, q);
        PageRanker.rankPage(testPage5, q);
        PageRanker.rankPage(testPage6, q);
        assertEquals((1.0/8) + (7.0/4), testPage1.getPageRank());
        assertEquals((1.0/4)+1.0+(1.0/4)*(8.0/3), testPage4.getPageRank());
        assertEquals((1.0/4)+1.0+(3.0/4), testPage5.getPageRank());
        assertEquals((2.0/9)+(4.0/3), testPage6.getPageRank());
    }

    @Test void rankPage_differentPagesAndQueryWithOR_ranksCorrectValue() {
        setUpIndex(RANKMETHOD.TFIDF);
        Page testPage1 = easyPage(1, List.of(3, 6, 6, 6, 6, 6, 6, 6));
        Page testPage4 = easyPage(4, List.of(3, 6, 6, 8));
        Page testPage5 = easyPage(5, List.of(6, 6, 3, 9));
        Page testPage6 = easyPage(6, List.of(3, 6, 6, 3, 6, 6, 6, 6, 7));
        Query q = new Query(Set.of(Set.of("word3", "word8"), Set.of("word6", "word9"), Set.of("word1"), Set.of("word6", "word7")));
        PageRanker.rankPage(testPage1, q);
        PageRanker.rankPage(testPage4, q);
        PageRanker.rankPage(testPage5, q);
        PageRanker.rankPage(testPage6, q);
        assertEquals((7.0/4), testPage1.getPageRank());
        assertEquals(1, testPage4.getPageRank());
        assertEquals(1.0+(3.0/4), testPage5.getPageRank());
        assertEquals((4.0/3)+(1.0/9)*(7.0/3), testPage6.getPageRank());
    }

    @Test void rankPage_computationStyleSetToTF_ranksCorrectValue() {
        setUpIndex(RANKMETHOD.TF);
        Page testPage1 = easyPage(1, List.of(3, 6, 6, 6, 6, 6, 6, 6));
        Page testPage4 = easyPage(4, List.of(3, 6, 6, 8));
        Page testPage5 = easyPage(5, List.of(6, 6, 3, 9));
        Page testPage6 = easyPage(6, List.of(3, 6, 6, 3, 6, 6, 6, 6, 7));
        Query q = new Query(Set.of(Set.of("word3", "word6", "word9", "word8")));
        PageRanker.rankPage(testPage1, q);
        PageRanker.rankPage(testPage4, q);
        PageRanker.rankPage(testPage5, q);
        PageRanker.rankPage(testPage6, q);
        assertEquals((1.0/8) + (7.0/8), testPage1.getPageRank());
        assertEquals((1.0/4)+(2.0/4)+(1.0/4), testPage4.getPageRank());
        assertEquals((1.0/4)+(2.0/4)+(1.0/4), testPage5.getPageRank());
        assertEquals((2.0/9)+(6.0/9), testPage6.getPageRank());
    }

}
