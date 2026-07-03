package com.example.purchasesapp.adapter.listeners;

import com.example.purchasesapp.data.db.entity.Product;

public interface OnSwitchClickListener {
    void onSwitchChanged(Product currentProduct, boolean isChecked);
}
