package clemson.edu.myipm.database.dao;

import android.content.Context;

import clemson.edu.myipm.database.DBAdapter;

/**
 * Created by gedison on 7/16/2017.
 */

public class AutoCompleteTextDAO {

    private Context mContext;

    public AutoCompleteTextDAO(Context context){
        mContext = context;
    }

    public String[] getAutoCompleteText(){
        DBAdapter dbAdapter = new DBAdapter(mContext);

        String sql = "SELECT distinct name FROM trade";
        String[][] a = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        sql = "SELECT distinct name FROM active";
        String[][] b = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        String[] ret = new String[a.length+b.length];
        for(int i=0; i<a.length; i++)ret[i]=a[i][0];
        for(int i=0; i<b.length; i++)ret[i+(a.length)]=b[i][0];
        return ret;
    }

}
