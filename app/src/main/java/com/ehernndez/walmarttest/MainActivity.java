package com.ehernndez.walmarttest;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btnData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnData = findViewById(R.id.btn_data);

        btnData.setOnClickListener(v -> {
            FragmentProduct fragmentProduct = new FragmentProduct();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragmentProduct)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null).commit();
        });
    }
}