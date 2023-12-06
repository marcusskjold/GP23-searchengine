package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;

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
