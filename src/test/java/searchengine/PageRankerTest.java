package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashMap;
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
// import org.junit.platform.engine.discovery.FileSelector;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PageRankerTest {
    private Database database;
    private InvertedIndex index;
    private Set<Page> pagesUnderTest;

    void setUpDatabase(String filePath){
        try {database = new Database(filePath);} 
        catch (IOException e) { e.printStackTrace(); }
    }

    void setUpIndex(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename)); 
            index = new InvertedIndex(lines);
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @BeforeEach void initializeDatabase(){ 
        database = null; 
        pagesUnderTest = null;
    }
    
    @Test void computeTF_PageWithWords_returnCorrectValue() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            Page page = new Page(lines.subList(0, 4));
            double pageRankUnderTest = PageRanker.computeTF("word1", page);
            assertEquals(0.5, pageRankUnderTest);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    @Test void computeIDF_PageWithWords_returnCorrectValue() {
        setUpIndex("new_data/test-file-database1.txt");
        PageRanker.setInvertedIndex(index);
        double pageRankUnderTest = PageRanker.computeIDF("word1");
        assertEquals(0, pageRankUnderTest);
    }

    // Test created to check for non existing terms in TF method
    @Test void computeTF_NonExistentTerm_returnZero() {
        try {
             List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt")); 
             Page page = new Page(lines.subList(0,4)); 
             double pageRankUnderTest = PageRanker.computeTF("nonWord1", page);
             assertEquals(0, pageRankUnderTest);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Test to check for Term in Compute-IDF method which does not exist in the database

    @Test void computeIDF_NonExistentTerm_returnCorrectvalue(){
        setUpIndex("new_data/test-file-database1.txt");
        PageRanker.setInvertedIndex(index); 
        double pageRankUnderTest = PageRanker.computeIDF("nonWord1"); 
        assertTrue (Double.isInfinite(pageRankUnderTest)); 

    }

    @Test void rankPages_SetOfCorrectPages_returnCorrectSortedList() {
        setUpIndex("new_data/test-file-pageRanker1.txt");
        
        
    }


}
