package searchengine;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class WebServerTest {
    WebServer server = null;

    @BeforeAll
    void setUp() {
        try {
            Random rnd = new Random();
            while (server == null) {
                try {
                    Database database = new ImmutableDatabase("new_data/database/multiplePages.txt");
                    PageRanker.setDatabase(database);
                    QueryMatcher.setDatabase(database);  
                } catch (Exception e) { fail(e); }
                server = new WebServer(rnd.nextInt(60000) + 1024);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    void tearDown() {
        server.stopServer();
        server = null;
    }

    private String httpGet(String url) {
        URI uri = URI.create(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            return client.send(request, BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ____________________________________________________
    // Tests

    @Test void lookupWebServer() {
        String baseURL = String.format("http://localhost:%d/search?q=", server.getAddress());
        assertEquals("[{\"url\": \"http://page1.com\", \"title\": \"title1\"}]",
            httpGet(baseURL + "word1"));
        assertEquals("[{\"url\": \"http://page2.com\", \"title\": \"title2\"}, {\"url\": \"http://page1.com\", \"title\": \"title1\"}]", 
            httpGet(baseURL + "word2"));
        assertEquals("[{\"url\": \"http://page3.com\", \"title\": \"title3\"}, {\"url\": \"http://page2.com\", \"title\": \"title2\"}, {\"url\": \"http://page1.com\", \"title\": \"title1\"}]", 
            httpGet(baseURL + "word3"));
        assertEquals("[]", 
            httpGet(baseURL + "word4"));
    }

    @Test void avoidEmptyPagesInWebSearch() {
        String baseURL = String.format("http://localhost:%d/search?q=", server.getAddress());
        assertEquals("[]", 
            httpGet(baseURL + "titleword1"));
        assertEquals("[]", 
            httpGet(baseURL + "titleword2"));
        assertEquals("[]", 
            httpGet(baseURL + "word5"));
    }

    @Test void browsing(){
        String baseURL = String.format("http://localhost:%d", server.getAddress());
        assertDoesNotThrow( () -> httpGet(baseURL + "/"));
        assertDoesNotThrow( () -> httpGet(baseURL + "/code.js"));
        assertDoesNotThrow( () -> httpGet(baseURL + "/favicon"));
        assertDoesNotThrow( () -> httpGet(baseURL + "/style.css"));
        
        
    }

}
