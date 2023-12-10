package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
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
        return new Query(word);
        // Set<Set<String>> orSET = new HashSet<Set<String>>();
        // Set<String> andSET = new HashSet<String>();
        // andSET.add(word);
        // orSET.add(andSET);
        // Query q = new Query(orSET);
        // return q;
    }

    void setUpDatabase(String filePath){
        try {
            QueryMatcher.setDatabase(new ImmutableDatabase("new_data/queryMatcher/" + filePath));
        } catch (Exception e) { fail(e); }
    }

    void addExpectedResult(String URL){
        try{
            Page page = new Page("expectedResult", URL, List.of("test"));
            expectedResults.add(page);
        } catch (Exception e) { fail(e); }
    }

    @BeforeEach
    void setUp() {
        QueryMatcher.setDatabase(null);
        expectedResults = new HashSet<>();
    }

    @Test void matchQuery_queryWordNotContainedInPages_returnEmptySet() {
        setUpDatabase("database1.txt");
        Query q = makeOneWordQuery("bobobo");
        Set<Page> actualResult = QueryMatcher.matchQuery(q);
        assertEquals(expectedResults, actualResult);
    }

    @Test void matchQuery_queryWithFilter_allowsResultsThrough() {
        setUpDatabase("database1.txt");
        addExpectedResult("http://page1.com");
        Query q = makeOneWordQuery("word1");
        q.setURLFilter("page");
        Set<Page> actualResult = QueryMatcher.matchQuery(q);
        assertEquals(expectedResults, actualResult);
    }

    @Test void matchQuery_queryWithFilter_filtersresults() {
        setUpDatabase("database1.txt");
        Query q = makeOneWordQuery("word1");
        q.setURLFilter("page2");
        Set<Page> actualResult = QueryMatcher.matchQuery(q);
        assertEquals(expectedResults, actualResult);
    }


    @Test void matchQuery_noDatabase_returnEmptySet() {
        Query q = makeOneWordQuery("word1");
        Set<Page> actualResult = QueryMatcher.matchQuery(q);
        assertEquals(expectedResults, actualResult);
    }



    @Test void matchQuery_queryWordContainedInOnePage_returnsPage() {
        setUpDatabase("database1.txt");
        addExpectedResult("http://page1.com");
        Query q = makeOneWordQuery("word1");

        Set<Page> actualResults = QueryMatcher.matchQuery(q);
        assertEquals(expectedResults, actualResults);
    }

    @Test void matchQuery_queryWordContainedInMorePages_returnsAllPages() {
        setUpDatabase("database1.txt");
        addExpectedResult("http://page1.com");
        addExpectedResult("http://page2.com");
        Query q = makeOneWordQuery("word2");

        Set<Page> actualResults = QueryMatcher.matchQuery(q);
        assertEquals(expectedResults, actualResults);
    }
}
