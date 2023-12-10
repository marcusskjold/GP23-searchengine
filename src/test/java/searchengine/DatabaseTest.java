package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.NoSuchFileException;

public class DatabaseTest {
    private Database databaseUnderTest;
    private Map<String, Set<Page>> expectedIndex;

    void setUpDatabase(String filename) {
        filename = "new_data/database/" + filename;
        try { databaseUnderTest = new Database(filename); }
        catch (Exception e) { 
            System.out.println(e.getMessage());;
            fail(e); 
        }
    }

    void setUpExpectedIndex(){ expectedIndex = new HashMap<String,Set<Page>>(); }

    void addTestEntries() {
        addTestEntry("word1", Set.of(1));
        addTestEntry("word2", Set.of(1,2));
        addTestEntry("word3", Set.of(1,2,3));
    }

    void addTestEntry(String word, Set<Integer> IDs){
        Set<Page> value = new HashSet<>();
        IDs.forEach(e -> value.add(easyPage(e)));
        expectedIndex.put(word, value);
    }

    // TODO make this Page constructor reject pages without content
    Page easyPage(int id){
        String URL = "http://page" + id + ".com";
        return new Page("test", URL, List.of("test"));
    }

    String easyFileName(String filename) { return "new_data/database/" + filename; }

    @BeforeEach void setup() {
        expectedIndex = null;
        databaseUnderTest = null;
    }

    // ____________________________________________________
    // constructor tests

    @Test void constructor_emptyFile_throwsError(){
        assertThrowsExactly(NoSuchElementException.class, () -> {
            databaseUnderTest = new Database(easyFileName("empty.txt"));
        });
    }
    
    @Test void constructor_invalidFileName_throwsError(){
        assertThrowsExactly(NoSuchFileException.class, () -> {
            new Database(easyFileName("invalidFileName"));
        });
    }
    
    @Test void constructor_firstLineOfFileIsWrong_throwsError(){
        assertThrowsExactly(InvalidDataFormatException.class, () -> {
            new Database(easyFileName("firstLineInvalid.txt"));
        });
    }
    
    // TODO Currently, this test fails if data file includes spaces. 
    // This is because page object check for content size before removing empty lines.
    @Test void constructor_fileWithOnlyInvalidPages_throwsError(){
        assertThrowsExactly(InvalidDataFormatException.class, () -> {
            new Database(easyFileName("onlyInvalid.txt"));
        });
    }

    @Test void constructor_fileWithPage_producesIndex(){
        setUpDatabase("onePage.txt");
        assertInstanceOf(Map.class, databaseUnderTest.getInvertedIndex());
    }

    @Test void constructor_fileWithMultiplePages_producesIndex(){
        setUpDatabase("multiplePages.txt");
        assertInstanceOf(Map.class, databaseUnderTest.getInvertedIndex());
    }
    
    @Test void constructor_fileWithAnInvalidPage_producesIndex(){
        setUpDatabase("withInvalid.txt");
        assertInstanceOf(Map.class, databaseUnderTest.getInvertedIndex());
    }

    @Test void constructor_fileWithCorrectPages_producesCorrectIndex(){
        setUpDatabase("multiplePages.txt");
        setUpExpectedIndex();
        Map<String,Set<Page>> actualIndex = databaseUnderTest.getInvertedIndex();
        addTestEntries();
        assertEquals(expectedIndex, actualIndex);
    }

    // ____________________________________________________
    // getPages

    @Test void getPages_keyWithNoMapping_returnsEmptySet() {
        setUpDatabase("multiplePages.txt");
        assertEquals(new HashSet<>(), databaseUnderTest.getPages("unmappedWord"));
    }

    @Test void getPages_wordWithMapping_returnsCorrectPages(){
        setUpDatabase("multiplePages.txt");
        Set<Page> expectedResult = Set.of(easyPage(1), easyPage(2));
        Set<Page> actualResult = databaseUnderTest.getPages("word2");
        assertEquals(expectedResult, actualResult);
    }

    @Test void getPages_wordWithMapping_isImmutable(){
        setUpDatabase("multiplePages.txt");
        Set<Page> originalSet1 = databaseUnderTest.getPages("word2");
        Set<Page> mutatedSet = databaseUnderTest.getPages("word2");
        mutatedSet.remove(easyPage(1));
        mutatedSet.add(easyPage(3));
        Set<Page> originalSet2 = databaseUnderTest.getPages("word2");
        assertEquals(originalSet1, originalSet2);
        assertNotEquals(mutatedSet, originalSet2);
    }

    // ____________________________________________________
    // getIDF
    
    @Test void getIDF_wordInIndex_returnsCorrectValue(){
        setUpDatabase("multiplePages.txt");
        double expectedResult = Math.log((3d/2d));
        double actualResult = databaseUnderTest.getIDF("word2");
        assertEquals(expectedResult, actualResult);
    }

    @Test void getIDF_wordInIndexCalledMultipleTimes_returnsCorrectValue(){
        setUpDatabase("multiplePages.txt");
        double expectedResult = Math.log((3d/2d));
        databaseUnderTest.getIDF("word2");
        double actualResult = databaseUnderTest.getIDF("word2");
        assertEquals(expectedResult, actualResult);
    }

    @Test void getIDF_wordInAllPages_returnsZero(){
        setUpDatabase("multiplePages.txt");
        double expectedResult = 0;
        double actualResult = databaseUnderTest.getIDF("word3");
        assertEquals(expectedResult, actualResult);
    }

    @Test void getIDF_wordNotInIndex_returnsNegOne(){
        setUpDatabase("multiplePages.txt");
        double expectedResult = -1;
        double actualResult = databaseUnderTest.getIDF("unmappedWord");
        assertEquals(expectedResult, actualResult);
    }


}
