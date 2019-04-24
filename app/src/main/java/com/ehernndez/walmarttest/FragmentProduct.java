package com.ehernndez.walmarttest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import lib.kingja.switchbutton.SwitchMultiButton;


public class FragmentProduct extends Fragment {

    TextView txtDisplayName;
    TextView txtOriginalPrice;
    TextView txtDescuentPrice;
    TextView txtMarca;
    TextView txtVendor;
    TextView txtDescription;
    ImageView imgProduct;
    SwitchMultiButton switchMultiButton;
    ProgressDialog progressDialog;
    protected RequestQueue requestQueue;

    //Strings Caracteristicas del producto
    String attrName25;
    String attrDesc25;
    String attrValue25;

    String attrName43;
    String attrDesc43;
    String attrValue43;

    String attrName118;
    String attrDesc118;
    String attrValue118;

    String attrName175;
    String attrDesc175;
    String attrValue175;

    String attrName395;
    String attrDesc395;
    String attrValue395;

    String attrName938;
    String attrDesc938;
    String attrValue938;

    String description;

    String caract25;
    String caract43;
    String caract118;
    String caract175;
    String caract395;
    String caract938;

    String caracteristicas;

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
        txtDescription = view.findViewById(R.id.txt_descr);
        imgProduct = view.findViewById(R.id.img_product);
        switchMultiButton = view.findViewById(R.id.switch_desc);

        getDataProduct();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.txt_enviando_espera));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        switchMultiButton.setOnSwitchListener((position, tabText) -> {
            if (position == 0) {
                txtDescription.setText(description);
            } else if (position == 1) {

                caract25 = "<b>" + attrDesc25 + "</b>" + "<br>" + attrName25 + ": " + attrValue25 + "<br>";
                caract43 = "<b>" + attrDesc43 + "</b>" + "<br>" + attrName43 + ": " + attrValue43 + "<br>";
                caract118 = "<b>" + attrDesc118 + "</b>" + "<br>" + attrName118 + ": " + attrValue118 + "<br>";
                caract175 = "<b>" + attrDesc175 + "</b>" + "<br>" + attrName175 + ": " + attrValue175 + "<br>";
                caract395 = "<b>" + attrDesc395 + "</b>" + "<br>" + attrName395 + ": " + attrValue395 + "<br>";
                caract938 = "<b>" + attrDesc938 + "</b>" + "<br>" + attrName938 + ": " + attrValue938 + "<br>";
                caracteristicas = caract25 + "<br>" + caract43 + "<br>" + caract118 + "<br>" + caract175 + "<br>" + caract395 + "<br>" + caract938;

                txtDescription.setText(Html.fromHtml(caracteristicas));
            }
        });

        return view;
    }

    @SuppressLint("CheckResult")
    private void getDataProduct() {

        RestClient.getwService().getNewProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                            progressDialog.dismiss();

                            JsonObject jsonObject = response.getAsJsonObject();
                            JsonObject product = jsonObject.getAsJsonObject("product");
                            JsonArray child = product.getAsJsonArray("childSKUs");

                            //Obtiene descripcion del product
                            JsonObject jsonObjectChild = child.get(0).getAsJsonObject();
                            description = jsonObjectChild.get("longDescription").getAsString();
                            String displayName = jsonObjectChild.get("displayName").getAsString();
                            String vendorName = jsonObjectChild.get("vendorName").getAsString();
                            String imageURL = jsonObjectChild.get("largeImageUrl").getAsString();

                            txtDisplayName.setText(displayName);
                            String vendor = "<b>" + "Vendido por: " + "</b>" + vendorName;

                            txtVendor.setText(Html.fromHtml(vendor));
                            txtDescription.setText(description);
                            getImage(imageURL);

                            JsonArray offerList = jsonObjectChild.get("offerList").getAsJsonArray();
                            JsonObject priceData = offerList.get(0).getAsJsonObject();
                            JsonObject priceInfo = priceData.getAsJsonObject("priceInfo");

                            //Precio

                            String originalPrice = priceInfo.get("originalPrice").getAsString();
                            String specialPrice = priceInfo.get("specialPrice").getAsString();

                            String original = "<strike>" + "$" + originalPrice + "</strike>";
                            txtOriginalPrice.setText(Html.fromHtml(original));
                            String special = "$" + specialPrice;
                            txtDescuentPrice.setText(special);

                            Log.e("-->", originalPrice + " " + specialPrice);

                            Log.e("-->", imageURL);

                            JsonObject dynamic = jsonObjectChild.getAsJsonObject("dynamicFacets");
                            JsonObject dataAtrr25 = dynamic.getAsJsonObject("25");

                            //Obtengo primera caracteristica
                            attrName25 = dataAtrr25.get("attrName").getAsString();
                            attrDesc25 = dataAtrr25.get("attrDesc").getAsString();
                            attrValue25 = dataAtrr25.get("value").getAsString();

                            Log.e("-->", description);
                            Log.e("-->", attrName25 + " " + attrDesc25 + " " + attrValue25);

                            //Obtengo segunda caracteristica
                            JsonObject dataAttr43 = dynamic.getAsJsonObject("43");
                            attrName43 = dataAttr43.get("attrName").getAsString();
                            attrDesc43 = dataAttr43.get("attrDesc").getAsString();
                            attrValue43 = dataAttr43.get("value").getAsString();

                            Log.e("-->", attrName43 + " " + attrDesc43 + " " + attrValue43);

                            //Obtengo tercera caracteristica
                            JsonObject dataAttr118 = dynamic.getAsJsonObject("118");
                            attrName118 = dataAttr118.get("attrName").getAsString();
                            attrDesc118 = dataAttr118.get("attrDesc").getAsString();
                            attrValue118 = dataAttr118.get("value").getAsString();

                            Log.e("-->", attrName118 + " " + attrDesc118 + " " + attrValue118);

                            //Obtengo cuarta caracteristica
                            JsonObject dataAttr175 = dynamic.getAsJsonObject("175");
                            attrName175 = dataAttr175.get("attrName").getAsString();
                            attrDesc175 = dataAttr175.get("attrDesc").getAsString();
                            attrValue175 = dataAttr175.get("value").getAsString();

                            Log.e("-->", attrName175 + " " + attrDesc175 + " " + attrValue175);

                            //Obtengo cuarta caracteristica
                            JsonObject dataAttr395 = dynamic.getAsJsonObject("395");
                            attrName395 = dataAttr395.get("attrName").getAsString();
                            attrDesc395 = dataAttr395.get("attrDesc").getAsString();
                            attrValue395 = dataAttr395.get("value").getAsString();

                            Log.e("-->", attrName395 + " " + attrDesc395 + " " + attrValue395);

                            //Obtengo cuarta caracteristica
                            JsonObject dataAttr938 = dynamic.getAsJsonObject("938");
                            attrName938 = dataAttr938.get("attrName").getAsString();
                            attrDesc938 = dataAttr938.get("attrDesc").getAsString();
                            attrValue938 = dataAttr938.get("value").getAsString();

                            String marca = "<b>" + "Marca: " + "</b>" + attrValue938;
                            txtMarca.setText(Html.fromHtml(marca));

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
