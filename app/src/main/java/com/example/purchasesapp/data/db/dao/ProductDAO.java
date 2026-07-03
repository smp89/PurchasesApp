package com.example.purchasesapp.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.purchasesapp.data.db.entity.Product;

import java.util.List;

@Dao
public interface ProductDAO {
    @Query("SELECT * FROM products ORDER BY is_purchased ASC, name ASC")
    LiveData<List<Product>> getAllItems();

    @Insert
    void insert(Product item);

    @Update
    void update(Product item);

    @Delete
    void delete(Product item);

}
