package clemson.edu.myipm.fragments.search;

import clemson.edu.myipm.database.dao.GalleryDAO;
import clemson.edu.myipm.database.dao.SearchDAO;


/**
 * Created by gedison on 6/11/2017.
 */

public interface SearchFragmentListener {
    void onItemSelection(SearchDAO.AffectionFruit result);
    void onInfoSelected(SearchDAO.AffectionFruit result);
}
