package clemson.edu.myipm.fragments.downloader;

import clemson.edu.myipm.database.dao.AppDAO;
import clemson.edu.myipm.database.dao.MainScreenDAO;


/**
 * Created by gedison on 6/11/2017.
 */

public interface OnFruitSelectionDownloadListener {
    void onFruitSelection(AppDAO.App item);
}


