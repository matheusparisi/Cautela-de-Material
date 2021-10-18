package com.example.admin.cauteladematerial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cautela.db";
    private static final int DATABASE_VERSION = 1;



    private SQLiteDatabase db;
    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;



        final String SQL_CREATE_STUDENT_TABLE = "CREATE TABLE " +
                DatabaseContract.CautelaDeMaterialTable.TABLE_NAME + " ( " +
                DatabaseContract.CautelaDeMaterialTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.CautelaDeMaterialTable.MATERIAL + " TEXT, " +
                DatabaseContract.CautelaDeMaterialTable.MILITAR + " TEXT,  " +
                DatabaseContract.CautelaDeMaterialTable.DATA + " TEXT,  " +
                DatabaseContract.CautelaDeMaterialTable.INFO + " TEXT,  " +
                DatabaseContract.CautelaDeMaterialTable.ANO + " TEXT,  " +
                DatabaseContract.CautelaDeMaterialTable.MES + " TEXT,  " +
                DatabaseContract.CautelaDeMaterialTable.DESTINO + " TEXT,  " +
                DatabaseContract.CautelaDeMaterialTable.TIPO + " TEXT,  " +
                DatabaseContract.CautelaDeMaterialTable.QUANTIA + " TEXT  " +
                ")";


        db.execSQL(SQL_CREATE_STUDENT_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.CautelaDeMaterialTable.TABLE_NAME);

        onCreate(db);
    }





    public long insertUser(Items model){
        db=getWritableDatabase();

        ContentValues cv=new ContentValues();

        cv.put( DatabaseContract.CautelaDeMaterialTable.MATERIAL,model.getitemmaterial());
        cv.put( DatabaseContract.CautelaDeMaterialTable.MILITAR,model.getitemmilitar());
        cv.put( DatabaseContract.CautelaDeMaterialTable.DATA,model.getitemdata());
        cv.put( DatabaseContract.CautelaDeMaterialTable.INFO,model.getiteminfo());
        cv.put( DatabaseContract.CautelaDeMaterialTable.ANO,model.getitemano());
        cv.put( DatabaseContract.CautelaDeMaterialTable.MES,model.getitemmes());
        cv.put( DatabaseContract.CautelaDeMaterialTable.DESTINO,model.getitemdestino());
        cv.put( DatabaseContract.CautelaDeMaterialTable.TIPO,model.getitemtipo());
        cv.put( DatabaseContract.CautelaDeMaterialTable.QUANTIA,model.getitemquantia());


        long id=db.insert(DatabaseContract.CautelaDeMaterialTable.TABLE_NAME,null,cv);

        return id;
    }

    public ArrayList<Items> getAllLocalUser(){
        db=getReadableDatabase();
        ArrayList<Items>list=new ArrayList<>();
        Cursor cursor=db.rawQuery("SELECT * FROM " + DatabaseContract.CautelaDeMaterialTable.TABLE_NAME,null);

        if(cursor.moveToFirst()){
            do {


                String itemmaterial = cursor.getString(cursor.getColumnIndex(DatabaseContract.CautelaDeMaterialTable.MATERIAL));
                String itemmilitar = cursor.getString(cursor.getColumnIndex(DatabaseContract.CautelaDeMaterialTable.MILITAR));
                String itemdata = cursor.getString(cursor.getColumnIndex(DatabaseContract.CautelaDeMaterialTable.DATA));
                String iteminfo = cursor.getString(cursor.getColumnIndex(DatabaseContract.CautelaDeMaterialTable.INFO));
                String itemano = cursor.getString(cursor.getColumnIndex(DatabaseContract.CautelaDeMaterialTable.ANO));
                String itemmes = cursor.getString(cursor.getColumnIndex(DatabaseContract.CautelaDeMaterialTable.MES));
                String itemdestino = cursor.getString(cursor.getColumnIndex(DatabaseContract.CautelaDeMaterialTable.DESTINO));
                String itemtipo = cursor.getString(cursor.getColumnIndex(DatabaseContract.CautelaDeMaterialTable.TIPO));
                String itemquantia = cursor.getString(cursor.getColumnIndex(DatabaseContract.CautelaDeMaterialTable.QUANTIA));

                list.add(new Items(itemmilitar, iteminfo, itemdata, itemano, itemmes, itemdestino, itemtipo, itemmaterial, itemquantia));
            } while (cursor.moveToNext());
        }

        return list;
    }

}
