package clemson.edu.myipm2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gedison on 6/11/2017.
 */

class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyIPM_2.db";
    private static final int DATABASE_VERSION = 40;

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        DBTables tables = new DBTables();
        for(int i=0; i<tables.size(); i++){
            DBTables.MyTable table = tables.getIthTableInstance(i);
            sqLiteDatabase.execSQL(table.getCreateStatement());
        }
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        DBTables tables = new DBTables();
        for(int i=0; i<tables.size(); i++){
            DBTables.MyTable table = tables.getIthTableInstance(i);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+table.getTableName());
        }onCreate(sqLiteDatabase);
    }
}