package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database {
    //Have one field of type Pagelist, which will be the pages of the Database-object
    private PageList pages;
    

    public Database(String filename) throws IOException {
    //Initializes the pages-field
        pages = new PageList();
    // reads the files and adds them to the pages-field 
    // basically done in the same way as with the initial implementation
    try {
      List<String> lines = Files.readAllLines(Paths.get(filename)); 
      var lastIndex = lines.size();
      for (var i = lines.size() - 1; i >= 0; --i) {
        if (lines.get(i).startsWith("*PAGE")) {
            if(lines.subList(i, lastIndex).size()>2) { //If not empty/errouneous page
            Page page = new Page(lines.subList(i, lastIndex).get(1) , lines.subList(i, lastIndex).get(0).substring(6), lines.subList(i, lastIndex)); //creates a Page-object with the specified information for the fields
            pages.addPage(page); //Adds it to the pages-field
            }
          lastIndex = i;
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Collections.reverse(pages.getPageList()); //Reverses the field
    }

    public PageList search(String searchTerm) { //Iterates through the stored pages and try to find one where the word exists
        PageList result = new PageList();
        if(pages!=null) { //Checks that pages are not empty. Probably not necessary
        for (Page page : pages.getPageList()) {
          if (page.getContent().contains(searchTerm)) {
            result.addPage(page);
          }
        }
    }
        return result;
      }
}
