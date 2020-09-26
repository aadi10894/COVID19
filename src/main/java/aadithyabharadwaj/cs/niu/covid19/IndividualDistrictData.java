package aadithyabharadwaj.cs.niu.covid19;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import aadithyabharadwaj.cs.niu.covid19.Adapters.DistrictWiseAdapter;
import aadithyabharadwaj.cs.niu.covid19.Models.DistrictWiseModel;

import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_NAME;

public class IndividualDistrictData extends AppCompatActivity
{

    //Variables

    private RecyclerView districtWiseRecyclerView;
    private DistrictWiseAdapter districtWiseAdapter;
    private ArrayList<DistrictWiseModel> districtWiseModelArrayList;
    private SwipeRefreshLayout districtWiseSwipeRefreshLayout;
    private EditText searchEditText;

    private String individualDistrictStateName, individualDistrict, districtConfirmed, districtConfirmedNew, districtActive,
            districtActiveNew, districtRecovered, districtRecoveredNew, districtDeath, districtDeathNew;

    private MainActivity activity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_district_data);
        GetDistrictIntent();

        //setting up the title to actionbar
        getSupportActionBar().setTitle("Region/District");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initialise all views
        districtInitialize();

        //Fetch Statewise data
        FetchDistrictWiseData();

        //Setting swipe refresh layout
        districtWiseSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                FetchDistrictWiseData();
                districtWiseSwipeRefreshLayout.setRefreshing(false);
            } // End onRefresh
        });

        //Search
        searchEditText.addTextChangedListener(new TextWatcher()
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
            } // Edit afterTextChanged
        });

    } // End onCreate


    private void Filter(String s)
    {
        ArrayList<DistrictWiseModel> filteredList = new ArrayList<>();
        for (DistrictWiseModel item : districtWiseModelArrayList) {
            if (item.getDistrict().toLowerCase().contains(s.toLowerCase()))
            {
                filteredList.add(item);
            } // End iff
        } // End Filters

        districtWiseAdapter.filterList(filteredList, s);
    } // End Filter

    private void FetchDistrictWiseData()
    {

        //Show progress dialog
        activity.ShowDialog(this);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiURL = "https://api.covid19india.org/v2/state_district_wise.json";

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
                            int flag=0;
                            districtWiseModelArrayList.clear();
                            for (int i=1;i<response.length();i++)
                            {
                                JSONObject jsonObjectState = response.getJSONObject(i);

                                if (individualDistrictStateName.toLowerCase().equals(jsonObjectState.getString("state").toLowerCase()))
                                {
                                    JSONArray jsonArrayDistrict = jsonObjectState.getJSONArray("districtData");

                                    for (int j=0; j<jsonArrayDistrict.length(); j++)
                                    {
                                        JSONObject jsonObjectDistrict = jsonArrayDistrict.getJSONObject(j);
                                        individualDistrict = jsonObjectDistrict.getString("district");
                                        districtConfirmed = jsonObjectDistrict.getString("confirmed");
                                        districtActive = jsonObjectDistrict.getString("active");
                                        districtDeath = jsonObjectDistrict.getString("deceased");
                                        districtRecovered = jsonObjectDistrict.getString("recovered");

                                        JSONObject jsonObjectDistNew = jsonObjectDistrict.getJSONObject("delta");
                                        districtConfirmedNew = jsonObjectDistNew.getString("confirmed");
                                        districtRecoveredNew = jsonObjectDistNew.getString("recovered");
                                        districtDeathNew = jsonObjectDistNew.getString("deceased");

                                        //Creating an object of our statewise model class and passing the values in the constructor
                                        DistrictWiseModel districtWiseModel = new DistrictWiseModel(individualDistrict, districtConfirmed,
                                                districtActive, districtRecovered, districtDeath, districtConfirmedNew, districtRecoveredNew,
                                                districtDeathNew);
                                        //adding data to our arraylist
                                        districtWiseModelArrayList.add(districtWiseModel);
                                    } // End for loop
                                    flag=1;
                                } // End if
                                if (flag==1)
                                    break;
                            } // End for loop
                            Handler makeDelay = new Handler();
                            makeDelay.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    districtWiseAdapter.notifyDataSetChanged();
                                    activity.DismissDialog();
                                }
                            }, 1000);
                        } // End try
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        } // End catch
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(jsonArrayRequest);

    } // End FetchDistrictWiseData

    private void districtInitialize()
    {
        districtWiseRecyclerView = findViewById(R.id.districtWiseRecyclerView);
        districtWiseSwipeRefreshLayout = findViewById(R.id.districtWiseSwipeRefreshLayout);
        searchEditText = findViewById(R.id.districtWiseSearchEditText);

        districtWiseRecyclerView.setHasFixedSize(true);
        districtWiseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        districtWiseModelArrayList = new ArrayList<>();
        districtWiseAdapter = new DistrictWiseAdapter(IndividualDistrictData.this, districtWiseModelArrayList);
        districtWiseRecyclerView.setAdapter(districtWiseAdapter);
    } // End districtInitialize

    private void GetDistrictIntent()
    {
        Intent intent = getIntent();
        individualDistrictStateName = intent.getStringExtra(STATE_NAME);
    } // End GetDistrictIntent

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    } // End onOptionsItemSelected

} // End IndividualDistrictData Activity
