package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


import java.nio.file.Files;
import java.nio.file.Paths;

public class InvertedIndexTest {
    private InvertedIndex invertedIndexUnderTest;
    private List<String> listForInvertedIndex;
    private Map<String, Set<Page>> expectedMap;


    @BeforeEach void setup() {
        expectedMap = new HashMap<>();
    }


    @Test void invertedIndex_listWithNoPages_returnsEmptyHashMap() {
        listForInvertedIndex = new ArrayList<>();
        assertThrows(Exception.class, () -> {
            new InvertedIndex(listForInvertedIndex);
        });
    }

    @Test void invertedIndex_listWithNoCorrectPages_returnsEmptyHashMap() {
        try {
            listForInvertedIndex = Files.readAllLines(Paths.get("new_data/test-file-invertedindex1.txt"));
            invertedIndexUnderTest = new InvertedIndex(listForInvertedIndex); 
            assertEquals(new HashMap<>(), invertedIndexUnderTest.getInvertedIndex());
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test void invertedIndex_listWithSomeCorrectPages_returnsProperHashMap() {
        try {
            listForInvertedIndex = Files.readAllLines(Paths.get("new_data/test-file-errors2.txt"));
            invertedIndexUnderTest = new InvertedIndex(listForInvertedIndex); 
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
        listForInvertedIndex = Files.readAllLines(Paths.get("new_data/test-file-errors2.txt"));
        invertedIndexUnderTest = new InvertedIndex(listForInvertedIndex);
        assertEquals(new HashSet<>(), invertedIndexUnderTest.getPages("ockceydockcey"));
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test void getPages_keyWithMappings_returnsSetFromMapping() {
        try {
        listForInvertedIndex = Files.readAllLines(Paths.get("new_data/test-file-errors2.txt"));
        invertedIndexUnderTest = new InvertedIndex(listForInvertedIndex);
        Page page1 = new Page(listForInvertedIndex.subList(3, 8));
        Page page2 = new Page(listForInvertedIndex.subList(13, 17));
        assertEquals(new HashSet<Page>(Set.of(page1, page2)), invertedIndexUnderTest.getPages("word1"));
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
