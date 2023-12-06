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
    


    @Test void computeTF_PageWithWords_returnCorrectValue() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            Page page = Database.convertToPage(lines.subList(0, 4));
            double pageRankUnderTest = PageRanker.computeTF("word1", page);
            assertEquals(0.5, pageRankUnderTest);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    @Test void computeIDF_PageWithWords_returnCorrectValue() {
        try {
            Database databaseUnderTest = new Database("new_data/test-file-database1.txt");
            double pageRankUnderTest = PageRanker.computeIDF(databaseUnderTest, "word1");
            assertEquals(0, pageRankUnderTest);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }


}
