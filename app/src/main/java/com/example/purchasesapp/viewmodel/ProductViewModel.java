package com.example.purchasesapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.purchasesapp.data.db.AppDatabase;
import com.example.purchasesapp.data.db.dao.ProductDAO;
import com.example.purchasesapp.data.db.entity.Product;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private final ProductDAO productDAO;
    private final LiveData<List<Product>> allProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        productDAO = db.productDAO();
        allProducts = productDAO.getAllItems();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }
}
