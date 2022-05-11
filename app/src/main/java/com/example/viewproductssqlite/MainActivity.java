package com.example.viewproductssqlite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.viewproductssqlite.Adapter.RecyclerAdapter;
import com.example.viewproductssqlite.DataBases.DataBaseAccess;
import com.example.viewproductssqlite.Model.ProductsModel;
import com.example.viewproductssqlite.interfaces.MyInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    RecyclerView rv;
    Toolbar toolbar;
    private DataBaseAccess dataBaseAccess;
    private ArrayList<ProductsModel> data ;
    FloatingActionButton fab;
    private final int REQUEST_CODE = 15;
    private final int PERMISSION_REQUEST_CODE = 13;
    public static final String PRODUCT_KEY = "product_id";
    int product_id = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        rv = findViewById(R.id.main_rv);
        fab = findViewById(R.id.main_fab);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }


        dataBaseAccess = DataBaseAccess.getInstance(MainActivity.this);



        dataBaseAccess.open();
        data = dataBaseAccess.getAllProducts();
        if (data.size()>0)
        {
            RecyclerAdapter adapter = new RecyclerAdapter(MainActivity.this, R.layout.items_layout, data, new MyInterface() {
                @Override
                public void onClickListener(int productId) {
                    Intent intent =new Intent(MainActivity.this,DetailsActivity.class);
                    intent.putExtra(PRODUCT_KEY,productId);
                    startActivityForResult(intent,REQUEST_CODE);
                }
            });
            RecyclerView.LayoutManager lm = new GridLayoutManager(MainActivity.this,2);
            rv.setLayoutManager(lm);
            rv.setHasFixedSize(true);
            rv.setAdapter(adapter);
        }else
        {
            Toast.makeText(this, getResources().getString(R.string.is_empty), Toast.LENGTH_SHORT).show();
        }



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this,DetailsActivity.class);
                in.putExtra(PRODUCT_KEY,product_id);
                startActivityForResult(in,REQUEST_CODE);
            }
        });







    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
       MenuInflater menuInflater= getMenuInflater();
       menuInflater.inflate(R.menu.main_menu,menu);
       SearchView searchView = (SearchView) menu.findItem(R.id.search_box).getActionView();
       searchView.setSubmitButtonEnabled(true);



       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               dataBaseAccess.open();
               data = dataBaseAccess.getProductsByName(query);
               dataBaseAccess.close();
               showDataByName();
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });
      searchView.setOnCloseListener(new SearchView.OnCloseListener() {
          @Override
          public boolean onClose() {
              dataBaseAccess.open();
              data = dataBaseAccess.getAllProducts();
              dataBaseAccess.close();
              showAllData();

              return false;
          }
      });


       return true;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent in) {
        super.onActivityResult(requestCode, resultCode, in);
        if (requestCode == REQUEST_CODE){
            dataBaseAccess.open();
            data = dataBaseAccess.getAllProducts();
           showAllData();
        }
    }
    private void showAllData(){
        if (data.size()>0)
        {
            RecyclerAdapter adapter = new RecyclerAdapter(MainActivity.this, R.layout.items_layout, data, new MyInterface() {
                @Override
                public void onClickListener(int productId) {
                    Intent intent =new Intent(MainActivity.this,DetailsActivity.class);
                    intent.putExtra(PRODUCT_KEY,productId);
                    startActivityForResult(intent,REQUEST_CODE);
                }
            });
            RecyclerView.LayoutManager lm = new GridLayoutManager(MainActivity.this,2);
            rv.setLayoutManager(lm);
            rv.setHasFixedSize(true);
            adapter.setData(data);
            adapter.notifyDataSetChanged();
            rv.setAdapter(adapter);
        }else
        {
            Toast.makeText(this, getResources().getString(R.string.is_empty), Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        }
    }

    private void showDataByName(){
        if (data.size()>0)
        {
            RecyclerAdapter adapter = new RecyclerAdapter(MainActivity.this, R.layout.items_layout, data, new MyInterface() {
                @Override
                public void onClickListener(int productId) {
                    Intent intent =new Intent(MainActivity.this,DetailsActivity.class);
                    intent.putExtra(PRODUCT_KEY,productId);
                    startActivityForResult(intent,REQUEST_CODE);
                }
            });
            RecyclerView.LayoutManager lm = new GridLayoutManager(MainActivity.this,2);
            rv.setLayoutManager(lm);
            rv.setHasFixedSize(true);
            adapter.setData(data);
            adapter.notifyDataSetChanged();
            rv.setAdapter(adapter);

        }else
        {
            Toast.makeText(this, getResources().getString(R.string.not_found_model), Toast.LENGTH_SHORT).show();
        }
    }
}