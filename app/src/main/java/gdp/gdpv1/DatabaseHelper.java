package gdp.gdpv1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper{
    private String theString;
   
        public static final String DATABASE_NAME="final.db";
        public static final String _ID="id";
        public static final String TABLE_NAME = "Device_1";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME="time";
        public static final String COLUMN_NAME_TEMPERATURE = "temperature";
        public static final String COLUMN_NAME_HUMIDITY = "humidity";
        public static final String COLUMN_NAME_SOIL = "soil_moisture";
        public static final String COLUMN_NAME_LIGHT = "light_intensity";
        public static final String COLUMN_NAME_WIND = "Wind_speed";
        public static final String COLUMN_NAME_WATER = "Water_level";


    String TEXT_TYPE = " TEXT";
        String COMMA_SEP = ",";
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY," + COLUMN_NAME_DATE + TEXT_TYPE +
                COMMA_SEP+ COLUMN_NAME_TIME+ TEXT_TYPE + " NOT NULL UNIQUE" + COMMA_SEP+
            COLUMN_NAME_TEMPERATURE + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_HUMIDITY + TEXT_TYPE + COMMA_SEP +
            COLUMN_NAME_SOIL + TEXT_TYPE + COMMA_SEP +
            COLUMN_NAME_LIGHT + TEXT_TYPE + COMMA_SEP +
            COLUMN_NAME_WIND + TEXT_TYPE + COMMA_SEP +
            COLUMN_NAME_WATER + TEXT_TYPE + ")";


    public DatabaseHelper(Context context) {
            super(context,DATABASE_NAME,null,1);
            SQLiteDatabase db = this.getWritableDatabase();
        }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    //Add new row ro database
    public long addData(String date,String time, String tempdata, String humdata, String soildata,
                        String lightdata, String winddata, String waterdata) {
        ContentValues values = new ContentValues();
        long rows = 0;

        values.put(COLUMN_NAME_DATE, date);
        values.put(COLUMN_NAME_TIME,time);
        values.put(COLUMN_NAME_TEMPERATURE, tempdata);
        values.put(COLUMN_NAME_HUMIDITY, humdata);
        values.put(COLUMN_NAME_SOIL, soildata);
        values.put(COLUMN_NAME_LIGHT, lightdata);
        values.put(COLUMN_NAME_WIND, winddata);
        values.put(COLUMN_NAME_WATER, waterdata);
        SQLiteDatabase db = getWritableDatabase();
        //Cursor cursor = db.rawQuery("SELECT date FROM Device_1 ", null);
         rows = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return rows;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"date","time","temperature","humidity","soil_moisture",
                        "light_intensity","Wind_speed","Water_level"},null,null,null,null,null,null);
        return cursor;
    }


    public ArrayList<String> queryXData(){
        SQLiteDatabase db= this.getReadableDatabase();

        String day = theString;

        ArrayList<String> xNewData = new ArrayList<String>();
        Cursor cursor =  db.rawQuery("SELECT * FROM Device_1 WHERE date ='" + day +"'" , null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            xNewData.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TIME)));
        }
        cursor.close();
        db.close();
        return xNewData;
    }

    public void setString(String str)
    {
        theString = str;
    }

    public ArrayList<Float> TempData(){
        ArrayList<Float> yNewData = new ArrayList<Float>();
        SQLiteDatabase db= this.getReadableDatabase();

        String day = theString;
        //System.out.println(theString);
        Cursor cursor =  db.rawQuery("SELECT * FROM Device_1 WHERE date ='" + day+"'" , null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            yNewData.add(cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_TEMPERATURE)));
        }
        //System.out.println(yNewData);
        cursor.close();
        db.close();
        return yNewData;
    }

    public ArrayList<Float> HumData(){
        ArrayList<Float> yNewData = new ArrayList<Float>();
        SQLiteDatabase db= this.getReadableDatabase();

        String day = theString;
       // System.out.println(theString);
        //String query = "SELECT " + COLUMN_NAME_TEMPERATURE + " FROM " + TABLE_NAME;
        Cursor cursor =  db.rawQuery("SELECT * FROM Device_1 WHERE date ='" + day+"'" , null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            yNewData.add(cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_HUMIDITY)));
        }
        //System.out.println(yNewData);
        cursor.close();
        db.close();
        return yNewData;
    }

    public ArrayList<Float> SoilData(){
        ArrayList<Float> yNewData = new ArrayList<Float>();
        SQLiteDatabase db= this.getReadableDatabase();

        String day = theString;
        //System.out.println(theString);
        Cursor cursor =  db.rawQuery("SELECT * FROM Device_1 WHERE date ='" + day+"'" , null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            yNewData.add(cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_SOIL)));
        }
        //System.out.println(yNewData);
        cursor.close();
        db.close();
        return yNewData;
    }

    /*public ArrayList<Float> LightData(){
        ArrayList<Float> yNewData = new ArrayList<Float>();
        SQLiteDatabase db= this.getReadableDatabase();

        String day = theString;
        System.out.println(theString);
        //String query = "SELECT " + COLUMN_NAME_TEMPERATURE + " FROM " + TABLE_NAME;
        Cursor cursor =  db.rawQuery("SELECT * FROM Device_1 WHERE date ='" + day+"'" , null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            yNewData.add(cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_LIGHT)));
        }
        System.out.println(yNewData);
        cursor.close();
        db.close();
        return yNewData;
    }*/

    public ArrayList<Float> WindData(){
        ArrayList<Float> yNewData = new ArrayList<Float>();
        SQLiteDatabase db= this.getReadableDatabase();

        String day = theString;
        //System.out.println(theString);
        Cursor cursor =  db.rawQuery("SELECT * FROM Device_1 WHERE date ='" + day+"'" , null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            yNewData.add(cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_WIND)));
        }
        //System.out.println(yNewData);
        cursor.close();
        db.close();
        return yNewData;
    }

    public ArrayList<Float> WaterData(){
        ArrayList<Float> yNewData = new ArrayList<Float>();
        SQLiteDatabase db= this.getReadableDatabase();

        String day = theString;
        //System.out.println(theString);
        Cursor cursor =  db.rawQuery("SELECT * FROM Device_1 WHERE date ='" + day+"'" , null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            yNewData.add(cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_WATER)));
        }
       // System.out.println(yNewData);
        cursor.close();
        db.close();
        return yNewData;
    }
}
