package clemson.edu.myipm2.database;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gedison on 7/22/2017.
 */

public class InitDatabaseTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private OnInitFinishedListener listener;

    public InitDatabaseTask(Context context, OnInitFinishedListener initFinishedListener){
        this.context = context;
        listener = initFinishedListener;
    }

    protected Void doInBackground(Void... voids) {
        DBAdapter mDBAdapter = new DBAdapter(context);
        DBTables tables = new DBTables();
        for(int i=0; i<tables.size()-1; i++) {
            DBTables.MyTable table = tables.getIthTableInstance(i);
            String jsonFileContents = getStringFromAssetsFolder(context, table.getTableName());
            try {
                System.out.println(table.getTableName());
                JSONArray mJSONArray = new JSONArray(jsonFileContents);
                List<String[]> rows = getRowsFromJSONArray(mJSONArray, table);
                mDBAdapter.insertRowsIntoTable(rows, table);
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        return null;
    }

    protected void onPostExecute(Void v) {
        listener.onInitFinished();
    }

    private static String getStringFromAssetsFolder(Context context, String fileName) {
        StringBuilder mStringBuilder = new StringBuilder();
        InputStream jsonInputStream = null;
        try {
            jsonInputStream = context.getAssets().open(fileName+".json");
            BufferedReader in = new BufferedReader(new InputStreamReader(jsonInputStream, "UTF-8"));
            String temp = "";
            while ((temp=in.readLine()) != null) mStringBuilder.append(temp);
            in.close();

            return mStringBuilder.toString();
        } catch (IOException e) {
            return null;
        }
    }

    private static List<String[]> getRowsFromJSONArray(JSONArray jsonArray, DBTables.MyTable table){
        List<String[]> rows = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            try {
                String[] temp = new String[table.getColumnCount()];
                JSONObject row = jsonArray.getJSONObject(i);

                for(int j=0; j<table.getColumnCount(); j++) {
                    temp[j] = (row.get(table.getColumnNames()[j])).toString();
                }

                rows.add(temp);
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(e.getLocalizedMessage());
                return null;
            }
        }
        return rows;
    }
}
