package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
  
  static final int BACKLOG = 0;
  static final Charset CHARSET = StandardCharsets.UTF_8;
  private QueryHandler queryHandler;

  HttpServer server;

  public WebServer(int port, QueryHandler queryHandler) throws IOException {


    // start server. Calls respond and seach 
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

    // Inform user of server
    String msg = " WebServer running on http://localhost:" + port + " ";
    System.out.println("╭"+"─".repeat(msg.length())+"╮");
    System.out.println("│"+msg+"│");
    System.out.println("╰"+"─".repeat(msg.length())+"╯");
  }
  
  void search(HttpExchange io) {
    String searchTerm = io.getRequestURI().getRawQuery().split("=")[1];
    var response;
    for (Page p : queryHandler.search(searchTerm)) {
      response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
        page.get(0).substring(6), page.get(1)));
    }
    var bytes = response.toString().getBytes(CHARSET);
    respond(io, 200, "application/json", bytes);
  }

  // List<List<String>> search(String searchTerm) { //Iterates through the stored pages and try to find one where the word exists? Probably fallibel by now?
  //   var result = new ArrayList<List<String>>();
  //   for (var page : pages) {
  //     if (page.contains(searchTerm)) {
  //       result.add(page);
  //     }
  //   }
  //   return result;
  // }

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