package aadithyabharadwaj.cs.niu.covid19.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aadithyabharadwaj.cs.niu.covid19.EachDistrictDataActivity;
import aadithyabharadwaj.cs.niu.covid19.Models.DistrictWiseModel;
import aadithyabharadwaj.cs.niu.covid19.R;

import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_ACTIVE;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_CONFIRMED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_CONFIRMED_NEW;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_DEATH;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_DEATH_NEW;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_NAME;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_RECOVERED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.DISTRICT_RECOVERED_NEW;

public class DistrictWiseAdapter extends RecyclerView.Adapter<DistrictWiseAdapter.MyViewHolder>
{

    private Context mContext;
    private ArrayList<DistrictWiseModel> districtWiseModelArrayList;

    private String searchText="";
    private SpannableStringBuilder spannableStringBuilder;

    public DistrictWiseAdapter(Context mContext, ArrayList<DistrictWiseModel> districtWiseModelArrayList)
    {
        this.mContext = mContext;
        this.districtWiseModelArrayList = districtWiseModelArrayList;
    } // End DistrictWiseAdapter

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.statewise_layout, parent, false);
        return new MyViewHolder(view);
    } // End onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {
        DistrictWiseModel currentItem = districtWiseModelArrayList.get(position);
        String districtName = currentItem.getDistrict();
        String districtTotal = currentItem.getConfirmed();
        holder.districtTotalCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(districtTotal)));
        //holder.tv_districtName.setText(districtName);
        if(searchText.length()>0)
        {
            //color your text here
            int index = districtName.indexOf(searchText);
            spannableStringBuilder = new SpannableStringBuilder(districtName);
            Pattern word = Pattern.compile(searchText.toLowerCase());
            Matcher match = word.matcher(districtName.toLowerCase());
            while(match.find())
            {
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(52, 195, 235)); //specify color here
                spannableStringBuilder.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                //index = stateName.indexOf(searchText,index+1);
            } // End While
            holder.districtNameTextView.setText(spannableStringBuilder);

        } // End if
        else
        {
            holder.districtNameTextView.setText(districtName);
        } // End else

        holder.districtWiseLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DistrictWiseModel clickedItem = districtWiseModelArrayList.get(position);
                Intent perDistrictIntent = new Intent(mContext, EachDistrictDataActivity.class);
                perDistrictIntent.putExtra(DISTRICT_NAME, clickedItem.getDistrict());
                perDistrictIntent.putExtra(DISTRICT_CONFIRMED, clickedItem.getConfirmed());
                perDistrictIntent.putExtra(DISTRICT_CONFIRMED_NEW, clickedItem.getNewConfirmed());
                perDistrictIntent.putExtra(DISTRICT_ACTIVE, clickedItem.getActive());
                perDistrictIntent.putExtra(DISTRICT_DEATH, clickedItem.getDeceased());
                perDistrictIntent.putExtra(DISTRICT_DEATH_NEW, clickedItem.getNewDeceased());
                perDistrictIntent.putExtra(DISTRICT_RECOVERED, clickedItem.getRecovered());
                perDistrictIntent.putExtra(DISTRICT_RECOVERED_NEW, clickedItem.getNewRecovered());
                mContext.startActivity(perDistrictIntent);
            } // End onClick
        });


    } // End onBindViewHolder

    @Override
    public int getItemCount()
    {
        return districtWiseModelArrayList==null ? 0 : districtWiseModelArrayList.size();
    } // End getItemCount

    public void filterList(ArrayList<DistrictWiseModel> filteredList, String search)
    {
        districtWiseModelArrayList = filteredList;
        this.searchText = search;
        notifyDataSetChanged();
    } // End filterList(ArrayList<DistrictWiseModel> filteredList, String search)

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView districtNameTextView, districtTotalCasesTextView;
        LinearLayout districtWiseLinearLayout;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            districtNameTextView = itemView.findViewById(R.id.statewiseLayoutNameTextView);
            districtTotalCasesTextView = itemView.findViewById(R.id.statewiseLayoutConfirmedTextView);
            districtWiseLinearLayout = itemView.findViewById(R.id.statewiseLinearLayout);
        } // End MyViewHolder constructor
    } // End MyViewHolder extends RecyclerView.ViewHolder

} // End DistrictWiseAdapter Class
