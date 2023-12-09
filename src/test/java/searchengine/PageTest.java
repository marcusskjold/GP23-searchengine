package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PageTest {
    
    //Also tests the equals-method?*
    @Test void Page_ConvertibleList_createsPageWithCorrectURL() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            Page page = new Page(lines.subList(0, 7));
            assertEquals(new Page("title1", "http://page1.com", lines), page);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e);
        } 
    }

    //Test to check for equality in the content-field of the pages, since they are not compared with equals-method
    @Test void Page_ConvertibleList_createsCorrectMapAndTotalTerms() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            Map<String,Integer> frequencyMapUnderTest = new HashMap<>();
            frequencyMapUnderTest.put("word1", 2);
            frequencyMapUnderTest.put("word2", 1);
            frequencyMapUnderTest.put("word3", 1);
            frequencyMapUnderTest.put("word7", 1);
            Page page = new Page(lines.subList(0, 7));
            assertEquals(5, page.getTotalTerms());
            assertEquals(frequencyMapUnderTest.keySet(), page.getWordSet());
            assertEquals(2, page.getFrequency("word1"));
            assertEquals(1, page.getFrequency("word2"));
            assertEquals(0, page.getFrequency("word4"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e);
        } 
    }

    //Maybe split up the one test above?

    //Maybe some error-tests?
    @Test void Page_inputWithMissingURL_throwsError() {
         try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            assertThrows(Exception.class, () -> {
                new Page(lines.subList(7, 11));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test void Page_inputWithTooFewLines_throwsError() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("new_data/test-file-database1.txt"));
            assertThrows(Exception.class, () -> {
                new Page(lines.subList(11, 13));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
