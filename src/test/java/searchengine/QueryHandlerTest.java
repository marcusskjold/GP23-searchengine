package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QueryHandlerTest {

    @BeforeEach void initializeTestEnvironment(){
        QueryMatcher.setDatabase(null);
        PageRanker.setDatabase(null);
    }

    void setUpDatabase(String filepath){
        try{
        Database database = new Database(filepath);
        QueryMatcher.setDatabase(database);
        PageRanker.setDatabase(database);
        } catch (Exception e){
            fail("fail" + e.getMessage());
        }

    }

    @Test void SearchTest(){
        try {
            setUpDatabase("new_data/test-file-database2.txt"); 
            Set<Page> search = QueryHandler.search("Test");
            assertNotNull(search);
        } catch (Exception e) {
            
        }
    }

    @Test void TestSplitSearchString() {
        try {
            setUpDatabase("new_data/test-file-database2.txt"); 
            Set<Set<String>> split = QueryHandler.splitSearchString("Test OR Query");
            int expectedResults = 1; 
            assertEquals(expectedResults, split.size());
        } catch (Exception e) {
            e.getMessage(); 
        }
    }


}
