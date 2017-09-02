package clemson.edu.myipm.database.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import clemson.edu.myipm.database.DBAdapter;

/**
 * Created by gedison on 6/18/2017.
 */

public class DownloadDAO {

    private Context mContext;

    public DownloadDAO(Context context){
        mContext = context;
    }

    public String[] getFilesToDownload(String fruitId, String affectionTypeId){
        List<String> files = new ArrayList<>();
        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT audioURL FROM audio WHERE affectionID IN (SELECT id FROM affection WHERE fruitID = '"+fruitId+"' AND affectionTypeID = '"+affectionTypeId+"')";
        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        for(String result[] : results)files.add(result[0]);

        sql = "SELECT imageURL FROM gallery WHERE affectionID IN (SELECT id FROM affection WHERE fruitID = '"+fruitId+"' AND affectionTypeID = '"+affectionTypeId+"')";
        results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        for(String result[] : results)files.add(result[0]);

        sql = "SELECT imageURL FROM guide WHERE fruitID = '"+fruitId+"'";
        results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        for(String result[] : results)files.add(result[0]);

        String[] mFiles = new String[files.size()];
        for(int i=0; i<files.size(); i++)mFiles[i] = files.get(i);
        return mFiles;
    }

    public String[] getAllFilesToDownload(){
        List<String> files = new ArrayList<>();
        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT audioURL FROM audio";
        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        for(String result[] : results)files.add(result[0]);

        sql = "SELECT imageURL FROM gallery";
        results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        for(String result[] : results)files.add(result[0]);

        String[] mFiles = new String[files.size()];
        for(int i=0; i<files.size(); i++)mFiles[i] = files.get(i);
        return mFiles;
    }


}
