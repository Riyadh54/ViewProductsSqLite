package com.example.viewproductssqlite.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewproductssqlite.MainActivity;
import com.example.viewproductssqlite.Model.ProductsModel;
import com.example.viewproductssqlite.R;
import com.example.viewproductssqlite.interfaces.MyInterface;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private Context context;
    private int resources;
    ArrayList<ProductsModel> data;
    MyInterface listener;
    public RecyclerAdapter(Context c,int res,ArrayList<ProductsModel> data,MyInterface myInterface){
        this.context = c;
        this.resources = res;
        this.data = data;
        this.listener = myInterface;
    }

    public ArrayList<ProductsModel> getData() {
        return data;
    }

    public void setData(ArrayList<ProductsModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(resources,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductsModel productsModel = data.get(position);
        holder.model.setText(productsModel.getModel());
        holder.company.setText(productsModel.getCompany());
        holder.price.setText(productsModel.getPrice()+"$");
        if (productsModel.getImg() != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(productsModel.getImg(), 0,productsModel.getImg().length);
            holder.img.setImageBitmap(bitmap);
        }else {
            holder.img.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickListener(productsModel.getId());

            }
        });
        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Add To Cart Methode", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView model;
        TextView company;
        TextView price;
        TextView addCart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.items_layout_img);
            model = itemView.findViewById(R.id.items_layout_tv_model);
            company = itemView.findViewById(R.id.items_layout_tv_company);
            price = itemView.findViewById(R.id.items_layout_tv_price);
            addCart = itemView.findViewById(R.id.items_layout_btn_add_cart);


        }
    }
}
