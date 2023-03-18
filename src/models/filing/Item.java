package models.filing;

import javax.swing.*;
import java.io.IOException;

public class Item {
    private String name;
    private double price;
    private int quantity;

    //Primary Constructor
    public Item(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    //Copy Constructor
    public Item(Item obj) {
        this.name = obj.name;
        this.price = obj.price;
        this.quantity = obj.quantity;
    }


    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
