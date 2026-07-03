package com.example.purchasesapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.support.PrePackagedCopyOpenHelper;

import com.example.purchasesapp.adapter.ProductAdapter;
import com.example.purchasesapp.adapter.listeners.OnItemClickListener;
import com.example.purchasesapp.data.db.AppDatabase;
import com.example.purchasesapp.data.db.entity.Product;
import com.example.purchasesapp.viewmodel.ProductViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private AppDatabase database;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private ProductViewModel userViewModel;
    private Product currentProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appbar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        currentProduct = null;

        database = AppDatabase.getDatabase(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.rvProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ProductAdapter adapter = new ProductAdapter(item -> {
                currentProduct = item;
                showDialog(DialogType.EditProduct);
                currentProduct = null;
        }, (item, isChecked) -> {
            Product editItem = new Product(item);
            editItem.isPurchased = isChecked;
            executorService.execute(() -> database.productDAO().update(editItem));
        });
        recyclerView.setAdapter(adapter);


        userViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        userViewModel.getAllProducts().observe(this, adapter::submitList);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> showDialog(DialogType.AddProduct));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    private void showDialog(DialogType dialogType) {

        String title = "";
        Product currentItem = currentProduct;

        if (dialogType == DialogType.AddProduct) {
            title = getString(R.string.add_dialog_title);
        } else if (dialogType == DialogType.EditProduct) {
            title = getString(R.string.edit_dialog_title);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);
        EditText inputName = viewInflated.findViewById(R.id.input_name);
        EditText inputAmount = viewInflated.findViewById(R.id.input_amount);
        Switch switchBuyed = viewInflated.findViewById(R.id.switchBuyed);

        if (currentItem != null) {
            inputName.setText(currentItem.name);
            inputAmount.setText(currentItem.amount);
            switchBuyed.setChecked(currentItem.isPurchased);
        }

        builder.setView(viewInflated);
        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            Product item = new Product();
            item.name = inputName.getText().toString();
            item.amount = inputAmount.getText().toString();
            item.isPurchased = switchBuyed.isChecked();

            if (currentItem == null) {
                executorService.execute(() -> database.productDAO().insert(item));
            } else  {
                item.id = currentItem.id;
                executorService.execute(() -> database.productDAO().update(item));
            }
        });
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.show();
        Button saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        saveButton.setEnabled(!inputName.getText().toString().trim().isEmpty());
        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveButton.setEnabled(charSequence != null && !charSequence.toString().trim().isEmpty());
            }
        });
    }

    enum DialogType {
        AddProduct,
        EditProduct
    }
}