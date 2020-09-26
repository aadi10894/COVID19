package aadithyabharadwaj.cs.niu.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;

import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_ACTIVE;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_CONFIRMED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_CONFIRMED_NEW;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_NAME;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_DEATH;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_DEATH_NEW;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_RECOVERED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_RECOVERED_NEW;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_LAST_UPDATE;

public class EachStateDataActivity extends AppCompatActivity
{

    // Variables
    private TextView eachStateConfirmedTextView, eachStateConfirmedNewTextView, eachStateActiveTextView, eachStateActiveNewTextView,
                     eachStateDeathTextView, eachStateDeathNewTextView, eachStateRecoveredTextView, eachStateRecoveredNewTextView,
                     eachStateLastUpdateDateTextView, eachStateDistrictTextView;

    private String eachStateStateName, eachStateConfirmed, eachStateConfirmedNew, eachStateActive, eachStateActiveNew,
                   eachStateDeath, eachStateDeathNew,eachStateRecovered, eachStateRecoveredNew, eachStateLastUpdateDate;

    private PieChart eachStatePieChart;

    private LinearLayout eachStateDistrictLinearLayout;

    private int eachStateActiveNewInteger = 0;

    private MainActivity activity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_state_data);
        //Fetching data which is passed from previous activity into this activity
        GetIntent();

        //Setting up the title of actionbar as State name
        getSupportActionBar().setTitle(eachStateStateName);

        //back menu icon on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initialise all textviews
        eachStateInitialize();

        LoadStateData();

        eachStateDistrictLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(EachStateDataActivity.this, "Select District of "+eachStateStateName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EachStateDataActivity.this, IndividualDistrictData.class);
                intent.putExtra(STATE_NAME, eachStateStateName);
                startActivity(intent);
            } // End onClick
        });


    } // End onCreate

    private void LoadStateData()
    {

        //Show dialog
        activity.ShowDialog(this);
        Handler postDelayToshowProgress = new Handler();
        postDelayToshowProgress.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                eachStateConfirmedTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(eachStateConfirmed)));
                eachStateConfirmedNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(eachStateConfirmedNew)));

                eachStateActiveTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(eachStateActive)));
                eachStateActiveNewInteger = Integer.parseInt(eachStateConfirmedNew)
                        - (Integer.parseInt(eachStateRecoveredNew) + Integer.parseInt(eachStateDeathNew));
                eachStateActiveNewTextView.setText("+"+NumberFormat.getInstance().format(eachStateActiveNewInteger<0 ? 0 : eachStateActiveNewInteger));

                eachStateDeathTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(eachStateDeath)));
                eachStateDeathNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(eachStateDeathNew)));

                eachStateRecoveredTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(eachStateRecovered)));
                eachStateRecoveredNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(eachStateRecoveredNew)));

                String formatDate = activity.FormatDate(eachStateLastUpdateDate, 0);
                eachStateLastUpdateDateTextView.setText(formatDate);

                eachStateDistrictTextView.setText("District data of "+eachStateStateName);

                //setting piechart
                eachStatePieChart.addPieSlice(new PieModel("Active", Integer.parseInt(eachStateActive), Color.parseColor("#007afe")));
                eachStatePieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(eachStateRecovered), Color.parseColor("#08a045")));
                eachStatePieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(eachStateDeath), Color.parseColor("#F6404F")));

                eachStatePieChart.startAnimation();

                activity.DismissDialog();
            } // End run
        },1000);


    } // End LoadStateData


    // For clicking Back Button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    } // End onOptionsItemSelected


    private void eachStateInitialize()
    {
        eachStateConfirmedTextView = findViewById(R.id.eachStateConfirmedNumber);
        eachStateConfirmedNewTextView = findViewById(R.id.eachStateConfirmedNew);
        eachStateActiveTextView = findViewById(R.id.eachStateActiveNumber);
        eachStateActiveNewTextView = findViewById(R.id.eachStateActiveNew);
        eachStateRecoveredTextView = findViewById(R.id.eachStateRecoveredNumber);
        eachStateRecoveredNewTextView = findViewById(R.id.eachStateRecoveredNew);
        eachStateDeathTextView = findViewById(R.id.eachStateDeathNumber);
        eachStateDeathNewTextView = findViewById(R.id.eachStateDeathNew);
        eachStateLastUpdateDateTextView = findViewById(R.id.eachStateLastUpdateNumber);
        eachStateDistrictTextView = findViewById(R.id.eachStateDistrictTitle);
        eachStatePieChart = findViewById(R.id.eachStatePieChart);
        eachStateDistrictLinearLayout = findViewById(R.id.eachStateDistrictLinearLayout);
    } // End eachStateInitialize


    private void GetIntent()
    {
        Intent intent = getIntent();
        eachStateStateName = intent.getStringExtra(STATE_NAME);
        eachStateConfirmed = intent.getStringExtra(STATE_CONFIRMED);
        eachStateConfirmedNew = intent.getStringExtra(STATE_CONFIRMED_NEW);
        eachStateActive = intent.getStringExtra(STATE_ACTIVE);
        eachStateDeath = intent.getStringExtra(STATE_DEATH);
        eachStateDeathNew = intent.getStringExtra(STATE_DEATH_NEW);
        eachStateRecovered = intent.getStringExtra(STATE_RECOVERED);
        eachStateRecoveredNew = intent.getStringExtra(STATE_RECOVERED_NEW);
        eachStateLastUpdateDate = intent.getStringExtra(STATE_LAST_UPDATE);
    } // End GetIntent
} // End EachStateDataActivity
