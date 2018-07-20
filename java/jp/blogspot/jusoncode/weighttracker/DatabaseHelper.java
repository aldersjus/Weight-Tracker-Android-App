package jp.blogspot.jusoncode.weighttracker;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class Database extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WeightTracker.db";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Contract.DBCon.TABLE_NAME;
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Contract.DBCon.TABLE_NAME + "(" +
            Contract.DBCon._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + Contract.DBCon.COLUMN_WEIGHT + " TEXT," +
            Contract.DBCon.COLUMN_DATE + " TEXT," + Contract.DBCon.COLUMN_MEASUREMENT + " TEXT)";


    Database(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

}
