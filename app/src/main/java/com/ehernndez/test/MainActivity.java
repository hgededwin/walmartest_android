package com.ehernndez.test;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ehernndez.test.singleton.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

        getDataProduct();
    }

    private void getDataProduct() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.txt_url), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.GONE);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    //get skuDisplayName
                    String dispName = jsonObject.getString("skuDisplayNameText");
                    String skuDisplay = "<b>" + "<font color='#424242'>" + "Producto: " + "</font>" + "</u>" + "</b>" + dispName;
                    txtDisplayName.setText(Html.fromHtml(skuDisplay));

                    //get department
                    String dep = jsonObject.getString("department");
                    String department = "<b>" + "<font color='#424242'>" + "Departamento: " + "</font>" + "</u>" + "</b>" + dep;
                    txtDepartment.setText(Html.fromHtml(department));

                    //get SKU ID
                    String sku = jsonObject.getString("skuId");
                    String skuID = "<b>" + "<font color='#424242'>" + "SKU: " + "</font>" + "</u>" + "</b>" + sku;
                    txtSKU.setText(Html.fromHtml(skuID));

                    //get Price
                    String price = jsonObject.getString("basePrice");
                    String basePrice = "<b>" + "<font color='#424242'>" + "Precio: " + "</font>" + "</u>" + "</b>" + "$" + price;
                    txtPrice.setText(Html.fromHtml(basePrice));

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("accept", "application/json");
                params.put("connection", "keep-alive");
                params.put("Content-Type", "application/json");
                params.put("cookie", "JSESSIONID_GR=ewnljk%2BfgPUYt2Uks5PY-vG9.restapp-298183240-6-359372925;" +
                        " TS01f4281b=0130aff232b466ffcc4072d1bbce44ecbbd89f5d479ff3da4fb92ddaa94160888e2ca908d1fa72efd98d848e3e8531b6c0d47a99fe; " +
                        "akavpau_vp_super=1544643414~id%3D0b950c261050bdafe78b05c390a1fd75; dtCookie=%7CbWV4aWNvX19ncm9jZXJpZXN8MA; " +
                        "TS01c7b722=0130aff232b466ffcc4072d1bbce44ecbbd89f5d479ff3da4fb92ddaa94160888e2ca908d1fa72efd98d848e3e8531b6c0d47a99fe");
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
