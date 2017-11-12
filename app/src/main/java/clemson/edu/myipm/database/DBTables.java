package clemson.edu.myipm.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gedison on 6/11/2017.
 */

public class DBTables {

    public int size(){
        return 17;
    }

    public MyTable getTableFromString(String tableName){
        MyTable ret = null;
        switch (tableName){
            case "fruit": ret = new FruitTable();break;
            case "affection_type": ret = new AffectionTypeTable();break;
            case "affection": ret = new AffectionTable();break;
            case "about": ret = new AffectionTable();break;
        }

        return ret;
    }

    public MyTable getIthTableInstance(int index){
        MyTable ret = null;
        switch (index){
            case 0: ret = new FruitTable();break;
            case 1: ret = new AffectionTypeTable();break;
            case 2: ret = new AffectionTable();break;
            case 3: ret = new AboutTable();break;
            case 4: ret = new ResistanceTable();break;
            case 5: ret = new GuideTable();break;
            case 6: ret = new SituationTable();break;
            case 7: ret = new OverviewTable();break;
            case 8: ret = new MoreTable();break;
            case 9: ret = new GalleryTable();break;
            case 10: ret = new ActiveTable();break;
            case 11: ret = new AffectionActiveTable();break;
            case 12: ret = new TradeTable();break;
            case 13: ret = new AffectionTradeTable();break;
            case 14: ret = new AudioTable();break;
            case 15: ret = new AppTable();break;
            case 16: ret = new DownloadedAppsTable();break;
        }

        return ret;
    }

    public interface MyTable{
        String getCreateStatement();
        String getTableName();
        String getURLName();
        int getColumnCount();
        String[] getColumnNames();
    }

    private class FruitTable implements MyTable {

        private String[] columnNames = {"id", "name", "color"};

        public String getCreateStatement() {
            return "CREATE TABLE " + getTableName()
                    + " ("
                    + " id INTEGER PRIMARY KEY"
                    + " ,name TEXT"
                    + " ,color TEXT"
                    + ");";
        }

        public String getTableName() {
            return "fruit";
        }


        public String getURLName() {
            return getTableName();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames() {
            return columnNames;
        }
    }


    private class AffectionTypeTable implements MyTable{

        private String[] columnNames = {"id", "name"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,name TEXT"
                    +");";
        }

        public String getTableName() {
            return "affection_type";
        }

        public String getURLName() {
            return getTableName().replace("_","");
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class AffectionTable implements MyTable{
        private String[] columnNames = {"id", "name", "affectionTypeID", "fruitID"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,name TEXT"
                    +" ,affectionTypeID INTEGER"
                    +" ,fruitID INTEGER"
                    +");";
        }

        public String getTableName() {
            return "affection";
        }

        public String getURLName() {
            return getTableName();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class AboutTable implements MyTable{
        private String[] columnNames = {"id", "about"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,about TEXT"
                    +");";
        }

        public String getURLName() {
            return getTableName();
        }

        public String getTableName() {
            return "about";
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class ResistanceTable implements MyTable{
        private String[] columnNames = {"id", "fruitID", "affectionTypeID","placement", "title", "content" };

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,fruitID INTEGER"
                    +" ,affectionTypeID INTEGER"
                    +" ,placement INTEGER"
                    +" ,title TEXT"
                    +" ,content TEXT"

                    +");";
        }

        public String getTableName() {
            return "resistance";
        }

        public String getURLName() {
            return getTableName();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class GuideTable implements MyTable{
        private String[] columnNames = {"id", "fruitID", "affectionTypeID", "placement", "title", "content","imageURL"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,fruitID INTEGER"
                    +" ,affectionTypeID INTEGER"
                    +" ,placement INTEGER"
                    +" ,title TEXT"
                    +" ,content TEXT"
                    +" ,imageURL TEXT"
                    +");";
        }

        public String getTableName() {
            return "guide";
        }

        public String getURLName() {
            return getTableName();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class SituationTable implements MyTable{
        private String[] columnNames = {"id", "fruitID", "affectionTypeID", "placement", "title", "content" };

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,fruitID INTEGER"
                    +" ,affectionTypeID INTEGER"
                    +" ,placement INTEGER"
                    +" ,title TEXT"
                    +" ,content TEXT"

                    +");";
        }

        public String getTableName() {
            return "situation";
        }

        public String getURLName() {
            return getTableName();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class OverviewTable implements MyTable{
        private String[] columnNames = {"id", "affectionID","summary"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,affectionID INTEGER"
                    +" ,summary TEXT"
                    +");";
        }

        public String getTableName() {
            return "overview";
        }

        public String getURLName() {
            return "affectionoverview";
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class MoreTable implements MyTable{
        private String[] columnNames = {"id", "affectionID", "name", "symptoms", "chemical", "fungicide", "biological"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,affectionID INTEGER"
                    +" ,name TEXT"
                    +" ,symptoms TEXT"
                    +" ,chemical TEXT"
                    +" ,fungicide TEXT"
                    +" ,biological TEXT"
                    +");";
        }

        public String getTableName() {
            return "more";
        }

        public String getURLName() {
            return getTableName();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class GalleryTable implements MyTable{
        private String[] columnNames = {"id", "affectionID", "imageURL", "placement"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,affectionID INTEGER"
                    +" ,imageURL TEXT"
                    +" ,placement INTEGER"
                    +");";
        }

        public String getTableName() {
            return "gallery";
        }

        public String getURLName() {
            return getTableName();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class AudioTable implements MyTable{
        private String[] columnNames = {"id", "title", "author", "audioURL", "affectionID"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,title TEXT"
                    +" ,author TEXT"
                    +" ,audioURL TEXT"
                    +" ,affectionID INTEGER"
                    +");";
        }

        public String getTableName() {
            return "audio";
        }

        public String getURLName() {
            return getTableName();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class ActiveTable implements MyTable{
        private String[] columnNames = {"id", "name", "codeID", "codeName", "color", "typeName", "typeID"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,name TEXT"
                    +" ,codeID INTEGER"
                    +" ,codeName TEXT"
                    +" ,color TEXT"
                    +" ,typeName TEXT"
                    +" ,typeID INTEGER"
                    +");";
        }

        public String getTableName() {
            return "active";
        }

        public String getURLName() {
            return getTableName();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class AffectionActiveTable implements MyTable{
        private String[] columnNames = {"id", "affectionID", "activeID", "efficacy", "fieldUse"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,affectionID INTEGER"
                    +" ,activeID INTEGER"
                    +" ,efficacy INTEGER"
                    +" ,fieldUse FLOAT"
                    +");";
        }

        public String getTableName() {
            return "affection_active";
        }

        public String getURLName() {
            return getTableName().replace("_","");
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class TradeTable implements MyTable{
        private String[] columnNames = {"id", "fruitID", "name", "activeName", "activeID", "rate", "phi", "rei","maxSpray", "maxProduct",
                "aquaticAlgae",
                "aquaticInvertebrates",
                "avianAcute",
                "avianReproductive",
                "earthworm",
                "fishChronic",
                "smallMammalAcute",
                "dermalCancer",
                "dermalAcute",
                "inhalation",
                "consumerCancer",
                "humanDietary",
                "pollinatorOffCrop",
                "pollinatorNoBloom",
                "pollinatorInBloom"
                , "typeID"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,fruitID INTEGER"
                    +" ,name TEXT"
                    +" ,activeName TEXT"
                    +" ,activeID INTEGER"
                    +" ,rate TEXT"
                    +" ,phi INTEGER"
                    +" ,rei INTEGER"
                    +" ,maxSpray TEXT"
                    +" ,maxProduct TEXT"
                    +" ,aquaticAlgae INTEGER"
                    +",aquaticInvertebrates INTEGER"
                    +",avianAcute INTEGER"
                    +",avianReproductive INTEGER"
                    +",earthworm INTEGER"
                    +",fishChronic INTEGER"
                    +",smallMammalAcute INTEGER"
                    +",dermalCancer INTEGER"
                    +",dermalAcute INTEGER"
                    +",inhalation INTEGER"
                    +",consumerCancer INTEGER"
                    +",humanDietary INTEGER"
                    +",pollinatorOffCrop INTEGER"
                    +",pollinatorNoBloom INTEGER"
                    +",pollinatorInBloom INTEGER"
                    +",typeID INTEGER"
                    +");";
        }

        public String getTableName() {
            return "trade";
        }

        public String getURLName() {
            return getTableName();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class AffectionTradeTable implements MyTable{
        private String[] columnNames = {"id", "tradeID", "affectionID"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,tradeID INTEGER"
                    +" ,affectionID INTEGER"
                    +");";
        }

        public String getTableName() {
            return "affection_trade";
        }

        public String getURLName() {
            return getTableName().replace("_","");
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class AppTable implements MyTable{
        private String[] columnNames = {"id", "fruitID", "fruitName", "affectionTypeID", "typeName"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,fruitID INTEGER"
                    +" ,fruitName TEXT"
                    +" ,affectionTypeID INTEGER"
                    +" ,typeName TEXT"
                    +");";
        }

        public String getTableName() {
            return "app";
        }

        public String getURLName() {
            return getTableName();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }

    private class DownloadedAppsTable implements MyTable{
        private String[] columnNames = {"id", "appID"};

        public String getCreateStatement() {
            return "CREATE TABLE "+getTableName()
                    +" ("
                    +" id INTEGER PRIMARY KEY"
                    +" ,appID INTEGER"
                    +");";
        }

        public String getTableName() {
            return "downloads";
        }

        public String getURLName() {
            return getTableName();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String[] getColumnNames(){
            return columnNames;
        }
    }
}
