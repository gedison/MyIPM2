package clemson.edu.myipm2.database.dao;

import android.content.Context;

import clemson.edu.myipm2.database.DBAdapter;
import clemson.edu.myipm2.utility.SharedPreferencesHelper;

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
                "affection_active.fieldUse, active.id, active.typeID FROM active " +
                "INNER JOIN affection_active ON affection_active.activeID = active.id " +
                "WHERE affection_active.affectionID = '"+affectionId+"' AND " +
                "active.typeID = '"+typeId+"' " +
                "ORDER BY active.name";


        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        TableEntry[] ret = new TableEntry[results.length];
        for(int i = 0; i<results.length; i++){
            if(SharedPreferencesHelper.getAffectionTypeId(mContext).equals("1")){
                ret[i] = new AffectionActive(results[i]);
            }else{
                ret[i] = new AffectionActivePest(results[i]);
            }
        }
        return ret;
    }

    public TableEntry[] getAffectionActiveDataWithTypeWithAffectionWithActiveId(int typeId, String affectionId, int activeId){
        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT DISTINCT active.name, active.codeName, active.color, affection_active.efficacy, " +
                "affection_active.fieldUse, active.id, active.typeID FROM active " +
                "INNER JOIN affection_active ON affection_active.activeID = active.id " +
                "WHERE affection_active.affectionID = '"+affectionId+"' AND " +
                "active.id = '" +activeId+"' "+
                "ORDER BY active.name";



        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        TableEntry[] ret = new TableEntry[results.length];
        for(int i = 0; i<results.length; i++){
            if(SharedPreferencesHelper.getAffectionTypeId(mContext).equals("1")){
                ret[i] = new AffectionActive(results[i]);
            }else{
                ret[i] = new AffectionActivePest(results[i]);
            }


        }
        return ret;
    }
}
