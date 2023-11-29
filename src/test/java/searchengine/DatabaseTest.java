package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


import java.nio.file.Files;
import java.nio.file.Paths;

@TestInstance(Lifecycle.PER_CLASS)
public class DatabaseTest {
    private Database databaseUnderTest;
    private HashSet<Page> results;
    
    public Query makeOneWordQuery(String word){
        Set<Set<String>> orSET = new HashSet<Set<String>>();
        Set<String> andSET = new HashSet<String>();
        andSET.add(word);
        orSET.add(andSET);
        Query q = new Query(word);
        q.TESTaddQuery(orSET);
        return q;
    }

    @BeforeEach //Used BeforeEach so that results will be empty at the start of each test.
    void setUp() {
        try {databaseUnderTest = new Database("new_data/test-file-errors2.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        results = new HashSet<>();
    }

    
    //@Test
    //void database_inputWithErroneousPages_StoreOnlyCorrectPagesAtInitialization() {
    //        assertEquals(2, databaseUnderTest.getNumberOfPages());
    //}

    @Test
    void search_queryWordNotContainedInPages_returnEmptyList() {
        Query q = makeOneWordQuery("bobobo");
        assertEquals(results, databaseUnderTest.matchQuery(q));
    }

    //Reads lines to convert them to a page and adds them to the result list. Compares with the list returned by the search-method
    @Test
    void search_queryWordContainedInOnePage_returnListWithPage() {
        try {
        List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
        Page page = Database.convertToPage(lines.subList(0, 4));
        results.add(page);
        // assertTrue(results.get(0).equals(databaseUnderTest.search("word2").get(0)));
        assertEquals(results, databaseUnderTest.matchQuery(makeOneWordQuery("word2")));
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    @Test
    void search_queryWordContainedInMorePages_returnListWithAllPagesInCorrectOrder() {
        try {
        List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-errors2.txt"));
        Page page1 = Database.convertToPage(lines.subList(2, 6));
        Page page2 = Database.convertToPage(lines.subList(11, 15));
        results.add(page1);
        results.add(page2);
        //assertTrue(results.get(0).equals(databaseUnderTest.search("word2").get(0)));
        assertEquals(results, databaseUnderTest.matchQuery(makeOneWordQuery("word1")));
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    @Test
    void convertToPage_ConvertibleList_createsCorrectPageObject() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            Page page = Database.convertToPage(lines.subList(0, 4));
            assertEquals(new Page("title1", "http://page1.com", lines), page);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    //Test to check for equality in the content-field of the pages, since they are not compared with equals-method
    @Test
    void convertToPage_ConvertibleList_createsCorrectContent() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            Page page = Database.convertToPage(lines.subList(0, 4));
            assertEquals(new Page("title1", "http://page1.com", lines.subList(2, 4)).getContent(), page.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

//     @Test
//     void testInvertedIndex(){

//     }

}
