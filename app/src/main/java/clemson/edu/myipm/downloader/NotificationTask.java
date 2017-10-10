package clemson.edu.myipm.downloader;

/**
 * Created by gedison on 7/16/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import clemson.edu.myipm.R;
import clemson.edu.myipm.database.DBTables;
import clemson.edu.myipm.downloader.entity.Notification;

/**
 * Created by gedison on 2/24/2016.
 *
 * Loads an image from a URL
 */

public class NotificationTask extends AsyncTask<Void, Void, List<Notification>> {

    private static final String URL = "http://myipm.bugwoodcloud.org/test/myipm.api.php/notification";
    private Context context;
    private OnNotificationTaskCompleteListener listener;

    public NotificationTask(Context context, OnNotificationTaskCompleteListener listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected List<Notification> doInBackground(Void... voids) {
        List<Notification> notifications = new ArrayList<>();

        String notificationJSON = getStringFromURL();
        if(notificationJSON == null){
            return null;
        }

        try {
            JSONArray mJSONArray = new JSONArray(notificationJSON);
            for(int i=0; i<mJSONArray.length(); i++){
                JSONObject object = mJSONArray.getJSONObject(i);
                notifications.add(getNotificationFromJSON(object));
            }
        }catch (JSONException e){
            return null;
        }

        return notifications;
    }

    @Override
    protected void onPostExecute(List<Notification> notifications) {

        listener.onNotificationTaskComplete(notifications);
    }

    private Notification getNotificationFromJSON(JSONObject object) throws JSONException{
        return new Notification(
                object.get("id").toString(),
                object.get("notificationTypeId").toString(),
                object.get("title").toString(),
                object.get("content").toString()
        );

    }

    private static String getStringFromURL(){

        InputStream inputStream = null;
        try {
            inputStream = new URL(URL).openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String ret = "";
            String inputLine;

            while ((inputLine = in.readLine()) != null){
                ret += inputLine;
            }

            return ret;
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


