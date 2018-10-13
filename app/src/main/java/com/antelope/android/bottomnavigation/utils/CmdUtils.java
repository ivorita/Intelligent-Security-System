package com.antelope.android.bottomnavigation.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CmdUtils {

    private OkHttpClient mOkHttpClient;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    String url = "http://api.heclouds.com/cmds?device_id=30964714&qos=0&timeout=300";

    public CmdUtils(){
        mOkHttpClient = new OkHttpClient();
    }

    public void cmd(String json){

        /*String json = "Fire:1";*/
        RequestBody requestBody = RequestBody.create(JSON,json);

        Request request = new Request.Builder()
                .url("http://api.heclouds.com/cmds?device_id=30964714&qos=0&timeout=300")
                .addHeader("api-key","Uv=e=yMBymo8In9FVA4Ub16Oleo=")
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200) {
                    System.out.println("cmd" + response.body().string());
                }
            }
        });
    }

}
