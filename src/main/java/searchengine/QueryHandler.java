package searchengine;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import java.util.StringTokenizer;

import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList; 

public class QueryHandler{
    private Database database;

    public QueryHandler(String filename) throws IOException{
        this.database = new Database(filename); 
    }

    public Set<Page> search(String searchString) {
        Query q = new Query(splitSearchString(searchString));
        return database.matchQuery(q);
    }

    public Set<Set<String>> splitSearchString(String searchString) {
        Set<Set<String>> returnSets = new HashSet<>();
        Scanner sc = new Scanner(searchString);
        sc.useDelimiter("%20OR%20");

        while (sc.hasNext()){
            returnSets.add(splitString(sc.next()));
        }
        return returnSets;
    }

    public Set<String> splitString(String searchString) { 
        StringTokenizer tk = new StringTokenizer(searchString,"%20");
        Set<String> returnSet = new HashSet<>(); 
        while (tk.hasMoreTokens()){
            returnSet.add(tk.nextToken().toLowerCase());
        }
        return returnSet;
    }

    

}
