package com.example.choikn.precious;

/**
 * Created by ChoiKN on 2016-07-14.
 */
public class Product {
    int id;
    String name;
    int price;
    int maxprice;
    int minprice;
    int avgprice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(int maxprice) {
        this.maxprice = maxprice;
    }

    public int getMinprice() {
        return minprice;
    }

    public void setMinprice(int minprice) {
        this.minprice = minprice;
    }

    public int getAvgprice() {
        return avgprice;
    }

    public void setAvgprice(int avgprice) {
        this.avgprice = avgprice;
    }
}
