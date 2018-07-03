package com.example.administrator.ydxcfwpt.Bean;

/**
 * Created by Administrator on 2017/8/26.
 */

public class Fuwuxiangmu {

    private String name;
    private String price;

    @Override
    public String toString() {
        return "Fuwuxiangmu{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public Fuwuxiangmu(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public Fuwuxiangmu() {

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
