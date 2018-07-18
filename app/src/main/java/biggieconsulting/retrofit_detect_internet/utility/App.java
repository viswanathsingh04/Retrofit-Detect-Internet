package biggieconsulting.retrofit_detect_internet.utility;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.TimeUnit;

import biggieconsulting.retrofit_detect_internet.in.ApiInterface;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private ApiInterface apiService;
    private ApiInterface.InternetConnectionListener mInternetConnectionListener;
    public static final int DISK_CACHE_SIZE = 10 * 1024 * 1024; // 10 MB

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setInternetConnectionListener(ApiInterface.InternetConnectionListener listener) {
        mInternetConnectionListener = listener;
    }

    public void removeInternetConnectionListener() {
        mInternetConnectionListener = null;
    }

    public ApiInterface getApiService() {
        if (apiService == null) {
            apiService = provideRetrofit(ApiInterface.URL).create(ApiInterface.class);
        }
        return apiService;
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private Retrofit provideRetrofit(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    private OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.cache(getCache());

        okhttpClientBuilder.addInterceptor(new NetworkConnectionInterceptor() {
            @Override
            public boolean isInternetAvailable() {
                return App.this.isInternetAvailable();
            }

            @Override
            public void onInternetUnavailable() {
                if (mInternetConnectionListener != null) {
                    mInternetConnectionListener.onInternetUnavailable();
                }
            }

            @Override
            public void onCacheUnavailable() {
                if (mInternetConnectionListener != null) {
                    mInternetConnectionListener.onCacheUnavailable();
                }
            }
        });

        return okhttpClientBuilder.build();
    }

    public Cache getCache() {
        File cacheDir = new File(getCacheDir(), "cache");
        return new Cache(cacheDir, DISK_CACHE_SIZE);
    }
    /*private ApiInterface apiService;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public ApiInterface getApiService() {
        if (apiService == null) {
            apiService = provideRetrofit(ApiInterface.URL).create(ApiInterface.class);
        }
        return apiService;
    }

    private Retrofit provideRetrofit(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    private OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(60, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(60, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(60, TimeUnit.SECONDS);
        return okhttpClientBuilder.build();
    }*/
}
