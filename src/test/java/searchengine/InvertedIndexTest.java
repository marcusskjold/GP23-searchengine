package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test void invertedIndex_listWithNoPages_returnsEmptyHashMap() {
        listForInvertedIndex = new ArrayList<>();
        invertedIndexUnderTest = new InvertedIndex(listForInvertedIndex);
        assertEquals(new HashMap<>(), invertedIndexUnderTest.getInvertedIndex());
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


}
