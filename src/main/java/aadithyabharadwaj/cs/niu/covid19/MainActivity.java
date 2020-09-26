package aadithyabharadwaj.cs.niu.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{

    // Variables
    private TextView confirmedTextView, confirmedNewTextView, activeTextView, activeNewTextView, recoveredTextView,
            recoveredNewTextView, deathTextView,deathNewTextView, testedTextView, testedNewTextView, lastUpdatedDate, lastUpdatedTime;

    private String confirmed, confirmednew, active, activenew, recovered, recoverednew, death, deathnew, tested, testednew , lastupdatetime, lastupdatedate, updatedate, updatetime;

    private int activeNewInteger;

    private LinearLayout stateDataLinearLayout, worldDataLinearLayout;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ProgressDialog progressDialog;

    private PieChart pieChart;

    private boolean doubleBackToExitPressedOnce = false;

    private Toast backPressToast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the Titlebar text
        getSupportActionBar().setTitle("COVID-19 Tracker (India)");

        // Instantiate the Variables
        Initialize();

        // Fetching the data from the API
        FetchData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                FetchData();
                swipeRefreshLayout.setRefreshing(false);
                //Toast.makeText(MainActivity.this, "Data refreshed!", Toast.LENGTH_SHORT).show();
            } // End onRefresh
        });

        stateDataLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), IndividualStateData.class);
                startActivity(intent);
            } // End onClick
        });

        worldDataLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), WorldData.class);
                startActivity(intent);
            } // End onClick
        });


    } // End onCreate

    private void FetchData()
    {
        //show progress dialog
        ShowDialog(this);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://api.covid19india.org/data.json";
        pieChart.clearChart();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                //As the data of the json are in a nested array, so we need to define the array from which we want to fetch the data.
                JSONArray all_state_jsonArray = null;
                JSONArray testData_jsonArray = null;

                try
                {
                    all_state_jsonArray = response.getJSONArray("statewise");
                    testData_jsonArray = response.getJSONArray("tested");
                    JSONObject data_india = all_state_jsonArray.getJSONObject(0);
                    JSONObject test_data_india = testData_jsonArray.getJSONObject(testData_jsonArray.length()-1);

                    //Fetching data for India and storing it in String

                    confirmed = data_india.getString("confirmed");   //Confirmed cases in India
                    confirmednew = data_india.getString("deltaconfirmed");   //New Confirmed cases from last update time
                    active = data_india.getString("active");    //Active cases in India
                    recovered = data_india.getString("recovered");  //Total recovered cased in India
                    recoverednew = data_india.getString("deltarecovered"); //New recovered cases from last update time
                    death = data_india.getString("deaths");     //Total deaths in India
                    deathnew = data_india.getString("deltadeaths");    //New death cases from last update time
                    tested = test_data_india.getString("totalsamplestested"); //Total samples tested in India
                    testednew = test_data_india.getString("samplereportedtoday");   //New samples tested today
                    lastupdatetime = data_india.getString("lastupdatedtime"); //Last update date and time

                    Handler delayToshowProgess = new Handler();

                    delayToshowProgess.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //Setting text in the textview
                            confirmedTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(confirmed)));
                            confirmedNewTextView.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(confirmednew)));

                            activeTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(active)));

                            activeNewInteger = Integer.parseInt(confirmednew) - (Integer.parseInt(recoverednew) + Integer.parseInt(deathnew));
                            activeNewTextView.setText("+"+NumberFormat.getInstance().format(activeNewInteger));

                            recoveredTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(recovered)));
                            recoveredNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(recoverednew)));

                            deathTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(death)));
                            deathNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(deathnew)));

                            testedTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(tested)));
                            testedNewTextView.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(testednew)));

                            lastUpdatedDate.setText(FormatDate(lastupdatetime, 1));
                            lastUpdatedTime.setText(FormatDate(lastupdatetime, 2));


                            pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(active), Color.parseColor("#007afe")));
                            pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(recovered), Color.parseColor("#08a045")));
                            pieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(death), Color.parseColor("#F6404F")));

                            pieChart.startAnimation();

                            DismissDialog();

                        } // End run
                    }, 1000);


                } // End try
                catch (JSONException e)
                {
                    e.printStackTrace();
                } // End catch
            } // onResponse
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            } // End onErrorResponse
        });
        requestQueue.add(jsonObjectRequest);

    } // End FetchData

    public void ShowDialog(Context context)
    {
        //setting up progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    } // End ShowDialog

    public void DismissDialog()
    {
        progressDialog.dismiss();
    } // End DismissDialog

    public String FormatDate(String date, int testCase)
    {
        Date mDate = null;
        String dateFormat;
        try
        {
            mDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).parse(date);
            if (testCase == 0)
            {
                dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(mDate);
                return dateFormat;
            } // End if
            else if (testCase == 1)
            {
                dateFormat = new SimpleDateFormat("dd MMM yyyy").format(mDate);
                return dateFormat;
            } // End else if
            else if (testCase == 2)
            {
                dateFormat = new SimpleDateFormat("hh:mm a").format(mDate);
                return dateFormat;
            } // End else if
            else
            {
                Log.d("error", "Wrong input! Choose from 0 to 2");
                return "Error";
            } // End else
        } // End try
        catch (ParseException e)
        {
            e.printStackTrace();
            return date;
        } // End catch
    } // End FormatDate

    private void Initialize()
    {

        confirmedTextView = findViewById(R.id.confirmedNumberTV);
        confirmedNewTextView = findViewById(R.id.confirmedNewTV);
        activeTextView = findViewById(R.id.activeNumberTV);
        activeNewTextView= findViewById(R.id.activeNewTV);
        recoveredTextView = findViewById(R.id.recoveredNumberTV);
        recoveredNewTextView = findViewById(R.id.recoveredNewTV);
        deathTextView = findViewById(R.id.deathNumberTV);
        deathNewTextView = findViewById(R.id.deathNewTV);
        testedTextView = findViewById(R.id.testedNumberTV);
        testedNewTextView = findViewById(R.id.testedNewTV);
        pieChart = findViewById(R.id.covidPieChart);
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        stateDataLinearLayout = findViewById(R.id.statedata);
        worldDataLinearLayout = findViewById(R.id.worlddata);
        lastUpdatedDate = findViewById(R.id.lastUpdateDateTextView);
        lastUpdatedTime = findViewById(R.id.lastUpdateTimeTextView);

    } // End Initialize

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    } // End onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        // Using if as there is just one menu item, else I would have used switch cases

        if(item.getItemId() == R.id.menu_about)
        {
            //Toast.makeText(MainActivity.this, "About Menu Icon has been clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        } // End if
        else if(item.getItemId() == R.id.menu_help)
        {

                //Toast.makeText(MainActivity.this, "About Menu Icon has been clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
        } // End else if

        return super.onOptionsItemSelected(item);

    } // End onOptionsItemSelected

    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce)
        {
            backPressToast.cancel();
            super.onBackPressed();
            return;
        } // End if
        doubleBackToExitPressedOnce = true;
        backPressToast = Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT);
        backPressToast.show();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                doubleBackToExitPressedOnce = false;
            } // End run()
        }, 4000); // If back button is pressed twice within 4 seconds, app will close
    } // End onBackPressed

} // End Main Activity
