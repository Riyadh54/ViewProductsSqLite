package com.example.viewproductssqlite.DataBases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.viewproductssqlite.Model.ProductsModel;

import java.util.ArrayList;

public class DataBaseAccess {

    private static DataBaseAccess instance;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase sqLiteDatabase ;

    private DataBaseAccess(Context context){
        this.openHelper = new DataBase(context);
    }

    public static DataBaseAccess getInstance(Context context){
      if (instance == null){
          instance = new DataBaseAccess(context);
      }
      return instance;
    }

    public void open(){
        this.sqLiteDatabase = this.openHelper.getWritableDatabase();
    }
    public void close(){
        if (sqLiteDatabase != null)
        {
            sqLiteDatabase.close();
        }
    }

    public boolean insert_items(ProductsModel productsModel){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.MODEL,productsModel.getModel());
        contentValues.put(DataBase.COMPANY,productsModel.getCompany());
        contentValues.put(DataBase.PRICE,productsModel.getPrice());
        contentValues.put(DataBase.DESCRIPTION,productsModel.getDescription());
        contentValues.put(DataBase.IMAGE,productsModel.getImg());
        long result = sqLiteDatabase.insert(DataBase.TABLE_NAME,null,contentValues);
        return result == -1;
    }

    public ArrayList<ProductsModel> getAllProducts(){
        ArrayList<ProductsModel> data = new ArrayList<>();
        String query = "SELECT * FROM "+DataBase.TABLE_NAME;
        Cursor cursor= sqLiteDatabase.rawQuery(query,null,null);
        if (cursor != null)
        {
            if (cursor.moveToFirst()){
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String model = cursor.getString(cursor.getColumnIndex(DataBase.MODEL));
                    String company = cursor.getString(cursor.getColumnIndex(DataBase.COMPANY));
                    int price = cursor.getInt(cursor.getColumnIndex(DataBase.PRICE));
                    String description = cursor.getString(cursor.getColumnIndex(DataBase.DESCRIPTION));
                    byte[] image = cursor.getBlob(cursor.getColumnIndex(DataBase.IMAGE));
                    ProductsModel productsModel = new ProductsModel(id,model,company,description,price,image);
                    data.add(productsModel);

                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        return data;
    }

    public ArrayList<ProductsModel> getProductsByName(String model0){
        ArrayList<ProductsModel> data = new ArrayList<>();
        String[] args = new String[]{"%"+model0+"%"};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ DataBase.TABLE_NAME +" WHERE model LIKE ?",args);
        if (cursor != null)
        {
            if (cursor.moveToFirst()){
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String model = cursor.getString(cursor.getColumnIndex(DataBase.MODEL));
                    String company = cursor.getString(cursor.getColumnIndex(DataBase.COMPANY));
                    int price = cursor.getInt(cursor.getColumnIndex(DataBase.PRICE));
                    String description = cursor.getString(cursor.getColumnIndex(DataBase.DESCRIPTION));
                    byte[] image = cursor.getBlob(cursor.getColumnIndex(DataBase.IMAGE));
                    ProductsModel productsModel = new ProductsModel(id,model,company,description,price,image);
                    data.add(productsModel);

                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        return data;
    }


    public ProductsModel getProductsById(int productId){
        ProductsModel productsModel = null;
        String[] args = new String[]{String.valueOf(productId)};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ DataBase.TABLE_NAME +" WHERE id=?",args);
        if (cursor != null)
        {
            if (cursor.moveToFirst()){
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String model = cursor.getString(cursor.getColumnIndex(DataBase.MODEL));
                    String company = cursor.getString(cursor.getColumnIndex(DataBase.COMPANY));
                    int price = cursor.getInt(cursor.getColumnIndex(DataBase.PRICE));
                    String description = cursor.getString(cursor.getColumnIndex(DataBase.DESCRIPTION));
                    byte[] image = cursor.getBlob(cursor.getColumnIndex(DataBase.IMAGE));
                    productsModel = new ProductsModel(id,model,company,description,price,image);
                    

                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        return productsModel;
    }

    public boolean delete_item(int productID){
        String[] args = new String[]{String.valueOf(productID)};
       int result = sqLiteDatabase.delete(DataBase.TABLE_NAME,"id=?",args);
       return result>0;
    }

    public boolean update_item(int productId,ProductsModel productsModel){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.MODEL,productsModel.getModel());
        contentValues.put(DataBase.COMPANY,productsModel.getCompany());
        contentValues.put(DataBase.PRICE,productsModel.getPrice());
        contentValues.put(DataBase.DESCRIPTION,productsModel.getDescription());
        contentValues.put(DataBase.IMAGE,productsModel.getImg());
        String[] args = new String[]{String.valueOf(productId)};
        int result = sqLiteDatabase.update(DataBase.TABLE_NAME,contentValues,"id=?",args);
        return result>0;
    }





    public long getSize(){
       long size = DatabaseUtils.queryNumEntries(sqLiteDatabase,DataBase.TABLE_NAME);
       return size;
    }


}
