package biggieconsulting.retrofit_detect_internet.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import biggieconsulting.retrofit_detect_internet.R;
import biggieconsulting.retrofit_detect_internet.adapter.CountryAdapter;
import biggieconsulting.retrofit_detect_internet.in.ApiInterface;
import biggieconsulting.retrofit_detect_internet.model.Country;
import biggieconsulting.retrofit_detect_internet.model.GetCountry;
import biggieconsulting.retrofit_detect_internet.utility.App;
import biggieconsulting.retrofit_detect_internet.utility.GlobalActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends GlobalActivity implements ApiInterface.InternetConnectionListener {

    List<Country> data;
    private TextView mView;
    private RecyclerView recyclerView;
    CountryAdapter countryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        mView =  findViewById(R.id.view);
        data = new ArrayList<>();
        ((App) getApplication()).setInternetConnectionListener(this);
        getCountries();
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        countryAdapter = new CountryAdapter(MainActivity.this, data);
        recyclerView.setAdapter(countryAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((App) getApplication()).removeInternetConnectionListener();
    }

    private void getCountries() {
        ((App) getApplication()).getApiService().GetCountries().enqueue(new Callback<GetCountry>() {
            @Override
            public void onResponse(@NonNull Call<GetCountry> call, @NonNull Response<GetCountry> response) {
                if (response.isSuccessful()) {
                    GetCountry getCountry = response.body();
                    assert getCountry != null;
                    if (getCountry.getStatus().equals("success") && getCountry.getCode() == 200) {
                        try {
                            List<Country> sampleData = fetchResults(response);
                            Log.d("Countries_List", "proceeded");
                            if (sampleData != null && sampleData.size() > 0) {
                                data.addAll(sampleData);
                            }
                            countryAdapter.notifyDataSetChanged();
                            System.out.println("Countries_Size-->>" + data.size());
                        } catch (Exception e) {
                            e.printStackTrace();
                            dismissProgressDialog();
                        }
                    } else if (getCountry.getStatus().equals("error") && getCountry.getCode() == 404) {
                        Toast.makeText(MainActivity.this, "Issues Occured, Please try again...", Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                    }
                    dismissProgressDialog();
                } else {
                    Toast.makeText(MainActivity.this, "Fails", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCountry> call, @NonNull Throwable t) {
                t.printStackTrace();
                dismissProgressDialog();
                Toast.makeText(MainActivity.this, "Server Connectivity Issues Found.", Toast.LENGTH_SHORT).show();
                Log.d("Main", "onResponse: 3");
            }
        });
    }

    private List<Country> fetchResults(Response<GetCountry> response) {
        GetCountry status = response.body();
        assert status != null;
        status.getCountries().getClass().getName();
        return status.getCountries();
    }

    @Override
    public void onInternetUnavailable() {
        // show temporary UI
        // Toast or Snackbar
        mView.setVisibility(View.VISIBLE);
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Internet is not available...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCacheUnavailable() {
        // no content to show
        // hide content UI
        // show a full covered UI saying 'No Internet'
        // or 'No Content Available'
    }

}
