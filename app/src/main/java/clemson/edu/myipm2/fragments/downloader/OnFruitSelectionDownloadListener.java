package clemson.edu.myipm2.fragments.downloader;

import clemson.edu.myipm2.database.dao.AppDAO;


/**
 * Created by gedison on 6/11/2017.
 */

public interface OnFruitSelectionDownloadListener {
    void onFruitSelection(AppDAO.App item);
}


