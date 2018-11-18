package com.antelope.android.bottomnavigation.net;

import com.antelope.android.bottomnavigation.data.DS;
import com.antelope.android.bottomnavigation.data.IntegerData;
import com.antelope.android.bottomnavigation.data.Picture;

import io.reactivex.Flowable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public class APIService {

    public APIs apis;
    private static APIService instance;

    public static APIService getInstance(){
        if (instance == null){
            instance = new APIService();
        }
        return instance;
    }

    private APIService(){
        Retrofit storeRestAPI = new Retrofit.Builder().baseUrl("http://api.heclouds.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apis = storeRestAPI.create(APIs.class);
    }

    public interface APIs{
        @Headers("api-key:xxx")
        @GET("devices/xxxxxxxx/datastreams/pic10")
        Flowable<DS<Picture>> latestPic();

       /*@Headers("api-key:xxx")
        @GET("bindata/{index}")
        Flowable<Picture> getPic(@Path("index") String index);*/

        @Headers("api-key:xxx")
        @GET("devices/xxxxxxxx/datastreams/Temperature")
        Flowable<DS<IntegerData>> lastestTemp();

        @Headers("api-key:xxx")
        @GET("devices/xxxxxxxx/datastreams/Humidity")
        Flowable<DS<IntegerData>> lastestHum();

        @Headers("api-key:xxx")
        @GET("devices/xxxxxxxx/datastreams/door:")
        Flowable<DS<IntegerData>> lastestDState();

        @Headers("api-key:xxx")
        @GET("devices/xxxxxxxx/datastreams/{data}")
        Flowable<DS<IntegerData>> latest(@Path("data") String data);

        /*@Headers("api-key:xxx")
        @POST("cmds?device_id=xxxxxxxx&qos=0&timeout=300")*/

    }


}
