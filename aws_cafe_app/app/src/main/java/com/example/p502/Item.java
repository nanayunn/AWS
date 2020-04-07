package com.example.p502;

import java.io.Serializable;

public class Item implements Serializable{
    String name;
    String image;
    int star;
    String description;
    double leti;
    double longti;

    public Item(){
        super();
    }

    public Item(String name, String image, int star, String description, double leti, double longti) {
        this.name = name;
        this.image = image;
        this.star = star;
        this.description = description;
        this.leti = leti;
        this.longti = longti;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLeti() {
        return leti;
    }

    public void setLeti(double leti) {
        this.leti = leti;
    }

    public double getLongti() {
        return longti;
    }

    public void setLongti(double longti) {
        this.longti = longti;
    }
}
