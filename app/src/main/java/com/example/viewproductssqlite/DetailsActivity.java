package com.example.viewproductssqlite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.viewproductssqlite.Adapter.RecyclerAdapter;
import com.example.viewproductssqlite.DataBases.DataBaseAccess;
import com.example.viewproductssqlite.Model.ProductsModel;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView img;
    TextInputEditText et_name,et_company,et_price,et_des;
    int productId;
    DataBaseAccess dataBaseAccess;
    private static final int RESULT_CODE = 19;
    private static final int PICK_IMAGE_REQUEST_CODE = 14;
    Uri imageUri = null;
    byte[] imageByte = null;
    Button btn_addImage;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        img = findViewById(R.id.details_img);
        et_name = findViewById(R.id.details_et_name);
        et_company = findViewById(R.id.details_et_company);
        et_price = findViewById(R.id.details_et_price);
        et_des = findViewById(R.id.details_et_des);
        btn_addImage = findViewById(R.id.details_btn_add_image);
        progressBar = findViewById(R.id.progressBar);



        dataBaseAccess = DataBaseAccess.getInstance(DetailsActivity.this);

        Intent intent = getIntent();
        productId = intent.getIntExtra(MainActivity.PRODUCT_KEY,-1);
        if (productId == -1)
        {
            // Add new Product
            enableFields();

        }else {
            // View Product Details
            disableFields();
            dataBaseAccess.open();
            ProductsModel productsModel = dataBaseAccess.getProductsById(productId);
            dataBaseAccess.close();
            if (productsModel.getImg() == null)
            {
                img.setImageResource(R.drawable.ic_launcher_background);
            }else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(productsModel.getImg(), 0,productsModel.getImg().length);
                img.setImageBitmap(bitmap);
            }
            et_name.setText(productsModel.getModel());
            et_company.setText(productsModel.getCompany());
            et_des.setText(productsModel.getDescription());
            et_price.setText(productsModel.getPrice()+"$");


        }
        btn_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1,PICK_IMAGE_REQUEST_CODE);
            }
        });

    }

    private void disableFields() {
        img.setEnabled(false);
        et_name.setEnabled(false);
        et_company.setEnabled(false);
        et_price.setEnabled(false);
        et_des.setEnabled(false);
        btn_addImage.setVisibility(View.INVISIBLE);
    }
    private void enableFields() {
        img.setEnabled(true);
        et_name.setEnabled(true);
        et_company.setEnabled(true);
        et_price.setEnabled(true);
        et_des.setEnabled(true);
        btn_addImage.setVisibility(View.VISIBLE);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.details_menu,menu);
        MenuItem delete = menu.findItem(R.id.delete);
        MenuItem save = menu.findItem(R.id.save);
        MenuItem edit = menu.findItem(R.id.edit);
        if (productId ==-1){
            delete.setVisible(false);
            save.setVisible(true);
            edit.setVisible(false);
        }else {
            delete.setVisible(true);
            save.setVisible(false);
            edit.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                enableFields();
                String price0 = et_price.getText().toString();
                int subString = price0.lastIndexOf("$");
                String newPrice = price0.substring(0,subString);
                et_price.setText(newPrice);
                item.setVisible(false);
                MenuItem save = toolbar.getMenu().findItem(R.id.save);
                save.setVisible(true);
                MenuItem delete = toolbar.getMenu().findItem(R.id.delete);
                delete.setVisible(false);
                break;
            case R.id.save:
                String name = et_name.getText().toString();
                String company = et_company.getText().toString();
                String description = et_des.getText().toString();
                int price = 0;
                if (!et_price.getText().toString().isEmpty())
                {
                    price = Integer.parseInt(et_price.getText().toString());
                }

                Bitmap bitmap = null;
                if (img.getDrawable() != null)
                {
                    BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
                    bitmap = drawable.getBitmap();
                    if (bitmap.getByteCount()>5000000)
                    {
                        Toast.makeText(this, getResources().getString(R.string.is_loading), Toast.LENGTH_LONG).show();
                    }
                    imageByte = getBytes(bitmap);
                    if (imageByte.length>500000){

                        Bitmap resized = bitmap.createScaledBitmap(bitmap, 720, 720, true);
                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        resized.compress(Bitmap.CompressFormat.PNG, 0, stream1);
                        imageByte = stream1.toByteArray();

                    }else {
                        imageByte = getBytes(bitmap);
                    }
                }





                if (productId ==-1){
                    if (name.isEmpty() || company.isEmpty() || description.isEmpty() || et_price.getText().toString().isEmpty() || imageByte == null)
                    {
                        Toast.makeText(this, getResources().getString(R.string.fill_fields), Toast.LENGTH_SHORT).show();
                    }else {
                        dataBaseAccess.open();
                        ProductsModel productsModel = new ProductsModel(name,company,description,price,imageByte);
                        dataBaseAccess.insert_items(productsModel);
                        dataBaseAccess.close();
                        Toast.makeText(this, getResources().getString(R.string.added_successfully), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_CODE);
                        finish();
                    }

                }else {
                    if (et_name.getText().toString().isEmpty() || et_company.getText().toString().isEmpty() || et_des.getText().toString().isEmpty() || et_price.getText().toString().isEmpty() || imageByte == null)
                    {
                        Toast.makeText(this, getResources().getString(R.string.fill_fields), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        dataBaseAccess.open();
                        ProductsModel productsModel = new ProductsModel(name,company,description,price,imageByte);
                        dataBaseAccess.update_item(productId,productsModel);
                        dataBaseAccess.close();
                        Toast.makeText(this, getResources().getString(R.string.edit_successfully), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_CODE);
                        finish();
                    }

                }
                break;
            case R.id.delete:
                dataBaseAccess.open();
                dataBaseAccess.delete_item(productId);
                dataBaseAccess.close();
                Toast.makeText(this, getResources().getString(R.string.delete_successfully), Toast.LENGTH_SHORT).show();
                setResult(RESULT_CODE);
                finish();
                break;
        }
        return false;
    }

    public static byte[] getBytes(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE){
            if (resultCode == RESULT_OK && data != null){
                imageUri = data.getData();
                img.setImageURI(imageUri);
            }
        }
    }
}