package clemson.edu.myipm2.fragments.search;

import clemson.edu.myipm2.database.dao.SearchDAO;


/**
 * Created by gedison on 6/11/2017.
 */

public interface SearchFragmentListener {
    void onItemSelection(SearchDAO.AffectionFruit result);
    void onInfoSelected(SearchDAO.AffectionFruit result);
}
