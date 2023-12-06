package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class QueryHandlerTest {


    @Test void SearchTest(){
        try {
            QueryHandler QueryHandlerTest = new QueryHandler("new_data/test-file-database2.txt"); 
            List<Page> search = QueryHandlerTest.search("Test");
            assertNotNull(search);
        } catch (Exception e) {
            fail("fail" + e.getMessage());
        }
    }

    @Test void TestSplitSearchString() {
        try {
            QueryHandler QueryHandlerTest = new QueryHandler("new_data/test-file-database2.txt"); 
            Set<Set<String>> split = QueryHandlerTest.splitSearchString("Test OR Query");
            int expectedResults = 1; 
            assertEquals(expectedResults, split.size());
        } catch (Exception e) {
            e.getMessage(); 
        }
    }

    @Test void TestSplitString(){
        try {
            QueryHandler QueryHandlerTest = new QueryHandler("new_data/test-file-database2.txt");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }



}
