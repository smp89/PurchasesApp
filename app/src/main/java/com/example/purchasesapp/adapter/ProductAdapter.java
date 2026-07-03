package com.example.purchasesapp.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purchasesapp.R;
import com.example.purchasesapp.adapter.listeners.OnItemClickListener;
import com.example.purchasesapp.adapter.listeners.OnSwitchClickListener;
import com.example.purchasesapp.data.db.entity.Product;

public class ProductAdapter extends ListAdapter<Product, ProductAdapter.ProductViewHolder> {

    private final OnItemClickListener itemClickListener;
    private final OnSwitchClickListener switchClickListener;

    public ProductAdapter(OnItemClickListener listener, OnSwitchClickListener switchClickListener) {
        super(DiffCallback);
        this.itemClickListener = listener;
        this.switchClickListener = switchClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentProduct = getItem(position);
        holder.tvName.setText(currentProduct.name);
        holder.tvCount.setText(currentProduct.amount.isEmpty() ? "0" : currentProduct.amount);
        holder.switchBuy.setChecked(currentProduct.isPurchased);

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(currentProduct);
            }
        });

        holder.switchBuy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchClickListener.onSwitchChanged(currentProduct, isChecked);
        });


        if (currentProduct.isPurchased) {
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            // Убираем флаг зачеркивания (сбрасываем маску)
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

    }

    private static final DiffUtil.ItemCallback<Product> DiffCallback = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.name.equals(newItem.name) && oldItem.amount.equals(newItem.amount)
                    && (oldItem.isPurchased == newItem.isPurchased);
        }
    };


    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvCount;
        private final Switch switchBuy;

        public ProductViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCount = itemView.findViewById(R.id.tvCount);
            switchBuy = itemView.findViewById(R.id.swBuyed);
        }
    }
}
