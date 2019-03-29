package clemson.edu.myipm2.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

/**
 * Created by gedison on 6/11/2017.
 */

public class DBAdapter {

    private final Context mContext;
    private DBHelper mDBHelper;
    private DBTables tables = new DBTables();

    public DBAdapter(Context context) {
        mContext = context;
        mDBHelper = new DBHelper(context);
    }

    //==============================================================================================
    //Utility
    public boolean isDatabaseEmpty(){
        String sql = "SELECT COUNT(*) FROM "+tables.getIthTableInstance(0).getTableName();
        Cursor mCursor = runSelectQuery(sql, debugSelectStatement);
        if(mCursor==null)return true;
        else{
            int count = mCursor.getInt(0);
            if(count==0)return true;
            else return false;
        }
    }

    public void deleteContent(){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        db.beginTransaction();

        for(int i=0; i<tables.size()-1; i++){
            String sql = "DELETE FROM "+tables.getIthTableInstance(i).getTableName();
            db.execSQL(sql);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //==============================================================================================
    //READ
    private boolean debugSelectStatement = false;
    public Cursor runSelectQuery(String sql, boolean showQuery){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor mCursor = db.rawQuery(sql, null);
        if(mCursor!=null)mCursor.moveToFirst();
        mDBHelper.close();

        return mCursor;
    }

    public String[][] getMultidimensionalArrayOfStringsFromCursor(Cursor mCursor){
        if(mCursor==null)return null;

        String[][] returnValues = new String[mCursor.getCount()][];
        for (int rowNumber = 0; rowNumber < mCursor.getCount(); rowNumber++) {
            String[] rowValues = new String[mCursor.getColumnCount()];
            for (int columnNumber = 0; columnNumber < mCursor.getColumnCount(); columnNumber++) {
                rowValues[columnNumber] = mCursor.getString(columnNumber);
            }returnValues[rowNumber]=rowValues;
            mCursor.moveToNext();
        }return returnValues;
    }

    public void executeStatement(String sql){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        db.execSQL(sql);
    }

    //==============================================================================================
    //CREATE
    private boolean debugInsertStatement = false;

    private void insertRowWithPreparedStatement(SQLiteStatement statement, String[] row){
        for (int column=0; column<row.length; column++){
            if(row[column].equals("NULL"))statement.bindNull(column+1);
            else statement.bindString(column+1, row[column]);
        }statement.executeInsert();
    }

    public void insertRowsIntoTable(List<String[]> rows, DBTables.MyTable table){
        String sql = "INSERT INTO "+table.getTableName()+" VALUES(";
        for(int i=0; i<table.getColumnCount(); i++){
            if(i!=0)sql+=", ";
            sql+="?";
        }sql+=");";

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        for (String[] row : rows)insertRowWithPreparedStatement(statement, row);
        db.setTransactionSuccessful();
        db.endTransaction();
        mDBHelper.close();
    }


}
