package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PageRankerTest {

    private InvertedIndex index;
    private List<Page> expectedResult;

    // Helper methods


    void addTestPage(String URL){
        expectedResult.add(new Page("expectedResult", URL, null));
    }

    void setUpIndex(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename)); 
            index = new InvertedIndex(lines);
            PageRanker.setInvertedIndex(index);
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @BeforeEach void initializeDatabase(){ 
        expectedResult = new ArrayList<>();
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

    @Test void rankPage_givenPage_returnsCorrectValue(){
        setUpIndex("new_data/test-file-pageRanker1.txt");
        List<String> content2 = List.of("word1", "word2", "word2", "word2", "word2");
        Page testPage2 = new Page("expectedResult2", "test2.com", content2);
        List<String> content3 = List.of("word1", "word2", "word2", "word2");
        Page testPage3 = new Page("expectedResult3", "test3.com", content3);
        List<String> content4 = List.of("word1", "word2", "word2");
        Page testPage4 = new Page("expectedResult4", "test4.com", content4);
        
        Query q = new Query("word2");
        // assertEquals(PageRanker.rankPage(testPage1, q), (double) 2.000);
        double result2 = PageRanker.rankPage(testPage2, q); // gives 0.978515
        // assertTrue(result1 == 0.978515);
        double result3 = PageRanker.rankPage(testPage3, q); // gives 0.917358
        double result4 = PageRanker.rankPage(testPage4, q); // gives 0.815429
        assert (result2 > result3);
        assert (result3 > result4);
    }

    @Test void rankPages_setOfCorrectPages_ranksAccordingToPageRankValue(){
        setUpIndex("new_data/test-file-pageRanker1.txt");
        Set<Page> pages = index.getPages("word1");
        Query q = new Query("word2");
        List<Page> rankedPages = PageRanker.rankPages(pages, q);
        List<Double> result = rankedPages.stream().map(p -> PageRanker.rankPage(p, q)).toList();
        List<Double> expectedResult = new ArrayList<>(result);
        expectedResult.sort(null);
        assertEquals(result, expectedResult);
    }
}
