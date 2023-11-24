package searchengine;

import java.util.ArrayList;
import java.util.List;

/** Represents a list of Page objects
 * @author Marcus Skjold, Andreas Riget Bagge, Sean Weston
 * @version 0.1
 */

public class PageList {
    private List<Page> pageList;

    /**
     * Initialises the PageList with an empty List of Page
     */
    public PageList() {
        pageList = new ArrayList<>();
    }

    /**
     * @return the list of pages kept by this PageList
     */
    public List<Page> getPageList() {
        return pageList;
    }

    /** Returns the Page at the specified position of the PageList.
     * @param index the index of the element to be returned
     * @return the Page at the specified position in this PageList
     * @throws IndexOutOfBoundsException - if the index is out of range
     *                                     ({@code index < 0 || index >= size()})
     */
    public Page get(int index) {
        return pageList.get(index);
    }

    /**
     * Adds a page to the pageList field
     * @param page the page to be added
     */
    public void addPage (Page page) {
        pageList.add(page);
    }

    /**
     * Returns the number of pages in the Pagelist
     * @return the number of Page-objects in the pagelist field of the PageList
     */
    public int getSize() {
        return pageList.size();
    }

}
