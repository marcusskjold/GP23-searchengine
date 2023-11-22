package searchengine;

import java.util.ArrayList;
import java.util.List;


public class PageList {
    private List<Page> pageList;

    public PageList () {
        pageList = new ArrayList<>();
    }

    public List<Page> getPageList() {
        return pageList;
    }

    public void addPage (Page page) {
        pageList.add(page);
    }

}
