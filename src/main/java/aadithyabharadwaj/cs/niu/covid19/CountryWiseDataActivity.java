package aadithyabharadwaj.cs.niu.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import aadithyabharadwaj.cs.niu.covid19.Adapters.CountryWiseAdapter;
import aadithyabharadwaj.cs.niu.covid19.Models.CountryWiseModel;

public class CountryWiseDataActivity extends AppCompatActivity
{

    // Variables

    private RecyclerView countryWiseRecyclerView;
    private CountryWiseAdapter countryWiseAdapter;
    private ArrayList<CountryWiseModel> countryWiseModelArrayList;
    private SwipeRefreshLayout countryWiseSwipeRefreshLayout;
    private EditText countrySearchEditText;

    private String countryName, countryConfirmed, countryConfirmedNew, countryActive, countryActiveNew, countryRecovered,
            countryRecoveredNew, countryDeath, countryDeathNew, countryTests;

    private MainActivity activity = new MainActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_wise_data);
        //setting up the title to actionbar
        getSupportActionBar().setTitle("World Data (Select Country)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initialise all views
        CountryWiseInitialize();

        //Fetch Statewise data
        FetchCountryWiseData();

        //Setting swipe refresh layout
        countryWiseSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                FetchCountryWiseData();
                countryWiseSwipeRefreshLayout.setRefreshing(false);
            } // End onRefresh
        });

        //Search
        countrySearchEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            } // End beforeTextChanged

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            } // End onTextChanged

            @Override
            public void afterTextChanged(Editable s)
            {
                Filter(s.toString());
            } // End afterTextChanged
        });
    } // End onCreate

    private void Filter(String text)
    {
        ArrayList<CountryWiseModel> filteredList = new ArrayList<>();
        for (CountryWiseModel item : countryWiseModelArrayList)
        {
            if (item.getCountry().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            } // End if
        } // End for loop
        countryWiseAdapter.filterList(filteredList, text);
    } // End Filter


    private void FetchCountryWiseData()
    {
        //Show progress dialog
        activity.ShowDialog(this);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiURL = "https://corona.lmao.ninja/v2/countries";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiURL,
                null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try
                        {
                            countryWiseModelArrayList.clear();
                            for (int i=0;i<response.length(); i++)
                            {
                                JSONObject countryJSONObject = response.getJSONObject(i);

                                countryName = countryJSONObject.getString("country");
                                countryConfirmed = countryJSONObject.getString("cases");
                                countryConfirmedNew = countryJSONObject.getString("todayCases");
                                countryActive = countryJSONObject.getString("active");
                                countryRecovered = countryJSONObject.getString("recovered");
                                countryDeath = countryJSONObject.getString("deaths");
                                countryDeathNew = countryJSONObject.getString("todayDeaths");
                                countryTests = countryJSONObject.getString("tests");
                                JSONObject flagObject = countryJSONObject.getJSONObject("countryInfo");
                                String flagUrl = flagObject.getString("flag");

                                //Creating an object of our country model class and passing the values in the constructor
                                CountryWiseModel countryWiseModel  = new CountryWiseModel(countryName, countryConfirmed, countryConfirmedNew, countryActive,
                                        countryDeath, countryDeathNew, countryRecovered, countryTests, flagUrl);
                                //adding data to our arraylist
                                countryWiseModelArrayList.add(countryWiseModel);
                            } // End for loop
                            Collections.sort(countryWiseModelArrayList, new Comparator<CountryWiseModel>()
                            {
                                @Override
                                public int compare(CountryWiseModel o1, CountryWiseModel o2)
                                {
                                    if (Integer.parseInt(o1.getConfirmed())>Integer.parseInt(o2.getConfirmed()))
                                    {
                                        return -1;
                                    } // End if
                                    else
                                    {
                                        return 1;
                                    } // End else
                                } // End compare(CountryWiseModel o1, CountryWiseModel o2)
                            });
                            Handler makeDelay = new Handler();
                            makeDelay.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    countryWiseAdapter.notifyDataSetChanged();
                                    activity.DismissDialog();
                                } // End run
                            }, 1000);
                        } // End try
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        } // End catch
                    } // End onResponse
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    } // End onErrorResponse
                });
        requestQueue.add(jsonArrayRequest);
    } // End FetchCountryWiseData

    private void CountryWiseInitialize()
    {
        countryWiseSwipeRefreshLayout = findViewById(R.id.countryWiseSwipeRefreshLayout);
        countrySearchEditText = findViewById(R.id.countryWiseSearchEditText);

        countryWiseRecyclerView = findViewById(R.id.countryWiseRecyclerview);
        countryWiseRecyclerView.setHasFixedSize(true);
        countryWiseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        countryWiseModelArrayList = new ArrayList<>();
        countryWiseAdapter = new CountryWiseAdapter(CountryWiseDataActivity.this, countryWiseModelArrayList);
        countryWiseRecyclerView.setAdapter(countryWiseAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    } // End onOptionsItemSelected

} // End CountryWiseDataActivity
