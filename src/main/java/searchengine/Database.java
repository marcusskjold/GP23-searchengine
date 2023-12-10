package searchengine;

import java.util.Set;

public interface Database {
    public Set<Page> getPages(String word);
    public double getIDF(String word);
}
