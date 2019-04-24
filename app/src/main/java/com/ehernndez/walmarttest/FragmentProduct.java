package com.ehernndez.walmarttest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.ehernndez.walmarttest.singleton.Singleton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class FragmentProduct extends Fragment {

    TextView txtDisplayName;
    TextView txtOriginalPrice;
    TextView txtDescuentPrice;
    TextView txtMarca;
    TextView txtVendor;
    ImageView imgProduct;
    ProgressDialog progressDialog;
    protected RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        Singleton singleton = Singleton.getInstance(getContext());
        requestQueue = singleton.getmRequestQueue();

        txtDisplayName = view.findViewById(R.id.txt_display_name);
        txtOriginalPrice = view.findViewById(R.id.txt_precio_original);
        txtDescuentPrice = view.findViewById(R.id.txt_precio_descuento);
        txtMarca = view.findViewById(R.id.txt_marca);
        txtVendor = view.findViewById(R.id.txt_vendido_by);
        imgProduct = view.findViewById(R.id.img_product);

        getDataProduct();

        return view;
    }

    @SuppressLint("CheckResult")
    private void getDataProduct() {

        RestClient.getwService().getNewProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                            JsonObject jsonObject = response.getAsJsonObject();
                            JsonObject product = jsonObject.getAsJsonObject("product");
                            JsonArray child = product.getAsJsonArray("childSKUs");

                            //Obtiene descripcion del product
                            JsonObject jsonObjectChild = child.get(0).getAsJsonObject();
                            String description = jsonObjectChild.get("longDescription").getAsString();
                            String displayName = jsonObjectChild.get("displayName").getAsString();
                            String vendorName = jsonObjectChild.get("vendorName").getAsString();
                            String imageURL = jsonObjectChild.get("largeImageUrl").getAsString();

                            txtDisplayName.setText(displayName);
                            txtVendor.setText(vendorName);
                            getImage(imageURL);

                            JsonArray offerList = jsonObjectChild.get("offerList").getAsJsonArray();
                            JsonObject priceData = offerList.get(0).getAsJsonObject();
                            JsonObject priceInfo = priceData.getAsJsonObject("priceInfo");

                            //Precio

                            String originalPrice = priceInfo.get("originalPrice").getAsString();
                            String specialPrice = priceInfo.get("specialPrice").getAsString();

                            txtOriginalPrice.setText(originalPrice);
                            txtDescuentPrice.setText(specialPrice);

                            Log.e("-->", originalPrice + " " + specialPrice);

                            Log.e("-->", imageURL);

                            JsonObject dynamic = jsonObjectChild.getAsJsonObject("dynamicFacets");
                            JsonObject dataAtrr25 = dynamic.getAsJsonObject("25");

                            //Obtengo primera caracteristica
                            String attrName25 = dataAtrr25.get("attrName").getAsString();
                            String attrDesc25 = dataAtrr25.get("attrDesc").getAsString();
                            String attrValue25 = dataAtrr25.get("value").getAsString();

                            Log.e("-->", description);
                            Log.e("-->", attrName25 + " " + attrDesc25 + " " + attrValue25);

                            //Obtengo segunda caracteristica
                            JsonObject dataAttr43 = dynamic.getAsJsonObject("43");
                            String attrName43 = dataAttr43.get("attrName").getAsString();
                            String attrDesc43 = dataAttr43.get("attrDesc").getAsString();
                            String attrValue43 = dataAttr43.get("value").getAsString();

                            Log.e("-->", attrName43 + " " + attrDesc43 + " " + attrValue43);

                            //Obtengo tercera caracteristica
                            JsonObject dataAttr118 = dynamic.getAsJsonObject("118");
                            String attrName118 = dataAttr118.get("attrName").getAsString();
                            String attrDesc118 = dataAttr118.get("attrDesc").getAsString();
                            String attrValue118 = dataAttr118.get("value").getAsString();

                            Log.e("-->", attrName118 + " " + attrDesc118 + " " + attrValue118);

                            //Obtengo cuarta caracteristica
                            JsonObject dataAttr175 = dynamic.getAsJsonObject("175");
                            String attrName175 = dataAttr175.get("attrName").getAsString();
                            String attrDesc175 = dataAttr175.get("attrDesc").getAsString();
                            String attrValue175 = dataAttr175.get("value").getAsString();

                            Log.e("-->", attrName175 + " " + attrDesc175 + " " + attrValue175);

                            //Obtengo cuarta caracteristica
                            JsonObject dataAttr395 = dynamic.getAsJsonObject("395");
                            String attrName395 = dataAttr395.get("attrName").getAsString();
                            String attrDesc395 = dataAttr395.get("attrDesc").getAsString();
                            String attrValue395 = dataAttr395.get("value").getAsString();

                            Log.e("-->", attrName395 + " " + attrDesc395 + " " + attrValue395);

                            //Obtengo cuarta caracteristica
                            JsonObject dataAttr938 = dynamic.getAsJsonObject("938");
                            String attrName938 = dataAttr938.get("attrName").getAsString();
                            String attrDesc938 = dataAttr938.get("attrDesc").getAsString();
                            String attrValue938 = dataAttr938.get("value").getAsString();

                            txtMarca.setText(attrValue938);

                            Log.e("-->", attrName938 + " " + attrDesc938 + " " + attrValue938);

                        },
                        Throwable::printStackTrace
                );
    }


    public void getImage(String url) {
        ImageRequest request = new ImageRequest("https://www.walmart.com.mx" + url, new com.android.volley.Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                progressDialog.dismiss();
                 imgProduct.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    new MaterialDialog.Builder(getContext())
                            .title(getString(R.string.txt_title_dialog))
                            .titleColorRes(R.color.colorPrimary)
                            .cancelable(false)
                            .canceledOnTouchOutside(false)
                            .content(getString(R.string.txt_problema_internet))
                            .positiveText(getString(R.string.txt_btn_entendido))
                            .positiveColorRes(R.color.colorAccent)
                            .show();

                }
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.txt_enviando_espera));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
