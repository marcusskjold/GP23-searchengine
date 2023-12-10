package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PageRankerTest {

    private Database database;
    private List<Page> expectedResult;

    class TestDatabase implements Database{
        TestDatabase(){}
        public Set<Page> getPages(String word){ return null;}
        public double getIDF(String word){
            double rarity = Integer.parseInt(word.substring(word.length()-1));
            return rarity/3;}
    }



    // Helper methods

    Page easyPage(int titleID, List<Integer> contentID){
        List<String> content = new ArrayList<>();
        contentID.forEach(e -> content.add("word" + e));
        String URL = "http://page" + titleID + ".com";
        try{return new Page("test" + titleID, URL, content);}
        catch (Exception e){fail(e); return null;}
    }


    void setUpIndex(String filename) {
            database = new TestDatabase();
            PageRanker.setDatabase(database);
    }

    @BeforeEach void initializeDatabase(){
        expectedResult = new ArrayList<>();
        
    }



    @Test void rankPage_differentPagesAndOneWordQuery_ranksInCorrectOrder(){
        setUpIndex("new_data/test-file-pageRanker1.txt");
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

    @Test void rankPage_differentPagesAndMultipleWordQuery_ranksInCorrectOrder() {
        setUpIndex("new_data/test-file-pageRanker1.txt");
        Page testPage2 = easyPage(2, List.of(1, 2, 2, 2, 2));
        Page testPage3 = easyPage(3, List.of(1, 2, 2, 2));
        Page testPage4 = easyPage(4, List.of(1, 2, 2));
        Query q = new Query(Set.of(Set.of("word1", "word2")));
        PageRanker.rankPage(testPage2, q);
        PageRanker.rankPage(testPage3, q);
        PageRanker.rankPage(testPage4, q);
        
    }

    // RankPages

    // @Test void rankPages_SetOfCorrectPages_returnCorrectSortedList() {
    //     addTestPage("http://page1.com");
    //     addTestPage("http://page2.com");
    //     addTestPage("http://page3.com");
    //     addTestPage("http://page4.com");
    //     addTestPage("http://page5.com");

    //     setUpIndex("new_data/test-file-pageRanker1.txt");
    //     Set<Page> pages = index.getPages("word1");
    //     Query q = new Query("word2");
    //     List<Page> results = PageRanker.rankPages(pages, q);

    //     assertEquals(expectedResult, results);
    // }

    // SetInvertedIndex
    // TODO

    // computeTF
    // Not tested as it is a private method
    // Method made private as of 2023-12-06
    // All tests below passed at that point
    
    // @Test void computeTF_PageWithWords_returnCorrectValue() {
    //     try {
    //         List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
    //         Page page = new Page(lines.subList(0, 4));
    //         double pageRankUnderTest = PageRanker.computeTF("word1", page);
    //         assertEquals(0.5, pageRankUnderTest);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     } 
    // }

    // @Test void computeTF_NonExistentTerm_returnZero() {
    //     try {
    //          List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt")); 
    //          Page page = new Page(lines.subList(0,4)); 
    //          double pageRankUnderTest = PageRanker.computeTF("nonWord1", page);
    //          assertEquals(0, pageRankUnderTest); 
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    // computeIDF
    // Not tested as it is a private method
    // Method made private as of 2023-12-06. 
    // All tests below passed at that point

    // @Test void computeIDF_PageWithWords_returnCorrectValue() {
    //     setUpIndex("new_data/test-file-database1.txt");
    //     PageRanker.setInvertedIndex(index);
    //     double pageRankUnderTest = PageRanker.computeIDF("word1");
    //     assertEquals(0, pageRankUnderTest);
    // }

    // @Test void computeIDF_NonExistentTerm_returnCorrectvalue(){
    //     setUpIndex("new_data/test-file-database1.txt");
    //     PageRanker.setInvertedIndex(index); 
    //     double pageRankUnderTest = PageRanker.computeIDF("nonWord1"); 
    //     assertTrue (Double.isInfinite(pageRankUnderTest)); 
    // }

    // computeTFIDF
    // Not tested as it is a private method

    // rankPage

    

    // @Test void rankPages_setOfCorrectPages_ranksAccordingToPageRankValue(){
    //     setUpIndex("new_data/test-file-pageRanker1.txt");
    //     Set<Page> pages = index.getPages("word1");
    //     Query q = new Query("word2");
    //     List<Page> rankedPages = PageRanker.rankPages(pages, q);
    //     List<Double> result = rankedPages.stream().map(p -> PageRanker.rankPage(p, q)).toList();
    //     List<Double> expectedResult = new ArrayList<>(result);
    //     expectedResult.sort(null);
    //     assertEquals(result, expectedResult);
    // }


    // @Test void rankPages_setOfPagesContainingSimilarWordFrequency_ranksPages(){ //
    //     setUpIndex("new_data/test-file-pageRanker1.txt");
    //     try {
    //         List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-pageRanker1.txt")); 
    //         Set<Page> pages = index.getPages("word2");
    //         Query q = new Query("word2");
    //         Page page1 = new Page(lines.subList(15, 25));
    //         Page page2 = new Page(lines.subList(8, 15));
    //         Page page3 = new Page(lines.subList(25, 31));
    //         Page page4 = new Page(lines.subList(0, 5));
    //         List<Page> expectedResult = new ArrayList<>(List.of(page1, page2, page3, page4));
    //         List<Page> rankList = PageRanker.rankPages(pages, q);
    //         Collections.reverse(rankList);
    //         assertEquals(expectedResult, rankList);
    //     } 
    //     catch (Exception e){
    //         e.printStackTrace();
    //     }
    // }
}
