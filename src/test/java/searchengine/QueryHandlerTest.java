package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QueryHandlerTest {
    

    @BeforeEach void initializeTestEnvironment(){
        QueryMatcher.setDatabase(null);
        PageRanker.setDatabase(null);
    }

    void setUpDatabase(String filepath){
        try{
        Database database = new ImmutableDatabase(filepath);
        QueryMatcher.setDatabase(database);
        PageRanker.setDatabase(database);
        } catch (Exception e){
            fail("fail" + e.getMessage());
        }

    }

    Page easyPage(int titleID, List<Integer> contentID){
        List<String> content = new ArrayList<>();
        contentID.forEach(e -> content.add("word" + e));
        String URL = "http://page" + titleID + ".com";
        try{return new Page("test" + titleID, URL, content);}
        catch (Exception e){fail(e); return null;}
    }

    @Test void search_searchStringNotPresent_returnEmptySet(){
        try {
            setUpDatabase("new_data/test-file-database2.txt"); 
            Set<Page> searchUnderTest = QueryHandler.search("bobobobo");
            assertTrue(searchUnderTest.isEmpty());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test void search_singleWordSearchString_returnsCorrectList() {
        try {
            setUpDatabase("new_data/test-file-database2.txt"); 
            Set<Page> searchUnderTest = QueryHandler.search("word2");
            Page page1 = easyPage(1, List.of(1, 2));
            Page page2 = easyPage(2, List.of(1, 2, 3));
            Page page3 = easyPage(3, List.of(2, 3));
            Query query = new Query("word2");
            PageRanker.rankPage(page1, query);
            PageRanker.rankPage(page2, query);
            PageRanker.rankPage(page3, query);
            Set<Page> returnedSet = new TreeSet<>(Set.of(page1, page2, page3));
            assertEquals(returnedSet ,searchUnderTest);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test void search_MultipleWordSearchString_returnsCorrectList() {
        try {
            setUpDatabase("new_data/test-file-database2.txt"); 
            Set<Page> searchUnderTest = QueryHandler.search("word1%20word2");
            Page page1 = easyPage(1, List.of(1, 2));
            Page page2 = easyPage(2, List.of(1, 2, 3));
            Query query = new Query(Set.of(Set.of("word1", "word2")));
            PageRanker.rankPage(page1, query);
            PageRanker.rankPage(page2, query);
            Set<Page> returnedSet = new TreeSet<>(Set.of(page1, page2));
            assertEquals(returnedSet ,searchUnderTest);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test void search_SearchStringWithOR_returnsCorrectList() {
        try {
            setUpDatabase("new_data/test-file-database2.txt"); 
            Set<Page> searchUnderTest = QueryHandler.search("word1%20word3%20OR%20word3%20word100%20OR%20word55%20%20%20");
            Page page2 = easyPage(2, List.of(1, 2, 3));
            Page page4 = easyPage(4, List.of(3, 100));
            Query query = new Query(Set.of(Set.of("word1","word3"), Set.of("word3", "word100"), Set.of("word55")));
            PageRanker.rankPage(page2, query);
            PageRanker.rankPage(page4, query);
            Set<Page> returnedSet = new TreeSet<>(Set.of(page2, page4));
            assertEquals(returnedSet ,searchUnderTest);
        } catch (Exception e) {
            fail(e);
        }
    }



    //@Test void TestSplitSearchString() {
    //    try {
    //        setUpDatabase("new_data/test-file-database2.txt"); 
    //        Set<Set<String>> split = QueryHandler.splitSearchString("Test%20OR%20Query");
    //        int expectedResults = 2; 
    //        assertEquals(expectedResults, split.size());
    //    } catch (Exception e) {
    //        e.getMessage(); 
    //    }
    //}


}
