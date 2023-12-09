package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseTest {
    private Database invertedIndexUnderTest;
    private List<String> listForInvertedIndex;
    private Map<String, Set<Page>> expectedMap;


    @BeforeEach void setup() {
        expectedMap = new HashMap<>();
    }

    @Test void invertedIndex_listWithNoCorrectPages_returnsEmptyHashMap() {
        try {
            invertedIndexUnderTest = new Database("new_data/test-file-invertedindex1.txt"); 
            assertEquals(new HashMap<>(), invertedIndexUnderTest.getInvertedIndex());
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test void invertedIndex_listWithSomeCorrectPages_returnsProperHashMap() {
        try {
            invertedIndexUnderTest = new Database("new_data/test-file-errors2.txt"); 
            Page page1 = new Page(listForInvertedIndex.subList(3, 8));
            Page page2 = new Page(listForInvertedIndex.subList(13, 17));
            expectedMap.put("word1", new HashSet<>(Set.of(page1, page2)));
            expectedMap.put("word2", new HashSet<>(Set.of(page1)));
            expectedMap.put("word3", new HashSet<>(Set.of(page2)));
            assertEquals(expectedMap.keySet(), invertedIndexUnderTest.getInvertedIndex().keySet());
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test void getPages_keyWithNoMapping_returnsEmptySet() {
        try {
            invertedIndexUnderTest = new Database("new_data/test-file-errors2.txt");
            assertEquals(new HashSet<>(), invertedIndexUnderTest.getPages("ockceydockcey"));
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test void getPages_keyWithMappings_returnsSetFromMapping() {
        try {
            invertedIndexUnderTest = new Database("new_data/test-file-errors2.txt");
            Page page1 = new Page(listForInvertedIndex.subList(3, 8));
            Page page2 = new Page(listForInvertedIndex.subList(13, 17));
            assertEquals(new HashSet<Page>(Set.of(page1, page2)), invertedIndexUnderTest.getPages("word1"));
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
