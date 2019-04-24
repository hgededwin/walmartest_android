package com.ehernndez.walmarttest;

import com.google.gson.JsonElement;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RestClient {

    protected interface WService {

        @GET("rest/model/atg/commerce/catalog/ProductCatalogActor/getProduct?id=00750940180662")
        Observable<JsonElement> getNewProduct();
    }

    private static WService wService;

    public static WService getwService() {
        if (RestClient.wService == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.walmart.com.mx/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();


            RestClient.wService = retrofit.create(WService.class);
        }

        return RestClient.wService;
    }
}