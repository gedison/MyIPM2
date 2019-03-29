package clemson.edu.myipm2.database;

import java.util.List;

import clemson.edu.myipm2.database.dao.AppDAO;

public interface OnGetAppsFinishedListener {

    void onGetApps(List<AppDAO.App> apps);
}
