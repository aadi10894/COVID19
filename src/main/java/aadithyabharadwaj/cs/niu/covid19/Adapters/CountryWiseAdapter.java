package aadithyabharadwaj.cs.niu.covid19.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aadithyabharadwaj.cs.niu.covid19.EachCountryDataActivity;
import aadithyabharadwaj.cs.niu.covid19.Models.CountryWiseModel;
import aadithyabharadwaj.cs.niu.covid19.R;

import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_ACTIVE;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_CONFIRMED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_DECEASED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_FLAGURL;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_NAME;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_NEW_CONFIRMED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_NEW_DECEASED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_RECOVERED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.COUNTRY_TESTS;

public class CountryWiseAdapter extends RecyclerView.Adapter<CountryWiseAdapter.MyViewHolder>
{
    private Context mContext;
    private ArrayList<CountryWiseModel> countryWiseModelArrayList;
    private String searchText="";
    private SpannableStringBuilder spannableStringBuilder;


    public CountryWiseAdapter(Context mContext, ArrayList<CountryWiseModel> countryWiseModelArrayList)
    {
        this.mContext = mContext;
        this.countryWiseModelArrayList = countryWiseModelArrayList;
    } // End CountryWiseAdapter Constructor

    public void filterList(ArrayList<CountryWiseModel> filteredList, String text)
    {
        countryWiseModelArrayList = filteredList;
        this.searchText = text;
        notifyDataSetChanged();
    } // End filterList(ArrayList<CountryWiseModel> filteredList, String text)



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.countrywise_layout, parent, false);
        return new MyViewHolder(view);
    } // End onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {
        CountryWiseModel currentItem = countryWiseModelArrayList.get(position);
        String countryName = currentItem.getCountry();
        String countryTotal = currentItem.getConfirmed();
        String countryFlag = currentItem.getFlag();
        String countryRank = String.valueOf(position+1);
        int countryTotalInt = Integer.parseInt(countryTotal);
        Log.d("country rank", countryRank);
        holder.countryRankTextView.setText(countryRank+".");
        Log.d("country total cases int", String.valueOf(countryTotalInt));
        holder.countryTotalCasesTextView.setText(NumberFormat.getInstance().format(countryTotalInt));
        //holder.tv_countryName.setText(countryName);

        if(searchText.length()>0)
        {
            //color your text here
            int index = countryName.indexOf(searchText);
            spannableStringBuilder = new SpannableStringBuilder(countryName);
            Pattern word = Pattern.compile(searchText.toLowerCase());
            Matcher match = word.matcher(countryName.toLowerCase());
            while(match.find())
            {
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(52, 195, 235)); //specify color here
                spannableStringBuilder.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                //index = stateName.indexOf(searchText,index+1);

            } // End while
            holder.countryNameTextView.setText(spannableStringBuilder);

        } // End if
        else
        {
            holder.countryNameTextView.setText(countryName);
        } // End else

        Glide.with(mContext).load(countryFlag).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.flagImageView);
        holder.countryWiseLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountryWiseModel clickedItem = countryWiseModelArrayList.get(position);
                Intent perCountryIntent = new Intent(mContext, EachCountryDataActivity.class);

                perCountryIntent.putExtra(COUNTRY_NAME, clickedItem.getCountry());
                perCountryIntent.putExtra(COUNTRY_CONFIRMED, clickedItem.getConfirmed());
                perCountryIntent.putExtra(COUNTRY_ACTIVE, clickedItem.getActive());
                perCountryIntent.putExtra(COUNTRY_RECOVERED, clickedItem.getRecovered());
                perCountryIntent.putExtra(COUNTRY_DECEASED, clickedItem.getDeceased());
                perCountryIntent.putExtra(COUNTRY_NEW_CONFIRMED, clickedItem.getNewConfirmed());
                perCountryIntent.putExtra(COUNTRY_NEW_DECEASED, clickedItem.getNewDeceased());
                perCountryIntent.putExtra(COUNTRY_TESTS, clickedItem.getTests());
                perCountryIntent.putExtra(COUNTRY_FLAGURL, clickedItem.getFlag());

                mContext.startActivity(perCountryIntent);
            }
        });

    } // End onBindViewHolder

    @Override
    public int getItemCount()
    {
        return countryWiseModelArrayList==null || countryWiseModelArrayList.isEmpty() ? 0 : countryWiseModelArrayList.size();
    } // End getItemCount

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView countryNameTextView, countryTotalCasesTextView, countryRankTextView;
        ImageView flagImageView;
        LinearLayout countryWiseLinearLayout;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            countryNameTextView = itemView.findViewById(R.id.countryWiseCountryNameTextView);
            countryTotalCasesTextView = itemView.findViewById(R.id.countryWiseConfirmedTextview);
            flagImageView = itemView.findViewById(R.id.countryWiseFlagImageView);
            countryRankTextView = itemView.findViewById(R.id.countryWiseCountryRank);
            countryWiseLinearLayout = itemView.findViewById(R.id.countryWiseLinearLayout);
        } // End MyViewHolder
    } // End MyViewHolder extends RecyclerView.ViewHolder
} // End CountryWiseAdapter Class