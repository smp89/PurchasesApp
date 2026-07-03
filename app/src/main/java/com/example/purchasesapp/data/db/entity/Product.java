package com.example.purchasesapp.data.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    public Product() {
    }

    public Product(Product item) {
        id = item.id;
        name = item.name;
        amount = item.name;;
        isPurchased = item.isPurchased;
    }


    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "amount")
    public String amount;

    @ColumnInfo(name = "is_purchased")
    public boolean isPurchased;

}
