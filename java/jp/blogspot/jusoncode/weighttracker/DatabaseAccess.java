package jp.blogspot.jusoncode.weighttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


class DatabaseAccess {

    private Database database;
    private SQLiteDatabase db;
    final ArrayList<Weight> databaseList = new ArrayList<>();

    DatabaseAccess(Context context){

        this.database = new Database(context);
    }

    void addWeight(Weight weight, View view){

        db = database.getWritableDatabase();

        try{
            db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put(Contract.DBCon.COLUMN_WEIGHT,weight.weight);
            values.put(Contract.DBCon.COLUMN_DATE,weight.date);
            values.put(Contract.DBCon.COLUMN_MEASUREMENT,weight.measurement);

            db.insertOrThrow(Contract.DBCon.TABLE_NAME,null,values);

        }catch (Exception e){
            Toast.makeText(view.getContext(),"Database error adding weight, report to developer",Toast.LENGTH_SHORT).show();
        }finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }

    void getWeights(){
        databaseList.clear();
        int id;
        String date,measurement,weight;
        String columns[] = {Contract.DBCon._ID, Contract.DBCon.COLUMN_WEIGHT, Contract.DBCon.COLUMN_DATE, Contract.DBCon.COLUMN_MEASUREMENT};

        db = database.getReadableDatabase();


        db.beginTransaction();

        Cursor cursor = db.query(Contract.DBCon.TABLE_NAME, columns, null, null, null, null, Contract.DBCon._ID);

        while (cursor.moveToNext()){
            id = cursor.getInt(0);
            weight = cursor.getString(1);
            date = cursor.getString(2);
            measurement = cursor.getString(3);

            Weight weightCreated = new Weight(date,weight,measurement);
            weightCreated.id = id;
            if(!databaseList.contains(weightCreated)){
                    databaseList.add(weightCreated);
            }
        }

        cursor.close();


        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();




    }
}
