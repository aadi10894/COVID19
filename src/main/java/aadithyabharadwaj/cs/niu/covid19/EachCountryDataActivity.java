package aadithyabharadwaj.cs.niu.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;

import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_ACTIVE;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_CONFIRMED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_DECEASED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_NAME;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_NEW_CONFIRMED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_NEW_DECEASED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_RECOVERED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_TESTS;


public class EachCountryDataActivity extends AppCompatActivity
{
    // Variables

    private TextView eachCountryConfirmedTextView, eachCountryConfirmedNewTextView, eachCountryActiveTextView,
            eachCountryActiveNewTextView, eachCountryDeathTextView, eachCountryDeathNewTextView, eachCountryRecoveredTextView,
            eachCountryRecoveredNewTextView, eachCountryTestsTextView;

    private String eachCountryCountryName, eachCountryConfirmed, eachCountryConfirmedNew, eachCountryActive, eachCountryActiveNew,
            eachCountryDeath, eachCountryDeathNew, eachCountryRecovered, eachCountryRecoveredNew, eachCountryTests;
    private PieChart eachCountryPieChart;
    private int eachCountryActiveNewInteger = 0;
    private MainActivity activity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_country_data);

        //Fetching data which is passed from previous activity into this activity
        GetEachCountryIntent();

        //Setting up the title of actionbar as State name
        getSupportActionBar().setTitle(eachCountryCountryName);

        //back menu icon on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initialise all textviews
        EachCountryInitialize();

        LoadCountryData();

    } // End onCreate


    private void EachCountryInitialize()
    {
        eachCountryConfirmedTextView = findViewById(R.id.eachCountryConfirmedNumber);
        eachCountryConfirmedNewTextView = findViewById(R.id.eachCountryConfirmedNew);
        eachCountryActiveTextView = findViewById(R.id.eachCountryActiveNumber);
        eachCountryActiveNewTextView = findViewById(R.id.eachCountryActiveNew);
        eachCountryRecoveredTextView = findViewById(R.id.eachCountryRecoveredNumber);
        eachCountryRecoveredNewTextView = findViewById(R.id.eachCountryRecoveredNew);
        eachCountryDeathTextView = findViewById(R.id.eachCountryDeathNumber);
        eachCountryDeathNewTextView = findViewById(R.id.eachCountryDeathNew);
        eachCountryTestsTextView = findViewById(R.id.eachCountryTestsNumber);
        eachCountryPieChart = findViewById(R.id.eachCountryPieChart);
    } // End EachCountryInitialize

    private void LoadCountryData()
    {
        //Show dialog
        activity.ShowDialog(this);

        Handler postDelayToshowProgress = new Handler();
        postDelayToshowProgress.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                eachCountryConfirmedTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(eachCountryConfirmed)));
                eachCountryConfirmedNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(eachCountryConfirmedNew)));

                eachCountryActiveTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(eachCountryActive)));
                //eachCountryActiveNewInteger = Integer.parseInt(eachCountryConfirmedNew)
                  //      - (Integer.parseInt(eachCountryRecoveredNew) + Integer.parseInt(eachCountryDeathNew));
                //eachCountryActiveNewTextView.setText("+"+NumberFormat.getInstance().format(eachCountryActiveNewInteger<0 ? 0 : eachCountryActiveNewInteger));
                eachCountryActiveNewTextView.setText("N/A");

                eachCountryDeathTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(eachCountryDeath)));
                //eachCountryDeathNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(eachCountryDeathNew)));

                eachCountryRecoveredTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(eachCountryRecovered)));
                //eachCountryRecoveredNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(eachCountryRecoveredNew)));
                eachCountryRecoveredNewTextView.setText("N/A");

                eachCountryTestsTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(eachCountryTests)));

                //setting piechart
                eachCountryPieChart.addPieSlice(new PieModel("Active", Integer.parseInt(eachCountryActive), Color.parseColor("#007afe")));
                eachCountryPieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(eachCountryRecovered), Color.parseColor("#08a045")));
                eachCountryPieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(eachCountryDeath), Color.parseColor("#F6404F")));

                eachCountryPieChart.startAnimation();

                activity.DismissDialog();
            } // End run
        },1000);

    } // End LoadCountryData

    private void GetEachCountryIntent ()
    {
        Intent intent = getIntent();
        eachCountryCountryName = intent.getStringExtra(COUNTRY_NAME);
        eachCountryConfirmed = intent.getStringExtra(COUNTRY_CONFIRMED);
        eachCountryActive = intent.getStringExtra(COUNTRY_ACTIVE);
        eachCountryDeath = intent.getStringExtra(COUNTRY_DECEASED);
        eachCountryRecovered = intent.getStringExtra(COUNTRY_RECOVERED);
        eachCountryConfirmedNew = intent.getStringExtra(COUNTRY_NEW_CONFIRMED);
        eachCountryDeathNew = intent.getStringExtra(COUNTRY_NEW_DECEASED);
        eachCountryTests = intent.getStringExtra(COUNTRY_TESTS);
    } // End GetEachCountryIntent
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    } // End onOptionsItemSelected
} // End EachCountryDataActivity


