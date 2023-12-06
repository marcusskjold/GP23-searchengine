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

public class PageTest {
    
    @Test void Page_ConvertibleList_createsCorrectPageObject() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            Page page = new Page(lines.subList(0, 4));
            assertEquals(new Page("title1", "http://page1.com", lines), page);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    //Test to check for equality in the content-field of the pages, since they are not compared with equals-method
    @Test void Page_ConvertibleList_createsCorrectContent() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            Page page = new Page(lines.subList(0, 4));
            Page expectedPage = new Page("title1",
                                           "http://page1.com", 
                                               lines.subList(2, 4));
            assertEquals(expectedPage.getContent(),page.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
