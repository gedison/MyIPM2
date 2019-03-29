package clemson.edu.myipm2.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String MY_PACKAGE = "edu.clemson.myipm";
    private static final String FRUIT_ID = "fruit_id",
                                AFFECTION_TYPE_ID = "affection_type_id",
                                AFFECTION_ID = "affection_id",
                                DOWNLOADER_HELP="first_download_help_text",
                                TABLE_HELP = "show_table_popup",
                                TABLE_NO_DATA ="show_no_data_popup",
                                LAST_SURVEY = "last_survey",
                                LAST_UPDATE = "last_update";

    public static int getLastUpdate(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        return prefs.getInt(LAST_UPDATE, 0);
    }

    public static int getLastSurvey(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        return prefs.getInt(LAST_SURVEY, 0);
    }

    public static void setLastUpdate(Context mContext, int lastId){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        prefs.edit().putInt(LAST_UPDATE, lastId).apply();
    }

    public static void setLastSurvey(Context mContext, int lastId){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        prefs.edit().putInt(LAST_SURVEY, lastId).apply();
    }

    public static void setFruitId(Context mContext, String fruitId){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        prefs.edit().putString(FRUIT_ID, fruitId).apply();
    }

    public static void setAffectionId(Context mContext, String affectionId){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        prefs.edit().putString(AFFECTION_ID, affectionId).apply();
    }

    public static void setAffectionTypeId(Context mContext, String affectionTypeId){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        prefs.edit().putString(AFFECTION_TYPE_ID, affectionTypeId).apply();
    }

    public static String getFruitId(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        return prefs.getString(FRUIT_ID, "");
    }

    public static String getAffectionId(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        return prefs.getString(AFFECTION_ID, "");
    }

    public static String getAffectionTypeId(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        return prefs.getString(AFFECTION_TYPE_ID, "");
    }

    public static boolean getDownloaderHelp(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        boolean ret = prefs.getBoolean(DOWNLOADER_HELP, true);
        prefs.edit().putBoolean(DOWNLOADER_HELP, false).apply();
        return ret;
    }

    public static void setTableNoData(Context mContext, boolean showTableHelp){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(TABLE_NO_DATA, showTableHelp).apply();
    }

    public static boolean getTableNoData(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        return prefs.getBoolean(TABLE_NO_DATA, true);
    }

    public static void setTableHelp(Context mContext, boolean showTableHelp){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(TABLE_HELP, showTableHelp).apply();
    }

    public static boolean getTableHelp(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        return prefs.getBoolean(TABLE_HELP, true);
    }
}
