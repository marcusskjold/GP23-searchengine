package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database {
    //Will be of type PageList, when that is defined
    private PageList pages;
    

    public Database(String filename) throws IOException {
        pages = new PageList();
    // constructs the page-list of the database as initially defined (code from original application)
    try {
        //System.out.println("Reading file: " + filename);
      List<String> lines = Files.readAllLines(Paths.get(filename)); //Something here?
      //System.out.println("Number of lines read: " + lines.size());
      var lastIndex = lines.size();
      for (var i = lines.size() - 1; i >= 0; --i) {
        if (lines.get(i).startsWith("*PAGE")) {
            if(lines.subList(i, lastIndex).size()>2) { //If not empty/errouneous page
            Page page = new Page(lines.subList(i, lastIndex).get(1) , lines.subList(i, lastIndex).get(0).substring(6), lines.subList(i, lastIndex));
          pages.addPage(page);
          //System.out.println("Added page: " + page.getTitle() + "with URL:" + page.getURL()); A debugging statement
          }
          lastIndex = i;
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Collections.reverse(pages.getPageList());
    }

    public PageList search(String searchTerm) { //Iterates through the stored pages and try to find one where the word exists? Probably fallibel by now?
        //var result = new ArrayList<List<String>>();
        PageList result = new PageList();
        if(pages!=null) {
            //ArrayList<Page>() pl = pages.getPageList();
        for (Page page : pages.getPageList()) {
          if (page.getContent().contains(searchTerm)) {
            result.addPage(page);
          }
        }
    }
        return result;
      }
}
