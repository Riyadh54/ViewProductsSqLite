package com.example.viewproductssqlite.Model;

public class ProductsModel {
    private int id ;
    private String model;
    private String company;
    private String description;
    private int price;
    private byte[] img;

    public ProductsModel(int id, String model, String company, String description, int price, byte[] img) {
        this.id = id;
        this.model = model;
        this.company = company;
        this.description = description;
        this.price = price;
        this.img = img;
    }

    public ProductsModel(String model, String company, String description, int price, byte[] img) {
        this.model = model;
        this.company = company;
        this.description = description;
        this.price = price;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
