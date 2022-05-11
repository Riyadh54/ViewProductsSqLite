package com.example.viewproductssqlite.DataBases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.viewproductssqlite.Model.ProductsModel;

public class DataBase extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME ="myData.db";
    public static final String TABLE_NAME ="products";
    public static final String MODEL ="model";
    public static final String COMPANY ="company";
    public static final String PRICE ="price";
    public static final String DESCRIPTION ="description";
    public static final String IMAGE ="image";
    private static final int VERSION =1;


    public DataBase(Context context){
        super(context,DATABASE_NAME,null,VERSION);
        this.context= context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"( id INTEGER PRIMARY KEY AUTOINCREMENT ,"+MODEL+" TEXT NOT NULL ,"+COMPANY+" TEXT NOT NULL ,"+PRICE+" INTEGER NOT NULL ,"+DESCRIPTION+" TEXT NOT NULL ,"+IMAGE+" BLOB )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
