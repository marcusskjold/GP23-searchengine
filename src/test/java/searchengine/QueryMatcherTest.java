package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class QueryMatcherTest {
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
        try {
            QueryMatcher.setDatabase(new ImmutableDatabase(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){ 
            // TODO handle exception
        }
    }

    void addExpectedResult(String URL){
        Page page = new Page("expectedResult", URL, null);
        expectedResults.add(page);
    }

    @BeforeEach
    void setUp() {
        QueryMatcher.setDatabase(null);
        expectedResults = new HashSet<>();
    }

    @Test void matchQuery_queryWordNotContainedInPages_returnEmptySet() {
        setUpDatabase("new_data/test-file-errors2.txt");
        Query q = makeOneWordQuery("bobobo");
        Set<Page> actualResult = QueryMatcher.matchQuery(q);
        assertEquals(expectedResults, actualResult);
    }

    @Test void matchQuery_queryWordContainedInOnePage_returnsPage() {
        setUpDatabase("new_data/test-file-errors2.txt");
        addExpectedResult("http://page1.com");
        Query q = makeOneWordQuery("word2");

        Set<Page> actualResults = QueryMatcher.matchQuery(q);
        assertEquals(expectedResults, actualResults);
    }

    @Test void matchQuery_queryWordContainedInMorePages_returnsAllPages() {
        setUpDatabase("new_data/test-file-errors2.txt");
        addExpectedResult("http://page1.com");
        addExpectedResult("http://page2.com");
        Query q = makeOneWordQuery("word1");

        Set<Page> actualResults = QueryMatcher.matchQuery(q);
        assertEquals(expectedResults, actualResults);
    }
}
