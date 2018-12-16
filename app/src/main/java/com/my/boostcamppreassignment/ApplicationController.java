package com.my.boostcamppreassignment;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.my.boostcamppreassignment.data.XNaverClientInfo;
import com.my.boostcamppreassignment.network.NetworkService;
import com.my.boostcamppreassignment.views.toast.CustomToast;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationController extends Application {

    public NetworkService networkService;
    public static ApplicationController instance;
    CustomToast customToast;
    String baseUrl = "https://openapi.naver.com/";
    Context appContext = null;

    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        Fresco.initialize(this);
        customToast = new CustomToast(appContext);
        instance = this;

        buildNetwork();
    }

    public void buildNetwork() {

        final PersistentCookieStore cookieStore = new PersistentCookieStore(instance);
        CookieManager cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        OkHttpClient okHttpClient = new OkHttpClient();
//        var sharedPreference = getSharedPreferences(LoginToken.PREF_KEY, Context.MODE_PRIVATE);
//        LoginToken.token = sharedPreference.getString(LoginToken.PREF_KEY, null);

        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.newBuilder().addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header(XNaverClientInfo.ID.getxNaverClientKey(), XNaverClientInfo.ID.getxNaverClientValue())
                                .header(XNaverClientInfo.PWD.getxNaverClientKey(), XNaverClientInfo.PWD.getxNaverClientValue())
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                }).connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .cookieJar(new JavaNetCookieJar(cookieManager)).build()).build();

        this.networkService = retrofit.create(NetworkService.class);
    }

    public void makeCustomToast(String textToShow, int duration){
        customToast.makeToast(textToShow, duration);
    }
}
