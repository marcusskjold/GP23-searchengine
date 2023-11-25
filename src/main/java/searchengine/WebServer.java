package searchengine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
  
  static final int BACKLOG = 0;
  static final Charset CHARSET = StandardCharsets.UTF_8;
  private QueryHandler queryHandler;

  private HttpServer server;

  //The Webserver initially initializes a Queryhandler-object, which in turn initializes a Database-object
  public WebServer(int port, String filename) throws IOException {
    queryHandler = new QueryHandler(filename);
    setupServer(port);
    printServerAddress(port);
  }

  private void printServerAddress(int port) {
    String msg = " WebServer running on http://localhost:" + port + " ";
    System.out.println("╭"+"─".repeat(msg.length())+"╮");
    System.out.println("│"+msg+"│");
    System.out.println("╰"+"─".repeat(msg.length())+"╯");
  }

  private void setupServer(int port) throws IOException {
    server = HttpServer.create(new InetSocketAddress(port), BACKLOG);
    server.createContext("/", io -> respond(io, 200, "text/html", getFile("web/index.html")));
    server.createContext("/search", io -> search(io));
    server.createContext(
        "/favicon.ico", io -> respond(io, 200, "image/x-icon", getFile("web/favicon.ico")));
    server.createContext(
        "/code.js", io -> respond(io, 200, "application/javascript", getFile("web/code.js")));
    server.createContext(
        "/style.css", io -> respond(io, 200, "text/css", getFile("web/style.css")));
    server.start();
  }
  
  /**
   * Converts the io into a searchTerm. 
   * Sends the searchTerm to the queryHandler.
   * Generates a response containing formatted links from the returned list of pages.
   * @param io the HttpExchange to generate a searchTerm from
   */
  private void search(HttpExchange io) {
    String searchTerm = io.getRequestURI().getRawQuery().split("=")[1];
    List<String> response = new ArrayList<String>();
    List<Page> pages = null; 

    try {
      pages = queryHandler.search(searchTerm);
    } catch (QueryStringException e) {
      e.printStackTrace();
      // TODO: handle exception
    }
    for (Page p : pages) {
      response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
        p.getURL(), p.getTitle()));
    }
    byte[] bytes = response.toString().getBytes(CHARSET);
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
    } finally {
      io.close();
    }
  }

  public void stopServer(){
    server.stop(0);
  }

  public int getAddress() {
    return server.getAddress().getPort();
  }
}