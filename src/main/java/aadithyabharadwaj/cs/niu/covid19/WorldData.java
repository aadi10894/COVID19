package aadithyabharadwaj.cs.niu.covid19;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

public class WorldData extends AppCompatActivity
{

    // Variables

    TextView worldConfirmedTextView, worldConfirmedNewTextView, worldActiveTextView, worldActiveNewTextView, worldRecoveredTextView,
             worldRecoveredNewTextView, worlddDeathTextView, worldDeathNewTextView, worldTestsTextView;

    SwipeRefreshLayout worldSwipeRefreshLayout;

    String worldConfirmed, worldConfirmedNew, worldActive, worldActiveNew, worldRecovered, worldRecoveredNew, worldDeath,
           worldDeathNew, worldTests;

    LinearLayout worldCountryWiseLinearLayout;

    ProgressDialog progressDialog;

    PieChart worldPieChart;

    private int worldActiveNewInteger = 0;

    private MainActivity activity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setting up the titlebar text
        getSupportActionBar().setTitle("Covid-19 Tracker (World)");

        //Initialise UI
        worldInitialize();

        //Fetch world's data
        FetchWorldData();

        worldSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                FetchWorldData();
                worldSwipeRefreshLayout.setRefreshing(false);
                //Toast.makeText(MainActivity.this, "Data refreshed!", Toast.LENGTH_SHORT).show();
            } // End onRefresh
        }); // End worldSwipeRefreshLayout.setOnRefreshListener

        worldCountryWiseLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(WorldData.this, "Country wise data", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(WorldData.this, CountryWiseDataActivity.class));
            } // End onClick
        }); // End worldCountryWiseLinearLayout.setOnClickListener



    } // End onCreate

    private void FetchWorldData()
    {

        //show dialog
        activity.ShowDialog(this);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://corona.lmao.ninja/v2/all";
        worldPieChart.clearChart();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {

                        try
                        {
                            //Fetching data from API and storing into string
                            worldConfirmed = response.getString("cases");
                            worldConfirmedNew = response.getString("todayCases");
                            worldActive = response.getString("active");
                            worldRecovered = response.getString("recovered");
                            worldRecoveredNew = response.getString("todayRecovered");
                            worldDeath = response.getString("deaths");
                            worldDeathNew = response.getString("todayDeaths");
                            worldTests = response.getString("tests");

                            Handler delayToshowProgress = new Handler();
                            delayToshowProgress.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    // setting up texted in the text view
                                    worldConfirmedTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(worldConfirmed)));
                                    worldConfirmedNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(worldConfirmedNew)));

                                    worldActiveTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(worldActive)));

                                    worldActiveNewInteger = Integer.parseInt(worldConfirmedNew)
                                            - (Integer.parseInt(worldRecoveredNew) + Integer.parseInt(worldDeathNew));
                                    worldActiveNewTextView.setText("+"+NumberFormat.getInstance().format(worldActiveNewInteger));

                                    worldRecoveredTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(worldRecovered)));
                                    worldRecoveredNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(worldRecoveredNew)));

                                    worlddDeathTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(worldDeath)));
                                    worldDeathNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(worldDeathNew)));

                                    worldTestsTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(worldTests)));

                                    worldPieChart.addPieSlice(new PieModel("Active", Integer.parseInt(worldActive), Color.parseColor("#007afe")));
                                    worldPieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(worldRecovered), Color.parseColor("#08a045")));
                                    worldPieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(worldDeath), Color.parseColor("#F6404F")));

                                    worldPieChart.startAnimation();

                                    activity.DismissDialog();

                                } // End rund
                            },1000);
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
        requestQueue.add(jsonObjectRequest);
    } // End FetchWorldData

    private void worldInitialize()
    {
        worldConfirmedTextView = findViewById(R.id.worldConfirmedNumber);
        worldConfirmedNewTextView = findViewById(R.id.worldConfirmedNew);
        worldActiveTextView = findViewById(R.id.worldActiveNumber);
        worldActiveNewTextView = findViewById(R.id.worldActiveNew);
        worldRecoveredTextView = findViewById(R.id.worldRecoveredNumber);
        worldRecoveredNewTextView = findViewById(R.id.worldRecoveredNew);
        worlddDeathTextView = findViewById(R.id.worldDeathNumber);
        worldDeathNewTextView = findViewById(R.id.worldDeathNew);
        worldTestsTextView = findViewById(R.id.worldTestsNumber);
        worldSwipeRefreshLayout = findViewById(R.id.worldswiperefreshlayout);
        worldPieChart = findViewById(R.id.worldPieChart);
        worldCountryWiseLinearLayout = findViewById(R.id.worldCountrywiseLinearLayout);
    } // End Initialize
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    } // End onOptionsItemSelected


} // End WorldData Activity