package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PageTest {

    class TestDatabase implements Database{
        TestDatabase(){}
        public Set<Page> getPages(String word){ return null;}
        public double getIDF(String word){
            double rarity = Integer.parseInt(word.substring(word.length()-1));
            return rarity/3;}
    }


    Page easyPage(String fileName, int start, int end){
        try { 
            Path path = Paths.get("new_data/page/" + fileName);
            return new Page(Files.readAllLines(path).subList(start, end)); 
        }
        catch (Exception e) { 
            e.printStackTrace();
            fail(e); 
            return null; }
    }

    Page easyPage(int id){
        String URL = "http://page" + id + ".com";
        try { return new Page("test", URL, List.of("test1")); }
        catch (Exception e) { fail(e); return null; }
    }

    Map<String, Integer> generateTestFrequencyMap() {
        Map<String,Integer> frequencyMapUnderTest = new HashMap<>();
        frequencyMapUnderTest.put("word1", 2);
        frequencyMapUnderTest.put("word2", 1);
        frequencyMapUnderTest.put("word3", 1);
        frequencyMapUnderTest.put("word7", 1);
        return frequencyMapUnderTest;
    }
    // ____________________________________________________
    // TODO Constructor

    // ____________________________________________________
    // getTotalTerms
    
    @Test void getTotalTerms_returnCorrectAmount() {
        Page page = easyPage("database1.txt", 0, 7);
        assertEquals(5, page.getTotalTerms());
    }

    // ____________________________________________________
    // getFrequency

    //Test to check for equality in the content-field of the pages, since they are not compared with equals-method
    @Test void getFrequency_returnsCorrectFrequencies() {
        Page page = easyPage("database1.txt", 0, 7);
        Map<String, Integer> frequencyMapUnderTest = generateTestFrequencyMap();
        assertEquals(frequencyMapUnderTest.keySet(), page.getWordSet());
        assertEquals(2, page.getFrequency("word1"));
        assertEquals(1, page.getFrequency("word2"));
        assertEquals(1, page.getFrequency("word3"));
        assertEquals(0, page.getFrequency("word4"));
        assertEquals(1, page.getFrequency("word7"));
    }

    // ____________________________________________________
    // TODO getWordSet

    // ____________________________________________________
    // compareTo

    @Test void compareTo_DifferentPage_byDefault_doesNotReturnZero() {
        Page page1 = easyPage(1);
        Page page2 = easyPage(2);
        assert (page1.compareTo(page2) != 0);
    }

    @Test void compareTo_identicalPage_byDefault_ReturnZero() {
        Page page1 = easyPage(1);
        Page page2 = easyPage(1);
        assert (page1.compareTo(page2) == 0);
    }
    @Test void compareTo_samePage_byDefault_ReturnZero() {
        Page page1 = easyPage(1);
        assert (page1.compareTo(page1) == 0);
    }

    @Test void compareTo_samePageWithDifferentRank_doesNotReturnZero() {
        Page page1 = easyPage(1);
        assert (page1.getPageRank() == 0d);
        PageRanker.setDatabase(new TestDatabase());
        PageRanker.rankPage(page1, new Query("test1"));
        assert (page1.getPageRank() >= 0d);
        Page page2 = easyPage(1);
        assert (page1.compareTo(page2) <= 0);
        assert (page2.compareTo(page1) >= 0);
    }

    // ____________________________________________________
    // equals, implicit test of hashCodes

    @Test void equals_identicalPages_areEqual() {
        Page page1 = easyPage("database1.txt", 0, 8);
        Page page2 = easyPage("database1.txt", 0, 8);
        assertEquals(page1, page2);
    }

    @Test void equals_samePages_isEqual() {
        Page page1 = easyPage("database1.txt", 0, 8);
        assertEquals(page1, page1);
    }

    @Test void equals_pagesWithSameURLandDifferentContent_areEqual() {
        Page page1 = easyPage("database1.txt", 0, 8);
        Page page2 = easyPage(1);
        assertEquals(page1, page2);
    }

    @Test void equals_pagesDifferentURLandSameContent_areNotEqual() {
        Page page1 = easyPage(1);
        Page page2 = easyPage(2);
        assertNotEquals(page1, page2);
    }

    @Test void equals_pageAndOtherClass_areNotEqual() {
        Page page1 = easyPage(1);
        assertNotEquals(page1, "notPage1");
    }

    @Test void equals_pageAndNull_areNotEqual() {
        Page page1 = easyPage(1);
        assertNotEquals(page1, null);
    }

    @Test void equals_pagesWithSameURLandDifferentRank_areEqual() {
        Page page1 = easyPage(1);
        Page page2 = easyPage(2);
        assert (page1.getPageRank() == 0d);
        assert (page2.getPageRank() == 0d);
        PageRanker.setDatabase(new TestDatabase());
        PageRanker.rankPage(page1, new Query("test1"));
        assert (page1.getPageRank() != 0);
        assertNotEquals(page1, page2);
    }

    //Maybe some error-tests?
    @Test void Constructor_inputWithMissingURL_throwsError() {
         try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            assertThrows(Exception.class, () -> {
                new Page(lines.subList(7, 11));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test void Constructor_inputWithTooFewLines_throwsError() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            assertThrows(Exception.class, () -> {
                new Page(lines.subList(11, 13));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
