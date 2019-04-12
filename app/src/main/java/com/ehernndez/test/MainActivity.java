package com.ehernndez.test;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.ehernndez.test.singleton.Singleton;

public class MainActivity extends AppCompatActivity {


    Singleton singleton;
    protected RequestQueue requestQueue;
    ProgressBar progressBar;
    TextView txtDisplayName;
    TextView txtDepartment;
    TextView txtSKU;
    TextView txtPrice;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        singleton = Singleton.getInstance(this);
        requestQueue = singleton.getmRequestQueue();

        progressBar = findViewById(R.id.prograssbar_horizontal);
        txtDisplayName = findViewById(R.id.txt_prod);
        txtDepartment = findViewById(R.id.txt_department);
        txtSKU = findViewById(R.id.txt_sku);
        txtPrice = findViewById(R.id.txt_price);
        btnAdd = findViewById(R.id.btn_add);

        progressBar.setVisibility(View.VISIBLE);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "No disponible", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
