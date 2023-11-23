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

    //A method that adds a page to the pagelist field of the PageList
    public void addPage (Page page) {
        pageList.add(page);
    }

}
