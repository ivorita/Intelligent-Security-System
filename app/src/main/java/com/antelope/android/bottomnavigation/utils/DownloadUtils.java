package com.antelope.android.bottomnavigation.utils;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadUtils {

    private OkHttpClient mOkHttpClient;

    public DownloadUtils(){
        mOkHttpClient = new OkHttpClient();
    }

    public Observable<byte[]> downloadImg(final String index) {
        final String url = "http://api.heclouds.com/bindata/" + index;
        return Observable.create(new ObservableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(final ObservableEmitter<byte[]> emitter) throws Exception {
                final Request request = new Request.Builder()
                        .url(url)
                        .addHeader("api-key","Uv=e=yMBymo8In9FVA4Ub16Oleo=")
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            byte[] result = response.body().bytes();
                            if (result != null){
                                emitter.onNext(result);
                            }
                        }
                        emitter.onComplete();
                    }
                });
            }
        });

    }
}
