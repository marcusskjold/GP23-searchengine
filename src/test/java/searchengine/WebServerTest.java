package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.BindException;
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
                    Database database = new Database("new_data/test-file-errors1.txt");
                    PageRanker.setDatabase(database);
                    QueryMatcher.setDatabase(database);
                    
                } catch (BindException e) {}
                catch (Exception e){} //TODO handle exception
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

    @Test
    void lookupWebServer() {
        String baseURL = String.format("http://localhost:%d/search?q=", server.getAddress());
        assertEquals("[{\"url\": \"http://page1.com\", \"title\": \"title1\"}, {\"url\": \"http://page2.com\", \"title\": \"title2\"}]", 
            httpGet(baseURL + "word1"));
        assertEquals("[{\"url\": \"http://page1.com\", \"title\": \"title1\"}]",
            httpGet(baseURL + "word2"));
        assertEquals("[{\"url\": \"http://page2.com\", \"title\": \"title2\"}]", 
            httpGet(baseURL + "word3"));
        assertEquals("[]", 
            httpGet(baseURL + "word4"));
    }

    @Test
    void avoidEmptyPagesInWebSearch() {
        String baseURL = String.format("http://localhost:%d/search?q=", server.getAddress());
        assertEquals("[]", 
            httpGet(baseURL + "titleword1"));
        assertEquals("[]", 
            httpGet(baseURL + "titleword2"));
        assertEquals("[]", 
            httpGet(baseURL + "word5"));
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
}
