package clemson.edu.myipm2.database;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import clemson.edu.myipm2.database.dao.AppDAO;


public class GetAppsFromWebTask extends AsyncTask<Void, Void, List<AppDAO.App>> {
    private Gson gson;
    private OnGetAppsFinishedListener listener;
    private static final String BASE_URL = "http://myipm.bugwoodcloud.org/test/myipm.api.php/";

    public GetAppsFromWebTask(OnGetAppsFinishedListener initFinishedListener){
        this.gson = new Gson();
        this.listener = initFinishedListener;
    }

    protected List<AppDAO.App> doInBackground(Void... voids) {
        String apps = getStringFromURL("appItem");
        if(apps != null){
            return gson.fromJson(apps, new TypeToken<List<AppDAO.App>>(){}.getType());
        }else{
            return new ArrayList<>();
        }
    }

    protected void onPostExecute(List<AppDAO.App> apps) {
        listener.onGetApps(apps);
    }


    private static String getStringFromURL(String table){
        String url = BASE_URL+table;

        InputStream inputStream = null;
        try {
            inputStream = new URL(url).openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder ret = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null){
                ret.append(inputLine);
            }

            return ret.toString();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                }catch (IOException e){

                }
            }
        }


    }
}
