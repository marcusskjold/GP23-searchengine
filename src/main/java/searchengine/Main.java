package searchengine;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Main {
    static final int PORT = 8080;

    public static void main(final String... args) throws IOException {
        var filename = Files.readString(Paths.get("config.txt")).strip();

        Database db = new Database();
        QueryHandler qh = new QueryHandler();
        WebServer ws = new WebServer(PORT, qh);
      }
    
}
