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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import aadithyabharadwaj.cs.niu.covid19.Adapters.StateWiseAdapter;
import aadithyabharadwaj.cs.niu.covid19.Models.StateWiseModel;


public class IndividualStateData extends AppCompatActivity
{

    // Variables

    private RecyclerView stateWiseRecyclerView;
    private StateWiseAdapter stateWiseAdapter;
    private ArrayList<StateWiseModel> stateWiseModelArrayList;
    private SwipeRefreshLayout stateWiseSwipeRefreshLayout;
    private EditText searchEditText;


    private String stateName, stateConfirmed, stateConfirmedNew, stateActive, stateActiveNew, stateDeath, stateDeathNew,
                   stateRecovered, stateRecoveredNew, stateLastUpdateDate;


    private MainActivity activity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_state_data);

        //setting up the title to actionbar
        getSupportActionBar().setTitle("Select State");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initialise all views
        stateInitialize();

        //Fetch Statewise data
        FetchStateWiseData();

        //Setting swipe refresh layout
        stateWiseSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                FetchStateWiseData();
                stateWiseSwipeRefreshLayout.setRefreshing(false);
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
            } // End afterTextChanged
        });
    } // End onCreate

    private void Filter(String text)
    {
        ArrayList<StateWiseModel> filteredList = new ArrayList<>();
        for (StateWiseModel item : stateWiseModelArrayList)
        {
            if (item.getState().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            } // End if
        } // End for loop
        stateWiseAdapter.filterList(filteredList, text);
    } // End Filter

    private void FetchStateWiseData()
    {

        //Show progress dialog
        activity.ShowDialog(this);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiURL = "https://api.covid19india.org/data.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiURL,
                null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            JSONArray jsonArray = response.getJSONArray("statewise");
                            stateWiseModelArrayList.clear();

                            for (int i = 1; i < jsonArray.length() ; i++)
                            {
                                JSONObject statewise = jsonArray.getJSONObject(i);

                                //After fetching, storing the data into strings
                                stateName = statewise.getString("state");

                                stateConfirmed = statewise.getString("confirmed");
                                stateConfirmedNew = statewise.getString("deltaconfirmed");

                                stateActive = statewise.getString("active");

                                stateDeath = statewise.getString("deaths");
                                stateDeathNew = statewise.getString("deltadeaths");

                                stateRecovered = statewise.getString("recovered");
                                stateRecoveredNew = statewise.getString("deltarecovered");
                                stateLastUpdateDate = statewise.getString("lastupdatedtime");

                                //Creating an object of our statewise model class and passing the values in the constructor
                                StateWiseModel stateWiseModel = new StateWiseModel(stateName, stateConfirmed, stateConfirmedNew, stateActive,
                                                                                   stateDeath, stateDeathNew, stateRecovered, stateRecoveredNew,
                                                                                   stateLastUpdateDate);
                                //adding data to our arraylist
                                stateWiseModelArrayList.add(stateWiseModel);
                            } // End for loop

                            Handler makeDelay = new Handler();
                            makeDelay.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    stateWiseAdapter.notifyDataSetChanged();
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
                        error.printStackTrace();
                    } // End onErrorResponse
                });
        requestQueue.add(jsonObjectRequest);

    } // End FetchStateWiseData


    private void stateInitialize()
    {
        stateWiseSwipeRefreshLayout = findViewById(R.id.stateWiseSwipeRefreshLayout);
        searchEditText = findViewById(R.id.stateWiseSearchEditText);

        stateWiseRecyclerView = findViewById(R.id.stateWiseRecyclerView);
        stateWiseRecyclerView.setHasFixedSize(true);
        stateWiseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        stateWiseModelArrayList = new ArrayList<>();
        stateWiseAdapter = new StateWiseAdapter(IndividualStateData.this, stateWiseModelArrayList);
        stateWiseRecyclerView.setAdapter(stateWiseAdapter);
    } // End stateInitialize

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    } // End stateInitialize

} // End IndividualStateData
