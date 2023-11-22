package searchengine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
  
  static final int BACKLOG = 0;
  static final Charset CHARSET = StandardCharsets.UTF_8;
  private QueryHandler queryHandler;

  HttpServer server;

  //The Webserver initially initializes a Queryhandler-object, which in turn initializes a Database-object
  public WebServer(int port, String filename) throws IOException {
    queryHandler = new QueryHandler(filename);
    setupServer(port);
    printServerAdress(port);
  }

  private void printServerAdress(int port) {
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
   * Generates a response containing formatted links from the returned PageList.
   * @param io the HttpExchange to generate a searchTerm from
   */
  public void search(HttpExchange io) {
    String searchTerm = io.getRequestURI().getRawQuery().split("=")[1];
    var response = new ArrayList<String>();
    PageList pages = null; 

    try {
      pages = queryHandler.search(searchTerm);
    } catch (QueryStringException e) {
      e.printStackTrace();
      // TODO: handle exception
    }
    for (Page p : pages.getPageList()) {
      response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
        p.getURL(), p.getTitle()));
    }
    var bytes = response.toString().getBytes(CHARSET);
    respond(io, 200, "application/json", bytes);
  }

  byte[] getFile(String filename) {
    try {
      return Files.readAllBytes(Paths.get(filename));
    } catch (IOException e) {
      e.printStackTrace();
      return new byte[0];
    }
  }

  void respond(HttpExchange io, int code, String mime, byte[] response) {
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
}