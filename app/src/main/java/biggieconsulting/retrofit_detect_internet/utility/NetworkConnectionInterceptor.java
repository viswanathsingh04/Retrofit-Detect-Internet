package biggieconsulting.retrofit_detect_internet.utility;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public abstract class NetworkConnectionInterceptor implements Interceptor {

    public abstract boolean isInternetAvailable();

    public abstract void onInternetUnavailable();

    public abstract void onCacheUnavailable();

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!isInternetAvailable()) {
            onInternetUnavailable();
            request = request.newBuilder().header("Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24).build();
            Response response = chain.proceed(request);
            if (response.cacheResponse() == null) {
                onCacheUnavailable();
            }
            return response;
        }
        return chain.proceed(request);
    }
}
