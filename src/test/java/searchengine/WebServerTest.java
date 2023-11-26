
package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

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


import java.nio.file.Files;
import java.nio.file.Paths;


@TestInstance(Lifecycle.PER_CLASS)
public class WebServerTest {
    WebServer server = null;
    QueryHandler queryHandler;
    Database database;

    @BeforeAll
    void setUp() {
        try {
            Random rnd = new Random();
            while (server == null) {
                try {
                    //String filename = Files.readString(Paths.get("config.txt")).strip();
                    //database = new Database(filename);
                    //queryHandler = new QueryHandler(database);
                    server = new WebServer(rnd.nextInt(60000) + 1024, "new_data/test-file-errors2.txt");
                } catch (BindException e) {
                    // port in use. Try again
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    void tearDown() {
        server.stopServer();;
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
