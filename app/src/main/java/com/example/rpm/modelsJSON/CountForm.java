package com.example.rpm.modelsJSON;

import com.google.gson.annotations.SerializedName;

public class CountForm {

    @SerializedName("name")
    public String name;

    @SerializedName("price")
    public int price;

    @SerializedName("in_stock")
    public int inStock;

    @Override
    public String toString() {
        return "CountForm{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", inStock=" + inStock +
                '}';
    }
}