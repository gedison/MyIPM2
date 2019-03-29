package clemson.edu.myipm2.database.dao;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import clemson.edu.myipm2.database.DBAdapter;

/**
 * Created by gedison on 7/22/2017.
 */

public class AppDAO {

    private Context mContext;

    public AppDAO(Context context){
        mContext = context;
    }

    public boolean hasAppBeenDownloaded(App app){
        String sql = "SELECT COUNT(*) FROM downloads WHERE appID = '"+app.getAppId()+"'";
        DBAdapter dbAdapter = new DBAdapter(mContext);
        Cursor mCursor = dbAdapter.runSelectQuery(sql, false);
        return (mCursor == null || mCursor.getInt(0) == 0);
    }

    public boolean doesAppExist(App app){
        String sql = "SELECT COUNT(*) FROM app where id = '"+app.getAppId()+"'";
        DBAdapter dbAdapter = new DBAdapter(mContext);
        Cursor mCursor = dbAdapter.runSelectQuery(sql, false);
        return (mCursor != null && mCursor.getInt(0) >= 1);
    }

    public void setAppToDownloaded(App app){
        String sql = "INSERT INTO downloads (appId) VALUES('" + app.getAppId() + "')";
        DBAdapter dbAdapter = new DBAdapter(mContext);
        dbAdapter.executeStatement(sql);
    }

    public void removeApp(App app){
        String sql ="DELETE FROM downloads WHERE appID = '"+app.getAppId()+"'";
        DBAdapter dbAdapter = new DBAdapter(mContext);
        dbAdapter.executeStatement(sql);
    }

    public List<App> getApps(){
        Map<String, AppFruit> items = new HashMap<>();
        List<App> ret = new ArrayList<>();

        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT app.fruitName, app.typeName, " +
                "fruit.color, app.fruitID, app.affectionTypeID, app.id " +
                "FROM app " +
                "INNER JOIN fruit ON fruit.id = app.fruitID " +
                "ORDER BY fruit.name; ";

        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));

        for(String[] result : results){
            if(items.containsKey(result[0])){
                items.get(result[0]).addApp(result);
            }else{
                items.put(result[0], new AppFruit(result));
            }
        }

        Set<String> keys = items.keySet();
        for(String key : keys){
            AppFruit fruit = items.get(key);
            List<App> apps = fruit.getApps();
            for(App app : apps){
                sql = "Select COUNT(*) FROM downloads WHERE downloads.appID = '"+app.getAppId()+"'";
                results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
                app.setIsDownloaded(results[0][0]);
                ret.add(app);
            }
        }

        return ret;
    }

    public List<AppFruit> getAppItems(){
        Map<String, AppFruit> items = new HashMap<String, AppFruit>();
        List<AppFruit> ret = new ArrayList<AppFruit>();

        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT app.fruitName, app.typeName, " +
                "fruit.color, app.fruitID, app.affectionTypeID, app.id " +
                "FROM app " +
                "INNER JOIN fruit ON fruit.id = app.fruitID " +
                "ORDER BY fruit.name; ";

        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));

        for(String[] result : results){
            if(items.containsKey(result[0])){
                items.get(result[0]).addApp(result);
            }else{
                items.put(result[0], new AppFruit(result));
            }
        }

        Set<String> keys = items.keySet();
        for(String key : keys){
            AppFruit fruit = items.get(key);
            List<App> apps = fruit.getApps();
            for(App app : apps){
                sql = "Select COUNT(*) FROM downloads WHERE downloads.appID = '"+app.getAppId()+"'";
                results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
                app.setIsDownloaded(results[0][0]);
            }ret.add(fruit);
        }

        return ret;
    }

    public List<AppFruit> getAppItems(List<App> apps){

        Map<String, AppFruit> items = new HashMap<>();
        List<AppFruit> ret = new ArrayList<>();
        DBAdapter dbAdapter = new DBAdapter(mContext);

        for(App app : apps){
            if(!items.containsKey(app.getFruitName())){
                items.put(app.getFruitName(), new AppFruit(app.getFruitName(), app.getFruitColor(), app.getFruitId()));
            }

            items.get(app.getFruitName()).addApp(app);
        }

        for(String key : items.keySet()){
            AppFruit fruit = items.get(key);
            List<App> fruitApps = fruit.getApps();
            for(App app : fruitApps){
                String sql = "Select COUNT(*) FROM downloads WHERE downloads.appID = '"+app.getAppId()+"'";
                String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(
                        dbAdapter.runSelectQuery(sql, true));
                app.setIsDownloaded(results[0][0]);
            }ret.add(fruit);
        }

        return ret;
    }


    public class AppFruit{
        String fruitName, fruitColor, fruitId;
        List<App> apps = new ArrayList<>();

        AppFruit(String[] bundle) {
            fruitName = bundle[0];
            fruitColor = bundle[2];
            fruitId = bundle[3];

            addApp(bundle);
        }

        AppFruit(String fruitName, String fruitColor, String fruitId){
            this.fruitColor = fruitColor;
            this.fruitId = fruitId;
            this.fruitName = fruitName;
        }


        public String toString(){
            return fruitName;
        }

        public String getFruitId(){
            return fruitId;
        }

        public String getColor(){
            return fruitColor;
        }

        public void addApp(String[] appArray){
            App app = new App(appArray);
            apps.add(app);
        }

        public void addApp(App app){
            apps.add(app);
        }

        public List<App> getApps(){
            return apps;
        }
    }

    public class App{
        String affectionType, affectionTypeId, appId, fruitId, fruitName, fruitColor;
        boolean isDownloaded = false;

        App(String bundle[]){
            fruitId = bundle[3];
            affectionType =  bundle[1];
            affectionTypeId = bundle[4];
            appId = bundle[5];
        }

        public String getFruitId(){return  fruitId;}

        public String getAffectionType(){return affectionType;}

        public String getAffectionTypeId(){return affectionTypeId;}

        public String getAppId(){return  appId;}

        public void setIsDownloaded(String isDownloadedString){
            if(!isDownloadedString.equals("0"))isDownloaded=true;
        }

        public boolean getIsDownloaded(){
            return isDownloaded;
        }

        public String getFruitName() {
            return fruitName;
        }

        public void setFruitName(String fruitName) {
            this.fruitName = fruitName;
        }

        public String getFruitColor() {
            return fruitColor;
        }

        public void setFruitColor(String fruitColor) {
            this.fruitColor = fruitColor;
        }
    }

}
