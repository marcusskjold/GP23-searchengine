package searchengine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/** The webserver is the interface between the user of the application and the database.
 * The WebServer only interacts with the QueryHandler.
 */
public class WebServer {
    static final int BACKLOG = 0;
    static final Charset CHARSET = StandardCharsets.UTF_8;
    private HttpServer server;

    /** Opens a new server and prints it adress to the terminal
     * @param port
     * @throws IOException
     */
    public WebServer(int port) throws IOException {
        setupServer(port);
        printServerAddress(port);
    }

    /** Stops the server
     * Used for testing
     */
    public void stopServer(){
        server.stop(0);
    }

    /** Used for testing
     * @return the port adress of the server
     */
    protected int getAddress() {
        return server.getAddress().getPort();
    }

    private void printServerAddress(int port) {
        String msg = " WebServer running on http://localhost:" + port + " ";
        System.out.println("╭"+"─".repeat(msg.length())+"╮");
        System.out.println("│"+msg+"│");
        System.out.println("╰"+"─".repeat(msg.length())+"╯");
    }

    private void setupServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), BACKLOG);
        server.createContext(
            "/", io -> respond(io, 200, "text/html", getFile("web/index.html")));
        server.createContext(
            "/search", io -> search(io));
        server.createContext(
            "/favicon.ico", io -> respond(io, 200, "image/x-icon", getFile("web/favicon.ico")));
        server.createContext(
            "/code.js", io -> respond(io, 200, "application/javascript", getFile("web/code.js")));
        server.createContext(
            "/style.css", io -> respond(io, 200, "text/css", getFile("web/style.css")));
        server.start();
    }
  
    /** Converts the io into a searchTerm. 
     * Sends the searchTerm to the queryHandler.
     * Generates a response containing formatted links from the returned list of pages.
     * @param io the HttpExchange to generate a searchTerm from
     */
    private void search(HttpExchange io) {
        String searchTerm = io.getRequestURI().getRawQuery().split("=")[1];
        List<String> response = new ArrayList<String>();
        Set<Page> pages = null; 
        pages = QueryHandler.search(searchTerm);
        
        if (pages.size() == 0) System.out.println("No web page contains the query word.");
        else for (Page p : pages) {
            response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
            p.getURL(), p.getTitle()));
            // response.add(String.format("{\"url\": \"%s\", \"title\": \"%s - %f\"}",
            // p.getURL(), p.getTitle(), p.getPageRank()));
        }

        var bytes = response.toString().getBytes(CHARSET);
        respond(io, 200, "application/json", bytes);
    }

    private byte[] getFile(String filename) {
        try {
        return Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
        e.printStackTrace();
        return new byte[0];
        }
    }

    private void respond(HttpExchange io, int code, String mime, byte[] response) {
        try {
        io.getResponseHeaders()
            .set("Content-Type", String.format("%s; charset=%s", mime, CHARSET.name()));
        io.sendResponseHeaders(200, response.length);
        io.getResponseBody().write(response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        io.close();
        }
    }
}