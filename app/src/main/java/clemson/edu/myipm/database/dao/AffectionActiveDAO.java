package clemson.edu.myipm.database.dao;

import android.content.Context;

import java.io.Serializable;

import clemson.edu.myipm.database.DBAdapter;

/**
 * Created by gedison on 7/8/2017.
 */

public class AffectionActiveDAO {

    private Context mContext;

    public AffectionActiveDAO(Context context){
        mContext = context;
    }

    public TableEntry[] getAffectionActiveDataWithTypeWithAffection(int typeId, String affectionId){
        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT DISTINCT active.name, active.codeName, active.color, affection_active.efficacy, " +
                "affection_active.fieldUse, affection_active.consumer, affection_active.worker, " +
                "affection_active.ecological, active.id, active.typeID FROM active " +
                "INNER JOIN affection_active ON affection_active.activeID = active.id " +
                "WHERE affection_active.affectionID = '"+affectionId+"' AND " +
                "active.typeID = '"+typeId+"' " +
                "ORDER BY active.name";


        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        TableEntry[] ret = new TableEntry[results.length];
        for(int i = 0; i<results.length; i++) ret[i] = new AffectionActive(results[i]);
        return ret;
    }

    public TableEntry[] getAffectionActiveDataWithTypeWithAffectionWithActiveId(int typeId, String affectionId, int activeId){
        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT DISTINCT active.name, active.codeName, active.color, affection_active.efficacy, " +
                "affection_active.fieldUse, affection_active.consumer, affection_active.worker, " +
                "affection_active.ecological, active.id, active.typeID FROM active " +
                "INNER JOIN affection_active ON affection_active.activeID = active.id " +
                "WHERE affection_active.affectionID = '"+affectionId+"' AND " +
                "active.id = '" +activeId+"' "+
                "ORDER BY active.name";


        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        TableEntry[] ret = new TableEntry[results.length];
        for(int i = 0; i<results.length; i++) ret[i] = new AffectionActive(results[i]);
        return ret;
    }
}
