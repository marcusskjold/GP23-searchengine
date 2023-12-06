package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.MethodSource;
// import org.junit.jupiter.params.provider.Arguments;

import java.nio.file.Files;
import java.nio.file.Paths;

@TestInstance(Lifecycle.PER_CLASS)
public class DatabaseTest {
    private Database databaseUnderTest;
    private HashSet<Page> expectedResults;
    
    Query makeOneWordQuery(String word){
        Set<Set<String>> orSET = new HashSet<Set<String>>();
        Set<String> andSET = new HashSet<String>();
        andSET.add(word);
        orSET.add(andSET);
        Query q = new Query(orSET);
        return q;
    }

    void setUpDatabase(String filePath){
        try {databaseUnderTest = new Database(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void addExpectedResult(String URL){
        Page page = new Page("expectedResult", URL, null);
        expectedResults.add(page);
    }

    @BeforeEach
    void setUp() {
        databaseUnderTest = null;
        expectedResults = new HashSet<>();
    }

    @Test void matchQuery_queryWordNotContainedInPages_returnEmptySet() {
        setUpDatabase("new_data/test-file-errors2.txt");
        Query q = makeOneWordQuery("bobobo");
        Set<Page> actualResult = databaseUnderTest.matchQuery(q);
        assertEquals(expectedResults, actualResult);
    }

    @Test void matchQuery_queryWordContainedInOnePage_returnsPage() {
        setUpDatabase("new_data/test-file-errors2.txt");
        addExpectedResult("http://page1.com");
        Query q = makeOneWordQuery("word2");

        Set<Page> actualResults = databaseUnderTest.matchQuery(q);
        assertEquals(expectedResults, actualResults);
    }

    @Test void matchQuery_queryWordContainedInMorePages_returnsAllPages() {
        setUpDatabase("new_data/test-file-errors2.txt");
        addExpectedResult("http://page1.com");
        addExpectedResult("http://page2.com");
        Query q = makeOneWordQuery("word1");

        Set<Page> actualResults = databaseUnderTest.matchQuery(q);
        assertEquals(expectedResults, actualResults);
    }

    @Test void convertToPage_ConvertibleList_createsCorrectPageObject() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            Page page = InvertedIndex.convertToPage(lines.subList(0, 4));
            assertEquals(new Page("title1", "http://page1.com", lines), page);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    //Test to check for equality in the content-field of the pages, since they are not compared with equals-method
    @Test void convertToPage_ConvertibleList_createsCorrectContent() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            Page page = InvertedIndex.convertToPage(lines.subList(0, 4));
            Page expectedPage = new Page("title1",
                                           "http://page1.com", 
                                               lines.subList(2, 4));
            assertEquals(expectedPage.getContent(),page.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

}
