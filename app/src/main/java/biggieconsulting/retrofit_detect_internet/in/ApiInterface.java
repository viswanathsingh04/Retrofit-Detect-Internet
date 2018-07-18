package biggieconsulting.retrofit_detect_internet.in;

import biggieconsulting.retrofit_detect_internet.model.GetCountry;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by VPS on 23-02-2018.
 */

public interface ApiInterface {

    String URL = "http://www.biggieconsulting.com/" + "courier/api/v1/";

    @GET("locations/countries")
    Call<GetCountry> GetCountries();

    interface InternetConnectionListener {

        void onInternetUnavailable();

        void onCacheUnavailable();
    }
}