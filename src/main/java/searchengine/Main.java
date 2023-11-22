package searchengine;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Main {
    static final int PORT = 8080;

    public static void main(final String... args) throws IOException {
        var filename = Files.readString(Paths.get("config.txt")).strip();

        //Database db = new Database(filename);
        //QueryHandler qh = new QueryHandler(filename);
        new WebServer(PORT, filename);
      }
    
}
