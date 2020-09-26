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

import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_ACTIVE;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_CONFIRMED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_CONFIRMED_NEW;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_DEATH;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_DEATH_NEW;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_NAME;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_RECOVERED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_RECOVERED_NEW;

public class EachDistrictDataActivity extends AppCompatActivity
{
    // Variables
    private TextView eachDistrictConfirmedTextView, eachDistrictConfirmedNewTextView, eachDistrictActiveTextView,
                     eachDistrictActiveNewTextView, eachDistrictRecoveredTextView, eachDistrictRecoveredNewTextView,
                     eachDistrictDeathTextView, eachDistrictDeathNewTextView;

    private String eachDistrictName, eachDistrictConfirmed, eachDistrictConfirmedNew, eachDistrictActive, eachDistrictActiveNew,
                   eachDistrictstrDeath, eachDistrictDeathNew, eachDistrictRecovered, eachDistrictRecoveredNew;

    private PieChart eachDistrictPieChart;

    private int eachDistrictActiveNewInteger = 0;

    private MainActivity activity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_district_data);

        //Fetching data which is passed from previous activity into this activity
        GetEachDistrictIntent();

        //Setting up the title of actionbar as State name
        getSupportActionBar().setTitle(eachDistrictName);

        //back menu icon on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initialise all textviews
        EachDistrictInitialize();

        //Load data
        LoadDistrictData();
    } // End onCreate

    private void LoadDistrictData()
    {
        //Show dialog
        activity.ShowDialog(this);

        Handler postDelayToshowProgress = new Handler();
        postDelayToshowProgress.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                eachDistrictConfirmedTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(eachDistrictConfirmed)));
                eachDistrictConfirmedNewTextView.setText("+"+ NumberFormat.getInstance().format(Integer.parseInt(eachDistrictConfirmedNew)));

                eachDistrictActiveTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(eachDistrictActive)));
                eachDistrictActiveNewInteger = Integer.parseInt(eachDistrictConfirmedNew)
                        - (Integer.parseInt(eachDistrictRecoveredNew) + Integer.parseInt(eachDistrictDeathNew));
                eachDistrictActiveNewTextView.setText("+"+ NumberFormat.getInstance().format(eachDistrictActiveNewInteger<0 ? 0 : eachDistrictActiveNewInteger));

                eachDistrictDeathTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(eachDistrictstrDeath)));
                eachDistrictDeathNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(eachDistrictDeathNew)));

                eachDistrictRecoveredTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(eachDistrictRecovered)));
                eachDistrictRecoveredNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(eachDistrictRecoveredNew)));

                //setting piechart
                eachDistrictPieChart.addPieSlice(new PieModel("Active", Integer.parseInt(eachDistrictActive), Color.parseColor("#007afe")));
                eachDistrictPieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(eachDistrictRecovered), Color.parseColor("#08a045")));
                eachDistrictPieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(eachDistrictstrDeath), Color.parseColor("#F6404F")));

                eachDistrictPieChart.startAnimation();

                activity.DismissDialog();
            } // End run
        },1000);
    } // End LoadDistrictData

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    } // End onOptionsItemSelected

    private void GetEachDistrictIntent()
    {
        Intent intent = getIntent();
        eachDistrictName = intent.getStringExtra(DISTRICT_NAME);
        eachDistrictConfirmed = intent.getStringExtra(DISTRICT_CONFIRMED);
        eachDistrictConfirmedNew = intent.getStringExtra(DISTRICT_CONFIRMED_NEW);
        eachDistrictActive = intent.getStringExtra(DISTRICT_ACTIVE);
        eachDistrictstrDeath = intent.getStringExtra(DISTRICT_DEATH);
        eachDistrictDeathNew = intent.getStringExtra(DISTRICT_DEATH_NEW);
        eachDistrictRecovered = intent.getStringExtra(DISTRICT_RECOVERED);
        eachDistrictRecoveredNew = intent.getStringExtra(DISTRICT_RECOVERED_NEW);
    } // End GetEachDistrictIntent

    private void EachDistrictInitialize()
    {
        eachDistrictConfirmedTextView = findViewById(R.id.eachDistrictConfirmedNumber);
        eachDistrictConfirmedNewTextView = findViewById(R.id.eachDistrictConfirmedNew);
        eachDistrictActiveTextView = findViewById(R.id.eachDistrictActiveNumber);
        eachDistrictActiveNewTextView = findViewById(R.id.eachDistrictActiveNew);
        eachDistrictRecoveredTextView = findViewById(R.id.eachDistrictRecoveredNumber);
        eachDistrictRecoveredNewTextView = findViewById(R.id.eachDistrictRecoveredNew);
        eachDistrictDeathTextView = findViewById(R.id.eachDistrictDeathNumber);
        eachDistrictDeathNewTextView = findViewById(R.id.eachDistrictDeathNew);
        eachDistrictPieChart = findViewById(R.id.eachDistrictPieChart);
    } // End EachDistrictInitialize



} // End DistrictDataActivity
